import time
import requests.exceptions
import webbrowser
from views.pricing.ui_pricingboard import Ui_Form
from datetime import datetime
from PyQt5.QtWidgets import QFrame, QDialog, QTableWidgetItem, QMessageBox, QAbstractItemView
from PyQt5.QtCore import Qt, pyqtSignal, QObject, QThread, QPoint
from views.pricing.components.baseui import BaseUi
from views.pricing.components.competitorstat import CompetitorStatDialog
from views.pricing.components.newofferdialog import NewOfferDialogLogic, ShippingGroupThread
from utils.log import *
from utils.exceptions import *
from utils.general import *
import traceback
from views.pricing.components.edititemdialog import EditItemDialogLogic
# pyuic5 pricingboard.ui -o ui_pricingboard.py

class PricingBoard(QFrame, Ui_Form, BaseUi):

    def __init__(self, parent=None):
        super(PricingBoard, self).__init__(parent)
        self.setupUi(self)
        self.setFixedSize(self.size())
        self.setWindowTitle("Pricing Board")
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
        cell = self.createCell(self.pricingBoard, iRow, 4, float2str2(row[4], flag=True), readOnly=True, **kwargs)  # Reduce
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 5, float2str2(row[5], flag=True), readOnly=True, **kwargs)  # Profit
        cell.setTextAlignment(Qt.AlignRight)
        cell = self.createCell(self.pricingBoard, iRow, 6, row[6], readOnly=True, **kwargs)  # Status
        cell.setTextAlignment(Qt.AlignLeft)
        cellName = self.createCell(self.pricingBoard, iRow, 7, row[7], readOnly=True, **kwargs)  # Name
        cellName.setTextAlignment(Qt.AlignLeft)

        cellName.setFlags(Qt.ItemIsUserCheckable | Qt.ItemIsEnabled)
        # cellName.setCheckState(Qt.Checked if row[8] else Qt.Unchecked)

    def setCurrentTime(self):
        now = datetime.now()
        dt_string = now.strftime("%b.%d.%Y %H:%M:%S")
        print("date and time =", dt_string)
        self.updateTime.setText(dt_string)

    def setProductName(self, name):
        self.productName.setText(name)

    def clearAllItems(self):
        # rowCount = self.pricingBoard.rowCount()
        # self.pricingBoard.model().removeRows(0, rowCount)
        self.pricingBoard.setRowCount(0)
        self.pricingBoard.clearContents()

    def setDetails(self, content):
        self.detailBrowser.setPlainText(content)

    def appendDetails(self, line):
        content = self.detailBrowser.toPlainText()
        mx = self.detailBrowser.verticalScrollBar().maximum()
        content = str(content).split('\n')
        content.append(line)
        s = "\n".join(content)
        self.detailBrowser.setPlainText(s)
        self.detailBrowser.verticalScrollBar().setValue(mx)


    def disabledInputWidgets(self, disabled=True):
        self.startButton.setDisabled(disabled)
        self.pricingBoard.setDisabled(disabled)
        # self.refreshButton.setDisabled(disabled)
        # self.editButton.setDisabled(disabled)

# ================ Logic ====================

class Communication(QObject):
    sg_offer = pyqtSignal(dict, list, name="getOffer")
    sg_productPage = pyqtSignal(dict, str, name="productPage")
    sg_finished = pyqtSignal(bool, name="finished")
    sg_errormessage = pyqtSignal(str, Exception, name="errormessage")

class BasePricingThread(QThread):

    def __init__(self, parent=None, api=None, target_index=None):
        super(BasePricingThread, self).__init__(parent)
        self.api = api
        self.parent = parent
        self.target_index = target_index

class FetchPricingDataThread(BasePricingThread):

    def run(self) -> None:
        try:
            offer = self.api.fetchListOffer()
            listConf = self.api.fetchListConfiguration()['data']
            offer['data'] = sorted(offer['data'], key=lambda o: (o["note"].lower(), o["lowestPrice"]))
            self.parent.communication.sg_finished.emit(False)
            if self.target_index is None:
                self.parent.communication.sg_offer.emit(offer, listConf)
            cnt = 0
            for i, o in enumerate(offer['data']):
                try:
                    pid = o['id']
                    ic = findi(listConf, lambda o: pid == o['productId'])
                    if self.target_index is None and listConf[ic]['enabled']:
                        self.__fetchItem(o, listConf[ic], f"{i+1}/{len(offer['data'])}")
                        cnt += 1
                    elif self.target_index == i:
                        self.__fetchItem(o, listConf[ic], f"{i+1}/{len(offer['data'])}")
                    else:
                        pass
                except Exception:
                    log_stdout(traceback.format_exc())
                    log_error(traceback.format_exc())
                # if cnt >= 10:
                #     break
        except Exception as e:
            self.parent.communication.sg_errormessage.emit(traceback.format_exc(), e)
        finally:
            self.parent.communication.sg_finished.emit(True)

    def __fetchItem(self, offer, conf, progress:str):
        pid = offer['id']
        product = offer['productName']
        log_stdout(f"Fetch {pid}: {product}")
        sig = dict(offer=offer, productPage=self.api.fetchProductPage(pid)['data'],
                   conf=conf, suggest=self.api.fetchSuggest(pid)['data'])
        self.parent.communication.sg_productPage.emit(sig, progress)


