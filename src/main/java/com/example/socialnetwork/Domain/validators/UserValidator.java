package com.example.socialnetwork.Domain.validators;


import com.example.socialnetwork.Domain.User;

public class UserValidator implements Validator<User>{
    public UserValidator(){}
    @Override
    public void validate(User entity)throws ValidationException{
        if(entity.getID()<0)
            throw new ValidationException("ID can't be neggative!");
        if(entity.getFirstName()=="")
            throw new ValidationException("First name can't pe empty!");
        if(entity.getLastName()=="")
            throw new ValidationException("Last name can't pe empty!");
    }
}
