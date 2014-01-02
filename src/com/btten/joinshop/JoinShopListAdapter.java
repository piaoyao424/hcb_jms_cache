package com.btten.joinshop;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.btten.Jms.ListItemAdapter;

public class JoinShopListAdapter extends ListItemAdapter{
	JoinShopActivity context;
	
	public JoinShopListAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = (JoinShopActivity) context;  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null)
			convertView = inflater.inflate(items[position].getItemLayout(), null);
		
		((JoinshopListItem)items[position]).num = (position+1)+"";
		
		items[position].initView(convertView);
		
		convertView.setOnClickListener(new adapterListner(position));
		
		return convertView;
	}
	
	public class adapterListner implements OnClickListener{
		int index;
		public adapterListner (int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, JoinShopContentActivity.class);
			intent.putExtra("id", ((JoinshopListItem)items[index]).id);
			context.startActivity(intent);
		}
	}
}
