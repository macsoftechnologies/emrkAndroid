package com.macsoftech.ekart.model;

public class LoginRootResponse {
    public int statusCode;
    public String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResponse getLoginRes() {
        return loginRes;
    }

    public void setLoginRes(LoginResponse loginRes) {
        this.loginRes = loginRes;
    }

    public LoginResponse loginRes;
}