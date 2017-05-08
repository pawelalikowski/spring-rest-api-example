package com.example.exceptions;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public RestException(RestError errorCodes) {
        super(errorCodes.getMessage());
        this.code = errorCodes.name();
        this.httpStatus = errorCodes.getHttpStatus();
    }

    public RestException(RestError errorCodes, String message) {
        super(message);
        this.code = errorCodes.name();
        this.httpStatus = errorCodes.getHttpStatus();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
