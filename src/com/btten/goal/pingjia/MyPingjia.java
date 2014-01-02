package com.btten.goal.pingjia;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.calltaxi.wheelview.WheelShow;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;
import com.btten.ui.view.BaseView;

public class MyPingjia extends BaseView implements ListMoreListener, OnClickListener, OnSceneCallBack{
	public MyPingjia(Activity context) {
		super(context);
		init();
	}
	MyPingjiaAdapter myPingjiaAdapter;
	WheelShow startDate,endDate;
	ListView list;
	LoadFooterBar loadMore = null;
	
	MainActivity root;
	
	String begin;
	String end;
	PingjiaScene scene;
	public void init(){
		root = (MainActivity)context;
		
		startDate=(WheelShow)GetView().findViewById(R.id.mypingjia_start_time);
		endDate=(WheelShow)GetView().findViewById(R.id.mypingjia_end_time);
		startDate.initDateTimePicker(this, true);
		endDate.initDateTimePicker(this, false);
		list=(ListView) GetView().findViewById(R.id.mypingjia_list);
		
		loadMore = new LoadFooterBar(context, this);
		list.addFooterView(loadMore.GetView());
		
		myPingjiaAdapter=new MyPingjiaAdapter(context);
		
		if (requireData())
		{
			pageindex = 1;
			root.ShowRunning();
			scene = new PingjiaScene();
			scene.doScene(this, JmsAccountManager.getInstance().getJmsUserid(),begin,end,pageindex);
		}
	}
	@Override
	public int GetLayoutId() {
		return R.layout.my_pingjia;
	}
	@Override
	public void OnViewHide() {	
	}
	@Override
	public void OnViewShow() {	
	}
	public String getRealDate(String str){
		String realDate=str.replace("-","");
		return realDate;
	}
	
	
	
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
		
		begin += " 00:00:00";
		end += " 23:59:59";
		
		return true;
	}
	
	private int pageindex = 1;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.mypingjia_start_time:
		case R.id.mypingjia_end_time:
			if (requireData())
			{
				pageindex = 1;
				root.ShowRunning();
				scene=new PingjiaScene();
				scene.doScene(this, JmsAccountManager.getInstance().getJmsUserid(),begin,end,pageindex);
				
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		root.HideProgress();
		root.ErrorAlert(status, info);
	}	
	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		root.HideProgress();
		PingjiaItems items=(PingjiaItems)data;
		
		
		if (items.item.length == 0)
			root.Alert("没有数据！");
		
		if (1 == pageindex)
		{
			myPingjiaAdapter.setItems(items.item);
			if (list.getAdapter() == null)
				list.setAdapter(myPingjiaAdapter);
			else
				myPingjiaAdapter.notifyDataSetChanged();
		}
		else {
			myPingjiaAdapter.addMore(items.item);
			myPingjiaAdapter.notifyDataSetChanged();
		}
		
		if (items.item.length == Util.PAGE_LOAD_MIN_NUM){
			loadMore.ShowMore();
			++pageindex;
		}
		else
			loadMore.ShowNoMore();
		
	}
	
	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		loadMore.ShowLoading();
		scene = new PingjiaScene();
		scene.doScene(this, JmsAccountManager.getInstance().getJmsUserid(),begin,end,pageindex);
	}
	
	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		if (requireData())
		{
			pageindex = 1;
			root.ShowRunning();
			scene=new PingjiaScene();
			scene.doScene(this, JmsAccountManager.getInstance().getJmsUserid(),begin,end,pageindex);
		}
	}
}
