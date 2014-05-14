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
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v4.app.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
	/** GoogleMap インスタンス. */
	private GoogleMap mMap;

	/** マーカー. */
	private Marker mMarker;
	Boolean f_select_map_kind = false;
	ProgressDialog pd;

	/*for ParseJsonpOfDirectionAPI*/
    public static String posinfo = "";
    public static String info_A = "";
    public static String info_B = "";
    public ArrayList<LatLng> markerPoints;
    
    public static MarkerOptions options;
    
    public ProgressDialog progressDialog;
    
    public String travelMode = "driving";//default
    
    /* for routeSearch*/
    private void routeSearch(){
    	Log.d("MainActivity", "routeSearch");
        progressDialog.show();
        Log.d("MainActivity", "routeSearch");
        LatLng origin = markerPoints.get(0);
        LatLng dest = markerPoints.get(1);
        Log.d("MainActivity", "routeSearch");
        
        String url = getDirectionsUrl(origin, dest);
        Log.d("MainActivity", "routeSearch");
        DownloadTask downloadTask = new DownloadTask();
        Log.d("MainActivity", "routeSearch");
        
        downloadTask.execute(url);
        Log.d("MainActivity", "routeSearch");
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        
        String sensor = "sensor=false";

        //パラメータ
        String parameters = str_origin+"&"+str_dest+"&"+sensor + "&language=ja" + "&mode=" + travelMode;

        //JSON指定
        String output = "json";

        
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            
            urlConnection = (HttpURLConnection) url.openConnection();

            
            urlConnection.connect();

            
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    
    private class DownloadTask extends AsyncTask<String, Void, String>{
    //非同期で取得
        
        @Override
        protected String doInBackground(String... url) {

            
            String data = "";

            try{
                // Fetching the data from web service
            	
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
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

    /*parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                ParseJsonpOfDirectionAPI parser = new ParseJsonpOfDirectionAPI();

                
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        //ルート検索で得た座標を使って経路表示
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            
            
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            
            if(result.size() != 0){
                
                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
    
                    
                    List<HashMap<String, String>> path = result.get(i);
    
                    
                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);
    
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
    
                        points.add(position);
                    }
    
                    //ポリライン
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(0x550000ff);
                    
                }
            
                //描画
                mMap.addPolyline(lineOptions);
            }else{
                mMap.clear();
                Toast.makeText(MainActivity.this, "ルート情報を取得できませんでした", Toast.LENGTH_LONG).show();
            }
            progressDialog.hide();
            
        }
    }
    
    /*end of for routemap*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		menu.add(0,R.id.action_settings,0,R.string.action_settings);
		menu.add(0,R.id.action_settings2,0,R.string.action_settings2);
		menu.add(0,R.id.action_settings3,0,R.string.action_settings3);
		menu.add(0,R.id.action_settings4,0,R.string.action_settings4);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		// TODO 自動生成されたメソッド・スタブ
		switch(item.getItemId()){
		case R.id.action_settings:
			Log.d("Menu", "item1 selected");
			f_select_map_kind = true;
			ret = true;
			break;
		case R.id.action_settings2 :
			Log.d("Menu", "item2 selected");
			f_select_map_kind = false;
			ret = true;
			break;
		case R.id.action_settings3 :
			Log.d("Menu", "item3 selected");
			markerPoints.add(new LatLng(36.061897,136.222714));
			markerPoints.add(new LatLng(36.074311,136.217229));
			Log.d("Menu", "item3 selected");
			routeSearch();
			ret = true;
			break;
		case R.id.action_settings4 :
			startActivity(new Intent(this,SubActivity.class));
			break;
			default : ret = false;
						break;
		}

		// インジケータを表示
		
		
		// ピン情報を削除
		mMap.clear();
		// ピンの再配置
		setUpMap();
		
		//pd.dismiss();
		return ret;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// レイアウトを読み込む
		pd = new ProgressDialog(this);
		// インジケータのメッセージ
		pd.setMessage("Loading...");
		// インジケータのタイプ
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		setUpMapIfNeeded();

		// camera位置の定義(福井県に設定)
		CameraPosition cameraPos = new CameraPosition.Builder()
				.target(new LatLng(36.065219, 136.221642)).zoom(15.0f)
				.bearing(0).build();
		
		/*for route search*/
		markerPoints= new ArrayList<LatLng>();
		 progressDialog = new ProgressDialog(this);
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setMessage("検索中だす......");
	        progressDialog.hide();

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
		//	FragmentManager manager = (FragmentManager) getSupportFragmentManager();
		//	SupportMapFragment frag = (SupportMapFragment) manager
		//			.findFragmentById(R.id.map);
		//	mMap = frag.getMap();
			mMap =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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
					// 取り出した行ごとに処理
					sb.append(str + "\n");
					
					String[] data = str.split("\t",0);
					//String tmp;
					//tmp = "";
					// 半角スペースで区切ったものを再連結
					//for(int i = 0;i < data.length; i++)
					//	tmp = tmp.concat(data[i]);
					
					// 全角で分割
					//data = tmp.split("　",7);
					
					
					// dataは全角及び半角で分割されたトークンの配列になっている
					if(f_select_map_kind){
						options.title(data[1]);
						options.position(new LatLng(Float.valueOf(data[5]),Float.valueOf(data[6])));
						options.snippet(data[2]);
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
		mMap.setInfoWindowAdapter(new CustomInfoAdapter());
		
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