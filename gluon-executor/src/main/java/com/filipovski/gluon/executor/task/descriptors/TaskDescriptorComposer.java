package com.filipovski.gluon.executor.task.descriptors;

import java.util.Map;

/**
 * Implementations of this interface provide the means for composing a corresponding
 * {@link TaskDescriptor} given a set of properties. These implementations are useful
 * for recreating descriptors on remote systems and utilizing them for the creation of
 * tasks.
 *
 * <p>Interface implementations should provide validation of the supplied properties
 * before creating the descriptor instance.</p>
 */

public interface TaskDescriptorComposer {

    String TASK_ID = "task.id";

    String TASK_CLASS_NAME = "task.className";

    TaskDescriptor compose(Map<String, String> descriptorProperties);

}
