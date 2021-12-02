package com.example.foodsales20072021.model;

public class ConfirmModel {
    public String orderId;
    public String status;

    public ConfirmModel(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public ConfirmModel() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
