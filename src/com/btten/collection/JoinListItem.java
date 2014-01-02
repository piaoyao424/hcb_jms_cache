package com.btten.collection;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;

public class JoinListItem extends ListItemBase{

	
	public JoinListItem (){
		layoutId = R.layout.joinshop_list_item;
	}
	
	public String num;
	public String id;
	public String name;
	public String carnum;
	public String telephone;
	public String address;
	public String photo;
	
	
	ImageView joinshop_item_picture;
	TextView joinshop_shop_name;
	RatingBar joinshop_item_ratingbar;
	TextView joinshop_shop_address;
	TextView joinshop_shop_phone;
	
	@Override
	public void initView(View view) {

		joinshop_shop_name = (TextView) view.findViewById(R.id.joinshop_shop_name);
		joinshop_shop_address = (TextView) view.findViewById(R.id.joinshop_shop_address);
		joinshop_shop_phone = (TextView) view.findViewById(R.id.joinshop_shop_phone);
		joinshop_item_picture = (ImageView) view.findViewById(R.id.joinshop_item_picture);
		joinshop_item_ratingbar = (RatingBar) view.findViewById(R.id.joinshop_item_ratingbar);
		 
		joinshop_shop_address.setText(address);
		joinshop_shop_phone.setText(telephone);
		joinshop_shop_name.setText(num+". "+name);
		
	}
	
 

}
