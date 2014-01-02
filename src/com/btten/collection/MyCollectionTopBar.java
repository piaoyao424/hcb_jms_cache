package com.btten.collection;

import com.btten.Jms.R;
import com.btten.uikit.IViewFactory;
import com.btten.uikit.ViewContainer;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyCollectionTopBar implements IViewFactory {
	MyCollectionActivity context;
 
	int viewNum=2;
	DisplayMetrics metric=new DisplayMetrics();
	TranslateAnimation sepAnim;
	ImageView sep;
	int lastIndex=-1;
	int curIndex=-1;
	TextView[] texts=new TextView[viewNum];
	int screenWidth;
	int[] texts_id=new int[]{R.id.mycollection_text_join,R.id.mycollection_text_driver};
	public MyCollectionTopBar(Activity context) {
		this.context=(MyCollectionActivity)context;
		initTopBar();
		switchTab(0);
	}
	ViewContainer collection_container;
	public void initTopBar(){
		 
		for(int i=0;i<viewNum;i++){
			texts[i]=(TextView)context.findViewById(texts_id[i]);
			texts[i].setOnClickListener(listener);
		}
		sep=(ImageView)context.findViewById(R.id.mycollection_separator);
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth=metric.widthPixels;
        
        collection_container=(ViewContainer)context.findViewById(R.id.collection_container);
        collection_container.SetViewFactory(this);
	}
	OnClickListener listener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			for(int i=0;i<viewNum;i++)
				if(texts_id[i]==v.getId()){
					switchTab(i);
				}
		}
	};
	public void switchTab(int index){
		lastIndex=curIndex;
		curIndex=index;
		setTab(index);
		collection_container.FlipToView(index);
	}
	public float getX(int index){
		if(index<0){
			return 0;
		}
		float itemWidth=screenWidth/viewNum;
		return itemWidth*index;
	}
	public void setTab(int index){
		//标签页颜色变化
		if (lastIndex >= 0)
		{
			texts[lastIndex].setBackgroundResource(R.color.white);
			texts[lastIndex].setTextColor(Color.rgb(0xb2, 0xb2, 0xb2));
		}
		texts[index].setTextColor(Color.WHITE);
		texts[index].setBackgroundResource(R.drawable.title_table_bg);
		
		sepAnim=new TranslateAnimation(
				Animation.ABSOLUTE,getX(lastIndex),
				Animation.ABSOLUTE,getX(curIndex),
				Animation.ABSOLUTE,0f,
				Animation.ABSOLUTE,0f);
		if(lastIndex<0){
			sepAnim.setDuration(0);
		}else{
			sepAnim.setDuration(250);
		}
		sepAnim.setFillAfter(true);
		sep.startAnimation(sepAnim);
	}
	@Override
	public View CreateView(int index) {
		if(index==0)
		{
			//加盟商
			return (new JoinView(context)).GetView();
		}else
		{
			//司机
			return (new DriverView(context)).GetView();
			
		}
	}
}
