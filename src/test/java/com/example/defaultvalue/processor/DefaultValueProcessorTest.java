package com.example.defaultvalue.processor;

import com.example.defaultvalue.mapper.DefaultValueMapper;
import com.example.defaultvalue.model.Address;
import com.example.defaultvalue.model.DefaultValue;
import com.example.defaultvalue.model.User;
import com.example.defaultvalue.service.DefaultValueService;
import com.example.defaultvalue.util.ReflectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultValueProcessorTest {

    @Mock
    private DefaultValueMapper defaultValueMapper;

    private DefaultValueProcessor defaultValueProcessor;

    private Map<String, DefaultValue> defaultValueMap;

    private User user;

    @Before
    public void setUp() {
        defaultValueProcessor = new DefaultValueProcessor(
                new DefaultValueService(defaultValueMapper), new ReflectionUtil());

        defaultValueMap = new HashMap<>();

        user = new User(34022999);

        Address address1 = new Address("GZ", "TianHe", 123, true);
        Address address2 = new Address("FS", "NanHai", null, null);

        user.setAddresses(Arrays.asList(address1, address2));
    }


    @Test
    public void testSetDefaultValuesWithoutUserName() throws Exception {

        defaultValueMap.put("name", new DefaultValue(1, "s1", "user", "name", "New User"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user, "s1", "user", false);

        assertEquals("New User", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithUserNameAndNotOverride() throws Exception {

        user.setName("Justin");

        defaultValueMap.put("name", new DefaultValue(1, "s1", "user", "name", "New User"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user, "s1","user", false);

        assertEquals("Justin", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithUserNameAndOverride() throws Exception {

        user.setName("Justin");

        defaultValueMap.put("name", new DefaultValue(1, "s1", "user", "name", "New User"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user, "s1","user", true);

        assertEquals("New User", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithoutUserNameAndNoDefaultValue() throws Exception {

        given(defaultValueMapper.findByServiceAndClazz("s3", "user")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user, "s3", "user", false);

        assertNull(user.getName());
    }

    @Test
    public void testSetDefaultValuesWithAddressAndNotOverride() throws Exception {

        defaultValueMap.put("room", new DefaultValue(1, "s1", "user.address", "room", "999"));
        defaultValueMap.put("primary", new DefaultValue(2, "s1", "user.address", "primary", "false"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user.address")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user.getAddresses(), "s1","user.address", false);

        Address address = user.getAddresses().get(0);

        assertEquals(123, address.getRoom().intValue());
        assertTrue(address.getPrimary());
    }

    @Test
    public void testSetDefaultValuesWithAddressAndOverride() throws Exception {

        defaultValueMap.put("room", new DefaultValue(1, "s1", "user.address", "room", "999"));
        defaultValueMap.put("primary", new DefaultValue(2, "s1", "user.address", "primary", "false"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user.address")).willReturn(defaultValueMap);

        defaultValueProcessor.setDefaultValues(user.getAddresses(), "s1","user.address", true);

        Address address = user.getAddresses().get(0);

        assertEquals(999, address.getRoom().intValue());
        assertFalse(address.getPrimary());
    }

}