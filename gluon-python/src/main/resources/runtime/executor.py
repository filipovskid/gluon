from concurrent import futures
import grpc

from rpc import executor_driver_pb2_grpc
from rpc.executor_driver_pb2_grpc import ExecutorDriverServicer
from rpc.executor_driver_pb2 import ExecutionResponse, ExecutionRequest
from shell import InteractiveShell


class ExecutorApp(ExecutorDriverServicer):
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
        self.shell = InteractiveShell()

    def start(self):
        """Start the gRPC server and wait for requests from Python executor."""

        if self.started:
            return

        self.server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
        executor_driver_pb2_grpc.add_ExecutorDriverServicer_to_server(self, self.server)
        self.server.add_insecure_port(self._create_address())
        self.started = True
        self.server.start()
        self.server.wait_for_termination()

    def _create_address(self):
        return f'[::]:{self.port}'

    def execute(self, request: ExecutionRequest, context):
        self.shell.run_source_code(request.code)
        return ExecutionResponse(output="Test output")
