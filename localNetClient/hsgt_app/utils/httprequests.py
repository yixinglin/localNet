import requests
import requests as req


def get(url, **kwargs):
    resp = requests.get(url, **kwargs)
    return resp

def post(url, **kwargs):
    resp = requests.post(url, **kwargs)
    return resp
