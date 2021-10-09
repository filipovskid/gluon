package com.filipovski.gluonserver.configuration.source;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigResolveOptions;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

public class TypesafePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Config config = ConfigFactory
                .load(resource.getResource().getFilename(),
                        ConfigParseOptions.defaults().setAllowMissing(false),
                        ConfigResolveOptions.noSystem()).resolve();
        String sourceName = name == null ? "typesafe" : name;

        return new TypesafePropertySource(sourceName, config);
    }
}
