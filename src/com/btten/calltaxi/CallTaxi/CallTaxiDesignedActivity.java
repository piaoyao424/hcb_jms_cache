package com.btten.calltaxi.CallTaxi;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;

public class CallTaxiDesignedActivity extends BaseActivity {
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

	OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String text = getIntent().getStringExtra("text");
		if (text.equals("我要找陪驾"))
			setCurrentTitle("陪驾");
		if (text.equals("我要找代驾"))
			setCurrentTitle("代驾");
		mainLayout.addView((new CallTaxiNeedDriver(this, text)).getView(),
				params);
	}
}
