package com.btten.about;

import org.json.JSONObject;

import com.btten.calltaxi.Login.LoginResultItem_Jms;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;
import com.btten.Jms.BtAPPJMS;

public class changePwdResultItems extends BaseJsonItem{
	private static String TAG="changePwdResultItems";
	
	public LoginResultItem_Jms item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		changePwdResultItems items=this;
		item = new LoginResultItem_Jms();
		try {
			items.status = result.getInt("status");
			items.info = result.getString("info");
			// 返回值为真。
			if (items.status == 1) {
				CommonConvert convert=new CommonConvert(result);
				item.jms_username = convert.getString("username");
				item.jms_userid = convert.getString("userid");
				//item.userimage=convert.getString("userimage");
				item.jms_status=convert.getInt("status");
				//item.workkind=convert.getString("workkind");
				//item.mesnum=convert.getInt("mesnum");
			}
		} catch (Exception e) {
			items.status=-1;
			items.info=e.toString();
			BtAPPJMS.getInstance().ReportError(TAG+"error:\n"+e.toString()+"\nresult:\n"+result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
