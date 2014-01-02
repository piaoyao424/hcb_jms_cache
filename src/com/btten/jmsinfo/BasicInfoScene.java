package com.btten.jmsinfo;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class BasicInfoScene extends NomalJsonSceneBase{
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		BaseInfoItem item=new BaseInfoItem();
		return item;
	}
	public void doScene(OnSceneCallBack oncallBack,String userid){
		SetCallBack(oncallBack);
		targetUrl=UrlFactory.GetUrlNew("UserBaseInfo","getBaseInfo","userid",userid);
		Log.d("mytag",targetUrl);
		ThreadPoolUtils.execute(this);
	}
}
