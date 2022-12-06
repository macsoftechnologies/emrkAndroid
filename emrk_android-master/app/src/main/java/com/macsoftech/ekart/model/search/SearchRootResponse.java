package com.macsoftech.ekart.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchRootResponse {
    private List<UserProdResponse> userProdResponse;

    private List<UserProdResponse> Data;

    private String productVendorsCount;

    private String StatusCode;

    public List<UserProdResponse> getUserProdResponse() {
        if (userProdResponse == null) {
            return new ArrayList<>();
        }
        return userProdResponse;
    }

    public void setUserProdResponse(List<UserProdResponse> userProdResponse) {
        this.userProdResponse = userProdResponse;
    }

    public String getProductVendorsCount() {
        return productVendorsCount;
    }

    public void setProductVendorsCount(String productVendorsCount) {
        this.productVendorsCount = productVendorsCount;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [userProdResponse = " + userProdResponse + ", productVendorsCount = " + productVendorsCount + ", StatusCode = " + StatusCode + "]";
    }

    public List<UserProdResponse> getData() {
        if (Data == null) {
            return new ArrayList<>();
        }
        return Data;
    }

    public void setData(List<UserProdResponse> data) {
        Data = data;
    }
}