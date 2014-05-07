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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	/** GoogleMap インスタンス. */
	private GoogleMap mMap;

	/** マーカー. */
	private Marker mMarker;

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
		BufferedReader br = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		try {
			try {
				is = getAssets().open("test.txt");
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
		TextView label = (TextView) this.findViewById(R.id.txt);
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
		//StringBuffer sb = new StringBuffer(); // 読みだしたデータを格納するバッファ
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_logo);    // アイコン画像の宣言
		//options.icon(icon);							   // アイコン画像をマーカに設定
		try {
			try {
				is = getAssets().open("test.txt");
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while ((str = br.readLine()) != null) {
					// 取り出した行ごとに処理
					String[] data = str.split(",",4);
					
					options.title(data[0]);
					options.position(new LatLng(Float.valueOf(data[1]),Float.valueOf(data[2])));
					options.snippet(data[3]);
					mMarker = mMap.addMarker(options);
					// 単純に保存用バッファに格納
					//sb.append(str + "\n");
				}
			} finally {
				if (br != null)
					br.close();
			}

		} catch (IOException io) {

		}
		
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