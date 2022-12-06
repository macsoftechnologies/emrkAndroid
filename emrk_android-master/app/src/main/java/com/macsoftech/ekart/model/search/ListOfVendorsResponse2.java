package com.macsoftech.ekart.model.search;

import com.google.gson.annotations.SerializedName;
import com.macsoftech.ekart.model.Vendors;

import java.util.ArrayList;
import java.util.List;

public class ListOfVendorsResponse2 {
    @SerializedName("Data")
    private List<Vendors> Data;

    private String statusCode;

    public List<Vendors> getData() {
        if (Data == null) {
            return new ArrayList();
        }
        return Data;
    }

    public void setData(List<Vendors> Data) {
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
	