# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file '.\views\pricing\components\edititemdialog.ui'
#
# Created by: PyQt5 UI code generator 5.15.7
#
# WARNING: Any manual changes made to this file will be lost when pyuic5 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt5 import QtCore, QtGui, QtWidgets


class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(514, 281)
        self.confirmBox = QtWidgets.QDialogButtonBox(Dialog)
        self.confirmBox.setGeometry(QtCore.QRect(150, 220, 311, 32))
        self.confirmBox.setOrientation(QtCore.Qt.Horizontal)
        self.confirmBox.setStandardButtons(QtWidgets.QDialogButtonBox.Cancel|QtWidgets.QDialogButtonBox.Ok)
        self.confirmBox.setObjectName("confirmBox")
        self.label = QtWidgets.QLabel(Dialog)
        self.label.setGeometry(QtCore.QRect(30, 40, 49, 16))
        self.label.setObjectName("label")
        self.productName = QtWidgets.QLabel(Dialog)
        self.productName.setGeometry(QtCore.QRect(130, 40, 441, 16))
        self.productName.setObjectName("productName")
        self.label_3 = QtWidgets.QLabel(Dialog)
        self.label_3.setGeometry(QtCore.QRect(30, 70, 71, 16))
        self.label_3.setObjectName("label_3")
        self.productId = QtWidgets.QLabel(Dialog)
        self.productId.setGeometry(QtCore.QRect(130, 70, 421, 16))
        self.productId.setObjectName("productId")
        self.label_5 = QtWidgets.QLabel(Dialog)
        self.label_5.setGeometry(QtCore.QRect(30, 130, 71, 16))
        self.label_5.setObjectName("label_5")
        self.label_6 = QtWidgets.QLabel(Dialog)
        self.label_6.setGeometry(QtCore.QRect(30, 160, 71, 16))
        self.label_6.setObjectName("label_6")
        self.label_7 = QtWidgets.QLabel(Dialog)
        self.label_7.setGeometry(QtCore.QRect(30, 100, 71, 16))
        self.label_7.setObjectName("label_7")
        self.label_9 = QtWidgets.QLabel(Dialog)
        self.label_9.setGeometry(QtCore.QRect(30, 190, 71, 16))
        self.label_9.setObjectName("label_9")
        self.label_10 = QtWidgets.QLabel(Dialog)
        self.label_10.setGeometry(QtCore.QRect(280, 130, 71, 16))
        self.label_10.setObjectName("label_10")
        self.label_11 = QtWidgets.QLabel(Dialog)
        self.label_11.setGeometry(QtCore.QRect(280, 160, 71, 16))
        self.label_11.setObjectName("label_11")
        self.note = QtWidgets.QLineEdit(Dialog)
        self.note.setGeometry(QtCore.QRect(130, 130, 113, 22))
        self.note.setObjectName("note")
        self.lowestPrice = QtWidgets.QDoubleSpinBox(Dialog)
        self.lowestPrice.setGeometry(QtCore.QRect(130, 160, 111, 22))
        self.lowestPrice.setAlignment(QtCore.Qt.AlignRight|QtCore.Qt.AlignTrailing|QtCore.Qt.AlignVCenter)
        self.lowestPrice.setMaximum(99999999.0)
        self.lowestPrice.setSingleStep(0.1)
        self.lowestPrice.setObjectName("lowestPrice")
        self.maxAdjust = QtWidgets.QDoubleSpinBox(Dialog)
        self.maxAdjust.setGeometry(QtCore.QRect(370, 130, 91, 22))
        self.maxAdjust.setAlignment(QtCore.Qt.AlignRight|QtCore.Qt.AlignTrailing|QtCore.Qt.AlignVCenter)
        self.maxAdjust.setMaximum(9999999999999.0)
        self.maxAdjust.setSingleStep(0.1)
        self.maxAdjust.setObjectName("maxAdjust")
        self.reduce = QtWidgets.QDoubleSpinBox(Dialog)
        self.reduce.setGeometry(QtCore.QRect(370, 160, 91, 22))
        self.reduce.setAlignment(QtCore.Qt.AlignRight|QtCore.Qt.AlignTrailing|QtCore.Qt.AlignVCenter)
        self.reduce.setMaximum(99999999999999.0)
        self.reduce.setSingleStep(0.01)
        self.reduce.setObjectName("reduce")
        self.amount = QtWidgets.QSpinBox(Dialog)
        self.amount.setGeometry(QtCore.QRect(130, 100, 111, 22))
        self.amount.setAlignment(QtCore.Qt.AlignRight|QtCore.Qt.AlignTrailing|QtCore.Qt.AlignVCenter)
        self.amount.setMaximum(999999999)
        self.amount.setObjectName("amount")
        self.enabled = QtWidgets.QCheckBox(Dialog)
        self.enabled.setGeometry(QtCore.QRect(280, 100, 76, 20))
        self.enabled.setLayoutDirection(QtCore.Qt.LeftToRight)
        self.enabled.setChecked(False)
        self.enabled.setTristate(False)
        self.enabled.setObjectName("enabled")
        self.strategy = QtWidgets.QComboBox(Dialog)
        self.strategy.setGeometry(QtCore.QRect(130, 190, 111, 22))
        self.strategy.setObjectName("strategy")

        self.retranslateUi(Dialog)
        self.confirmBox.accepted.connect(Dialog.accept) # type: ignore
        self.confirmBox.rejected.connect(Dialog.reject) # type: ignore
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Dialog"))
        self.label.setText(_translate("Dialog", "Product"))
        self.productName.setText(_translate("Dialog", "Name"))
        self.label_3.setText(_translate("Dialog", "Product ID"))
        self.productId.setText(_translate("Dialog", "Product ID"))
        self.label_5.setText(_translate("Dialog", "Note"))
        self.label_6.setText(_translate("Dialog", "Lowest Price"))
        self.label_7.setText(_translate("Dialog", "Amount"))
        self.label_9.setText(_translate("Dialog", "Strategy"))
        self.label_10.setText(_translate("Dialog", "Max. Adjust"))
        self.label_11.setText(_translate("Dialog", "Reduce"))
        self.enabled.setText(_translate("Dialog", "Enabled"))