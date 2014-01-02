package com.btten.goal.pingjia;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;

public class PingjiaItems extends BaseJsonItem {
	private JSONArray jsonArray = null;
	PingjiaItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			if (this.status == 1 && !result.isNull("DATA")) {
				jsonArray = result.getJSONArray("DATA");
				int length = jsonArray.length();
				item = new PingjiaItem[length];
				for (int i = 0; i < length; i++)
					{
						JSONObject obj = jsonArray.getJSONObject(i);
						CommonConvert convert = new CommonConvert(obj);
						PingjiaItem temp = new PingjiaItem();
						/*temp.id = convert.getString("ID");
						temp.score = convert.getInt("SCORE");
						temp.cash = convert.getInt("CASH");
						temp.name = convert.getString("NAME");
						temp.time = convert.getString("TIME");*/
						temp.content=convert.getString("CONTENT");
//						temp.photo=convert.getString("PHOTO");
						temp.score=convert.getInt("SCORE");
						temp.name = convert.getString("NAME");
						temp.time = convert.getString("TIME");
						item[i] = temp;
					}
			}
		} catch (Exception ex) {
			this.status = -1;
			this.info = ex.toString();
			Log.d("gwjtag", info);
			return false;
		}
		return true;
	}

}
