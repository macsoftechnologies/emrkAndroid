package com.macsoftech.ekart.model.search;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListOfVendorsResponse {
    @SerializedName("resp")
    private List<ListOfVendorsData> Data;

    private String statusCode;

    public List<ListOfVendorsData> getData() {
        if (Data == null) {
            return new ArrayList();
        }
        return Data;
    }

    public void setData(List<ListOfVendorsData> Data) {
        this.Data = Data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [Data = " + Data + ", statusCode = " + statusCode + "]";
    }
}
	