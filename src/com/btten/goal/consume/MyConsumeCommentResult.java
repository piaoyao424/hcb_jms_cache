package com.btten.goal.consume;

import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class MyConsumeCommentResult extends BaseJsonItem{
	private static final String TAG = "MyConsumeCommentResult";
	
	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		MyConsumeCommentResult items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
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
