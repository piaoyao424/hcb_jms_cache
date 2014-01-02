package com.btten.goal.bank;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;

public class bankOfFenlei extends Activity {
	// 控件
	private TextView tv_bankfenlei_title = null;
	private ListView lv_bankfenlei_banklist = null;
	// 线程
	private shengThread updthread = null;
	// 省市信息
	private String getShengshiInfo = null;
	// 异常日志
	private final static String TAG = "Log_bankfenlei";
	// 省市的数组
	private String[] str_bankname = { "中国工商银行", "中国建设银行", "中国农业银行", "中国银行", "招商银行",
	"交通银行" };
// 线程
	private MyUIHandler uiHandler;
	// msghandle通信
	private final static int BANK_UI = 1;
	//省市银行的信息
	private String shengshi = null;
	private String fengefu = "XX";
	// list
	private ArrayList<HashMap<String, Object>> fenleiList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bankoffenlei);
		// setBackKeyListner(backListener);
		// start

		// 准备工作
		findViews();
		init();
	}

	private void findViews() {
		tv_bankfenlei_title = (TextView) findViewById(R.id.tvId_bankfenlei_title);
		lv_bankfenlei_banklist = (ListView) findViewById(R.id.lvId_bankfenlei_banklist);
	}

	private void init() {
		// 取得主页面发送来的数据
		Bundle bundle = this.getIntent().getExtras();
		getShengshiInfo = bundle.getString("KEY_SHENGSHIINFO");
		System.out.println("来源省市的信息为>>>>>>>>>>>>> " + getShengshiInfo);

		uiHandler = new MyUIHandler();
		updthread = new shengThread();
		updthread.start();

	}

	private void setBankList() {
		SimpleAdapter adapter = new SimpleAdapter(this, fenleiList,
				R.layout.bankitem, new String[] { "fenleiname" },
				new int[] { R.id.tvId_bankitem_item });

		lv_bankfenlei_banklist.setAdapter(adapter);

		lv_bankfenlei_banklist
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// 显示对应的章节
						System.out.println("点中银行 :"	+ fenleiList.get(arg2).get("fenleiname")
												.toString());
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
//						intent.setClass(bankOfFenlei.this,debitCardActivity.class);
						// send Mes to other view
						shengshi = getShengshiInfo + fengefu +fenleiList.get(arg2).get("fenleiname")
								.toString();
						System.out.println("银行名字信息为 :"	+ shengshi);
						bundle.putString("KEY_BANKALLINFO",shengshi);
						// intent.setClass(ShowDirectory.this, ReadBook.class);
						// bundle.putString("KEY_BOOKPATH",
						// dirList.get(arg2).get("dirpath").toString());
						// convertCodeAndGetText(dirList.get(arg2).get("dirpath").toString());
						intent.putExtras(bundle);
						startActivity(intent);
						bankOfFenlei.this.finish();
					}
				});

	}

	class MyUIHandler extends Handler {
		public MyUIHandler() {
		}

		public MyUIHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case BANK_UI:
				System.out.println("打开分类的列表!");

				setBankList();
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	// 定义一个读取省的名字的线程。
	class shengThread extends Thread {
		public shengThread() {
			super();
		}

		public void run() {
			try {
				System.out.println("打开银行分类目录线程执行！");
				// 取得本地的txt文件中的字符案串
				// String newReadStr = readTxtFile(bookFiles.toString());
				// split函数返回的数组以实际得到的元素个数为准。而不是定义的时候的255个。
				// arrayNewRead = newReadStr.split("\\|\\|");
				// getBookNameFromPath(arrayNewRead);
				// 取得数据库中的记录保存到listview中去
				getAllShi();
				Message msg = uiHandler.obtainMessage(BANK_UI, 0, 0, 0);
				uiHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.e(TAG, "Error in shengThread -> run() " + e.toString());
			}

		}
	}

	private void getAllShi() {
		// String[][] list = new String[50][3];
		// 所有省市的个数
		int allCount = str_bankname.length;
		System.out.println("所有银行的个数为：" + allCount);

		for (int i = 0; i < allCount; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("fenleiindex", i);
			map.put("fenleiname", str_bankname[i]);
			fenleiList.add(map);
		}
	}

	OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};

}
