package com.btten.calltaxi.Gonggao;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;

public class GonggaoItems extends BaseJsonItem {
	private JSONArray jsonArray = null;
	GonggaoItem[] item = null;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			if (this.status == 1 && !result.isNull("DATA")) {
				jsonArray = result.getJSONArray("DATA");
				int length = jsonArray.length();
				item = new GonggaoItem[length];
				for (int i = 0; i < length; i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					CommonConvert convert = new CommonConvert(obj);
					GonggaoItem temp = new GonggaoItem();
					temp.title = convert.getString("F2_4230");
					temp.id = convert.getString("F1_4230");
					temp.date = convert.getString("F4_4230");
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
