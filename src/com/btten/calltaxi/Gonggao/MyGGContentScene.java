package com.btten.calltaxi.Gonggao;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class MyGGContentScene extends NomalJsonSceneBase {
	public MyGGContentScene() {
		super();
	}

	public void doscene(OnSceneCallBack callBack, String ggid) {
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("PublicNotice",
										 "getPublicNoticeDetail",
										 "id",ggid
										 );
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new GGContentItems();
	}

}
