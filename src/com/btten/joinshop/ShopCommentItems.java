package com.btten.joinshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class ShopCommentItems extends BaseJsonItem {

	public  String TAG="ShopCommentItems";
	private CommonConvert convert;

	public ArrayList<ShopCommentItem> items; 

	@Override
	public boolean CreateFromJson(JSONObject result) {
		// TODO Auto-generated method stub
		ShopCommentItems comItems=this;
		try {
			comItems.status = result.getInt("STATUS");
			comItems.info = result.getString("INFO");
			// 有数据
			if (comItems.status == 1&&!result.isNull("DATA")){
				comItems.items=new ArrayList<ShopCommentItem>();
				JSONArray comlist=result.getJSONArray("DATA");
				int length = comlist.length();
				for (int i = 0; i < length; i++) {// 遍历JSONArray
					JSONObject obj= comlist.getJSONObject(i);
					convert=new CommonConvert(obj);
					ShopCommentItem item=new ShopCommentItem();
					item.time=convert.getString("TIME");
					item.photo=convert.getString("PHOTO");
					item.content=convert.getString("CONTENT");
					item.name=convert.getString("NAME");
					item.id=convert.getInt("ID");
					item.star=convert.getInt("STAR");
					comItems.items.add(item);
				}
			}

		} catch (Exception e) {
			comItems.status=-1;
			comItems.info=e.toString();
			BtAPPJMS.getInstance().ReportError(TAG+"error:\n"+e.toString()+"\nresult:\n"+result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}

}
