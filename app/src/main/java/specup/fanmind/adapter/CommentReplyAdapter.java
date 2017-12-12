package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.CommentList2;

/**
 * 답글 달기
 */
public class CommentReplyAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sSrl;
    private View.OnClickListener onClickListener;

    public CommentReplyAdapter(Context context, List<Object> mList, String sSrl, View.OnClickListener onClickListener) {
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
        convertView = layout_In.inflate(R.layout.list_comment_reply, null);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.listcomment_profile = (ImageView) convertView.findViewById(R.id.listcomment_profile);
        holder.imageView_secret = (ImageView) convertView.findViewById(R.id.imageView_secret);
        holder.textView_content = (TextView) convertView.findViewById(R.id.textView_content);
        holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
        holder.textView_time = (TextView) convertView.findViewById(R.id.textView_time);

        final CommentList2 commentList2 = (CommentList2) mList.get(position);


        HttpRequest.getNetworkCircleImage(mContext,commentList2.getPic_base() + commentList2.getPic(), holder.listcomment_profile);
        holder.textView_nickName.setText(commentList2.getNick());
        holder.textView_content.setText(commentList2.getContent());

        //secret
        if (commentList2.getIs_secret().equals("Y")) {
            holder.imageView_secret.setBackgroundResource(R.drawable.a1200_6_close);
        } else {
            holder.imageView_secret.setBackgroundResource(R.drawable.a1200_6_open);
        }

        holder.textView_time.setText(commentList2.getTime());

        holder.textView_delete = (TextView) convertView.findViewById(R.id.textView_delete);
        holder.textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCommentDialog(position);
            }
        });


        //글쓴 아이디와 나의 아이디가 같다면 안보이게 설정.
        if (FanMindSetting.getNICK_NAME(mContext).equals(commentList2.getNick())) {
            holder.textView_delete.setVisibility(View.VISIBLE);

        } else {
            holder.textView_delete.setVisibility(View.GONE);
        }


        convertView.setTag(holder);


        return convertView;

    }


    public static CustomDialog mDialog;


    /**
     * 삭제 다이얼로그
     */
    public void deleteCommentDialog(final int position) {
        String title = mContext.getString(R.string.alert);
        String content = mContext.getString(R.string.replydel);
        String left = mContext.getString(R.string.confirmation);//확인
        String right = mContext.getString(R.string.cancel);//취소
        mDialog = new CustomDialog(mContext, title, content, new View.OnClickListener() {//left
            @Override
            public void onClick(View v) {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("reply_srl", ((CommentList2) mList.get(position)).getReply_srl()));
                mParams = Utils.setSession(mContext, mParams);
                HttpRequest.setHttp1(mContext, URLAddress.REPLY_DELETE(), mParams, new OnTask() {
                            @Override
                            public void onTask(int output, String result) throws JSONException {
                                if (Utils.getJsonData(result, "code").equals("DELETED")) {
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Utils.setToast(mContext, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                                }
                            }
                        }
                );
                mDialog.dismiss();
            }
        }, new View.OnClickListener() {//right
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        }, left, right);
        mDialog.show();
    }

    public class Holder {
        ImageView listcomment_profile, imageView_secret;
        TextView textView_nickName, textView_content, textView_delete,
                textView_time, textView_recomment_reply, textView_comment_reply_all;
    }
}
