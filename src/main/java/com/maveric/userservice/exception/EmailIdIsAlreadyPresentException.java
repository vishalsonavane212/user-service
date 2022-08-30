package com.maveric.userservice.exception;

import org.springframework.stereotype.Component;

@Component
public class EmailIdIsAlreadyPresentException extends RuntimeException{
     String errorMessage;
    public EmailIdIsAlreadyPresentException(String message) {
        super(message);
        this.errorMessage=message;

    }

    public EmailIdIsAlreadyPresentException() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
