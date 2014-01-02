package com.btten.calltaxi.Gonggao;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;
import com.umeng.common.Log;

public class GonggaoAdScene extends NomalJsonSceneBase {
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new GonggaoAdItems();
	}

	public void doScene(OnSceneCallBack callBack) {
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("JmsInfo", "getAd");
		System.out.println(targetUrl);
		Log.d("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
}