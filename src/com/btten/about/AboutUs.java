package com.btten.about;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.btten.Jms.BaseActivity;
import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.Jms.WelcomActivity;
import com.btten.account.JmsAccountManager;
import com.btten.calltaxi.Service.CallTaxiNotification;
import com.btten.ui.view.BaseView;

public class AboutUs extends BaseView implements OnClickListener {

	RelativeLayout contactUs, guide, fadeBack, welcome, clearCache,
			changePasswd;
	Button logout = null;
	TextView version_tv = null;

	MainActivity root = null;

	public AboutUs(Activity context) {
		super(context);
		root = (MainActivity) context;
		init();
	}

	public void init() {
		SetTitle("关于我们");
		// 版本号
		version_tv = (TextView) GetView().findViewById(
				R.id.versioncode_textview);
		version_tv.setText("version " + BtAPPJMS.getInstance().GetVersion());
		// 联系我们
		contactUs = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_contact);
		contactUs.setOnClickListener(this);
		// 叫车指南
		guide = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_callguide);
		guide.setOnClickListener(this);
		// 意见反馈
		fadeBack = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_feedback);
		fadeBack.setOnClickListener(this);
		// 欢迎界面
		welcome = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_welcome);
		welcome.setOnClickListener(this);
		// 清空缓存
		clearCache = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_clearcache);
		clearCache.setOnClickListener(this);
		// 修改密码
		changePasswd = (RelativeLayout) GetView().findViewById(
				R.id.aboutus_relative_changepasswd);
		changePasswd.setOnClickListener(this);
		// 注销登陆
		logout = (Button) GetView().findViewById(R.id.aboutus_logout_button);
		logout.setOnClickListener(this);
	}

	@Override
	public int GetLayoutId() {
		return R.layout.about_us;
	}

	@Override
	public void OnViewHide() {
	}

	@Override
	public void OnViewShow() {
		// 显示关于我们
		root.tabBar.setTab(1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aboutus_relative_contact:
			root.startActivity(new Intent(root, ContactUsActivity.class));
			break;

		case R.id.aboutus_relative_callguide:
			root.startActivity(new Intent(root, GuideActivity.class));
			break;

		case R.id.aboutus_relative_clearcache:
			new AlertDialog.Builder(root)
					.setTitle("提示")
					.setMessage("清除缓存？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									MainActivity.ClearOtherActivity();
									clearTemp();
									root.Alert("缓存清除成功！");
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();
			break;
		// 欢迎画面
		case R.id.aboutus_relative_welcome:
			root.startActivity(new Intent(root, WelcomActivity.class));
			break;
		// 意见反馈
		case R.id.aboutus_relative_feedback:
			root.startActivity(new Intent(root, FeedBackActivity.class));
			break;
		// 修改密码
		case R.id.aboutus_relative_changepasswd:
			root.startActivity(new Intent(root, ChangePasswdActivity.class));
			break;
		// 注销登陆
		case R.id.aboutus_logout_button:
			new AlertDialog.Builder(root)
					.setTitle("提示")
					.setMessage("注销到登录界面？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									/*
									 * root.mManager.SetListener(null); if
									 * (root.mManager.GetIsDriver())
									 * root.mManager.UnMarkDriver();
									 * root.mManager.Logout();
									 * 
									 * if(LocationClientService.getInstance().
									 * isStarted())
									 * LocationClientService.getInstance
									 * ().stop();
									 * root.stopService(root.serviceIntent);
									 */

									JmsAccountManager.getInstance().Logout();

									CallTaxiNotification.getInstance()
											.LogoutApp();
									root.startActivity(new Intent(root,
											MainActivity.class));
									// root.finish();
									BaseActivity.ClearOtherActivity();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
			break;

		}
	}

	@Override
	public void Backward() {
		// TODO Auto-generated method stub
		super.Backward();
		root.GoToView(0);
	}

	/**
	 * 清空/sdcard/calltaxi/Temp
	 */
	private void clearTemp() {
		String tempPath;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			tempPath = Environment.getExternalStorageDirectory().getPath()
					.toString()
					+ root.getResources().getString(R.string.TEMP_PATH);
			clearFiles(new File(tempPath));
		}
	}

	private void clearFiles(File file) {
		// TODO Auto-generated method stub
		if (file.isDirectory()) {
			File temp = null;
			File[] files = file.listFiles();
			int lenth = files.length;
			for (int i = 0; i < lenth; i++) {
				temp = files[i];
				clearFiles(temp);
			}
		} else if (file.isFile())
			file.delete();
	}
}
