import sys
from utils import glo
from views.pricing.components.baseui import BaseUi

glo._init()
from views import *
from api.pricing import MetroPricing
from PyQt5.QtWidgets import QApplication
from utils.app_init import *

def testConfigurePricing(api):
    app = QApplication(sys.argv)
    w = ConfigurePricingLogic(api=api)
    w.setServerURL(server)
    w.fetchConfiguration()
    w.show()
    sys.exit(app.exec())

def testPricingBoard(path):
    app = QApplication(sys.argv)
    # api = MetroPricing(baseURL=server, parent=None)
    # w = PricingBoardLogic(parent=None, api=api)
    # w = MainPricingBoard(api=api)
    w = MainWindow(parent=None, conf_path=path)
    w.show()
    sys.exit(app.exec())

def testSellerBoard(api):
    app = QApplication(sys.argv)
    w = CompetitorStatDialog()
    w.appendRow(("ajshdaoskd", "cheapest", 2, 2.3, 100, 22, 12312),
                bgcolor=BaseUi.Q_RED, fgcolor=BaseUi.Q_GREED)
    w.show()
    sys.exit(app.exec())

if __name__ == '__main__':
    init_app(sys.argv[1])
    #testSellerBoard(api)
    testPricingBoard(sys.argv[1])
    #testConfigurePricing(api)
