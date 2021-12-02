package com.example.foodsales20072021.model;

public class Sender {
    //Các biến trong Data có thể thay đổi
    public Data data;
    public String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
