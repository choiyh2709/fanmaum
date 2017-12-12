package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.vo.RightList;

import specup.fanmind.common.Util.Utils;



public class MyPointAdapter  extends BaseExpandableListAdapter{

	private ArrayList<String> mParentList;
	private ArrayList<ArrayList<RightList>> mChildList;
	private LayoutInflater inflater = null;
	Context mContext;

	public MyPointAdapter(Context c, ArrayList<String> parentList, ArrayList<ArrayList<RightList>> childList){
		mParentList = parentList;
		mChildList = childList;
		this.inflater = LayoutInflater.from(c);
		mContext = c;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mParentList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mChildList.get(groupPosition).size();
	}

	@Override
	public String getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mParentList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mChildList.get(groupPosition).get(childPosition);
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
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		if(convertView ==null){
			convertView = inflater.inflate(R.layout.ex_parent, parent, false);
			viewHolder.mParentLayout = (RelativeLayout)convertView.findViewById(R.id.exparent_layout);
			viewHolder.mTvParent01 = (TextView)convertView.findViewById(R.id.exparent_tv01);
			viewHolder.mParentImg = (ImageView)convertView.findViewById(R.id.exparent_img01);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.mTvParent01.setText(mParentList.get(groupPosition));

		if(isExpanded){
			viewHolder.mParentImg.setBackgroundResource(R.drawable.heart_list_arrow02);
		}else{
			viewHolder.mParentImg.setBackgroundResource(R.drawable.heart_list_arrow01);
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		if(convertView ==null){
			convertView = inflater.inflate(R.layout.ex_child, null);
			viewHolder.mTvChild01 = (TextView)convertView.findViewById(R.id.exchild_tv01);
			viewHolder.mTvChild02 = (TextView)convertView.findViewById(R.id.exchild_tv02);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		if(groupPosition ==0){
			viewHolder.mTvChild01.setText(mChildList.get(groupPosition).get(childPosition).getFan());
			String point  = Utils.getMoney(mChildList.get(groupPosition).get(childPosition).getName());
			viewHolder.mTvChild02.setText(point);
		}else{
			String date = mChildList.get(groupPosition).get(childPosition).getPath();
			String type = mChildList.get(groupPosition).get(childPosition).getStarsrl();
			String retype =null, redate =null;
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(5, 7));
			int day = Integer.parseInt(date.substring(8, 10));
			
			if(type.equals("1")){
				redate = mContext.getString(R.string.mypoint_tv08).replace("{YEAR}", String.valueOf(year))
						.replace("{MONTH}", String.valueOf(month)).replace("{DAY}", String.valueOf(day));
				 retype = mContext.getString(R.string.buypoint202); 
			}else if(type.equals("2")){
				redate = mContext.getString(R.string.mypoint_tv08).replace("{YEAR}", String.valueOf(year))
						.replace("{MONTH}", String.valueOf(month)).replace("{DAY}", String.valueOf(day));
				 retype = mContext.getString(R.string.buypoint203); 
			}else if(type.equals("3")){
				redate = mContext.getString(R.string.mypoint_tv08).replace("{YEAR}", String.valueOf(year))
						.replace("{MONTH}", String.valueOf(month)).replace("{DAY}", String.valueOf(day));
				 retype = mContext.getString(R.string.buypoint204); 
			}else if(type.equals("4")){
				if(Utils.getLocal(mContext, "English")){
					redate =Utils.chageTime(month)+"/"+Utils.chageTime(day)+"/"+String.valueOf(year);
					 retype = mContext.getString(R.string.buypoint205); 
				}else{
					redate = mContext.getString(R.string.mypoint_tv08).replace("{YEAR}", String.valueOf(year))
							.replace("{MONTH}", String.valueOf(month)).replace("{DAY}", String.valueOf(day));
					 retype = mContext.getString(R.string.buypoint205); 
				}
			} 
			viewHolder.mTvChild01.setText(redate+" "+retype);
			viewHolder.mTvChild02.setText(mChildList.get(groupPosition).get(childPosition).getName());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class ViewHolder {
		public RelativeLayout mParentLayout;
		public ImageView mParentImg;
		public TextView mTvParent01, mTvChild01, mTvChild02;
	}

}
