from PyQt5.QtGui import QColor
from PyQt5.QtWidgets import QTableWidgetItem, QTableWidget
from PyQt5.QtCore import Qt

class BaseUi(object):
    Q_GREED = QColor("#66FF66")
    Q_RED = QColor("#FFCCCC")
    Q_YELLOW = QColor("#FFFF99")

    def createCell(self, table:QTableWidget, r: int, c: int, val: str,
                   readOnly=True, bgcolor=None, fgcolor=None) -> QTableWidgetItem:
        cell = QTableWidgetItem(val) if isinstance(val, str) else QTableWidgetItem(str(val))
        table.setItem(r, c, cell)
        if readOnly:
            cell.setFlags(Qt.ItemIsEnabled)
        if bgcolor is not None:
            cell.setBackground(bgcolor)
        if fgcolor is not None:
            cell.setForeground(fgcolor)
        return cell