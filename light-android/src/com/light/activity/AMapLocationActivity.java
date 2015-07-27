package com.light.activity;

import io.rong.message.LocationMessage;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.util.SessionUtils;

public class AMapLocationActivity extends BaseActivity implements
		LocationSource, AMapLocationListener {

	private static final String TAG = "AMapLocationActivity";

	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	LocationMessage mMsg;

	private Marker mGPSMarker;

	@ViewInject(R.id.btn_send_location)
	private Button sendLocation;
	
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;
	
	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_amaplocation, TAG);

		mapView = (MapView) findViewById(R.id.map);
		if (getIntent().hasExtra("location")) {
			mMsg = getIntent().getParcelableExtra("location");
		}
		mapView.onCreate(savedInstanceState);// 此方法必须重写

		init();


		if (mMsg != null)
			sendLocation.setVisibility(View.GONE);

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
		title.setText("位置");
		
		backIv.setImageResource(R.drawable.icons_back);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (mListener != null && location != null && mMsg == null) {
			mListener.onLocationChanged(location);// 显示系统小蓝点

			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}

			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					geoLat, geoLng), 16));

			desc = desc.replaceAll("\\s+", "");
			Uri uri = Uri.parse("http://m.amap.com/?q=" + geoLat + "," + geoLng
					+ "&name=" + desc);

			Log.d("uri", uri.toString());

			mMsg = LocationMessage.obtain(geoLat, geoLng, desc, uri);
		}

	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null && mMsg == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		} else {

			mGPSMarker = aMap.addMarker(new MarkerOptions().icon(

					BitmapDescriptorFactory.fromBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.location_marker))).anchor(
					(float) 0.5, (float) 0.5));
			LatLng point = new LatLng(mMsg.getLat(), mMsg.getLng());
			mGPSMarker.setPosition(point);

			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@OnClick(R.id.btn_send_location)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_location:
			if (mMsg != null) {
				SessionUtils.getInstance().getLastLocationCallback()
						.onSuccess(mMsg);
				SessionUtils.getInstance().setLastLocationCallback(null);
				finish();
			} else {
				SessionUtils.getInstance().getLastLocationCallback()
						.onFailure("定位失败");
			}
			break;
		}
	}

}
