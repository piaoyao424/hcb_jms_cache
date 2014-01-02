package com.btten.goal.consume.Detail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.TextView;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class MyConsumeDetailItems extends BaseJsonItem {
	private static String TAG = "MyConsumedetailResult";
	private JSONArray jsonArray = null;
	public MyConsumeDetailItem[] item;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		MyConsumeDetailItems items = this;
		try {
			items.status = result.getInt("STATUS");
			items.info = result.getString("INFO");
			
			// 有数据
			if (items.status == 1 && !result.isNull("DATA"))
			{	
				this.jsonArray = result.getJSONArray("DATA");
				item = new MyConsumeDetailItem[this.jsonArray.length()];
				MyConsumeDetailItem temp;
				for (int i=0; i<item.length; ++i){
					temp = new MyConsumeDetailItem();
					CommonConvert convert = new CommonConvert(this.jsonArray.getJSONObject(i));
					
//					temp.idStr = convert.getString("ID");
//					temp.shopnameStr = convert.getString("SHOP");
//					temp.jidStr = convert.getString("JID");
					temp.goodsName = convert.getString("GOODSNAME");
					temp.goodsPrice = convert.getString("GOODSPRICE");
//					temp.ordertypeStr = convert.getString("ORDERTYPE");
//					temp.earnedscoreStr = convert.getString("EARNEDSCORE");
					System.out.println("temp.goodsName :" + temp.goodsName);
					System.out.println("temp.goodsPrice :" + temp.goodsPrice);
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
