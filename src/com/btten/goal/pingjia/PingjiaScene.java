package com.btten.goal.pingjia;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;
import com.umeng.common.Log;

public class PingjiaScene extends NomalJsonSceneBase {
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new PingjiaItems();
	}

	public void doScene(OnSceneCallBack oncallback, String userid,
			String begin, String end, int page)
	{
		SetCallBack(oncallback);
		targetUrl = UrlFactory.GetUrlNew("JmsInfo","getCommentaryHistory",
				"userid", userid,
				"begin", begin,
				"end", end,
				"page", page+""
				);
		System.out.println(targetUrl);
		Log.d("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
}
