import pytest

from tests.constants import LOGIN_PAGE_URL
from tests.pages import PointsPage, LoginPage


@pytest.fixture(scope="function")
def points_page(request, driver) -> PointsPage:
    page = LoginPage(driver)
    page.open_url(LOGIN_PAGE_URL)
    return page.go_to_points_page(request.param["login"], request.param["password"])
