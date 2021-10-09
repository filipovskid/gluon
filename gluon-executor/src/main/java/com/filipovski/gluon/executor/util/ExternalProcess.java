package com.filipovski.gluon.executor.util;

import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;

public abstract class ExternalProcess implements ExecuteResultHandler {

    private ExecuteWatchdog watchdog;

    public ExternalProcess(ExecuteWatchdog watchdog) {
        this.watchdog = watchdog;
    }

    public void stop() {
        this.watchdog.destroyProcess();
        this.watchdog.stop();
    }

}
