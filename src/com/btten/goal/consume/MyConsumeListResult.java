package com.btten.goal.consume;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.TextView;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class MyConsumeListResult extends BaseJsonItem {
	private static String TAG = "MyConsumeResult";
	private JSONArray jsonArray = null;
	public MyConsumeListItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		MyConsumeListResult items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
			
			// 有数据
			if (items.status == 1 && !result.isNull("DATA"))
			{	
				this.jsonArray = result.getJSONArray("DATA");
				item = new MyConsumeListItem[this.jsonArray.length()];
				MyConsumeListItem temp;
				for (int i=0; i<item.length; ++i){
					temp = new MyConsumeListItem();
					CommonConvert convert = new CommonConvert(this.jsonArray.getJSONObject(i));
					
					temp.sale_bianhaoStr = convert.getString("ID");
					temp.timeStr = convert.getString("TIME");
					temp.totalcostStr = convert.getString("TOTALCOST");
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
