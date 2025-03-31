import sys

from PyQt5.QtWidgets import QApplication

from solvers import GaussSeidelMethod
from ui.widgets import MainWindow, InputWidget, OutputWidget


class Window(MainWindow):
    def __init__(self):
        super().__init__()
        
        self.input_widget = InputWidget(self.input_widget)
        self.output_widget = OutputWidget(self.output_widget)
        
        self.clear_input_data.clicked.connect(self.input_widget.clear_input)
        self.start_calculation.clicked.connect(self._process_calculation)
    
    def _process_calculation(self):
        if (data := self.input_widget.get_input()) is None:
            return 
        
        self.output_widget.set_result(GaussSeidelMethod.solve(*data)) 
        

if __name__ == "__main__":
    app = QApplication(sys.argv)
    ex = Window()
    ex.show()
    sys.exit(app.exec())
    