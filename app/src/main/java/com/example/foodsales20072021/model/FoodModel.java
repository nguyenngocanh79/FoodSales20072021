package com.example.foodsales20072021.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FoodModel implements Parcelable {
    public String foodId;
    public String foodName;
    public List<ImageModel> images;
    public String description;
    public int price;
    public String cateId;
    public String cateName;
    public String createdAt;
    public String updatedAt;

    public FoodModel(String foodId){
        this.foodId = foodId;
    }

    public FoodModel(String foodId, String foodName, List<ImageModel> images, String description, int price, String cateId, String cateName, String createdAt, String updatedAt) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.images = images;
        this.description = description;
        this.price = price;
        this.cateId = cateId;
        this.cateName = cateName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    protected FoodModel(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        images = in.createTypedArrayList(ImageModel.CREATOR);
        description = in.readString();
        price = in.readInt();
        cateId = in.readString();
        cateName = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<FoodModel> CREATOR = new Creator<FoodModel>() {
        @Override
        public FoodModel createFromParcel(Parcel in) {
            return new FoodModel(in);
        }

        @Override
        public FoodModel[] newArray(int size) {
            return new FoodModel[size];
        }
    };

    @Override
    public String toString() {
        return "FoodModel{" +
                "foodId='" + foodId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", images=" + images +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", cateId='" + cateId + '\'' +
                ", cateName='" + cateName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodId);
        parcel.writeString(foodName);
        parcel.writeTypedList(images);
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeString(cateId);
        parcel.writeString(cateName);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
    }
}
