from typing import Union

from selenium import webdriver
from selenium.webdriver.common.by import By

from .base_page import BasePage
from .register_page import RegisterPage
from .points_page import PointsPage


class LoginPage(BasePage):
    LOGIN_INPUT = (By.XPATH, "//input[@id='login']")
    PASSWORD_INPUT = (By.XPATH, "//input[@id='password']")
    LOG_IN_BUTTON = (By.XPATH, "//button[normalize-space()='Войти']")
    NO_ACCOUNT_BUTTON = (By.XPATH, "//button[normalize-space()='Нет аккаунта']")

    def __init__(self, driver: Union[webdriver.Firefox, webdriver.Chrome]):
        super().__init__(driver)

    def go_to_register_page(self) -> RegisterPage:
        self.click_element(self.NO_ACCOUNT_BUTTON)
        return RegisterPage(self._driver)

    def go_to_points_page(self, login, password) -> PointsPage:
        self.send_keys_to_input(self.LOGIN_INPUT, login)
        self.send_keys_to_input(self.PASSWORD_INPUT, password)
        self.click_element(self.LOG_IN_BUTTON)
        return PointsPage(self._driver)
