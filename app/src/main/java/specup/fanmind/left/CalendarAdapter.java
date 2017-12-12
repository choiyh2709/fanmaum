package specup.fanmind.left;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.MainDayInfo;


/**
 * BaseAdapter를 상속받아 구현한 CalendarAdapter
 *
 * @author croute
 * @since 2011.03.08
 */
public class CalendarAdapter extends BaseAdapter{

	private ArrayList<MainDayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;

    /**
  	 * Adpater 생성자
  	 *
  	 * @param context
  	 *            컨텍스트
  	 * @param textResource
  	 *            레이아웃 리소스
  	 * @param dayList
  	 *            날짜정보가 들어있는 리스트
  	 */
	public CalendarAdapter(Context context, int textResource, ArrayList<MainDayInfo> dayList){
		this.mContext = context;
		this.mDayList = dayList;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return mDayList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return mDayList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position){
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		MainDayInfo day = mDayList.get(position);
		DayViewHolde dayViewHolder;
		if(convertView == null){
			convertView = mLiInflater.inflate(mResource, null);
			dayViewHolder = new DayViewHolde();
			dayViewHolder.llBackground = (RelativeLayout)convertView.findViewById(R.id.day_cell_ll_background);
			dayViewHolder.mTodayLayout= (RelativeLayout)convertView.findViewById(R.id.day_cell);
			dayViewHolder.mSchLayout = (RelativeLayout)convertView.findViewById(R.id.day_layout);
			dayViewHolder.mUnderImg= (ImageView)convertView.findViewById(R.id.under_img);
			if(position % 7 == 0){
				dayViewHolder.llBackground.setLayoutParams(new RelativeLayout.LayoutParams(getCellWidthDP(), getCellHeightDP()));
			}else{
				dayViewHolder.llBackground.setLayoutParams(new RelativeLayout.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));	
			}

			dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);
			dayViewHolder.tvNum = (TextView) convertView.findViewById(R.id.day_tv);

			convertView.setTag(dayViewHolder);
		}else{
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}

		if(day != null){
			dayViewHolder.tvDay.setText(day.getDay());
			if(day.getTitle1() !=null){
				if(!day.getTitle1().equals("0")){
					String sch =mContext.getString(R.string.sch).replace("{NUM}", day.getTitle1());
					dayViewHolder.mSchLayout.setVisibility(View.VISIBLE);
					dayViewHolder.tvNum.setText(sch);
				}else{
					dayViewHolder.mSchLayout.setVisibility(View.GONE);
				}
			}

			if(day.isInMonth()){
				if(position % 7 == 0){
					dayViewHolder.tvDay.setTextColor(Color.parseColor("#ff0000"));
				}else if(position % 7 == 6){
					dayViewHolder.tvDay.setTextColor(Color.parseColor("#47a2f7"));
				}else{
					dayViewHolder.tvDay.setTextColor(Color.parseColor("#464646"));
				}

				if(day.isToday()){
					dayViewHolder.mTodayLayout.setVisibility(View.VISIBLE);
				}else{
					dayViewHolder.mTodayLayout.setVisibility(View.GONE);
				}

			}else{
				dayViewHolder.tvDay.setTextColor(Color.parseColor("#8e8e8e"));
			}
		}
		return convertView;
	}

	public class DayViewHolde{
		public RelativeLayout llBackground, mTodayLayout, mSchLayout;
		public TextView tvDay, tvNum;
		public ImageView mUnderImg;
	}

	private int getCellWidthDP(){
		int cellWidth = Utils.getDP(mContext, 360)/7;
		return cellWidth;
	}

	private int getRestCellWidthDP(){
		int cellWidth =  Utils.getDP(mContext, 360)%7;
		return cellWidth;
	}

	private int getCellHeightDP(){
		//		int height = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellHeight = 0;
		if(FanMindSetting.getCAL_WEEK(mContext)){
			cellHeight = Utils.getDP(mContext, 440)/6;
		}else{
			cellHeight = Utils.getDP(mContext, 440)/5;
		}
		return cellHeight;
	}

}
