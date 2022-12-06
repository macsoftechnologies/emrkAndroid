package com.macsoftech.ekart.model.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.macsoftech.ekart.model.Vendors;

import java.util.ArrayList;
import java.util.List;

public class ListOfVendorsData implements Parcelable {
    private String createdAt;

    private List<String> productImage;

    private List<String> productImage2;

    private String productCode;

    private String productId;

    private String productName1;

    private String __v;

    private String[] productAliasName;

    private String _id;

    private List<Vendors> vendors;

    private String productName;

    private String updatedAt;

    protected ListOfVendorsData(Parcel in) {
        createdAt = in.readString();
        productImage = in.createStringArrayList();
        // productImage2 = in.readString();
        productImage2 = in.createStringArrayList();
        productCode = in.readString();
        productId = in.readString();
        productName1 = in.readString();
        __v = in.readString();
        productAliasName = in.createStringArray();
        _id = in.readString();
        vendors = in.createTypedArrayList(Vendors.CREATOR);
        productName = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<ListOfVendorsData> CREATOR = new Creator<ListOfVendorsData>() {
        @Override
        public ListOfVendorsData createFromParcel(Parcel in) {
            return new ListOfVendorsData(in);
        }

        @Override
        public ListOfVendorsData[] newArray(int size) {
            return new ListOfVendorsData[size];
        }
    };

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public List<String> getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(List<String> productImage2) {
        this.productImage2 = productImage2;
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

    public String getProductName1() {
        return productName1;
    }

    public void setProductName1(String productName1) {
        this.productName1 = productName1;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String[] getProductAliasName() {
        return productAliasName;
    }

    public void setProductAliasName(String[] productAliasName) {
        this.productAliasName = productAliasName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Vendors> getVendors() {
        /*if (vendors.size() > 0) {
            return vendors;
        } else {
            vendors = new ArrayList<>();
            return vendors;
        }*/
        return vendors;
    }

    public void setVendors(List<Vendors> vendors) {
        this.vendors = vendors;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ClassPojo [createdAt = " + createdAt + ", productImage = " + productImage + ", productImage2 = " + productImage2 + ", productCode = " + productCode + ", productId = " + productId + ", productName1 = " + productName1 + ", __v = " + __v + ", productAliasName = " + productAliasName + ", _id = " + _id + ", vendors = " + vendors + ", productName = " + productName + ", updatedAt = " + updatedAt + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeStringList(productImage);
        //dest.writeString(productImage2);
        dest.writeStringList(productImage2);
        dest.writeString(productCode);
        dest.writeString(productId);
        dest.writeString(productName1);
        dest.writeString(__v);
        dest.writeStringArray(productAliasName);
        dest.writeString(_id);
        dest.writeTypedList(vendors);
        dest.writeString(productName);
        dest.writeString(updatedAt);
    }

    public String getQuantity() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getQuantity();
        }
    }

    public String getLength() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getLength();
        }
    }

    public String getSize() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getSize();
        }
    }

    public String getLocation() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getLocation();
        }
    }

    public String getEntityName() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getEntityName();
        }
    }

    public String getMobileNum() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getMobileNum();
        }
    }

    public String getVendorName() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getVendorName();
        }
    }

    public String getUserId() {
        if (getVendors().size() == 0) {
            return "";
        } else {
            return getVendors().get(0).getUserId();
        }
    }
}