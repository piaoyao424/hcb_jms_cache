package com.btten.calltaxi.CallTaxi.copy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.baidu.location.BDLocation;
import com.btten.Jms.ClassTools;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.calltaxi.CallTaxi.tools.CallTaxiScreenLock;
import com.btten.calltaxi.CallTaxi.tools.CallTaxiVoiceUpload;
import com.btten.calltaxi.CallTaxi.tools.ListMenuPop;
import com.btten.calltaxi.CallTaxi.tools.VoiceUploadCallBack;
import com.btten.calltaxi.CallTaxi.tools.WheelDateShow;
import com.btten.calltaxi.Service.CallTaxiJumpWindow;
import com.btten.calltaxi.Service.LocationClientService;
import com.btten.calltaxi.Service.core.JmsMapManager;
import com.btten.calltaxi.Service.extmodel.CallTaxiInfo;
import com.btten.uikit.bubbleani.BubbleAniView;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CallTaxiNeedCar {
	LinearLayout centerTextLayout;
	RelativeLayout calltaxiCenter;
	View view;
	String[] texts = {"预约出租车", "预约货车", "预约代驾", "预约陪驾", "我要叫出租车", "我要叫货车", "我要找代驾", "我要找陪驾", "救援"};
	// 加价菜单文本
	String[] addPriceStrings = { "0元", "5元", "10元", "15元", "20元" };
	// 等待时间文本
	String[] waitingTimeStrings = { "0分钟", "5分钟", "10分钟", "15分钟", "20分钟" };

	Button leftMenu, rightMenu;
	String addPriceChoice = addPriceStrings[0],
			waitingTimeChoice = waitingTimeStrings[0];
	String time, start, end, cargo;
	EditText startEdit, endEdit, cargoEdit;
	LinearLayout menuLayout, timeLayout, startLayout, endLayout, cargoLayout;
	RelativeLayout centerImageView;
	ImageButton voice2textImageButton;
	Button submitButton;
	
	View blank;
	
	WheelDateShow timeShow;

	final int minSpeekTime = 3000;

	int type;
	LayoutInflater inflater;
	Activity context;

	JmsMapManager mManager;

	CallTaxiPublishActivity root;

	BubbleAniView voiceAnimation;

	public CallTaxiNeedCar(CallTaxiPublishActivity context, String text) {
		this.context = context;
		// 获取构造器
		inflater = LayoutInflater.from(context);
		// 确定页面功能类型
		type = -1;
		for (int i = 0; i < texts.length; ++i) {
			if (text.equals(texts[i]))
				type = i;
		}

		root = context;

		mManager = JmsMapManager.getInstance();

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.calltaxi_needcar, null);
		view.setClickable(true);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		// 语音界面中心背景图
		centerImageView = (RelativeLayout) view
				.findViewById(R.id.calltaxi_center_voice_bg);
		voiceAnimation = (BubbleAniView) view
				.findViewById(R.id.calltaxi_center_animation);

		// 语音录入和文字录入切换按钮
		voice2textImageButton = (ImageButton) view
				.findViewById(R.id.calltaxi_switch_imagebutton);
		// 提交按钮 录音界面长按录音
		submitButton = (Button) view
				.findViewById(R.id.calltaxt_submit_textbutton);
		blank = view.findViewById(R.id.calltaxi_blank);
		menuLayout = (LinearLayout) view.findViewById(R.id.calltaxi_MenuLayout);
		// 顶部左侧菜单
		leftMenu = (Button) view.findViewById(R.id.calltaxi_LeftListMenuLayout);
		// 顶部右侧菜单
		rightMenu = (Button) view
				.findViewById(R.id.calltaxi_RightListMenuLayout);
		// 中间文字视图文本输入框
		centerTextLayout = (LinearLayout) view
				.findViewById(R.id.calltaxi_center_textlayout);
		// 时间输入框
		timeShow = (WheelDateShow) view
				.findViewById(R.id.calltaxi_center_time_editview);
		// 出发地输入框
		startEdit = (EditText) view
				.findViewById(R.id.calltaxi_center_start_editview);
		// 目的地输入框
		endEdit = (EditText) view
				.findViewById(R.id.calltaxi_center_end_editview);
		// 货物输入框
		cargoEdit = (EditText) view
				.findViewById(R.id.calltaxi_center_cargo_editview);
		// 时间输入框整体层
		timeLayout = (LinearLayout) view.findViewById(R.id.calltaxi_timelayout);
		// 出发地输入框整体层
		startLayout = (LinearLayout) view
				.findViewById(R.id.calltaxi_startlayout);
		// 目的地输入框整体层
		endLayout = (LinearLayout) view.findViewById(R.id.calltaxi_endlayout);
		// 货物输入框整体层
		cargoLayout = (LinearLayout) view
				.findViewById(R.id.calltaxi_cargolayout);

		leftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListMenuPop temp = new ListMenuPop(context);
				temp.setTitle("选择加价");
				temp.setData(addPriceStrings, new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						leftMenu.setText(((TextView) view).getText().toString()
								.trim());
						addPriceChoice = addPriceStrings[position];
					}
				});
				temp.getPopupWindow().showAsDropDown(v);
			}
		});

		rightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListMenuPop temp = new ListMenuPop(context);
				temp.setTitle("愿意等待");
				temp.setData(waitingTimeStrings, new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						rightMenu.setText(((TextView) view).getText()
								.toString().trim());
						waitingTimeChoice = waitingTimeStrings[position];
					}
				});
				temp.getPopupWindow().showAsDropDown(v);
			}
		});

		// 依据type取值设置对应的不同的显示视图
		switch (type) {
		case 0:// 预约出租车
		case 2:// 预约代驾
		case 3:// 预约陪驾
			rightMenu.setVisibility(View.INVISIBLE);
			cargoLayout.setVisibility(View.GONE);
			break;
		case 1:// 预约货车
			rightMenu.setVisibility(View.INVISIBLE);
			break;
		case 4:// 呼叫出租车
		case 6:// 呼叫代驾
		case 7:// 呼叫陪驾
			timeLayout.setVisibility(View.GONE);
			cargoLayout.setVisibility(View.GONE);
			break;
		case 5:// 呼叫货车
			timeLayout.setVisibility(View.GONE);
			break;
		case 8:// 呼叫救援
			blank.setVisibility(View.GONE);
			menuLayout.setVisibility(View.GONE);
			endLayout.setVisibility(View.GONE);
			cargoLayout.setVisibility(View.GONE);
			timeLayout.setVisibility(View.GONE);
			//timeShow.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
			break;
		default:
			break;
		}

		// 设置 录音/文本 转换监听
		voice2textImageButton.setOnClickListener(voice2textListener);

		setVoiceOffState();

		preUploadFile = null;
	}

	// 录音文本切换按钮监听
	OnClickListener voice2textListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (centerImageView.getVisibility() == View.VISIBLE)
				setVoiceOffState();
			else
				setVoiceOnState();
		}
	};

	// 布置录音界面控件
	protected void setVoiceOnState() {
		// TODO Auto-generated method stub
		voice2textImageButton.setImageResource(R.drawable.keyboard_input_icon);
		submitButton.setText("按住说话");
		submitButton.setOnTouchListener(voiceLongPressListener);
		centerTextLayout.setVisibility(View.GONE);
		centerImageView.setVisibility(View.VISIBLE);
		view.setOnClickListener(null);
	}

	// 布置文字界面控件
	protected void setVoiceOffState() {
		// TODO Auto-generated method stub
		voice2textImageButton
				.setImageResource(R.drawable.home_tab_microphone_icon);
		submitButton.setText("提    交");
		submitButton.setOnTouchListener(textSubmitListener);
		centerTextLayout.setVisibility(View.VISIBLE);
		centerImageView.setVisibility(View.GONE);
		view.requestFocus();
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					closeSoft();
			}
		});
	}

	File preUploadFile;

	// 录音按键长按监听
	Long downTime, upTime;
	boolean clickCanceled;
	OnTouchListener voiceLongPressListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				clickCanceled = false;
				submitButton
						.setBackgroundResource(R.drawable.umeng_share_send_button_sel);
				downTime = event.getDownTime();

				if (!voiceAnimation.IsRunning())
					voiceAnimation.Start();

				if (ClassTools.getInstance().isRequesting)
					break;

				recordStart();
				break;
			case MotionEvent.ACTION_MOVE:
				if (!inTheRangeOfView(v, event.getX(), event.getY())) {
					if (!clickCanceled) {
						clickCanceled = true;
						submitButton
								.setBackgroundResource(R.drawable.umeng_share_send_button_nor);

						upTime = event.getEventTime();
						recordStop();

						if (voiceAnimation.IsRunning())
							voiceAnimation.Stop();

						if (ClassTools.getInstance().isRequesting) {
							//root.Alert("一个更早的订单正在申请中，请稍后再试！");
							isCalling();
							break;
						}

						if (upTime - downTime < minSpeekTime) {
							preUploadFile = null;
							root.Alert("按下时间太短!");
						} else {
							if (!JmsMapManager.getInstance().IsConnect()) {
								root.Alert("未连接服务器！\n请稍后再试！");
								break;
							}
							doUpload();
						}
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				submitButton
						.setBackgroundResource(R.drawable.umeng_share_send_button_nor);
				clickCanceled = true;
				break;
			case MotionEvent.ACTION_UP:
				if (!clickCanceled) {
					submitButton
							.setBackgroundResource(R.drawable.umeng_share_send_button_nor);
					upTime = event.getEventTime();
					recordStop();

					if (voiceAnimation.IsRunning())
						voiceAnimation.Stop();

					if (ClassTools.getInstance().isRequesting) {
						//root.Alert("一个更早的订单正在申请中，请稍后再试！");
						isCalling();
						break;
					}

					if (upTime - downTime < minSpeekTime) {
						preUploadFile = null;
						root.Alert("按下时间太短!");
					} else {
						if (!JmsMapManager.getInstance().IsConnect()) {
							root.Alert("未连接服务器！\n请稍后再试！");
							break;
						}
						doUpload();
					}
				}
				break;
			default:
				break;
			}

			return true;
		}
	};

	public final int ONSUCCESS = 0;
	public final int ONFAIL = 1;

	public void doUpload() {
		if (preUploadFile != null)
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new CallTaxiVoiceUpload(new VoiceUploadCallBack() {

						@Override
						public void onSuccess(String link) {
							// TODO Auto-generated method stub
							Message msg = Message.obtain();
							msg.obj = link;
							msg.what = ONSUCCESS;
							uploadHandler.sendMessage(msg);
						}

						@Override
						public void onFail() {
							// TODO Auto-generated method stub
							uploadHandler.sendEmptyMessage(ONFAIL);
						}
					}, preUploadFile);
				}
			}).start();
		else
			root.Alert("没有可用录音文件！");
	}

	String voiceLink = null;
	Handler uploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ONFAIL)
				root.Alert("发布失败！");
			else if (msg.what == ONSUCCESS) {
				if (msg.obj == null)
					root.Alert("发布失败！");
				else {
					voiceLink = msg.obj.toString();
					doCall();
				}
			}
		};
	};

	OnTouchListener textSubmitListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				clickCanceled = false;
				submitButton
						.setBackgroundResource(R.drawable.umeng_share_send_button_sel);
				break;
			case MotionEvent.ACTION_MOVE:
				if (!inTheRangeOfView(v, event.getX(), event.getY()))
					if (!clickCanceled) {
						clickCanceled = true;
						submitButton
								.setBackgroundResource(R.drawable.umeng_share_send_button_nor);

						if (ClassTools.getInstance().isRequesting) {
							//root.Alert("一个更早的订单正在申请中，请稍后再试！");
							isCalling();
							break;
						}

						if (!JmsMapManager.getInstance().IsConnect()) {
							root.Alert("未连接服务器！\n请稍后再试！");
							break;
						}

						if (checkTextInfo(type)) {
							doCall();
						}
					}
				break;
			case MotionEvent.ACTION_CANCEL:
				clickCanceled = true;
				submitButton
						.setBackgroundResource(R.drawable.umeng_share_send_button_nor);
				break;
			case MotionEvent.ACTION_UP:
				if (!clickCanceled) {
					submitButton
							.setBackgroundResource(R.drawable.umeng_share_send_button_nor);

					if (ClassTools.getInstance().isRequesting) {
						isCalling();
						//root.Alert("一个更早的订单正在申请中，请稍后再试！");
						break;
					}

					if (!JmsMapManager.getInstance().IsConnect()) {
						root.Alert("未连接服务器！\n请稍后再试！");
						break;
					}

					if (checkTextInfo(type)) {
						doCall();
					}
				}
				break;
			}
			return true;
		}
	};

	private boolean inTheRangeOfView(View v, float x, float y) {
		float w, h;
		w = (float) v.getMeasuredWidth();
		h = (float) v.getMeasuredHeight();

		if (0 < x && x < w && 0 < y && y < h)
			return true;
		else
			return false;
	}

	private File tempFile;
	private File tempDir;
	private String tempDirStr;
	private String tempName = "calltaxi_voice_temp";
	private MediaRecorder recorder;

	/**
	 * 录音开始
	 */
	private void recordStart() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			try {
				tempDirStr = Environment.getExternalStorageDirectory()
						.getPath().toString()
						+ context.getResources().getString(R.string.TEMP_PATH);
				tempDir = new File(tempDirStr);

				if (!tempDir.exists()) {
					tempDir.mkdirs();
					if (!tempDir.isDirectory())
						Log.i("yexinTAG", "tempDir can't be created!");
				}
				tempFile = new File(tempDirStr + "/" + tempName + ".amr");
				preUploadFile = tempFile;

				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				recorder.setOutputFile(tempFile.getAbsolutePath());
				recorder.prepare();
				recorder.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			root.Alert("无法缓存语音文件!请发送文本信息！");
		}
	}

	/**
	 * 录音结束
	 */
	private void recordStop() {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}
	}

	private boolean checkTextInfo(int type) {
		boolean check = true;

		start = startEdit.getText().toString().trim();
		end = endEdit.getText().toString().trim();
		time = timeShow.getText().toString().trim();
		cargo = cargoEdit.getText().toString().trim();

		if (type < 4 && time.equals("")) {
			root.Alert("请输入用车时间！");
			check = false;
		} 
		if (start.equals("")) {
			root.Alert("请输入出发地！");
			check = false;
		}
		if (type != 8 && end.equals("")) {
			root.Alert("请输入目的地！");
			check = false;
		}
		
		if ((type == 1 || type == 5) && cargo.equals("")) {
			root.Alert("请输入货物！");
			check = false;
		}

		return check;
	}

	/**
	 * @return 返回构建好的页面
	 */
	public View getView() {
		return this.view;
	}
	
	private void isCalling(){
		Intent intent = new Intent(root, CallTaxiJumpWindow.class);
		intent.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_NO_ANIMATION
				);
		intent.putExtra("ACTION", "reShow");
		root.startActivity(intent);
		root.overridePendingTransition(0, 0);
	}

	private void doCall() {
		ClassTools.getInstance().isRequesting = true;

		Intent intent = new Intent(root, CallTaxiJumpWindow.class);
		intent.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_NO_ANIMATION
				);
		intent.putExtra("ACTION", "doCall");
		intent.putExtra("ORDERTYPE", type);
		root.startActivity(intent);
		root.overridePendingTransition(0, 0);

		CallTaxiInfo info = new CallTaxiInfo();
		info.setType(type);
		info.setAddmoney(addPriceChoice);
		info.setWaittime(waitingTimeChoice);
		info.setNeedtime(time);
		info.setStartloc(start);
		info.setEndloc(end);
		info.setCargo(cargo);

		String name = JmsAccountManager.getInstance().getUsername();
		if (name == null || name.equals(""))
			name = JmsAccountManager.getInstance().getNickname();
		info.setNickname(name);

		info.setPhone(JmsAccountManager.getInstance().getUserPhone());
		info.setVoicelink(voiceLink);

		if (!LocationClientService.getInstance().isStarted()) {
			root.Alert("定位服务已经停止！\n    正在重启服务！");
			LocationClientService.getInstance().start();
			return;
		}

		BDLocation location;

		if ((location = LocationClientService.getInstance().getLocation()) == null) {
			root.Alert("没能获取地理坐标，请稍后再试！");
			return;
		}

		info.setLat(location.getLatitude());
		info.setLon(location.getLongitude());

		mManager.CallTaxi(info);

		time = null;
		start = null;
		end = null;
		cargo = null;
		voiceLink = null;
	}

	private void closeSoft() {
		InputMethodManager imm = (InputMethodManager) root
				.getSystemService(root.INPUT_METHOD_SERVICE);
		if (imm.isActive())
			imm.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
