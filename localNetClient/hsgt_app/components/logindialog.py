import requests

from components.ui_logindialog import Ui_Dialog
from PyQt5.QtWidgets import QDialog, QMessageBox
import utils.glo as  glo
from utils.app_init import *

class LoginDialog(QDialog, Ui_Dialog):

    def __init__(self, parent):
        super(LoginDialog, self).__init__(parent)
        self.setupUi(self)

    def setInput(self, username, password):
        self.username.setText(username)
        self.password.setText(password)

    def setServerOptions(self, servers: list, currentIndex: int):
        self.server.addItems(servers)
        self.server.setCurrentIndex(currentIndex)

    def getInput(self):
        return (self.username.text(), self.password.text())

    def getServerIndex(self):
        return self.server.currentIndex()

class LoginDialogLogic(LoginDialog):

    def __init__(self, parent, api=None):
        super(LoginDialogLogic, self).__init__(parent)
        self.api = api
        self.settings = glo.getValue("app_settings")
        self.serverSettings = self.settings['server']
        self.setInput(self.serverSettings['username'],
                      self.serverSettings['password'])
        self.setServerOptions(self.serverSettings['host']['options'],
                              self.serverSettings['host']['selected'])
        self.buttonBox.accepted.connect(self.onClickConfirmButton)

    def onClickConfirmButton(self):
        username = self.username.text()
        password = self.password.text()
        serverIndex = self.getServerIndex()
        self.serverSettings['username'] = username
        self.serverSettings['password'] = password
        self.serverSettings['host']['selected'] = serverIndex
        server = self.server.currentText()
        self.api.setBaseURL(server)
        token = None
        try:
            resp = self.api.login(username, password)
            if resp['code'] == 60204:
                QMessageBox.information(self, "Login", resp['message'])
            else:
                token = resp['data']['token']
                glo.setValue('token', token)
                QMessageBox.information(self, "Login", "You have been logged in.")
        except requests.exceptions.ConnectionError:
            QMessageBox.critical(self, "Login", "Connection error!")
        return token
