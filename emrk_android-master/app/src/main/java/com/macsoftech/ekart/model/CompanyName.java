package com.macsoftech.ekart.model;

public class CompanyName {

    private String companyName;

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    private String mobileNo;
    private String qty;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getQty() {
        return qty;
    }
}
