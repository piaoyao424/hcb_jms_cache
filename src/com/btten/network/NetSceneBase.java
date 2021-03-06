package com.btten.network;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import com.btten.Jms.BtAPPJMS;
import com.btten.tools.Log;
import com.vincent.tool.CacheData;
import com.vincent.tool.DBManager;

@SuppressLint("NewApi")
public abstract class NetSceneBase<Result> implements Runnable {

	private static final String TAG = "NetSceneBase";
	private static final int CancelCode = -1;
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAIL = 1;

	public enum Status {
		/**
		 * Indicates that the task has not been executed yet.
		 */
		PENDING,
		/**
		 * Indicates that the task is running.
		 */
		RUNNING,
		/**
		 * Indicates that {@link AsyncTask#onPostExecute} has finished.
		 */
		FINISHED,

	}

	private static final InternalHandler sHandler = new InternalHandler();

	private static class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			NetResult result = (NetResult) msg.obj;
			if (result == null)
				return;
			if (result.scene == null)
				return;

			result.scene.doInForeground(msg.what, result);

		}
	}

	private static class NetResult {
		NetSceneBase scene;
		Object result;
		int statusCode;
	}

	private volatile Status mStatus = Status.PENDING;

	private boolean isPost = false;

	public NetSceneBase(boolean isPost) {
		this.isPost = isPost;
		params = new BasicHttpParams();
		mStatus = Status.PENDING;
	}

	protected void SetPost(boolean post) {
		isPost = post;
	}

	protected HttpParams params;
	protected HttpGet httpGet;
	protected HttpPost httpPost;
	protected String targetUrl;

	private void httpInit() {
		if (isPost) {
			httpPost = new HttpPost(targetUrl);
			httpPost.setParams(params);
		} else {
			httpGet = new HttpGet(targetUrl);
			httpGet.setParams(params);

		}
	}

	private void httpAbord() {
		if (isPost) {
			if (httpPost == null)
				return;
			httpPost.abort();

		} else {
			if (httpGet == null)
				return;
			httpGet.abort();
		}
	}

	private void httpReset() {
		httpGet = null;
		httpPost = null;
	}

	private HttpResponse httpRequest() throws ClientProtocolException,
			IOException {
		if (isPost) {
			if (httpPost == null)
				return null;
			return HttpClientHelper.getHttpClient().execute(httpPost);

		} else {
			if (httpGet == null)
				return null;
			return HttpClientHelper.getHttpClient().execute(httpGet);
		}

	}

	@SuppressWarnings("unchecked")
	public void run() {
		// 只能执行一次
		if (mStatus != Status.PENDING)
			return;

		mStatus = Status.RUNNING;

		// 为这个HttpGet设置一些特定的属性，别的属性沿用HttpClient
		HttpConnectionParams.setConnectionTimeout(params, 60000);

		httpInit();

		try {
			HttpResponse httpResponse = httpRequest();
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
			}
			Result result = doReceive(httpResponse.getEntity());
			success(result, 0);

		} catch (Exception ex) {
			// 读出缓存
			DBManager dbManager = new DBManager(BtAPPJMS.getInstance());
			String value = dbManager.queryValuebyUrl(targetUrl);
			dbManager.closeDB();

			if (value == null || value.trim().isEmpty()) {
				// 无相关缓存
			} else {
				try {
					success((Result) new JSONObject(value), 1);
					return;
				} catch (Exception e) {

				}
			}
			failed(NetConst.NetConnectError);
			Log.Exception(TAG, ex);
		}
		return;
	}

	// 设置回调
	OnSceneCallBack callBack;

	public void SetCallBack(OnSceneCallBack oncallBack) {
		callBack = oncallBack;
	}

	// 只能回调一次
	public OnSceneCallBack GetCallBack() {
		OnSceneCallBack ret = callBack;
		callBack = null;
		return ret;
	}

	protected void success(Result result, Integer Flag) {
		if (mStatus != Status.RUNNING)
			return;
		mStatus = Status.FINISHED;
		httpReset();

		NetResult resultObj = new NetResult();
		resultObj.scene = this;
		resultObj.result = result;
		// 写入数据库备份缓存
		DBManager dbManager = new DBManager(BtAPPJMS.getInstance());
		CacheData cacheData = new CacheData(targetUrl, result.toString());
		dbManager.updateValue(cacheData);
		dbManager.closeDB();

		Message message;
		message = sHandler.obtainMessage(MSG_SUCCESS, resultObj);
		message.sendToTarget();
	}

	private void failed(int statusCode) {
		if (mStatus != Status.RUNNING)
			return;
		mStatus = Status.FINISHED;
		httpReset();

		NetResult resultObj = new NetResult();
		resultObj.scene = this;
		resultObj.statusCode = statusCode;

		Message message;
		message = sHandler.obtainMessage(MSG_FAIL, resultObj);
		message.sendToTarget();

	}

	public void cancel() {
		if (mStatus != Status.RUNNING)
			return;
		try {
			httpAbord();
		} catch (Exception ex) {
			Log.Exception(TAG, ex);
		}

		failed(CancelCode);
	}

	private void doInForeground(int what, NetResult result) {
		switch (what) {
		case MSG_SUCCESS:
			onSuccess((Result) result.result);
			break;
		case MSG_FAIL:
			onFailed(result.statusCode);
			break;

		default:
			break;
		}
		result.scene = null;
		result = null;
	}

	abstract protected Result doReceive(HttpEntity httpEntity);

	abstract protected void onFailed(int statusCode);

	abstract protected void onSuccess(Result result);

	// targetUrl,params,ThreadPoolUtils.execute(this);
	// abstract public void doScene();

}
