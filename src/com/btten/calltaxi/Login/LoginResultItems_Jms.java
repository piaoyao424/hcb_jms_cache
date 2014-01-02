package com.btten.calltaxi.Login;

import org.json.JSONObject;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;
import com.btten.Jms.BtAPPJMS;

public class LoginResultItems_Jms extends BaseJsonItem{
	private static String TAG="LoginResultItems";
	
	public LoginResultItem_Jms item_Jms;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		LoginResultItems_Jms items=this;
		item_Jms = new LoginResultItem_Jms();
		try {
//			items.status = result.getInt("status");
			items.status = result.getInt("status");
			items.info = result.getString("info");
			// 有数据
			if (items.status == 1) {
				CommonConvert convert=new CommonConvert(result);
				item_Jms.jms_username = convert.getString("jms_username");
				item_Jms.jms_userid = convert.getString("jms_userid");
				item_Jms.jms_phone = convert.getString("jms_phone");
				//item.userimage=convert.getString("userimage");
				item_Jms.jms_status = convert.getInt("status");
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
