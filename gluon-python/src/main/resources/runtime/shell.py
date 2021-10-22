import ast
import sys
import traceback
import types

from typing import List

from IPython.core import ultratb
from IPython.core.compilerop import check_linecache_ipython

# TODO: Handle shell inputs or just drop them
# TODO: Handle shell exceptions both on parse and during code execution


class InteractiveShell:
    """Interactive shell for python that can execute arbitrary code."""

    def __init__(self, output_stream, result_queue):
        self.output_stream = output_stream
        self.result_queue = result_queue

        self.init_execution_namespace()
        self.init_traceback_handlers()

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
        self._clear()

        try:
            nodes = ast.parse(source_code).body
            self.run_ast_nodes(nodes)
        except (OverflowError, SyntaxError, ValueError, TypeError,
                MemoryError, IndentationError) as e:
            self.showsyntaxerror()
        except Exception as e:
            self.output_stream.write(str(e))
            traceback.print_exc()

        self.flush_shell_output()

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
        Execute a code object.

        :param code : code object
            Execute a code object as determined during compilation
        """

        try:
            exec(code, self.global_ns, self.local_ns)
        except Exception as e:
            self.showtraceback(exception_only=True)
            # self.output_stream.write(str(e))
            # self.output_stream.write(traceback.print_exc())

    def _clear(self):
        """Clear the buffer for output stream."""
        self.output_stream.clear()

    def flush_shell_output(self):
        """Flushing shell execution output and making it available within result queue.
        This is useful when when we want to ensure that the system outputs are available
        at certain intervals."""

        output = self.output_stream.getvalue()

        if output:
            self.output_stream.clear()
            self.result_queue.put(("text", output))

    # IPython traceback handling
    # Adapted code from https://github.com/ipython/ipython/blob/master/IPython/core/interactiveshell.py

    def init_traceback_handlers(self, custom_exceptions=((), None)):
        # Syntax error handler.
        self.SyntaxTB = ultratb.SyntaxTB(color_scheme='NoColor')

        # The interactive one is initialized with an offset, meaning we always
        # want to remove the topmost item in the traceback, which is our own
        # internal code. Valid modes: ['Plain','Context','Verbose','Minimal']
        self.InteractiveTB = ultratb.AutoFormattedTB(mode='Plain',
                                                     color_scheme='NoColor',
                                                     tb_offset=1,
                                                     check_cache=check_linecache_ipython)

        # The instance will store a pointer to the system-wide exception hook,
        # so that runtime code (such as magics) can access it.  This is because
        # during the read-eval loop, it may get temporarily overwritten.
        self.sys_excepthook = sys.excepthook

        # and add any custom exception handlers the user may have specified
        # self.set_custom_exc(*custom_exceptions)

        # Mode is one of 'Context', 'Plain', 'Verbose', 'Minimal'

        # Set the exception mode
        self.InteractiveTB.set_mode(mode='Verbose')

    def showsyntaxerror(self, filename=None, running_compiled_code=False):
        """Display the syntax error that just occurred.

        This doesn't display a stack trace because there isn't one.

        If a filename is given, it is stuffed in the exception instead
        of what was there before (because Python's parser always uses
        "<string>" when reading from a string).

        If the syntax error occurred when running a compiled code (i.e. running_compile_code=True),
        longer stack trace will be displayed.
         """
        etype, value, last_traceback = self._get_exc_info()

        if filename and issubclass(etype, SyntaxError):
            try:
                value.filename = filename
            except:
                # Not the format we expect; leave it alone
                pass

        # If the error occurred when executing compiled code, we should provide full stacktrace.
        elist = traceback.extract_tb(last_traceback) if running_compiled_code else []
        stb = self.SyntaxTB.structured_traceback(etype, value, elist)
        self._showtraceback(etype, value, stb)

    def _showtraceback(self, etype, evalue, stb: str):
        """Actually show a traceback.

        Subclasses may override this method to put the traceback on a different
        place, like a side channel.
        """
        val = self.InteractiveTB.stb2text(stb)
        try:
            print(val)
        except UnicodeEncodeError:
            print(val.encode("utf-8", "backslashreplace").decode())

    def _get_exc_info(self, exc_tuple=None):
        """get exc_info from a given tuple, sys.exc_info() or sys.last_type etc.

        Ensures sys.last_type,value,traceback hold the exc_info we found,
        from whichever source.

        raises ValueError if none of these contain any information
        """
        if exc_tuple is None:
            etype, value, tb = sys.exc_info()
        else:
            etype, value, tb = exc_tuple

        if etype is None:
            if hasattr(sys, 'last_type'):
                etype, value, tb = sys.last_type, sys.last_value, \
                                   sys.last_traceback

        if etype is None:
            raise ValueError("No exception to find")

        # Now store the exception info in sys.last_type etc.
        # WARNING: these variables are somewhat deprecated and not
        # necessarily safe to use in a threaded environment, but tools
        # like pdb depend on their existence, so let's set them.  If we
        # find problems in the field, we'll need to revisit their use.
        sys.last_type = etype
        sys.last_value = value
        sys.last_traceback = tb

        return etype, value, tb

    def showtraceback(self, exc_tuple=None, filename=None, tb_offset=None,
                      exception_only=False, running_compiled_code=False):
        """Display the exception that just occurred.

        If nothing is known about the exception, this is the method which
        should be used throughout the code for presenting user tracebacks,
        rather than directly invoking the InteractiveTB object.

        A specific showsyntaxerror() also exists, but this method can take
        care of calling it if needed, so unless you are explicitly catching a
        SyntaxError exception, don't try to analyze the stack manually and
        simply call this method."""

        try:
            try:
                etype, value, tb = self._get_exc_info(exc_tuple)
            except ValueError:
                print('No traceback available to show.', file=sys.stderr)
                return

            if issubclass(etype, SyntaxError):
                # Though this won't be called by syntax errors in the input
                # line, there may be SyntaxError cases with imported code.
                self.showsyntaxerror(filename, running_compiled_code)
            else:
                if exception_only:
                    stb = ['An exception has occurred.\n']
                    stb.extend(self.InteractiveTB.get_exception_only(etype,
                                                                     value))
                else:
                    try:
                        # Exception classes can customise their traceback - we
                        # use this in IPython.parallel for exceptions occurring
                        # in the engines. This should return a list of strings.
                        stb = value._render_traceback_()
                    except Exception:
                        stb = self.InteractiveTB.structured_traceback(etype,
                                            value, tb, tb_offset=tb_offset)

                    self._showtraceback(etype, value, stb)

                # Actually show the traceback
                self._showtraceback(etype, value, stb)

        except KeyboardInterrupt:
            print('\n' + self.get_exception_only(), file=sys.stderr)

    def get_exception_only(self, exc_tuple=None):
        """
        Return as a string (ending with a newline) the exception that
        just occurred, without any traceback.
        """
        etype, value, tb = self._get_exc_info(exc_tuple)
        msg = traceback.format_exception_only(etype, value)
        return ''.join(msg)
