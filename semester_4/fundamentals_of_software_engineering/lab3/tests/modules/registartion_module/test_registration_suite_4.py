import time

from tests.constants import LOGIN_PAGE_URL


class TestRegistrationSuite4:
    def test_case_from_4_1(self, register_page):
        # 1) Клик на "Есть аккаунт" (открылась страница с авторизацией)
        register_page.click_element(register_page.HAVE_ACCOUNT_BUTTON)
        time.sleep(5)
        register_page.assert_current_url(LOGIN_PAGE_URL)

    def test_case_from_4_2(self, register_page):
        # 1) Клик на "Неавторизован :(" в шапке профиля (открылась страница с авторизацией)
        register_page.click_element(register_page.AUTH_NAME_HEADER)
        time.sleep(5)
        register_page.assert_current_url(LOGIN_PAGE_URL)
