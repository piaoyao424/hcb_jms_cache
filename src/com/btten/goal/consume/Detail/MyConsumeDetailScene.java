package com.btten.goal.consume.Detail;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class MyConsumeDetailScene extends NomalJsonSceneBase{
	public MyConsumeDetailScene(){
		super();
	}
	
	public void doscene(OnSceneCallBack callBack, String bianhao){
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("JmsInfo",
										 "getSaleDetailHistory",
										 "id", bianhao,
										 "jid", JmsAccountManager.getInstance().getJmsUserid());
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new MyConsumeDetailItems();
	}

}
