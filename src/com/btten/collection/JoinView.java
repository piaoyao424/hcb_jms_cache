package com.btten.collection;

import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.R;
import com.btten.joinshop.JoinShopContentActivity;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;
import com.btten.ui.view.BaseView;

public class JoinView  extends BaseView implements OnSceneCallBack, ListMoreListener,OnActivityResultListener {

	MyCollectionActivity root;
	
	 public JoinView(MyCollectionActivity context){
	    	super(context);
	    	this.root = context;
	    	Init();
	    }
	 
	 JoinListAdapter adapter;
	 ListView listView;

	 LoadFooterBar loadMore;
	 JoinListScene listScene;
	 private int pageindex = 1;
	 
	 private void Init()
	 {
		//初始化列表页面
		listView = (ListView) GetView().findViewById(R.id.my_collection_list_join);
		adapter = new JoinListAdapter(context);
		 
		root.activityResultListener = this;
		
		loadMore = new LoadFooterBar(context, this);
		listView.addFooterView(loadMore.GetView());
		listView.setOnItemClickListener(listItemListener);
		
		pageindex = 1;
		loadMore.ShowLoading();
		listScene = new JoinListScene();
		listScene.doscene(this, pageindex);
	 }
	 
	 OnItemClickListener listItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(root, JoinShopContentActivity.class);
			intent.putExtra("parent", "COLLECTION");
			intent.putExtra("id", ((JoinListItem)adapter.getItem(position)).id);
			root.startActivityForResult(intent, 0);
			root.RightToLeft();
		}
	};
	 
	@Override
	public int GetLayoutId() {
		return R.layout.my_collection_join;
	}

	@Override
	public void OnViewShow() {
		
	}	
	

	@Override
	public void OnViewHide() {
		
	}

	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		loadMore.ShowNoMore();
		root.ErrorAlert(status, info);
	}

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		JoinResult items = (JoinResult) data;

		if (pageindex == 1) {
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
		listScene = new JoinListScene();
		listScene.doscene(this, pageindex);
	}

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		pageindex = 1;
		loadMore.ShowLoading();
		listScene = new JoinListScene();
		listScene.doscene(this, pageindex);
	}

	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 10001)
			if (data.getBooleanExtra("cancel", false))
				ReFresh();
		return true;
	}
}
