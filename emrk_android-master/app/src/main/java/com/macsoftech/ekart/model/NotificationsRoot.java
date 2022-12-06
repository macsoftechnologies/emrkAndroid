package com.macsoftech.ekart.model;

import com.macsoftech.ekart.adapter.NotificationsModel;

import java.util.List;

public class NotificationsRoot {
    private List<NotificationsModel> data;

    public List<NotificationsModel> getData() {
        return data;
    }

    public void setData(List<NotificationsModel> data) {
        this.data = data;
    }
}
