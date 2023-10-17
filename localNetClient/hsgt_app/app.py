import sys
from views.pricing.components.configurepricing import ConfigurePricingLogic
from views.pricing.components.pricingboard import PricingBoardLogic
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

if __name__ == '__main__':
    app = QApplication(sys.argv)
    server = "http://localhost:8088"
    api = MetroPricing(server)
    w = PricingBoardLogic(parent=None, api=api)
    w.show()
    sys.exit(app.exec())
