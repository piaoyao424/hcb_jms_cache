package com.btten.goal.pingjia;

import java.util.ArrayList;

import com.btten.Jms.ListItemAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyPingjiaAdapter extends ListItemAdapter {
	View view;
	Activity context;

	public MyPingjiaAdapter(Activity context) {
		super(context);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Log.i("position = "+position, "parent.getchildposition"+parent.getChildCount());
		if (convertView == null){
			convertView = inflater.inflate(items[position].getItemLayout(), null);
		}
		items[position].initView(convertView);
//		((PingjiaItem)items[position]).tv_position.setText(String.valueOf(position+1));

		return convertView;
	}
}
