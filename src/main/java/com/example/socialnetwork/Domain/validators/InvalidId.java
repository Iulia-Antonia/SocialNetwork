package com.example.socialnetwork.Domain.validators;

public class InvalidId extends Exception{

    InvalidId(){
    }
    public InvalidId(String message){
        super(message);
    }

}
