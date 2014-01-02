package com.btten.costitem;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;
import com.nostra13.universalimageloader.core.ImageLoader;
//creat-gwj-20130902
public class Costitem_ListItem extends ListItemBase {
	static private final int DESIGNATE = 2;
	static private final int PATERNITY = 3;
	static private final int BOTH = 6;
	
	public int type = -1;
//	public String num_str = null;
	public String name_str = null;
//	public String oldpr_str = null;
	public String vippr_str = null;
	public String itemid_str = null;
	public String itemupid_str = null;
	//位置
	public String num = null;
	public int average_rate = -1;

	private ViewHolder mHolder;
	//链表
	public ArrayList<String> list = new ArrayList<String>();
	// 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;
    
	public Costitem_ListItem() {
		// TODO Auto-generated constructor stub
		this.layoutId = R.layout.costitem_childlist_item;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		if (view.getTag() instanceof ViewHolder)
			mHolder = (ViewHolder) view.getTag();
		else {
			mHolder = new ViewHolder();
//			mHolder.num_tv = (TextView) view
//					.findViewById(R.id.tvId_costitem_item_num);
			mHolder.name_tv = (TextView) view
					.findViewById(R.id.tvId_costitem_childitem_name);
//			mHolder.olcprice_tv = (TextView) view
//					.findViewById(R.id.tvId_costitem_item_oldprice);
			mHolder.vipprice_tv = (TextView) view
					.findViewById(R.id.tvId_costitem_childitem_vipprice);
			mHolder.check_cb = (CheckBox) view
					.findViewById(R.id.cbId_costitem_childitem_check);
			mHolder.root_ll = (LinearLayout) view.findViewById(R.id.llId_costitem_item_ll);
			// 为view设置标签
			view.setTag(mHolder);
		}

//		mHolder.num_tv.setText(num_str);
		mHolder.name_tv.setText(name_str);
		mHolder.itemId = itemid_str;
//		mHolder.oldprice_tv.setText(oldpr_str);
		mHolder.vipprice_tv.setText(vippr_str);
		
//		mHolder.root_ll.setOnClickListener(new View.OnClickListener() {
//
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// 改变CheckBox的状态
//				 // 根据isSelected来设置checkbox的选中状况  
//				System.out.println("点击!");
//			}
//		
//		});
		
	}
	public ViewHolder getHolder() {
        return mHolder;
    }
	
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer,Boolean> Selected) {
        isSelected = Selected;
    }
    
	class ViewHolder {
//		public TextView num_tv;
		public TextView name_tv;
//		public TextView oldprice_tv;
		public TextView vipprice_tv;
		public CheckBox check_cb;
		
		public LinearLayout root_ll;
		public String itemId;
	}
}
