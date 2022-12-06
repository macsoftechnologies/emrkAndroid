package com.macsoftech.ekart.model.register;

public class RegistrationRootResponse {
    private RegisterResponse registerResponse;

    private String message;

    private String statusCode;

    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(RegisterResponse registerResponse) {
        this.registerResponse = registerResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [registerResponse = " + registerResponse + ", message = " + message + ", statusCode = " + statusCode + "]";
    }
}