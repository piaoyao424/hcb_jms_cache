package com.btten.goal.consume;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.calltaxi.wheelview.WheelShow;
import com.btten.goal.consume.Detail.MyConsumeDetailActivity;
import com.btten.goal.jiesuan.MyJiesuanListAdapter;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;
import com.btten.ui.view.BaseView;

public class MyConsumeView extends BaseView implements OnClickListener,
		ListMoreListener {
	LoadFooterBar loadMore = null;
	MyConsumeListScene listScene = null;

	MyConsumeCommentPop commentPopCreator = null;
	PopupWindow commentWindow = null;

	MainActivity root;
	MyConsumeListAdapter adapter;

	WheelShow startDate, endDate;
	ListView listView;
	String saleBianhaoStr = null;

	public MyConsumeView(MainActivity context) {
		super(context);
		root = context;
		init();
	}

	public void init() {
		// 初始化时间按钮
		startDate = (WheelShow) GetView().findViewById(
				R.id.myconsume_start_time);
		endDate = (WheelShow) GetView().findViewById(R.id.myconsume_end_time);
		startDate.initDateTimePicker(this, true);
		endDate.initDateTimePicker(this, false);

		// 初始化列表页面
		listView = (ListView) GetView().findViewById(R.id.myconsume_list);

		loadMore = new LoadFooterBar(context, this);
		listView.addFooterView(loadMore.GetView());

		adapter = new MyConsumeListAdapter(context, this);

		// 初始化评价popup窗口
		commentPopCreator = new MyConsumeCommentPop(context);
		initTitle();
		doRequest();
		
	}
	
	private ImageButton refreshKeyImageButton = null;
	protected TextView titleTextView = null;
	
	private void initTitle(){
		titleTextView = (TextView)  GetView().findViewById(R.id.tvId_consume_title);
		refreshKeyImageButton = (ImageButton) GetView().findViewById(R.id.ibId_consume_title_refresh);
		titleTextView.setText("销售记录");
		refreshKeyImageButton.setOnClickListener(listener);
		
	}
	
	OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			doRequest();
		}
	};
	private void doRequest() {
		if (requireData()) {
			pageindex = 1;
			listScene = new MyConsumeListScene();
			listScene.doscene(callBack, begin, end, pageindex);
			root.ShowRunning();
		}
	}

	@Override
	public int GetLayoutId() {
		return R.layout.my_consume;
	}

	@Override
	public void OnViewHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnViewShow() {
		// TODO Auto-generated method stub

	}

	String begin;
	String end;

	private boolean requireData() {
		begin = startDate.getText().toString().trim();
		end = endDate.getText().toString().trim();

		if (begin.length() <= 0 || end.length() <= 0)
			return false;

		StringBuffer startString = new StringBuffer("");
		StringBuffer endString = new StringBuffer("");
		String[] temp1 = begin.split("-", 0);
		String[] temp2 = end.split("-", 0);
		for (int i = 0; i < 3; ++i) {
			startString.append(temp1[i]);
			endString.append(temp2[i]);
		}

		if (Integer.parseInt(startString.toString()) > Integer
				.parseInt(endString.toString())) {
			root.Alert("结束日期不能小于开始日期！");
			return false;
		}
		System.out.println("开始日期 = " + begin);
		System.out.println("结束日期 = " + end);
		// begin += " 00:00:00";
		// end += " 23:59:59";
		return true;
	}

	private int pageindex = 1;

	public MyConsumeListItem curItem = null; // set by adapter

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.myconsume_start_time:
		case R.id.myconsume_end_time:
			if (requireData()) {
				pageindex = 1;
				listScene = new MyConsumeListScene();
				listScene.doscene(callBack, begin, end, pageindex);
				root.ShowRunning();
			}
			break;
		case R.id.myconsume_comment_button:
			// 这里生成评价窗口
			commentWindow = commentPopCreator.getPopupWindow(curItem);
			commentWindow.showAtLocation(GetView(), Gravity.CENTER, 0, 0);
			break;
		case R.id.myconsume_comment_content:
			// 这里生成消费详情窗口
			System.out.println("点击了消费详情页面");
			Intent intent = new Intent(root, MyConsumeDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sale_bianhao", curItem.sale_bianhaoStr);
			System.out.println("消费 sale_bianhao = " + curItem.sale_bianhaoStr);
			// bundle.putParcelable("item", curItem);
			intent.putExtras(bundle);
			root.startActivity(intent);
			System.out.println("点击了消费详情页面, end");
			break;
		default:
			break;
		}
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {
		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			root.ErrorAlert(status, info);
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			MyConsumeListResult items = (MyConsumeListResult) data;
			if (1 == pageindex) {
				adapter.setItems(items.item);
				if (listView.getAdapter() == null)
					listView.setAdapter(adapter);
				else
					adapter.notifyDataSetChanged();
			} else {
				adapter.addMore(items.item);
				adapter.notifyDataSetChanged();
			}

			if (items.item.length == 0)
				root.Alert("没有数据！");

			if (items.item.length == Util.PAGE_LOAD_MIN_NUM) {
				loadMore.ShowMore();
				++pageindex;
			} else {
				loadMore.ShowNoMore();
			}

		}
	};

	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		loadMore.ShowLoading();
		listScene = new MyConsumeListScene();
		listScene.doscene(callBack, begin, end, pageindex);
	}

	@Override
	public void Backward() {
		// TODO Auto-generated method stub
		super.Backward();

		/*
		 * if (!backTip.getView().isShown()){ backTip.show(); return;}
		 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		root.startActivity(intent);
		// root.moveTaskToBack(false);
	}
}
