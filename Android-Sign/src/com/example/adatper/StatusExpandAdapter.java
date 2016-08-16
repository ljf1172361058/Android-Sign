package com.example.adatper;

import java.util.List;

import com.example.entity.ChildStatusEntity;
import com.example.entity.GroupStatusEntity;
import com.example.testmap.R;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class StatusExpandAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater = null;
	private List<GroupStatusEntity> groupList;
	private Context context;
	private ForegroundColorSpan colorSpan;
	private AbsoluteSizeSpan sizeSpan;
	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param oneList
	 */
	public StatusExpandAdapter(Context context,
			List<GroupStatusEntity> group_list) {
		this.groupList = group_list;
		this.context=context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sizeSpan = new AbsoluteSizeSpan(12,true);
		colorSpan = new ForegroundColorSpan(Color.RED);
	}

	/**
	 * 返回一级Item总数
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupList.size();
	}

	/**
	 * 返回二级Item总数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupList.get(groupPosition).getChildList() == null) {
			return 0;
		} else {
			return groupList.get(groupPosition).getChildList().size();
		}
	}

	/**
	 * 获取一级Item内容
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	/**
	 * 获取二级Item内容
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getChildList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		GroupViewHolder holder = new GroupViewHolder();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group_status_item, null);
		}
		holder.groupName = (TextView) convertView
				.findViewById(R.id.one_status_name);
		String string=groupList.get(groupPosition).getGroupName();
		if(string.contains("签到")||string.contains("签退")){
			// 新建一个可以添加属性的文本对象
			SpannableString spannableString=new SpannableString(string);
			// 附加属性到文本 可重复设置
			spannableString.setSpan(colorSpan,2,string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			spannableString.setSpan(sizeSpan,2,string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			holder.groupName.setText(spannableString);
		}
		else{
			holder.groupName.setText(string);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder = null;
		ChildStatusEntity entity = (ChildStatusEntity) getChild(groupPosition,
				childPosition);
		if (convertView != null) {
			viewHolder = (ChildViewHolder) convertView.getTag();
		} else {
			viewHolder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.child_status_item, null);
			viewHolder.twoStatusTime = (TextView) convertView
					.findViewById(R.id.two_complete_time);
		}
		viewHolder.twoStatusTime.setText(entity.getAddress());
		String str=groupList.get(groupPosition).getGroupName();
		if("签到".equals(str)||"签退".equals(str)){
			viewHolder.twoStatusTime.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_list_phone),null,null,null);
		}
		else if("完成".equals(str)){
			viewHolder.twoStatusTime.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_signin_coffee),null,null,null);
		}
		convertView.setTag(viewHolder);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private class GroupViewHolder {
		TextView groupName;
	}

	private class ChildViewHolder {
		public TextView twoStatusTime;
	}

}
