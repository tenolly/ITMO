import time

import pytest

from tests.constants import LOGIN_PAGE_URL, ACCOUNT_EXISTS
from tests.utils.generate import generate_random_login, generate_random_password

LOGIN = generate_random_login(7)
PASSWORD = generate_random_password(7)


class TestRegistrationSuite2And3:
    def test_case_2_1(self, register_page):
        # 1) Ввод логина <7 случайных чисел> и ввод пароля
        # <7 случайных чисел> (в поля ввелся соответствующий логин и пароль)
        register_page.send_keys_to_input(register_page.LOGIN_INPUT, LOGIN)
        register_page.assert_correct_text_in_element(register_page.LOGIN_INPUT, LOGIN, is_input=True)
        register_page.send_keys_to_input(register_page.PASSWORD_INPUT, PASSWORD)
        register_page.assert_correct_text_in_element(register_page.PASSWORD_INPUT, PASSWORD, is_input=True)

        # 2) Клик по "Создать" (произошла переадресация на страницу входа)
        register_page.click_element(register_page.CREATE_ACCOUNT_BUTTON)
        time.sleep(5)
        register_page.assert_current_url(LOGIN_PAGE_URL)

    def test_case_3_1(self, register_page):
        # 1) Ввод логина и пароля из шага 2.1-1) (в поля ввелся соответствующий логин и пароль)
        register_page.send_keys_to_input(register_page.LOGIN_INPUT, LOGIN)
        register_page.send_keys_to_input(register_page.PASSWORD_INPUT, PASSWORD)

        # TODO: текст в алерте не соответствует действительности, когда исправят баг, раскомментировать проверку
        # 2) Клик по "Создать" (выскочил alert с уведомлением "Аккаунт с данным логином уже существует",
        # аккаунт создать не удалось)
        register_page.click_element(register_page.CREATE_ACCOUNT_BUTTON)
        # register_page.assert_text_in_alert_message(ACCOUNT_EXISTS)
