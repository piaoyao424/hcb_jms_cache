package com.btten.costitem;

import org.json.JSONObject;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;
import com.btten.Jms.BtAPPJMS;

public class UserPayResultItems extends BaseJsonItem{
	private static String TAG="LoginResultItems";
	
	public UserPayResultItem item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		UserPayResultItems items = this;
		item = new UserPayResultItem();
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
			// 有数据
			if (items.status == 1) {
				CommonConvert convert = new CommonConvert(result);
				item.status = convert.getInt("status");
			}
		} catch (Exception e) {
			items.status=-1;
			items.info=e.toString();
			BtAPPJMS.getInstance();
			BtAPPJMS.ReportError(TAG+"error:\n"+e.toString()+"\nresult:\n"+result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
