package com.btten.collection;

import android.app.Activity;
import android.widget.ListView;

import com.btten.Jms.ListItemAdapter;
import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;
import com.btten.ui.view.BaseView;

public class DriverView  extends BaseView implements OnSceneCallBack, ListMoreListener {
	
	MyCollectionActivity root;
	
	 public DriverView(Activity context){
	    	super(context);
	    	root = (MyCollectionActivity) context;
	    	Init();
	    }
	 
 
	ListItemAdapter adapter;
	ListView listView;
	
	 LoadFooterBar loadMore;
	 DriverListScene listScene;
	 private int pageindex = 1;
	 
	 private void Init()
	 {
			//初始化列表页面
			listView = (ListView) GetView().findViewById(R.id.my_collection_list_driver);
			adapter = new ListItemAdapter(context);

			loadMore = new LoadFooterBar(context, this);
			listView.addFooterView(loadMore.GetView());
			
			pageindex = 1;
			listScene = new DriverListScene();
			listScene.doscene(this, pageindex);
			loadMore.ShowLoading();
	 }
	 
	@Override
	public int GetLayoutId() {
		return R.layout.my_collection_driver;
	}

	@Override
	public void OnViewShow() {
		
	}

	@Override
	public void OnViewHide() {
		
	}


	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		((MyCollectionActivity)context).ErrorAlert(status, info);
	}

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		DriverResult items = (DriverResult) data;

		
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
	
	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		loadMore.ShowLoading();
		listScene = new DriverListScene();
		listScene.doscene(this, pageindex);
	}

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		pageindex = 1;
		loadMore.ShowLoading();
		listScene = new DriverListScene();
		listScene.doscene(this, pageindex);
	}
}
