package com.vincent.tool;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.c;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper = null;
	private SQLiteDatabase db = null;
	public final static String TABLENAME_STRING = "CacheData";

	// 建议放在Application里面创建DBManager对象
	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param Persons
	 */
	public void add(List<CacheData> cacheDatas) {
		db.beginTransaction(); // 开始事务
		try {
			for (CacheData cacheData : cacheDatas) {
				db.execSQL("INSERT INTO " + TABLENAME_STRING
						+ " VALUES(?, ?, datetime('now'))", new Object[] {
						cacheData.url, cacheData.value });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void updateValue(CacheData cacheData) {

		db.execSQL(" REPLACE INTO " + TABLENAME_STRING
				+ " (url,value,date) VALUES (?, ?, datetime('now'))",
				new Object[] { cacheData.url, cacheData.value });
	}

	/**
	 * update Person's age
	 * 
	 * @param Person
	 */
	public void updateData(CacheData cacheData) {
		ContentValues cv = new ContentValues();
		cv.put("value", cacheData.value);
		db.update(TABLENAME_STRING, cv, "url = ?",
				new String[] { cacheData.url });
	}

	/**
	 * delete old Person
	 * 
	 * @param Person
	 */
	public void delete(CacheData cacheData) {
		db.delete(TABLENAME_STRING, "url = ?", new String[] { cacheData.url });
	}

	/**
	 * query all persons, return list
	 * 
	 * @return List<Person>
	 */
	public List<CacheData> queryAll() {
		ArrayList<CacheData> cacheDatas = new ArrayList<CacheData>();
		Cursor c = queryTheCursor("SELECT * FROM " + TABLENAME_STRING);
		while (c.moveToNext()) {
			CacheData cacheData = new CacheData();
			cacheData.url = c.getString(c.getColumnIndex("url"));
			cacheData.value = c.getString(c.getColumnIndex("value"));
			cacheData.date = c.getString(c.getColumnIndex("date"));
			cacheDatas.add(cacheData);
		}
		c.close();
		return cacheDatas;
	}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String sql) {
		Cursor c = db.rawQuery(sql, null);
		return c;
	}

	public String queryValuebyUrl(String url) {
		Cursor c = null;
		try {
			c = db.rawQuery(" select value from " + TABLENAME_STRING
					+ " where url = ? ", new String[] { url });
			c.moveToFirst();
			return c.getString(0);
		} catch (Exception e) {
			return null;
		} finally {
			c.close();
		}
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

}
