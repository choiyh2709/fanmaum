package specup.fanmind.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
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

import specup.fanmind.R;
import specup.fanmind.common.Util.PagerCon;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.NewsFeedList;


public class NewsFeedAdapter extends BaseAdapter{
	private ArrayList<NewsFeedList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	ImagePagerAdapter adapter;
	private OnClickListener mOnClickListener = null;
	int mWidth;
	public NewsFeedAdapter(Context context,  ArrayList<NewsFeedList> mList, OnClickListener onClickListener){
		this.mList = mList;
		mContext = context;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOnClickListener = onClickListener;
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		mWidth = displayMetrics.widthPixels;
	}

	public void add(NewsFeedList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}

	public void removeList(){
		mList = new ArrayList<NewsFeedList>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public NewsFeedList getItem(int position) {
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
			convertView = layout_In.inflate(R.layout.list_newspeed, null);
			holder.mProfile = (ImageView)convertView.findViewById(R.id.listnews_img);
			holder.mLike = (ImageView)convertView.findViewById(R.id.listnews_like);
			holder.mHide =(ImageView)convertView.findViewById(R.id.listnews_hide);
			holder.mTitle =(TextView)convertView.findViewById(R.id.listnews_tv01);
			holder.mDate =(TextView)convertView.findViewById(R.id.listnews_tv02);
			holder.mContents =(TextView)convertView.findViewById(R.id.listnews_tv03);
			holder.mLikeClick =(TextView)convertView.findViewById(R.id.listnews_tv04);
			holder.mMsgClick =(TextView)convertView.findViewById(R.id.listnews_tv05);
			holder.LikeLayout =(RelativeLayout)convertView.findViewById(R.id.listnews_layout04);
			holder.MsgLayout =(RelativeLayout)convertView.findViewById(R.id.listnews_layout05);
			holder.mBtn =(Button)convertView.findViewById(R.id.listnews_btn01);

			holder.mPagerCon = (PagerCon)convertView.findViewById(R.id.pager_container);
			holder.mPager =holder.mPagerCon.getViewPager();
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		NewsFeedList newsfeed = mList.get(position);

		if(newsfeed!=null){
			
 			if(!newsfeed.getPic().contains("null")){
				
				ImageLoader.getInstance().displayImage(newsfeed.getPic(), holder.mProfile);

			}else{
				holder.mProfile.setImageBitmap(null);
				holder.mProfile.setBackgroundResource(R.drawable.profile_basic01);
			}
			
			holder.mTitle.setText(newsfeed.getNick());
			String time = Utils.chageCommentDate(mContext, newsfeed.getTime());
			holder.mDate.setText(time);
			holder.mContents.setText(newsfeed.getText());
			holder.mLikeClick.setText(Utils.getMoney(newsfeed.getLikes()));
			holder.mMsgClick.setText(Utils.getMoney(newsfeed.getReplyCnt()));
			
			if(newsfeed.getImg().length == 1){
				holder.mPager.setVisibility(View.GONE);
			}else{
				holder.mPager.setVisibility(View.VISIBLE);
				adapter = new ImagePagerAdapter(mContext, newsfeed.getImg(), mOnClickListener, position);
				holder.mPager.setAdapter(adapter);
				holder.mPager.setOffscreenPageLimit(adapter.getCount());
				if(mWidth ==768){
					holder.mPager.setPageMargin(Utils.getDP(mContext, -60));
				}else{
					holder.mPager.setPageMargin(Utils.getDP(mContext, -43));
//					holder.mPager.setPageMargin(Utils.getDP(mContext, -140));
				}
			}

			
			if(newsfeed.getMyLike().equals("1")){
				holder.mLike.setBackgroundResource(R.drawable.history_like_on);
			}else{
				holder.mLike.setBackgroundResource(R.drawable.history_like);
			}
			
			holder.MsgLayout.setTag(position);
			holder.LikeLayout.setTag(position);

			holder.MsgLayout.setOnClickListener(mOnClickListener);
			holder.LikeLayout.setOnClickListener(mOnClickListener);
			holder.mBtn.setOnClickListener(mOnClickListener);
			holder.mBtn.setTag(position);
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
		TextView mTitle, mDate, mContents, mLikeClick, mMsgClick;
		PagerCon mPagerCon;
		ViewPager mPager;
		Button mBtn;
		RelativeLayout LikeLayout, MsgLayout;
	}


}
