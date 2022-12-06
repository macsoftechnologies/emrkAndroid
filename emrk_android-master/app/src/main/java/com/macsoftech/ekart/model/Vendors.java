package com.macsoftech.ekart.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Vendors implements Parcelable {
    private String vendorName;
    private String quantity;

    private String productId;

    private String length;

    private String vendorId;

    private String userId;

    private String productName;

    private String createdAt;

    private String mobileNum;

    private List<String> productImage;

    private String size;

    private String entityName;

    private String location;

    private String _id;

    private String updatedAt;

    protected Vendors(Parcel in) {
        vendorName = in.readString();
        quantity = in.readString();
        productId = in.readString();
        length = in.readString();
        vendorId = in.readString();
        userId = in.readString();
        productName = in.readString();
        createdAt = in.readString();
        mobileNum = in.readString();
        productImage = in.createStringArrayList();
        size = in.readString();
        entityName = in.readString();
        location = in.readString();
        _id = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendorName);
        dest.writeString(quantity);
        dest.writeString(productId);
        dest.writeString(length);
        dest.writeString(vendorId);
        dest.writeString(userId);
        dest.writeString(productName);
        dest.writeString(createdAt);
        dest.writeString(mobileNum);
        dest.writeStringList(productImage);
        dest.writeString(size);
        dest.writeString(entityName);
        dest.writeString(location);
        dest.writeString(_id);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vendors> CREATOR = new Creator<Vendors>() {
        @Override
        public Vendors createFromParcel(Parcel in) {
            return new Vendors(in);
        }

        @Override
        public Vendors[] newArray(int size) {
            return new Vendors[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ClassPojo [quantity = " + quantity + ", productId = " + productId + ", length = " + length + ", vendorId = " + vendorId + ", userId = " + userId + ", productName = " + productName + ", createdAt = " + createdAt + ", mobileNum = " + mobileNum + ", productImage = " + productImage + ", size = " + size + ", entityName = " + entityName + ", location = " + location + ", _id = " + _id + ", updatedAt = " + updatedAt + "]";
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
