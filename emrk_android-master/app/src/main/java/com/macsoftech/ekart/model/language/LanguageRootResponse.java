package com.macsoftech.ekart.model.language;

public class LanguageRootResponse {
    private String Message;

    private LanguageData Data;

    private String StatusCode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public LanguageData getData() {
        return Data;
    }

    public void setData(LanguageData Data) {
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
			