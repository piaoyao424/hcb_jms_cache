package com.btten.jmsinfo;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class BasicInfoScene_Jms extends NomalJsonSceneBase{
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		BaseInfoItem_Jms item=new BaseInfoItem_Jms();
		return item;
	}
	public void doJmsScene(OnSceneCallBack oncallBack,String userid){
		SetCallBack(oncallBack);
		targetUrl=UrlFactory.GetUrlNew("JmsBaseInfo","getJmsBaseInfo","userid",userid);
		Log.d("gwjtag",targetUrl);
		ThreadPoolUtils.execute(this);
	}
}
