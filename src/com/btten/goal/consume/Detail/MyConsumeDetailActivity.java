package com.btten.goal.consume.Detail;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.goal.consume.MyConsumeListScene;
import com.btten.goal.consume.MyConsumeView;
import com.btten.goal.consume.MyConsumeListItem;
import com.btten.jmsinfo.JmsInfoView;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;

import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MyConsumeDetailActivity extends BaseActivity {
//	MyConsumeListItem item = null;
//	int[] main_tv_id = { R.id.myconsume_list_shop_name,
//			R.id.myconsume_list_goods_num, R.id.myconsume_list_goods_totalcost,
//			R.id.myconsume_list_time };
//	int[] main_tv_id = { R.id.myconsume_list_shop_name,
//			R.id.myconsume_list_goods_num, R.id.myconsume_list_goods_totalcost,
//			R.id.myconsume_list_time };
//	TextView[] main_tv = new TextView[main_tv_id.length];
	private TextView tv_goods_num , tv_goods_totalcost;
	
	ListView lv = null;
	MyConsumeDetailAdapter adapter = null;
	MyConsumeDetailScene myScene;
	private String bianhao_Str = null;
//	private String counts_Str = null;
	private String total_Str = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_consume_detail);
		setCurrentTitle("消费详情");
		setBackKeyListner(backListener);
		setRefreshKeyListner(refreshListener);
		init();
	}
	
	private void init(){
		
		tv_goods_num = (TextView) findViewById(R.id.myconsume_detail_list_goods_num);
		tv_goods_totalcost = (TextView) findViewById(R.id.myconsume_detail_list_goods_totalcost);
		getBundle();
		lv = (ListView) findViewById(R.id.llId_consume_detail_list);
		adapter = new MyConsumeDetailAdapter(this);
		
		doRequest();
		setRefreshKeyListner(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doRequest();
			}
		});

	}

//	private void setJiashuju(){
//		MyConsumeDetailItem[] items = new MyConsumeDetailItem[3];
//		{
//			MyConsumeDetailItem item = new MyConsumeDetailItem();
//			item.goodsName = "娃哈哈";
//			item.goodsPrice = "3.5";
//			items[0] = item;
//		}
//		{
//			MyConsumeDetailItem item = new MyConsumeDetailItem();
//			item.goodsName = "可口可乐";
//			item.goodsPrice = "2.5";
//			items[1] = item;
//		}
//		{
//			MyConsumeDetailItem item = new MyConsumeDetailItem();
//			item.goodsName = "趣多多";
//			item.goodsPrice = "4.5";
//			items[2] = item;
//		}
//		adapter.setItems(items);
//		lv.setAdapter(adapter);
//	}
	
	private void doRequest() {
		ShowRunning();
		myScene = new MyConsumeDetailScene();
		myScene.doscene(callBack, bianhao_Str);
		
		
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			
			MyConsumeDetailItems items = (MyConsumeDetailItems) data;
			int itemsLenth = 0;
			itemsLenth = items.item.length;
			tv_goods_num.setText(String.valueOf(itemsLenth));
			// 设置所有的item都是没有被选中的。
			adapter.setItems(items.item);
			lv.setAdapter(adapter);
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
//			mLoadMore.ShowNoMore();
			HideProgress();
			ErrorAlert(status, info);
		}
	};

	
	OnClickListener refreshListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ShowProgress("读取数据中", "请稍候……");
//			(new MyConsumeDetailScene()).doscene(MyConsumeDetailActivity.this, item.idStr, item.jidStr);
			
		}
	};
	
	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
	private void getBundle()
	{
		Bundle bundle = this.getIntent().getExtras();
		bianhao_Str = bundle.getString("KEY_BIANHAO");
//		counts_Str = bundle.getString("KEY_COUNTS");
		total_Str = bundle.getString("KEY_TOTAL");
		System.out.println("收到的 bianhao_str = " + bianhao_Str);
//		System.out.println("收到的 counts_Str = " + counts_Str);
		System.out.println("收到的 total_Str = " + total_Str);
//		tv_goods_num.setText(counts_Str);
		tv_goods_totalcost.setText(total_Str);
		
	}
}
