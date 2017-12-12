package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.CommentList;
import specup.fanmind.R;


public class CommentAdapter extends BaseAdapter{
	private ArrayList<CommentList> mList = null;
	private LayoutInflater layout_In = null;
	private OnClickListener mOnClickListener = null;
	private Context mContext;
	
	public CommentAdapter(Context context,  ArrayList<CommentList> mList, OnClickListener onClickListener){
		this.mList = mList;
		mContext = context;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOnClickListener = onClickListener;
	}


	public void add(CommentList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public CommentList getItem(int position) {
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
			convertView = layout_In.inflate(R.layout.list_comment, null);
			holder.mProfile =(ImageView)convertView.findViewById(R.id.listcomment_profile);
			holder.mLike =(ImageView)convertView.findViewById(R.id.listcomment_img02);
			holder.mLayout = (RelativeLayout)convertView.findViewById(R.id.listcomment_layout01);
					
			holder.mNickName =(TextView)convertView.findViewById(R.id.listcomment_tv01);
			holder.mContents =(TextView)convertView.findViewById(R.id.listcomment_tv02);
			holder.mDate =(TextView)convertView.findViewById(R.id.listcomment_tv03);
			holder.mIsLike=(TextView)convertView.findViewById(R.id.listcomment_tv04);
			holder.mLikeCount =(TextView)convertView.findViewById(R.id.listcomment_tv05);
			holder.mReport =(TextView)convertView.findViewById(R.id.listcomment_tv06);
			holder.mDel =(TextView)convertView.findViewById(R.id.listcomment_tv07);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		CommentList comment = mList.get(position);
		if(comment!=null){
			
			if(!comment.getProfilePath().contains("null")){
				ImageLoader.getInstance().displayImage(comment.getProfilePath(), holder.mProfile);
			}else{
				holder.mProfile.setImageBitmap(null);
				holder.mProfile.setBackgroundResource(R.drawable.profile_basic01);
			}

			holder.mNickName.setText(comment.getNickName());
			holder.mContents.setText(Utils.getColorNickName(comment.getContent()));
			if(Utils.getLocal(mContext, mContext.getString(R.string.korean))){
				holder.mDate.setText(Utils.chageCommentDate(mContext, comment.getDate()));
			}else{
				holder.mDate.setText(comment.getDate().substring(0, 10));
			}
			
			holder.mLikeCount.setText(comment.getLikeCount());
			
			if(comment.getLike().equals("0")){
				holder.mLike.setBackgroundResource(R.drawable.detail_like_off);
				holder.mIsLike.setText(R.string.like01);
				holder.mIsLike.setTextColor(Color.parseColor("#c4c4c4"));
				holder.mLikeCount.setTextColor(Color.parseColor("#c4c4c4"));
			}else{
				holder.mLike.setBackgroundResource(R.drawable.detail_like);
				holder.mIsLike.setText(R.string.like02);
				holder.mIsLike.setTextColor(Color.parseColor("#ed232b"));
				holder.mLikeCount.setTextColor(Color.parseColor("#ed232b"));
			}
			
			
			//글쓴 아이디와 나의 아이디가 같다면 안보이게 설정.
			if(FanMindSetting.getEMAIL_ID(mContext).equals(comment.getID())){
				holder.mReport.setVisibility(View.GONE);
				holder.mDel.setVisibility(View.VISIBLE);
				
			}else{
				holder.mReport.setVisibility(View.VISIBLE);
				holder.mDel.setVisibility(View.GONE);
			}
			
			holder.mReport.setTag(position);
			holder.mReport.setOnClickListener(mOnClickListener);
			
			holder.mDel.setTag(position);
			holder.mDel.setOnClickListener(mOnClickListener);
			
			holder.mLayout.setTag(position);
			holder.mLayout.setOnClickListener(mOnClickListener);
		}
		
		return convertView;
	}

	public class Holder{
		ImageView mProfile, mLike;
		TextView mNickName, mContents, mDate, mLikeCount, mIsLike, mReport, mDel;
		RelativeLayout mLayout;
	}
}
