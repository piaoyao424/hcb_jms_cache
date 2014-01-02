package com.btten.calltaxi.Gonggao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import com.btten.model.BaseJsonItem;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;
import com.btten.tools.CommonConvert;

@SuppressLint("HandlerLeak")
public class GonggaoAdItems extends BaseJsonItem {
	private JSONArray jsonArray = null;
	List<Bitmap> item = new ArrayList<Bitmap>();
	boolean isFinish = false;

	@Override
	public boolean CreateFromJson(JSONObject result) {
		try {
			this.status = result.getInt("STATUS");
			this.info = result.getString("INFO");
			if (this.status == 1 && !result.isNull("DATA")) {
				jsonArray = result.getJSONArray("DATA");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = null;
					String url = null;
					try {
						obj = jsonArray.getJSONObject(i);
						CommonConvert convert = new CommonConvert(obj);
						// 生成图片网络地址
						url = UrlFactory.rootUrl_short
								+ convert.getString("F1_4415");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						continue;
					}
					// 获取图片
					Bitmap bitmap0 = getBitmap(url);
					if (bitmap0 != null) {
						item.add(bitmap0);
					}
				}
			}
		} catch (Exception ex) {
			this.status = -1;
			this.info = ex.toString();
			return false;
		}
		return true;
	}

	@SuppressLint("SdCardPath")
	//本地有缓存文件直接加载，否则后台下载图片，下次启动加载
	public Bitmap getBitmap(final String imageUrl) {
		String fileName = null;
		Bitmap bitmap = null;

		if (imageUrl == null || imageUrl.length() == 0) {
			return null;
		}
		// 获取图片的文件名与后缀
		fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		// 图片在手机本地的存放路径,注意：fileName为空的情况
		final File file = new File("/sdcard/calltaxi/Cache/", fileName);

		if (!file.exists() && !file.isDirectory()) {
			ThreadPoolUtils.execute(new Runnable() {

				@Override
				public void run() {
					InputStream inputStream = null;
					try {
						inputStream = new URL(imageUrl).openStream();
						if (inputStream == null) {
							System.out.println("获取网络图片数据失败");
						} else {
							Bitmap bitmap = BitmapFactory
									.decodeStream(inputStream);
							// 保存到本地
							file.createNewFile();
							FileOutputStream out = new FileOutputStream(file);
							bitmap.compress(CompressFormat.PNG, 100, out);
							out.flush();
							out.close();
							System.out.println("保存本地图片成功!");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("保存到本地失败!");
					}
				}
			});
		} else {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File("/sdcard/calltaxi/Cache/",
						fileName));
				bitmap = BitmapFactory.decodeStream(fis);
				if (bitmap == null) {
					System.out.println("读取本地图片失败!");
				}
				System.out.println("读取本地图片成功!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("读取本地图片失败!");
			}
		}
		return bitmap;
	}
}
