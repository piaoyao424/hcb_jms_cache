package com.btten.account;

import com.btten.Jms.BtAPPJMS;
import com.btten.tools.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class JmsAccountManager {
	
	private static final String PREFS_NAME = "user_info";
	private static final String UID_KEY = "userid";
	private static final String NICKNAME_KEY = "nickname";
	private static final String USERNAME_KEY = "username";
	private static final String USERIMAGE_KEY="userimage";
	private static final String USERPHONE_KEY = "userphone";
	private static final String USERCARNUM_KEY="usercarnum";
	private static final String USERTYPE_KEY = "usertype";
	//加盟商
	private static final String JMS_PHONE_KEY = "jms_phone";
	private static final String JMS_USERNAME_KEY = "jms_username";
	private static final String JMS_USERID_KEY = "jms_userid";
	//加盟商-end
	private String userid;
	private String username;
	private String userimage;
	
	private String userphone; //用户手机号
	private String usercarnum;
	
	private int usertype;
	//jms
	private String jms_phone;
	private String jms_username;
	private String jms_userid;
	
	private String nickname;
	//user
	SharedPreferences settings = BtAPPJMS.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = settings.edit();
	
	//jms
	SharedPreferences jmsSettings = BtAPPJMS.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	SharedPreferences.Editor JmsEditor = jmsSettings.edit();
	/**
	 * 设置当前用户车牌号
	 */
	public void setCarnum(String carnum) {
		this.usercarnum = carnum;
		editor.putString(USERCARNUM_KEY, usercarnum).commit();
	}
	/**
	 * 获取当前用户车牌号
	 */
	public String getCarnum() {
		return usercarnum;
	}
	
	/**
	 * 设置用户手机号
	 */
	public void setUserPhone(String phone){
		this.userphone = phone;
		editor.putString(USERPHONE_KEY, userphone).commit();
	}
	
	/**
	 * 设置用户类型
	 * @param type
	 */
	public void setUserType(int type){
		this.usertype = type;
		editor.putInt(USERTYPE_KEY, type).commit();
	}
	public int getUserType() {
		return usertype;
	}
	
	/**
	 * 获取缓存的用户手机号
	 */
	public String getUserPhone(){
		
		return userphone;
	}
	
	public void setUsername(String username) {
		this.username = username;
		editor.putString(USERNAME_KEY, username).commit();
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
		editor.putString(NICKNAME_KEY, nickname).commit();
	}
	
	public String getUserImage(){
		return userimage;
	}
	public String getUserid() {
		return userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getNickname() {
		if(Util.IsEmpty(nickname))
			return BtAPPJMS.getInstance().GetAnonymousName();
		return nickname;
	}
	//jms
	public void setJmsUserPhone(String phone){
		this.jms_phone = phone;
		JmsEditor.putString(JMS_PHONE_KEY, jms_phone).commit();
	}
	public void setJmsUsername(String username) {
		this.jms_username = username;
		JmsEditor.putString(JMS_USERNAME_KEY, jms_username).commit();
	}
	public String getJmsUserPhone(){
		
		return jms_phone;
	}
	public String getJmsUsername() {
		return jms_username;
	}
	public String getJmsUserid() {
		return jms_userid;
	}
	
	public void SetInfo(String username,String userid,String nickname,String userimage) {
		this.username = username;
		this.nickname = nickname;
		this.userid = userid;
		this.userimage = userimage;
		
		editor.putString(UID_KEY, userid);
		editor.putString(NICKNAME_KEY, nickname);
		editor.putString(USERNAME_KEY, username);
		editor.putString(USERIMAGE_KEY, userimage);
		editor.commit();
		
	}

	
	public void SetJmsInfo(String jms_phone,String jms_username, String jms_userid) {
		
		this.jms_phone = jms_phone;
		this.jms_username = jms_username;
		this.jms_userid = jms_userid;
		
		JmsEditor.putString(JMS_PHONE_KEY, jms_phone);
		JmsEditor.putString(JMS_USERNAME_KEY, jms_username);
		JmsEditor.putString(JMS_USERID_KEY, jms_userid);
		JmsEditor.commit();
		
	}
	//jms-end
	public void setPhoto(String userimage){
		this.userimage=userimage;
		editor.putString(USERIMAGE_KEY, userimage);
		editor.commit();
	}
	
	private JmsAccountManager()
	{
		SharedPreferences jmsUserInfo =BtAPPJMS.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);  
		userid = jmsUserInfo.getString(UID_KEY, "");  
		username = jmsUserInfo.getString(USERNAME_KEY, "");  
		nickname = jmsUserInfo.getString(NICKNAME_KEY, ""); 
		userimage= jmsUserInfo.getString(USERIMAGE_KEY,"");
		userphone = jmsUserInfo.getString(USERPHONE_KEY, ""); 
		usercarnum= jmsUserInfo.getString(USERCARNUM_KEY, "");
		usertype = jmsUserInfo.getInt(USERTYPE_KEY, 5);
		//jms
		jms_phone = jmsUserInfo.getString(JMS_PHONE_KEY, ""); 
		jms_username = jmsUserInfo.getString(JMS_USERNAME_KEY, ""); 
		jms_userid = jmsUserInfo.getString(JMS_USERID_KEY, "");
		//jms-end
	}
	private static JmsAccountManager instance;
	public static JmsAccountManager getInstance()
	{
		if(instance==null)
		{
			instance=new JmsAccountManager();
		}
		return instance;
	}
	
	//是否登录成功
	public  boolean IsLogin()
	{
		if(userid==null||userid.length()<=0)
			return false;
		return true;
	}
	public void Logout()
	{
		SetInfo("","","","");
	}
	
	//Jms是否登录成功
	public  boolean IsJmsLogin()
	{
		if(jms_userid == null||jms_userid.length() <= 0)
		{
			return false;
		}
		else
		{
		return true;
		}
	}
	public void JmsLogout()
	{
		SetJmsInfo("","","");
	}
}
