package bahir.server.form;

public class ValidationError {
    String fieldName;
    String errorType;

    public ValidationError(String fieldName, String errorType)
    {
        this.fieldName = fieldName;
        this.errorType = errorType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorType() {
        return errorType;
    }
}
