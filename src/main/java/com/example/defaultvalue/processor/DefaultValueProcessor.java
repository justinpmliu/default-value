package com.example.defaultvalue.processor;

import com.example.defaultvalue.service.DefaultValueService;
import com.example.defaultvalue.util.ReflectionUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class DefaultValueProcessor {

    private final DefaultValueService defaultValueService;

    private final ReflectionUtil reflectionUtil;

    public DefaultValueProcessor(DefaultValueService defaultValueService, ReflectionUtil reflectionUtil) {
        this.defaultValueService = defaultValueService;
        this.reflectionUtil = reflectionUtil;
    }

    public void setDefaultValues(List objs, String service, String clazz, boolean override) throws IllegalAccessException {
        log.info(String.format("Set default values for list, service = %s, clazz = %s", service, clazz));

        Map<String, Object> defaultValues = this.getDefaultValues(objs.get(0), service, clazz);
        for (Object obj : objs) {
            this.setDefaultValues(obj, defaultValues, override);
        }
    }

    public void setDefaultValues(Object obj, String service, String clazz, boolean override) throws IllegalAccessException, NoSuchFieldException {
        log.info(String.format("Set default values for object, service = %s, clazz = %s", service, clazz));

        // handle the non List fields
        Map<String, Object> defaultValues = this.getDefaultValues(obj, service, clazz);
        this.setDefaultValues(obj, defaultValues, override);

        // handle the List fields
        Map<String, Class> fieldTypes = reflectionUtil.getFieldTypes(obj.getClass());

        for (Map.Entry<String, Class> entry : fieldTypes.entrySet()) {
            String fieldName = entry.getKey();
            Class fieldType = entry.getValue();

            if (fieldType == List.class) {
                // get the actual type of a List<T> field
                Field field = obj.getClass().getDeclaredField(fieldName);
                Type genericType = field.getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] types = parameterizedType.getActualTypeArguments();

                    // prepare parameters to call this.setDefaultValues(List objs, ...)
                    String clazzName = (obj.getClass().getSimpleName() + "." + this.getClassName(types[0])).toLowerCase();
                    List objs = (List) FieldUtils.readDeclaredField(obj, fieldName, true);

                    this.setDefaultValues(objs, service, clazzName, override);
                }
            }
        }
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

    private void setDefaultValues(Object obj, Map<String, Object> defaultValues, boolean override) throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            if (!override) {
                Object fieldValue = FieldUtils.readDeclaredField(obj, field, true);
                if (fieldValue != null) {
                    continue;
                }
            }
            FieldUtils.writeDeclaredField(obj, field, value, true);
        }

    }

    private String getClassName(Type type) {
        String typeName = type.getTypeName();
        int pos = typeName.lastIndexOf('.');

        return typeName.substring(pos + 1);
    }
}
