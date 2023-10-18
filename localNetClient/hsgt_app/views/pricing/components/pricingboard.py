from views.pricing.components.ui_pricingboard import Ui_Form
from datetime import datetime
from PyQt5.QtWidgets import QFrame
from PyQt5.QtCore import Qt, pyqtSignal, QObject, QThread
from views.pricing.components.baseui import BaseUi
from views.pricing.components.competitorstat import CompetitorStatDialog
from utils.general import *

# pyuic5 pricingboard.ui -o ui_pricingboard.py

class PricingBoard(QFrame, Ui_Form, BaseUi):

    def __init__(self, parent=None):
        super(PricingBoard, self).__init__(parent)
        self.setupUi(self)
        self.setFixedSize(self.size())
        self.pricingBoard.setColumnWidth(0, 120)
        self.pricingBoard.setColumnWidth(1, 70)
        self.pricingBoard.setColumnWidth(2, 70)
        self.pricingBoard.setColumnWidth(3, 70)
        self.pricingBoard.setColumnWidth(4, 60)
        self.pricingBoard.setColumnWidth(5, 70)
        self.pricingBoard.setColumnWidth(6, 70)
        self.pricingBoard.setColumnWidth(7, 180)

    def appendRow(self, row: tuple, **kwargs):
        iRow = self.pricingBoard.rowCount()
        self.pricingBoard.setRowCount(iRow + 1)
        self.updateRow(iRow, row, **kwargs)

    def updateRow(self, iRow, row:tuple, **kwargs):
        cellNo1 = self.createCell(self.pricingBoard, iRow, 0, row[0], **kwargs)  # No. 1
        cellNo1.setTextAlignment(Qt.AlignLeft)
        cell = self.createCell(self.pricingBoard, iRow, 1, row[1], **kwargs)  # Rank
        cell.setTextAlignment(Qt.AlignCenter)
        cell = self.createCell(self.pricingBoard, iRow, 2, f"{row[2]: .2f}", readOnly=True, **kwargs)  # Unit Price
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 3, f"{row[3]: .2f}", readOnly=False, **kwargs)  # LowestPrice
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 4, f"{row[4]: .2f}", readOnly=True, **kwargs)  # Reduce
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 5, f"{row[5]: .2f}", readOnly=True, **kwargs)  # Profit
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 6, row[6], readOnly=True, **kwargs)  # Status
        cell.setTextAlignment(Qt.AlignLeft)
        cellName = self.createCell(self.pricingBoard, iRow, 7, row[7], readOnly=True, **kwargs)  # Name
        cellName.setTextAlignment(Qt.AlignLeft)

        cellName.setFlags(Qt.ItemIsUserCheckable | Qt.ItemIsEnabled)
        cellName.setCheckState(Qt.Checked if row[8] else Qt.Unchecked)

    def setCurrentTime(self):
        now = datetime.now()
        dt_string = now.strftime("%b.%d.%Y %H:%M:%S")
        print("date and time =", dt_string)
        self.updateTime.setText(dt_string)

    def clearAllItems(self):
        rowCount = self.pricingBoard.rowCount()
        self.pricingBoard.model().removeRows(0, rowCount)

    def setDetails(self, content):
        self.detailBrowser.setText(content)

    def disabledInputWidgets(self, disabled=True):
        self.offerButton.setDisabled(disabled)
        self.refreshButton.setDisabled(disabled)
        self.editButton.setDisabled(disabled)

# ================ Logic ====================

class Communication(QObject):
    sg_offer = pyqtSignal(dict, name="getOffer")
    sg_productPage = pyqtSignal(dict, name="productPage")
    sg_finished = pyqtSignal(int, name="finished")

class BasePricingThread(QThread):

    def __init__(self, parent=None, api=None):
        super(BasePricingThread, self).__init__(parent)
        self.api = api
        self.parent = parent

class FetchPricingDataThread(BasePricingThread):

    def run(self) -> None:
        offer = self.api.fetchListOffer()
        offer['data'] = sorted(offer['data'], key=lambda o: (o["note"], o["lowestPrice"]))
        self.parent.communication.sg_offer.emit(offer)
        listConf = self.api.fetchListConfiguration()['data']
        for o in offer['data']:
            pid = o['id']
            ic = findi(listConf, lambda o: pid == o['productId'])
            if not listConf[ic]['enabled']:
                continue
            sig = dict(offer=o, productPage=self.api.fetchProductPage(pid)['data'],
                       conf=listConf[ic], suggest = self.api.fetchSuggest(pid)['data'])
            self.parent.communication.sg_productPage.emit(sig)
        self.parent.communication.sg_finished.emit(1)


