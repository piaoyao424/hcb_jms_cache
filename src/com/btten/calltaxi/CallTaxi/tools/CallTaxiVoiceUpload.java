package com.btten.calltaxi.CallTaxi.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountsException;
import android.util.Log;

import com.btten.account.JmsAccountManager;
import com.btten.network.OnUploadCallBack;
import com.btten.network.UrlFactory;
import com.btten.tools.CommonConvert;

/**
 * 上传音频文件到服务器中
 * 
 * @author YXML
 */
public class CallTaxiVoiceUpload {

	VoiceUploadCallBack callback;
	String TAG = "UploadVoice";

	public CallTaxiVoiceUpload(VoiceUploadCallBack callback, File file) {
		this.callback = callback;
		doUpload(file, "UploadVoice", "doUploadVoice", "userid", JmsAccountManager
				.getInstance().getUserid());
	}

	private void doUpload(File file, String data, String aciton, String... args) {
		URL url;
		try {
			url = new URL(UrlFactory.GetUrlNew(data, aciton, args));
			Log.i(TAG, url.toString());
			uploadFile(file, url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.onFail();
			return;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.onFail();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.onFail();
			return;
		}
	}

	private void uploadFile(File file, URL url) throws InterruptedException,
			IOException {
		{
			int timeout = 30000;
			HttpURLConnection httpUrlConnection = null;
			try {
				httpUrlConnection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callback.onFail();
				return;
			}
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setConnectTimeout(timeout);
			httpUrlConnection.setReadTimeout(timeout);
			httpUrlConnection.setRequestProperty("Content-Type",
					"binary/octet-stream");
			try {
				httpUrlConnection.setRequestMethod("POST");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callback.onFail();
				return;
			}
			OutputStream os = null;
			try {
				os = httpUrlConnection.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callback.onFail();
				return;
			}

			int bufferLenth = 10*1024;

			DataInputStream datais = new DataInputStream(new FileInputStream(
					file));
			byte[] bufferOut = new byte[bufferLenth];
			while (datais.read(bufferOut, 0, bufferLenth) != -1) {
				os.write(bufferOut, 0, bufferLenth);
			}
			datais.close();
			os.flush();
			os.close();

			int responCode = httpUrlConnection.getResponseCode();

			if (responCode == HttpURLConnection.HTTP_OK) {
				InputStream ins = httpUrlConnection.getInputStream();
				String str = null;
				StringBuffer temp = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						ins, "UTF-8"));
				while ((str = br.readLine()) != null)
					temp.append(str);
				str = temp.toString();
				
				try {
					
					JSONObject tempJSON = new JSONObject(str);
					CommonConvert convert = new CommonConvert(tempJSON);
					callback.onSuccess(convert.getString("VOICELINK"));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			} else if (responCode != HttpURLConnection.HTTP_OK) {
				callback.onFail();
			}

		}
	}
}