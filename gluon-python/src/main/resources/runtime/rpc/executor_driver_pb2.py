# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: executor_driver.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='executor_driver.proto',
  package='',
  syntax='proto3',
  serialized_options=b'\n!com.filipovski.gluon.python.protoP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x15\x65xecutor_driver.proto\" \n\x10\x45xecutionRequest\x12\x0c\n\x04\x63ode\x18\x01 \x01(\t\"#\n\x11\x45xecutionResponse\x12\x0e\n\x06output\x18\x01 \x01(\t2D\n\x0e\x45xecutorDriver\x12\x32\n\x07\x65xecute\x12\x11.ExecutionRequest\x1a\x12.ExecutionResponse\"\x00\x42%\n!com.filipovski.gluon.python.protoP\x01\x62\x06proto3'
)




_EXECUTIONREQUEST = _descriptor.Descriptor(
  name='ExecutionRequest',
  full_name='ExecutionRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='code', full_name='ExecutionRequest.code', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=25,
  serialized_end=57,
)


_EXECUTIONRESPONSE = _descriptor.Descriptor(
  name='ExecutionResponse',
  full_name='ExecutionResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='output', full_name='ExecutionResponse.output', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=59,
  serialized_end=94,
)

DESCRIPTOR.message_types_by_name['ExecutionRequest'] = _EXECUTIONREQUEST
DESCRIPTOR.message_types_by_name['ExecutionResponse'] = _EXECUTIONRESPONSE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

ExecutionRequest = _reflection.GeneratedProtocolMessageType('ExecutionRequest', (_message.Message,), {
  'DESCRIPTOR' : _EXECUTIONREQUEST,
  '__module__' : 'executor_driver_pb2'
  # @@protoc_insertion_point(class_scope:ExecutionRequest)
  })
_sym_db.RegisterMessage(ExecutionRequest)

ExecutionResponse = _reflection.GeneratedProtocolMessageType('ExecutionResponse', (_message.Message,), {
  'DESCRIPTOR' : _EXECUTIONRESPONSE,
  '__module__' : 'executor_driver_pb2'
  # @@protoc_insertion_point(class_scope:ExecutionResponse)
  })
_sym_db.RegisterMessage(ExecutionResponse)


DESCRIPTOR._options = None

_EXECUTORDRIVER = _descriptor.ServiceDescriptor(
  name='ExecutorDriver',
  full_name='ExecutorDriver',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=96,
  serialized_end=164,
  methods=[
  _descriptor.MethodDescriptor(
    name='execute',
    full_name='ExecutorDriver.execute',
    index=0,
    containing_service=None,
    input_type=_EXECUTIONREQUEST,
    output_type=_EXECUTIONRESPONSE,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_EXECUTORDRIVER)

DESCRIPTOR.services_by_name['ExecutorDriver'] = _EXECUTORDRIVER

# @@protoc_insertion_point(module_scope)