package com.macsoftech.ekart.model.register;

public class RegisterResponse {
    private String lastName;

    private String[] availableLocation;

    private String emailId;

    private String userId;

    private String[] altNumber;

    private String firstName;

    private String createdAt;

    private String mobileNum;

    private String password;

    private String chooseLanguage;

    private String entityName;

    private String __v;

    private String _id;

    private String updatedAt;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String[] getAvailableLocation() {
        return availableLocation;
    }

    public void setAvailableLocation(String[] availableLocation) {
        this.availableLocation = availableLocation;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getAltNumber() {
        return altNumber;
    }

    public void setAltNumber(String[] altNumber) {
        this.altNumber = altNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChooseLanguage() {
        return chooseLanguage;
    }

    public void setChooseLanguage(String chooseLanguage) {
        this.chooseLanguage = chooseLanguage;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
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
        return "ClassPojo [lastName = " + lastName + ", availableLocation = " + availableLocation + ", emailId = " + emailId + ", userId = " + userId + ", altNumber = " + altNumber + ", firstName = " + firstName + ", createdAt = " + createdAt + ", mobileNum = " + mobileNum + ", password = " + password + ", chooseLanguage = " + chooseLanguage + ", entityName = " + entityName + ", __v = " + __v + ", _id = " + _id + ", updatedAt = " + updatedAt + "]";
    }
}
	