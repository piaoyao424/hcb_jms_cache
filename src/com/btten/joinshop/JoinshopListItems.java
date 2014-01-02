package com.btten.joinshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.collection.DriverListItem;
import com.btten.collection.DriverResult;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class JoinshopListItems extends BaseJsonItem{
	private String TAG="JoinshopInfoItems";
	
	
	private JSONArray jsonArray = null;
	public JoinshopListItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		JoinshopListItems items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
			
			// 有数据
			if (items.status == 1 && !result.isNull("DATA"))
			{	
				this.jsonArray = result.getJSONArray("DATA");
				item = new JoinshopListItem[this.jsonArray.length()];
				JoinshopListItem temp;
				for (int i=0; i<item.length; ++i){
					temp = new JoinshopListItem();
					CommonConvert convert = new CommonConvert(this.jsonArray.getJSONObject(i));
					temp.name=convert.getString("NAME");
					temp.address=convert.getString("ADDRESS");
					temp.telephone=convert.getString("TELEPHONE");
					temp.score=convert.getString("star");
					temp.id=convert.getString("ID");
					temp.photo=convert.getString("PHOTO");
					
					
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
