import sys
import threading
import time
from concurrent import futures
from queue import Queue

import grpc

from glutils import OutputStreamBuffer, OutputCapture
from rpc import python_driver_pb2_grpc
from rpc.python_driver_pb2_grpc import PythonShellDriverServicer
from rpc.python_driver_pb2 import ExecutionResponse, ExecutionRequest
from shell import InteractiveShell


class Executor:

    def __init__(self):
        self.output_buffer = OutputStreamBuffer(sys.stdout)
        self.output_capture = OutputCapture(output_stream=self.output_buffer)
        self.result_queue = Queue()
        self.shell = InteractiveShell(output_stream=self.output_buffer,
                                      result_queue=self.result_queue)

    def execute(self, code):
        with self.output_capture:
            self.shell.run_source_code(code)

        self.shell.flush_shell_output()

    def get(self):
        output = self.result_queue.get()
        return ExecutionResponse(output=output[1])

    def flush(self):
        self.shell.flush_shell_output()

    def has_output(self):
        return not self.result_queue.empty()


class ExecutorApp(PythonShellDriverServicer):
    """Entrypoint for executing python code dispatched by Python executor.

    This class acts as a gRPC server accepting execution requests from and returning
    results to Python executor. The code execution is facilitated by InteractiveShell
    which works with components for capturing, handling and collecting execution outputs.
    """
    # TODO: Dispatch execution outputs in real-time

    def __init__(self, port):
        self.port = port
        self.started = False
        self.server = None
        self.executor = Executor()

        self._lock = threading.Lock()

    def start(self):
        """Start the gRPC server and wait for requests from Python executor."""

        if self.started:
            return

        self.server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
        python_driver_pb2_grpc.add_PythonShellDriverServicer_to_server(self, self.server)
        self.server.add_insecure_port(self._create_address())
        self.started = True
        self.server.start()
        self.server.wait_for_termination()

    def _create_address(self):
        return f'[::]:{self.port}'

    def execute(self, request: ExecutionRequest, context):
        execution_thread = threading.Thread(target=self.executor.execute, args=(request.code,))

        with self._lock:
            execution_thread.start()

            while execution_thread.is_alive() or self.executor.has_output():
                time.sleep(.2)
                self.executor.flush()

                while self.executor.has_output():
                    yield self.executor.get()

            self.executor.flush()
            if self.executor.has_output():
                yield self.executor.get()
