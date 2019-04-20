package com.example.defaultvalue.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Log4j2
@Configuration
public class DefaultValueProperties {
    private Properties prop = new Properties();

    public DefaultValueProperties() throws IOException {
        try (InputStream in = DefaultValueProperties.class.getClassLoader().getResourceAsStream("default-value.properties")) {
            if (in != null) {
                prop.load(in);
            } else {
                log.error("Unable to find config file!");
            }
        }
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public Map<String, String> getProperties(String prefix) {
        Map<String, String> result = new HashMap<>();
        Set<String> propertyNames = prop.stringPropertyNames();

        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(prefix)) {
                String fieldName = propertyName.substring(prefix.length() + 1);
                if (!fieldName.contains(".")) {
                    result.put(fieldName, this.getProperty(propertyName));
                }
            }
        }

        return result;
    }
}
