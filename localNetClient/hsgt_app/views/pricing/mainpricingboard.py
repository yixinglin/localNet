from PyQt5 import QtGui

from views.pricing.ui_mainpricingboard import Ui_Form
from views.pricing.pricingboard import PricingBoardLogic
from PyQt5.QtWidgets import QWidget

class MainPricingBoard(QWidget, Ui_Form):

    def __init__(self, parent=None, api=None, **kwargs):
        super(MainPricingBoard, self).__init__(parent)
        self.setupUi(self)
        self.setWindowTitle("Pricing Board")
        self.metroPricingWidget = PricingBoardLogic(parent=self.metroTab, api=api, **kwargs)

    def destroy(self, destroyWindow: bool = ..., destroySubWindows: bool = ...) -> None:
        super(MainPricingBoard, self).destroy()
        self.metroPricingWidget.destroy()