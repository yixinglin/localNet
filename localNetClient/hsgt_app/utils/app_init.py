import yaml
import utils.glo as glo
from utils.log import *

def init_app(path):
    with open(path, 'r', encoding='utf-8') as f:
        conf = yaml.load(f, Loader=yaml.SafeLoader)
    log_info(conf)
    glo.setValue("app_settings", conf)

def save_app_settings(path, conf: dict):
    print(conf)
    with open(path, 'w', encoding='utf-8') as f:
        yaml.dump(conf, f)

