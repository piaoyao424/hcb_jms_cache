package com.btten.goal.jiesuan;

import org.json.JSONArray;
import org.json.JSONObject;
import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class MyJiesuanListResult extends BaseJsonItem {
	private static String TAG = "MyConsumeResult";
	private JSONArray jsonArray = null;
	public MyJiesuanListItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		MyJiesuanListResult items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");

			// 有数据
			if (items.status == 1 && !result.isNull("DATA")) {
				this.jsonArray = result.getJSONArray("DATA");
				item = new MyJiesuanListItem[this.jsonArray.length()];
				MyJiesuanListItem temp;
				for (int i = 0; i < item.length; ++i) {
					temp = new MyJiesuanListItem();
					CommonConvert convert = new CommonConvert(
							this.jsonArray.getJSONObject(i));

					temp.dayStr = convert.getString("DAYTIME");
					temp.huibiStr = convert.getString("HUIBI");
					temp.cardStr = convert.getString("CARD");
					if (temp.huibiStr == null || temp.huibiStr.equals("")) {
						temp.huibiStr = "0";
					}
					if (temp.cardStr == null || temp.cardStr.equals("")) {
						temp.cardStr = "0";
					}
					temp.totalmoneyStr = String.valueOf(Integer
							.valueOf(temp.huibiStr)
							+ Integer.valueOf(temp.cardStr));
					// 保留两位小数
					temp.totaljiesuanStr = String.format("%.1f", (Integer
							.valueOf(temp.huibiStr) + Integer
							.valueOf(temp.cardStr)) * 0.9922);
					item[i] = temp;
				}

			}
		} catch (Exception e) {
			items.status = -1;
			items.info = e.toString();
			BtAPPJMS.getInstance();
			BtAPPJMS.ReportError(
					TAG + "error:\n" + e.toString() + "\nresult:\n"
							+ result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
