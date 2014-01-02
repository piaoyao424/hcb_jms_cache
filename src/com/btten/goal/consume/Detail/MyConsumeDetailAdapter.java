package com.btten.goal.consume.Detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.btten.Jms.ListItemAdapter;
import com.btten.goal.consume.MyConsumeListItem;
import com.btten.goal.consume.MyConsumeView;
import com.btten.joinshop.JoinShopActivity;

public class MyConsumeDetailAdapter extends ListItemAdapter{
	MyConsumeDetailActivity context;
	
	public MyConsumeDetailAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = (MyConsumeDetailActivity) context;  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null)
			convertView = inflater.inflate(items[position].getItemLayout(), null);
		
//		Button bn = ((Button) convertView.findViewById(R.id.myconsume_comment_button));
//		bn.setOnClickListener(new adapterListner(position));
		//条目点击
//		LinearLayout ll = ((LinearLayout) convertView.findViewById(R.id.myconsume_comment_content));
//		ll.setOnClickListener(new adapterListner(position));
		
//		((MyConsumeListItem)items[position]).numStr = (position+1)+"";
//		convertView.setOnClickListener(new adapterListner(position));
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
			System.out.println("点击了consume detail");
//			Intent intent = new Intent(context, MyConsumeDetailActivity.class);
//			intent.putExtra("KEY_BIANHAO", ((MyConsumeListItem)items[index]).sale_bianhaoStr);
//			context.startActivity(intent);
			System.out.println("点击了consume detail, end");
		
		}
	}
}
