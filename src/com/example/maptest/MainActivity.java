package com.example.maptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.maptest.R.string;
import com.google.android.gms.internal.bg;
import com.google.android.gms.internal.gm;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	/** GoogleMap インスタンス. */
	private GoogleMap mMap;
	private MapFragment fmap;
	/** マーカー. */
	private Marker mMarker;
	Boolean f_select_map_kind = false;
	ProgressDialog pd;

	/* for ParseJsonpOfDirectionAPI */
	public static String posinfo = "";
	public static String info_A = "";
	public static String info_B = "";
	public ArrayList<LatLng> markerPoints;

	public static MarkerOptions options;

	public ProgressDialog progressDialog;

	public String travelMode = "driving";// default

	// for drawer
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;

	/* for routeSearch */
	private void routeSearch() {
		progressDialog.show();
		LatLng origin = markerPoints.get(0);
		LatLng dest = markerPoints.get(1);

		String url = getDirectionsUrl(origin, dest);
		DownloadTask downloadTask = new DownloadTask();

		downloadTask.execute(url);
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		String sensor = "sensor=false";

		// パラメータ
		String parameters = str_origin + "&" + str_dest + "&" + sensor
				+ "&language=ja" + "&mode=" + travelMode;

		// JSON指定
		String output = "json";

		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {
		// 非同期で取得

		@Override
		protected String doInBackground(String... url) {

			String data = "";

			try {
				// Fetching the data from web service

				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			parserTask.execute(result);
		}
	}

	/* parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				ParseJsonpOfDirectionAPI parser = new ParseJsonpOfDirectionAPI();

				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// ルート検索で得た座標を使って経路表示
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			// MarkerOptions markerOptions = new MarkerOptions();

			if (result.size() != 0) {

				for (int i = 0; i < result.size(); i++) {
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();

					List<HashMap<String, String>> path = result.get(i);

					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);
					}
					
					// ポリライン
					lineOptions.addAll(points);
					lineOptions.width(10);
					lineOptions.color(0x550000ff);

				}

				// 描画
				mMap.addPolyline(lineOptions);
			} else {
				mMap.clear();
				Toast.makeText(MainActivity.this, "ルート情報を取得できませんでした",
						Toast.LENGTH_LONG).show();
			}
			progressDialog.hide();

		}
	}

	/* end of for routemap */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		menu.add(0, R.id.action_settings, 0, R.string.action_settings);
		menu.add(0, R.id.action_settings2, 0, R.string.action_settings2);
		menu.add(0, R.id.action_settings3, 0, R.string.action_settings3);
		menu.add(0, R.id.action_settings4, 0, R.string.action_settings4);
		menu.add(0, R.id.action_settings5, 0, R.string.action_settings5);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		Bg_setUpMap bg_map = new Bg_setUpMap();

		// TODO 自動生成されたメソッド・スタブ
		switch (item.getItemId()) {
		case R.id.action_settings:
			Log.d("Menu", "item1 selected");
			f_select_map_kind = true;
			ret = true;
			mMap.clear();// ピン情報を削除
			setUpMap2();
			break;
		case R.id.action_settings2:
			Log.d("Menu", "item2 selected");
			f_select_map_kind = false;
			ret = true;
			mMap.clear();// ピン情報を削除
			setUpMap2();
			break;
		case R.id.action_settings3:
			Log.d("Menu", "item3 selected");
			markerPoints.clear();
			markerPoints.add(new LatLng(36.061897, 136.222714));
			markerPoints.add(new LatLng(36.074311, 136.217229));
			routeSearch();
			ret = true;
			break;
		case R.id.action_settings4:
			startActivity(new Intent(this, SubActivity.class));
			break;
			
		case R.id.action_settings5:
			startActivity(new Intent(this, PageActivity.class));
			break;
		default:
			ret = false;
			break;
		}
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			Log.d("item navigation drawable", "test");
			return true;
		}
		return ret;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// レイアウトを読み込む

		/* for navigation drawer */
		((Button) findViewById(R.id.drawer_button)).setOnClickListener(this);
		((Button) findViewById(R.id.drawer_button2)).setOnClickListener(this);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
				R.drawable.ic_drawer, R.string.hello_world,
				R.string.hello_world) {
			@Override
			public void onDrawerClosed(View drawerView) {
				Log.i("navigation drawer", "onDrawerClosed");
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				Log.i("navigation drawer", "onDrawerOpened");
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// ActionBarDrawerToggleクラス内の同メソッドにてアイコンのアニメーションの処理をしている。
				// overrideするときは気を付けること。
				super.onDrawerSlide(drawerView, slideOffset);
				Log.i("navigation drawer", "onDrawerSlide : " + slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				// 表示済み、閉じ済みの状態：0
				// ドラッグ中状態:1
				// ドラッグを放した後のアニメーション中：2
				Log.i("navigation drawer", "onDrawerStateChanged  new state : "
						+ newState);
			}
		};
		mDrawer.setDrawerListener(mDrawerToggle);

		// UpNavigationアイコン(アイコン横の<の部分)を有効に
		// NavigationDrawerではR.drawable.drawerで上書き
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// UpNavigationを有効に
		getActionBar().setHomeButtonEnabled(true);

		pd = new ProgressDialog(this);
		// インジケータのメッセージ
		pd.setMessage("Loading...");
		// インジケータのタイプ
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// camera位置の定義(福井県に設定)
		CameraPosition cameraPos = new CameraPosition.Builder()
				.target(new LatLng(36.065219, 136.221642)).zoom(15.0f)
				.bearing(0).build();


		/* for route search */
		markerPoints = new ArrayList<LatLng>();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("検索中だす......");
		progressDialog.hide();
		setUpMapIfNeeded();

		// カメラ移動のアニメーション
		if(mMap!=null)
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
		BufferedReader br = null; // 読み出しバッファ
		InputStream is = null; // ファイル入力のストリーム
		StringBuffer sb = new StringBuffer(); // 読み出しバッファから取り出したデータの格納用バッファ

		// ファイルのオープンクローズ,および読み出しは例外を投げるので
		// tryのブロック内に記述しキャッチしてあげないとダメ
		try {
			try {
				if (f_select_map_kind)
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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
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
			Log.d("map fragment test", "1.1");
			
			// findfragmentbyidメソッドがnullの可能性がある場合は
			// 以下のように分離した形で書いたほう安全．
			// 一文にまとめて書くと，fragmentのインスタンスがnullの時，getMapメソッドで
			// ヌルポで落ちる．
			
			// FragmentManager manager = (FragmentManager)
			// getSupportFragmentManager();
			// SupportMapFragment frag = (SupportMapFragment) manager
			// .findFragmentById(R.id.map);
			// mMap = frag.getMap();

			// google mapのインスタンスを取得するのに，getfragmentmanagerを使う方法と
			// getsupportfragmentmanagerを使う２種類がある．
			// layoutのxml内で，MapfragmentとsupportMapfragmentのどちらを利用しているかを確認すること．
			// getsupportxxは，honeycomb以前のOSにも対応できる仕組みで，現在はそれを使っている．
			
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap2();
			}

		}
	}

	/**
	 * Map をセットアップする.
	 * (現時点では，非同期で設定するBg_setUpMapを使用しているため
	 * このメソッドは使っていない
	 */
	private void setUpMap() {
		progressDialog.show();
		// 座標とマーカの宣言
		// LatLng location = new LatLng(35.697261, 139.774728);
		MarkerOptions options = new MarkerOptions();

		/* ハードコーディングによるマーカの設定 */
		/*
		 * // マーカーの設定1 options.position(location); // マーカ座標の指定
		 * options.title("クラスメソッド株式会社"); // マーカのタイトル
		 * options.snippet(location.toString()); // マーカの詳細？ BitmapDescriptor
		 * icon = BitmapDescriptorFactory .fromResource(R.drawable.ic_logo); //
		 * アイコン画像の宣言 options.icon(icon); // アイコン画像をマーカに設定 mMarker =
		 * mMap.addMarker(options); // 設定したマーカを地図に追加
		 * 
		 * // マーカーの設定2 options.position(new LatLng(43.063968, 141.347899));
		 * options.title("札幌市"); options.snippet(new LatLng(43.063968,
		 * 141.347899).toString()); mMarker = mMap.addMarker(options);
		 * 
		 * // マーカーの設定3 options.position(new LatLng(36.065219, 136.221642));
		 * options.title("福井市"); options.snippet(new LatLng(36.065219,
		 * 136.221642).toString()); mMarker = mMap.addMarker(options);
		 */

		/* ファイルからマーカの情報を読みだして設定 */
		BufferedReader br = null; // inputstreamから読みだしに使うバッファ
		InputStream is = null; // ファイル読み出し用のストリーム
		StringBuffer sb = new StringBuffer(); // 読みだしたデータを格納するバッファ
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_logo); // アイコン画像の宣言
		// options.icon(icon); // アイコン画像をマーカに設定
		try {
			try {
				if (f_select_map_kind)
					is = getAssets().open("test1.txt");
				else
					is = getAssets().open("test2.txt");
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while ((str = br.readLine()) != null) {
					// 取り出した行ごとに処理
					sb.append(str + "\n");

					String[] data = str.split("\t", 0);
					// String tmp;
					// tmp = "";
					// 半角スペースで区切ったものを再連結
					// for(int i = 0;i < data.length; i++)
					// tmp = tmp.concat(data[i]);

					// 全角で分割
					// data = tmp.split("　",7);

					// dataはtab(\t)で区切られたデータになっている
					if (f_select_map_kind) {
						options.title(data[1]);
						options.position(new LatLng(Float.valueOf(data[5]),
								Float.valueOf(data[6])));
						options.snippet(data[2]);

					} else {
						options.title(data[1]);
						options.position(new LatLng(Float.valueOf(data[4]),
								Float.valueOf(data[5])));
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
		progressDialog.hide();
		// マーカの表示をカスタマイズ
		// mMap.setInfoWindowAdapter(new CustomInfoAdapter());

	}
	private void setUpMap2() {
		progressDialog.show();
		Bg_setUpMap bg_map = new Bg_setUpMap();

		bg_map.execute();
	}

	/*
	 * 非同期な（バックグラウンドで実行可能な）マーカー設定クラス
	 * 
	 * f_select_map_kindに応じて
	 * 
	 * 1:test1.txt
	 * 2:test2.txt
	 * 
	 * からマーカの情報を取得しマーカーオプションリストを生成し，
	 * 最後にマップにマーカを配置する．テキストからマーカーを取得する
	 * 際はバックグラウンド，最後のマーカーの配置は最後に実行される．
	 * 
	 * 
	 * 
	 * */
	
	private class Bg_setUpMap extends AsyncTask<Void, Void, ArrayList<MarkerOptions>> {

		String m_str;
		@Override
		protected ArrayList<MarkerOptions> doInBackground(Void... params) {
			MarkerOptions m_options = null;

			ArrayList<MarkerOptions> markerlist = new ArrayList<MarkerOptions>();
			/* ファイルからマーカの情報を読みだして設定 */
			BufferedReader br = null; // inputstreamから読みだしに使うバッファ
			InputStream is = null; // ファイル読み出し用のストリーム
			StringBuffer sb = new StringBuffer(); // 読みだしたデータを格納するバッファ
			// BitmapDescriptor icon = BitmapDescriptorFactory
			// .fromResource(R.drawable.ic_logo); // アイコン画像の宣言
			// options.icon(icon); // アイコン画像をマーカに設定
			try {
				try {
					if (f_select_map_kind)
						is = getAssets().open("test1.txt");
					else
						is = getAssets().open("test2.txt");
					br = new BufferedReader(new InputStreamReader(is));
					String str;
					while ((str = br.readLine()) != null) {
						m_options = new MarkerOptions();
						// 取り出した行ごとに処理
						sb.append(str + "\n");
						String[] data = str.split("\t", 0);

						if (f_select_map_kind) {
							m_options.title(data[1]);
							m_options.position(new LatLng(Float
									.valueOf(data[5]), Float.valueOf(data[6])));
							m_options.snippet(data[2]);

						} else {
							m_options.title(data[1]);
							m_options.position(new LatLng(Float
									.valueOf(data[4]), Float.valueOf(data[5])));
							m_options.snippet(data[2]);

						}
						markerlist.add(m_options);
					}
				} finally {
					if (br != null)
						br.close();
				}
			} catch (IOException io) {
			}
			m_str = new String();
			m_str = sb.toString();
			
			return markerlist;
			// TODO 自動生成されたメソッド・スタブ
		}

		@Override
		protected void onPostExecute(ArrayList<MarkerOptions> result) {
			// text view のレイアウトを読み込み

			if(result.size()!=0 && mMap!=null)
			for(int i =0;i < result.size();i++){
				mMap.addMarker(result.get(i));
			}
			// text view のレイアウトを読み込み
			TextView label = (TextView) findViewById(R.id.txt);
			// viewにテキストを設定
			label.setText(m_str);
			
			progressDialog.hide();
		}
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

			// if (marker.equals(mMarker)) {
			// if(marker.getId()){
			// 画像
			ImageView badge = (ImageView) view.findViewById(R.id.badge);
			badge.setImageResource(R.drawable.ic_logo);
			Log.d("Custominfowindow", "Mmarker==selected maker");
			// }
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView snippet = (TextView) view.findViewById(R.id.snippet);
			title.setText(marker.getTitle());
			snippet.setText(marker.getSnippet() + marker.getId());
			Log.d("Custominfowindow", mMarker.getId());
		}

	}

	/*for button in navigation drawer*/
	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch (v.getId()) {
		case R.id.drawer_button:
			f_select_map_kind = true;
			mMap.clear();
			setUpMap2();
			mDrawer.closeDrawers();
			break;
		case R.id.drawer_button2:
			f_select_map_kind = false;
			mMap.clear();
			mDrawer.closeDrawers();
			setUpMap2();
			break;
		default:
			break;
		}
	}
}