package com.example.defaultvalue;

import com.example.defaultvalue.model.Address;
import com.example.defaultvalue.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultValueUtilTest {

    @Autowired
    private DefaultValueUtil defaultValueUtil;

    private User user;

    @Before
    public void setUp() {
        user = new User("Justin");
    }


    @Test
    public void setDefaultValuesTestWithoutUserId() throws Exception {

        defaultValueUtil.setDefaultValues(user, "user", false);

        assertEquals(99999, user.getId().intValue());
    }

    @Test
    public void setDefaultValuesTestWithUserIdAndNotOverride() throws Exception {

        user.setId(12345);

        defaultValueUtil.setDefaultValues(user, "user", false);

        assertEquals(12345, user.getId().intValue());
    }

    @Test
    public void setDefaultValuesTestWithUserIdAndOverride() throws Exception {

        user.setId(12345);

        defaultValueUtil.setDefaultValues(user, "user", true);

        assertEquals(99999, user.getId().intValue());
    }

    @Test
    public void setDefaultValuesTestWithAddressAndNotOverride() throws Exception {

        Address address1 = new Address("GZ", "TianHe");
        address1.setRoom(123);
        address1.setPrimary(true);

        Address address2 = new Address("FS", "NanHai");

        user.setAddresses(Arrays.asList(address1, address2));

        defaultValueUtil.setDefaultValues(user.getAddresses(), "user.address", false);

        address1 = user.getAddresses().get(0);
        address2 = user.getAddresses().get(1);

        assertEquals(123, address1.getRoom().intValue());
        assertTrue(address1.getPrimary());

        assertEquals(999, address2.getRoom().intValue());
        assertFalse(address2.getPrimary());
    }

    @Test
    public void setDefaultValuesTestWithAddressAndOverride() throws Exception {

        Address address1 = new Address("GZ", "TianHe");
        address1.setRoom(123);
        address1.setPrimary(true);

        Address address2 = new Address("FS", "NanHai");

        user.setAddresses(Arrays.asList(address1, address2));

        defaultValueUtil.setDefaultValues(user.getAddresses(), "user.address", true);

        address1 = user.getAddresses().get(0);
        address2 = user.getAddresses().get(1);

        assertEquals(999, address1.getRoom().intValue());
        assertFalse(address1.getPrimary());

        assertEquals(999, address2.getRoom().intValue());
        assertFalse(address2.getPrimary());
    }

}