package com.example.maptest;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
/**
 * Created by BLACK_LAGOON on 2014/07/16.
 */
public class FavoriteList {

    public String title;
    public ArrayList<Favorite> favlist;
    public String tag;
    static int count=0;

    FavoriteList(){
        favlist = new ArrayList<Favorite>();
    }

    public int getid(int pos){
        return favlist.get(pos).id;
    }

    // 唯一無二なidを振りつつ
    // itmeを追加
    public int addFavorite(Favorite fv){
        fv.id = ++count;
        favlist.add(fv);
        return fv.id;
    }
}
