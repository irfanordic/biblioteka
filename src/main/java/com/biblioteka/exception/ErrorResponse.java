package com.biblioteka.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String error;

    public ErrorResponse(int status, String error, String message) {
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
