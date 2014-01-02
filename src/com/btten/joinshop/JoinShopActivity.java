package com.btten.joinshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;

public class JoinShopActivity extends BaseActivity{
	EditText searchEdit;
	ImageButton searchSubmit;
	JoinShopTopBar topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joinshop);
		setCurrentTitle("加盟网点");
		setBackKeyListner(backListener);
		setRefreshKeyListner(refreshListener);
		topbar = new JoinShopTopBar(this);
		init();
	}

	public void init() {
		searchEdit = (EditText) findViewById(R.id.search_textview);
		searchSubmit = (ImageButton) findViewById(R.id.search_imagebutton);
		searchSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				// 得到InputMethodManager的实例
				if (imm.isActive()) {
					// 如果开启
					imm.hideSoftInputFromWindow(searchEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
				}

				String temp = searchEdit.getText().toString().trim();
				JoinView view = (JoinView) topbar.join_shop_content
						.getCurrentView().getTag();
				view.pageindex = 1;
				(new JoinshopListScene()).doScene(view, temp, view.type, 1);
			}
		});
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
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 得到InputMethodManager的实例
			if (imm.isActive()) {
				// 如果开启
				imm.hideSoftInputFromWindow(searchEdit.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
			}

			String temp = searchEdit.getText().toString().trim();
			JoinView view = (JoinView) topbar.join_shop_content
					.getCurrentView().getTag();
			view.pageindex = 1;
			(new JoinshopListScene()).doScene(view, temp, view.type, 1);
			view.loadMore.ShowLoading();
		}
	};

}
