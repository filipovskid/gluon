# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from . import python_driver_pb2 as python__driver__pb2


class PythonShellDriverStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.execute = channel.unary_stream(
                '/PythonShellDriver/execute',
                request_serializer=python__driver__pb2.ExecutionRequest.SerializeToString,
                response_deserializer=python__driver__pb2.ExecutionResponse.FromString,
                )


class PythonShellDriverServicer(object):
    """Missing associated documentation comment in .proto file."""

    def execute(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_PythonShellDriverServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'execute': grpc.unary_stream_rpc_method_handler(
                    servicer.execute,
                    request_deserializer=python__driver__pb2.ExecutionRequest.FromString,
                    response_serializer=python__driver__pb2.ExecutionResponse.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'PythonShellDriver', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class PythonShellDriver(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def execute(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_stream(request, target, '/PythonShellDriver/execute',
            python__driver__pb2.ExecutionRequest.SerializeToString,
            python__driver__pb2.ExecutionResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)