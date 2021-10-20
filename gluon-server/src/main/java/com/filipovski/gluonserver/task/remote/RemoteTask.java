package com.filipovski.gluonserver.task.remote;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.task.Task;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptor;

public class RemoteTask extends Task {

    public RemoteTask(String taskId, TaskDescriptor taskDescriptor, ExecutionEnvironment environment) {
        super(taskId, taskDescriptor, environment);
    }

    @Override
    public void doRun() {
        throw new RuntimeException("");
    }
}
