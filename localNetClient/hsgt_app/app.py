import sys
from views.pricing.components.configurepricing import ConfigurePricingLogic

from PyQt5.QtWidgets import QApplication

if __name__ == '__main__':
    app = QApplication(sys.argv)
    w = ConfigurePricingLogic()
    # http://localhost:8088/pricing/metro/conf
    server = "http://localhost:8088"
    w.setServerURL(server)
    w.fetchConfiguration()
    w.show()
    sys.exit(app.exec())
