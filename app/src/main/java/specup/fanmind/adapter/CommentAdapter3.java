package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Color;
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
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.main.tab0.AlertDialogQnA;
import specup.fanmind.main.tab0.AlertDialogQnAReply;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.CommentList2;

/**
 * 댓글 달기
 */
public class CommentAdapter3 extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sSrl;
    private View.OnClickListener onClickListener;
    private AlertDialogQnA alertDialogQnA;
    public CommentAdapter3(AlertDialogQnA alertDialogQnA , Context context, List<Object> mList, String sSrl, View.OnClickListener onClickListener) {
        this.mList = mList;
        this.sSrl = sSrl;
        this.onClickListener = onClickListener;
        this.alertDialogQnA = alertDialogQnA;
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
        convertView = layout_In.inflate(R.layout.list_comment2, null);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.listcomment_profile = (ImageView) convertView.findViewById(R.id.listcomment_profile);
        holder.layout_like_heart = (ImageView) convertView.findViewById(R.id.layout_like_heart);
        holder.imageView_secret = (ImageView) convertView.findViewById(R.id.imageView_secret);
        holder.textView_content = (TextView) convertView.findViewById(R.id.textView_content);
        holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
        holder.layout_like_text = (TextView) convertView.findViewById(R.id.layout_like_text);
        holder.layout_like_number = (TextView) convertView.findViewById(R.id.layout_like_number);
        holder.textView_time = (TextView) convertView.findViewById(R.id.textView_time);

        final CommentList2 commentList2 = (CommentList2) mList.get(position);

        //답글달기
        holder.textView_recomment_reply = (TextView) convertView.findViewById(R.id.textView_recomment_reply);
        holder.textView_recomment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogQnAReply alertDialogQnAReply = new AlertDialogQnAReply(alertDialogQnA,mContext, commentList2, (((CommentList2) mList.get(position)).getReply_srl()));
                alertDialogQnAReply.show(ActivityManager.fragmentManager, "commentReply");

            }
        });

        HttpRequest.getNetworkCircleImage(mContext,commentList2.getPic_base() + commentList2.getPic(), holder.listcomment_profile);
        holder.textView_nickName.setText(commentList2.getNick());
        holder.textView_content.setText(commentList2.getContent());

        holder.layout_like = (LinearLayout) convertView.findViewById(R.id.layout_like);
        holder.layout_like.setTag(position);
        holder.layout_like.setOnClickListener(onClickListener);


        //secret
        holder.layout_secret = (LinearLayout) convertView.findViewById(R.id.layout_secret);
        holder.layout_non_secret = (LinearLayout) convertView.findViewById(R.id.layout_non_secret);

        if (commentList2.getIs_secret().equals("Y")) {

            holder.layout_secret.setVisibility(View.VISIBLE);
            holder.layout_non_secret.setVisibility(View.GONE);
            holder.imageView_secret.setBackgroundResource(R.drawable.a1200_6_close);
        } else {
            holder.layout_secret.setVisibility(View.GONE);
            holder.layout_non_secret.setVisibility(View.VISIBLE);
            holder.imageView_secret.setBackgroundResource(R.drawable.a1200_6_open);
        }

        //좋아요
        if (commentList2.getIs_liked().equals("Y")) {
            holder.layout_like_text.setTextColor(Color.parseColor("#ed232b"));
            holder.layout_like_number.setTextColor(Color.parseColor("#ed232b"));
            holder.layout_like_heart.setBackgroundResource(R.drawable.a1200_7_like_active);
        } else {
            holder.layout_like_text.setTextColor(Color.parseColor("#888888"));
            holder.layout_like_number.setTextColor(Color.parseColor("#888888"));
            holder.layout_like_heart.setBackgroundResource(R.drawable.a1200_7_like_normal);
        }
        holder.layout_like_number.setText(commentList2.getTotal_likes());


        holder.textView_time.setText(commentList2.getTime());

        //답글 모두 보기
        holder.textView_comment_reply_all = (TextView) convertView.findViewById(R.id.textView_comment_reply_all);
        holder.textView_comment_reply_all.setText(mContext.getString(R.string.comment_reply_all).replace("{수}", commentList2.getTotal_replies()));
        holder.textView_comment_reply_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogQnAReply alertDialogQnAReply = new AlertDialogQnAReply(alertDialogQnA,mContext, commentList2, sSrl);
                alertDialogQnAReply.show(ActivityManager.fragmentManager, "commentReply");
            }
        });



        holder.textView_delete = (TextView) convertView.findViewById(R.id.textView_delete);
        holder.textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCommentDialog(position);
            }
        });

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
        ImageView listcomment_profile, layout_like_heart, imageView_secret;
        TextView textView_nickName, textView_content, textView_delete,
                layout_like_text, layout_like_number,
                textView_time, textView_recomment_reply, textView_comment_reply_all;
        LinearLayout layout_like, layout_secret, layout_non_secret;
    }
}
