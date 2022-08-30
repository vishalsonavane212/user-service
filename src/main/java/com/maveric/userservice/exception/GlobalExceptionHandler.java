package com.maveric.userservice.exception;

import com.maveric.userservice.utils.UserServiceConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            //String fieldName = ((FieldError) error).getField();
            String fieldName = "400";
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors,status );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorDetails handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST,ex.getMessage(), ex.getConstraintViolations().toString());
        return errorDetails;
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                UserServiceConstant.access_denied_message_here, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmailIdIsAlreadyPresentException.class)
    @ResponseBody
    public ErrorDetails emailIdIsAlreadyPresent(EmailIdIsAlreadyPresentException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrorMessage());
        return errorDetails;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorDetails handleExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(), "Exception");
        return errorDetails;
    }

}
