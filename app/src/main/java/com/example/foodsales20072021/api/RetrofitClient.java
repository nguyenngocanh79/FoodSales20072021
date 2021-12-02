package com.example.foodsales20072021.api;

import com.example.foodsales20072021.common.AppConstants;
import com.example.foodsales20072021.common.GetTokenResult;
import com.example.foodsales20072021.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private Retrofit retrofit;
    private ApiRequest apiRequest;


    private RetrofitClient() {
        retrofit = createRetrofit();
        apiRequest = retrofit.create(ApiRequest.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    private Retrofit createRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                //Dùng Interceptor sẽ tự động add Header nếu token!=null
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
//                        String token = TokenManager.getInstance().fetchAuthToken();
//                        if (token == null){
//                            return null;
//                        }
                        GetTokenResult getTokenResult = TokenManager.getInstance().getAuthToken();
                        Request newRequest  = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + getTokenResult.getToken())
                                    .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        Gson gson = new GsonBuilder().setLenient().create();

        // base url : host

        Retrofit.Builder retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(AppConstants.BASE_URL);
        return retrofit.build();
    }
    public ApiRequest getApiRequest(){
        return apiRequest;
    }
}