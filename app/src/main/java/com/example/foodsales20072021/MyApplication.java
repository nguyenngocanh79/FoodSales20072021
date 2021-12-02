package com.example.foodsales20072021;

import android.app.Application;

import com.example.foodsales20072021.api.ApiRequest;
import com.example.foodsales20072021.api.NotificationApiRequest;
import com.example.foodsales20072021.api.NotificationRetrofitClient;
import com.example.foodsales20072021.api.RetrofitClient;
import com.example.foodsales20072021.repository.AuthenRepository;
import com.example.foodsales20072021.repository.FoodRepository;
import com.example.foodsales20072021.repository.NotificationRepository;

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public FoodRepository foodRepository;
    public AuthenRepository authenRepository;
    public NotificationRepository notificationRepository;

    public ApiRequest apiRequest;
    public NotificationApiRequest notificationApiRequest;
    @Override
    public void onCreate() {
        super.onCreate();
        //Dùng để truyền application, dùng: MyApplication.myapplication
        myApplication = this;

        //Khi vừa chạy app là có RetrofitClient (có Token và apiRequest) để dùng
        apiRequest = RetrofitClient.getInstance().getApiRequest();
        //Khởi tạo các Repository để inject vào các ViewModel trong View
        foodRepository = new FoodRepository();
        foodRepository.updateRequest(apiRequest);
        authenRepository = new AuthenRepository();
        authenRepository.updateRequest(apiRequest);

        //Phần notification
        notificationApiRequest = NotificationRetrofitClient.getInstance().getApiRequest();
        notificationRepository = new NotificationRepository();
        notificationRepository.updateRequest(notificationApiRequest);

    }
    public static MyApplication getContext() {
        return myApplication;
    }

}
