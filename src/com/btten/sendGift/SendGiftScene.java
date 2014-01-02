package com.btten.sendGift;

import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class SendGiftScene extends NomalJsonSceneBase {

	public SendGiftScene() {
		super();
	}

	public void doScene(OnSceneCallBack callBack,
			SendGiftMsgObject msgObject) {

		SetCallBack(callBack);

		targetUrl = UrlFactory.GetUrlNew("PublicNotice", "doGetGift", "jid",
				msgObject.jid, "gid", msgObject.gid, "gname", msgObject.gname,
				"gnumber", msgObject.gnumber, "mobile", msgObject.userName,
				"pw", msgObject.password);
		ThreadPoolUtils.execute(this);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new SendGiftResultItems();
	}

}
