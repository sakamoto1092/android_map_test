package com.example.maptest;

import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public  class MyAdapter extends FragmentPagerAdapter {
    String s;
	public MyAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
		Log.d("MyAdapter", "constracter");
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		Log.d("MyAdapter", "get item");


		switch (position) {
		case 0:
			return new Listfragment().newInstance(s, s);
		case 1:
			return new Listfragment().newInstance("1", "1");
		case 2:
			return new Listfragment().newInstance("2", "2");
		case 3:
			return new Listfragment().newInstance("3", "3");
		case 4:
			return new Listfragment().newInstance("4", "4");
		case 5:
			return new Listfragment().newInstance("5", "5");
		case 6:
			return new Listfragment().newInstance("6", "6");
		case 7:
			return new Listfragment().newInstance("7", "7");
		case 8:
			return new Listfragment().newInstance("8", "8");
		case 9:
			return new Listfragment().newInstance("9", "9");
		default:
			return null;
		}
		
	}

    public void setInitstr(String[] strings){
        s = strings[0];
    }

}