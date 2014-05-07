package com.example.maptest;

import android.support.v4.app.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.content.Intent;

public class SplashActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
        	@Override
        	public void run() {
        		startActivity(new Intent(getApplication(), MainActivity.class));
        		finish();
        	}
	}, 1500);//1500ms後に画面遷移する
    }
}