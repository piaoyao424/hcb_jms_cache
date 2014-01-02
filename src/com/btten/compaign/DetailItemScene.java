package com.btten.compaign;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class DetailItemScene extends NomalJsonSceneBase{
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new DetailItem();
	}
	public void doScene(OnSceneCallBack oncallBack,String id){
		SetCallBack(oncallBack);
		targetUrl=UrlFactory.GetUrlNew
				("DiscountActivity","getActivityDetail","acnum",id);
		Log.d("mytag",targetUrl);
		ThreadPoolUtils.execute(this);
	}
}
