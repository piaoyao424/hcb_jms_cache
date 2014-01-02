package com.btten.sendGift;

import com.btten.Jms.ListItemAdapter;
import com.btten.Jms.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class GetGiftItemAdapter extends ListItemAdapter {
	private Activity context = null;
	private Handler handler = null;

	public GetGiftItemAdapter(Activity context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("position = " + position,
				"parent.getchildposition" + parent.getChildCount());
		if (convertView == null) {
			convertView = inflater.inflate(items[position].getItemLayout(),
					null);
		}
		items[position].initView(convertView);
		convertView.setOnClickListener(new adapterListner(position));
		return convertView;
	}

	class adapterListner implements OnClickListener {
		int index;

		public adapterListner(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			LayoutInflater factory = LayoutInflater.from(context);
			final View textEntryView = factory.inflate(
					R.layout.sendgift_dialog, null);
			builder.setIcon(R.drawable.app_icon_old);
			builder.setTitle("验证会员");
			builder.setView(textEntryView);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							EditText userName = (EditText) textEntryView
									.findViewById(R.id.etUserName);
							EditText password = (EditText) textEntryView
									.findViewById(R.id.etPassWord);
							Message message = new Message();
							message.what = 0;
							message.obj = new SendGiftMsgObject(
									((GetGiftResultItem) items[index]).gid,
									((GetGiftResultItem) items[index]).gname,
									((GetGiftResultItem) items[index]).gnumber,
									userName.getText().toString(), password
											.getText().toString());
							handler.sendMessage(message);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			builder.create().show();
		}
	}
}
