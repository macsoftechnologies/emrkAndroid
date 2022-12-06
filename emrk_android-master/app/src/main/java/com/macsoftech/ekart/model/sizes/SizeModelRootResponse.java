package com.macsoftech.ekart.model.sizes;

public class SizeModelRootResponse {
    private String Message;

    private SizeModelData Data;

    private String StatusCode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public SizeModelData getData() {
        return Data;
    }

    public void setData(SizeModelData Data) {
        this.Data = Data;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [Message = " + Message + ", Data = " + Data + ", StatusCode = " + StatusCode + "]";
    }
}