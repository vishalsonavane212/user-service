package com.maveric.userservice.exception;

import org.springframework.stereotype.Component;

@Component
public class UserException extends RuntimeException{

    private  String message;

    public UserException(){

    }

    public UserException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

}
