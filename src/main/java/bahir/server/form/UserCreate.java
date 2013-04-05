package bahir.server.form;

import bahir.server.model.User;
import java.util.HashMap;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserCreate extends FormAbstract{
    private Session databaseSession;

    public UserCreate(Session dataSession)
    {
        this.databaseSession = dataSession;
    }

    @Override
    public void validate(HashMap<Object, Object> data) {

        // email parametresi gerekiyor.
        if (!data.containsKey("email"))
        {
            addError(new ValidationError("email", "required"));
        }else{
            String email = data.get("email").toString().toLowerCase();
            // email doğrulanmalı.
            if (!EmailValidator.getInstance().isValid(data.get("email").toString()))
                addError(new ValidationError("email", "invalid"));

            // Email tekil olmalı.
            int count = databaseSession.createCriteria(User.class)
                .add(Restrictions.eq("email", email))
                .list()
                .size();
            if (count > 0)
                addError(new ValidationError("email", "unique"));
        }

        // username parametresi gerekiyor.
        if (!data.containsKey("username"))
        {
            addError(new ValidationError("username", "required"));
        }else{
            // username doğrulanmalı.
            String username = data.get("username").toString().toLowerCase().trim();
            if (username.length() < 4)
                addError(new ValidationError("username", "min-length"));
            if (username.length() > 16)
                addError(new ValidationError("username", "max-length"));

            // username tekil olmalı.
            int count = databaseSession.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .list()
                    .size();
            if (count > 0)
                addError(new ValidationError("username", "unique"));
        }

        // password gerekli.
        if (!data.containsKey("password"))
        {
            addError(new ValidationError("password", "required"));
        }else{
            // Parola doğrulanmalı.
            String password = data.get("password").toString().trim();
            if (password.length() < 8)
                addError(new ValidationError("password", "invalid"));
        }

        if (!hasError())
        {
            addValidData("email", data.get("email"));
            addValidData("username", data.get("username"));
            addValidData("password", data.get("password"));
        }
    }
}
