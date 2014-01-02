package com.btten.sendGift;

import android.view.View;
import android.widget.TextView;
import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;

public class GetGiftResultItem extends ListItemBase {
	public String gid = null;
	public String gname = null;
	public String gnumber = null;
	public TextView tv_gname = null;
	public TextView tv_gnumber = null;

	public GetGiftResultItem() {
		layoutId = R.layout.sendgift_list_item;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		tv_gname = (TextView) view.findViewById(R.id.tvId_sendgift_gname);
		tv_gnumber = (TextView) view.findViewById(R.id.tvId_sendgift_gnumber);

		tv_gname.setText(gname);
		tv_gnumber.setText("赠品编号："+gnumber);
	}
}
