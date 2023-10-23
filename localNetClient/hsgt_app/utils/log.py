import logging
import sys

# logging.basicConfig(
#     level=logging.CRITICAL,
#     format='[%(asctime)s] {%(pathname)s:%(lineno)d} %(levelname)s - %(message)s',
#     datefmt='%H:%M:%S',
#     stream=sys.stdout
# )

format1="[%(asctime)s][%(levelname)s][%(filename)s|%(funcName)s]: %(message)s"
format2="[%(levelname)s][%(asctime)s][%(filename)s:%(lineno)d] - %(message)s"

logging.basicConfig(level=logging.INFO,
                    # stream=sys.stdout,
                    datefmt='%Y-%m-%d %H:%M:%S',
                    format=format1,
                    filename="app.log")

def log_error(message):
    logging.log(logging.ERROR, message)

def log_info(message):
    logging.log(logging.INFO, message)

def log_warning(message):
    logging.log(logging.WARNING, message)

def log_stdout(*args, **kwargs):
    print(*args, **kwargs)