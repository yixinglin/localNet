from utils import httprequests
import json

class MetroPricing:

    def __init__(self, baseURL):
        self.baseURL = baseURL

    def fetchListConfiguration(self) -> dict:
        resp = httprequests.get(f"{self.baseURL}/pricing/metro/conf")
        return json.loads(resp.content)


