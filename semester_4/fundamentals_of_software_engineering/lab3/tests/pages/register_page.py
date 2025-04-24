from typing import Union, Optional, Tuple

from selenium import webdriver
from selenium.webdriver.common.by import By

from .base_page import BasePage
from ..utils.generate import generate_random_login, generate_random_password


class RegisterPage(BasePage):
    LOGIN_INPUT = (By.XPATH, "//input[@id='login']")
    PASSWORD_INPUT = (By.XPATH, "//input[@id='password']")
    CREATE_ACCOUNT_BUTTON = (By.XPATH, "//button[normalize-space()='Создать']")
    HAVE_ACCOUNT_BUTTON = (By.XPATH, "//button[normalize-space()='Есть аккаунт']")

    def __init__(self, driver: Union[webdriver.Firefox, webdriver.Chrome]):
        super().__init__(driver)

    def create_account(self, login: Optional[str], password: Optional[str]) -> Tuple[str, str]:
        if login is None:
            login = generate_random_login(7)

        if password is None:
            password = generate_random_password(7)

        self.send_keys_to_input(self.LOGIN_INPUT, login)
        self.send_keys_to_input(self.PASSWORD_INPUT, password)
        self.click_element(self.CREATE_ACCOUNT_BUTTON)

        return login, password
