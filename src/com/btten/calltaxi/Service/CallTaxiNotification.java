package com.btten.calltaxi.Service;

import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.R;
import com.btten.calltaxi.Service.core.JmsMapManager;
import com.btten.calltaxi.Service.core.DriverState;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

public class CallTaxiNotification {
	private static CallTaxiNotification instance = new CallTaxiNotification();

	public static CallTaxiNotification getInstance() {
		return instance;
	}
	//工具栏上面显示的图标
	static final int ICON_ID = R.drawable.notification_icon;
	static final String TITLE = "惠车宝";

	NotificationManager notificationManager = null;
	Notification notification = null;
	LayoutInflater inflater = null;
	RemoteViews notifyView = null;

	private CallTaxiNotification() {
		inflater = (LayoutInflater) BtAPPJMS.getInstance().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		notificationManager = (NotificationManager) BtAPPJMS.getInstance()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyView = new RemoteViews(BtAPPJMS.getInstance().getPackageName(),
				R.layout.notification);
		notifyView.setTextViewText(R.id.notify_title, TITLE);
	}

	private PendingIntent getPendIntent() {
		// TODO Auto-generated method stub
		Intent intent = null;
		try {
			intent = new Intent(BtAPPJMS.getInstance(),
					Class.forName("com.btten.calltaxi.SplashScreen"));
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PendingIntent pendingIntent = null;

		if (intent != null) {
			pendingIntent = PendingIntent.getActivity(BtAPPJMS.getInstance(), 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}

		return pendingIntent;
	}

	private String getState() {
		// TODO Auto-generated method stub
		/*String state = null;
		String loginState = null;*/
		String driverState = null;
		
		/*if (AccountManager.getInstance().IsLogin()){
			loginState = "已登录";
		}
		else{
			loginState = "未登录";
		}*/
		JmsMapManager manager = JmsMapManager.getInstance();
		if (manager.GetIsDriver()){
			String typeStr = "";
			String stateStr = "";
			if (manager.getDriverType() == 0)
				typeStr = "出租车";
			else if (manager.getDriverType() == 1)
					typeStr = "货车";
			else if (manager.getDriverType() == 2)
				typeStr = "代驾";
			else if (manager.getDriverType() == 3)
				typeStr = "陪驾";
			else if (manager.getDriverType() == 6)
				typeStr = "代陪驾";
			else if (manager.getDriverType() == 6)
				typeStr = "救援";
			
			if(manager.GetDriverState().equals(DriverState.DriverIdle))
				stateStr = "空闲";
			else if(manager.GetDriverState().equals(DriverState.DriverBusy))
				stateStr = "繁忙";
			
			driverState = "，"+stateStr+"("+typeStr+")";
		}
		else 
			driverState = "";
		
		return driverState;
	}

	String title = "未连接";
	String tickerText = TITLE;

	public void LoginApp() {
		tickerText = TITLE + ":" + "登陆成功";
		showNotification();
	}

	public void RegistApp() {
		tickerText = TITLE + ":" + "注册成功";
		showNotification();
	}

	public void LogoutApp() {
		tickerText = TITLE + ":" + "注销登录";
		showNotification();
		cancelNotification();
	}

	public void MarkNotification() {
		tickerText = TITLE + ":" + "已经开始接单";
		showNotification();
	}

	public void UnMarkNotification() {
		tickerText = TITLE + ":" + "已经退出接单";
		showNotification();
	}

	/*
	 * public void StartApp(){ tickerText = TITLE + ":"+"启动";
	 * showNotification(); }
	 */

	public void ExitApp() {
		tickerText = TITLE + ":" + "退出";
		showNotification();
		cancelNotification();
	}

	public void OnUnConnecting() {
		tickerText = TITLE + ":" + "未连接服务器";
		title = "未连接服务器";
		showNotification();
	}

	public void OnConnecting() {
		tickerText = TITLE + ":" + "已连接服务器";
		title = "已连接服务器";
		showNotification();
	}

	public void showNotification() {
		notifyView.setTextViewText(R.id.notify_content, title + getState());
		notifyView.setOnClickPendingIntent(R.id.notify_icon, getPendIntent());

		notification = new Notification();
		notification.icon = ICON_ID;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.contentView = notifyView;
		notification.tickerText = tickerText;
		notification.contentIntent = getPendIntent();

		if (notificationManager != null)
			notificationManager.notify(ICON_ID, notification);
	}

	public void cancelNotification() {
		if (notificationManager != null)
			notificationManager.cancel(ICON_ID);
	}

}
