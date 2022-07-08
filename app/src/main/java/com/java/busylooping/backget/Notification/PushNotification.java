package com.java.busylooping.backget.Notification;

public class PushNotification {
    private NotificationData data;
    private String to;


    public PushNotification(NotificationData notificationData, String to) {
        this.data = notificationData;
        this.to = to;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
