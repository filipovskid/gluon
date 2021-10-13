package com.filipovski.gluon.executor.executor.runtime;

import com.filipovski.gluon.executor.configuration.ExecutorConfigOptions;
import com.filipovski.gluon.executor.executor.ExecutorProvider;
import com.filipovski.gluon.executor.plugin.PluginDescriptor;
import com.filipovski.gluon.executor.plugin.PluginManager;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class for loading {@link com.filipovski.gluon.executor.executor.Executor} plugins
 * and their configuration. The main responsibility is to construct executors on-demand.
 *
 * <p>Executor creation is enabled by building and maintaining {@link ExecutorSpec}s for
 * each configured executor.</p>
 */

public class ExecutorLoader {

    private final Logger logger = LogManager.getLogger(ExecutorLoader.class);

    private final PluginManager pluginManager;

    private List<ExecutorSpec> executorSpecs;

    private boolean initialized;

    public ExecutorLoader(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.executorSpecs = Collections.emptyList();
        this.initialized = false;
    }

    public void initialize() {
        if (initialized) {
            logger.warn("Executor loader has already been initialized!");
            return;
        }

        initialized = true;
        pluginManager.initialize();
        load();
    }

    public List<ExecutorSpec> getExecutorSpecs() {
        return executorSpecs;
    }

    public void load() {
        executorSpecs = pluginManager.getPluginDescriptors().stream()
                .flatMap(descriptor -> buildExecutorSpecs(descriptor).stream())
                .collect(Collectors.toList());
    }

    private List<ExecutorSpec> buildExecutorSpecs(PluginDescriptor pluginDescriptor) {
        File configFile = findConfigFile(pluginDescriptor).orElseThrow(() -> {
            logger.error("Executor [{}] configuration file not found!", pluginDescriptor.getPluginId());
            return new IllegalStateException("Executor configuration not found!");
        });
        Config config = ConfigFactory.parseFile(configFile);

        String executorProviderClassName = config.getConfig("common")
                .getString(ExecutorConfigOptions.EXECUTOR_PROVIDER_CLASS_NAME);
        ExecutorProvider provider = pluginManager.load(
                pluginDescriptor,
                ExecutorProvider.class,
                executorProviderClassName
        ).orElseThrow(() -> {
            logger.error("Executor provider [{}] for plugin [{}] not found!", executorProviderClassName,
                    pluginDescriptor.getPluginId());
            return new IllegalStateException("Executor provider not found!");
        });

        return createExecutorConfigs(config).stream()
                .map(c -> createExecutorSpec(c, provider))
                .collect(Collectors.toList());
    }

    private ExecutorSpec createExecutorSpec(Config config, ExecutorProvider provider) {
        String executorName = config.getString(ExecutorConfigOptions.EXECUTOR_NAME);
        return new ExecutorSpec(executorName, provider, config);
    }


    private List<Config> createExecutorConfigs(Config config) {
        return config.root().keySet().stream()
                .filter(executor -> !executor.equals("common"))
                .map(value -> createExecutorConfig(config, value))
                .collect(Collectors.toList());
    }

    private Config createExecutorConfig(Config config, String configKey) {
        return config.getConfig(configKey)
                .withFallback(config.getConfig("common"))
                .resolve();
    }

    private Optional<File> findConfigFile(PluginDescriptor pluginDescriptor) {
        return Arrays.stream(pluginDescriptor.getPluginMetadataURLs())
                .map(URL::getPath)
                .map(Path::of)
                .filter(metafilePath -> metafilePath.getFileName().toString().equals("executor.conf"))
                .map(Path::toFile)
                .findFirst();
    }

    private void assertInitialized() {
        if (!initialized)
            throw new IllegalStateException("Executor loader has not been initialized!");
    }

}
