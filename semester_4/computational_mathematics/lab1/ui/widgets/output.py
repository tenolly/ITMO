from typing import Any

from PyQt5.QtWidgets import QWidget

from ._generated.output import Ui_Form


class OutputWidget(QWidget, Ui_Form):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setupUi(parent)
        
    def set_result(self, value: Any):
        if isinstance(value, str):
            self.output_text.setPlainText(value)
        else:
            answer_vector, norma, iteration_number, e_vector = value
            self.output_text.setPlainText(
                f"Ответ: {answer_vector}\n"
                + f"Норма: {norma}\n" 
                + f"Количество итераций: {iteration_number}\n"
                + f"Вектор погрешностей: {e_vector}"
            )
