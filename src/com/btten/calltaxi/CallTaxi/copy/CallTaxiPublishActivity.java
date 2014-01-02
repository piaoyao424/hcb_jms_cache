package com.btten.calltaxi.CallTaxi.copy;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;

public class CallTaxiPublishActivity extends BaseActivity {
	LinearLayout mainLayout;
	LinearLayout.LayoutParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calltaxigeneral);
		setBackKeyListner(backListener);
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mainLayout = (LinearLayout) findViewById(R.id.calltaxi_general);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String text = getIntent().getStringExtra("text");
		showContent(text);
	}
	
	
	private void showContent(String info) {
		// TODO Auto-generated method stub
		String[] infos = {"预约出租车", "预约货车", "预约代驾", "预约陪驾", "我要叫出租车", "我要叫货车", "我要找代驾", "我要找陪驾", "救援"};
		String[] title = {"预约出租车", "预约货车","预约代驾","预约陪驾", "呼叫出租车","呼叫货车","呼叫代驾","呼叫陪驾","呼叫救援"};
		for (int i = 0, lenth = infos.length; i <lenth; ++i){
			if (infos[i].equals(info)){
				setCurrentTitle(title[i]);
				break;
			}
		}
		mainLayout.addView((new CallTaxiNeedCar(this, info)).getView(), params);
	}

	OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
}
