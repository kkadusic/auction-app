package com.atlantbh.auctionapp.response;

import java.util.List;

public class NotificationPageResponse {

    private List<NotificationResponse> notifications;
    private Boolean lastPage;

    public NotificationPageResponse() {
    }

    public NotificationPageResponse(List<NotificationResponse> notifications, Boolean lastPage) {
        this.notifications = notifications;
        this.lastPage = lastPage;
    }

    public List<NotificationResponse> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationResponse> notifications) {
        this.notifications = notifications;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }
}
