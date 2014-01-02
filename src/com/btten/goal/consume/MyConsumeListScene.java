package com.btten.goal.consume;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class MyConsumeListScene extends NomalJsonSceneBase{
	public MyConsumeListScene(){
		super();
	}
	
	public void doscene(OnSceneCallBack callBack, String begin, String end, int page){
		SetCallBack(callBack);
		/*targetUrl = UrlFactory.GetUrlNew("UserBaseInfo",
										 "getExpense",
										 "userid", AccountManager.getInstance().getUserid(),
										 "begin", begin,
										 "end", end,
										 "page", page+""
										 );*/
		targetUrl = UrlFactory.GetUrlNew("JmsInfo",
				 "getSaleMasterHistory",
				 "jid", JmsAccountManager.getInstance().getJmsUserid(),
//				 "jid", "0661D7DD-423E-4A22-97A5-17CC80F451D6",
				 "begin", begin,
				 "end", end,
				 "page", page+""
				 );
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
	
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new MyConsumeListResult();
	}

}
