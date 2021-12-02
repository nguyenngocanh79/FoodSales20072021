package com.example.foodsales20072021.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.api.RetrofitClient;
import com.example.foodsales20072021.databinding.ActivityMainBinding;
import com.example.foodsales20072021.model.ApiResponse;
import com.example.foodsales20072021.model.UserModel;
import com.example.foodsales20072021.repository.AuthenRepository;
import com.example.foodsales20072021.viewmodel.AuthenViewModel;

public class MainActivity extends AppCompatActivity {

    AuthenViewModel mAuthenViewModel;
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAuthenViewModel = new ViewModelProvider(this).get(AuthenViewModel.class);
        mAuthenViewModel.setAuthenRepository(((MyApplication)getApplication()).authenRepository);

        observerData();
        event();


    }

    private void event() {
        mBinding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.textEditEmail.getText().toString();
                String pass = mBinding.textEditPassword.getText().toString();

                if (email.length() > 0 && pass.length() > 0) {
                    mAuthenViewModel.handleSignIn(email, pass);
                } else {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }

    private void observerData() {
        mAuthenViewModel.getResult().observe(this, new Observer<ApiResponse<UserModel>>() {
            @Override
            public void onChanged(ApiResponse<UserModel> userModelApiResponse) {
                if (userModelApiResponse.getData() != null) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//                    intent.putExtra("Token",mAuthenViewModel.getTokenManager().fetchAuthToken());
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));

                }
            }
        });

        mAuthenViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.VISIBLE);
                } else {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.GONE);
                }
            }
        });

        mAuthenViewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null){
                    Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}