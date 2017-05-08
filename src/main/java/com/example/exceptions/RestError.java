package com.example.exceptions;

import org.springframework.http.HttpStatus;

public enum RestError {
    NonExistentUser("User does not exists.", HttpStatus.BAD_REQUEST),
    EmailTaken("Email already exists in database.", HttpStatus.BAD_REQUEST),
    EmailNotFound("Email not found in database", HttpStatus.BAD_REQUEST),
    IllegalTokenStatus("Illegal token status", HttpStatus.BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;

    RestError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
