package iss.validation;

import iss.exceptions.ValidationException;
import iss.model.User;

public class UserValidator implements IValidator<Long, User>{
    @Override
    public void validate(User user) {
        if (user == null){
            throw new ValidationException("User is null");
        }

        if (user.getUsername() == null){
            throw new ValidationException("Username is null");
        }

        if (user.getPassword() == null){
            throw new ValidationException("Password is null");
        }

        if (user.getUserType() == null){
            throw new ValidationException("User type is null");
        }

        if (user.getUsername().length() < 3){
            throw new ValidationException("Username should be at least 3 characters");
        }

        if (user.getPassword().length() < 5){
            throw new ValidationException("Password should be at least 5 characters");
        }
    }
}
