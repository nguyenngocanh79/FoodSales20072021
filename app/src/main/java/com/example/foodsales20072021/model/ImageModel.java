package com.example.foodsales20072021.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    public String imageUrl;
    //Phải có constructor rỗng cho quá trình deserialize khi đọc từ FireStore về
    public ImageModel() {
    }

    protected ImageModel(Parcel in) {
        imageUrl = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public String toString() {
        return "ImageModel{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
    }
}
