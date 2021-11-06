package com.filipovski.gluonserver.environment;

import com.filipovski.gluonserver.environment.events.EnvironmentStartCommandEvent;
import com.filipovski.gluonserver.environment.events.EnvironmentStopCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling environment management commands coming
 * in as events.
 *
 * <p>This is where commands for environments from external systems are handled.
 * If another environment command is made available to client services, that command
 * should be handled here.</p>
 */

@Service
public class EnvironmentCommandHandler {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentCommandHandler.class);

    private final EnvironmentManager environmentManager;

    public EnvironmentCommandHandler(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @EventListener
    public void onEnvironmentStartCommand(EnvironmentStartCommandEvent event) {
        environmentManager.createSessionEnvironment(event.getSessionId());
    }

    @EventListener
    public void onEnvironmentStopCommand(EnvironmentStopCommandEvent event) {
        environmentManager.stopSessionEnvironment(event.getSessionId());
    }

}
