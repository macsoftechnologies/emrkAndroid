package com.macsoftech.ekart.model.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserProdResponse implements Parcelable {

//    _id = "62fb42eb938389984c607839"
//    createdAt = "2022-08-16T07:10:35.860Z"
//    length = "15cm"
//    location = "Visakhapatnam"
//    productAlias = null
//    productCode = null
//    productId = "ec22028c-def6-4078-b10a-dac79ff2248d"
//    productImage = {ArrayList@12005}  size = 1
//    productName = "Shoes"
//    productStatus = null
//    quantity = "20"
//    size = "7inchs"
//    updatedAt = "2022-08-16T07:10:35.860Z"
//    userId = "6d43a713-1af0-40cf-96cc-aa6c76853ff3"

    private String createdAt;

//    private List<String> productImage;
    private List<String> productImage;
    private String quantity;

    private String mobileNum;
    private String vendorName;

    private String productCode;
    private String size;

    private String productId;
    private String userId;

    private String __v;

    private String productStatus;

//    private List<String> location;
    private String location;

    protected UserProdResponse(Parcel in) {
        createdAt = in.readString();
        productImage = in.createStringArrayList();
        quantity = in.readString();
        mobileNum = in.readString();
        productCode = in.readString();
        size = in.readString();
        productId = in.readString();
        userId = in.readString();
        __v = in.readString();
        productStatus = in.readString();
        location = in.readString();
        _id = in.readString();
        productName = in.readString();
        length = in.readString();
        productAlias = in.createStringArrayList();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeStringList(productImage);
        dest.writeString(quantity);
        dest.writeString(mobileNum);
        dest.writeString(productCode);
        dest.writeString(size);
        dest.writeString(productId);
        dest.writeString(userId);
        dest.writeString(__v);
        dest.writeString(productStatus);
        dest.writeString(location);
        dest.writeString(_id);
        dest.writeString(productName);
        dest.writeString(length);
        dest.writeStringList(productAlias);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserProdResponse> CREATOR = new Creator<UserProdResponse>() {
        @Override
        public UserProdResponse createFromParcel(Parcel in) {
            return new UserProdResponse(in);
        }

        @Override
        public UserProdResponse[] newArray(int size) {
            return new UserProdResponse[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String _id;

    private String productName;
    private String length;

    private List<String> productAlias;

    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getProductImage() {
        if (productImage == null) {
            return new ArrayList<>();
        }
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

//    public List<String> getLocation() {
//        return location;
//    }
//
//    public void setLocation(List<String> location) {
//        this.location = location;
//    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getProductAlias() {
        return productAlias;
    }

    public void setProductAlias(List<String> productAlias) {
        this.productAlias = productAlias;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ClassPojo [createdAt = " + createdAt + ", productImage = " + productImage + ", productCode = " + productCode + ", productId = " + productId + ", __v = " + __v + ", productStatus = " + productStatus + ", location = " + location + ", _id = " + _id + ", productName = " + productName + ", productAlias = " + productAlias + ", updatedAt = " + updatedAt + "]";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}