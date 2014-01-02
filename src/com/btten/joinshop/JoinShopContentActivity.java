package com.btten.joinshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.ListMoreListener;
import com.btten.Jms.LoadFooterBar;
import com.btten.Jms.R;
import com.btten.joinshop.logic.GetJoinshopInfoScene;
import com.btten.joinshop.logic.GetShopCommentScene;
import com.btten.joinshop.logic.JoinshopInfoItems;
import com.btten.joinshop.logic.ShopRemarkAdapter;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JoinShopContentActivity extends BaseActivity implements OnSceneCallBack,
		ListMoreListener {

	ListView commentList;
	TextView name, address, descrition, phone;
	ImageView shopimg;
	RatingBar star;
	Button collectBtn;
	ImageButton callBtn;
	ShopRemarkAdapter adapter;
	LoadFooterBar footloader;
	int position;
	String id;
	String fid;
	
	ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joinshop_listview);
		setCurrentTitle("加盟商详情");
		setBackKeyListner(backListener);
		setRefreshKeyListner(refreshListener);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		scrollView = (ScrollView) findViewById(R.id.joindetail_scroll_layout);
		name = (TextView) findViewById(R.id.shop_detail_name);
		address = (TextView) findViewById(R.id.shop_detail_address);
		descrition = (TextView) findViewById(
				R.id.shop_detail_description);
		phone = (TextView) findViewById(R.id.shop_detail_phone);
		shopimg = (ImageView) findViewById(R.id.shop_detail_picture);
		star = (RatingBar) findViewById(R.id.shop_detail_ratingbar);
		collectBtn = (Button) findViewById(
				R.id.shop_detail_collect_btn);
		callBtn = (ImageButton) findViewById(R.id.shop_detail_callbtn);

		footloader = new LoadFooterBar(this, this);
		// 评论列表
		commentList = (ListView)findViewById(
				R.id.shop_detail_commentlist);
		commentList.addFooterView(footloader.GetView());
		adapter = new ShopRemarkAdapter(this);
		
		collectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fid!=null && !fid.equals("")){
					addtofavScene scene = new addtofavScene();
					scene.doscene(collectionCallBack, "deleteFav", fid, 1);
				}
				else
				{
					if (!id.equals("")){
						addtofavScene scene = new addtofavScene();
						scene.doscene(collectionCallBack, "addFav", id, 1);
					}
					else
						Alert("没有加盟商信息！");
				}
			}
		});
		
		ShowProgress("读取数据中", "请稍候……");
		this.id = getIntent().getStringExtra("id");
		GetJoinshopInfoScene scene = new GetJoinshopInfoScene();
		scene.doScene(this, id);
		footloader.ShowLoading();
		getComments();
	}

	GetShopCommentScene comScene;
	int curPage = 1;

	private void getComments() {
		if (comScene != null)
			return;
		comScene = new GetShopCommentScene();
		comScene.doScene(id, curPage, oncallBack);
	}
	
	OnSceneCallBack oncallBack = new OnSceneCallBack() {

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			comScene = null;
			comItems = (ShopCommentItems) data;
			footloader.ShowNoMore();
			if (comItems != null && comItems.items != null
					&& comItems.items.size() == Util.PAGE_LOAD_MIN_NUM)
				footloader.ShowMore();
			if (curPage > 1)
				adapter.addItem(comItems);
			else
				adapter.setItem(comItems);
			
			commentList.setAdapter(adapter);
			setLvHeight();
			
			++curPage;
		}
	};
	
	OnSceneCallBack collectionCallBack = new OnSceneCallBack() {
		
		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			addtofavItems items = (addtofavItems) data;
			
			if (items.fid!=null && !items.fid.equals("")){
				fid = items.fid;
				collectBtn.setText("取消收藏");
				Alert("收藏成功！");
			}
			else{
				fid = null;
				collectBtn.setText("收藏");
				Alert("取消收藏成功！");
				String parent = getIntent().getStringExtra("parent");
				if (parent != null && parent.equals("COLLECTION")){
					Intent intent= new Intent();
					intent.putExtra("cancel", true);
					setResult(10001, intent);
				}
			}
		}
		
		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			ErrorAlert(status, info);
		}
	};

	// 自动调整listview的高度，解决listview在scrollview中只显示一行的问题
	private void setLvHeight() {
		ListAdapter adapter = commentList.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View itemView = adapter.getView(i, null, commentList);
			itemView.measure(0, 0);
			totalHeight += itemView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams layoutParams = commentList.getLayoutParams();
		layoutParams.height = totalHeight
				+ (commentList.getDividerHeight() * (adapter.getCount()+1))+footloader.GetView().getMeasuredHeight();// 总行高+每行的间距
		commentList.setLayoutParams(layoutParams);
	}

	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		HideProgress();
		comScene = null;
		ErrorAlert(status, info);
	}

	ShopCommentItems comItems;

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		HideProgress();

		JoinshopInfoItems item = (JoinshopInfoItems) data;

		ImageLoader imageLoader = ImageLoader.getInstance();
		if (!item.photo.equals("")) {
			imageLoader.displayImage(item.photo, shopimg);
		}
		
		this.fid = item.fid;
		
		if (!this.fid.equals("") && this.fid != null)
			collectBtn.setText("取消收藏");
		else
			collectBtn.setText("收藏");
		
		
		name.setText(item.name);
		phone.setText(item.telephone);
		address.setText(item.address);
		descrition.setText(Html.fromHtml(item.des).toString());
		if (!item.score.equals("")) {
			star.setRating(Integer.parseInt(item.score));
		}
		
		callBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog dlg = new AlertDialog.Builder(JoinShopContentActivity.this)
									.setTitle("提示")
									.setMessage("拨打电话给 "+name.getText().toString()+" ？")
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.getText().toString()));
											startActivity(intent);
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
	}

	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		footloader.ShowLoading();
		getComments();
	}

	OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
	OnClickListener refreshListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ShowProgress("读取数据中", "请稍候……");
			curPage = 1;
			GetJoinshopInfoScene scene = new GetJoinshopInfoScene();
			scene.doScene(JoinShopContentActivity.this, id);
			footloader.ShowLoading();
			getComments();
		}
	};

}
