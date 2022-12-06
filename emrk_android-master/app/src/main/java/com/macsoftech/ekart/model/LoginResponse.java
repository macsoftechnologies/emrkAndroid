package com.macsoftech.ekart.model;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {
    public String _id;
    public String firstName;
    public String lastName;
    private Object primaryLocation;
    private List<String> entityImage;
    private List<String> userImage;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAvailableLocation() {
        if (availableLocation == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for (Object locationData : availableLocation) {
            list.add(((LocationData) locationData).getVillage());
        }
        return list;
    }

    public List<LocationData> getAvailableLocationObj() {
        if (availableLocation == null) {
            return new ArrayList<>();
        }
        return availableLocation;
    }

    public void setAvailableLocation(List<LocationData> availableLocation) {
        this.availableLocation = availableLocation;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Object getAltNumber() {
        return altNumber;
    }

    public void setAltNumber(String altNumber) {
        this.altNumber = altNumber;
    }

    public String getChooseLanguage() {
        return chooseLanguage;
    }

    public void setChooseLanguage(String chooseLanguage) {
        this.chooseLanguage = chooseLanguage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String mobileNum;
    public String entityName;
    public String password;
    public List availableLocation;
    public String emailId;
    public Object altNumber;
    public String chooseLanguage;
    public String userId;
    public String createdAt;
    public String updatedAt;

    public String getEntityImage() {
        if (entityImage == null || entityImage.isEmpty()) {
            return "";
        }
        return entityImage.get(0);
    }

    public void setEntityImage(List<String> entityImage) {
        this.entityImage = entityImage;
    }

    public String getUserImage() {
        if (userImage == null || userImage.isEmpty()) {
            return "";
        }
        return userImage.get(0);
    }

    public void setUserImage(List<String> userImage) {
        this.userImage = userImage;
    }

    public String getPrimaryLocation() {
        if (primaryLocation == null) {
            return "";
        }
        if (primaryLocation instanceof List && !((List<?>) primaryLocation).isEmpty()) {
            return (String) ((List<?>) primaryLocation).get(0);
        }
        return "";
    }

    public void setPrimaryLocation(String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }
}