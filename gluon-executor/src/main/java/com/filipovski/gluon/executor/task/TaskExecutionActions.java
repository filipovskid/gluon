package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.remote.RemoteEnvironmentEventClient;
import com.filipovski.gluon.executor.proto.TaskExecutionStatus;
import com.filipovski.gluon.executor.proto.TaskStateDetails;

/**
 * Facility for propagating actions, occurred during task execution, to remote system
 * which dispatched the corresponding event.
 *
 * <p>This class is used for communicating with gluon server and notifying it for any
 * relevant events that might have occurred during task execution.</p>
 */

// TODO: Incorporate task result and handle update failures (update status).

public class TaskExecutionActions {

    private final RemoteEnvironmentEventClient client;

    public TaskExecutionActions(RemoteEnvironmentEventClient client) {
        this.client = client;
    }

    public void updateTaskExecutionState(TaskExecutionState taskExecutionState) {
        TaskStateDetails taskStateDetails = TaskStateDetails.newBuilder()
                .setTaskId(taskExecutionState.getTaskId())
                .setStatus(TaskExecutionStatus.valueOf(taskExecutionState.getStatus().name()))
                .setResult("empty - check TaskExecutionActions")
                .build();

        client.updateTaskStatus(taskStateDetails);
    }
}
