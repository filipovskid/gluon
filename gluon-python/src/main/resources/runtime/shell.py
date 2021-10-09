import ast
import types

from typing import List

# TODO: Handle shell inputs or just drop them

class InteractiveShell:
    """Interactive shell for python that can execute arbitrary code."""

    def __init__(self):
        self.init_execution_namespace()

    def init_execution_namespace(self):
        """Initialize the namespace of the execution environment.

        The interactive execution behaves as if the code is being executed as part of
        a module, thus we initialize a module and use its __dict__ as a namespace
        during execution."""

        self.module = types.ModuleType("__main__", doc="Gluon interactive executor environment")
        self.local_ns = self.module.__dict__

    @property
    def global_ns(self):
        return self.module.__dict__

    def run_source_code(self, source_code: str):
        """
        Facilitate execution of a raw block of code. This method is primarily
        responsible for parsing the raw code into AST nodes.

        :param source_code : str
            Block of code represented as a string
        """

        nodes = ast.parse(source_code).body
        self.run_ast_nodes(nodes)

    def run_ast_nodes(self, nodes: List[ast.AST]):
        """
        Facilitate execution of a list of ast nodes such that the last expression's
        result is returned.

        :param nodes : list[ast.AST]
            A list of AST nodes to be compiled and executed
        """

        if isinstance(nodes[-1], ast.Expr):
            execution_nodes, interactive_nodes = nodes[:-1], [nodes[-1]]
        else:
            execution_nodes, interactive_nodes = nodes, []

        runnable_nodes = []
        for node in execution_nodes:
            runnable_nodes.append((node, 'exec'))

        for node in interactive_nodes:
            runnable_nodes.append((node, 'single'))

        for node, mode in runnable_nodes:
            if mode == 'exec':
                source = ast.Module([node], [])
            elif mode == 'single':
                source = ast.Interactive([node])

            code = compile(source, '<string>', mode=mode)
            self.execute_code(code)

    def execute_code(self, code):
        """
        Execute a code object. This method is responsible for handling
        any problems that might arise during code execution.

        :param code : code object
            Execute a code object as determined during compilation
        """

        try:
            exec(code, self.global_ns, self.local_ns)
        except:
            pass
