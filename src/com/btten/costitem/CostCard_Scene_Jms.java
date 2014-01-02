package com.btten.costitem;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class CostCard_Scene_Jms extends NomalJsonSceneBase{
	public static final String TAG="CostCheckScene";
	
	public CostCard_Scene_Jms(){
		super();
	}
	//银行卡
	public void doJmsScene(OnSceneCallBack callBack,String userphone,String result_item,String result_money){
		
		SetCallBack(callBack);
		
		targetUrl = UrlFactory.GetUrlNew("Sale", "doSale", "jid",
				JmsAccountManager.getInstance().getJmsUserid(),
				"userPhone", userphone, 
				"items", result_item,
				"money", result_money,
				"flag","0"
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
