package com.btten.calltaxi.CallTaxi.tools;

import org.json.JSONException;
import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class CallTaxiPublishDesignateItem extends BaseJsonItem{
	private static final String TAG = "CallTaxiPublishDesignateItem";

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		CommonConvert convert = new CommonConvert(result);
		try {
			this.status = convert.getInt("STATUS");
			this.info = convert.getString("INFO");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			this.status = -1;
			this.info = e.toString();
			BtAPPJMS.getInstance().ReportError(
					TAG + "error:\n" + e.toString() + "\nresult:\n"
							+ result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		
		return true;
	}

}
