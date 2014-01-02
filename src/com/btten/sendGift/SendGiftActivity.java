package com.btten.sendGift;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

public class SendGiftActivity extends BaseActivity {

	private ListView lv_sendGift_shownews = null;
	private GetGiftItemAdapter sendGiftItemsAdapter = null;
	private ImageButton refreshKeyImageButton = null;
	protected TextView titleTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendgift_activity);
		init();
	}

	private void init() {
		lv_sendGift_shownews = (ListView) findViewById(R.id.lvId_sendGift_items);
		initTitle();
		DoRequest();
	}

	private void initTitle() {
		titleTextView = (TextView) findViewById(R.id.tvId_sendGift_title);
		refreshKeyImageButton = (ImageButton) findViewById(R.id.ibId_sendGift_title_refresh);
		titleTextView.setText("惠车宝赠品列表");
		refreshKeyImageButton.setOnClickListener(listener);
	}

	OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DoRequest();
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ShowRunning();
			switch (msg.what) {
			case 0:
				// 检查手机号合法性
				if (isPhone(((SendGiftMsgObject) msg.obj).userName)) {
					new SendGiftScene().doScene(dialogCallBack,
							(SendGiftMsgObject) msg.obj);
				} else {
					HideProgress();
					new AlertDialog.Builder(SendGiftActivity.this)
							.setTitle("提示")
							.setMessage("请输入正确的手机号！")
							.setPositiveButton(
									"确定",
									new android.content.DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
				}

				break;
			default:
				HideProgress();
				break;
			}
		};
	};

	private void DoRequest() {
		ShowRunning();
		new GetGiftItemsScene().doScene(callBack, JmsAccountManager
				.getInstance().getJmsUserid());
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			// 设置所有的item都是没有被选中的。
			HideProgress();
			GetGiftResultItems items = (GetGiftResultItems) data;
			sendGiftItemsAdapter = new GetGiftItemAdapter(
					SendGiftActivity.this, handler);
			sendGiftItemsAdapter.setItems(items.item);
			lv_sendGift_shownews.setAdapter(sendGiftItemsAdapter);
			sendGiftItemsAdapter.notifyDataSetChanged();
			if (items.item.length == 0) {
				Alert("没有赠品信息！");
			}
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);
		}
	};

	OnSceneCallBack dialogCallBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			new AlertDialog.Builder(SendGiftActivity.this)
					.setTitle("提示")
					.setMessage("礼品赠送已成功，请将礼品移交给会员！")
					.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			new AlertDialog.Builder(SendGiftActivity.this)
					.setTitle("提示")
					.setMessage(info)
					.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}
	};

	private boolean isPhone(String name) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}
}
