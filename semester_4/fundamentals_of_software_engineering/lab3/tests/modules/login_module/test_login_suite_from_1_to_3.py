import time

from tests.constants import NO_AUTH_HEADER_NAME, INCORRECT_LOGIN_DATA, POINTS_PAGE_URL, LOGIN_PAGE_URL
from tests.utils.generate import generate_random_login, generate_random_password

AK1_LOGIN, AK1_PASSWORD = generate_random_login(7), generate_random_password(7)


class TestLoginSuiteFrom1To3:
    def test_pre(self, register_page):
        register_page.create_account(AK1_LOGIN, AK1_PASSWORD)

    def test_suite_1_case_1_1(self, login_page):
        # 1) Начало на странице авторизации (В шапке страницы "Неавторизован :(")
        login_page.assert_correct_text_in_element(login_page.AUTH_NAME_HEADER, NO_AUTH_HEADER_NAME)

        # 2) Клик на кнопку "Войти" (ничего не произошло, кнопка не активна)
        login_page.click_element(login_page.LOG_IN_BUTTON)
        login_page.assert_is_element_disabled(login_page.LOG_IN_BUTTON, True)

    def test_suite_1_case_1_2(self, login_page):
        # 1) Ввести логин от АК1 (ввелся корректный логин)
        login_page.send_keys_to_input(login_page.LOGIN_INPUT, AK1_LOGIN)
        login_page.assert_correct_text_in_element(login_page.LOGIN_INPUT, AK1_LOGIN, is_input=True)

        # 2) Ввести <случайный набор из 8 чисел> в качестве пароля (ввелся <случайный набор из 8 чисел>)
        fake_password = generate_random_password(8)
        login_page.send_keys_to_input(login_page.PASSWORD_INPUT, fake_password)
        login_page.assert_correct_text_in_element(login_page.PASSWORD_INPUT, fake_password, is_input=True)

        # TODO: текст в алерте не соответствует действительности, когда исправят баг, раскомментировать проверку
        # 3) Клик на кнопку "Войти" (выскочил алерт с сообщением "Некорректный логин или пароль")
        login_page.click_element(login_page.LOG_IN_BUTTON)
        # login_page.assert_text_in_alert_message(INCORRECT_LOGIN_DATA)

    def test_suite_1_case_1_3(self, login_page):
        # 1) Ввести <случайный набор из 8 чисел> в качестве логина (ввелся <случайный набор из 8 чисел>)
        fake_login = generate_random_login(8)
        login_page.send_keys_to_input(login_page.LOGIN_INPUT, fake_login)
        login_page.assert_correct_text_in_element(login_page.LOGIN_INPUT, fake_login, is_input=True)

        # 2) Ввести пароль от АК1 (ввелся пароль от АК1)
        login_page.send_keys_to_input(login_page.PASSWORD_INPUT, AK1_PASSWORD)
        login_page.assert_correct_text_in_element(login_page.PASSWORD_INPUT, AK1_PASSWORD, is_input=True)

        # TODO: текст в алерте не соответствует действительности, когда исправят баг, раскомментировать проверку
        # 3) Клик на кнопку "Войти" (выскочил алерт с сообщением "Некорректный логин или пароль")
        login_page.click_element(login_page.LOG_IN_BUTTON)
        # login_page.assert_text_in_alert_message(INCORRECT_LOGIN_DATA)

    def test_suite_2_case_1_1_and_suite_3_case_1_1(self, login_page):
        # 1) Ввести логин от АК1 (ввелся логин от АК1)
        login_page.send_keys_to_input(login_page.LOGIN_INPUT, AK1_LOGIN)
        login_page.assert_correct_text_in_element(login_page.LOGIN_INPUT, AK1_LOGIN, is_input=True)

        # 2) Ввести пароль от АК1 (ввелся пароль от АК1)
        login_page.send_keys_to_input(login_page.PASSWORD_INPUT, AK1_PASSWORD)
        login_page.assert_correct_text_in_element(login_page.PASSWORD_INPUT, AK1_PASSWORD, is_input=True)

        # 3) Клик на кнопку "Войти" (мы вошли в аккаунт АК1, в хедере появился логин авторизованного пользователя)
        login_page.click_element(login_page.LOG_IN_BUTTON)
        time.sleep(5)
        login_page.assert_correct_text_in_element(login_page.AUTH_NAME_HEADER, AK1_LOGIN)
        login_page.assert_current_url(POINTS_PAGE_URL)

        # *Продолжаем в том же окне*

        # 1) Клик по логину в хедере (мы попали на страницу авторизации, в шапке страницы "Неавторизован(")
        login_page.click_element(login_page.AUTH_NAME_HEADER)
        time.sleep(5)
        login_page.assert_correct_text_in_element(login_page.AUTH_NAME_HEADER, NO_AUTH_HEADER_NAME)
        login_page.assert_current_url(LOGIN_PAGE_URL)
