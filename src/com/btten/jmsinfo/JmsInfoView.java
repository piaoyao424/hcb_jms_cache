package com.btten.jmsinfo;

import android.view.View;
import android.view.View.OnClickListener;

import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.ui.view.BaseView;

public class JmsInfoView extends BaseView{
	public JmsInfoTopBar topBar;
	
	MainActivity root;
	
	public JmsInfoView(MainActivity root){
		super(root);
		this.root = root;
		init();
	}
	
	private void init(){
		SetTitle("爱车管家");
		//这里做的view的生成
		topBar=new JmsInfoTopBar(root, GetView());
	}
	
	public void FlipToView(int index){
		topBar.switchTab(index);
	}
	
	@Override
	public int GetLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.jmsinfo_layout;
	}

	@Override
	public void OnViewShow() {
		// TODO Auto-generated method stub
		root.tabBar.setTab(2);
		root.setRefreshKeyListner(refreshListener);
		
	}
	
	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Backward();
		}
	};
	
	OnClickListener refreshListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ReFresh();
		}
	};

	@Override
	public void OnViewHide() {
		// TODO Auto-generated method stub
		root.setRefreshKeyListner(null);
	}
	
	@Override
	public void Backward() {
		// TODO Auto-generated method stub
		super.Backward();
		root.GoToView(0);
	}

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		((BaseView)topBar.flipper.getCurrentView().getTag()).ReFresh();
	}
}
