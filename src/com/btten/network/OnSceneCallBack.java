package com.btten.network;

public interface OnSceneCallBack {
	void OnFailed(int status,String info,NetSceneBase<?> netScene);
	void OnSuccess(Object data,NetSceneBase<?> netScene);
}
