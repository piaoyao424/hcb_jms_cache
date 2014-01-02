package com.btten.calltaxi.Service.core;

import java.util.ArrayList;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.LoginRequest;

import android.util.Log;

import com.btten.calltaxi.Service.extmodel.AcceptInfo;
import com.btten.calltaxi.Service.extmodel.CConst;
import com.btten.calltaxi.Service.extmodel.CallTaxiInfo;
import com.btten.calltaxi.Service.extmodel.MarkDriverInfo;
import com.btten.calltaxi.Service.extmodel.TaxiPairInfo;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class JmsMapManager implements IEventListener{
	
	JmsMapLisener mlisener;
	public void SetListener(JmsMapLisener lisener)
	{
		mlisener=lisener;
	}
	
	public static SmartFox sfsClient;

	//所有请求
	private ArrayList<CallTaxiInfo> allCallTaxiInfo;
	private Object t_request_lock=new Object();
	//当前请求
	private CallTaxiInfo cuCallTaxiInfo;
	
	//加入队列
	private void AddCallTaxiInfo(CallTaxiInfo trequest)
	{
		synchronized (t_request_lock) {
			allCallTaxiInfo.add(trequest);
		}
	}
	private void ClearCallTaxiInfo()
	{
		synchronized (t_request_lock) {
			allCallTaxiInfo.clear();
		}
	}
	
	private CallTaxiInfo PopRequest()
	{
		synchronized (t_request_lock) {
			if(allCallTaxiInfo.size()<=0)
				return null;
			//这里有超时的逻辑要判断
			while(allCallTaxiInfo.size()!=0)
			{
				CallTaxiInfo ret=allCallTaxiInfo.get(0);
				allCallTaxiInfo.remove(0);
				if(System.currentTimeMillis()-ret.getRequestTime()>CConst.RequestDelayTime)
				{
					//超时
					continue;
				}else
				{
					return ret;
				}
				
			}
			
			return null;
		}
	}
	//默认是未连接
	Boolean isLastConnected;
	//状态改变会调用
	private void connectionCheck()
	{
		Boolean tmpConnected=sfsClient.isConnected();
		if(tmpConnected!=isLastConnected)
		{
			isLastConnected=tmpConnected;
			if(mlisener!=null)
			{
				mlisener.OnConnectionChange(isLastConnected);
			}
		}
	}
	
	//去掉队列中的request ,如果当前用户正在accept这个request,要return false,
	//要跟cuCallTaxiInfo 比较
	private void RemoveCallTaxiInfo(String SID)
	{
		if(SID==null)
			return;
		synchronized (t_request_lock) {
			/*
			if(cuCallTaxiInfo!=null)
			{
				if(trequest.serverID.equals(cuCallTaxiInfo.serverID))
				{
					cuCallTaxiInfo=null;
				}
			}*/
			for(int i=0;i<allCallTaxiInfo.size();i++)
			{
				CallTaxiInfo tmp = allCallTaxiInfo.get(i);
				if(SID.equals(tmp.getSID()))
				{
					allCallTaxiInfo.remove(i);
					break;
				}
			}
		}
	}
	//去掉所有的超时请求
	/*
	private void ClearOldRequest()
	{
		synchronized (t_request_lock) {
			long cuTime=System.currentTimeMillis();
			for(int i=allCallTaxiInfo.size()-1;i>=0;i--)
			{
				CallTaxiInfo tmp=	allCallTaxiInfo.get(i);
				//超时了
				if(cuTime-tmp.getRequestTime()>CConst.RequestDelayTime)
				{
					allCallTaxiInfo.remove(i);
				}
			}
		}
	}
	*/
	
	private static JmsMapManager instance;
	public static JmsMapManager getInstance()
	{
		 if(instance==null)
		 {
			 instance=new JmsMapManager();
		 }
		 return instance;
	}
	
	public Boolean IsConnect()
	{
		 if(sfsClient.isConnected())
		 {
				return true;
		 }
		 return false;
	}
	LoginState loginState;
	DriverState driverState;
	String loginID;
	private JmsMapManager()
	{
		sfsClient = new SmartFox(false);
		sfsClient.addEventListener(SFSEvent.CONNECTION, this);
		sfsClient.addEventListener(SFSEvent.CONNECTION_LOST, this);
		sfsClient.addEventListener(SFSEvent.PING_PONG, this);
		sfsClient.addEventListener(SFSEvent.EXTENSION_RESPONSE, this);
		sfsClient.addEventListener(SFSEvent.LOGIN, this);
		sfsClient.addEventListener(SFSEvent.CONNECTION_RETRY, this);
		sfsClient.addEventListener(SFSEvent.CONNECTION_RESUME, this);
		sfsClient.addEventListener(SFSEvent.HANDSHAKE, this);
		sfsClient.addEventListener(SFSEvent.SOCKET_ERROR, this);
		sfsClient.setReconnectionSeconds(0);
		sfsClient.setUseBlueBox(false) ;
		//sfsClient.enableLagMonitor(true,5) ;
		 
		allCallTaxiInfo=new ArrayList<CallTaxiInfo>();	
		IsDriver=false;
		isLastConnected=false;
		driverState=DriverState.DriverIdle;
		loginState=LoginState.UnLogin;
		loginID=GenCID();
		tryTime=0;
		MonitorThreadInit();
		
	}

	private String cuUserID;
	public void Login(String username)
	{
		cuUserID=username;
		loginState=LoginState.Logined;
	}
	
	public void Logout()
	{
		loginState=LoginState.UnLogin;
	}
	
	//外部网络变化
	public void ConnectionChange()
	{
		SafeKillConnection();
	}
	
	private synchronized void SafeKillConnection()
	{
		try {
			if((sfsClient.isConnected())||sfsClient.isConnecting())
			{
				sfsClient.disconnect();
				//sfsClient.killConnection();
			}
		} catch (Exception e) {

		}
	}
	//外部网络断了
	public void ConnectionDown()
	{
		SafeKillConnection();
	}
	Thread monitorThread;
	private void MonitorThreadInit()
	{
		monitorThread= new Thread() {
			@Override
			public void run() {
				while (monitorThread!=null) {
					try {
						TryConnect();
					}catch (Exception e) {
						Log.e("CallTaxiManager", e.toString());
					}
					try {
						//状态改变就通知一下
						connectionCheck();
						
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
				}
				
			}
		};
		monitorThread.start();
	}
	
	private void MonitorThreadUnit()
	{
		if(monitorThread!=null)
		{
			monitorThread=null;
		}
	}
	
	int tryTime;
	private void TryConnect() {
		 if(loginState==LoginState.UnLogin)
		 {
			 SafeKillConnection();
		 }else if(loginState==LoginState.Logined)
		 {
			 if(sfsClient.isConnected())
				{
				 	tryTime=0;
					//已经连接上服务器
				 	
				 	if(IsHeartBitOver())
				 	{
				 		//如果长时间没有收到心跳，直接当断线
				 		doConnect();
				 	}
				}else if(sfsClient.isConnecting())
				{
					//正在连接中
					tryTime++;
					if(tryTime>4)
					{
						tryTime=0;
						doConnect();
					}
				}else
				{
					tryTime=0;
					doConnect();
				}
		 
		 }
	}
	
	private synchronized void doConnect() {
		Log.i("CallTaxiManager","doConnect()");
		SafeKillConnection();
		lastHeartBitTime=System.currentTimeMillis();
		sfsClient.connect(CConst.CallTaxiServerIP, CConst.CallTaxiServerPort);
	}
	public synchronized void CheckIfIdle()
	{
		if(GetDriverState()!=DriverState.DriverIdle)
			return;
		if(mlisener==null)
			return;

		cuCallTaxiInfo= PopRequest();
		if(cuCallTaxiInfo==null)
		{
			return;
		}
		
		//返回给UI
		SetDriverBusy();
		mlisener.OnNewOrder(cuCallTaxiInfo);
	}
	
	private void SetDriverIdle()
	{
		driverState=driverState.DriverIdle;
	}
	private void SetDriverBusy()
	{
		driverState=driverState.DriverBusy;
	}
	public DriverState GetDriverState()
	{
		return driverState;
	}
	public int getDriverType(){
		return currentDriverType;
	}
	
	boolean IsDriver;
	//加入司机角色,同时给出位置信息
	int currentDriverType=0;
	String lastLonLat;
	public void MarkAsDriver(int type,double longitude,double latitude)
	{
		IsDriver=true;
		currentDriverType=type;
		//坐标信息
		ISFSObject sfso = new SFSObject();
		MarkDriverInfo newInfo=new MarkDriverInfo();
		Geohash e = new Geohash();
		String lonlat = e.encode(latitude, longitude);
		newInfo.setType(type);
		newInfo.setLonLat(lonlat);
		lastLonLat=lonlat;
		sfso.putClass("data", newInfo);
		sfsClient.send( new ExtensionRequest(CConst.RequestMark, sfso, null));
		
		ClearCallTaxiInfo();
		SetDriverIdle();
	}
	
	//如果断线重新连接了，再自动传一下自己的信息
	private void SendDriverInfo()
	{
		if(!IsDriver)
			return;
		if(lastLonLat==null||lastLonLat.length()<=0)
			return;
		ISFSObject sfso = new SFSObject();
		MarkDriverInfo newInfo=new MarkDriverInfo();
		 
		newInfo.setType(currentDriverType);
		newInfo.setLonLat(lastLonLat);
		sfso.putClass("data", newInfo);
		sfsClient.send( new ExtensionRequest(CConst.RequestMark, sfso, null));
		
	}
	//退出司机角色
	public void UnMarkDriver()
	{
		IsDriver=false;
		ISFSObject sfso = new SFSObject();	
		sfsClient.send( new ExtensionRequest(CConst.RequestUnMark, sfso, null));
	}
	
	
	public Boolean GetIsDriver()
	{
		return IsDriver;
	}
	
	//更新位置信息
	public void UpdatePosition(double longitude,double latitude)
	{
		ISFSObject sfso = new SFSObject();
		MarkDriverInfo newInfo=new MarkDriverInfo();
		Geohash e = new Geohash();
		String lonlat = e.encode(latitude, longitude);
		newInfo.setType(currentDriverType);
		newInfo.setLonLat(lonlat);
		lastLonLat=lonlat;
		sfso.putClass("data", newInfo);
		sfsClient.send( new ExtensionRequest(CConst.RequestUpdate, sfso, null));
	}
	
	//拿到附近点的位置
	public void RequestNearBy(double longitude,double latitude)
	{
		ISFSObject sfso = new SFSObject();
		Geohash e = new Geohash();
		String lonlat = e.encode(latitude, longitude);
		sfso.putUtfString("lonlat", lonlat);
		sfsClient.send( new ExtensionRequest(CConst.RequestNearby, sfso, null));
	}
	
	//接受叫的请求 ,如果接受超时，直接超时
	public Boolean Accept(AcceptInfo acceptyInfo)
	{
		//当前的单子已经被人抢走了
		ISFSObject sfso = new SFSObject();
		acceptyInfo.setTid(cuUserID);
		sfso.putClass("data", acceptyInfo);
		sfsClient.send( new ExtensionRequest(CConst.RequestAccept, sfso, null));
		return true;
	}
	
	//忽略请求,又开始看下一次的请求
	public void MakeDriverIdler()
	{
		SetDriverIdle();
		CheckIfIdle();
	}
	
	//叫taxi 返回ClientID
	public String CallTaxi(CallTaxiInfo callinfo)
	{
		ISFSObject sfso = new SFSObject();
		Geohash e = new Geohash();
		String lonlat = e.encode(callinfo.getLat(), callinfo.getLon());
		callinfo.setLonLat(lonlat);
		callinfo.setCID(GenCID());
		callinfo.setUID(cuUserID);
		callinfo.setLoginID(loginID);
		sfso.putClass("data", callinfo);
		sfsClient.send( new ExtensionRequest(CConst.RequestCallTaxi, sfso, null));
		return callinfo.getCID();
	}
	
	//得到本地的唯一ID
	private String GenCID()
	{
		return java.util.UUID.randomUUID().toString();
	}
	 
	public void Uninit()
	{
		
	}

	private void handleUserResponse(String cmd,ISFSObject resObj)
	{
		if(cmd.equalsIgnoreCase(CConst.RequestAnswer))
		{
				//叫车成功或者失败，用户角度
			if(mlisener==null)
				return;
		 
			int status=resObj.getInt("status");
			String CID=resObj.getUtfString("CID");
			AcceptInfo acceptInfo=null;
			if(status==1)
				 acceptInfo=(AcceptInfo) resObj.getClass("data");
			 
			mlisener.OnCallResultBack(status,CID,acceptInfo);
		}else if(cmd.equalsIgnoreCase(CConst.RequestNearbyResult))
		{
			if(mlisener==null)
				return;
			String lonlatlist=resObj.getUtfString("lonlat");
			mlisener.OnNearByPosition(lonlatlist);
		}
	}
	long lastHeartBitTime=0;
	
	//是不是长时间没有心跳了
	private Boolean IsHeartBitOver()
	{
		//40秒看情况
		if(System.currentTimeMillis()-lastHeartBitTime>40000)
		{
			Log.i("CallTaxiManager","HeartBitOver");
			return true;
		}
		return false;
	}
	private Boolean HandleHeartBit(String cmd)
	{
		if(cmd.equalsIgnoreCase(CConst.HeartBit))
		{
		//	Log.i("tt","HeartBit");
			lastHeartBitTime=System.currentTimeMillis();
			return true;
		}
		return false;
	}
	private void handleDriverResponse(String cmd,ISFSObject resObj)
	{
		if(!IsDriver)
			return;
		if(cmd.equalsIgnoreCase(CConst.RequestCallInfo))
		{
			
			  
			CallTaxiInfo newReq=new CallTaxiInfo();
			newReq=(CallTaxiInfo) resObj.getClass("data");
			newReq.setRequestTime(System.currentTimeMillis());
			 
			//自己又当司机又当用户
			if(newReq.getUID().equals(cuUserID))
			{
				return;
			}
			
			AddCallTaxiInfo(newReq);
			//如果有空闲，就浮上去
			CheckIfIdle();
			
		}else if(cmd.equalsIgnoreCase(CConst.RequestGrabResult))
		{
			//抢单结果，司机角度
			/*
			   请求的serverID (SID)
			   状态（成功或者失败）(STA)
			   说明信息（文字描述）(INFO)
			   请求人信息（UID，名字 NAME，联系方式 PHONE）*/
			 
			if(mlisener==null)
				return;
			
			CallTaxiInfo info=null;
			int status=resObj.getInt("status");
			String orderid=resObj.getUtfString("orderid");
			if(status==1)
				info=(CallTaxiInfo) resObj.getClass("data");
			
			mlisener.OnOrderResultBack(status,orderid,info);
			
		}else if(cmd.equalsIgnoreCase(CConst.RequestTaxiPairInfo))
		{
			//别人 的叫车匹配信息
			TaxiPairInfo newInfo=new TaxiPairInfo();
			newInfo=(TaxiPairInfo) resObj.getClass("data");
			RemoveCallTaxiInfo(newInfo.getSID());			   
		} 
	}
	@Override
	public void dispatch(BaseEvent event) throws SFSException {
		if (event.getType().equalsIgnoreCase(SFSEvent.EXTENSION_RESPONSE))
		{
			String cmd = event.getArguments().get("cmd").toString();
			if(HandleHeartBit(cmd))
			{
				return;
			}
			ISFSObject resObj = new SFSObject();
			resObj = (ISFSObject) event.getArguments().get("params");
			handleUserResponse(cmd,resObj);
			handleDriverResponse(cmd,resObj);
			
		}else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION))
		{
			Log.i("CallTaxiManager","CONNECTION");
			try {
				sfsClient.send(new LoginRequest(loginID, "", "BasicExamples"));	
			} catch (Exception e) {
				 
			}
		}else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST))
		{
			//doConnect();
			Log.i("CallTaxiManager","CONNECTION_LOST");
		}else if(event.getType().equalsIgnoreCase(SFSEvent.LOGIN))
		{
			Log.i("CallTaxiManager","CONNECTION_LOGIN");
			SendDriverInfo();
		}else if(event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_RETRY))
		{
			Log.i("CallTaxiManager","CONNECTION_RETRY");
			 
		}else if(event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_RESUME))
		{
			Log.i("CallTaxiManager","CONNECTION_RESUME");
			 
		}else if(event.getType().equalsIgnoreCase(SFSEvent.HANDSHAKE))
		{
			Log.i("CallTaxiManager","HANDSHAKE");
			
		}
		else if(event.getType().equalsIgnoreCase(SFSEvent.SOCKET_ERROR))
		{	 
			Log.i("CallTaxiManager","SOCKET_ERROR");
			
		}
		else if(event.getType().equalsIgnoreCase(SFSEvent.PING_PONG))
		{	 
			Log.i("CallTaxiManager","PING_PONG");
			
		}
		
		 
		
	}
}
