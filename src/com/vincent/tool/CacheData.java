package com.vincent.tool;

public class CacheData {
	public String url = null;
	public String value = null;
	public String date = null ;

	public CacheData() {

	}

	public CacheData(String url, String cacheData) {
		this.url = url;
		this.value = cacheData;
	}

	public String getCacheData(String url) {

		try {
			if (url.equals(this.url)) {
				return value;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
