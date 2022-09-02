package com.maveric.userservice.exception;

import org.springframework.stereotype.Component;

@Component
public class GetUserByIdException extends RuntimeException{

    private  String message;

    public GetUserByIdException(){

    }

    public GetUserByIdException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }
}
