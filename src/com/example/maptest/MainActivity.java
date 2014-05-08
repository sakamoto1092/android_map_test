package com.example.maptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.*;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	/** GoogleMap インスタンス. */
	private GoogleMap mMap;

	/** マーカー. */
	private Marker mMarker;
	Boolean f_select_map_kind = false;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		menu.add(0,R.id.action_settings,0,R.string.action_settings);
		menu.add(0,R.id.action_settings2,0,R.string.action_settings2);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		switch(item.getItemId()){
		case R.id.action_settings:
			Log.d("Menu", "item1 selected");
			f_select_map_kind = true;
			break;
		case R.id.action_settings2 :
			Log.d("Menu", "item2 selected");
			f_select_map_kind = false;
			break;
		}
		
		// ピン情報を削除
		mMap.clear();
		// ピンの再配置
		setUpMap();
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// レイアウトを読み込む
		setUpMapIfNeeded();

		// camera位置の定義(福井県に設定)
		CameraPosition cameraPos = new CameraPosition.Builder()
				.target(new LatLng(36.065219, 136.221642)).zoom(15.0f)
				.bearing(0).build();

		// カメラ移動のアニメーション
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));

		// マーカーを貼る緯度・経度
		// LatLng location = new LatLng(35.697261, 139.774728);

		// マーカーの設定
		// MarkerOptions options = new MarkerOptions();
		// options.position(location);
		// options.title("クラスメソッド株式会社");
		// options.snippet(location.toString());

		// マップにマーカーを追加
		// mMarker = mMap.addMarker(options);

		// google mapアプリに経路検索と表示を任せる方法
		/*
		 * String url = "http://maps.google.com/maps?dirflg=w"; url += "&saddr="
		 * + 36.594682 + "," + 136.625573 + "(現在地)"; url += "&daddr=" +
		 * 34.686297 + "," + 135.519661 + "(目的地)";
		 * 
		 * Intent intent = new Intent(); intent.setAction(Intent.ACTION_VIEW);
		 * intent.setClassName("com.google.android.apps.maps",
		 * "com.google.android.maps.MapsActivity");
		 * intent.setData(Uri.parse(url)); startActivity(intent);
		 */
		
		// textview にtest.txtの内容を格納して表示
		BufferedReader br = null;				// 読み出しバッファ
		InputStream is = null;					// ファイル入力のストリーム
		StringBuffer sb = new StringBuffer(); // 読み出しバッファから取り出したデータの格納用バッファ
		
		// ファイルのオープンクローズ,および読み出しは例外を投げるので
		// tryのブロック内に記述しキャッチしてあげないとダメ
		try {
			try {
				if(f_select_map_kind)
					is = getAssets().open("test1.txt");
				else
					is = getAssets().open("test2.txt");
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str + "\n");
				}
			} finally {
				if (br != null)
					br.close();
			}

		} catch (IOException io) {

		}
		
		// text view のレイアウトを読み込み
		TextView label = (TextView) this.findViewById(R.id.txt);
		// viewにテキストを設定
		label.setText(sb.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		Log.d("MainActivity", "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("MainActivity", "onResume");
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			FragmentManager manager = (FragmentManager) getSupportFragmentManager();
			SupportMapFragment frag = (SupportMapFragment) manager
					.findFragmentById(R.id.map);
			mMap = frag.getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * Map をセットアップする.
	 */
	private void setUpMap() {

		// 座標とマーカの宣言
		LatLng location = new LatLng(35.697261, 139.774728);
		MarkerOptions options = new MarkerOptions();
		
		/*ハードコーディングによるマーカの設定*/
		/*
		// マーカーの設定1
		options.position(location); 					// マーカ座標の指定
		options.title("クラスメソッド株式会社");			// マーカのタイトル
		options.snippet(location.toString());			// マーカの詳細？
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_logo);    // アイコン画像の宣言
		options.icon(icon);							   // アイコン画像をマーカに設定
		mMarker = mMap.addMarker(options);			   // 設定したマーカを地図に追加

		// マーカーの設定2
		options.position(new LatLng(43.063968, 141.347899));
		options.title("札幌市");
		options.snippet(new LatLng(43.063968, 141.347899).toString());
		mMarker = mMap.addMarker(options);

		// マーカーの設定3
		options.position(new LatLng(36.065219, 136.221642));
		options.title("福井市");
		options.snippet(new LatLng(36.065219, 136.221642).toString());
		mMarker = mMap.addMarker(options);
		 */
		
		
		/*ファイルからマーカの情報を読みだして設定*/
		BufferedReader br = null;	// inputstreamから読みだしに使うバッファ
		InputStream is = null;		// ファイル読み出し用のストリーム
		StringBuffer sb = new StringBuffer(); // 読みだしたデータを格納するバッファ
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_logo);    // アイコン画像の宣言
		//options.icon(icon);							   // アイコン画像をマーカに設定
		try {
			try {
				if(f_select_map_kind)
					is = getAssets().open("test1.txt");
				else
					is = getAssets().open("test2.txt");
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str + "\n");
					// 取り出した行ごとに処理
					String[] data = str.split(" |　",4);
					if(f_select_map_kind){
						options.title(data[1]);
						options.position(new LatLng(Float.valueOf(data[5]),Float.valueOf(data[6])));
						options.snippet(data[2]+data[4]);
					}else{
						options.title(data[1]);
						options.position(new LatLng(Float.valueOf(data[4]),Float.valueOf(data[5])));
						options.snippet(data[2]);
					}
					mMarker = mMap.addMarker(options);
					// 単純に保存用バッファに格納
					
				}
			} finally {
				if (br != null)
					br.close();
			}

		} catch (IOException io) {

		}
		// text view のレイアウトを読み込み
		TextView label = (TextView) this.findViewById(R.id.txt);
		// viewにテキストを設定
		label.setText(sb.toString());
		
		// マーカの表示をカスタマイズ
		//mMap.setInfoWindowAdapter(new CustomInfoAdapter());

	}

	private class CustomInfoAdapter implements InfoWindowAdapter {

		/** Window の View. */
		private final View mWindow;

		/**
		 * コンストラクタ.
		 */
		public CustomInfoAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			return null;
		}

		/**
		 * InfoWindow を表示する.
		 * 
		 * @param marker
		 *            {@link Marker}
		 * @param view
		 *            {@link View}
		 */
		private void render(Marker marker, View view) {
			// ここでどの Marker がタップされたか判別する
			if (marker.equals(mMarker)) {
				// 画像
				ImageView badge = (ImageView) view.findViewById(R.id.badge);
				badge.setImageResource(R.drawable.ic_logo);
			}
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView snippet = (TextView) view.findViewById(R.id.snippet);
			title.setText(marker.getTitle());
			snippet.setText(marker.getSnippet());
		}

	}
}