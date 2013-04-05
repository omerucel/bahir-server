package bahir.server.form;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class FormAbstract {
    private ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
    private HashMap<Object, Object> validData = new HashMap<Object, Object>();

    public Boolean hasError()
    {
        return errors.size() > 0;
    }

    public Boolean hasError(String field)
    {
        for(ValidationError error : errors)
            if (error.getFieldName().equals(field))
                return true;

        return false;
    }

    public Boolean hasError(String field, String errorType)
    {
        for(ValidationError error : errors)
            if (error.getFieldName().equals(field) 
                    && error.getErrorType().equals(errorType))
                return true;

        return false;
    }

    public ArrayList<ValidationError> getErrors()
    {
        return (ArrayList<ValidationError>) errors.clone();
    }

    public HashMap<Object, Object> getValidData()
    {
        return (HashMap<Object, Object>) validData.clone();
    }

    public Object getValidData(String key)
    {
        return validData.get(key);
    }

    public void addError(ValidationError error)
    {
        errors.add(error);
    }

    public void addValidData(String key, Object value)
    {
        validData.put(key, value);
    }

    public void validate(HashMap<Object, Object> data)
    {}
}
