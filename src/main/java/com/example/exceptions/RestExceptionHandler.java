package com.example.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ RestException.class })
    protected ResponseEntity<Object> handleRestException(RuntimeException e, WebRequest request) {
        RestException re = (RestException) e;
        ErrorResource error = new ErrorResource(re.getHttpStatus(), re.getCode(), re.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, re.getHttpStatus(), request);
    }

    @ExceptionHandler({ InvalidRequestException.class })
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;
        List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResource error = new ErrorResource(status, "InvalidRequest", ire.getMessage());
        error.setFieldErrors(fieldErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException e, WebRequest request) {
        IllegalArgumentException ire = (IllegalArgumentException) e;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResource error = new ErrorResource(status, "InvalidRequest", ire.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, status, request);
    }

    @ExceptionHandler({ InvalidTokenException.class })
    protected ResponseEntity<Object> handleInvalidToken(RuntimeException e, WebRequest request) {
        InvalidTokenException ire = (InvalidTokenException) e;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResource error = new ErrorResource(status, "InvalidRequest", ire.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, status, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleOtherException(Exception e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResource error = new ErrorResource(status, "InternalServerError", "Internal Server Error");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.error(e.getMessage());
        e.printStackTrace();

        return handleExceptionInternal(e, error, headers, status, request);
    }

}
