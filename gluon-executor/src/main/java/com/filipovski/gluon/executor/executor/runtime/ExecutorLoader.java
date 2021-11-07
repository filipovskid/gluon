package com.filipovski.gluon.executor.executor.runtime;

import com.filipovski.gluon.executor.configuration.ExecutorConfigOptions;
import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.executor.Executor;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class for loading {@link Executor} plugins and their configuration. The main responsibility
 * is to construct executors on demand.
 *
 * <p>Executor creation is enabled by building and maintaining {@link ExecutorSpec}s for
 * each configured executor.</p>
 */

public class ExecutorLoader {

    private final Logger logger = LogManager.getLogger(ExecutorLoader.class);

    private final PluginManager pluginManager;

    private Map<String, ExecutorSpec> executorSpecs;

    private boolean initialized;

    public ExecutorLoader(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.executorSpecs = Collections.emptyMap();
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
        return List.copyOf(executorSpecs.values());
    }

    public void load() {
        executorSpecs = pluginManager.getPluginDescriptors().stream()
                .flatMap(descriptor -> buildExecutorSpecs(descriptor).stream())
                .collect(Collectors.toMap(ExecutorSpec::getName, Function.identity(),
                        this::executorNamingConflictHandler));
    }

    public Optional<Executor> createExecutor(String executorName, ExecutionEnvironment environment) {
        assertInitialized();

        return Optional.ofNullable(executorSpecs.get(executorName))
                .map(executorSpec ->
                        executorSpec.getExecutorProvider().create(environment, executorSpec.getConfiguration())
                ).or(Optional::empty);
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
                .map(c -> createExecutorSpec(pluginDescriptor, provider, c))
                .collect(Collectors.toList());
    }

    private ExecutorSpec createExecutorSpec(PluginDescriptor pluginDescriptor,
                                            ExecutorProvider provider,
                                            Config config) {
        String executorName = config.getString(ExecutorConfigOptions.EXECUTOR_NAME);
        logger.info("Executor [{}] from plugin [{}] registered!", executorName, pluginDescriptor.getPluginId());

        return new ExecutorSpec(executorName, pluginDescriptor, provider, config);
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

    private ExecutorSpec executorNamingConflictHandler(ExecutorSpec existing, ExecutorSpec replacement) {
        String existingPluginId = existing.getPluginDescriptor().getPluginId();
        String replacementPluginId = replacement.getPluginDescriptor().getPluginId();

        logger.warn("Conflicting [{}] executor configuration, for plugins [{}] and [{}], was loaded. " +
                "Retaining [{}] plugin configuration.",
                existing.getName(), existingPluginId, replacementPluginId, existingPluginId);

        return existing;
    }

    private void assertInitialized() {
        if (!initialized)
            throw new IllegalStateException("Executor loader has not been initialized!");
    }

}
