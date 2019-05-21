package com.example.defaultvalue.service;

import com.example.defaultvalue.mapper.DefaultValueMapper;
import com.example.defaultvalue.model.DefaultValue;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class DefaultValueService {

    private DefaultValueMapper defaultValueMapper;

    public DefaultValueService(DefaultValueMapper defaultValueMapper) {
        this.defaultValueMapper = defaultValueMapper;
    }

    @Cacheable(value="defaultValueCache", key="#service + '.' + #clazz")
    public Map<String, String> getFieldValues(String service, String clazz) {
        Map<String, String> result = new HashMap<>();
        Map<String, DefaultValue> defaultValueMap = defaultValueMapper.findByServiceAndClazz(service, clazz);

        for (Map.Entry<String, DefaultValue> entry : defaultValueMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }

        return result;
    }

    @CacheEvict(value="defaultValueCache", allEntries = true)
    public void evictDefaultValueCache(){}

}
