package com.btten.calltaxi.Service.core;

import com.btten.calltaxi.Service.extmodel.AcceptInfo;
import com.btten.calltaxi.Service.extmodel.CallTaxiInfo;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public interface JmsMapLisener {
	// 收到各种事件
	// 司机的抢单结果,status为1时 为成功，orderid为某订单id, info可能为空
	void OnOrderResultBack(int status, String orderid, CallTaxiInfo info);

	// 用户的下单结果,status为1时 为成功,失败时，info为null
	void OnCallResultBack(int status, String CID, AcceptInfo acceptInfo);

	// 收到新的请求
	void OnNewOrder(CallTaxiInfo request);

	// 收到附近的位置列表
	void OnNearByPosition(String nearbylist);

	// 连接改变
	void OnConnectionChange(Boolean isConnected);

}
