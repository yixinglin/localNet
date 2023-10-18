from inspect import currentframe

# Get index of a target element in a list or tuple.
def findi(__iterable: list, key) -> int:
    for i, item in enumerate(__iterable):
            if key(item):
                return i
    return None

# Get line number in a file
def lineno():
    cf = currentframe()
    return cf.f_back.f_lineno

if __name__ == '__main__':
    x = ("a", "asd", "ddddd", "a")
    print(findi(x, key=lambda o: len(o) == 5))