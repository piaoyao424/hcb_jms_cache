package com.btten.calltaxi.Login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.ClassTools;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.account.JmsAccountManager;
import com.btten.calltaxi.Service.CallTaxiNotification;
import com.btten.calltaxi.Service.LocationClientService;
import com.btten.msgcenter.MsgCenter;
import com.btten.msgcenter.MsgConst;
import com.btten.network.NetConst;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

public class LoginActivity_Jms extends BaseActivity implements OnClickListener {
	final int[] bns_id = { R.id.btnId_loginjms_ok, R.id.btnId_loginjms_back };
	int[] editTexts_id = { R.id.etId_loginjms_jmsname,
			R.id.etId_loginjms_jmspwd };
	Button[] bns = new Button[2];
	EditText[] editTexts = new EditText[2];
	CheckBox rememberPwdBox;
	// 加盟商配置信息
	SharedPreferences jmsSetting = null;
	String nameStr = null;
	String pwdStr = null;
	boolean boxIsChecked = false;
	// 是否勾选

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_jms);
		setCurrentTitle("加盟商登录");

		init();
		initPersonalInfo();
	}

	private void init() {
		for (int i = 0; i < 2; ++i) {
			bns[i] = (Button) findViewById(bns_id[i]);
			bns[i].setOnClickListener(this);
			editTexts[i] = (EditText) findViewById(editTexts_id[i]);
		}
		
		rememberPwdBox = (CheckBox) findViewById(
				R.id.cbId_loginjms_remember_pwd);
	}

	private void initPersonalInfo() {
		jmsSetting = getSharedPreferences("jmscfg", MODE_PRIVATE);
		boxIsChecked = jmsSetting.getBoolean("rememberPWD", false);
		
		nameStr = jmsSetting.getString("nameStr", "");
		if (boxIsChecked)
		{
			pwdStr = jmsSetting.getString("pwdStr", "");
		}
		else
		{
			pwdStr = "";
		}
		
		if (boxIsChecked)
		{
			rememberPwdBox.setChecked(true);
			editTexts[0].setText(nameStr);
			editTexts[1].setText(pwdStr);
		}
		else
		{
			rememberPwdBox.setChecked(false);
			editTexts[0].setText(nameStr);
			editTexts[1].setText("");
		}
	}

	private void savePersonalInfo(){
		if (rememberPwdBox.isChecked())
		{
			jmsSetting.edit()
				   .putBoolean("rememberPWD", true)
				   .putString("nameStr", nameStr)
				   .putString("pwdStr", pwdStr)
				   .commit();
		}
		else {
			jmsSetting.edit()
				   .putBoolean("rememberPWD", false)
				   .putString("nameStr", nameStr)
				   .putString("pwdStr", "")
				   .commit();
			 }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnId_loginjms_ok:
			doLogin();
			// startActivity(new Intent(this, MainActivity.class));
			break;
		case R.id.btnId_loginjms_back:
			// TODO 跳转到注册页面
			onBackPressed();
			break;
		}
	}

	LoginScene_Jms loginScene_Jms;
	ProgressDialog progress;

	private void doLogin() {
		nameStr = editTexts[0].getText().toString().trim();
		pwdStr = editTexts[1].getText().toString().trim();

		if (nameStr.length() <= 0) {
			Toast.makeText(this, "请输入加盟商手机号", Toast.LENGTH_SHORT).show();
			return;
		} else if (!isPhone(nameStr)) {
			Toast.makeText(this, "无效的手机号", Toast.LENGTH_SHORT).show();
			return;
		} else if (pwdStr.length() <= 0) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		} else if (!isGoodPWD(pwdStr)) {
			Toast.makeText(this, "无效密码", Toast.LENGTH_SHORT).show();
			editTexts[1].setText("");
			return;
		}

		ShowProgress("登录中", "请稍候……");

//		bns[0].setOnClickListener(onLoadingListener);
		loginScene_Jms = new LoginScene_Jms();
		loginScene_Jms.doJmsScene(callBack, nameStr, pwdStr);

	}

	private boolean isPhone(String name) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	private boolean isGoodPWD(String pwd) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,18}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {
		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);

//			bns[0].setOnClickListener(onLoadingListener);

			if (status != NetConst.NetConnectError) {
				// 清空密码
				editTexts[1].setText("");
			}
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			HideProgress();

			savePersonalInfo();

			LoginResultItems_Jms items = (LoginResultItems_Jms) data;
			LoginResultItem_Jms item = items.item_Jms;
			JmsAccountManager.getInstance().SetJmsInfo(nameStr,
					item.jms_username, item.jms_userid);
			 
			MsgCenter.getInstance().PostMsg(MsgConst.LOGIN_SUCCESS, this);

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 得到InputMethodManager的实例
			if (imm.isActive()) {
				// 如果开启
				imm.hideSoftInputFromWindow(editTexts[1].getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
			}

			ClassTools.getInstance().isRequesting = false;
			// TODO 返回成功后跳转到HOME页面
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity((new Intent(LoginActivity_Jms.this,
							MainActivity.class)));
					finish();
					BaseActivity.ClearOtherActivity();
				}
			}, 200);
		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("退出惠车宝？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						CallTaxiNotification.getInstance().ExitApp();
						if (LocationClientService.getInstance().getMapManager() != null)
							LocationClientService.getInstance().getMapManager()
									.destroy();
						ClearAllActivity();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}
}
