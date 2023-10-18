
# pyuic5 competitorstat.ui -o ui_competitorstat.py

from PyQt5.QtWidgets import QWidget, QDialog
from views.pricing.components.ui_competitorstat import Ui_Widget
from PyQt5.QtCore import Qt
from views.pricing.components.baseui import BaseUi


class CompetitorStat(QWidget, Ui_Widget, BaseUi):

    def __init__(self, parent=None):
        super(CompetitorStat, self).__init__(parent)
        self.setupUi(self)
        self.setFixedSize(self.size())
        self.setWindowTitle("Seller Overview")
        self.sellerBoard.setColumnWidth(0, 150)
        self.sellerBoard.setColumnWidth(1, 90)
        self.sellerBoard.setColumnWidth(2, 60)
        self.sellerBoard.setColumnWidth(3, 60)
        self.sellerBoard.setColumnWidth(4, 80)
        self.sellerBoard.setColumnWidth(5, 80)
        self.sellerBoard.setColumnWidth(6, 60)

    def appendRow(self, row:tuple, **kwargs):
        iRow = self.sellerBoard.rowCount()
        self.sellerBoard.setRowCount(iRow+1)
        cell = self.createCell(self.sellerBoard, iRow, 0, row[0], **kwargs)  # Seller
        cell.setTextAlignment(Qt.AlignLeft)
        cell = self.createCell(self.sellerBoard, iRow, 1, row[1], **kwargs)  # Label
        cell.setTextAlignment(Qt.AlignHCenter)
        cell = self.createCell(self.sellerBoard, iRow, 2, f"{row[2]: .2f}", **kwargs)  # U.Price
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.sellerBoard, iRow, 3, f"{row[3]: .2f}", **kwargs)  # T.Price
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.sellerBoard, iRow, 4, f"{row[4]: .2f}", **kwargs)  # Shipment
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.sellerBoard, iRow, 5, f"{row[5]: .2f}", **kwargs)  # Shipment
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.sellerBoard, iRow, 6, f"{row[6]: d}", **kwargs)  # Qty
        cell.setTextAlignment(Qt.AlignHCenter)

    def setProductName(self, name):
        self.productLabel.setText(name)

class CompetitorStatLogic(CompetitorStat):

    def __init__(self, parent=None):
        super(CompetitorStatLogic, self).__init__(parent)


class CompetitorStatDialog(QDialog, CompetitorStat):
    
    def __init__(self, parent=None):
        super(CompetitorStatDialog, self).__init__(parent)

