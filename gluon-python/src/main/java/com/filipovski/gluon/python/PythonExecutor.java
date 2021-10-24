package com.filipovski.gluon.python;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.executor.AbstractExecutor;
import com.filipovski.gluon.executor.executor.ExecutionContext;
import com.filipovski.gluon.executor.executor.ExecutionData;
import com.filipovski.gluon.executor.executor.output.DisplayData;
import com.filipovski.gluon.executor.util.ConnectivityUtil;
import com.filipovski.gluon.executor.util.ExternalProcess;
import com.filipovski.gluon.python.utils.FileUtils;
import com.typesafe.config.Config;
import org.apache.commons.exec.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Python executor capable of executing python code in an interactive python shell.
 *
 * <p>This implementation starts a python shell process using the shell implementation
 * scripts available in the plugin's resources. Communication with this process is
 * enabled through gRPC, where the shell process acts a server by spinning up a gRPC
 * server instance listening to a port supplied at startup.</p>
 *
 * <p>Python executor delegates execution to python shell through asynchronous calls
 * to the server. Execution output is streamed back to the executor, where they can
 * be handled asynchronously through a supplied output handler.</p>
 */

public class PythonExecutor extends AbstractExecutor {

    private final Logger logger = LogManager.getLogger(PythonExecutor.class);

    private static final String PYTHON_SHELL_SCRIPT_RESOURCE_PATH = "/runtime/executor_launcher.py";

    private final ExecutionEnvironment environment;

    private final Config config;

    private final PythonShellLauncher shellLauncher;

    private PythonShellProcess pythonShellProcess;

    private PythonShellClient pythonShellClient;

    public PythonExecutor(ExecutionEnvironment environment, Config config) {
        this.environment = environment;
        this.config = config;
        this.shellLauncher = new PythonShellLauncher(config.getString(PythonConfigOptions.PYTHON_INTERPRETER));
    }

    @Override
    public void start() {
        try {
            int port = ConnectivityUtil.findAvailablePort();
            pythonShellProcess = shellLauncher.launch(port);
            pythonShellClient = new PythonShellClient("localhost", port);
        } catch (IOException e) {
            logger.error("Failed to start the python shell process [{}]", e.toString());
        }
    }

    @Override
    public void execute(ExecutionData data, ExecutionContext executionContext) {
        pythonShellClient.execute(data.getCode(), (executionResponse) -> {
            DisplayData output = new DisplayData(executionResponse.getOutput(),
                    DisplayData.Type.valueOf(executionResponse.getType().name()));
            executionContext.getTaskOutputWriter().write(output);
        });
    }

    @Override
    public void stop() {
        try {
            pythonShellClient.shutdown();
        } catch (InterruptedException e) {
            logger.warn("Exception occurred while shutting down python shell client !");
        }
        pythonShellProcess.stop();
    }

    public static class PythonShellLauncher {

        private final Logger logger = LogManager.getLogger(PythonShellLauncher.class);

        private final String pythonInterpreter;

        public PythonShellLauncher(String pythonInterpreter) {
            this.pythonInterpreter = pythonInterpreter;
        }

        //  TODO: Script arguments
        public PythonShellProcess launch(int port) throws IOException {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
            DefaultExecutor executor = new DefaultExecutor();
            PythonShellProcess process = new PythonShellProcess(watchdog, port);
            executor.setWatchdog(watchdog);
            // Dismiss output
            executor.setStreamHandler(new PumpStreamHandler(null, null, null));
            String scriptPath = preparePythonShellScript().toString();

            CommandLine cmdLine = new CommandLine(pythonInterpreter);
            cmdLine.addArgument(scriptPath);
            cmdLine.addArgument("--port");
            cmdLine.addArgument(String.valueOf(port)); // Fixed port for testing purposes. Replace with port

            try {
                executor.execute(cmdLine, process);
                logger.info("Python shell process starting on port [{}] [{}]", port, cmdLine);
            } catch (IOException e) {
                logger.error("Python shell failed to launch [{}] [{}]", cmdLine, e);
            }

            return process;
        }

        private Path preparePythonShellScript() throws IOException {
            Path rawShellLauncherResourcePath = Path.of(PYTHON_SHELL_SCRIPT_RESOURCE_PATH);
            Path rawRuntimeResourcePath = rawShellLauncherResourcePath.getParent();
            URL runtimeResource = getClass().getResource(rawRuntimeResourcePath.toString());
            File tmpResourceDir = Files.createTempDirectory("pythonResources").toFile();

            FileUtils.copyResourcesRecursively(runtimeResource, tmpResourceDir);
            tmpResourceDir.deleteOnExit();

            return tmpResourceDir.toPath().resolve(rawShellLauncherResourcePath.getFileName());
        }
    }

    public static class PythonShellProcess extends ExternalProcess {

        private final Logger logger = LogManager.getLogger(PythonShellProcess.class);

        private final int port;

        public PythonShellProcess(ExecuteWatchdog watchdog, int port) {
            super(watchdog);
            this.port = port;
        }

        @Override
        public void onProcessComplete(int i) {
            logger.info("Python process completed");
        }

        @Override
        public void onProcessFailed(ExecuteException e) {
            logger.error("Python process failed [{}]", e.toString());
        }

        public int getPort() {
            return port;
        }
    }
}
