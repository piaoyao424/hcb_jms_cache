package com.btten.joinshop;

import org.json.JSONException;
import org.json.JSONObject;

import com.btten.model.BaseJsonItem;

public class addtofavItems extends BaseJsonItem{
	public String fid;
	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			if (status == 1 && !result.isNull("FID"))
				fid = result.getString("FID");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
