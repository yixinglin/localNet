from utils import httprequests
import json
from PyQt5.QtCore import QObject
class MetroPricing(QObject):

    def __init__(self, parent=None, baseURL=""):
        super(MetroPricing, self).__init__(parent)
        self.baseURL = baseURL

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

    def updateConfiguration(self, conf:list):
        resp = httprequests.post(f"{self.baseURL}/pricing/metro/conf", json=conf)
        return json.loads(resp.content)
