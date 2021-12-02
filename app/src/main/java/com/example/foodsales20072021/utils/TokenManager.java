package com.example.foodsales20072021.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.common.GetTokenResult;

public class TokenManager {
//    private Context context;
    private static TokenManager instance = null;
    private SharedPreferences prefs;
    private static final String USER_TOKEN = "user_token";
    private static final String USER_ID = "user_id";

    private TokenManager() {
//        this.context = context;
        prefs = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext().getString(R.string.app_name)
                        ,Context.MODE_PRIVATE);
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public String getUserTokenKey(){
        return USER_TOKEN;
    }

    //Function to save Authen token
    public void saveAuthToken(String token, String userid) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_ID, userid);
        editor.apply();
    }

    //Function to remove Authen token
    public void removeAuthToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(USER_TOKEN);
        editor.remove(USER_ID);
        editor.apply();
    }

    //Function to clear toàn bộ các giá trị đã đưa vào prefs
    public void clearPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    //Function to fetch Authen token
    public String fetchAuthToken() {
        return prefs.getString(USER_TOKEN, "");
    }

    //Function to fetch Authen token (trả về Enum)
    //Nếu muốn kiểm tra là SUCCESS
    //    GetTokenResult getTokenResult = TokenManager.getInstance().getAuthToken();
    //    if(getTokenResult.equals(GetTokenResult.SUCCESS)) {
    //         ///
    //    }

    //Function to fetch Authen token
    public String getUserId() {
        return prefs.getString(USER_ID, "");
    }


    public GetTokenResult getAuthToken(){
        if(prefs.contains(USER_TOKEN)){
            GetTokenResult result = GetTokenResult.SUCCESS;
            //Nếu Authen token đã được lưu
            String token = prefs.getString(USER_TOKEN, "");
            result.setToken(token);
            return result;
        } else {
            //Nếu Authen token chưa được lưu
            return GetTokenResult.FAIL;
        }
    }

}
