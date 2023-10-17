from utils import httprequests
import json

class MetroPricing:

    def __init__(self, baseURL):
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