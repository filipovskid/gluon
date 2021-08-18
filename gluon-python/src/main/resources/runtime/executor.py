from concurrent import futures
import grpc

from rpc import executor_driver_pb2_grpc
from rpc.executor_driver_pb2_grpc import ExecutorDriverServicer
from rpc.executor_driver_pb2 import ExecutionResponse


class ExecutorApp(ExecutorDriverServicer):

    def __init__(self):
        self.started = False
        self.server = None

    def start(self):
        if self.started:
            return

        self.server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
        executor_driver_pb2_grpc.add_ExecutorDriverServicer_to_server(self, self.server)
        self.server.add_insecure_port('[::]:50051')
        self.started = True
        self.server.start()
        self.server.wait_for_termination()

    def execute(self, request, context):
        return ExecutionResponse(output="Test output")
