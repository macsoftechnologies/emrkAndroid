package com.macsoftech.ekart.model.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.macsoftech.ekart.model.Vendors;

import java.util.List;

public class ListOfVendorsData2 implements Parcelable {

    private List<Vendors> vendors;


    protected ListOfVendorsData2(Parcel in) {

        vendors = in.createTypedArrayList(Vendors.CREATOR);

    }

    public static final Creator<ListOfVendorsData2> CREATOR = new Creator<ListOfVendorsData2>() {
        @Override
        public ListOfVendorsData2 createFromParcel(Parcel in) {
            return new ListOfVendorsData2(in);
        }

        @Override
        public ListOfVendorsData2[] newArray(int size) {
            return new ListOfVendorsData2[size];
        }
    };



    public List<Vendors> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendors> vendors) {
        this.vendors = vendors;
    }


    @Override
    public String toString() {
        return "ClassPojo [createdAt = "+ ", vendors = " + vendors+ "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(vendors);

    }



    public String getVendorName() {
        if (getVendors().get(0).getVendorName() == null) {
            return "";
        }
        return getVendors().get(0).getVendorName();
    }

 }