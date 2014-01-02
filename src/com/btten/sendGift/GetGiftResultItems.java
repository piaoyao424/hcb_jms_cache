package com.btten.sendGift;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;

public class GetGiftResultItems extends BaseJsonItem {
	private JSONArray jsonArray = null;
	GetGiftResultItem[] item = null;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			if (this.status == 1 && !result.isNull("DATA")) {
				jsonArray = result.getJSONArray("DATA");
				int length = jsonArray.length();
				item = new GetGiftResultItem[length];
				for (int i = 0; i < length; i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					CommonConvert convert = new CommonConvert(obj);
					GetGiftResultItem temp = new GetGiftResultItem();

					temp.gid = convert.getString("GID");
					temp.gname = convert.getString("GNAME");
					temp.gnumber = convert.getString("GNUMBER");
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
