package com.btten.jmsinfo;

import org.json.JSONObject;

import android.util.Log;

import com.btten.model.BaseJsonItem;
import com.btten.tools.CommonConvert;

public class BaseInfoItem_Jms extends BaseJsonItem{
	public String name,sex,level,qq,phone,username,
	       referrer,birthday,email,creditnum,
	       identitynum,district,address,photourl;
	public int usertype;
	@Override
	public boolean CreateFromJson(JSONObject result) {
		try{
			CommonConvert convert=new CommonConvert(result);
			this.status=convert.getInt("STATUS");
			this.info=convert.getString("INFO");
			if(this.status==1){
				
				this.photourl = convert.getString("PIC");
				this.name=convert.getString("NAME");
				this.sex=convert.getString("SEX");
				this.level=convert.getString("LEVEL");
				this.qq=convert.getString("QQ");
				this.phone=convert.getString("PHONE");
				this.username=convert.getString("USERNAME");
				this.referrer=convert.getString("REFERRER");
				this.birthday=convert.getString("BIRTHDAY");
				this.email=convert.getString("EMAIL");
				this.creditnum=convert.getString("CREDITNUM");
				this.identitynum=convert.getString("IDENTITYNUM");
				this.district=convert.getString("DISTRICT");
				this.address=convert.getString("ADDRESS");
				this.usertype = convert.getInt("USERTYPE");
			}
		}catch(Exception ex){
			this.status=-1;
			this.info=ex.toString();
			Log.d("Error",this.info);
			return false;
		}
		return true;
	}
	
}
