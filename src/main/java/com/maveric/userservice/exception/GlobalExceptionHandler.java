package com.maveric.userservice.exception;

import com.maveric.userservice.utils.UserServiceConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
            errors.put(UserServiceConstant.error_message, errorMessage);
        });
        return new ResponseEntity(errors,status );
    }

    @ExceptionHandler(CreateUserException.class)
    @ResponseBody
    public  Map<String,String> createUserException(CreateUserException ex, WebRequest request){
        Map<String ,String > error=new HashMap();
        error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        error.put(UserServiceConstant.error_message,UserServiceConstant.USER_NOT_CREATED);
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorDetails handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return new ErrorDetails(HttpStatus.BAD_REQUEST,ex.getMessage(), ex.getConstraintViolations().toString());
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                UserServiceConstant.ACCESS_DENIED, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorDetails handleExceptions(Exception ex, WebRequest request) {
        return new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(), "Exception");
    }

}
