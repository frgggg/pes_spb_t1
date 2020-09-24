package com.pes.spb.t1.controller;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.exception.TestServiceExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionController {

    public static final String NOT_IMPLEMENTED_MSG = "not implemented";
    public static final String VALIDATION_PROBLEMS_PREFIX = "Validation problems: ";
    public static final String VALIDATION_PROBLEM_PREFIX = "\n";

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
        return new ResponseEntity<>(NOT_IMPLEMENTED_MSG, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<StringBuilder> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        StringBuilder message = new StringBuilder(VALIDATION_PROBLEMS_PREFIX);
        for(ObjectError objectError: ex.getBindingResult().getAllErrors()) {
            message.append(VALIDATION_PROBLEM_PREFIX);
            message.append(objectError.getDefaultMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(TestServiceException.class)
    protected ResponseEntity<StringBuilder> testServiceExceptionHandler(TestServiceException ex, WebRequest request) {
        StringBuilder msg = new StringBuilder(ex.getMessage());
        HttpStatus status = (ex.getType().equals(TestServiceExceptionType.QUERY_ERROR))?
                HttpStatus.BAD_REQUEST:
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(msg, status);

    }
}
