package com.btten.calltaxi.CallTaxi.copy;

import com.btten.Jms.R;
import com.btten.calltaxi.CallTaxi.tools.CallTaxiNetScene;
import com.btten.calltaxi.CallTaxi.tools.WheelDateShow;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class CallTaxiNeedDriver {
	View view;
	String text; // 标识当前页面内容:找代驾 || 找陪驾
	LayoutInflater inflater;
	Activity context;

	TextView startEdit, endEdit;
	WheelDateShow timeEdit;
	Button submitButton;

	String action = null;

	String time, start, end;

	CallTaxiDesignedActivity root;

	public CallTaxiNeedDriver(CallTaxiDesignedActivity context, String text) {
		this.context = context;
		root = context;
		inflater = LayoutInflater.from(context);
		this.text = text;
		if (text.equals("我要找代驾"))
			action = "designate";
		else if (text.equals("我要找陪驾"))
			action = "paternity";

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.calltaxi_needdriver, null);
		view.setClickable(true);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus){
					closeSoft();
				}
			}
		});

		timeEdit = (WheelDateShow) view.findViewById(R.id.reservetaxi_time_editview);
		startEdit = (TextView) view
				.findViewById(R.id.reservetaxi_start_editview);
		endEdit = (TextView) view.findViewById(R.id.reservetaxi_end_editview);
		submitButton = (Button) view
				.findViewById(R.id.reservetaxi_submit_button);
		submitButton.setOnClickListener(submitListener);
	}

	public View getView() {
		return this.view;
	}

	private boolean chechInfo() {
		boolean check = true;

		time = timeEdit.getText().toString().trim();
		start = startEdit.getText().toString().trim();
		end = endEdit.getText().toString().trim();

		if (time.equals("")) {
			check = false;
			root.Alert("请填写时间！");
		} else if (start.equals("")) {
			check = false;
			root.Alert("请填写出发地！");
		} else if (end.equals("")) {
			check = false;
			root.Alert("请填写目的地！");
		}

		return check;
	}

	OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			closeSoft();
			if (chechInfo()) {
				root.ShowProgress("信息发布中", "请稍候……");
				CallTaxiNetScene scene = new CallTaxiNetScene();
				scene.doScene(callBack, action, time, start, end);
			}
		}
	};
	
	private void closeSoft(){
		InputMethodManager imm = (InputMethodManager) root.getSystemService(root.INPUT_METHOD_SERVICE);
		if (imm.isActive())
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
				root.HideProgress();
				root.Alert("信息发布成功！");
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
				root.HideProgress();
				root.ErrorAlert(status, info);
		}
	};

}
