package com.ets.fileStorageSystem.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    @SneakyThrows(IOException.class)
    @ExceptionHandler(MultipartException.class)
    void handleMultipartException(MultipartException ex,HttpServletResponse response)  {
        response.sendError(HttpStatus.BAD_REQUEST.value(),"Please select a file");
    }
    @SneakyThrows(IOException.class)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException ex,HttpServletResponse response) {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
