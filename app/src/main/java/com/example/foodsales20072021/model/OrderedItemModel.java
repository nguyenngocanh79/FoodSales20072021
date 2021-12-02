package com.example.foodsales20072021.model;

import java.util.List;

public class OrderedItemModel {

    public String orderId;
    public String foodId;
    public String foodName;
    public List<ImageModel> images;
    public int quantity;
    public int price;
    public String createdAt;
    public String updatedAt;

    public OrderedItemModel(String orderId, String foodId, int quantity) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
    }
    //Phải có constructor rỗng cho quá trình deserialize khi đọc từ FireStore về
    public OrderedItemModel(){

    }

    public OrderedItemModel(String orderId, String foodId, String foodName, List<ImageModel> images, int quantity, int price, String createdAt, String updatedAt) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.images = images;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
