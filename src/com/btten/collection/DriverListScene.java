package com.btten.collection;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class DriverListScene extends NomalJsonSceneBase{
	public DriverListScene(){
		super();
	}
	
	public void doscene(OnSceneCallBack callBack, int page){
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("Favorite",
										 "getMyDriver",
										 "userid", JmsAccountManager.getInstance().getUserid(),
										 "page", page+""
										 );
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
	
	@Override
	protected BaseJsonItem CreateJsonItems() {
		return new DriverResult();
	}

}
