package com.btten.calltaxi.CallTaxi.tools;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class CallTaxiNetScene extends NomalJsonSceneBase {
	public static final String TAG = "CallTaxiNetScene";

	public CallTaxiNetScene() {
		super();
	}

	public void doScene(OnSceneCallBack callBack, String action, String time, String start, String end) {
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew(
				"CallTaxi",
				 action,
				"userid", JmsAccountManager.getInstance().getUserid(),
				"phone", JmsAccountManager.getInstance().getUserPhone(),
				"needtime", time,
				"start", start,
				"dest", end);
		ThreadPoolUtils.execute(this);
		Log.i(TAG, targetUrl);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new CallTaxiPublishDesignateItem();
	}

}
