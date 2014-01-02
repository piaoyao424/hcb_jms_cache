package com.btten.joinshop.logic;

import org.json.JSONObject;

import com.btten.Jms.BtAPPJMS;
import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;

public class JoinshopInfoItems extends BaseJsonItem{
	private String TAG="JoinshopInfothiss";
	private CommonConvert convert;
	//public JoinshopInfothis this;
	public String name,telephone,score,address,des,photo,fid;
	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			convert=new CommonConvert(result);
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			// 有数据
			if (this.status == 1){
				
					this.name=convert.getString("NAME");
					this.address=convert.getString("ADDRESS");
					this.telephone=convert.getString("TELEPHONE");
					this.score=convert.getString("star");
					this.des=convert.getString("DES");
					this.photo=convert.getString("PHOTO");
					this.fid = convert.getString("FID");
			}

		} catch (Exception e) {
			this.status=-1;
			this.info=e.toString();
			BtAPPJMS.getInstance().ReportError(TAG+"error:\n"+e.toString()+"\nresult:\n"+result.toString());
			Log.Exception(TAG, e);
			return false;
		}
		return true;
	}
	
}
