package bahir.server.form;

import bahir.DatabaseTestCase;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserCreateTest extends DatabaseTestCase{
    private UserCreate form;
    private HashMap<Object, Object> data;

    @Before
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        form = new UserCreate(sessionFactory.openSession());
        data = new HashMap<Object, Object>();
    }

    @Test
    public void instance()
    {
        assertTrue(form instanceof FormAbstract);
    }

    @Test
    public void emailRequired()
    {
        form.validate(data);
        assertTrue(form.hasError("email"));
        assertTrue(form.hasError("email", "required"));
    }

    @Test
    public void emailUnique()
    {
        data.put("email", "test1@test.com");
        form.validate(data);
        assertTrue(form.hasError("email", "unique"));
    }

    @Test
    public void emailInvalid()
    {
        data.put("email", "test");
        form.validate(data);
        assertTrue(form.hasError("email"));
        assertTrue(form.hasError("email", "invalid"));
    }

    @Test
    public void usernameRequired()
    {
        form.validate(data);
        assertTrue(form.hasError("username"));
        assertTrue(form.hasError("username", "required"));
    }

    @Test
    public void usernameTooShort()
    {
        data.put("username", "ab");
        form.validate(data);
        assertTrue(form.hasError("username"));
        assertTrue(form.hasError("username", "min-length"));
    }

    @Test
    public void usernameTooLong()
    {
        data.put("username", "12345678901234566");
        form.validate(data);
        assertTrue(form.hasError("username"));
        assertTrue(form.hasError("username", "max-length"));
    }

    @Test
    public void usernameUnique()
    {
        data.put("username", "username1");
        form.validate(data);
        assertTrue(form.hasError("username", "unique"));
    }

    @Test
    public void passwordRequired()
    {
        form.validate(data);
        assertTrue(form.hasError("password", "required"));
    }

    @Test
    public void passwordInvalid()
    {
        data.put("password", "1234567");
        form.validate(data);
        assertTrue(form.hasError("password", "invalid"));
    }

    @Test
    public void valid()
    {
        data.put("email", "test@test.com");
        data.put("username", "test");
        data.put("password", "password1");
        form.validate(data);
        assertFalse(form.hasError());
        assertEquals("test@test.com", form.getValidData("email"));
        assertEquals("test", form.getValidData("username"));
        assertEquals("password1", form.getValidData("password"));
    }
}
