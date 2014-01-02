package com.btten.joinshop;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JoinshopListItem extends ListItemBase{
	public String name,telephone,score,address,id,photo, num;
	
	public JoinshopListItem (){
		layoutId = R.layout.joinshop_list_item;
	}
	
	ImageView shopimg;
	TextView shopname;
	TextView shopadd;
	TextView shoptel;
	RatingBar shopRating;
	
	@Override
	public void initView(View view) {
		shopimg=(ImageView)view.findViewById(R.id.joinshop_item_picture);		
		shopname=(TextView)view.findViewById(R.id.joinshop_shop_name);		
		shopadd=(TextView)view.findViewById(R.id.joinshop_shop_address);
		shoptel=(TextView)view.findViewById(R.id.joinshop_shop_phone);
		shopRating=(RatingBar)view.findViewById(R.id.joinshop_item_ratingbar);
		
		
		if(!photo.equals("")){
			//ImageLoader.getInstance().displayImage(item.photo, photo);
			ImageLoader.getInstance().displayImage(photo, shopimg);
		}
		
		shopname.setText(num+". "+name);
		shopadd.setText(address);
		shoptel.setText(telephone);
	
		if(!score.equals("")){
			shopRating.setRating(Integer.parseInt(score));
		}
		
	}

	
}
