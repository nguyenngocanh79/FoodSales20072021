package com.example.foodsales20072021.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class CartModel {

    public int total;
    public List<OrderedItemModel> items;
    public String createdAt;
    public String updatedAt;
    //Thêm 2 field cho Firestore
    @ServerTimestamp
    public Date confirmedAt;
    public String userId;


}
