package com.example.maptest;

import com.example.maptest.Listfragment.OnFragmentInteractionListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PageActivity extends FragmentActivity implements
		OnFragmentInteractionListener {

	MyAdapter mAdapter;
	ViewPager mPager;
	ListView lv;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_page);
		mAdapter = new MyAdapter(getSupportFragmentManager());

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
		
			return true;
		}
		return false;
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO 自動生成されたメソッド・スタブ
	};
}
