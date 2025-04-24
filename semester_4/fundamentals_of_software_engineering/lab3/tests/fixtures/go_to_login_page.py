import pytest
from tests.pages import LoginPage
from tests.constants import LOGIN_PAGE_URL


@pytest.fixture(scope="function")
def login_page(driver) -> LoginPage:
    page = LoginPage(driver)
    page.open_url(LOGIN_PAGE_URL)
    return page
