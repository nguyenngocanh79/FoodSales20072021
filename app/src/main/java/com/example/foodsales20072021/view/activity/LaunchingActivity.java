package com.example.foodsales20072021.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.common.GetTokenResult;
import com.example.foodsales20072021.databinding.ActivityLaunchingBinding;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.viewmodel.FoodViewModel;

public class LaunchingActivity extends AppCompatActivity {

    //Binding
    ActivityLaunchingBinding mBinding;
    //Model
    FoodViewModel mFoodViewModel;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLaunchingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel
        mFoodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mFoodViewModel.updateFoodRepository(((MyApplication) getApplication()).foodRepository);

        mFoodViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.VISIBLE);
                } else {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.GONE);
                }
            }
        });

        //Observe biến code nhận về (từ hàm fetchTotalCount) để kiểm tra token
        mFoodViewModel.getResponseCodeLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int code = integer;
                // 200: thành công có token và có sp trong cart
                //404: đã confirm, không có sp trong cart
                if (code == 200 || code == 404) {
                    Intent intent = new Intent(LaunchingActivity.this, HomeActivity.class);
                    intent.putExtra("code", code);
                    startActivity(intent);
                } else {//Nếu code == 401 , token đã expired, hoặc trường hợp khác thì mở Sign in
                    startActivity(new Intent(LaunchingActivity.this, MainActivity.class));
                }
                finish();
            }
        });

        GetTokenResult getTokenResult = TokenManager.getInstance().getAuthToken();
        if (getTokenResult == GetTokenResult.SUCCESS) {
            //Nếu đã lưu token, gửi thử lấy Total Cart để kiểm tra Token còn hiệu lực không. Sau đó kiểm tra kết quả server trả về
            //Load TotalCount (số sản phẩm trong cart)
            mFoodViewModel.fetchTotalCount();

        } else {
            //Nếu getTokenResult = FAIL tức chưa lưu token, mở Sign in
            mFoodViewModel.setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LaunchingActivity.this, MainActivity.class));
                    finish();
                    mFoodViewModel.setLoading(false);
                }
            }, 1000);
        }


    }
}