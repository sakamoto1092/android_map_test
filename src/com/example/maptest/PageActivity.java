package com.example.maptest;

import com.example.maptest.Listfragment.OnFragmentInteractionListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PageActivity extends FragmentActivity implements
		OnFragmentInteractionListener{

	MyAdapter mAdapter;
	ViewPager mPager;
	ListView lv;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_page);
		mAdapter = new MyAdapter(getSupportFragmentManager());
        try {
            FileInputStream fileInputStream;
            fileInputStream = openFileInput("myfile.txt");
            byte[] readBytes = new byte[fileInputStream.available()];
            fileInputStream.read(readBytes);
            String readString = new String(readBytes);
            Log.v("readString", readString);
            String[] s = new String[3];
            s[0] = readString;
            mAdapter.setInitstr(s);
        } catch (FileNotFoundException e) {
            String[] s = new String[3];
            s[0] = "not found";
            mAdapter.setInitstr(s);
        } catch (IOException e) {
        }
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		Log.d("page_activity", "on create");

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
