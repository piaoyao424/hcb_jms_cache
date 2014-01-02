package com.btten.goal.jiesuan;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;

public class MyJiesuanListItem extends ListItemBase {
	ViewChild Child = new ViewChild();
	
	public String huibiStr;
	public String cardStr;
	public String dayStr;
	public String totalmoneyStr;
	public String totaljiesuanStr;
	
	public MyJiesuanListItem (){
		layoutId = R.layout.my_jiesuan_list_item;
	}
	
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

		Child.tv_date = (TextView) view.findViewById(R.id.myjiesuan_item_numdate);
		Child.tv_huibipay = (TextView) view.findViewById(R.id.myjiesuan_item_huibipay);
		Child.tv_cardpay = (TextView) view.findViewById(R.id.myjiesuan_item_cardpay);
		Child.tv_totalmoney = (TextView) view.findViewById(R.id.myjiesuan_item_totalmoney);
		Child.tv_totaljiesuan = (TextView) view.findViewById(R.id.myjiesuan_item_totaljiesuan);
		Child.tv_date.setText(dayStr);
		Child.tv_huibipay.setText(huibiStr);
		Child.tv_cardpay.setText(cardStr);
		Child.tv_totalmoney.setText(totalmoneyStr);
		Child.tv_totaljiesuan.setText(totaljiesuanStr);
	}
	
	class ViewChild {
		public String totalmoney;
		public TextView tv_date;
		public TextView tv_huibipay;
		public TextView tv_cardpay;
		public TextView tv_totalmoney;
		public TextView tv_totaljiesuan;

	}

}
