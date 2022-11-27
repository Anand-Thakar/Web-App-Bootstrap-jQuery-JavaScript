package com.example.demo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
@Data
public class BlogApiException extends  RuntimeException {

    private HttpStatus status;
    private String message;

    public BlogApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BlogApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}
