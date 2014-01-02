package com.btten.calltaxi.CallTaxi.copy;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.btten.Jms.ClassTools;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.calltaxi.Service.LocationClientService;
import com.btten.calltaxi.Service.core.JmsMapManager;
import com.btten.ui.view.BaseView;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

public class CallTaxiView extends BaseView {
//	String[] menuTitle = { "预约用车", "现在叫车" };
//	String[][] menuContent = { { "预约出租车", "预约货车", "预约代驾", "预约陪驾" }, { "我要叫出租车", "我要叫货车", "我要找代驾", "我要找陪驾",  "救援"}};// ,
//	Button reserveMenu, callMenu;

	// baidu map
	boolean IsFresh = false;
	boolean IsLocating = false;
	final int deltaTime = 1000;
	MapView bMapView = null;
	MapController bMapController = null;
	LocationClientService bMapClient = null;
	BDLocation location = null;
	MyLocationOverlay myLocationOverlay = null;
	ItemizedOverlay<OverlayItem> jmsOverlay = null;

	// end baidu

	MainActivity root;

	public CallTaxiView(MainActivity context) {
		super();
		this.context = context;
		root = context;
		view = LayoutInflater.from(context).inflate(GetLayoutId(), null);
		view.setTag(this);

		init();
	}

	private void init() {
		SetTitle("地图网点");
//		initMenu();
		initMap();
		initLocateHandler();
	}
	//初始化地图信息，调用百度接口
	private void initMap() {
		bMapClient = LocationClientService.getInstance();
		if (!bMapClient.isStarted())
			bMapClient.start();

		bMapView = (MapView) GetView().findViewById(R.id.jms_baidu_map);
		bMapController = bMapView.getController();
		//用于在地图上标注位置
		myLocationOverlay = new MyLocationOverlay(bMapView);
		myLocationOverlay.enableCompass();
		bMapView.getOverlays().add(myLocationOverlay);
		
		//地图上的出租车图标
		Drawable maker = root.getResources().getDrawable(R.drawable.taxi);
		jmsOverlay = new ItemizedOverlay<OverlayItem>(maker, bMapView);
		bMapView.getOverlays().add(jmsOverlay);

		//可以缩放
		bMapView.setBuiltInZoomControls(true);
	}

	boolean isFirstStart = true;
/*	//获取司机位置信息
	private void getDrivers(double longitude, double latitude) {
		// TODO Auto-generated method stub
		JmsMapManager.getInstance().RequestNearBy(longitude, latitude);
	}*/
	//获取jms目前位置信息
	private void getJmsAddr(double longitude, double latitude) {
		// TODO Auto-generated method stub
		JmsMapManager.getInstance().RequestNearBy(longitude, latitude);
	}
	
/*	public void setDrivers(List<OverlayItem> drivers_pos) {
		if (drivers_pos.size() == 0)
		{
			return;
		}
		if (driversOverlay != null) 
		{
			driversOverlay.addItem(drivers_pos);
		}
	}*/
	
	public void setArroundJmsAddr(List<OverlayItem> aroundjms_pos) {
		if (aroundjms_pos.size() == 0)
		{
			return;
		}
		if (jmsOverlay != null) 
		{
			jmsOverlay.addItem(aroundjms_pos);
		}
	}
	
	Handler locateHandler = null;

	private void initLocateHandler() {
		locateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what != ONHIDE) {
					LocationData loc = (LocationData) msg.obj;

					GeoPoint centerPoint = new GeoPoint(
							(int) (loc.latitude * 1E6),//经度
							(int) (loc.longitude * 1E6));//纬度
					//如果已经连接，且是第一次打开地图。
					if (JmsMapManager.getInstance().IsConnect()
							&& isFirstStart) {
						isFirstStart = false;
						ClassTools.getInstance().put(
								ClassTools.NEAR_BY_DRIVER_POSITION,
								CallTaxiView.this);
						//取得附近的加盟店的信息
						getJmsAddr(loc.longitude, loc.latitude);
					}

					if (IsFresh) {
						bMapController.setCenter(centerPoint);
						bMapController.setZoom(14);
						IsFresh = false;
					}

					if (IsLocating) {
						myLocationOverlay.setData(loc);
						bMapView.refresh();
					}
				} else {
					bMapView.setVisibility(View.GONE);
				}

			}
		};
	}

	Thread locateThread = null;
	private static final int ONHIDE = 19091;

	private void startLocateThread() {
		IsFresh = true;
		IsLocating = true;
		if (locateThread == null) {
			locateThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					while (IsLocating) {
						bMapClient.requestLocation();
						if ((location = bMapClient.getLocation()) != null) {
							LocationData loc = new LocationData();
							loc.direction = location.getDerect();
							loc.latitude = location.getLatitude();
							loc.longitude = location.getLongitude();
							loc.accuracy = 3000; // 半径3000m

							location = null;

							Message msg = locateHandler.obtainMessage();
							msg.obj = loc;
							locateHandler.sendMessage(msg);
						}
						try {
							Thread.sleep(deltaTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
					}

					Message msg = locateHandler.obtainMessage();
					msg.what = ONHIDE;
					locateHandler.sendMessage(msg);
				}
			});
			locateThread.start();
		}
	}

	private void stopLocateThread() {
		IsLocating = false;
		locateThread.interrupt();
		locateThread = null;
	}

	// initMenu
	/*private void initMenu() {
		reserveMenu = (Button) GetView().findViewById(
				R.id.reserve_taxi_buttonLayout);
		reserveMenu.setText(menuTitle[0]);
		callMenu = (Button) GetView().findViewById(R.id.call_taxi_buttonLayout);
		callMenu.setText(menuTitle[1]);
		reserveMenu.setOnClickListener(reserveMenuTitleListener);
		callMenu.setOnClickListener(callMenuTitleListener);
	}

	OnItemClickListener MenuItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(root, CallTaxiPublishActivity.class);
			intent.putExtra("text", ((TextView) view).getText().toString().trim());
			root.startActivity(intent);
		}
	};

	OnClickListener reserveMenuTitleListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ListMenuPop temp = new ListMenuPop(context);
			temp.setTitle(menuTitle[0]);
			temp.setData(menuContent[0], MenuItemListener);
			temp.getPopupWindow().showAsDropDown(reserveMenu);
		}
	};

	OnClickListener callMenuTitleListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ListMenuPop temp = new ListMenuPop(context);
			temp.setTitle(menuTitle[1]);
			temp.setData(menuContent[1], MenuItemListener);
			temp.getPopupWindow().showAsDropDown(callMenu);
		}
	};*/

	@Override
	public int GetLayoutId() {
		return R.layout.calltaxiview_wangdian;
	}

	@Override
	public void OnViewShow() {
		root.tabBar.setTab(1);
		if (bMapView.getVisibility() == View.GONE) {
			bMapView.setVisibility(View.VISIBLE);
		}
		if (LocationClientService.getInstance().getMapManager() != null)
			LocationClientService.getInstance().getMapManager().start();
		bMapView.onResume();
		startLocateThread();
	}

	@Override
	public void OnViewHide() {
		stopLocateThread();
		bMapView.onPause();
		if (LocationClientService.getInstance().getMapManager() != null)
			LocationClientService.getInstance().getMapManager().stop();
	}

	@Override
	public void Backward() {
		// TODO Auto-generated method stub
		super.Backward();
		root.GoToView(0);
	}
}