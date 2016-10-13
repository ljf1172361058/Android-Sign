package com.example.testmap;


import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.adatper.StatusExpandAdapter;
import com.example.entity.ChildStatusEntity;
import com.example.entity.GroupStatusEntity;
import com.example.utils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import okhttp3.Call;

/**
 * AMapV2地图中介绍定位三种模式的使用，包括定位，追随，旋转
 */
public class MainActivity extends Activity implements LocationSource,
AMapLocationListener,OnCheckedChangeListener {
	
	private AMap aMap;
	private MapView mapView;
	private Button mBut;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	
	private ExpandableListView expandlistView;
	private StatusExpandAdapter statusAdapter;
	private MarkerOptions markerOption;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapView = (MapView) findViewById(R.id.map);
		mBut = (Button) findViewById(R.id.sign_IO);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		initExpandListView();
		initData();
	}
	/**
	 * 初始化可拓展列表
	 */
	private void initExpandListView() {
		statusAdapter = new StatusExpandAdapter(this, getListData());
		expandlistView.setAdapter(statusAdapter);
		expandlistView.setGroupIndicator(null); // 去掉默认带的箭头
		expandlistView.setSelection(0);// 设置默认选中项

		// 遍历所有group,将所有项设置成默认展开
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);//expandGroup:展开group collapseGroup:收起gtroup
		}

		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	
	List<GroupStatusEntity> groupList;
	private List<GroupStatusEntity> getListData() {
		String[] groupArray = new String[] { "考勤时间"};
		String[][] childArray = new String[][] {{ "09:00-18:00"}};
//		String[] strArray = new String[] { "考勤时间", "签到", "签退","完成"};
//		String[][] childTimeArray = new String[][] {
//				{ "09:00-18:00"},{ "北京西路静安中华大厦靠近中信银行"},{ "北京西路静安中华大厦靠近中信银行"},{"工作辛苦了,好好休息!"}};
		groupList = new ArrayList<GroupStatusEntity>();
		for (int i = 0; i < groupArray.length; i++) {
			GroupStatusEntity groupStatusEntity = new GroupStatusEntity();
			groupStatusEntity.setGroupName(groupArray[i]);

			List<ChildStatusEntity> childList = new ArrayList<ChildStatusEntity>();

			for (int j = 0; j < childArray[i].length; j++) {
				ChildStatusEntity childStatusEntity = new ChildStatusEntity();
				childStatusEntity.setAddress(childArray[i][j]);
				childList.add(childStatusEntity);
			}

			groupStatusEntity.setChildList(childList);
			groupList.add(groupStatusEntity);
		}
		return groupList;
	}
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
//		MyLocationStyle myLocationStyle = new MyLocationStyle();
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
//		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	    //aMap.setMyLocationType()
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
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
	
	private String address="";
	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);//注册定位监听
				markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
						.position(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude())).title(amapLocation.getAddress()!=""?address=amapLocation.getAddress():address)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
						.draggable(false);
				if(isAuto){//是否是自动定位
					markerOption.snippet("初始位置");
					//请求签到或签退成功后再添加标记
					Marker marker=aMap.addMarker(markerOption);
					marker.showInfoWindow();//设置默认显示一个infowinfow
				}
				else{//是否是手动定位
					final String str=mBut.getText().toString();
					call = OkHttpUtils
						    .post()
						    .url(url)//签到
						    .addParams("user_id", "1")
						    .addParams("address",address)
						    .build();
					call.execute(new StringCallback() {
						@Override
						public void onResponse(String string, int arg1) {
							// TODO Auto-generated method stub
							//ToastUtil.show(MainActivity.this,string);
							Log.i("test",string+"==="+string.length());
							Gson gson=new Gson();
							GroupStatusEntity  groupStatusEntity= gson.fromJson(string,GroupStatusEntity.class);
							if(groupStatusEntity!=null&&groupStatusEntity.isSucceed()){//响应结果是否正确
								groupList.add(groupStatusEntity);
								//将最后一个分组展开 为了提高效率就不采用for循环一个一个判断
								expandlistView.expandGroup(groupList.size()-1);
								if("签到".equals(str)){
									ToastUtil.show(MainActivity.this,"签到成功");
									mBut.setText("签退");
								}
								else if("签退".equals(str)){
									ToastUtil.show(MainActivity.this,"签退成功");
									mBut.setVisibility(View.GONE);
									GroupStatusEntity  groupStatusEntity01=new GroupStatusEntity();
									List<ChildStatusEntity> childList = new ArrayList<ChildStatusEntity>();
									ChildStatusEntity childStatusEntity = new ChildStatusEntity();
									childStatusEntity.setAddress("工作辛苦了,好好休息!");
									childList.add(childStatusEntity);
									groupStatusEntity01.setGroupName("完成");
									groupStatusEntity01.setChildList(childList);
									groupList.add(groupStatusEntity01);
									//将最后一个分组展开 为了提高效率就不采用for循环一个一个判断
									expandlistView.expandGroup(groupList.size()-1);
								}
								//请求签到或签退成功后再添加标记
								Marker marker=aMap.addMarker(markerOption);
								marker.showInfoWindow();//设置默认显示一个infowinfow
								//通知适配器发送改变
								statusAdapter.notifyDataSetChanged();
							}
							else{
								ToastUtil.show(MainActivity.this,str+"失败");
							}
							if(ProgressDialogUtils.isShowing()){
								ProgressDialogUtils.dismiss();
							}
						}
						@Override
						public void onError(Call arg0, Exception e, int arg2) {
							// TODO Auto-generated method stub
							Log.e("onError:",e.getMessage());
							if(ProgressDialogUtils.isShowing()){
								ProgressDialogUtils.dismiss();
							}
						}
					});
				}
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("test",errText);
			}
			Log.i("test",aMap.getMapScreenMarkers().size()+"个");
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置是否只定位一次,默认为false
	        mLocationOption.setOnceLocation(true);
	        //设置定位间隔,单位毫秒,默认为2000ms
	        mLocationOption.setInterval(10000);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}
	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}
	String url="";
	boolean isAuto=true;//是否是自动定位
	public static RequestCall call;
	/**
	 * 签到签退接口
	 */
	public void sign_IO(View view){
		//手动定位
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置是否只定位一次,默认为false
	        mLocationOption.setOnceLocation(true);
	        //设置定位间隔,单位毫秒,默认为2000ms
	        mLocationOption.setInterval(10000);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
		else{
			mlocationClient.startLocation();
		}
		isAuto=false;
		String str=mBut.getText().toString();
		//进行网络请求
		if("".equals(address)||address.length()<1){
			ToastUtil.show(MainActivity.this,"请先定位");
			return;
		}
		if("签到".equals(str)){
			url="请填写你自己签到接口地址";
		}else if("签退".equals(str)){
			url="请填写你自己签退接口地址";
		}
	    ProgressDialogUtils.show(true,false,this,"",str+"中...",0,str+"已取消");
	}
	private void initData(){
		
	}
 }
