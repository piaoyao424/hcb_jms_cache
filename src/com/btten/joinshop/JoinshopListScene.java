package com.btten.joinshop;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class JoinshopListScene extends NomalJsonSceneBase{
	
	public JoinshopListScene(){
		super();
	}
	public void doScene(OnSceneCallBack oncallBack,String search,int type,int pageingdex){
		SetCallBack(oncallBack);
		String userid = JmsAccountManager.getInstance().getUserid();
		targetUrl=UrlFactory.GetUrlNew("ChainShop","getShopsList","search", search,
				"userid",userid,"type",""+type,"pageindex",""+pageingdex);
		Log.d("mytag",targetUrl);
		ThreadPoolUtils.execute(this);
	}
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new JoinshopListItems();
	}
}
