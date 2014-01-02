package com.btten.collection;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class JoinListScene extends NomalJsonSceneBase{
	public JoinListScene(){
		super();
	}
	
	public void doscene(OnSceneCallBack callBack, int page){
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("Favorite",
										 "getMySeller",
										 "userid", JmsAccountManager.getInstance().getUserid(),
										 "page", page+""
										 );
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
	
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new JoinResult();
	}

}
