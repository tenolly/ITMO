import time
from typing import Union, Tuple

from selenium import webdriver
from selenium.webdriver import ActionChains
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webelement import WebElement
from selenium.common import TimeoutException, ElementNotInteractableException, StaleElementReferenceException
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait


TIMEOUT = 15


class BasePage:
    AUTH_NAME_HEADER = (By.XPATH, "//header//button")

    def __init__(self, driver: Union[webdriver.Firefox, webdriver.Chrome]):
        self._driver = driver
    
    def open_url(self, url: str) -> None:
        self._driver.get(url)
        
    def assert_current_url(self, url: str) -> None:
        current_url = self._driver.current_url
        assert self._driver.current_url == url, f"Текущий URL {current_url} не совпадает с ожидаемым {url}"

    def click_element(self, web_element: Tuple[str, str], double_click=False, timeout=TIMEOUT):
        element = self._find_element(*web_element)

        try:
            time.sleep(0.5)
            chain = ActionChains(self._driver).move_to_element(element)
            if double_click:
                chain = chain.double_click()
            else:
                chain = chain.click()
            chain.perform()
            time.sleep(0.5)
        except StaleElementReferenceException:
            self.click_element(web_element, double_click, timeout=timeout)
    
    def send_keys_to_input(self, web_element: Tuple[str, str], text: str) -> None:
        try:
            self._find_element(*web_element).send_keys(text)
        except ElementNotInteractableException:
            raise AssertionError(f"В элемент {web_element[1]} нельзя ввести значение '{text}'")
    
    def assert_correct_text_in_element(self, web_element: Tuple[str, str], text: str, is_input: bool = False) -> None:
        text = text.strip()
        
        if is_input:
            element_text = self._get_attribute_value(web_element, "value").strip()
        else:
            element_text = self._get_element_text(*web_element).strip()
            
        assert element_text == text, f"В элементе {web_element[1]} текст '{element_text}', а не '{text}'"

    def assert_is_element_disabled(self, web_element: Tuple[str, str], is_disabled: bool):
        result = str(self._get_attribute_value(web_element,"disabled"))
        if is_disabled:
            assert result == "true", f"Элемент активен, значение аттрибута disabled: {result}"
        else:
            assert result == "None", f"Элемент неактивен, значение аттрибута disabled: {result}"

    def assert_text_in_alert_message(self, text: str, timeout=TIMEOUT):
        alert = WebDriverWait(self._driver, timeout).until(expected_conditions.alert_is_present())
        alert_text = alert.text
        alert.accept()

        assert alert_text == text, f"Текст в alert ({repr(alert_text)}) не совпадает с ожидаемым ({repr(text)})"

    def assert_element_visibility(self, web_element: Tuple[str, str], is_visible: bool, timeout=TIMEOUT):
        if is_visible:
            self._is_element_visible(*web_element, timeout=timeout)
        else:
            self._is_element_not_visible(*web_element, timeout=timeout)

    def _get_attribute_value(self, web_element: Tuple[str, str], attribute: str, timeout=TIMEOUT):
        return self._get_element(*web_element, timeout=timeout).get_attribute(attribute)

    def _get_element_text(self, strategy: str, path: str, timeout=TIMEOUT):
        return self._get_element(strategy, path, timeout=timeout).text

    def _get_element(self, strategy: str, path: str, timeout=TIMEOUT):
        return self._find_element(strategy, path, timeout=timeout)

    def _is_element_visible(self, strategy: str, path: str, timeout=TIMEOUT):
        return self._find_element(strategy, path, timeout).is_displayed()

    def _is_element_not_visible(self, strategy: str, path: str, timeout=TIMEOUT):
        try:
            return WebDriverWait(self._driver, timeout).until(
                expected_conditions.invisibility_of_element_located(
                    (strategy, path),
                ),
            )
        except TimeoutException:
            raise AssertionError(f"Элемент {path} найден")

    def _find_element(self, strategy: str, path: str, timeout=TIMEOUT) -> WebElement:
        try:
            element = WebDriverWait(self._driver, timeout).until(
                expected_conditions.
                visibility_of_element_located((strategy, path)),
            )
            return element
        except TimeoutException:
            raise AssertionError(f"Попытка найти элемент {path} провалилась")
