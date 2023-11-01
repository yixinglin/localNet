from utils import httprequests
import json
from PyQt5.QtCore import QObject
from utils.exceptions import AuthenticationError

class MetroPricing(QObject):

    def __init__(self, parent=None, baseURL="", token=""):
        super(MetroPricing, self).__init__(parent)
        self.baseURL = baseURL
        self.token = token

    def setBaseURL(self, url):
        self.baseURL = url

    def fetchListConfiguration(self) -> dict:
        resp = httprequests.get(f"{self.baseURL}/pricing/metro/conf")
        return json.loads(resp.content)

    def fetchProductPage(self, productId):
        resp = httprequests\
            .get(f"{self.baseURL}/offer/metro/productpage?productId={productId}")
        return json.loads(resp.content)

    def fetchListOffer(self):
        resp = httprequests.get(f"{self.baseURL}/offer/metro/selectAll")
        return json.loads(resp.content)

    def fetchSuggest(self, productId):
        resp = httprequests.get(f"{self.baseURL}/pricing/metro/suggest?productId={productId}")
        return json.loads(resp.content)

    def fetchListShippingGroup(self):
        resp = httprequests.get(f"{self.baseURL}/shipment/metro/groups")
        return json.loads(resp.content)

    def fetchShippingGroupById(self, id):
        resp = httprequests.get(f"{self.baseURL}/shipment/metro/groups/db-{id}")
        return json.loads(resp.content)

    def updateConfiguration(self, conf: list):
        resp = httprequests.post(f"{self.baseURL}/pricing/metro/conf", json=conf)
        return json.loads(resp.content)

    def login(self, username, password):
        payload = dict(username=username, password=password)
        resp = httprequests.post(f"{self.baseURL}/vue-element-admin/user/login", json=payload)
        return json.loads(resp.content)

    def postNewOffer(self, offer: dict):
        resp = httprequests.post(f"{self.baseURL}/pricing/metro/edit",
                                 json=offer)
        ans = json.loads(resp.content)
        if ans['code'] != 20000:
            raise AuthenticationError(f"Failed to post new offer. {offer}")
        return ans