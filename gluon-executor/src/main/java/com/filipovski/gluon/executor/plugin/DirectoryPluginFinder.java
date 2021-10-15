package com.filipovski.gluon.executor.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements a directory based plugin location strategy.
 *
 * <p>Plugins are located within a root plugin directory, representing each plugin
 * as its own directory. The plugin directory contains plugin resources (jar files)
 * and plugin metadata files. Each found plugin is described with a {@link PluginDescriptor}
 * where the plugin id is taken to be the plugin directory, the jar files are plugin
 * resources and any remaining files are taken to be metadata files.</p>
 */

public class DirectoryPluginFinder implements PluginFinder {

    private final String JAR_RESOURCE_PATTERN = "glob:**.jar";

    private final Logger logger = LogManager.getLogger(DirectoryPluginFinder.class);

    private final PathMatcher resourceMatcher;

    private final Path pluginsRootDir;

    public DirectoryPluginFinder(Path pluginsRootDir) {
        this.pluginsRootDir = pluginsRootDir;
        this.resourceMatcher = pluginsRootDir.getFileSystem().getPathMatcher(JAR_RESOURCE_PATTERN);
    }

    @Override
    public List<PluginDescriptor> findPlugins() {
        if (!Files.isDirectory(pluginsRootDir)) {
            logger.warn("Plugins root directory [{}] is not available!", pluginsRootDir);
            return Collections.emptyList();
        }

        try (Stream<Path> pluginDirs = Files.list(pluginsRootDir)) {
            return pluginDirs
                    .filter(Files::isDirectory)
                    .map(this::createPluginDescriptorFromPluginDirectory)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read plugins from [{}]. [{}]", pluginsRootDir, e);

            return Collections.emptyList();
        }
    }

    private Optional<PluginDescriptor> createPluginDescriptorFromPluginDirectory(Path pluginDirectory) {
        URL[] resourceURLs = obtainPluginResources(pluginDirectory);
        URL[] metafileURLs = obtainPluginMetafiles(pluginDirectory);

        if (resourceURLs.length < 1) {
            logger.warn("Could not find/access plugin [{}] resources.", pluginDirectory);
            return Optional.empty();
        }

        return Optional.of(PluginDescriptor.create(pluginDirectory.getFileName().toString(), resourceURLs, metafileURLs));
    }

    private URL[] obtainPluginResources(Path pluginDirectory) {
        return obtainResourceURLs(pluginDirectory, resourceMatcher::matches);
    }

    private URL[] obtainPluginMetafiles(Path pluginDirectory) {
        return obtainResourceURLs(pluginDirectory, path -> !resourceMatcher.matches(path));
    }

    private URL[] obtainResourceURLs(Path pluginDirectory, Predicate<Path> filter) {
        try (Stream<Path> pluginPaths = Files.list(pluginDirectory)) {
            return pluginPaths.filter(Files::isRegularFile)
                    .filter(filter)
                    .map(path -> {
                        try {
                            return path.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(URL[]::new);
        } catch (IOException e) {
            logger.error("Unable to access plugin [{}] resources. [{}]", pluginDirectory, e);
        }

        return new URL[0];
    }
}
