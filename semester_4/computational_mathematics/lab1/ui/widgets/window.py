import os

from PyQt5 import Qt
from PyQt5.QtWidgets import QMainWindow

from ._generated.main import Ui_MainWindow


class MainWindow(QMainWindow, Ui_MainWindow):
    def __init__(self):
        super().__init__()
        
        self.setupUi(self)
        self.setFixedSize(self.size())
        self.setWindowIcon(Qt.QIcon(os.path.join("ui", "icons", "favicon.png")))
        