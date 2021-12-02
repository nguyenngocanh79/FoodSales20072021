package com.example.foodsales20072021.viewmodel;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.base.BaseViewModel;
import com.example.foodsales20072021.model.ApiResponse;
import com.example.foodsales20072021.model.NotificationResponse;
import com.example.foodsales20072021.model.Sender;
import com.example.foodsales20072021.model.UserModel;
import com.example.foodsales20072021.repository.NotificationRepository;
import com.example.foodsales20072021.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends BaseViewModel {
    private NotificationRepository notificationRepository;
    private MutableLiveData<NotificationResponse> notificationResponseLiveData;

    public void setNotificationRepository(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    //Có thể không dùng Handler().postDelayed cũng được. Chủ yếu để cho hiện lâu 1 chút
    public void sendNotification(Sender body) {
        if (notificationRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notificationRepository.sendNotification(body)
                            .enqueue(new Callback<NotificationResponse>() {
                                @Override
                                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(MyApplication.getContext(), "Send notification Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                                    Toast.makeText(MyApplication.getContext(), "Cannot send notification", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }, 0);

        }
    }

}
