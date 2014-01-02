package com.btten.joinshop.logic;

import android.util.Log;

import com.btten.jmsinfo.BaseInfoItem;
import com.btten.joinshop.JoinshopListItem;
import com.btten.joinshop.JoinshopListItems;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class GetJoinshopInfoScene extends NomalJsonSceneBase{
	
	public GetJoinshopInfoScene(){
		super();
	}
	public void doScene(OnSceneCallBack oncallBack,String shopid){
		SetCallBack(oncallBack);
		targetUrl=UrlFactory.GetUrlNew("ChainShop","getShopInfo",
				"id",shopid);
		Log.d("shopInfo_TAG",targetUrl);
		ThreadPoolUtils.execute(this);
	}
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
			
		return new JoinshopInfoItems();
	}
}
