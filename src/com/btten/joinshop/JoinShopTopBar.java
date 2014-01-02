package com.btten.joinshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.btten.Jms.R;
import com.btten.network.NetConst;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.ui.view.BaseView;
import com.btten.uikit.IViewFactory;
import com.btten.uikit.ViewContainer;

public class JoinShopTopBar implements IViewFactory{
	BaseView curView;
 
	int viewNum = 3;
	DisplayMetrics metric = new DisplayMetrics();
	TranslateAnimation sepAnim;
	ImageView sep;
	int lastIndex = -1;
	int curIndex = -1;
	TextView[] texts = new TextView[viewNum];
	int screenWidth;
	int[] texts_id = new int[] { R.id.joinshop_text_recommand,//推荐
			R.id.joinshop_text_all//全部
			, R.id.joinshop_text_new };//最新

	JoinShopActivity root;
	
	public JoinShopTopBar(Activity context) {
		root = (JoinShopActivity) context;
		initTopBar();
		switchTab(0);
	}
	ViewContainer	join_shop_content;
	public void initTopBar(){
		 
		for (int i = 0; i < viewNum; i++) {
			texts[i] = (TextView) root.findViewById(texts_id[i]);
			texts[i].setOnClickListener(listener);
		}
		sep = (ImageView) root.findViewById(R.id.joinshop_separator);
		root.getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		join_shop_content=(ViewContainer)root.findViewById(R.id.join_shop_content);
	 
		join_shop_content.SetViewFactory(this);
		
		switchTab(0);
	}
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			for (int i = 0; i < viewNum; i++)
				if (texts_id[i] == v.getId()) {
					switchTab(i);
				}
		}
	};
	 

	public void switchTab(int index) {
		if (curIndex == index) return;
		
		lastIndex = curIndex;
		curIndex = index;
		setTab(index);
		join_shop_content.FlipToView(index);
		
	}

	public float getX(int index) {
		if (index < 0) {
			return 0;
		}
		float itemWidth = screenWidth / viewNum;
		return itemWidth * index;
	}

	public void setTab(int index) {
		//标签页颜色变化
		if (lastIndex >= 0)
		{
			texts[lastIndex].setBackgroundResource(R.color.white);
			texts[lastIndex].setTextColor(Color.rgb(0xb2, 0xb2, 0xb2));
		}
		texts[curIndex].setTextColor(Color.WHITE);
		texts[curIndex].setBackgroundResource(R.drawable.title_table_bg);
		
		sepAnim = new TranslateAnimation(Animation.ABSOLUTE, getX(lastIndex),
				Animation.ABSOLUTE, getX(curIndex), Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		if (lastIndex < 0) {
			sepAnim.setDuration(0);
		} else {
			sepAnim.setDuration(250);
		}
		sepAnim.setFillAfter(true);
		sep.startAnimation(sepAnim);
	}

	@Override
	public View CreateView(int index) {
 
		return (new JoinView(root,index+1)).GetView();
		 
	}
	
}
