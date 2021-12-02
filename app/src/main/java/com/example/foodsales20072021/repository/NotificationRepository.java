package com.example.foodsales20072021.repository;

import com.example.foodsales20072021.api.NotificationApiRequest;
import com.example.foodsales20072021.model.NotificationResponse;
import com.example.foodsales20072021.model.Sender;

import retrofit2.Call;

public class NotificationRepository {
    private NotificationApiRequest notificationApiRequest;

    public NotificationRepository() {
    }

    public void updateRequest(NotificationApiRequest notificationApiRequest){
        this.notificationApiRequest = notificationApiRequest;
    }

    public Call<NotificationResponse> sendNotification(Sender body){
        return notificationApiRequest.sendNotification(body);
    }

}
