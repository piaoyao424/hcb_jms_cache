package com.btten.costitem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

public class UserPayChoiceActivity extends BaseActivity implements  OnClickListener {
	
	private TextView tv_paychoice_showname = null;
	private TextView tv_paychoice_yue = null;
	private TextView tv_paychoice_showyue = null;
	private TextView tv_paychoice_showtongji = null;
	
	
	private Button btn_paychoice_huibipay = null;
	private Button btn_paychoice_cardpay = null;
	
	private String total_cost_str = null;
	private String total_costcounts_str = null;
	private String cost_phone_str = null;
	private String cost_userid_str = null;
	private String huibi_yue_str = null;
	
	SharedPreferences setting = null;
	
	CostCard_Scene_Jms costChoiceScene;
	private String total_itemids_str = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpaychoice_activity);
		
		init();
	}
	
	private void init() {
		getViews();
		getBundleValue();
		initTitle();
	}
	
	private void getViews() {
		tv_paychoice_showname = (TextView) findViewById(R.id.tvId_paychoice_showname);
		tv_paychoice_yue = (TextView) findViewById(R.id.tvId_paychoice_yue);
		tv_paychoice_showyue = (TextView) findViewById(R.id.tvId_paychoice_showyue);
		tv_paychoice_showtongji = (TextView) findViewById(R.id.tvId_paychoice_showtongji);
		
		btn_paychoice_huibipay = (Button) findViewById(R.id.btnId_paychoice_huibipay);
		btn_paychoice_cardpay = (Button) findViewById(R.id.btnId_paychoice_cardpay);
		btn_paychoice_huibipay.setOnClickListener(this);
		btn_paychoice_cardpay.setOnClickListener(this);
		
	}
	private void getBundleValue()
	{
		// 取得主页面发送来的数据
		
		setting = getSharedPreferences("resultprice", MODE_PRIVATE);
		//消费金钱
		total_cost_str = String.valueOf(setting.getInt("total_money", 0));
		//消费项目
		total_costcounts_str = String.valueOf(setting.getInt("total_costcounts", 0));
		//付款人名字
		cost_phone_str = setting.getString("cost_phone", null);
		huibi_yue_str = setting.getString("server_huibi", null);
		total_itemids_str = setting.getString("total_itemids", "");
		
		System.out.println("惠币付款的物品id为: "+total_itemids_str);
		System.out.println("待付款金额为： "+ total_cost_str);
		System.out.println("惠币余额为： "+ huibi_yue_str);
		System.out.println("userpaychoice -> getBundleValue ->消费项目为 = "+ total_costcounts_str);
		System.out.println("userpaychoice -> getBundleValue -> 用户名为 = "+cost_phone_str);
		
		tv_paychoice_showname.setText(cost_phone_str);
		tv_paychoice_showyue.setText(huibi_yue_str);
		tv_paychoice_showtongji.setText("本次消费 "+total_costcounts_str+" 个项目,订单合计: "+total_cost_str+"元");
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		setCurrentTitle("选择消费方式");
		setBackKeyListner(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//会员手机号登录
		case R.id.btnId_paychoice_huibipay:
			float cost = setting.getInt("total_money", 0);
			float yue = Float.valueOf(huibi_yue_str);
			System.out.println("cost = " + cost);
			System.out.println("yue = " + yue);
			if (cost > yue) {
				Alert("该会员的惠币余额不足，请充值后再付款。或用银行卡付款");
				return;
			}
			Intent intent = new Intent();
			intent.setClass(UserPayChoiceActivity.this, UserPay_HuibiActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("KEY_PAYNAME", editTexts[0].getText().toString());
//			System.out.println(SYSLOGOUT + " -> btnListener -> 传递出去用户名  = " + editTexts[0].getText().toString());
//			intent.putExtras(bundle);
			startActivity(intent);
			break;
		
		//取消
		case R.id.btnId_paychoice_cardpay:
			// TODO 银行卡付款
			cardCost();
			break;
		}
	}
	
	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);
			btn_paychoice_huibipay.setOnClickListener(UserPayChoiceActivity.this);
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			Alert("数据提交成功,跳转回首页!");
			startActivity(new Intent(UserPayChoiceActivity.this, MainActivity.class));
		}
	};
	
	private void cardCost(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("会员本次消费"+ total_costcounts_str +"个项目，订单合计： "+ total_cost_str +"元，请确认会员已成功刷银行卡？")
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						costChoiceScene = new CostCard_Scene_Jms();
						costChoiceScene.doJmsScene(callBack, cost_phone_str,total_itemids_str,total_cost_str);
						// TODO Auto-generated method stub
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}
}
