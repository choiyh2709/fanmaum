package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.URLImageParser;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.CommentList2;


public class EventCommentAdapter extends BaseAdapter {
    private ArrayList<CommentList2> mList = null;
    private LayoutInflater layout_In = null;
    private OnClickListener mOnClickListener = null;
    private Context mContext;

    public EventCommentAdapter(Context context, ArrayList<CommentList2> mList, OnClickListener onClickListener) {
        this.mList = mList;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOnClickListener = onClickListener;
    }


    public void add(CommentList2 data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public CommentList2 getItem(int position) {
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

        Holder holder = null;
        holder = new Holder();
        convertView = layout_In.inflate(R.layout.list_comment3, null);
        holder.mProfile = (ImageView) convertView.findViewById(R.id.listcomment_profile);
        holder.mLike = (ImageView) convertView.findViewById(R.id.listcomment_img02);
        holder.mLayout = (RelativeLayout) convertView.findViewById(R.id.layout_bottom);

        holder.mNickName = (TextView) convertView.findViewById(R.id.listcomment_tv01);
        holder.mContents = (TextView) convertView.findViewById(R.id.listcomment_tv02);
        holder.mDate = (TextView) convertView.findViewById(R.id.listcomment_tv03);
        holder.mIsLike = (TextView) convertView.findViewById(R.id.listcomment_tv04);
        holder.mLikeCount = (TextView) convertView.findViewById(R.id.listcomment_tv05);
        holder.mReport = (TextView) convertView.findViewById(R.id.listcomment_tv06);
        holder.mDel = (TextView) convertView.findViewById(R.id.textView_delete);
        holder.textView_comment_reply_all = (TextView) convertView.findViewById(R.id.textView_comment_reply_all);

        CommentList2 comment = mList.get(position);
        if (comment != null) {

            if (!comment.getPic_base().contains("null")) {
                ImageLoader.getInstance().displayImage(comment.getPic_base() + comment.getPic(), holder.mProfile);
            } else {
                holder.mProfile.setImageBitmap(null);
                holder.mProfile.setBackgroundResource(R.drawable.profile_basic01);
            }

            holder.mNickName.setText(comment.getNick());

            URLImageParser p = new URLImageParser(holder.mContents);
            Spanned htmlSpan = Html.fromHtml(comment.getContent(), p, null);
            holder.mContents.setText(htmlSpan);
            holder.mDate.setText(comment.getTime());


            //secret
            holder.layout_secret = (LinearLayout) convertView.findViewById(R.id.layout_secret);
            holder.layout_non_secret = (RelativeLayout) convertView.findViewById(R.id.layout_non_secret);

            if(!FanMindSetting.getNICK_NAME(mContext).equals(comment.getNick())) {
                if (comment.getIs_secret().equals("Y")) {

                    holder.layout_secret.setVisibility(View.VISIBLE);
                    holder.layout_non_secret.setVisibility(View.GONE);
                } else {
                    holder.layout_secret.setVisibility(View.GONE);
                    holder.layout_non_secret.setVisibility(View.VISIBLE);
                }
            }

            holder.mLikeCount.setText(comment.getTotal_likes());
            if (comment.getIs_liked().equals("N")) {
                holder.mLike.setBackgroundResource(R.drawable.detail_like_off);
                holder.mIsLike.setText(R.string.like01);
                holder.mIsLike.setTextColor(Color.parseColor("#c4c4c4"));
                holder.mLikeCount.setTextColor(Color.parseColor("#c4c4c4"));
            } else {
                holder.mLike.setBackgroundResource(R.drawable.detail_like);
                holder.mIsLike.setText(R.string.like02);
                holder.mIsLike.setTextColor(Color.parseColor("#ed232b"));
                holder.mLikeCount.setTextColor(Color.parseColor("#ed232b"));
            }
            holder.mLayout.setTag(position);
            holder.mLayout.setOnClickListener(mOnClickListener);

            //글쓴 아이디와 나의 아이디가 같다면 안보이게 설정.
            if (FanMindSetting.getNICK_NAME(mContext).equals(comment.getNick())) {
                //				holder.mReport.setVisibility(View.GONE);
                holder.mDel.setVisibility(View.VISIBLE);

            } else {
                //				holder.mReport.setVisibility(View.VISIBLE);
                holder.mDel.setVisibility(View.GONE);
            }
            //
            //			holder.mReport.setTag(position);
            //			holder.mReport.setOnClickListener(mOnClickListener);
            //
            holder.mDel.setTag(position);
            holder.mDel.setOnClickListener(mOnClickListener);

            //			holder.mLayout.setTag(position);
            //			holder.mLayout.setOnClickListener(mOnClickListener);

            holder.textView_comment_reply_all.setTag(position);
            holder.textView_comment_reply_all.setOnClickListener(mOnClickListener);


        }

        return convertView;
    }

    public class Holder {
        ImageView mProfile, mLike;
        TextView mNickName, mContents, mDate, mLikeCount, mIsLike, mReport, mDel, textView_comment_reply_all;
        RelativeLayout mLayout, layout_bottom, layout_non_secret;
        LinearLayout layout_secret;
    }
}
