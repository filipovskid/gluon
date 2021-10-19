package com.filipovski.gluonserver.task;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptor;
import com.filipovski.gluonserver.environment.EnvironmentManager;
import com.filipovski.gluonserver.task.events.ExecutionStatementSubmittedEvent;
import com.filipovski.gluonserver.task.remote.RemoteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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

    public TaskDispatcher(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @EventListener
    public void onExecutionStatementSubmission(@NonNull ExecutionStatementSubmittedEvent event) {
        logger.info("ExecutionStatementSubmittedEvent | Task submission arrived");
        environmentManager.createSessionEnvironment(event.getExecutorIdentifier());
        logger.info("Resources allocated");
        Optional<ExecutionEnvironment> environment = environmentManager.getSessionEnvironment(event.getExecutorIdentifier());
        environment.ifPresent(e -> e.execute(createExecutionStatementTask(event, e)));
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

}
