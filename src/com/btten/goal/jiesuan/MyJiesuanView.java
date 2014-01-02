package com.btten.goal.jiesuan;

import java.util.Calendar;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.ui.view.BaseView;

public class MyJiesuanView extends BaseView {
	// LoadFooterBar loadMore = null;
	MyJiesuanListScene jiesuanScene = null;
	MainActivity root;
	MyJiesuanListAdapter jsAdapter = null;
	private static final String[] month_toserver = { "01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12", };
	private ListView ll_myjiesuan_list = null;
	private Button btn_jiesuan_chaxun = null;
	private String month_str = null;
	private String year_str = null;
	private ArrayAdapter<CharSequence> monthAdapter = null;
	private ArrayAdapter<String> yearAdapter = null;
	// 下拉框控制
	private Spinner monthSpinner = null;
	private Spinner yearSpinner = null;

	public MyJiesuanView(MainActivity context) {
		super(context);
		root = context;
		init();
	}

	public void init() {
		yearSpinner = (Spinner) GetView().findViewById(R.id.spId_jiesuan_year);
		monthSpinner = (Spinner) GetView()
				.findViewById(R.id.spId_jiesuan_month);

		// 初始化列表页面
		ll_myjiesuan_list = (ListView) GetView().findViewById(
				R.id.llId_myjiesuan_list);
		btn_jiesuan_chaxun = (Button) GetView().findViewById(
				R.id.btnId_jiesuan_chaxun);
		btn_jiesuan_chaxun.setOnClickListener(btn_jiesuan_chaxunOnclick);
		initTitle();
		setSpinner();
	}

	private ImageButton refreshKeyImageButton = null;
	protected TextView titleTextView = null;

	private void initTitle() {
		titleTextView = (TextView) GetView().findViewById(
				R.id.tvId_jiesuan_title);
		refreshKeyImageButton = (ImageButton) GetView().findViewById(
				R.id.ibId_jiesuan_title_refresh);
		titleTextView.setText("消费结算");
		refreshKeyImageButton.setOnClickListener(listener);

	}

	OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doRequest();
		}
	};

	private void doRequest() {
		root.ShowRunning();
		jsAdapter = new MyJiesuanListAdapter(context, this);
		jiesuanScene = new MyJiesuanListScene();
		jiesuanScene.doscene(callBack, month_str, year_str);
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {
		@Override
		public void OnFailed(int status, String info, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			root.ErrorAlert(status, info);
		}

		@Override
		public void OnSuccess(Object data, NetSceneBase<?> netScene) {
			// TODO Auto-generated method stub
			root.HideProgress();
			MyJiesuanListResult items = (MyJiesuanListResult) data;

			if (items.item.length == 0) {
				// mLoadMore.ShowNoMore();
				root.Alert("该月没有结算记录!");
				ll_myjiesuan_list.setAdapter(null);
				return;
			} else {
				jsAdapter.setItems(items.item);
				ll_myjiesuan_list.setAdapter(jsAdapter);
			}
		}
	};

	private void setSpinner() {
		// 初始化年份
		yearAdapter = new ArrayAdapter<String>(root, R.layout.spinnerlayout);
		int year = Calendar.getInstance().get(Calendar.YEAR);

		for (int i = 0; i < 10; i++) {
			yearAdapter.add(String.valueOf(year - i));
		}
		yearAdapter.setDropDownViewResource(R.layout.spinnerlayout);
		yearSpinner.setAdapter(yearAdapter);
		yearSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		year_str = String.valueOf(year);
		yearSpinner.setSelection(0, true);

		// 初始化月份
		// 将可选内容与ArrayAdapter连接起来
		monthAdapter = ArrayAdapter.createFromResource(root, R.array.month,
				R.layout.spinnerlayout);
		// 设置下拉列表的风格
		monthAdapter.setDropDownViewResource(R.layout.spinnerlayout);
		monthSpinner.setAdapter(monthAdapter);
		// 添加事件Spinner事件监听
		monthSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		// 设置默认值
		month_str = month_toserver[Calendar.getInstance().get(Calendar.MONTH) - 1];
		monthSpinner.setSelection(
				(Calendar.getInstance().get(Calendar.MONTH) - 1), true);
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg0.equals(monthSpinner)) {
				month_str = month_toserver[arg2];
				doRequest();
			} else if (arg0.equals(yearSpinner)) {
				year_str = String.valueOf((Calendar.getInstance().get(
						Calendar.YEAR) - arg2));
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public int GetLayoutId() {
		return R.layout.my_jiesuan;
	}

	@Override
	public void OnViewHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnViewShow() {
		// TODO Auto-generated method stub

	}

	public MyJiesuanListItem curItem = null; // set by adapter

	OnClickListener btn_jiesuan_chaxunOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnId_jiesuan_chaxun:
				// 查询相应月份的数据
				doRequest();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void Backward() {
		// TODO Auto-generated method stub
		super.Backward();

		/*
		 * if (!backTip.getView().isShown()){ backTip.show(); return;}
		 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		root.startActivity(intent);
		// root.moveTaskToBack(false);
	}
}
