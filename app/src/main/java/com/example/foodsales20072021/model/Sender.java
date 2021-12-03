package com.example.foodsales20072021.model;

public class Sender {
    //Nếu muốn Firestore Messaging tự gửi Notification thì thêm trường này
    public Notification notification;
    //Các biến trong Data có thể thay đổi
    public Data data;
    public String to;

    public Sender( Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Sender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public Sender(Notification notification, Data data, String to) {
        this.notification = notification;
        this.data = data;
        this.to = to;
    }
}
