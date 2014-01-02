package com.btten.about;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;

public class ContactUsActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_contact_us);
		setBackKeyListner(backListener);
		setCurrentTitle("联系我们");
		init();
	}
	
	Button contact;
	private void init(){
		contact = (Button) findViewById(R.id.contact_us_btn);
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ContactUsActivity.this)
				.setTitle("提示")
				.setMessage("拨打热线 0717-6300407 ？")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:0717-6300407")));
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						})
				.show();
			}
		});
	}
	
	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
}
