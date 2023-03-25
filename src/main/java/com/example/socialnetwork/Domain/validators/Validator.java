package com.example.socialnetwork.Domain.validators;

public interface Validator<T> {
    void validate(T entity)throws ValidationException;
}
