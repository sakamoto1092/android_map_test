package com.example.maptest;

import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public  class MyAdapter extends FragmentPagerAdapter {
    String s;
    private ArrayList<FavoriteList> mList;

	public MyAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
        mList = new ArrayList<FavoriteList>();
		Log.d("MyAdapter", "constracter");
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		Log.d("MyAdapter", "get item");
		return new Listfragment().newInstance(mList.get(position));
	}
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "list1";
        if(position == 1)
            return "list2";
        if(position == 2)
            return "list3";
        if(position == 3)
            return "list4";
        if(position == 4)
            return "list5";
        if(position == 5)
            return "list6";
        if(position == 6)
            return "list7";
        if(position == 7)
            return "list8";
        if(position == 8)
            return "list9";
        if(position == 9)
            return "list10";

        return "undefined";
    }

    public void setInitstr(String[] strings){
        s = strings[0];
    }

    //　FavoriteListを登録する関数
    public void addFavoList(FavoriteList fl){
        mList.add(fl);
    }

    // posで指定したlistviewにitemとして
    // favを追加
    public int addFavolistItem( Favorite fav, int pos){
        return mList.get(pos).addFavorite(fav);
    }

    // posで指定した位置のlistviewのFavoriteListを
    // 取得する関数
    public FavoriteList getFavorist(int pos){
        return mList.get(pos);
    }

    // 複数のlistviewのうちpos番目のlistviewの
    // favlistのうち，idが_idのものを削除する
    // つまる話が，1つのitemをlistviewから削除する関数
    public void removeFavolistItem(int _id, int pos){
        FavoriteList f = mList.get(pos);
        for (int i = 0; i < f.favlist.size(); i++) {
            if (f.getid(i) ==_id) {
                f.favlist.remove(i);
                break;
            }
        }
    }

}