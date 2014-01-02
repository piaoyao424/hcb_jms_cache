package com.btten.joinshop;

import android.app.Activity;
import android.widget.ListView;

import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.ui.view.BaseView;

public class JoinView extends BaseView implements OnSceneCallBack,
		ListMoreListener {

	final int MAXITEMS = 10;
	int type;// 1,2,3
	
	JoinShopActivity root;
	public JoinView(Activity context, int type) {
		super(context);
		root = (JoinShopActivity) context;
		this.type = type;
		Init();
	}

	JoinShopListAdapter adapter;
	ListView listView;

	LoadFooterBar loadMore;
	JoinshopListScene listScene;
	int pageindex = 1;

	private void Init() {
		// 初始化列表页面
		listView = (ListView) GetView().findViewById(
				R.id.my_collection_list_driver);
		adapter = new JoinShopListAdapter(context);

		loadMore = new LoadFooterBar(context, this);
		listView.addFooterView(loadMore.GetView());
		
		pageindex = 1;
		root.ShowRunning();
		listScene = new JoinshopListScene();
		listScene.doScene(this, "", type, pageindex);
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
		root.HideProgress();
		loadMore.ShowNoMore();
		root.ErrorAlert(status, info);
	}

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		root.HideProgress();
		JoinshopListItems items = (JoinshopListItems) data;
		
			if (1 == pageindex) {

				adapter.setItems(items.item);
				if (items.item.length == 0)
					root.Alert("没有可用数据！");
				if (listView.getAdapter() == null)
					listView.setAdapter(adapter);
				else
					adapter.notifyDataSetChanged();
			} else {
				adapter.addMore(items.item);
				adapter.notifyDataSetChanged();
			}
		
			if (type != 3){
				if (items.item.length == MAXITEMS) {
					loadMore.ShowMore();
					++pageindex;
				} else {
					loadMore.ShowNoMore();
				}
			}
			else
				loadMore.ShowNoMore();
	}

	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		loadMore.ShowLoading();
		listScene = new JoinshopListScene();
		listScene.doScene(this, "", type, pageindex++);
	}

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		
	}

}
