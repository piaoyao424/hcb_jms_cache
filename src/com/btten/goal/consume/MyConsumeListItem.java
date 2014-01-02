package com.btten.goal.consume;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;

public class MyConsumeListItem extends ListItemBase implements Parcelable{
	ViewChild Child = new ViewChild();
	
	public String timeStr;
	public String sale_bianhaoStr;
	public String totalcostStr;
	public String item_count;
	
	public MyConsumeListItem (){
		layoutId = R.layout.my_consume_list_item;
	}
	
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		Child.tv_num = (TextView) view.findViewById(R.id.myconsume_numstr);
		
		Child.tv_shopname = (TextView) view.findViewById(R.id.myconsume_shop_name);
		Child.tv_time = (TextView) view.findViewById(R.id.myconsume_time);
		Child.tv_totalcost = (TextView) view.findViewById(R.id.myconsume_total);
		Child.tv_earnedscore = (TextView) view.findViewById(R.id.myconsume_goal);
		
		//总金额
		Child.tv_totalcost.setText(totalcostStr);
		//时间
		Child.tv_time.setText(timeStr);
		//消费数量
//		Child.tv_item_count.setText(item_count);
		//销售单号
		Child.sale_num = sale_bianhaoStr;
	}
	
	class ViewChild {
		public String id;
		public String item_count;
		public String sale_num;
		public TextView tv_num;
		public TextView tv_shopname;
		public TextView tv_time;
		public TextView tv_item_count;
		public TextView tv_totalcost;
		public TextView tv_earnedscore;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(timeStr);
		dest.writeString(item_count);
		dest.writeString(totalcostStr);
		dest.writeString(sale_bianhaoStr);
	}
	
	public static final Parcelable.Creator<MyConsumeListItem> CREATOR = new Parcelable.Creator<MyConsumeListItem>() {

		@Override
		public MyConsumeListItem createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			MyConsumeListItem item = new MyConsumeListItem();
			item.timeStr = source.readString();
			item.item_count = source.readString();
			item.totalcostStr = source.readString();
			item.sale_bianhaoStr = source.readString();
			return item;
		}

		@Override
		public MyConsumeListItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MyConsumeListItem[size];
		}
	};

}
