package com.atlantbh.auctionapp.request;

public class UpdateNotifRequest {

    private Boolean emailNotify;
    private Boolean pushNotify;

    public UpdateNotifRequest() {
    }

    public UpdateNotifRequest(Boolean emailNotify, Boolean pushNotify) {
        this.emailNotify = emailNotify;
        this.pushNotify = pushNotify;
    }

    public Boolean getEmailNotify() {
        return emailNotify;
    }

    public void setEmailNotify(Boolean emailNotify) {
        this.emailNotify = emailNotify;
    }

    public Boolean getPushNotify() {
        return pushNotify;
    }

    public void setPushNotify(Boolean pushNotify) {
        this.pushNotify = pushNotify;
    }
}
