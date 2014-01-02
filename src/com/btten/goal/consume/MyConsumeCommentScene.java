package com.btten.goal.consume;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class MyConsumeCommentScene extends NomalJsonSceneBase {
	public MyConsumeCommentScene(){
		super();
	}
	
	public void doscene(OnSceneCallBack callBack, String id, String comment, String star){
		SetCallBack(callBack);
		System.out.println("上传的评论内容是: " + comment);
		targetUrl=UrlFactory.GetUrlNew( "UserBaseInfo",
										"commentShop",
										"userid", JmsAccountManager.getInstance().getUserid(),
										"content", comment,
										"star", star,
										"cardnum", id);
		Log.i("url", targetUrl);
		ThreadPoolUtils.execute(this);
	}
	
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new MyConsumeCommentResult();
	}

}
