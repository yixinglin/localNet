import requests.exceptions
from PyQt5 import QtGui

from api.pricing import MetroPricing
from views.ui_mainwindow import Ui_MainWindow
from PyQt5.QtWidgets import QMainWindow, QMessageBox
from views.pricing.mainpricingboard import MainPricingBoard
from views.pricing.components.configurepricing import ConfigurePricingLogic
from components.logindialog import LoginDialogLogic
import traceback
from utils.log import *
import utils.glo as glo
from utils.app_init import *

class MainWindow(QMainWindow, Ui_MainWindow):

    def __init__(self, parent=None, conf_path=None):
        super(MainWindow, self).__init__(parent)
        self.conf_path = conf_path
        self.api = None
        self.settings = None
        self.loadConfigFile()
        self.setupUi(self)
        self.setupPricingUi()
        self.setupMenu()
        self.excepthook_ = sys.excepthook
        sys.excepthook = self.uncaught_exceptions

    def loadConfigFile(self):
        conf = glo.getValue("app_settings")
        selected = conf['server']['host']['selected']
        server = conf['server']['host']['options'][selected]
        self.api = MetroPricing(baseURL=server, parent=self)
        self.settings = conf
        glo.setValue("token", "")

    def setApi(self, api):
        self.api = api

    def setupPricingUi(self):
        self.mainpricingboard = MainPricingBoard(parent=self, api=self.api)
        geo = self.mainpricingboard.geometry()
        geo.setY(20)
        self.mainpricingboard.setGeometry(geo)

    def setupMenu(self):
        self.actionSettings.triggered.connect(self.triggeredActionSettings)
        self.actionlogin.triggered.connect(self.triggerdActionLogin)

    def triggeredActionSettings(self):
        dialog = ConfigurePricingLogic(self, self.api)
        dialog.setServerURL(self.api.baseURL)
        dialog.fetchConfiguration()
        dialog.exec_()

    def triggerdActionLogin(self):
        dialog = LoginDialogLogic(self, self.api)
        dialog.exec_()

    def uncaught_exceptions(self, exc_type, exc_value, exc_traceback):
        # filename = exc_traceback.tb_frame.f_code.co_filename
        # name = exc_traceback.tb_frame.f_code.co_name
        # line_no = exc_traceback.tb_lineno
        # print(f"{exc_type.__name__}, Message: {exc_value}")
        # print(f"File {filename} line {line_no}, in {name}")
        if exc_type is requests.exceptions.ConnectionError:
            ans = QMessageBox\
                .critical(self, "Error", "Server connection error.\n"
                                         "1. You may need to check your network connection.\n"
                                         "2. Your APP may connected to a wrong server.")
        else:
            traceback.print_tb(exc_traceback)
            errmsgs = traceback.format_tb(exc_traceback)
            s = "".join(errmsgs)
            log_error(s.strip())
            ans = QMessageBox.critical(self, "Uncaught Errors", s)

    def closeEvent(self, a0: QtGui.QCloseEvent) -> None:
        save_app_settings(self.conf_path, self.settings)





