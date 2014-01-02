package com.btten.costitem;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class CostItemCheck_Scene_Jms extends NomalJsonSceneBase{
	public static final String TAG="CostCheckScene";
	
	public CostItemCheck_Scene_Jms(){
		super();
	}
	
	public void doJmsScene(OnSceneCallBack callBack,String name){
		
		SetCallBack(callBack);
		
		targetUrl=UrlFactory.GetUrlNew(
				"Sale",
				"getVipHuibi",
				"name",name
				);
		ThreadPoolUtils.execute(this);
		Log.i(TAG,targetUrl);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new CostitemCheck_ResultItems_Jms();
	}

}
