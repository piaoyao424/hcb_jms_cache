package com.btten.costitem;

import java.util.List;

import com.btten.Jms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class Costitem_showGroupAdapter extends BaseExpandableListAdapter {
	class ExpandableListHolder { // 定义一个内部类，用于保存listitem的3个子视图引用,2个textview和1个checkbox
		TextView tvName;
		TextView tvMoney;
		CheckBox chkChecked;
		String itemid;
	}
	private String yuan = "元";
	private Context context; // 父activity
	private LayoutInflater mChildInflater; // 用于加载listitem的布局xml
	private LayoutInflater mGroupInflater; // 用于加载group的布局xml
	private List<Costitem_showGroup> groups; // 所有group

	// 构造方法：参数c － activity，参数group － 所有group

	public Costitem_showGroupAdapter(Context c, List<Costitem_showGroup> groups) {
		context = c;
		mChildInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groups = groups;
	}

	@Override
	public Object getChild(int arg0, int arg1) {// 根据组索引和item索引，取得listitem
		return groups.get(arg0).children.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {// 返回item索引
		return arg1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {// 根据组索引返回分组的子item数
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {// 根据组索引返回组
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {// 返回分组数
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {// 返回分组索引
		return groupPosition;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View view,
			ViewGroup parent) {// 根据组索引渲染"组视图"
		ExpandableListHolder holder = null; // 清空临时变量holder
		if (view == null) { // 判断view（即view是否已构建好）是否为空
			// 若组视图为空，构建组视图。注意flate的使用，R.layout.browser_expandable_list_item代表了
			// 已加载到内存的browser_expandable_list_item.xml文件
			view = mGroupInflater.inflate(R.layout.costitem_list_groupitem, null);
			// 下面主要是取得组的各子视图，设置子视图的属性。用tag来保存各子视图的引用
			holder = new ExpandableListHolder();
			// 从view中取得textView
			holder.tvName = (TextView) view.findViewById(R.id.tvId_costitem_groupitem_name);
			// 从view中取得textview
			holder.tvMoney = (TextView) view.findViewById(R.id.tvId_costitem_groupitem_vipprice);
			// 从view中取得checkbox
			holder.chkChecked = (CheckBox) view.findViewById(R.id.cbId_costitem_groupitem_check);
			// holder.chkChecked.setEnabled(false);//禁用checkbox
			// 把checkbox、textview的引用保存到组视图的tag属性中
//			view.setBackgroundResource(R.drawable.button_b_normal);
			view.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableListHolder) view.getTag();
		}
		// 对应于组索引的组数据（模型）
		Costitem_showGroup info = this.groups.get(position);
		if (info != null) {
			// 根据模型值设置textview的文本
			holder.tvName.setText(info.title);
//			holder.tvMoney.setText("全选");
			//0是可见,4是不可见，8是消失，不占据位置
			holder.tvMoney.setVisibility(4);
			holder.chkChecked.setVisibility(4);
			// 根据模型值设置checkbox的checked属性
			/*holder.chkChecked.setChecked(info.getChecked());
			holder.chkChecked.setTag(info);
			holder.chkChecked.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					PhoneGroup group = (PhoneGroup) v.getTag();
					group.setChecked(!group.getChecked());
					notifyDataSetChanged();
				}
			});*/
		}
		return view;
	}

	// 行渲染方法
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ExpandableListHolder holder = null; // 清空临时变量
		if (convertView == null) { // 若行未初始化
			// 通过flater初始化行视图
			convertView = mChildInflater
					.inflate(R.layout.costitem_childlist_item, null);
			// 并将行视图的3个子视图引用放到tag中
			holder = new ExpandableListHolder();
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvId_costitem_childitem_name);
			holder.tvMoney = (TextView) convertView
					.findViewById(R.id.tvId_costitem_childitem_vipprice);
			holder.chkChecked = (CheckBox) convertView
					.findViewById(R.id.cbId_costitem_childitem_check);
			// holder.chkChecked.setEnabled(false);
			convertView.setTag(holder);
		} else { // 若行已初始化，直接从tag属性获得子视图的引用
			holder = (ExpandableListHolder) convertView.getTag();
		}
		// 获得行数据（模型）
		Costitem_showListItem info = this.groups.get(groupPosition).children
				.get(childPosition);
		if (info != null) {
			// 根据模型数据，设置行视图的控件值
			holder.tvName.setText(info.name);
			holder.tvMoney.setText(info.money + yuan);
			holder.itemid = info.id;
			holder.chkChecked.setChecked(info.checked);
			holder.chkChecked.setTag(info);
			holder.chkChecked.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//CheckBox check = (CheckBox) v;
					Costitem_showListItem item = (Costitem_showListItem) v.getTag();
					item.checked = !item.checked;
				}
			});
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {// 行是否具有唯一id
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {// 行是否可选
		return true;
	}
}