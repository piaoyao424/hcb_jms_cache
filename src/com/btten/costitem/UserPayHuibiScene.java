package com.btten.costitem;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class UserPayHuibiScene extends NomalJsonSceneBase {
	public static final String TAG = "LoginScene";

	public UserPayHuibiScene() {
		super();
	}
	//惠币
	public void doScene(OnSceneCallBack callBack, String userphone,
			String huibipwd, String result_item, String result_money) {

		SetCallBack(callBack);

		targetUrl = UrlFactory.GetUrlNew("Sale", "doSale", "jid",
				JmsAccountManager.getInstance().getJmsUserid(),
				// "jid", "0661D7DD-423E-4A22-97A5-17CC80F451D6",
				"userPhone", userphone, "password", huibipwd, "items", result_item,
				"money", result_money,
				"flag","1"
				);
		ThreadPoolUtils.execute(this);
		Log.i(TAG, targetUrl);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new UserPayResultItems();
	}

}
