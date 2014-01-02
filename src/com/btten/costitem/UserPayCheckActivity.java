package com.btten.costitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

public class UserPayCheckActivity extends BaseActivity implements
		OnClickListener {
	final int[] bns_id = { R.id.btnId_usercheck_payok,
			R.id.btnId_usercheck_paycancel };
	int[] editTexts_id = { R.id.etId_usercheck_name,
	// R.id.etId_userpay_pwd
	};
	Button[] bns = new Button[2];
	EditText[] editTexts = new EditText[1];
	CheckBox rememberPwdBox;
	SharedPreferences setting = null;
	String nameStr = null;
	private String SYSLOGOUT = "UserPayCheckActivity";
	// 登录
	private ListView lv_paycheck_list = null;
	private TextView tv_usercheck_showtotal = null;
	private final static String INDEX = "index";
	private final static String NAME = "name";
	private final static String MONEY = "money";
	private ArrayList<HashMap<String, Object>> viewList = new ArrayList<HashMap<String, Object>>();
	// 显示list
	private MyUIHandler uiHandler;
	private dingdanShowThread ddShowThread = null;
	private final static int DINGDAN_UI = 5;
	private String[][] ddlist = null;
	private int ddlist_counts = 0;
	private String dd_bundle_str = "";
	private String total_money_str = "";
	private String totalItemIds_str = "";

	private String dafengefu = "\\|";
	private String xiaofengefu = "\\&";
	private final static String ITEMNAME = "itemname";
	private final static String ITEMOLDP = "itemoldprice";
	private final static String ITEMVIPP = "itemvipprice";
	private final static String ITEMID = "itemid";
	// 服务器交互
	CostItemCheck_Scene_Jms costCheckScene = null;
	ProgressDialog progress;

	// 保存数据的类
	// private itemSerializable myitemserializable ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpaycheck_activity);

		init();
		// initPersonalInfo();
	}

	private void init() {
		for (int i = 0; i < 2; ++i) {
			bns[i] = (Button) findViewById(bns_id[i]);
			bns[i].setOnClickListener(this);
		}
		editTexts[0] = (EditText) findViewById(editTexts_id[0]);
		lv_paycheck_list = (ListView) findViewById(R.id.lvId_paycheck_list);
		tv_usercheck_showtotal = (TextView) findViewById(R.id.tvId_usercheck_showtotal);
		initTitle();
		getBundleValue();
		splitStrToStr2();
		// setValueToList(viewList,ddlist);
		uiHandler = new MyUIHandler();
		ddShowThread = new dingdanShowThread();
		ddShowThread.start();
	}

	// private void initPersonalInfo(){
	// setting = getSharedPreferences("paycfg", MODE_PRIVATE);
	// nameStr = setting.getString("nameStr", "");
	//
	// }

	private void initTitle() {
		// TODO Auto-generated method stub
		setCurrentTitle("消费的项目");
		setBackKeyListner(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void setlist() {

		SimpleAdapter adapter = new SimpleAdapter(this, viewList,
				R.layout.userpaycheck_list_item, new String[] { INDEX, NAME,
						MONEY }, new int[] { R.id.tvId_userpaycheck_item_num,
						R.id.tvId_userpaycheck_item_name,
						R.id.tvId_userpaycheck_item_money });

		lv_paycheck_list.setAdapter(adapter);

		lv_paycheck_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.setClass(MainActivity.this, ShowBook.class);
				// send Mes to other view
				// Bundle bundle = new Bundle();
				// bundle.putString("KEY_BOOKPATH", bookPath);
				// intent.putExtras(bundle);
				// startActivity(intent);

				System.out.println("点击了显示的订单!");
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

			case DINGDAN_UI:
				// System.out.println("���������!");
				setlist();
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	class dingdanShowThread extends Thread {
		public dingdanShowThread() {
			super();
		}

		public void run() {
			try {
				Message msg = uiHandler.obtainMessage(DINGDAN_UI, 0, 0, 0);
				uiHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.e("gwj_dingdanshow", "Error in dingdanShowThread -> run() "
						+ e.toString());
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 会员手机号登录
		case R.id.btnId_usercheck_payok:

			doLogin();
			// Intent intent = new Intent();
			// intent.setClass(UserPayCheckActivity.this,
			// UserPayChoiceActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putString("cost_name", editTexts[0].getText().toString());
			// intent.putExtras(bundle);
			// startActivity(intent);
			break;

		// 取消
		case R.id.btnId_usercheck_paycancel:
			// TODO 跳转到注册页面
			onBackPressed();
			break;
		}
	}

	private void doLogin() {
		nameStr = editTexts[0].getText().toString().trim();

		if (nameStr.length() <= 0) {
			Toast.makeText(this, "请输消费者手机号", Toast.LENGTH_SHORT).show();
			return;
		} else if (!isPhone(nameStr)) {
			Toast.makeText(this, "无效手机号", Toast.LENGTH_SHORT).show();
			return;
		}

		ShowProgress("登录中", "请稍候……");

		costCheckScene = new CostItemCheck_Scene_Jms();
		costCheckScene.doJmsScene(callBack, nameStr);
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);
			bns[0].setOnClickListener(UserPayCheckActivity.this);
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();

			CostitemCheck_ResultItems_Jms items = (CostitemCheck_ResultItems_Jms) data;
			CostitemCheck_ResultItem_Jms item = items.costCheck_item;
			Toast.makeText(UserPayCheckActivity.this, "登录成功",
					Toast.LENGTH_SHORT).show();
			setting = getSharedPreferences("resultprice", MODE_PRIVATE);
			setting.edit()
					.putString("cost_phone",
							editTexts[0].getText().toString().trim())
					.putString("server_huibi", item.server_huibi)
					.putString("cost_userid",
							editTexts[0].getText().toString().trim()).commit();

			// TODO 返回成功后跳转到HOME页面
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(UserPayCheckActivity.this,
							UserPayChoiceActivity.class);
					// Bundle bundle = new Bundle();
					// bundle.putString("KEY_USERID", userid);
					// intent.putExtras(bundle);
					startActivity(intent);
				}
			}, 200);
		}
	};

	OnClickListener onLoadingListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ShowProgress("登录中", "请稍候……");

		}
	};

	private boolean isPhone(String name) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	int FailedCount = 0;

	private void getBundleValue() {
		// 取得主页面发送来的数据
		// myitemserializable = new itemSerializable();
		Bundle bundle = this.getIntent().getExtras();
		ddlist_counts = bundle.getInt("KEY_DINGDANCOUNT");
		dd_bundle_str = bundle.getString("KEY_DINGDANSTR");
		// myitemserializable = (itemSerializable)
		// bundle.getSerializable("KEY_DINGDAN");
		System.out.println("userpaycheck -> ddlist_counts = " + ddlist_counts);
		System.out.println("userpaycheck -> dd_bundle_str = " + dd_bundle_str);

		setting = getSharedPreferences("resultprice", MODE_PRIVATE);
		total_money_str = String.valueOf(setting.getInt("total_money", 0));
		totalItemIds_str = setting.getString("total_itemids", "");
		System.out.println("userpaycheck -> getBundleValue -> 取得物品id为 = "
				+ totalItemIds_str);
		System.out.println("取出来的的金额为： " + total_money_str);
		tv_usercheck_showtotal.setText(total_money_str + "元");
	}

	private void splitStrToStr2() {
		String[] dd_temp = new String[255];
		dd_temp = dd_bundle_str.split(dafengefu);
		System.out.println("userpaycheck -> splitStrToStr2->dd_temp.length = "
				+ dd_temp.length);
		for (int i = 0; i < dd_temp.length; i++) {
			String[] str = new String[255];
			HashMap<String, Object> map = new HashMap<String, Object>();
			str = dd_temp[i].split(xiaofengefu);
			map.put(INDEX, i + 1);
			map.put(NAME, str[1]);
			map.put(MONEY, str[2] + "元");
			viewList.add(map);
			System.out.println("userpaycheck -> splitStrToStr2->str[] = "
					+ str[1]);
		}
	}
}
