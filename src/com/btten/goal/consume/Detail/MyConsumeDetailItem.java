package com.btten.goal.consume.Detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyConsumeDetailItem extends ListItemBase{
//	ImageLoader imageLoader = null; 
	ViewChild Child = new ViewChild();
//	public String goodsnum;
	public String goodsName, goodsPrice ;
//	public String goodstotalprice;
	//public String goodspicurl;
	
	
	public MyConsumeDetailItem(){
		layoutId = R.layout.my_consume_list_detail_item;
	//	initImageLoader();
	}
	
	private void initImageLoader(){
//		imageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		Child.tv_shopname = (TextView) view.findViewById(R.id.consume_list_detail_goods_name);
		Child.tv_price = (TextView) view.findViewById(R.id.consume_list_detail_price);
		Child.tv_num = (TextView) view.findViewById(R.id.consume_list_detail_num);
//		Child.tv_num.setText(goodsnum);
		Child.tv_total_price = (TextView) view.findViewById(R.id.consume_list_detail_total_price);
//		Child.tv_total_price.setText(goodstotalprice);
		//Child.iv_pic = (ImageView) view.findViewById(R.id.consume_list_detail_image);
		
		//imageLoader.displayImage(goodspicurl, Child.iv_pic);
		Child.tv_shopname.setText(goodsName);
		Child.tv_total_price.setText(goodsPrice);
		}
	
	class ViewChild {
		public TextView tv_shopname;
		public TextView tv_price;
		public TextView tv_num;
		public TextView tv_total_price;
		//public ImageView iv_pic;
	}
}
