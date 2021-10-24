import sys
from io import StringIO


class OutputStreamBuffer(object):

    def __init__(self, stream):
        self.io = StringIO()

    def write(self, data):
        self.io.write(data)

    def getvalue(self):
        return self.io.getvalue()

    def clear(self):
        self.io = StringIO()

    def close(self):
        self.io.close()

    def flush(self):
        self.io.flush()


class OutputCapture:
    """Context manager that redirects output to external handlers."""

    def __init__(self, output_stream):
        self.output_stream = output_stream

    def __enter__(self):
        self.sys_stdout = sys.stdout
        self.sys_stderr = sys.stderr
        # self.sys_displayhook = sys.displayhook

        stdout = sys.stdout = self.output_stream
        # stderr = sys.stderr = self.output_stream
        # sys.displayhook = lambda value: print('called', value)

        return stdout #, stderr

    def __exit__(self, exc_type, exc_val, exc_tb):
        sys.stdout = self.sys_stdout
        # sys.stderr = self.sys_stderr
        # sys.displayhook = self.sys_displayhook
