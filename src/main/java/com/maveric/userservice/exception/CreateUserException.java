package com.maveric.userservice.exception;

import org.springframework.stereotype.Component;

@Component
public class CreateUserException extends RuntimeException{

    private  String message;

    public CreateUserException(){

    }

    public CreateUserException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

}
