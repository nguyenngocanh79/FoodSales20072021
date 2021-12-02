package com.example.foodsales20072021.api;

import com.example.foodsales20072021.model.NotificationResponse;
import com.example.foodsales20072021.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiRequest {

    @Headers({"Content-Type:application/json",
            "Authorization:key=AAAAgVzHM3s:APA91bFa5pxEjbq50lu69C0_Dx0Qlq1RcRVN1IO97r7tOoKCv8CtbjJ7z8U8p_Q3rrlDzEfLuOl3sCjW-xD9cXYekA7ek_oXTPO2XzU-9BidarORctCrVdgRDiw2soyzTH_DtMo7X4vM"
    })
    @POST("fcm/send")
    Call<NotificationResponse> sendNotification(@Body Sender body);
}
