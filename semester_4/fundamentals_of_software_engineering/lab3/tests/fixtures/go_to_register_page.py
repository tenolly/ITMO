import pytest
from tests.pages import RegisterPage


@pytest.fixture(scope="function")
def register_page(login_page) -> RegisterPage:
    return login_page.go_to_register_page()
