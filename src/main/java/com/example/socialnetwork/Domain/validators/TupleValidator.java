package com.example.socialnetwork.Domain.validators;


import com.example.socialnetwork.Domain.Tuple;

public class TupleValidator implements Validator<Tuple> {
    public TupleValidator(){}
    @Override
    public void validate(Tuple entity)throws ValidationException{
        if(entity.getLeft()==entity.getRight())
            throw new ValidationException("Left entity can't be the same with first one!");
    }
}
