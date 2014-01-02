package com.btten.costitem;

import org.json.JSONObject;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;
import com.btten.Jms.BtAPPJMS;

public class CostitemCheck_ResultItems_Jms extends BaseJsonItem {
	private static String TAG = "LoginResultItems";

	public CostitemCheck_ResultItem_Jms costCheck_item  ;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		costCheck_item = new CostitemCheck_ResultItem_Jms();
		try {
			this.status = result.getInt("STATUS");
			// 有数据
			if (this.status == 1) {
				CommonConvert convert = new CommonConvert(result);
				costCheck_item.server_huibi = convert.getString("INFO");
				costCheck_item.cost_status = 1;
			} else {
				this.info = result.getString("INFO");
			}
		} catch (Exception e) {
			this.status = -1;
			this.info = e.toString();
			BtAPPJMS.getInstance();
			BtAPPJMS.ReportError(TAG + "error:\n" + e.toString()
					+ "\nresult:\n" + result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
