package com.btten.calltaxi.Gonggao;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.adapter.ViewFlowAdapterOnline;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.ui.view.BaseView;
import com.btten.uikit.BttenFlipper;

//create-gwj-20130903
public class GonggaoView_Jms extends BaseView {

	private ListView lv_gonggao_shownews = null;
	private MainActivity root = null;
	private GonggaoAdapter myGonggaoAdapter = null;
	private ViewFlowAdapterOnline myVfadapter = null;
	private BttenFlipper viewFlow = null;

	public GonggaoView_Jms(MainActivity root) {
		super(root);
		this.root = root;
		init();
	}

	private void init() {
		lv_gonggao_shownews = (ListView) GetView().findViewById(
				R.id.lvId_gonggao_shownews);
		viewFlow = (BttenFlipper) GetView().findViewById(R.id.gonggao_viewFlow);
		initTitle();
		DoRequest();
		initViewFilpper();
	}

	private ImageButton refreshKeyImageButton = null;
	protected TextView titleTextView = null;

	private void initTitle() {
		titleTextView = (TextView) GetView().findViewById(
				R.id.tvId_gonggao_title);
		refreshKeyImageButton = (ImageButton) GetView().findViewById(
				R.id.ibId_gonggao_title_refresh);
		titleTextView.setText("惠车宝公告信息");
		refreshKeyImageButton.setOnClickListener(listener);
	}

	OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			DoRequest();
		}
	};

	// 初始化图片切换功能
	private void initViewFilpper() {
		viewFlow.setBackgroundColor(android.graphics.Color.TRANSPARENT);
		myVfadapter = new ViewFlowAdapterOnline(root);
		new GonggaoAdScene().doScene(adCallBack);
	}

	OnSceneCallBack adCallBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			GonggaoAdItems items = (GonggaoAdItems) data;
			myVfadapter.setImages(items.item);
			viewFlow.setAdapter(myVfadapter);
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			root.ErrorAlert(status, info);
		}
	};

	private void DoRequest() {
		root.ShowRunning();
		new GonggaoScene().doScene(callBack);
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			// 设置所有的item都是没有被选中的。
			root.HideProgress();
			GonggaoItems items = (GonggaoItems) data;
			myGonggaoAdapter = new GonggaoAdapter(root);

			myGonggaoAdapter.setItems(items.item);
			lv_gonggao_shownews.setAdapter(myGonggaoAdapter);
			myGonggaoAdapter.notifyDataSetChanged();
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			root.ErrorAlert(status, info);
		}
	};

	@Override
	public int GetLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.gonggaoview_jms;
	}

	@Override
	public void OnViewShow() {
		// TODO Auto-generated method stub
		root.tabBar.setTab(0);
		// GetView().refreshDrawableState();
	}

	@Override
	public void OnViewHide() {
		// TODO Auto-generated method stub
	}

	// Toast backTip;
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