package com.btten.calltaxi.CallTaxi.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.btten.Jms.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard.Key;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListMenuPop {
	Context context;
	LayoutInflater inflater;
	RelativeLayout popView;
	// LinearLayout popCenter;
	TextView title_tv;
	ListView content_lv;

	PopupWindow popup;

	String[] data;
	SimpleAdapter adapter;

	public ListMenuPop(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		popView = (RelativeLayout) inflater.inflate(R.layout.poplistmenu, null);
		// popCenter = (LinearLayout)
		// popView.findViewById(R.id.popListMenu_center_layout);
		title_tv = (TextView) popView.findViewById(R.id.popListMenu_Title);
		content_lv = (ListView) popView.findViewById(R.id.popListMenu_List);

		popup = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popup.setOutsideTouchable(true);
		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP)
					if (popup.isShowing())
						popup.dismiss();
				return true;
			}
		});
		
		content_lv.setOnKeyListener(keyListener);
	}
	
	OnKeyListener keyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == KeyEvent.ACTION_DOWN)
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
					if (popup.isShowing()){
						popup.dismiss();
						return true;
					}
			return false;
		}
	};

	public void setTitle(String title) {
		title_tv.setText(title);
	}

	public void setData(String[] data, final OnItemClickListener listener) {
		this.data = data;
		adapter = new SimpleAdapter(context, getData(), R.layout.poplistitem,
				new String[] { "content" }, new int[] { R.id.popListMenu_item });
		content_lv.setAdapter(adapter);
		content_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				listener.onItemClick(parent, view, position, id);
				popup.dismiss();
			}
		});
	}

	private ArrayList<HashMap<String, String>> getData() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		int lenth = data.length;
		for (int i = 0; i < lenth; ++i) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("content", data[i]);
			list.add(tempMap);
		}

		return list;
	}

	public PopupWindow getPopupWindow() {
		return popup;
	}

}
