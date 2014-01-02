package com.btten.calltaxi.Gonggao;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;
import android.view.View;
import android.widget.TextView;

public class GonggaoItem extends ListItemBase {
	String title= "",date= "",id = "";
	TextView tv_title, tv_date,tv_xiangxi;
	
	public GonggaoItem() {
		layoutId = R.layout.gonggao_list_item;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		tv_title=(TextView)view.findViewById(R.id.tvId_gonggao_title);
		tv_date=(TextView)view.findViewById(R.id.tvId_gonggao_date);
		tv_xiangxi = (TextView)view.findViewById(R.id.tvId_gonggao_xiangxi);
		
		tv_title.setText(title);
		tv_date.setText(date);
		tv_xiangxi.setText("详细");
	}
}
