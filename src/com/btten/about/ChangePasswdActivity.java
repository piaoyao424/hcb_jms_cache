//gwj-creat-20130818
package com.btten.about;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.ClassTools;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.calltaxi.Login.LoginActivity_Jms;
import com.btten.calltaxi.Service.CallTaxiNotification;
import com.btten.calltaxi.Service.LocationClientService;
import com.btten.calltaxi.Service.core.JmsMapManager;
import com.btten.msgcenter.MsgCenter;
import com.btten.msgcenter.MsgConst;
import com.btten.network.NetConst;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChangePasswdActivity extends BaseActivity {
	EditText et_oldpwd, et_newpwd, et_pwdAgain;
	Button btn_tijiao, btn_quxiao;
	String strOldPwd = null, strNewPwd = null, strPwdAgain = null;
	//开始启动服务
	public Intent serviceIntent;
	//司机管理
	public JmsMapManager mManager;
	boolean accept_terms;
	// 与服务器通信用
	ChangePwdScene chgScene;
	// 弹出信息
	ProgressDialog progress;
	// 保存本地的信息
	SharedPreferences setting = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepasswd_activity);

		setCurrentTitle("修改密码");
		setBackKeyListner(backListener);
		init();
	}

	private void init() {
		et_oldpwd = (EditText) findViewById(R.id.changepasswd_old_edittext);
		et_newpwd = (EditText) findViewById(R.id.changepasswd_new_edittext);
		et_pwdAgain = (EditText) findViewById(R.id.changepasswd_pwd_again_edittext);

		btn_tijiao = (Button) findViewById(R.id.changepasswd_tijiao_button);
		btn_tijiao.setOnClickListener(listener);
		btn_quxiao = (Button) findViewById(R.id.changepasswd_quxiao_button);
		btn_quxiao.setOnClickListener(listener);
		setting = getSharedPreferences("calltaxicfg", MODE_PRIVATE);
		
		startService(serviceIntent);
		
		mManager = JmsMapManager.getInstance();
		mManager.Login(JmsAccountManager.getInstance().getUserid());
	}

	OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.changepasswd_tijiao_button:

				doTijiao();
				break;
			case R.id.changepasswd_quxiao_button:
				// TODO 跳转到注册页面
				onBackPressed();
				break;
			}
		}

	};

	private boolean judge() {
		strOldPwd = et_oldpwd.getText().toString().trim();
		strNewPwd = et_newpwd.getText().toString().trim();
		strPwdAgain = et_pwdAgain.getText().toString().trim();

		if (strOldPwd.equals("")) {
			Alert("请输入原密码！");
			return false;
		}

		if (strNewPwd.equals("")) {
			Alert("新密码不能为空！");
			return false;
		}

		if (!isGoodPWD(strNewPwd)) {
			Alert("新密码长度必须为6~11位,且不能为非法字符！");
			return false;
		}
		if (strPwdAgain.equals("")) {
			Alert("请重复输入新密码！");
			return false;
		}
		if (!strPwdAgain.equals(strNewPwd)) {
			Alert("两次输入的密码不一致！");
			return false;
		}
		return true;
	}

	private boolean isGoodPWD(String pwd) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,18}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	private void doTijiao() {

		if (judge()) {
			ShowProgress("提交数据中", "请稍候……");
			System.out.println("userid + OldPwd + NewPwd : "
					+ JmsAccountManager.getInstance().getUserid() + " + "
					+ strOldPwd + " + " + strNewPwd);
			chgScene = new ChangePwdScene();
			chgScene.doChangePwdScene(saveBankCallBack, strOldPwd, strNewPwd);
		}
	}
	
	/**
	 * 菜单项
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem exit = menu.add(0, 0, 0, "退出");
		exit.setIcon(R.drawable.home_tab_exit_icon);
		return super.onCreateOptionsMenu(menu);
	}
	
	// 根据服务器返回值，来显示在页面中的 信息。
	OnSceneCallBack saveBankCallBack = new OnSceneCallBack() {
		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ShowProgress("密码修改成功", "请重新登录!");
			//清空密码
			setting.edit()
			   .putString("pwdStr", "")
			   .commit();
			// TODO 返回成功后跳转到登录页面
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
					// 清除缓存，
					ClearOtherActivity();
					clearTemp();
					// 注销到登录页面
					if (LocationClientService.getInstance().isStarted()) {
						LocationClientService.getInstance().stop();
					}
					JmsAccountManager.getInstance().Logout();
					//释放司机标记
					mManager.SetListener(null);
					if (mManager.GetIsDriver())
						mManager.UnMarkDriver();
					mManager.Logout();
					//关闭位置
					if (LocationClientService.getInstance()
							.isStarted())
						LocationClientService.getInstance()
								.stop();
					if (LocationClientService.getInstance()
							.getMapManager() != null)
						LocationClientService.getInstance()
								.getMapManager().destroy();
					//停止服务
					stopService(serviceIntent);
					
					CallTaxiNotification.getInstance().LogoutApp();
					startActivity((new Intent(ChangePasswdActivity.this,
							LoginActivity_Jms.class)));
					BaseActivity.ClearOtherActivity();
				}
			}, 200);

		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);

			if (status != NetConst.NetConnectError) {
				// 清空密码
				et_oldpwd.setText("");
				et_newpwd.setText("");
				et_pwdAgain.setText("");
			}
		}

	};

	/**
	 * 清空/sdcard/calltaxi/Temp
	 */
	private void clearTemp() {
		String tempPath;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			tempPath = Environment.getExternalStorageDirectory().getPath()
					.toString()
					+ this.getResources().getString(R.string.TEMP_PATH);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getGroupId() == 0 && item.getItemId() == 0)
			new AlertDialog.Builder(this)
					.setMessage("退出惠车宝？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									mManager.SetListener(null);
									if (mManager.GetIsDriver())
										mManager.UnMarkDriver();
									mManager.Logout();

									if (LocationClientService.getInstance()
											.isStarted())
										LocationClientService.getInstance()
												.stop();
									if (LocationClientService.getInstance()
											.getMapManager() != null)
										LocationClientService.getInstance()
												.getMapManager().destroy();
									stopService(serviceIntent);

									CallTaxiNotification.getInstance()
											.ExitApp();
									ClearAllActivity();
									System.exit(0);
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
		return super.onOptionsItemSelected(item);
	}
}
