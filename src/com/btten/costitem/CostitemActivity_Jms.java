package com.btten.costitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.ListMoreListener;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.costitem.Costitem_showGroupAdapter.ExpandableListHolder;
import com.btten.model.BaseJsonItem;
import com.btten.network.NetSceneBase;
import com.btten.network.NomalJsonSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.ThreadPoolUtils;
import com.btten.network.UrlFactory;
import com.btten.tools.CommonConvert;
import com.btten.tools.Log;
import com.btten.ui.view.BaseView;

//creat-gwj-20130902
public class CostitemActivity_Jms extends BaseView implements
		ExpandableListView.OnGroupClickListener,
		ExpandableListView.OnChildClickListener, ListMoreListener,
		OnClickListener {
	ListView mCostitemList;
	private List<Costitem_showGroup> groups;

	private Costitem_showGroupAdapter exlist_adapter = null;

	private ExpandableListView el_costitem_list;

	private Button btn_costitem_itemok = null;
	protected TextView tv_costitem_tongji = null;
	// CostitemListAdapter mAdapter;
	ItemListScene mScene = null;
	// 多选
	private ArrayList<HashMap<String, Object>> selectList = new ArrayList<HashMap<String, Object>>();

	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	// 将二级条目放在一个集合里，供显示时使用
	List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();
	private String iteminfo_bundle_str = "";
	private String dafengefu = "|";
	private String xiaofengefu = "&";
	String fengefu = "%";
	private int itemsLenth = 0;
	private int ddlist_counts = 0;
	private int tongji_counts = 0;
	// 保存结果
	Costitem_ListItem[] itemsResult = null;
	SharedPreferences setting = null;

	private MyUIHandler uiHandler;
	private final static int UI_SHOW = 1;
	MainActivity root;

	public CostitemActivity_Jms(MainActivity root) {
		super(root);
		this.root = root;
		init();
	}

	private void init() {
		btn_costitem_itemok = (Button) GetView().findViewById(
				R.id.btnId_costitem_itemok);
		btn_costitem_itemok.setOnClickListener(this);
		tv_costitem_tongji = (TextView) GetView().findViewById(
				R.id.tvId_costitem_tongji);
		el_costitem_list = (ExpandableListView) GetView().findViewById(
				R.id.elId_costitem_list);
		initTitle();
		DoRequest();
	}

	private ImageButton refreshKeyImageButton = null;
	protected TextView titleTextView = null;

	private void initTitle() {
		titleTextView = (TextView) GetView().findViewById(
				R.id.tvId_costitem_title);
		refreshKeyImageButton = (ImageButton) GetView().findViewById(
				R.id.ibId_costitem_title_refresh);
		titleTextView.setText("项目消费");
		refreshKeyImageButton.setOnClickListener(listener);

	}

	OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			DoRequest();
		}
	};

	private void DoRequest() {
		root.ShowRunning();
		mScene = new ItemListScene();
		mScene.SetCallBack(callBack);
		mScene.doScene();
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			CostItem_ListItems item = (CostItem_ListItems) data;

			itemsLenth = item.items.length;
			// 设置所有的item都是没有被选中的。
			if (itemsLenth == 0) {
				// mLoadMore.ShowNoMore();
				root.Alert("该加盟商没有物品可以经营!");
				return;
			} else {
				// 把数据显示到listview
				SetResultToList(item.items);
				exlist_adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			// mLoadMore.ShowNoMore();
			root.HideProgress();
			root.ErrorAlert(status, info);
		}
	};

	class ItemListScene extends NomalJsonSceneBase {
		// 请求服务器数据
		public void doScene() {
			// TODO Auto-generated method stub
			targetUrl = UrlFactory.GetUrlNew("Sale", "getItems", "jid",
					JmsAccountManager.getInstance().getJmsUserid());
			ThreadPoolUtils.execute(this);
			Log.i("gwjTAG", targetUrl);
		}

		@Override
		protected BaseJsonItem CreateJsonItems() {
			// TODO Auto-generated method stub
			return new CostItem_ListItems();
		}
	}

	class CostItem_ListItems extends BaseJsonItem {
		private final String TAG = "CostItem_ListItems";
		public Costitem_ListItem[] items = null;

		@Override
		public boolean CreateFromJson(JSONObject result) {
			// TODO Auto-generated method stub
			try {
				this.status = result.getInt("STATUS");
				this.info = result.getString("INFO");

				if (status == 1 && !result.isNull("DATA")) {
					JSONArray jsonArray = result.getJSONArray("DATA");
					int lenth = jsonArray.length();
					items = new Costitem_ListItem[lenth];

					for (int i = 0; i < lenth; ++i) {
						Costitem_ListItem temp = new Costitem_ListItem();
						CommonConvert convert = new CommonConvert(
								jsonArray.getJSONObject(i));

						// temp.num_str = Integer.toString(i);
						temp.itemid_str = convert.getString("ITEMID");
						temp.itemupid_str = convert.getString("UPID");
						temp.name_str = convert.getString("NAME");
						// temp.oldpr_str = convert.getString("OLDPRICE");
						temp.vippr_str = convert.getString("PRICE");
						items[i] = temp;

					}
					return true;
				} else
					return false;
			} catch (JSONException e) {
				this.status = -1;
				this.info = e.toString();
				BtAPPJMS.getInstance();
				BtAPPJMS.ReportError(TAG + "error:\n" + e.toString()
						+ "\nresult:\n" + result.toString());
				Log.Exception(TAG, e);
				return false;
			}
		}
	}

	@Override
	public void ClickMore() {
		// TODO Auto-generated method stub
		// DoRequest(mPage);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Costitem_showGroupAdapter.ExpandableListHolder holder = (ExpandableListHolder) v
				.getTag();
		holder.chkChecked.setChecked(!holder.chkChecked.isChecked());
		groups.get(groupPosition).children.get(childPosition).checked = !groups
				.get(groupPosition).children.get(childPosition).checked;
		holder.tvName.setSelected(true);
		if (holder.chkChecked.isChecked()) {
			tongji_counts++;
		} else {
			tongji_counts--;
		}
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return false;
	}

	private void SetResultToList(Costitem_ListItem[] itemResult) {
		groups = new ArrayList<Costitem_showGroup>();
		for (int i = 0; i < itemResult.length; i++) {
			if (itemResult[i].itemupid_str.equals("0")) {
				String Name = itemResult[i].name_str;
				String ID = itemResult[i].itemid_str;
				List<Costitem_showListItem> group_children = new ArrayList<Costitem_showListItem>();
				for (int j = 0; j < itemResult.length; j++) {
					if (itemResult[j].itemupid_str.equals(ID)) {
						Costitem_showListItem item = new Costitem_showListItem(
								itemResult[j].name_str,
								itemResult[j].vippr_str,
								itemResult[j].itemid_str, false);
						group_children.add(item);
					}
				}
				Costitem_showGroup phonegroup = new Costitem_showGroup(Name,
						false, group_children);
				groups.add(phonegroup);
			}
		}

		exlist_adapter = new Costitem_showGroupAdapter(context, groups);
		// el_costitem_list.setOnChildClickListener(this);
		el_costitem_list.setAdapter(exlist_adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 向服务器请求积分。
		case R.id.btnId_costitem_itemok:
			// 如果没有经营范围
			if (itemsLenth == 0) {
				root.Alert("没有经营范围,无法交易！");
				return;
			}
			totalMoney();
			// 如果没有选中
			if (ddlist_counts == 0) {
				root.Alert("请选择至少一个消费项目后再结算！");
				return;
			}

			// 跳转到付款画面
			Intent intent = new Intent();
			intent.setClass(context, UserPayCheckActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("KEY_DINGDANCOUNT", ddlist_counts);
			bundle.putString("KEY_DINGDANSTR", iteminfo_bundle_str);
			System.out.println("costitem -> btnListener->ddlist_counts = "
					+ ddlist_counts);
			// System.out.println("ddlist.toString() = "+ddlist.toString());
			intent.putExtras(bundle);
			root.startActivity(intent);
			break;
		default:
			break;
		}
	}

	// 找到list中勾选了的item 计算价格
	private void totalMoney() {
		int temp_money = 0;
		int temp_counts = 0;
		String totalItemIds_str = "";
		String total_str = "";

		for (int i = 0; i < groups.size(); i++) {
			for (int j = 0; j < groups.get(i).children.size(); j++) {
				if (groups.get(i).children.get(j).checked) {
					// 消费金额
					temp_money += Integer.parseInt(groups.get(i).children
							.get(j).money);
					// 物品id
					totalItemIds_str += groups.get(i).children.get(j).id
							+ fengefu;
					// 物品详细信息
					total_str += groups.get(i).children.get(j).id + xiaofengefu
							+ groups.get(i).children.get(j).name + xiaofengefu
							+ groups.get(i).children.get(j).money + dafengefu;
					temp_counts++;
				}
			}
		}
		iteminfo_bundle_str = total_str;
		ddlist_counts = temp_counts;
		System.out.println("costitem->totalMoney()->ddlist_counts = "
				+ ddlist_counts);
		System.out.println("costitem->totalMoney()->iteminfo_bundle_str = "
				+ iteminfo_bundle_str);
		System.out.println("costitem->totalMoney()->totalItemIds_str = "
				+ totalItemIds_str);
		// myitemserializable = new itemSerializable();
		// myitemserializable.setCheck_item(ddlist);

		setting = root.getSharedPreferences("resultprice", root.MODE_PRIVATE);
		setting.edit().putInt("total_money", temp_money)
				.putString("total_itemids", totalItemIds_str)
				.putInt("total_costcounts", ddlist_counts).commit();
		// System.out.println("costitem->totalMoney()->计算出总的金额，放在共享的SharedPreferences中!");
		System.out.println("costitem->totalMoney()->总的金额为： " + temp_money);
	}

	@Override
	public int GetLayoutId() {
		return R.layout.costitem_activity;
	}

	@Override
	public void OnViewShow() {
		root.tabBar.setTab(1);
	}

	@Override
	public void OnViewHide() {

	}

	@Override
	public void Backward() {
		super.Backward();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		root.startActivity(intent);
	}

	// 实时刷新text的内容
	// 主界面刷新
	class MyUIHandler extends Handler {
		public MyUIHandler() {
		}

		public MyUIHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case UI_SHOW:
				// System.out.println("");
				tv_costitem_tongji.setText("已选择 " + tongji_counts + " 个消费项目");
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	// 线程
	class ShowTextThread extends Thread {
		public ShowTextThread() {
			super();
		}

		public void run() {
			try {
				Message msg = uiHandler.obtainMessage(UI_SHOW, 0, 0, 0);
				uiHandler.sendMessage(msg);
			} catch (Exception e) {
				Log.e("gwjTag",
						"Error in netBookThread -> run() " + e.toString());
			}

		}
	}

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		DoRequest();

	}
}
