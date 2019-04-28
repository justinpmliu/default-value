package com.example.defaultvalue.util;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReflectionUtil {

    @Cacheable(value="fieldTypeCache")
    public Map<String, Class> getFieldTypes(Class aClass) {
        Map<String, Class> result = new HashMap<>();

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            result.put(field.getName(), field.getType());
        }

        return result;
    }
}
