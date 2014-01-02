package com.btten.joinshop.logic;

import android.util.Log;

import com.btten.joinshop.ShopCommentItems;
import com.btten.model.BaseJsonItem;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;

public class GetShopCommentScene extends NomalJsonSceneBase {

   String TAG="GetShopCommentScene";
   public GetShopCommentScene(){
	   super();
   }
    public void doScene(String id,int pageindex,OnSceneCallBack callBack){
    	SetCallBack(callBack);
		targetUrl=UrlFactory.GetUrlNew(
				"ChainShop", "getShopComment",
				"id",id,
				"pageindex",""+pageindex
				);
		ThreadPoolUtils.execute(this);
		Log.i(TAG,targetUrl);
    }
	@Override
	protected BaseJsonItem CreateJsonItems() {
		// TODO Auto-generated method stub
		return new ShopCommentItems();	}

}
