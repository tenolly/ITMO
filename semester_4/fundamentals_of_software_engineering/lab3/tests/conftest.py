import pytest
from selenium import webdriver


HEADLESS = False
WEBDRIVER_IMPLICITLY_WAIT_TIME = 15


pytest_plugins = [
    "tests.fixtures.go_to_register_page",
    "tests.fixtures.go_to_login_page",
    "tests.fixtures.go_to_points_page"
]


@pytest.fixture(scope="function")
def driver(browser="firefox"):
    match browser: 
        case "firefox":
            options = webdriver.FirefoxOptions()
            options.add_argument("--ignore-certificate-errors")
            options.add_argument("window-size=1920,1080")
            if HEADLESS:
                options.add_argument("--headless")
            driver = webdriver.Firefox(options=options)
            
        case "chrome":
            options = webdriver.ChromeOptions()
            options.add_argument("--ignore-certificate-errors")
            options.add_argument("window-size=1920,1080")
            if HEADLESS:
                options.add_argument("--headless")
            driver = webdriver.Chrome(options=options)
            
        case _:
            raise NotImplementedError(f"{browser} is not implemented")
    
    driver.implicitly_wait(WEBDRIVER_IMPLICITLY_WAIT_TIME)
    
    yield driver
    
    driver.quit()
