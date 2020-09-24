package com.pes.spb.t1.controller;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.exception.TestServiceExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<StringBuilder> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        StringBuilder message = new StringBuilder("Validation problems: ");
        for(ObjectError objectError: ex.getBindingResult().getAllErrors()) {
            message.append("\n");
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
