package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.CommentList2;

/**
 *  좋아하는 사람들
 */
public class CommentLikerAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sSrl;
    private View.OnClickListener onClickListener;

    public CommentLikerAdapter(Context context, List<Object> mList, String sSrl, View.OnClickListener onClickListener) {
        this.mList = mList;
        this.sSrl = sSrl;
        this.onClickListener = onClickListener;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(CommentList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Holder holder = new Holder();
        convertView = layout_In.inflate(R.layout.list_comment_liker, null);
        holder.listcomment_profile = (ImageView) convertView.findViewById(R.id.listcomment_profile);
        holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
//
        CommentList2 commentList2 = (CommentList2) mList.get(position);
        HttpRequest.getNetworkCircleImage(mContext,commentList2.getPic_base() + commentList2.getPic(), holder.listcomment_profile);
        holder.textView_nickName.setText(commentList2.getNick());

        convertView.setTag(holder);


        return convertView;

    }


    public static CustomDialog mDialog;



    public class Holder {
        ImageView listcomment_profile, layout_like_heart, imageView_secret;
        TextView textView_nickName, textView_content,
                layout_like_text, layout_like_number,
                textView_time, textView_recomment_reply, textView_comment_reply_all;
        LinearLayout layout_like;
    }
}
