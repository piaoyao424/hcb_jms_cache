//gwj-creat-20130818
package com.btten.about;

import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class ChangePwdScene extends NomalJsonSceneBase{
	public static final String TAG="ChangePwdScene";
	
	public ChangePwdScene(){
		super();
	}
	
	public void doChangePwdScene(OnSceneCallBack callBack,String oldpwd,String newpwd){
		SetCallBack(callBack);
		targetUrl=UrlFactory.GetUrlOld(
				"DoChangeJmsPwd",
				"userid", JmsAccountManager.getInstance().getJmsUserid(),
				"oldpwd",oldpwd,
				"newpwd",newpwd
				);
		ThreadPoolUtils.execute(this);
		Log.i(TAG,targetUrl);
	}

	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new changePwdResultItems();
	}

}
