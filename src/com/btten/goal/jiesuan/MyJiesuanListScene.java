package com.btten.goal.jiesuan;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class MyJiesuanListScene extends NomalJsonSceneBase {
	public MyJiesuanListScene() {
		super();
	}

	public void doscene(OnSceneCallBack callBack, String month, String year) {
		SetCallBack(callBack);
		targetUrl = UrlFactory.GetUrlNew("JmsInfo", "getSaleAccountHistory",
				"jid", JmsAccountManager.getInstance().getJmsUserid(), "month",
				month, "year", year);
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new MyJiesuanListResult();
	}

}
