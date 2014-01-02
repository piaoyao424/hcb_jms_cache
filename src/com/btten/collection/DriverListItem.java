package com.btten.collection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btten.Jms.ListItemBase;
import com.btten.Jms.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DriverListItem extends ListItemBase{

	
	public DriverListItem (){
		layoutId = R.layout.mycollection_driver_list_item;
	}

 
	public String name;
	public String carnum;
	public String telephone;
	public String address;
	public String photo;
	
	
	ImageView mycollection_driver_picture;
	TextView mycollection_driver_name;
	TextView mycolleticon_service_state;
	TextView mycollection_driver_license;
	TextView mycollection_driver_region;
	View mycollection_linear_call;
	
	@Override
	public void initView(View view) {

		mycollection_driver_name = (TextView) view.findViewById(R.id.mycollection_driver_name);
		mycolleticon_service_state = (TextView) view.findViewById(R.id.mycolleticon_service_state);
		mycollection_driver_license = (TextView) view.findViewById(R.id.mycollection_driver_license);
		mycollection_driver_region = (TextView) view.findViewById(R.id.mycollection_driver_region);
		mycollection_linear_call = (View) view.findViewById(R.id.mycollection_linear_call);
		mycollection_driver_picture = (ImageView) view.findViewById(R.id.mycollection_driver_picture);
		
		if (photo != null && !photo.equals(""))
			ImageLoader.getInstance().displayImage(photo, mycollection_driver_picture);
		else
			mycollection_driver_picture.setImageResource(R.drawable.head_default);
		mycollection_linear_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Context context = v.getContext();
				AlertDialog dlg = new AlertDialog.Builder(context)
									.setTitle("提示")
									.setMessage("拨打电话给 "+name+" ？")
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ telephone));
											context.startActivity(intent);
										}
									})
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
									.show();
			}
		});
		 
		mycollection_driver_name.setText(name);
		mycollection_driver_license.setText(carnum);
	}
	
 

}
