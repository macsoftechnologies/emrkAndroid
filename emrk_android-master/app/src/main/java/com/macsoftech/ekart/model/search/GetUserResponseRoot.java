package com.macsoftech.ekart.model.search;

import com.macsoftech.ekart.model.LoginResponse;

import java.util.List;

public class GetUserResponseRoot {
    private List<LoginResponse> userFeedbackResponse;

    public List<LoginResponse> getUserFeedbackResponse() {
        return userFeedbackResponse;
    }

    public void setUserFeedbackResponse(List<LoginResponse> userFeedbackResponse) {
        this.userFeedbackResponse = userFeedbackResponse;
    }
}
