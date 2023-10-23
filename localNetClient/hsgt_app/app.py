import sys
from utils import glo
glo._init()
from views import *
from api.pricing import MetroPricing
from PyQt5.QtWidgets import QApplication

def testConfigurePricing(api):
    app = QApplication(sys.argv)
    w = ConfigurePricingLogic(api=api)
    w.setServerURL(server)
    w.fetchConfiguration()
    w.show()
    sys.exit(app.exec())

def testPricingBoard(api):
    app = QApplication(sys.argv)
    # api = MetroPricing(baseURL=server, parent=None)
    # w = PricingBoardLogic(parent=None, api=api)
    # w = MainPricingBoard(api=api)
    w = MainWindow(parent=None, api=api)
    w.show()
    sys.exit(app.exec())

def testSellerBoard(api):
    app = QApplication(sys.argv)
    w = CompetitorStatDialog()
    w.appendRow(("ajshdaoskd", "cheapest", 2, 2.3, 100, 22, 12312), bgcolor=BaseUi.Q_RED, fgcolor=BaseUi.Q_GREED)
    w.show()
    sys.exit(app.exec())

if __name__ == '__main__':
    server = sys.argv[1]
    api = MetroPricing(baseURL=server, parent=None)
    #testSellerBoard(api)
    testPricingBoard(api)
    #testConfigurePricing(api)
