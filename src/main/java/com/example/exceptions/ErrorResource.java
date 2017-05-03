package com.example.exceptions;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResource {
    private Integer status;
    private String statusText;
    private String code;
    private String message;
    private List<FieldErrorResource> fieldErrors;

    public ErrorResource() { }

    public ErrorResource(HttpStatus status, String code, String message) {
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.code = code;
        this.message = message;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldErrorResource> getFieldErrors() { return fieldErrors; }

    public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
