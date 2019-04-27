package com.example.defaultvalue.config;

import com.example.defaultvalue.mapper.DefaultValueMapper;
import com.example.defaultvalue.model.DefaultValue;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
public class DefaultValueConfig {

    private Map<String, Map<String, String>> defaultValueMap;

    public DefaultValueConfig (DefaultValueMapper defaultValueMapper) {
        this.defaultValueMap = this.getDefaultValueMap(defaultValueMapper);
    }

    public Map<String, String> getFieldValues(String service, String clazz) {
        return defaultValueMap.get(this.getPrefix(service, clazz));
    }

    private Map<String, Map<String, String>> getDefaultValueMap(DefaultValueMapper defaultValueMapper) {
        List<DefaultValue> defaultValueList = defaultValueMapper.findAll();

        Map<String, Map<String, String>> result = new HashMap<>();

        for (DefaultValue defaultValue : defaultValueList) {
            String prefix = this.getPrefix(defaultValue.getService(), defaultValue.getClazz());

            result.computeIfAbsent(prefix, k -> new HashMap<>());
            result.get(prefix).put(defaultValue.getField(), defaultValue.getValue());

        }

        return result;
    }

    private String getPrefix(String service, String clazz) {
        return service + "." + clazz;
    }
}
