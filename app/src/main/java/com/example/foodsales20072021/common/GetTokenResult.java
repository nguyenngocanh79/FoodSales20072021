package com.example.foodsales20072021.common;

public enum GetTokenResult {
    SUCCESS(""),
    FAIL("");
    String token;
    //constructor phải là private
    private GetTokenResult(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
    public void setToken(String token){
        this.token = token;
    }
}
