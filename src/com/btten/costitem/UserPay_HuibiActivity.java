package com.btten.costitem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

public class UserPay_HuibiActivity extends BaseActivity {

	private TextView tv_payhuibi_showdd = null;
	private TextView tv_payhuibi_showname = null;
	private TextView tv_payhuibi_showyue = null;
	private TextView tv_payhuibi_queren = null;
	private TextView tv_payhuibi_showtongji = null;
	private EditText et_payhuibi_pwd = null;
	private Button btn_payhuibi_ok = null;
	// 保存的消费信息
	private String total_cost_str = "";
	private String total_costcounts_str = null;
	private String cost_phone_str = null;
	private String cost_userid_str = null;
	private String huibi_yue_str = null;
	private String cost_id_str = null;
	private String total_itemids_str = "";

	SharedPreferences setting = null;
	// 惠币密码
	String huibipwd = null;
	UserPayHuibiScene mScene = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpay_huibi_activity);
		init();
		// initPersonalInfo();
	}

	private void init() {
		getViews();
		getBundleValue();
		initTitle();
	}

	private void getViews() {
		tv_payhuibi_showname = (TextView) findViewById(R.id.tvId_payhuibi_showname);
		tv_payhuibi_showyue = (TextView) findViewById(R.id.tvId_payhuibi_showyue);
		tv_payhuibi_queren = (TextView) findViewById(R.id.tvId_payhuibi_queren);
		tv_payhuibi_showtongji = (TextView) findViewById(R.id.tvId_payhuibi_showtongji);

		et_payhuibi_pwd = (EditText) findViewById(R.id.etId_payhuibi_pwd);
		btn_payhuibi_ok = (Button) findViewById(R.id.btnId_payhuibi_ok);
		btn_payhuibi_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// switch (v.getId()) {
				// //惠币消费
				// case R.id.btnId_payhuibi_ok:
				System.out.println("提交惠币付款信息");
				DoRequest();
				// break;
				// }
			};
		});
	}

	private void getBundleValue() {
		// 取得主页面发送来的数据
		setting = getSharedPreferences("resultprice", MODE_PRIVATE);
		// 消费金钱
		total_cost_str = String.valueOf(setting.getInt("total_money", 0));
		// 消费项目
		total_costcounts_str = String.valueOf(setting.getInt(
				"total_costcounts", 0));
		// 付款人名字
		cost_phone_str = setting.getString("cost_phone", null);
		huibi_yue_str = setting.getString("server_huibi", null);
		// cost_id_str = setting.getString("cost_userid", null);
		cost_id_str = setting.getString("cost_userid", null);
		total_itemids_str = setting.getString("total_itemids", "");

		System.out.println("待付款金额为： " + total_cost_str);
		System.out.println("惠币余额为： " + huibi_yue_str);
		System.out.println("userpay_Huibi -> getBundleValue ->消费项目为 = "
				+ total_costcounts_str);
		System.out.println("userpay_Huibi -> getBundleValue -> 用户手机为 = "
				+ cost_phone_str);
		System.out.println("userpay_Huibi -> getBundleValue -> 用户id为 = "
				+ cost_id_str);
		System.out.println("userpay_Huibi -> getBundleValue -> 物品id为 = "
				+ total_itemids_str);
		tv_payhuibi_showname.setText(cost_phone_str);
		tv_payhuibi_showyue.setText(huibi_yue_str);
		tv_payhuibi_showtongji.setText("本次消费 " + total_costcounts_str
				+ " 个项目, 订单合计: " + total_cost_str + "元");
		tv_payhuibi_queren.setText("本次消费将扣除 " + total_cost_str + " 个惠币,请会员确认");
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		setCurrentTitle("惠币消费");
		setBackKeyListner(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private boolean getpwd() {
		huibipwd = et_payhuibi_pwd.getText().toString().trim();
		if (huibipwd.length() <= 0) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void DoRequest() {
		if (getpwd()) {
			ShowProgress("提交付款申请中", "请稍候……");
			mScene = new UserPayHuibiScene();
			System.out.println("惠币付款的加盟商id为: "
					+ JmsAccountManager.getInstance().getJmsUserid());
			System.out.println("惠币付款的物品id为: " + total_itemids_str);
			System.out.println("惠币付款的money为: " + total_cost_str);
			mScene.doScene(callBack, cost_phone_str, huibipwd,
					total_itemids_str, total_cost_str);
		}
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();
			ErrorAlert(status, info);
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			HideProgress();

			new AlertDialog.Builder(UserPay_HuibiActivity.this)
					.setTitle("提示")
					.setMessage(
							"会员本次消费" + total_costcounts_str + "个项目，订单合计： "
									+ total_cost_str + "元!,订单已成功。")
					.setPositiveButton("确认返回",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(
											UserPay_HuibiActivity.this,
											MainActivity.class));
								}
							}).show();
		}

	};

}
