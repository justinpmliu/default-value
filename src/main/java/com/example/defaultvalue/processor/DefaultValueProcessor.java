package com.example.defaultvalue.processor;

import com.example.defaultvalue.service.DefaultValueService;
import com.example.defaultvalue.util.ReflectionUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class DefaultValueProcessor {

    private DefaultValueService defaultValueService;

    private ReflectionUtil reflectionUtil;

    public DefaultValueProcessor(DefaultValueService defaultValueService, ReflectionUtil reflectionUtil) {
        this.defaultValueService = defaultValueService;
        this.reflectionUtil = reflectionUtil;
    }

    public void setDefaultValues(List objs, String service, String clazz, boolean override) throws IllegalAccessException {

        Map<String, Object> defaultValues = this.getDefaultValues(objs.get(0), service, clazz);

        for (Object obj : objs) {
            for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                try {
                    if (!override) {
                        Object fieldValue = FieldUtils.readDeclaredField(obj, field, true);
                        if (fieldValue != null) {
                            continue;
                        }
                    }
                    FieldUtils.writeDeclaredField(obj, field, value, true);
                } catch (Exception e) {
                    log.error("Cannot set default value '" + value + "' to the field " + obj.getClass().getName() + "." + field);
                    throw e;
                }
            }
        }

    }

    public void setDefaultValues(Object obj, String service, String clazz, boolean override) throws IllegalAccessException {
        List<Object> param = new ArrayList<>();
        param.add(obj);
        this.setDefaultValues(param, service, clazz, override);

    }

    private Map<String, Object> getDefaultValues(Object obj, String service, String clazz) {
        Map<String, Object> result = new HashMap<>();

        Map<String, String> fieldValues = defaultValueService.getFieldValues(service, clazz);
        Map<String, Class> fieldTypes = reflectionUtil.getFieldTypes(obj.getClass());

        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            String field = entry.getKey();
            result.put(field, ConvertUtils.convert(entry.getValue(), fieldTypes.get(field)));
        }

        return result;
    }

}
