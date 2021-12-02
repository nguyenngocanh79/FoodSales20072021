package com.example.foodsales20072021.model;

public class UserModel {
    public String userId;
    public String fullName;
    public String email;
    public String password;
    public String phone;
    public String address;
    public String role;
    public String createdAt;
    public String updatedAt;
    public String token;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserModel(String fullName, String email, String password, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
