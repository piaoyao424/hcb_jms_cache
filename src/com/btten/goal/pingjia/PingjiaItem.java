package com.btten.goal.pingjia;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class PingjiaItem extends ListItemBase {
	String id;
	int score;
	int cash;
	String name,content,time;
	int rating;
//	TextView tv_id, tv_score, tv_cash, tv_name, tv_time, tv_position;
	TextView tv_content, tv_name, tv_time;
	RatingBar tv_rating;
	public PingjiaItem() {
		layoutId = R.layout.mypingjia_list_item;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

		tv_content=(TextView)view.findViewById(R.id.tvId_pingjia_item_text);
		tv_rating=(RatingBar)view.findViewById(R.id.rbId_pingjia_item_rating);
		tv_name = (TextView)view.findViewById(R.id.tvId_pingjia_item_name);
		tv_time = (TextView)view.findViewById(R.id.tvId_pingjia_item_time);
		
		tv_name.setText(name);
		tv_time.setText(time);
		tv_content.setText(content);
		tv_rating.setRating(rating);
	}
}
