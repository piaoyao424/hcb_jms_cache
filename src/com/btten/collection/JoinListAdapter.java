package com.btten.collection;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.btten.Jms.ListItemAdapter;

public class JoinListAdapter extends ListItemAdapter{

	public JoinListAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(items[position].getItemLayout(), null);
			
		((JoinListItem)items[position]).num = (position+1)+"";
		
		items[position].initView(convertView);
		
		return convertView;
	}
}
