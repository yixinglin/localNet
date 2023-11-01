
class NoRowSelectedError(RuntimeError):

    def __init__(self, *args, **kwargs):
        super(NoRowSelectedError, self).__init__(*args, **kwargs)

class AuthenticationError(RuntimeError):

    def __init__(self, *args, **kwargs):
        super(AuthenticationError, self).__init__(*args, **kwargs)

