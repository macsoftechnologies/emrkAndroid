package com.macsoftech.ekart.model;

import java.util.ArrayList;
import java.util.List;

public class LocationResponseRoot {
    private String Message;

    private List<LocationData> Data;

    private String Count;

    private String StatusCode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<LocationData> getData() {
        if (Data == null) {
            return new ArrayList<>();
        }
        return Data;
    }

    public void setData(List<LocationData> Data) {
        this.Data = Data;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [Message = " + Message + ", Data = " + Data + ", Count = " + Count + ", StatusCode = " + StatusCode + "]";
    }
}