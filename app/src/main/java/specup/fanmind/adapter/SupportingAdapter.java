package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.SupportList;
import specup.fanmind.R;


public class SupportingAdapter extends BaseAdapter{
	private ArrayList<SupportList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	private String sToday;
	boolean isDelivery, isAll;
	OnClickListener mClick;

	public SupportingAdapter(Context context,  ArrayList<SupportList> mList, boolean isDelivery, OnClickListener click,
			boolean isall){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sToday = Utils.getTime();
		this.isDelivery = isDelivery;
		mClick = click;
		isAll = isall;
	}


	public void add(SupportList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public SupportList getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		SupportList supportlist = mList.get(position);
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_support, null);
			holder.layout01 =(RelativeLayout)convertView.findViewById(R.id.listsupport_layout04);
			holder.layout02 =(RelativeLayout)convertView.findViewById(R.id.listsupport_layout05);
			holder.img01 = (ImageView)convertView.findViewById(R.id.listsupport_img01);
			holder.img02 = (ImageView)convertView.findViewById(R.id.listsupport_img02);
			holder.extra = (Button)convertView.findViewById(R.id.listsupport_btn);
			holder.seekbar = (SeekBar)convertView.findViewById(R.id.listsupport_seekbar);
			holder.tv01 = (TextView)convertView.findViewById(R.id.listsupport_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listsupport_tv02);
			holder.tv03 = (TextView)convertView.findViewById(R.id.listsupport_tv04);
			holder.tv04 = (TextView)convertView.findViewById(R.id.listsupport_tv06);
			holder.hidetv01 = (TextView)convertView.findViewById(R.id.listsupport_hidetv02);
			holder.nametv = (TextView)convertView.findViewById(R.id.listsupport_tv);
			holder.layout =(RelativeLayout)convertView.findViewById(R.id.listsupport_layout);
			if(isDelivery){
				holder.layout02.setVisibility(View.VISIBLE);
			}else{
				holder.layout01.setVisibility(View.VISIBLE);
			}
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		if(supportlist!=null){

			holder.tv01.setText(mList.get(position).getTitle());
			double result = Utils.getPercent(mList.get(position).getNowMoney(), mList.get(position).getGoalMoney());
			int percent = (int)(result);

			ImageLoader.getInstance().displayImage(supportlist.getThumImgPath(), holder.img01);
			//			holder.seekbar.setFocusable(false);
			holder.seekbar.setProgress(percent);
			holder.tv02.setText(String.valueOf(percent));

			if(isDelivery){
				holder.extra.setBackgroundResource(R.drawable.supportready_all);
				holder.hidetv01.setText(Utils.getMoney(mList.get(position).getNowMoney()));				
			}else{
				holder.extra.setBackgroundResource(R.drawable.support_all);
				holder.tv03.setText(Utils.getMoney(mList.get(position).getNowMoney()));
			}

			int dday = Utils.GetDifferenceOfDate(mList.get(position).getDeadDate(), sToday);
			if(dday >0){
				holder.tv04.setText(mContext.getString(R.string.listsupport04).replace("{DAY}", String.valueOf(dday)));	
			}else if(dday==0){
				holder.tv04.setText("D - Day");
			}else if(dday<0){
				holder.tv04.setText(mContext.getString(R.string.deadline2));
			}

			//			holder.seekbar.setFocusableInTouchMode(false);
			holder.seekbar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});


			if(!isAll){
				if(position+1 == mList.size()){
					holder.extra.setVisibility(View.VISIBLE);
				}else{
					holder.extra.setVisibility(View.GONE);
				}
			}
			
			if(position+1 == mList.size()){
				holder.img02.setVisibility(View.VISIBLE);
			}else{
				holder.img02.setVisibility(View.GONE);
			}

			if(mList.get(position).getStarName() !=null){
				holder.layout.setVisibility(View.VISIBLE);
				holder.nametv.setText(mList.get(position).getStarName());	
			}else{
				holder.layout.setVisibility(View.GONE);
			}

			if(position>0){
				if(mList.get(position).getStarName() !=null){
					if(mList.get(position-1).getStarName().equals(mList.get(position).getStarName())){
						holder.layout.setVisibility(View.GONE);
					}else{
						holder.layout.setVisibility(View.VISIBLE);
					}
				}
			}

			holder.extra.setFocusable(false);
			holder.extra.setFocusableInTouchMode(false);
			holder.extra.setOnClickListener(mClick);

		}
		return convertView;
	}

	public class Holder{
		ImageView img01, img02;
		Button extra;
		TextView tv01, tv02, tv03, tv04, hidetv01, nametv;
		SeekBar seekbar;
		ProgressBar progress;
		RelativeLayout layout01, layout02, layout03, layout;
	}
}
