package com.example.foodsales20072021.model;

public class OrderModel {

    public String orderId;
    public int total;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "OrderModel{" +
                "orderId='" + orderId + '\'' +
                ", total=" + total +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
