package com.btten.collection;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.ui.view.BaseView;

public class MyCollectionActivity extends BaseActivity{
	MyCollectionTopBar topbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_collection);
		setCurrentTitle("我的收藏");
		setBackKeyListner(backListener);
		setRefreshKeyListner(refreshListener);
		topbar=new MyCollectionTopBar(this);
	}
	
	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
	OnClickListener refreshListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((BaseView)topbar.collection_container.getCurrentView().getTag()).ReFresh();
		}
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null)
			setIntent(intent);
	};
	
	OnActivityResultListener activityResultListener;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (activityResultListener != null)
			activityResultListener.onActivityResult(requestCode, resultCode, data);
	};
}
