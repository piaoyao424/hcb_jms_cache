package com.btten.calltaxi.CallTaxi.tools;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btten.Jms.R;
import com.btten.calltaxi.wheelview.NumericWheelAdapter;
import com.btten.calltaxi.wheelview.OnWheelChangedListener;
import com.btten.calltaxi.wheelview.WheelView;
import com.btten.tools.Util;

public class WheelDateShow extends TextView implements OnClickListener {
	private Context context;
	private Dialog dialog;
	private Calendar calendar;
	private static int START_YEAR = 1900, END_YEAR = 2100;

	String dateStr = null;
	boolean isShowed = false;

	public WheelDateShow(Context context) {
		super(context);
		init(context);
	}

	public WheelDateShow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WheelDateShow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		this.setClickable(true);
		this.setOnClickListener(this);
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		calendar = Calendar.getInstance();

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (isShowed) {
					WheelDateShow.this.setClickable(true);
					isShowed = false;
				}
			}
		});

		initDateTimePicker();
	}

	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;

	private LinearLayout date_ll;
	private LinearLayout time_ll;
	private TextView title;

	// 添加大小月月份并将其转换为list,方便之后的判断
	private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] months_little = { "4", "6", "9", "11" };

	private List<String> list_big = Arrays.asList(months_big);
	private List<String> list_little = Arrays.asList(months_little);

	public void initDateTimePicker() {

		// dialog.setTitle("请选择日期:");
		// 找到dialog的布局文件
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.calltaxi_time_layout, null);

		title = (TextView) view.findViewById(R.id.reserve_datetime_title);
		date_ll = (LinearLayout) view.findViewById(R.id.reserve_date);
		time_ll = (LinearLayout) view.findViewById(R.id.reserve_time);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.reserve_years);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字

		// 月
		wv_month = (WheelView) view.findViewById(R.id.reserve_monthes);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");

		// 日
		wv_day = (WheelView) view.findViewById(R.id.reserve_days);
		wv_day.setCyclic(true);
		wv_day.setLabel("日");

		// 时
		wv_hours = (WheelView) view.findViewById(R.id.reserve_hours);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setLabel("时");

		// 分
		wv_mins = (WheelView) view.findViewById(R.id.reserve_mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		wv_mins.setCyclic(true);
		wv_mins.setLabel("分");

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				int day = wv_day.getCurrentItem();
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0){
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					}
					else{
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
					}
				}
				wv_day.setCurrentItem(day);
			}
		};

		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小
		float textSize = 0;

		textSize = Util.dip2px(context, 20);

		wv_day.TEXT_SIZE = textSize;

		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		Button btn_sure = (Button) view
				.findViewById(R.id.reserve_datetime_sure);
		Button btn_cancel = (Button) view
				.findViewById(R.id.reserve_datetime_cancel);
		// 确定
		btn_sure.setOnClickListener(this);
		// 取消
		btn_cancel.setOnClickListener(this);

		// 设置dialog的布局,并显示
		dialog.setContentView(view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reserve_datetime_sure:
			String parten = "00";
			DecimalFormat decimal = new DecimalFormat(parten);

			if (!isDatePicked) {
				isDatePicked = true;
				// 如果是个数,则显示为"02"的样式

				// 设置日期的显示
				dateStr = (wv_year.getCurrentItem() + START_YEAR) + "-"
						+ decimal.format((wv_month.getCurrentItem() + 1)) + "-"
						+ decimal.format((wv_day.getCurrentItem() + 1));

				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);

				wv_hours.setCurrentItem(hour);
				wv_mins.setCurrentItem(minute);

				title.setText("设置时间");
				date_ll.setVisibility(View.GONE);
				time_ll.setVisibility(View.VISIBLE);

			} else {
				dateStr += " " + decimal.format(wv_hours.getCurrentItem())
						+ ":" + decimal.format(wv_mins.getCurrentItem());
				this.setText(dateStr);
				dialog.dismiss();
			}
			break;
		case R.id.reserve_datetime_cancel:
			dialog.dismiss();
			break;

		default:
			show();
			break;
		}
	}

	boolean isDatePicked = false;

	private void show() {
		isShowed = true;
		this.setClickable(false);

		isDatePicked = false;
		title.setText("设置日期");
		date_ll.setVisibility(View.VISIBLE);
		time_ll.setVisibility(View.GONE);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);

		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		wv_month.setCurrentItem(month);

		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setCurrentItem(day - 1);

		dialog.show();
	}
}
