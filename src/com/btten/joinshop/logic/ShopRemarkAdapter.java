package com.btten.joinshop.logic;

import java.util.ArrayList;

import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.R;
import com.btten.joinshop.ShopCommentItem;
import com.btten.joinshop.ShopCommentItems;
import com.btten.tools.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShopRemarkAdapter extends BaseAdapter {

	Activity context;
	public ArrayList<ShopCommentItem> items;

	public ShopRemarkAdapter(Activity context){
		this.context=context;
		init();
	}
	public void init(){
		items = new ArrayList<ShopCommentItem>(); 
	}
	
	public void setItem(ShopCommentItems shopItem){
		if(shopItem==null||shopItem.items==null||shopItem.items.size()<=0) return;
		items = shopItem.items;
	}
	
	public void addItem(ShopCommentItems shopItem){
		if(shopItem==null||shopItem.items==null||shopItem.items.size()<=0) return;
		for(int i=0;i<shopItem.items.size();i++ ){
			ShopCommentItem item=shopItem.items.get(i);
			if(item==null){
				continue;
			}
			items.add(item);
		}
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=context.getLayoutInflater().inflate(R.layout.joinshop_comment_list_item,null);
			//holder.image=(ImageView)convertView.findViewById(R.id.remark_host_image);
			holder.name=(TextView)convertView.findViewById(R.id.joinshop_comment_username);
			holder.time=(TextView)convertView.findViewById(R.id.joinshop_comment_time);
			holder.content=(TextView)convertView.findViewById(R.id.joinshop_comment_content);
			holder.star=(RatingBar)convertView.findViewById(R.id.joinshop_comment_item_ratingbar);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		ShopCommentItem item=items.get(position);
		holder.item=item;
		
		/*if(item.headimg.isEmpty()||item.headimg.equals("")||item.headimg==null)
		{
		holder.image.setImageResource(R.drawable.image_default_icon);
		}else
		{
			ImageLoader.getInstance().displayImage(item.headimg, holder.image);
		}*/
		
		if(Util.IsEmpty(item.name))
		{
			holder.name.setText(BtAPPJMS.getInstance().GetAnonymousName());
		}
		else
		{
			holder.name.setText(item.name);
		}
		holder.star.setRating(item.star);
		holder.time.setText(item.time);
		holder.content.setText(item.content);
		return convertView;
	}
	private class ViewHolder //implements OnImageGetListener
	{
		ImageView image;
		ShopCommentItem item;
		TextView name,time,content;
		RatingBar star;
		/*
		@Override
		public void OnGetImg(String url, Bitmap bmp, Object privateData) {
			// TODO Auto-generated method stub
			 if(null==privateData||bmp==null)
				 return;
			 if(item.headimg==url)
			 {
				 image.setImageBitmap(bmp); 

			 }
			 else{
			    image.setImageResource(R.drawable.profile_icon);
			 }
		}*/
	}
}

