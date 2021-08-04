package com.filipovski.gluonserver.task;

import com.filipovski.gluonserver.task.events.ExecutionStatementSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * The TaskDispatcher is responsible for receiving {@link com.filipovski.gluon.executor.task.Task}s,
 * persisting them, obtaining environments and delegating Task execution to them.
 *
 * <p>This is the entrypoint for executing Tasks. Executing a Task requires obtaining an
 * {@link com.filipovski.gluon.executor.environment.ExecutionEnvironment} which schedules
 * the execution.<p/>
 */

@Service
public class TaskDispatcher {

    private final Logger logger = LoggerFactory.getLogger(TaskDispatcher.class);

    @EventListener
    public void onExecutionStatementSubmission(@NonNull ExecutionStatementSubmittedEvent event) {
        logger.info("ExecutionStatementSubmittedEvent | Task submission arrived");
    }

}
