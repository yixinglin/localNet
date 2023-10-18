import sys
from views.pricing.components import *
from api.pricing import MetroPricing
from PyQt5.QtWidgets import QApplication

def testConfigurePricing():
    app = QApplication(sys.argv)
    w = ConfigurePricingLogic()
    # http://localhost:8088/pricing/metro/conf
    server = "http://localhost:8088"
    w.setServerURL(server)
    w.fetchConfiguration()
    w.show()
    sys.exit(app.exec())

def testPricingBoard():
    app = QApplication(sys.argv)
    server = "http://localhost:8088"
    api = MetroPricing(server)
    w = PricingBoardLogic(parent=None, api=api)
    w.show()
    sys.exit(app.exec())

def testSellerBoard():
    app = QApplication(sys.argv)
    server = "http://localhost:8088"
    w = CompetitorStatDialog()
    w.appendRow(("ajshdaoskd", "cheapest", 2, 2.3, 100, 22, 12312), bgcolor=BaseUi.Q_RED, fgcolor=BaseUi.Q_GREED)
    w.show()
    sys.exit(app.exec())

if __name__ == '__main__':
    #testSellerBoard()
    testPricingBoard()
