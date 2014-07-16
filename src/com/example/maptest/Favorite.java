package com.example.maptest;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

import com.google.android.gms.maps.model.LatLng;

public class Favorite implements Parcelable{
    int id;
    String title;
    LatLng latlng;
    String addres;
    String tag;

    Favorite(){
        title = "not defind";
        
    }
    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        public Favorite[] newArray(int size) {

            return new Favorite[size];
        }

    };
    
    Favorite(Parcel in){
    	id = in.readInt();
    	title = in.readString();
    	latlng = (LatLng) in.readValue(LatLng.class.getClassLoader());
    	addres = in.readString();
    	tag = in.readString();
    }

	@Override
	public int describeContents() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO 自動生成されたメソッド・スタブ
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeValue(latlng);
		dest.writeString(addres);
		dest.writeString(tag);	
	}
	
	
	
}
