from views.pricing.components.ui_newofferdialog import Ui_Dialog
from PyQt5.QtCore import Qt, QThread, QObject, pyqtSignal
from PyQt5.QtWidgets import QDialog
from jinja2 import Environment, FileSystemLoader

class NewOfferDialog(QDialog, Ui_Dialog):

    def __init__(self, parent=None):
        super(NewOfferDialog, self).__init__(parent)
        self.setupUi(self)
        self.setWindowTitle("Create New Offer")
        self.groupNameMap = {}  # GroupName: (GroupId, unitCost)
        self.lowestPrice = -1

    def addShippingGroupItem(self, groupName, groupId, unitCost):
        self.groupNameMap[groupName] = (groupId, unitCost)
        self.shippingGroup.addItem(groupName)

    def setNewOffer(self, price, quantity, shippingGroupId):
        self.unitPrice.setValue(price)
        self.quantity.setValue(quantity)

    def getOfferFromUserInput(self):
        unitPrice = self.unitPrice.text()
        quantity = self.quantity.value()
        groupName = self.getShippingGroupName()
        groupId = self.groupNameMap[groupName][0]
        return unitPrice, quantity, groupId

    def setTextBrowser(self, html):
        self.textBrowser.setText(html)

    def setProductName(self, productName):
        self.productName.setText(productName)

    def setLowestPrice(self, price):
        self.lowestPrice = price

    def getShippingGroupName(self):
        groupName = self.shippingGroup.currentText()
        return groupName

    def getShippingGroupId(self):
        groupName = self.getShippingGroupName()
        return self.groupNameMap[groupName][0]

    def getUnitPrice(self):
        return self.unitPrice.value()

    @property
    def profit(self):
        return self.totalPrice - self.lowestPrice

    @property
    def totalPrice(self):
        unitPrice, _, _ = self.getOfferFromUserInput()
        groupName = self.getShippingGroupName()
        return float(unitPrice) + self.groupNameMap[groupName][1]
# ============= Logic =====================

class Communication(QObject):
    sg_shippinggroups = pyqtSignal(dict)

class ShippingGroupThread(QThread):

    GET_LIST = 0

    def __init__(self, parent=None, api=None, task=0):
        super(ShippingGroupThread, self).__init__(parent)
        self.api = api
        self.parent = parent
        self.task = task

    def run(self) -> None:
        if self.task == self.GET_LIST:
            data = self.api.fetchListShippingGroup()
            self.parent.communication.sg_shippinggroups.emit(data)

class NewOfferDialogLogic(NewOfferDialog):

    def __init__(self, parent=None, api=None):
        super(NewOfferDialogLogic, self).__init__(parent)
        self.api = api
        self.communication = Communication()
        self.communication.sg_shippinggroups.connect(self.addShippingGroupItemsToComboBox)
        self.confirmBox.accepted.connect(self.onClickButtonConfirm)
        self.shippingGroup.currentTextChanged.connect(self.onComboboxChanged)
        self.unitPrice.textChanged.connect(self.onChangedUnitPrice)
        thread = ShippingGroupThread(self, api, task=ShippingGroupThread.GET_LIST)
        thread.start()

    def addShippingGroupItemsToComboBox(self, data):
        for sg in data['data']:
            self.addShippingGroupItem(sg['groupName'], sg['id'], sg['unitCost'])
        self.renderTextBrowser()


    def onClickButtonConfirm(self):
        unitPrice, quantity, groupId = self.getOfferFromUserInput()
        print(unitPrice, quantity, groupId)

    def onChangedUnitPrice(self):
        if self.getShippingGroupName() != "":
            self.renderTextBrowser()

    def onComboboxChanged(self):
        data = self.api.fetchShippingGroupById(self.getShippingGroupId())
        if data is not None:
            sg = data['data']
            self.groupNameMap[self.getShippingGroupName()] = (sg['id'], sg['unitCost'])
        self.renderTextBrowser()

    def renderTextBrowser(self):
        env = Environment(loader=FileSystemLoader('./'))
        template = env.get_template("assets/newofferdetails.html")
        content = template.render(lprice= f"{self.lowestPrice: .2f}",
                                  profit=f"{self.profit: .2f}",
                                  total=f"{self.totalPrice: .2f}")
        self.setTextBrowser(content)
