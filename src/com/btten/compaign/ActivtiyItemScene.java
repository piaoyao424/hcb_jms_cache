package com.btten.compaign;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class ActivtiyItemScene extends NomalJsonSceneBase{
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new ActivityItems();
	}
	public void doScene(OnSceneCallBack oncallBack,String page){
		SetCallBack(oncallBack);
		targetUrl=UrlFactory.GetUrlNew
				("DiscountActivity","getActivityList","page",page);
		Log.d("mytag",targetUrl);
		ThreadPoolUtils.execute(this);
	}
}
