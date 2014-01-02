package com.btten.goal.consume;

import com.btten.Jms.R;
import com.btten.network.NetConst;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyConsumeCommentPop implements OnSceneCallBack{
	Activity context;
	private PopupWindow commentPopWin = null;
	RelativeLayout commentLayout = null;
	MyConsumeListItem item = null;
	
	RatingBar ratingBar = null;
	ImageView camera_iv = null;
	Button submit_bn = null;
	Button cancel_bn = null;
	TextView content_ev = null;
	Gallery imageGallery = null;
	
	int ratingscore = 0;
	String commentStr = null;
	
	ProgressDialog progress = null;
	
	public MyConsumeCommentPop(Activity activity){
		this.context = activity;
		
		commentLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.myconsume_comment, null);
		submit_bn = (Button) commentLayout.findViewById(R.id.myconsume_comment_sure);
		cancel_bn = (Button) (Button) commentLayout.findViewById(R.id.myconsume_comment_abort);
		ratingBar = (RatingBar) commentLayout.findViewById(R.id.myconsume_comment_rating);
		content_ev = (TextView) commentLayout.findViewById(R.id.myconsume_comment_edit);
		
		ratingBar.setRating(5);
		
		commentPopWin = new PopupWindow(commentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		commentPopWin.setAnimationStyle(android.R.style.Animation_Dialog);
		commentPopWin.setBackgroundDrawable(new BitmapDrawable());
		//camera_iv.setOnClickListener(imageListener);
		cancel_bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (commentPopWin != null && commentPopWin.isShowing())
				{
					ratingBar.setRating(5);
					content_ev.setText("");
					commentPopWin.dismiss();
				}
			}
		});
		
		submit_bn.setOnClickListener(new View.OnClickListener() {
			//上传评价内容 --gwj 20130813
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ratingscore = (int) ratingBar.getRating();
				commentStr = content_ev.getText().toString().trim();
				if (!commentStr.equals(""))
				{
					if (progress == null)
						progress = ProgressDialog.show(context, "上传数据中", "请稍候……", true, true);
//					(new MyConsumeCommentScene()).doscene(MyConsumeCommentPop.this, item.idStr, commentStr, Integer.toString(ratingscore)); 
				}
				else
				{
					Alert("评论内容不能为空！");
				}
			}
		});
		
		commentPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
				if (imm.isActive())
					imm.hideSoftInputFromWindow(content_ev.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}
	
	public PopupWindow getPopupWindow(MyConsumeListItem item){
		this.item = item;
		if (commentPopWin != null)
			return commentPopWin;
		else
			return null;
	}
	
	public void Alert(String info) {
		if (info == null)
			info = "";

		AlertDialog dlg = new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage(info)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}

	public void ErrorAlert(int status, String info) {
		if (status == NetConst.NetConnectError) {
			Alert("网络不给力，请先检查网络再试!");

		} else if (status == NetConst.JsonParaseError) {
			Alert("数据解析出错，请稍后再试!");
		} else {
			if (info != null && info.length() > 0) {
				// 服务器返回的错误信息
				Alert(info);
			} else {
				Alert("遇到错误，请稍后再试!");
			}
		}
	}

	
	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		if (progress != null) {
			progress.dismiss();
			progress = null;
		}
		ErrorAlert(status, info);
		if (commentPopWin != null && commentPopWin.isShowing())
		{
			ratingBar.setRating(0);
			content_ev.setText("");
			commentPopWin.dismiss();
		}
	}

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		if (progress != null) {
			progress.dismiss();
			progress = null;
		}
		
		Alert("评论发表成功！");
		
		if (commentPopWin != null && commentPopWin.isShowing())
		{
			ratingBar.setRating(0);
			content_ev.setText("");
			commentPopWin.dismiss();
		}
	}
	
}
