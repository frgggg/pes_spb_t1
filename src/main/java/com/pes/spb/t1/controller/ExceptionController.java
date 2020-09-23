package com.pes.spb.t1.controller;

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
    protected ResponseEntity<StringBuffer> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        StringBuffer message = new StringBuffer("Validation problems: ");
        for(ObjectError objectError: ex.getBindingResult().getAllErrors()) {
            message.append("\n");
            message.append(objectError.getDefaultMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }
}
