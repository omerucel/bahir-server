package bahir.server.form;

import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FormAbstractTest {
    public FormAbstract form;

    @Before
    public void setUp()
    {
        form = Mockito.spy(new FormAbstract() {});
    }

    @Test
    public void addError()
    {
        assertFalse(form.hasError());
        form.addError(new ValidationError("error", "errorType"));
        assertTrue(form.hasError());
    }

    @Test
    public void hasError()
    {
        form.addError(new ValidationError("error", "error-type"));
        assertTrue(form.hasError());
        assertTrue(form.hasError("error"));
        assertTrue(form.hasError("error", "error-type"));
    }

    @Test
    public void getErrors()
    {
        form.getErrors().add(new ValidationError("error", "error-type"));
        assertFalse(form.hasError());
    }

    @Test
    public void getValidData()
    {
        form.addValidData("email", "test@test.com");
        assertEquals(1, form.getValidData().size());

        form.getValidData().put("test", "test");
        assertEquals(1, form.getValidData().size());

        assertEquals("test@test.com", form.getValidData("email"));
    }
}
