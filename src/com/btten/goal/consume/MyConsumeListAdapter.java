package com.btten.goal.consume;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.btten.Jms.ListItemAdapter;
import com.btten.Jms.R;
import com.btten.calltaxi.Gonggao.GonggaoContentActivity;
import com.btten.calltaxi.Gonggao.GonggaoItem;
import com.btten.goal.consume.Detail.MyConsumeDetailActivity;

public class MyConsumeListAdapter extends ListItemAdapter{
	MyConsumeView callBack;
	Activity context;
	
	public MyConsumeListAdapter(Activity context, MyConsumeView callBack) {
		super(context);
		// TODO Auto-generated constructor stub
		this.callBack = callBack;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null)
			convertView = inflater.inflate(items[position].getItemLayout(), null);
		
		convertView.setOnClickListener(new adapterListner(position));
		items[position].initView(convertView);
		
		return convertView;
	}

	class adapterListner implements OnClickListener{
		int index;
		public adapterListner (int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, MyConsumeDetailActivity.class);
			intent.putExtra("KEY_BIANHAO", ((MyConsumeListItem)items[index]).sale_bianhaoStr);
			intent.putExtra("KEY_TOTAL", ((MyConsumeListItem)items[index]).totalcostStr);
			context.startActivity(intent);
		
		}
	}
}
