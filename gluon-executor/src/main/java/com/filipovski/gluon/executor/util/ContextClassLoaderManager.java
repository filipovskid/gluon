package com.filipovski.gluon.executor.util;

/**
 * {@link ContextClassLoaderManager} wraps the execution of a block setting its
 * context class loader to {@param loader} and reverting it at the end of the block.
 * This is meant to be used within a try-with-resources statement, providing a
 * convenient block encapsulation.
 */

public class ContextClassLoaderManager implements AutoCloseable {

    private final Thread thread;

    private final ClassLoader initialContextClassLoader;

    private ContextClassLoaderManager(Thread thread, ClassLoader initialContextClassLoader) {
        this.thread = thread;
        this.initialContextClassLoader = initialContextClassLoader;
    }

    public static ContextClassLoaderManager with(ClassLoader loader) {
        Thread currentThread = Thread.currentThread();
        ClassLoader initialLoader = currentThread.getContextClassLoader();

        currentThread.setContextClassLoader(loader);

        return new ContextClassLoaderManager(currentThread, initialLoader);
    }

    @Override
    public void close() {
        thread.setContextClassLoader(initialContextClassLoader);
    }
}
