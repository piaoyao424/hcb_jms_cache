package com.btten.sendGift;

import org.json.JSONObject;
import android.util.Log;
import com.btten.model.BaseJsonItem;

public class SendGiftResultItems extends BaseJsonItem {

	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
		} catch (Exception ex) {
			this.status = -1;
			this.info = ex.toString();
			Log.d("gwjtag", info);
			return false;
		}
		return true;
	}

}
