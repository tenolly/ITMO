from typing import List, Optional, Tuple

from PyQt5 import QtCore
from PyQt5.QtWidgets import QWidget, QMessageBox, QLineEdit, QFileDialog

from ._generated.input import Ui_Form


class InputWidget(QWidget, Ui_Form):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setupUi(parent)
        
        self.matrix: List[List[QLineEdit]] = []
        
        self._draw_matrix()
        self.slider.valueChanged.connect(self._draw_matrix)
        self.slider.valueChanged.connect(self._update_slider_field)
        self.upload_from_file.clicked.connect(self._load_from_file)
    
    def get_input(self) -> Optional[Tuple[float, List[List[float]]]]:
        if self.accuracy_line.text().replace(",", ".").replace(".", "", 1).isdigit():
            accuracy = float(self.accuracy_line.text().replace(",", "."))
        else:
            self._throw_critical_modal_window(
                "Некорректное значение", 
                "Точность должна быть целым или вещественным числом."
            )
            return
        
        values = []
        for i, matrix_row in enumerate(self.matrix):
            values_row = []
            
            for j, cell in enumerate(matrix_row):
                if cell.text().replace(",", ".").replace(".", "", 1).isdigit():
                    values_row.append(float(cell.text().replace(",", ".")))
                else:
                    self._throw_critical_modal_window(
                        "Некорректное значение", 
                        f"{repr(cell.text())} должно быть числом ({i + 1} строка, {j + 1} столбец)"
                    )
                    return
                
            values.append(values_row)
        
        return accuracy, values
    
    def clear_input(self) -> None:
        for row in self.matrix:
            for cell in row:
                cell.clear()
    
    def _load_from_file(self):
        filename = QFileDialog.getOpenFileName(self, "Выберите файл с матрицей", "")[0]
        if filename.isspace() or filename == "":
            return
        
        try:
            values_matrix = []
            
            lines = open(filename, mode="r", encoding="utf-8").readlines()
            for i, line in enumerate(lines):
                row = [float(cell) for cell in line.split()]
                if len(lines) + 1 == len(row):
                    values_matrix.append(row)
                else:
                    self._throw_critical_modal_window(
                        "Ошибка во время прочтения файла", 
                        f"Нарушен размер матрицы ({i + 1} строка)"
                    )
                    return
                
        except Exception as e:
            self._throw_critical_modal_window(
                "Ошибка во время прочтения файла", 
                f"{type(e).__name__}: {e}"
            )
            return
        
        self.slider.setValue(len(lines))
        self._update_slider_field()
        self._draw_matrix()
        
        for values_row, matrix_row in zip(values_matrix, self.matrix):
            for cell_value, cell in zip(values_row, matrix_row):
                if cell_value % 1 == 0:
                    cell_value = int(cell_value)
                    
                cell.setText(str(cell_value)) 
        
    def _update_slider_field(self) -> None:
        self.slider_value.setText(str(self.slider.value()))
        
    def _draw_matrix(self) -> None:
        matrix_size = self.slider.value()
        
        size = self.matrix_box.size()
        height, width = size.height(), size.width()
        
        cell_size = int(
            min(
                height * 0.85 // matrix_size, 
                width * 0.85 // (matrix_size + 1)
            )
        )
        matrix_aligment_between_cells = int(
            min(
                height * 0.1 // matrix_size, 
                width * 0.1 // (matrix_size + 1)
            )
        )
        matrix_vertical_line_size = (cell_size + matrix_aligment_between_cells) * matrix_size - matrix_aligment_between_cells
        matrix_horizontal_line_size = (cell_size + matrix_aligment_between_cells) * (matrix_size + 1) - matrix_aligment_between_cells
        matrix_vertical_aligment = int((height - matrix_vertical_line_size) // 2)
        matrix_horizontal_aligment = int((width - matrix_horizontal_line_size) // 2)
        font_size = max(12, min(cell_size // 3, 40))
        
        self._reset_matrix()
        for row_index in range(matrix_size):
            row = []
            
            for column_index in range(matrix_size + 1):
                cell = QLineEdit(self.matrix_box)
                cell.setGeometry(
                    QtCore.QRect(
                        matrix_horizontal_aligment + column_index * (cell_size + matrix_aligment_between_cells), 
                        matrix_vertical_aligment + row_index * (cell_size + matrix_aligment_between_cells), 
                        cell_size, 
                        cell_size
                    )
                )
                cell.setAlignment(QtCore.Qt.AlignCenter)
                cell.setStyleSheet(f"font-size: {font_size}px")
                
                cell.show()
                row.append(cell)
                
            self.matrix.append(row)
    
    def _reset_matrix(self) -> None:
        for row in self.matrix:
            for cell in row:
                cell.deleteLater()
                
        self.matrix.clear()
    
    def _throw_critical_modal_window(self, title: str, description: str):
        message = QMessageBox()
        message.setIcon(QMessageBox.Critical)
        message.setWindowTitle(title)
        message.setText(description)
        message.setStandardButtons(QMessageBox.Ok)
        message.exec_()
