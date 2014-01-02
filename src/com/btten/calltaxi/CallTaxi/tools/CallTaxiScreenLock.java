package com.btten.calltaxi.CallTaxi.tools;

import com.btten.Jms.ClassTools;
import com.btten.Jms.R;
import com.btten.calltaxi.Service.CallTaxiJumpWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CallTaxiScreenLock {
	Context mContext;
	PopupWindow popupWindow;
	LayoutInflater inflater;

	View root;

	public CallTaxiScreenLock(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		root = inflater.inflate(R.layout.calltaxiscreenlock, null);

		popupWindow = new PopupWindow(root, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, false);

		init();
	}

	TextView failInfo;

	private void init() {
		// TODO Auto-generated method stub
		initNumShow();
		initState();
		failInfo = (TextView) root.findViewById(R.id.screen_lock_state_fail);
	}

	Thread count;
	private final int ONCHANGE = 10001;
	private final int ONEND = 10000;

	boolean isCounting = false;
	public void waitingForRequest() {
		// TODO Auto-generated method stub
		stateRoot.setVisibility(View.GONE);
		numRoot.setVisibility(View.VISIBLE);

		if (count == null) {
			count = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						isCounting = true;
						for (int i = 100; i >= 0 && isCounting; Thread.sleep(1000), --i) {
							Message msg = Message.obtain();
							msg.what = ONCHANGE;
							msg.arg1 = i;
							uiHandler.sendMessage(msg);
						}
						
						if (isCounting)
							uiHandler.sendEmptyMessage(ONEND);
						
						if (count != null)
							count.interrupt();
						count = null;

					} catch (InterruptedException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
			if (count != null && !count.isAlive())
				count.start();
		}
	}

	Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == ONCHANGE) {
				stopWatch.setText(msg.arg1 + "");
			} else if (msg.what == ONEND) {
				/*
				 * 调用cancel
				 */
				
				onFail(new Intent().putExtra("STATUS", msg.what));
			}
		}
	};

	LinearLayout stateRoot;
	TextView stateTitle;
	Button stateBack;

	private void initState() {
		// TODO Auto-generated method stub
		stateRoot = (LinearLayout) root.findViewById(R.id.screen_lock_state);
		stateTitle = (TextView) root.findViewById(R.id.screen_lock_state_title);
		stateBack = (Button) root.findViewById(R.id.screen_lock_state_back);

		stateBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) mContext).finish();
			}
		});

		initOnSuccess();
		initOnFail();
	}

	public void onFail(Intent info) {
		// TODO Auto-generated method stub
		if (count != null && !count.isInterrupted()) {
			isCounting = false;
			count = null;
		}
			
		int status = info.getIntExtra("STATUS", -1);
		
		Intent intent = new Intent(mContext.getApplicationContext(), CallTaxiJumpWindow.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_NO_ANIMATION
				);
		intent.putExtra("ACTION", "reShow");
		mContext.getApplicationContext().startActivity(intent);

		ClassTools.getInstance().isRequesting = false;

		numRoot.setVisibility(View.GONE);
		stateRoot.setVisibility(View.VISIBLE);
		stateSuccess.setVisibility(View.GONE);
		stateFail.setVisibility(View.VISIBLE);
		
		int type = info.getIntExtra("ORDERTYPE", 4);
		if (type >= 0 && type < 4)
			stateTitle.setText("预约失败");
		else if (type >= 4 && type <= 8)
			stateTitle.setText("呼叫失败");
		else
			stateTitle.setText("失败");
		
		switch (status) {
		case -1:
			failInfo.setText("附近范围内没有司机！");
			break;
		case ONEND:
			failInfo.setText("请求已超时！");
			break;

		default:
			failInfo.setText("没有司机应答您的请求！");
			break;
		}
	}

	public void onSuccess(Intent info) {
		if (count != null && !count.isInterrupted()) {
			isCounting = false;
			count = null;
		}

		numRoot.setVisibility(View.GONE);
		stateRoot.setVisibility(View.VISIBLE);
		stateSuccess.setVisibility(View.VISIBLE);
		stateFail.setVisibility(View.GONE);
		
		int type = info.getIntExtra("ORDERTYPE", 4);
		if (type >= 0 && type < 4)
			stateTitle.setText("预约成功");
		else if (type >= 4 && type <= 8)
			stateTitle.setText("呼叫成功");
		else
			stateTitle.setText("成功");

		stateCarnum.setText(info.getStringExtra("CARNUM"));
		stateName.setText(info.getStringExtra("DRIVERNAME"));
		statePhone.setText(info.getStringExtra("DRIVERPHONE"));
	}

	TextView stateFail;

	private void initOnFail() {
		// TODO Auto-generated method stub
		stateFail = (TextView) root.findViewById(R.id.screen_lock_state_fail);
	}

	LinearLayout stateSuccess, statePhoneLayout;
	TextView stateCarnum, stateName, statePhone;

	private void initOnSuccess() {
		// TODO Auto-generated method stub
		stateSuccess = (LinearLayout) root
				.findViewById(R.id.screen_lock_state_success);
		statePhoneLayout = (LinearLayout) root
				.findViewById(R.id.screen_lock_state_phone_layout);

		statePhoneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext)
						.setTitle("提示ʾ")
						.setMessage(
								"拨打电话给 "
										+ statePhone.getText().toString()
												.trim() + " ？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mContext.startActivity(new Intent(
												Intent.ACTION_CALL,
												Uri.parse("tel:"
														+ statePhone.getText()
																.toString()
																.trim())));
										dialog.dismiss();
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
			}
		});

		stateCarnum = (TextView) root
				.findViewById(R.id.screen_lock_state_carnum);
		stateName = (TextView) root.findViewById(R.id.screen_lock_state_name);
		statePhone = (TextView) root.findViewById(R.id.screen_lock_state_phone);
	}

	LinearLayout numRoot;
	TextView stopWatch;

	private void initNumShow() {
		// TODO Auto-generated method stub
		numRoot = (LinearLayout) root.findViewById(R.id.screen_lock_num_show);
		stopWatch = (TextView) root.findViewById(R.id.screen_lock_num);
	}

	public void show(View p) {
		if (popupWindow != null && !popupWindow.isShowing()) {

			popupWindow.showAtLocation(p, Gravity.TOP, 0, 0);
		}
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing())
			popupWindow.dismiss();
	}

	public boolean isShowing() {
		return popupWindow.isShowing();
	}
}
