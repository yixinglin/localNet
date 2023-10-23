from views.ui_mainwindow import Ui_MainWindow
from PyQt5.QtWidgets import QMainWindow, QMessageBox
from views.pricing.mainpricingboard import MainPricingBoard
from views.pricing.components.configurepricing import ConfigurePricingLogic
import traceback
from utils.log import *

class MainWindow(QMainWindow, Ui_MainWindow):

    def __init__(self, parent=None, api=None):
        super(MainWindow, self).__init__(parent)
        self.api = api
        self.setupUi(self)
        self.setupPricingUi()
        self.setupMenu()
        self.excepthook_ = sys.excepthook
        sys.excepthook = self.uncaught_exceptions

    def setApi(self, api):
        self.api = api

    def setupPricingUi(self):
        self.mainpricingboard = MainPricingBoard(parent=self, api=self.api)
        geo = self.mainpricingboard.geometry()
        geo.setY(20)
        self.mainpricingboard.setGeometry(geo)

    def setupMenu(self):
        self.actionSettings.triggered.connect(self.triggeredActionSettings)

    def triggeredActionSettings(self):
        dialog = ConfigurePricingLogic(self, self.api)
        dialog.setServerURL(self.api.baseURL)
        dialog.fetchConfiguration()
        dialog.exec_()

    def uncaught_exceptions(self, exc_type, exc_value, exc_traceback):
        # filename = exc_traceback.tb_frame.f_code.co_filename
        # name = exc_traceback.tb_frame.f_code.co_name
        # line_no = exc_traceback.tb_lineno
        # print(f"{exc_type.__name__}, Message: {exc_value}")
        # print(f"File {filename} line {line_no}, in {name}")

        traceback.print_tb(exc_traceback)
        errmsgs = traceback.format_tb(exc_traceback)
        s = "".join(errmsgs)
        log_error(s.strip())
        ans = QMessageBox.critical(self, "Uncaught Errors", s)








