package com.btten.joinshop;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class addtofavScene extends NomalJsonSceneBase{
	private static String TAG = "addtofavScene";
	public addtofavScene(){
		super();
	}
	public void doscene(OnSceneCallBack callBack, String action, String favid, int type) {
		SetCallBack(callBack);
		targetUrl=UrlFactory.GetUrlNew(
				"Favorite", action,
				"userid", JmsAccountManager.getInstance().getUserid(),
				action.equals("addFav")?"favid":"fid", favid,
				"type", type+""
				);
		ThreadPoolUtils.execute(this);
		Log.i(TAG,targetUrl);
	}
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new addtofavItems();
	}

}
