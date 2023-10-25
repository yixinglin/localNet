from views.pricing.components.ui_edititemdialog import Ui_Dialog
from PyQt5.QtWidgets import QDialog, QMessageBox, QComboBox
from PyQt5.QtCore import Qt
from utils.log import *

class EditItemDialog(QDialog, Ui_Dialog):

    def __init__(self, parent=None):
        super(EditItemDialog, self).__init__(parent)
        self.setupUi(self)
        self.strategy.addItem("TotalPriceStrategy")
        self.strategy.addItem("UnitPriceStrategy")

    def setData(self, conf):
        self.productName.setText(conf['productName'])
        self.productId.setText(conf['productId'])
        self.note.setText(conf['offerNote'])
        self.lowestPrice.setValue(conf['lowestPrice'])
        self.reduce.setValue(conf['reduce'])
        self.amount.setValue(conf['offerAmount'])
        self.maxAdjust.setValue(conf['maxAdjust'])
        self.enabled.setCheckState(Qt.Checked if conf['enabled'] else Qt.Unchecked)
        self.strategy.setCurrentText(conf['strategyId'])

    def getData(self):
        conf = {}
        conf['productId'] = self.productId.text().strip()
        conf['productName'] = self.productName.text().strip()
        conf['lowestPrice'] = self.lowestPrice.value()
        conf['offerNote'] = self.note.text().strip()
        conf['offerAmount'] = self.amount.value()
        conf['enabled'] = False if self.enabled.checkState() == Qt.Unchecked else True
        conf['strategyId'] = self.strategy.currentText()
        conf['maxAdjust'] = self.maxAdjust.value()
        conf['reduce'] = self.reduce.value()
        return conf


class EditItemDialogLogic(EditItemDialog):

    def __init__(self, parent=None, api=None):
        super(EditItemDialogLogic, self).__init__(parent)
        self.api = api

    def exec_(self) -> int:
        reply = super(EditItemDialogLogic, self).exec_()
        if reply == 1:
            conf = self.getData()
            self.api.updateConfiguration([conf])
            log_stdout(conf)
            log_info(conf)
            return conf
        else:
            return None





