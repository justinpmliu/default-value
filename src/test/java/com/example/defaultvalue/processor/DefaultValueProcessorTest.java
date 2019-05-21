package com.example.defaultvalue.processor;

import com.example.defaultvalue.mapper.DefaultValueMapper;
import com.example.defaultvalue.model.Address;
import com.example.defaultvalue.model.DefaultValue;
import com.example.defaultvalue.model.User;
import com.example.defaultvalue.service.DefaultValueService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultValueProcessorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private DefaultValueMapper defaultValueMapper;

    private DefaultValueProcessor defaultValueProcessor;

    private User user;

    @Before
    public void setUp() {
        defaultValueProcessor = new DefaultValueProcessor(new DefaultValueService(defaultValueMapper));

        user = new User(12345678);
        user.setAddresses(Arrays.asList(
                new Address("GZ", "TianHe", 123, true),
                new Address("FS", "NanHai", null, null)
        ));

        Map<String, DefaultValue> userDefaultValueMap = new HashMap<>();
        userDefaultValueMap.put("name", new DefaultValue(1, "s1", "user", "name", "New User"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user")).willReturn(userDefaultValueMap);

        Map<String, DefaultValue> addressDefaultValueMap = new HashMap<>();
        addressDefaultValueMap.put("room", new DefaultValue(1, "s1", "user.address", "room", "999"));
        addressDefaultValueMap.put("primary", new DefaultValue(2, "s1", "user.address", "primary", "false"));

        given(defaultValueMapper.findByServiceAndClazz("s1", "user.address")).willReturn(addressDefaultValueMap);

    }


    @Test
    public void testSetDefaultValuesWithoutUserName() throws Exception {

        defaultValueProcessor.setDefaultValues(user, "s1", "user", false);

        assertEquals("New User", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithUserNameAndNotOverride() throws Exception {

        user.setName("Justin");

        defaultValueProcessor.setDefaultValues(user, "s1","user", false);

        assertEquals("Justin", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithUserNameAndOverride() throws Exception {

        user.setName("Justin");

        defaultValueProcessor.setDefaultValues(user, "s1","user", true);

        assertEquals("New User", user.getName());
    }

    @Test
    public void testSetDefaultValuesWithoutUserNameAndNoDefaultValue() throws Exception {

        defaultValueProcessor.setDefaultValues(user, "s3", "user", false);

        assertNull(user.getName());
    }

    @Test
    public void testSetDefaultValuesWithAddressAndNotOverride() throws Exception {

        defaultValueProcessor.setDefaultValues(user, "s1","user", false);

        Address address = user.getAddresses().get(0);

        assertEquals(123, address.getRoom().intValue());
        assertTrue(address.getPrimary());
    }

    @Test
    public void testSetDefaultValuesWithAddressAndOverride() throws Exception {

        defaultValueProcessor.setDefaultValues(user, "s1","user", true);

        Address address = user.getAddresses().get(0);

        assertEquals(999, address.getRoom().intValue());
        assertFalse(address.getPrimary());
    }

    @Test
    public void testSetDefaultValuesWithoutAddressRoomAndPrimary() throws Exception {

        defaultValueProcessor.setDefaultValues(user, "s1","user", false);

        Address address = user.getAddresses().get(1);

        assertEquals(999, address.getRoom().intValue());
        assertFalse(address.getPrimary());
    }

}