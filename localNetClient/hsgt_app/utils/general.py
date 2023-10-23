from inspect import currentframe

# Get index of a target element in a list or tuple.
def findi(__iterable: list, key) -> int:
    for i, item in enumerate(__iterable):
            if key(item):
                return i
    return None

def find(__iterable: list, key) -> int:
    for i, item in enumerate(__iterable):
            if key(item):
                return item
    return None

# Get line number in a file
def lineno():
    cf = currentframe()
    return cf.f_back.f_lineno

def float2str2(f, flag=False):
    s = f"{f:.2f}"
    if flag and f > 1e-5:
        s = "+" + s
    return s

if __name__ == '__main__':
    x = ("a", "asd", "ddddd", "a")
    print(findi(x, key=lambda o: len(o) == 5))
    x = {"1": "python", "2": "c++", "3": "java"}
    print(find(x.keys(), key=lambda k: x[k] == 'python'))