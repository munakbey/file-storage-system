package com.ets.fileStorageSystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleMyApplicationException(AppException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}