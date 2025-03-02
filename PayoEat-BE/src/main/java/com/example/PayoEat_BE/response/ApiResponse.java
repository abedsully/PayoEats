package com.example.PayoEat_BE.response;

public class ApiResponse {
    private String message;
    private Object result;

    public ApiResponse() {
    }

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.result = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}