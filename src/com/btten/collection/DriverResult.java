package com.btten.collection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class DriverResult extends BaseJsonItem {
	private static String TAG = "DriverResult";
	private JSONArray jsonArray = null;
	public DriverListItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		DriverResult items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
			
			// 有数据
			if (items.status == 1 && !result.isNull("DATA"))
			{	
				this.jsonArray = result.getJSONArray("DATA");
				item = new DriverListItem[this.jsonArray.length()];
				DriverListItem temp;
				for (int i=0; i<item.length; ++i){
					temp = new DriverListItem();
					CommonConvert convert = new CommonConvert(this.jsonArray.getJSONObject(i));
					
					temp.name = convert.getString("NAME");
					temp.carnum = convert.getString("CARNUM");
					temp.telephone = convert.getString("TELEPHONE");
					temp.address = convert.getString("ADDRESS");
					temp.photo = convert.getString("PHOTO");
					
					
					item[i] = temp;
				}
				
			}
		} catch (Exception e) {
			items.status = -1;
			items.info = e.toString();
			BtAPPJMS.getInstance().ReportError(
					TAG + "error:\n" + e.toString() + "\nresult:\n"
							+ result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
