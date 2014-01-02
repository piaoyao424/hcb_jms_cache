package com.btten.about;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.ui.view.BaseView;

public class FeedBackActivity extends BaseActivity implements OnSceneCallBack {
	Button ok_btn, cancel_btn;
	EditText feedbk_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_feedback);
		setCurrentTitle("意见反馈");
		setBackKeyListner(backListener);
		init();
	}

	private void init() {

		feedbk_txt = (EditText) findViewById(R.id.feedbk_txt);

		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(okClick);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imm.isActive()) {
					// 如果开启
					imm.hideSoftInputFromWindow(feedbk_txt.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
				}
				startActivity(new Intent(FeedBackActivity.this,
						MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				finish();
			}
		});
	}

	ProgressDialog progress = null;
	FeedBackScene fbScene;
	InputMethodManager imm;
	//按下ok按钮键，向服务器发送内容
	private OnClickListener okClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// feedbk_txt
			if (imm.isActive()) {
				// 如果开启
				imm.hideSoftInputFromWindow(feedbk_txt.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
			}
			if (feedbk_txt.getText().toString().equals("")) {
				Alert("不能提交空消息！");
				return;
			}
			ShowProgress("正在提交中", "请稍候……");
			fbScene = new FeedBackScene();
			//像服务器发送内容。
			fbScene.doscene(FeedBackActivity.this, feedbk_txt.getText()
					.toString());
		}
	};

	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		HideProgress();

		ErrorAlert(status, info);
	}

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		HideProgress();
		feedbk_txt.setText("");
		Alert("提交成功");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		closeSoftInput();
		super.onBackPressed();
	}
	
	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
	public void closeSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive())
			imm.hideSoftInputFromWindow(feedbk_txt.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
