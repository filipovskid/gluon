package com.filipovski.gluon.python;

import com.filipovski.gluon.executor.executor.AbstractExecutor;
import com.filipovski.gluon.executor.util.ConnectivityUtil;
import com.filipovski.gluon.executor.util.ExternalProcess;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PythonExecutor extends AbstractExecutor {

    private final Logger logger = LoggerFactory.getLogger(PythonExecutor.class);
    private final PythonShellLauncher shellLauncher = new PythonShellLauncher();
    private PythonShellProcess pythonShellProcess;

    @Override
    public void start() {
        try {
            this.pythonShellProcess = shellLauncher.launch();
        } catch (IOException e) {
            logger.error("Failed to start the python shell process [{}]", e);
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public void stop() {

    }

    public class PythonShellLauncher {

//        TODO: Script arguments
        public PythonShellProcess launch() throws IOException {
            int port = ConnectivityUtil.findAvailablePort();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
            DefaultExecutor executor = new DefaultExecutor();
            PythonShellProcess process = new PythonShellProcess(watchdog, port);
            executor.setWatchdog(watchdog);
            // Dismiss output
            executor.setStreamHandler(new PumpStreamHandler(null, null, null));
            String scriptPath = PythonShellLauncher.class.getResource("/runtime/executor_launcher.py").getPath();

            CommandLine cmdLine = new CommandLine("python3");
            cmdLine.addArgument(scriptPath);
            cmdLine.addArgument("--port");
            cmdLine.addArgument(String.valueOf(50051)); // Fixed port for testing purposes. Replace with port

            try {
                executor.execute(cmdLine, process);
                logger.info("Python shell process starting on port [{}] [{}]", port, cmdLine);
            } catch (IOException e) {
                logger.error("Python shell failed to launch [{}] [{}]", cmdLine, e);
            }

            return process;
        }
    }

    public class PythonShellProcess extends ExternalProcess {

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
            logger.error("Python process failed");
        }

        public int getPort() {
            return port;
        }
    }
}
