import time
from typing import Union

from selenium import webdriver
from selenium.webdriver import ActionChains
from selenium.webdriver.common.by import By

from .base_page import BasePage


class PointsPage(BasePage):
    CHOOSE_X_VALUE = (By.XPATH, "(//div[@class='row'])[2]//input[@value='{}']")
    INPUT_Y_VALUE = (By.XPATH, "(//div[@class='row'])[3]//input")
    CHOOSE_R_VALUE = (By.XPATH, "(//div[@class='row'])[4]//input[@value='{}']")
    CHECK_BUTTON = (By.XPATH, "//button[normalize-space()='Проверить']")
    GRAPH = (By.XPATH, "//div[@id='graphContainer']")
    TABLE_ROW_BY_NUMBER = (By.XPATH, "(//div[@class='result-container'])[{}]")
    TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER = (By.XPATH, "(//div[@class='result-container'])[{}]/div[{}]")

    def __init__(self, driver: Union[webdriver.Firefox, webdriver.Chrome]):
        super().__init__(driver)

    def click_graph(self, x: int, y: int):
        time.sleep(0.5)
        ActionChains(self._driver).move_to_element_with_offset(self._find_element(*self.GRAPH), x, -y).click().perform()
        time.sleep(0.5)
    