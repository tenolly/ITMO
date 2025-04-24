import pytest

from tests.constants import R_MUST_BE_GREATER_THAN_0, HIT_RESULT, UNABLE_TO_CALCULATE_HIT, MISS_RESULT
from tests.utils.generate import generate_random_login, generate_random_password

AK1_LOGIN, AK1_PASSWORD = generate_random_login(7), generate_random_password(7)


class TestPointsSuite1:
    def test_pre(self, register_page):
        register_page.create_account(AK1_LOGIN, AK1_PASSWORD)

    @pytest.mark.parametrize("points_page", [{"login": AK1_LOGIN, "password": AK1_PASSWORD}], indirect=True)
    def test_case_1_1(self, points_page):
        # 1) Начало на странице АК1 (мы вошли в АК1, таблица с точками пустая)
        points_page.assert_element_visibility(
            (
                points_page.TABLE_ROW_BY_NUMBER[0],
                points_page.TABLE_ROW_BY_NUMBER[1].format("2")
            ),
            is_visible=False
        )

        # 2) Выбрать X=0, Y=0 и нажать на "Проверить" (выскочил алерт с предупреждением "Введите корректное
        # значение для R (R должен быть больше 0)", таблица все еще пустая)
        points_page.click_element((points_page.CHOOSE_X_VALUE[0], points_page.CHOOSE_X_VALUE[1].format(0)))
        points_page.send_keys_to_input(points_page.INPUT_Y_VALUE, "0")
        points_page.click_element(points_page.CHECK_BUTTON)

        points_page.assert_text_in_alert_message(R_MUST_BE_GREATER_THAN_0)
        points_page.assert_element_visibility(
            (
                points_page.TABLE_ROW_BY_NUMBER[0],
                points_page.TABLE_ROW_BY_NUMBER[1].format(2)
            ),
            is_visible=False
        )

        # 3) Выбрать R = 1 и нажать на "Проверить" (в таблице появилась строчка с результатами, в результате "Да")
        points_page.click_element((points_page.CHOOSE_R_VALUE[0], points_page.CHOOSE_R_VALUE[1].format(1)))
        points_page.click_element(points_page.CHECK_BUTTON)

        points_page.assert_element_visibility(
            (
                points_page.TABLE_ROW_BY_NUMBER[0],
                points_page.TABLE_ROW_BY_NUMBER[1].format(2)
            ),
            is_visible=True
        )
        points_page.assert_correct_text_in_element(
            (
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[0],
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[1].format(2, 4)
            ),
            HIT_RESULT
        )

    @pytest.mark.parametrize("points_page", [{"login": AK1_LOGIN, "password": AK1_PASSWORD}], indirect=True)
    def test_case_1_2(self, points_page):
        # 1) Начало на странице АК1 (мы вошли в АК1, в таблице с точками есть результат из предыдущего кейса)
        points_page.assert_element_visibility(
            (
                points_page.TABLE_ROW_BY_NUMBER[0],
                points_page.TABLE_ROW_BY_NUMBER[1].format(2)
            ),
            is_visible=True
        )
        points_page.assert_correct_text_in_element(
            (
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[0],
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[1].format(2, 4)
            ),
            HIT_RESULT
        )

        # 2) Клик по центру графика (выскочил алерт с надписью "Невозможно вычислить попадание")
        points_page.click_graph(0, 0)
        points_page.assert_text_in_alert_message(UNABLE_TO_CALCULATE_HIT)

        # 3) Выбрать R=1, клик по левому верхнему углу графика
        # (в таблице появилась строчка с результатами, в результате "Нет")
        points_page.click_element((points_page.CHOOSE_R_VALUE[0], points_page.CHOOSE_R_VALUE[1].format(1)))
        points_page.click_graph(-50, 50)

        points_page.assert_element_visibility(
            (
                points_page.TABLE_ROW_BY_NUMBER[0],
                points_page.TABLE_ROW_BY_NUMBER[1].format(2)
            ),
            is_visible=True
        )
        points_page.assert_correct_text_in_element(
            (
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[0],
                points_page.TABLE_CELL_BY_ROW_NUMBER_AND_CELL_NUMBER[1].format(2, 4)
            ),
            MISS_RESULT
        )