class PricingBoardLogic(PricingBoard):
    StatusDict = {0: "通过", 1: "满足", 2: "亏损", 3: "风险", 4: "无效", 5: "无竞争", 6: "调幅过大",}

    def __init__(self, parent=None, api=None):
        super(PricingBoardLogic, self).__init__(parent)
        self.api = api
        self.pricingData = None
        self.pricingBoardData = []
        self.pricingBoardMoreData = []
        self.pricingBoard.clicked.connect(self.onClickPricingBoard)
        self.pricingBoard.doubleClicked.connect(self.onDoubleClickPricingBoard)
        self.refreshButton.clicked.connect(self.onClickRefreshButton)
        self.openUrlButton.clicked.connect(self.onClickOpenUrlButton)
        self.editButton.clicked.connect(self.onClickEditButton)
        self.offerButton.clicked.connect(self.onClickOfferButton)
        self.communication = Communication()
        self.communication.sg_offer.connect(self.initPricingBoardPhase1)
        self.communication.sg_productPage.connect(self.initPricingBoardPhase2)
        self.communication.sg_finished.connect(self.eventFetchDataFinished)
        self.selectedRowIndex = 0

    def reset(self):
        self.clearAllItems()
        self.pricingBoardData = []
        self.pricingBoardMoreData = []
        self.setCurrentTime()

    def initPricingBoardPhase1(self, offers):
        table = []
        for offer in offers['data']:
            profit = offer['price'] - offer['lowestPrice']
            item = ("", "", offer['price'], offer['lowestPrice'],
                    0, profit, "", offer['productName'], False,
                    offer)
            table.append(item)

        self.pricingData = []
        for item in table:
            self.appendRow(item, bgcolor=None)
            self.pricingBoardData.append(item)

    def initPricingBoardPhase2(self, data: dict):
        offer = data['offer']
        productPage = data['productPage']
        conf = data['conf']
        suggest = data['suggest']

        profit = offer['price'] - offer['lowestPrice']
        shopName = productPage['self']['shopName']
        no1 = productPage['competitors'][0]['shopName']
        if no1 == productPage['self']['shopName']:
            bgcolor = self.Q_GREED
        else:
            bgcolor = self.Q_RED

        if conf['strategyId'] == "UnitPriceStrategy":
            no1 = "单: " + no1
        elif conf['strategyId'] == "TotalPriceStrategy":
            no1 = "总: " + no1

        rank = 1 + findi(productPage['competitors'], lambda o: o['shopName'] == shopName)
        suggestPrice = suggest['price']
        reduce = suggestPrice - offer['price']
        item = (no1, f"{rank}/{len(productPage['competitors'])}", offer['price'], offer['lowestPrice'],
                reduce, profit, self.StatusDict[suggest["status"]],
                offer['productName'], conf['enabled'],
                data)
        iRow = findi(self.pricingBoardData, lambda o: o[-1]['id'] == offer['id'])
        self.updateRow(iRow, item, bgcolor=bgcolor)
        self.pricingBoardMoreData.append(data)
        pass

    def fetchPricingData(self):
        thread = FetchPricingDataThread(self, self.api)
        thread.start()

    def onDoubleClickPricingBoard(self):
        index = self.pricingBoard.selectionModel().currentIndex()
        self.selectedRowIndex = index.row()
        try:
            data = self.pricingBoardMoreData[index.row()]
            productPage = data['productPage']
            dialog = CompetitorStatDialog()
            dialog.setProductName(productPage['productName'])
            for i, c in enumerate(productPage["competitors"]):
                total = c['price2'] + c['shippingGroup']['unitCost']
                dialog.appendRow((c['shopName'], c['label'], c['price2'],
                                total, c['shippingGroup']['unitCost'],
                                c['shippingGroup']['extraUnitCost'], c['quantity']))
            if dialog.exec_():
                pass
            dialog.destroy()
        except IndexError as e:
            print("line ", lineno(), e)

    def onClickPricingBoard(self):
        index = self.pricingBoard.selectionModel().currentIndex()
        self.selectedRowIndex = index.row()
        try:
            data = self.pricingBoardMoreData[index.row()]
            print(data)
        except IndexError as e:
            print("line ", lineno(), e)

    def onClickRefreshButton(self):
        self.reset()
        self.disabledInputWidgets(True)
        self.fetchPricingData()

    def eventFetchDataFinished(self, state):
        self.disabledInputWidgets(False)
        self.setCurrentTime()

    def onClickEditButton(self):
        pass

    def onClickOpenUrlButton(self):
        pass

    def onClickOfferButton(self):
        pass