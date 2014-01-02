package com.btten.goal.jiesuan;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.btten.Jms.ListItemAdapter;
import com.btten.Jms.R;

public class MyJiesuanListAdapter extends ListItemAdapter{
	MyJiesuanView callBack;
	
	public MyJiesuanListAdapter(Activity context, MyJiesuanView callBack) {
		super(context);
		// TODO Auto-generated constructor stub
		this.callBack = callBack;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null)
			convertView = inflater.inflate(items[position].getItemLayout(), null);
		
//		Button bn = ((Button) convertView.findViewById(R.id.myconsume_comment_button));
//		bn.setOnClickListener(new adapterListner(position));
//		LinearLayout ll = ((LinearLayout) convertView.findViewById(R.id.myconsume_comment_content));
//		ll.setOnClickListener(new adapterListner(position));
		
		items[position].initView(convertView);
		
		return convertView;
	}

//	class adapterListner implements OnClickListener{
//		int index;
//		public adapterListner (int index){
//			this.index = index;
//		}
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			callBack.curItem = ((MyJiesuanListItem)items[index]);
//			callBack.onClick(v);
//		}
//	}
}
