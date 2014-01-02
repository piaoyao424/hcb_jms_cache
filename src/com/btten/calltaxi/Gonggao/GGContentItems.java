package com.btten.calltaxi.Gonggao;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;

public class GGContentItems extends BaseJsonItem {
	private static String TAG = "MyGGContentItemsItems";
	private JSONArray jsonArray = null;
	GGContenItem item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		
		item = new GGContenItem();
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			
			if (this.status == 1) {
				CommonConvert convert = new CommonConvert(result);
				// item_Jms.jms_username = convert.getString("jms_username");
				// item.userimage=convert.getString("userimage");
				//标题
//				item.title = convert.getString("F2_4230");
//				//内容
				item.content = convert.getString("F3_4230");
//				//id
//				item.id = convert.getString("F1_4230");
//				//日期
//				item.date = convert.getString("F4_4230");
				// item.workkind=convert.getString("workkind");
				// item.mesnum=convert.getInt("mesnum");
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
