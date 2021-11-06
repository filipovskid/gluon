package com.filipovski.gluonserver.task;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.task.TaskStatus;
import com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptor;
import com.filipovski.gluonserver.environment.EnvironmentManager;
import com.filipovski.gluonserver.task.events.ExecutionStatementSubmittedEvent;
import com.filipovski.gluonserver.task.events.TaskStateChangedEvent;
import com.filipovski.gluonserver.task.remote.RemoteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * The TaskDispatcher is responsible for receiving {@link com.filipovski.gluon.executor.task.Task}s,
 * persisting them, obtaining environments and delegating Task execution to them.
 *
 * <p>This is the entrypoint for executing Tasks. Executing a Task requires obtaining an
 * {@link ExecutionEnvironment} which schedules the execution.<p/>
 */

@Service
public class TaskDispatcher {

    private final Logger logger = LoggerFactory.getLogger(TaskDispatcher.class);

    private final EnvironmentManager environmentManager;

    private final ApplicationEventPublisher publisher;

    public TaskDispatcher(EnvironmentManager environmentManager,
                          ApplicationEventPublisher publisher) {
        this.environmentManager = environmentManager;
        this.publisher = publisher;
    }

    @EventListener
    public void onExecutionStatementSubmission(@NonNull ExecutionStatementSubmittedEvent event) {
        Optional<ExecutionEnvironment> environment = environmentManager.getSessionEnvironment(event.getSessionId());
        environment.ifPresentOrElse(
                e -> {
                    e.execute(createExecutionStatementTask(event, e));
                    logger.info("Task [{}] has been dispatched to environment [{}] for execution.",
                            event.getTaskId(), event.getSessionId());
                },
                () -> handleEnvironmentNotFound(event.getTaskId(), event.getSessionId())
        );
    }

    private RemoteTask createExecutionStatementTask(ExecutionStatementSubmittedEvent event,
                                                    ExecutionEnvironment environment) {
        ExecutionStatementDescriptor descriptor = new ExecutionStatementDescriptor(
                        event.getTaskId(),
                        event.getStatement(),
                        event.getExecutorIdentifier()
        );

        return new RemoteTask(event.getTaskId(), descriptor, environment);
    }

    private void handleEnvironmentNotFound(String taskId, String sessionId) {
        logger.info("Environment with session id [{}] could not be found!", sessionId);
        TaskStateChangedEvent event = TaskStateChangedEvent.from(
                taskId,
                TaskStatus.FAILED.name(),
                "",
                Instant.now()
        );

        publisher.publishEvent(event);
    }

}