class PricingBoardLogic(PricingBoard):
    StatusDict = {0: "通过", 1: "满足", 2: "亏损", 3: "风险", 4: "无效", 5: "无竞争", 6: "调幅过大",}

    def __init__(self, parent=None, api=None, **kwargs):
        super(PricingBoardLogic, self).__init__(parent)
        self.api = api
        self.kwargs = kwargs
        self.pricingData = None
        self.pricingBoardData = []
        self.pricingBoardMoreData = []
        self.pricingBoard.clicked.connect(self.onClickPricingBoard)
        self.pricingBoard.doubleClicked.connect(self.onDoubleClickPricingBoard)
        self.pricingBoard.itemChanged.connect(self.eventPricingBoardCellChanged)
        self.refreshButton.clicked.connect(self.onClickRefreshButton)
        self.openUrlButton.clicked.connect(self.onClickOpenUrlButton)
        self.editButton.clicked.connect(self.onClickEditButton)
        self.startButton.clicked.connect(self.onClickStartButton)

        self.communication = Communication()
        self.communication.sg_offer.connect(self.initPricingBoardPhase1)
        self.communication.sg_productPage.connect(self.initPricingBoardPhase2)
        self.communication.sg_finished.connect(self.eventFetchDataFinished)
        self.communication.sg_errormessage.connect(self.eventErrorMessage)
        self.selectedRowIndex = 0
        self.statDialog: QDialog = None
        self.pricingDialog: QDialog = None
        self.isLoading = False
        self.reset()

    def reset(self):
        self.clearAllItems()
        self.pricingBoardData = []
        self.pricingBoardMoreData = []
        self.setCurrentTime()
        if self.statDialog:
            self.statDialog.destroy()
            self.statDialog = None
        if self.pricingDialog:
            self.pricingDialog.destroy()
            self.pricingDialog = None
        self.isLoading = False
        self.selectedRowIndex = 0

    def initPricingBoardPhase1(self, offers:dict, listConf:list):
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
            self.pricingBoardMoreData.append(None)

    def initPricingBoardPhase2(self, data: dict, progress: str):
        offer = data['offer']
        productPage = data['productPage']
        conf = data['conf']
        suggest = data['suggest']

        profit = offer['price'] - offer['lowestPrice']
        shopName = productPage['self']['shopName']
        productId = offer['id']
        self.setProductName(f"[{progress}] {productId} | {offer['productName']}")
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
        # suggestPrice = suggest['price']
        diff = suggest['diff']
        item = (no1, f"{rank}/{len(productPage['competitors'])}", offer['price'], offer['lowestPrice'],
                diff, profit, self.StatusDict[suggest["status"]],
                offer['productName'], conf['enabled'],
                data)
        iRow = findi(self.pricingBoardData, lambda o: o[-1]['id'] == offer['id'])
        self.updateRow(iRow, item, bgcolor=bgcolor)
        i = findi(self.pricingBoardData, key=lambda o: o[-1]['id'] == offer['id'])
        self.pricingBoardMoreData[i] = data # .append(data)
        # self.pricingBoard.verticalScrollBar().setSliderPosition(i-10)
        pass

    def fetchPricingData(self):
        thread = FetchPricingDataThread(self, self.api)
        thread.start()
        return thread

    def onClickPricingBoard(self):
        index = self.__getCurrentIndex()
        try:
            row = self.__get_data(index)
            if row:
                offer, _, _, _ = row
                self.__showCompetitorStatistics(index)
                self.setProductName(f"{offer['id']} | {offer['productName']}")
        except IndexError:
            log_stdout(traceback.format_exc())
            log_error(traceback.format_exc())
        except Exception as e:
            log_stdout(e)

    def onDoubleClickPricingBoard(self):
        index = self.__getCurrentIndex()
        c = index.column()
        if c == 1 or c == 0:
            # self.__showCompetitorStatistics(index)
            pass
        elif c == 2 or c == 4:
            self.__showNewOfferDialog(index)
        elif c == 7:
            self.onClickRefreshButton()

    def onClickRefreshButton(self):
        index = self.__getCurrentIndex()
        try:
            thread = FetchPricingDataThread(self, self.api, index.row())
            thread.start()
            thread.wait()
        except IndexError:
            log_stdout(traceback.format_exc())
            log_error(traceback.format_exc())

    def eventFetchDataFinished(self, finished):
        self.disabledInputWidgets(not finished)
        self.setCurrentTime()
        self.isLoading = not finished
        if finished:
            QMessageBox.information(self, "Info", "Finished!")

    def eventErrorMessage(self, message, exception):
        print(message)
        if isinstance(exception, requests.exceptions.ConnectionError):
            dialog = QMessageBox.critical(self, "Network Error", "Network connection error.")
        elif isinstance(exception, requests.exceptions.Timeout):
            dialog = QMessageBox.critical(self, "Network Error", "Timeout")
        else:
            raise exception

    def eventPricingBoardCellChanged(self, index):
        if not self.isLoading:
            log_stdout(index.row(), index.column())
            try:
                row = self.__get_data(index)
                if row:
                    offer, productPage, conf, suggest  = row
                    conf['lowestPrice'] = float(self.pricingBoard.item(index.row(), 3).text())
                    # conf['enabled'] = True if self.pricingBoard.item(index.row(), 7).checkState() else False
                    log_stdout(conf)
                    self.api.updateConfiguration([conf])
            except ValueError as e:
                QMessageBox.critical(self, "Error", "Please enter a number.")

    def onClickEditButton(self):
        index = self.__getCurrentIndex()
        row = self.__get_data(index)
        if row:
            _, _, conf, _ = row
            dialog = EditItemDialogLogic(self, self.api)
            dialog.setData(conf)
            reply = dialog.exec_()
            if reply:
                conf.update(**reply)

    def onClickOpenUrlButton(self):
        index = self.__getCurrentIndex()
        row = self.__get_data(index)
        if row:
            offer, productPage, conf, suggest = row
            webbrowser.open(suggest['url'], new=0, autoraise=True)


    def onClickStartButton(self):
        reply = QMessageBox.question(self, "Tips", "Start fetching data?")
        if reply == QMessageBox.Yes:
            self.reset()
            self.fetchPricingData()

    def closeEvent(self, a0) -> None:
        log_stdout("closePricingBoardWidget")
        if self.statDialog is not None:
            self.statDialog.destroy()

    def __showNewOfferDialog(self, index):
        try:
            if self.pricingDialog is None:
                self.pricingDialog = NewOfferDialogLogic(parent=self, api=self.api)
            offer, productPage, conf, suggest = self.__get_data(index)
            self.pricingDialog.setProductName(offer["productName"])
            self.pricingDialog.setLowestPrice(offer['lowestPrice'])

            if index.column() == 4:
                self.pricingDialog.setNewOffer(offer['price']+suggest['diff'],
                                               offer['quantity'], offer['shippingGroup']['id'])
            else:
                self.pricingDialog.setNewOffer(offer['price'], offer['quantity'], offer['shippingGroup']['id'])
            value = self.pricingDialog.exec_()
            if value:
                price, quantity, sg = value
                self.appendDetails(f"{price} | {quantity} | {sg}")
        except (IndexError, TypeError) as e:
            log_stdout(traceback.format_exc())
            log_error(traceback.format_exc())

    def __showCompetitorStatistics(self, index):
        try:
            row = self.__get_data(index)
            if row:
                _, productPage, _, _ = row
            if self.statDialog == None:
                log_stdout("Create statDialog")
                self.statDialog = CompetitorStatDialog()
                self.statDialog.move(QPoint(0, 0))
            self.statDialog.reset()
            self.statDialog.show()
            self.statDialog.setShopName(productPage['self']['shopName'])
            self.statDialog.setProductName(productPage['productName'])
            for i, c in enumerate(productPage["competitors"]):
                total = c['price2'] + c['shippingGroup']['unitCost']
                self.statDialog.appendRow((c['shopName'], c['label'], c['price2'],
                                total, c['shippingGroup']['unitCost'],
                                c['shippingGroup']['extraUnitCost'], c['quantity']))
        except (IndexError, TypeError) as e:
            log_stdout(traceback.format_exc())
            log_error(traceback.format_exc())


    def __getCurrentIndex(self):
        index = self.pricingBoard.selectionModel().currentIndex()
        self.selectedRowIndex = index.row()
        return index

    def __get_data(self, index):
        try:
            if index.row() == -1:
                raise NoRowSelectedError("No row is selected")
            data = self.pricingBoardMoreData[index.row()]
            return (data['offer'], data['productPage'],
                    data['conf'], data['suggest'])
        except NoRowSelectedError:
            QMessageBox.critical(self, "Error", "Please select a row.")
        except TypeError:
            reply = QMessageBox.question(self, "Error", "Fail: Please refresh the selected product.")
            if reply == QMessageBox.Yes:
                self.onClickRefreshButton()
