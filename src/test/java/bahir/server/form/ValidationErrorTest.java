package bahir.server.form;

import org.junit.Assert;
import org.junit.Test;

public class ValidationErrorTest {
    @Test
    public void getters()
    {
        ValidationError validationError = new ValidationError("field", "error-type");
        Assert.assertEquals("field", validationError.getFieldName());
        Assert.assertEquals("error-type", validationError.getErrorType());
    }
}
