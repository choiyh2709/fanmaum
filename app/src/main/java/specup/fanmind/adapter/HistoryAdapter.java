package specup.fanmind.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.common.Util.PagerCon;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.HistoryList;
import specup.fanmind.R;


public class HistoryAdapter extends BaseAdapter{
	private ArrayList<HistoryList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	ImagePagerAdapter adapter;
	//	Holder holder;
	private OnClickListener mOnClickListener = null;
	int mWidth;
	boolean isShow;
	
	public HistoryAdapter(Context context,  ArrayList<HistoryList> mList, OnClickListener onClickListener, boolean show){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOnClickListener = onClickListener;
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		mWidth = displayMetrics.widthPixels;
		isShow = show;
	}


	public void add(HistoryList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public HistoryList getItem(int position) {
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
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_history, null);
			holder.mProfile = (ImageView)convertView.findViewById(R.id.history_img);
			holder.mLike = (ImageView)convertView.findViewById(R.id.history_like);
			holder.mHide =(ImageView)convertView.findViewById(R.id.history_hide);
			holder.mTitle =(TextView)convertView.findViewById(R.id.history_tv01);
			holder.mDate =(TextView)convertView.findViewById(R.id.history_tv02);
			holder.mContents =(TextView)convertView.findViewById(R.id.history_tv03);
			holder.mLikeClick =(TextView)convertView.findViewById(R.id.history_tv04);
			holder.mMsgClick =(TextView)convertView.findViewById(R.id.history_tv05);
			holder.LikeLayout =(RelativeLayout)convertView.findViewById(R.id.history_layout04);
			holder.MsgLayout =(RelativeLayout)convertView.findViewById(R.id.history_layout05);
			holder.mHistoryAll =(Button)convertView.findViewById(R.id.history_btn);
			
			holder.mPagerCon = (PagerCon)convertView.findViewById(R.id.pager_container);
			holder.mPager =holder.mPagerCon.getViewPager();
			
			holder.mLayout =(RelativeLayout)convertView.findViewById(R.id.listhistory_layout);
			holder.mStarName =(TextView)convertView.findViewById(R.id.listhistory_tv);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		HistoryList history = mList.get(position);

		if(history!=null){
			if(history.getPic().contains("null")){
				holder.mProfile.setImageBitmap(null);
				holder.mProfile.setBackgroundResource(R.drawable.profile_basic01);
			}else{
				ImageLoader.getInstance().displayImage(history.getPic(), holder.mProfile);

			}

			holder.mTitle.setText(history.getTitle());
			String time = Utils.chageDate(history.getTime());
			holder.mDate.setText(mContext.getString(R.string.history01).replace("{DATE}", time));
			String text = history.getText()/*.replace("/n", "<br></br>")*/;
			holder.mContents.setText(Html.fromHtml(text));
			holder.mLikeClick.setText(Utils.getMoney(history.getLike()));
			holder.mMsgClick.setText(Utils.getMoney(history.getReplyCount()));
			adapter = new ImagePagerAdapter(mContext, mList.get(position).getImg(), mOnClickListener, position);

			holder.mPager.setAdapter(adapter);
			holder.mPager.setOffscreenPageLimit(adapter.getCount());
			
			if(mWidth ==768){
				holder.mPager.setPageMargin(Utils.getDP(mContext, -60));
			}else{
				holder.mPager.setPageMargin(Utils.getDP(mContext, -43));
			}

			holder.MsgLayout.setTag(position);
			holder.LikeLayout.setTag(position);
			holder.mHistoryAll.setTag(position);
			
			holder.MsgLayout.setOnClickListener(mOnClickListener);
			holder.LikeLayout.setOnClickListener(mOnClickListener);
			holder.mHistoryAll.setOnClickListener(mOnClickListener);
			
			holder.mHistoryAll.setFocusable(false);
			holder.mHistoryAll.setFocusableInTouchMode(false);
			
			if(history.getMyLike().equals("1")){
				holder.mLike.setBackgroundResource(R.drawable.history_like_on);
			}else{
				holder.mLike.setBackgroundResource(R.drawable.history_like);
			}

			if(mList.get(position).getStarName() !=null){
				holder.mLayout.setVisibility(View.VISIBLE);
				holder.mStarName.setText(mList.get(position).getStarName());	
			}else{
				holder.mLayout.setVisibility(View.GONE);
			}
			
		
			
			if(!isShow){
				if(position+1 == mList.size()){
					holder.mHistoryAll.setVisibility(View.VISIBLE);
				}else{
					holder.mHistoryAll.setVisibility(View.GONE);
				}
			}else{
				holder.mHistoryAll.setVisibility(View.GONE);
			} 
			

			if(position-1>0){
				if(mList.get(position).getStarName() !=null){
					if(mList.get(position-1).getStarName().equals(mList.get(position).getStarName())){
						holder.mLayout.setVisibility(View.GONE);
					}else{
						holder.mLayout.setVisibility(View.VISIBLE);
					}
				}
			}
			
			if(position+1 == mList.size()){
				holder.mHide.setVisibility(View.VISIBLE);
			}else{
				holder.mHide.setVisibility(View.GONE);
			}
		}		

		return convertView;
	}

	public class Holder{
		ImageView mProfile, mLike, mHide;
		TextView mTitle, mDate, mContents, mLikeClick, mMsgClick, mStarName;
		PagerCon mPagerCon;
		ViewPager mPager;
		RelativeLayout LikeLayout, MsgLayout, mLayout;
		Button mHistoryAll;
	}
}
