package com.example.foodsales20072021.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodsales20072021.base.BaseViewModel;
import com.example.foodsales20072021.model.ApiResponse;
import com.example.foodsales20072021.model.UserModel;
import com.example.foodsales20072021.repository.AuthenRepository;
import com.example.foodsales20072021.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenViewModel extends BaseViewModel {

    private AuthenRepository authenRepository;
    private MutableLiveData<ApiResponse<UserModel>> responseUserLiveData = new MutableLiveData<>();

//    private TokenManager tokenManager;
//    public void setTokenManager() {
//        tokenManager = TokenManager.getInstance();
//    }
//    public TokenManager getTokenManager() {
//        return tokenManager;
//    }



    public void setAuthenRepository(AuthenRepository repository) {
        this.authenRepository = repository;
    }


    public LiveData<ApiResponse<UserModel>> getResult() {
        return responseUserLiveData;
    }

    //Có thể không dùng Handler().postDelayed cũng được. Chủ yếu để cho hiện lâu 1 chút
    public void handleSignIn(String email, String password) {
        if (authenRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    authenRepository.signIn(new UserModel(email,password))
                            .enqueue(new Callback<ApiResponse<UserModel>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<UserModel>> call, Response<ApiResponse<UserModel>> response) {
                                    if (response.body() != null) {
                                        //Có kết quả trả về thành công, không bao gồm trường hợp trả về lỗi
                                        ApiResponse<UserModel> data = response.body();
                                        //Lưu token cho Retrofit (sẽ lấy ra add vào Header)
                                        TokenManager tokenManager = TokenManager.getInstance();
                                        tokenManager.saveAuthToken(data.getData().token,data.getData().userId);
                                        //Chủ yếu để báo với Main Activity là đã đăng nhập thành công
                                        responseUserLiveData.setValue(data);
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<UserModel>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });
                }
            }, 0);

        }
    }

    public void handleSignUp(String email, String password, String fullName , String phone , String address) {
        if (authenRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    authenRepository.signUp(new UserModel(fullName,email,password,phone,address))
                            .enqueue(new Callback<ApiResponse<UserModel>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<UserModel>> call, Response<ApiResponse<UserModel>> response) {
                                    if (response.body() != null) {
                                        ApiResponse<UserModel> data = response.body();
                                        responseUserLiveData.setValue(data);
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<UserModel>> call, Throwable t) {
                                    setError(t);
                                    setLoading(false);
                                }
                            });
                }
            }, 200);

        }
    }

}
