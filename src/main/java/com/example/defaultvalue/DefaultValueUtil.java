package com.example.defaultvalue;

import com.example.defaultvalue.config.DefaultValueProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class DefaultValueUtil {
    private DefaultValueProperties defaultValueProperties;

    public DefaultValueUtil(DefaultValueProperties defaultValueProperties){
        this.defaultValueProperties = defaultValueProperties;
    }

    public void setDefaultValues(List objs, String propertyPrefix, boolean override) throws Exception {
        if (!CollectionUtils.isEmpty(objs)) {
            Map<String, Object> defaultValues = this.getDefaultValues(objs.get(0), propertyPrefix);

            for (Object obj : objs) {
                for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {
                    String fieldName = entry.getKey();
                    Object defaultValue = entry.getValue();

                    try {
                        if (!override) {
                            Object fieldValue = FieldUtils.readDeclaredField(obj, fieldName, true);
                            if (fieldValue != null) {
                                continue;
                            }
                        }
                        FieldUtils.writeDeclaredField(obj, fieldName, defaultValue, true);
                    } catch (Exception e) {
                        log.error("Cannot set default value '" + defaultValue + "' to the field " + obj.getClass().getName() + "." + fieldName);
                        throw e;
                    }
                }
            }
        }
    }

    public void setDefaultValues(Object obj, String propertyPrefix, boolean override) throws Exception {
        if (obj != null) {
            List<Object> param = new ArrayList<>();
            param.add(obj);
            this.setDefaultValues(param, propertyPrefix, override);
        }
    }

    private Map<String, Object> getDefaultValues(Object obj, String propertyPrefix) {
        Map<String, Object> defaultValues = new HashMap<>();

        Map<String, Class> fieldTypes = this.getFieldTypes(obj);
        Map<String, String> properties = defaultValueProperties.getProperties(propertyPrefix);

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String fieldName = entry.getKey();
            String defaultValue = entry.getValue();

            defaultValues.put(fieldName, ConvertUtils.convert(defaultValue, fieldTypes.get(fieldName)));
        }

        return defaultValues;
    }


    private Map<String, Class> getFieldTypes(Object obj) {
        Map<String, Class> fieldTypes = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            fieldTypes.put(field.getName(), field.getType());
        }

        return fieldTypes;
    }

}
