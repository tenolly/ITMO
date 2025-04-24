import pytest

from tests.constants import NO_AUTH_HEADER_NAME


class TestRegistrationSuite1:
    @pytest.mark.parametrize(
        "login,password", 
        [
            ("12233", "12233"),
            ("login123", "1234"),
            ("12345678909876543211234567", "1231233"),
            ("1231233", "12345678909876543211234567")
        ]
    )
    def test_case_from_1_1_to_1_4(self, register_page, login, password):
        # 1) Начало на странице регистрации (В шапке страницы "Неавторизован :(")
        register_page.assert_correct_text_in_element(register_page.AUTH_NAME_HEADER, NO_AUTH_HEADER_NAME)
        
        # 2) Ввод логина "12233" (в поле логина ввелось "12233")
        register_page.send_keys_to_input(register_page.LOGIN_INPUT, login)
        register_page.assert_correct_text_in_element(register_page.LOGIN_INPUT, login, is_input=True)
        
        # 3) Ввод пароля "1231233" (в поле пароля ввелось "1231233")
        register_page.send_keys_to_input(register_page.PASSWORD_INPUT, password)
        register_page.assert_correct_text_in_element(register_page.PASSWORD_INPUT, password, is_input=True)
        
        # 3) Клик по "Создать" (ничего не произошло, кнопка "Создать" неактивна)
        register_page.click_element(register_page.CREATE_ACCOUNT_BUTTON)
        register_page.assert_is_element_disabled(register_page.CREATE_ACCOUNT_BUTTON, True)
