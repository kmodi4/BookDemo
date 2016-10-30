package com.example.karan.bookdemo;


import android.os.Parcel;
import android.os.Parcelable;

public class listinfo implements Parcelable {
    int icon;
    String title;
    String url;
    int yourprice;
    int originalprice;
    String seller;


    public listinfo(){

    }
    protected listinfo(Parcel in) {
        icon = in.readInt();
        title = in.readString();
        url = in.readString();
        yourprice = in.readInt();
        originalprice = in.readInt();
        seller = in.readString();
    }

    public static final Creator<listinfo> CREATOR = new Creator<listinfo>() {
        @Override
        public listinfo createFromParcel(Parcel in) {
            return new listinfo(in);
        }

        @Override
        public listinfo[] newArray(int size) {
            return new listinfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeInt(yourprice);
        parcel.writeInt(originalprice);
        parcel.writeString(seller);
    }
}
