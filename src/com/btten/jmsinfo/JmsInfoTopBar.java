package com.btten.jmsinfo;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.goal.consume.MyConsumeView;
import com.btten.goal.pingjia.MyPingjia;
import com.btten.uikit.IViewFactory;
import com.btten.uikit.ViewContainer;

public class JmsInfoTopBar implements IViewFactory {

	MainActivity context;
	View rootView = null;
	// view的个数
	int viewNum = 2;
	int curFocus = -1;
	int lastFocus = -1;
	int screenWidth;
	public ViewContainer flipper;
	TranslateAnimation sepAnim;
	DisplayMetrics metric;
	TextView[] texts = new TextView[viewNum];
	ImageView[] images = new ImageView[viewNum];
	ImageView goalSeparator;

	 /* int[] texts_id=new int[]{R.id.goal_text_basicinfo,//基本资料
	  R.id.goal_text_mygoal, //我的消费 
	  R.id.goal_text_myconsume, //我的结算
	  R.id.goal_text_mybalance};*/
	 
	int[] texts_id = new int[] {
			// 评价
			R.id.tvId_jmsinfo_pingjia,
			// 交易
			R.id.tvId_jmsinfo_jiaoyi };

	public JmsInfoTopBar(MainActivity context, View rootView) {
		this.context = context;
		this.rootView = rootView;
		initTab();
	}

	public void initTab() {
		for (int i = 0; i < viewNum; i++) {
			texts[i] = (TextView) rootView.findViewById(texts_id[i]);
			texts[i].setOnClickListener(listener);
		}
		
		metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		initFlip();
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			for (int i = 0; i < viewNum; i++) {
				if (v.getId() == texts_id[i]) {
					switchTab(i);
				}
			}
		}
	};

	public void initFlip() {
		flipper = (ViewContainer) rootView
				.findViewById((R.id.ivId_jmsinfo_flipper));
		flipper.SetViewFactory(this);
		goalSeparator = (ImageView) rootView
				.findViewById(R.id.ivId_jmsinfo_separator);
	}

	public void switchTab(int index) {
		if (index == curFocus)
			return;
		lastFocus = curFocus;
		curFocus = index;
		setTab(index);
		flipper.FlipToView(index);
	}

	public void setTab(int index) {
		// 之前的背景色和字体颜色还原
		if (lastFocus >= 0) {
			// texts[lastFocus].setBackgroundResource(R.color.white);
			texts[lastFocus].setTextColor(Color.rgb(0xb2, 0xb2, 0xb2));
			// 设置背景色
			texts[lastFocus].setBackgroundResource(R.color.white);

		}
		// 设置新的背景色和字体颜色
		// texts[index].setBackgroundResource(R.color.title_selected_gray);
		texts[index].setTextColor(Color.WHITE);
		// 设置背景色
		texts[index].setBackgroundResource(R.drawable.title_table_bg);
		switchSeparator(lastFocus, curFocus);
	}

	public void switchSeparator(int oldIndex, int curIndex) {
		sepAnim = new TranslateAnimation(Animation.ABSOLUTE, getX(oldIndex),
				Animation.ABSOLUTE, getX(curIndex), Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);

		sepAnim.setDuration(200 * ((curFocus > lastFocus) ? (curFocus - lastFocus)
				: (lastFocus - curFocus)));
		sepAnim.setFillAfter(true);

		goalSeparator.startAnimation(sepAnim);
	}

	public float getX(int index) {
		if (index < 0)
			return 0;
		float itemWidth = screenWidth / viewNum;
		return itemWidth * index;
	}

	@Override
	public View CreateView(int index) {
		switch (index) {
		case 0:
			// 评价
			MyPingjia myPingjia = new MyPingjia(context);
			return myPingjia.GetView();
		case 1:
			// 交易记录
			MyConsumeView myConsume = new MyConsumeView(context);
			return myConsume.GetView();
			/*
			 * case 0: //显示我的资料的信息 BasicInfoView basicInfo=new
			 * BasicInfoView(context); return basicInfo.GetView(); 
			 * case 1:
			 * //显示我的积分的信息 MyGoal myGoal=new MyGoal(context); return
			 * myGoal.GetView(); 
			 * case 2: //显示我的消费的信息 MyConsumeView myConsume=new
			 * MyConsumeView(context); return myConsume.GetView(); 
			 * case 3:
			 * //显示我的结算的信息 MyBalance myBalance=new MyBalance(context); return
			 * myBalance.GetView();
			 */
		}
		return null;
	}
}
