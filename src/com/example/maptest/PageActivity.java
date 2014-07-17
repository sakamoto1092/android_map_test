package com.example.maptest;

import com.example.maptest.Listfragment.OnFragmentInteractionListener;
import com.google.android.gms.maps.model.LatLng;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.example.maptest.FavoriteList.*;

public class PageActivity extends FragmentActivity implements
		OnFragmentInteractionListener{

	MyAdapter mAdapter;
	ViewPager mPager;
	ListView lv;

	@Override
	protected void onCreate(Bundle arg0) {
		Log.d("page_activity", "on create");
		super.onCreate(arg0);
		setContentView(R.layout.activity_page);
		mAdapter = new MyAdapter(getSupportFragmentManager());
        FavoriteList fl = new FavoriteList();
        FileReader fr;
        String str=null;
        String path;
        path="/data/data/com.example.maptest/files/favorite1.txt";
       
       
        try {
            // by stream
           // FileInputStream fileInputStream;
          //  fileInputStream = openFileInput("myfile.txt");
            //byte[] readBytes = new byte[fileInputStream.available()];
            //fileInputStream.read(readBytes);
           // String readString = new String(readBytes);
           // Log.v("readString", readString);
           // String[] s = new String[3];
           // s[0] = readString;
           // mAdapter.setInitstr(s);

            // by reader
        	 BufferedReader br = new BufferedReader(new FileReader(path));

           while((str = br.readLine() ) != null){
               String[] s = str.split("\t");
               Favorite fav = new Favorite();
               fav.title = s[0];
               fav.latlng = new LatLng(Double.valueOf(s[2]),Double.valueOf(s[3]));
               fav.addres = s[1];
               fav.tag = s[4];
               int id = fl.addFavorite(fav);
               Log.d("Page Activity","load Favolist item : "+id);
           }
          

        } catch (FileNotFoundException e) {
            //String[] s = new String[3];
            //s[0] = "not found";
            //mAdapter.setInitstr(s);
            fl.addFavorite(new Favorite());        
        } catch (IOException e) {

        }
        Log.d("test", "test");
        // ファイルから読み出したFavoriteListを
        //
        mAdapter.addFavoList(fl);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

        // viewpager の上のヤツの設定
        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.strip);
        strip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        strip.setTextColor(0xff9acd32);
        strip.setTextSpacing(50);
        strip.setNonPrimaryAlpha(0.3f);
        strip.setDrawFullUnderline(true);
        strip.setTabIndicatorColor(0xff9acd32);



	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		menu.add(0, R.id.action_settings, 0, R.string.action_settings);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		// TODO 自動生成されたメソッド・スタブ
		switch (item.getItemId()) {
		case R.id.action_settings:
			Listfragment lf = (Listfragment) getSupportFragmentManager()
			.findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
			lf = (Listfragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem()); 
			lf.change_listitem();
		
			ret = true;
		}
		return ret;
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO 自動生成されたメソッド・スタブ
	}


}
