package com.btten.sendGift;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class GetGiftItemsScene extends NomalJsonSceneBase {

	public GetGiftItemsScene() {
		super();
	}

	public void doScene(OnSceneCallBack callBack, String jid) {

		SetCallBack(callBack);

		targetUrl = UrlFactory.GetUrlNew("PublicNotice","getGiftItems", "jid",
				jid);
		ThreadPoolUtils.execute(this);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new GetGiftResultItems();
	}

}
