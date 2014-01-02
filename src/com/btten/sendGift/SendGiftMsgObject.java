package com.btten.sendGift;

import com.btten.account.JmsAccountManager;

public class SendGiftMsgObject {
	public String jid = null;
	public String gid = null;
	public String userName = null;
	public String password = null;
	public String gnumber = null;
	public String gname = null;

	public SendGiftMsgObject(String gid, String gname, String gnumber,
			String username, String password) {
		this.gid = gid;
		this.gname = gname;
		this.gnumber = gnumber;
		this.userName = username;
		this.password = password;
		this.jid = JmsAccountManager.getInstance().getJmsUserid();
	}
}
