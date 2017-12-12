package specup.fanmind.main.tab0;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.CommentAdapter2;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.CommentList2;

/**
 * 댓글
 */
public class AlertDialogComment extends DialogFragment {

    private View view;
    private Context context;
    private String sSrl, total_likes;

    private ListView listView;
    private TextView textView_comment;
    private CallbackManager callbackManager;
    private String total_Json;

    private int page = 0;
    private int loadCompletedCount = 0;
    /**
     * 종류(1 : 프로젝트 댓글, 3 : 프로젝트 후기 댓글, 4 : 뉴스피드 댓글, 5 : 타임라인 이미지 댓글, 6 : 이벤트 댓글, 8 : 프로젝트 공지사항 댓글)
     */
    private CommentAdapter2 commentAdapter2;
    private TextView textView_myReply;

    public AlertDialogComment(Context context, String sSrl, String total_likes, TextView textView_comment, CallbackManager callbackManager, String total_Json) {
        this.context = context;
        this.sSrl = sSrl;
        this.total_likes = total_likes;
        this.textView_comment = textView_comment;
        this.callbackManager = callbackManager;
        this.total_Json = total_Json;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.alertdialog_comment, container);

        setView();
        networkData("1");
        return view;
    }

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (page!=0 && (firstVisibleItem + visibleItemCount) == totalItemCount && loadCompletedCount != totalItemCount) {
                loadCompletedCount = totalItemCount;
                if (textView_myReply.getTag().equals("all")) {
                    networkData("1");
                }else{
                    getMyReply();


                }
            }
        }
    };


    private void setView() {

        ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnScrollListener(scrollListener);
        textView_myReply = (TextView) view.findViewById(R.id.textView_myReply);

        //좋아하는 사람들
        Button button_comment_like = (Button) view.findViewById(R.id.button_comment_like);
        button_comment_like.setText(getActivity().getString(R.string.like_list).replace("{수}", total_likes));

        //공개,비공개
        final CheckBox checkbox_secret = (CheckBox) view.findViewById(R.id.checkbox_secret);
        checkbox_secret.setChecked(false);

        //댓글 입력
        final EditText input_comment = (EditText) view.findViewById(R.id.input_comment);
        Utils.setEditTextFilter(input_comment);
        Button button_send = (Button) view.findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//댓글 입력
                if (FanMindSetting.getLOGIN_OK(getActivity())) {
                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                    mParams.add(new BasicNameValuePair("board", "1"));
                    mParams.add(new BasicNameValuePair("next", "0"));
                    mParams.add(new BasicNameValuePair("size", "10"));
                    mParams.add(new BasicNameValuePair("srl", sSrl));
                    mParams.add(new BasicNameValuePair("text", input_comment.getText().toString()));
                    mParams.add(new BasicNameValuePair("is_secret", checkbox_secret.isChecked() ? "Y" : "N"));
                    mParams = Utils.setSession(getActivity(), mParams);
                    HttpRequest.setHttp1(getActivity(), URLAddress.REPLY_WRITE(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("REPLY_WROTE")) {
                                        page = 0;
                                        commentAdapter2 = null;
                                        loadCompletedCount = 0;
                                        networkData("1");

                                    } else {
                                        Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                                    }
                                    input_comment.setText("");
                                }
                            }
                    );
                } else {
                    Utils.showDialog(getActivity());
                }

            }
        });

        //공유하기 버튼
        LinearLayout button_share = (LinearLayout) view.findViewById(R.id.layout_share);
        button_share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        AlertDialogShare dialog = new AlertDialogShare(context, total_Json, callbackManager);
                        dialog.show(fm, "dialog2");
                    }
                });

        //내 댓글 보기
        LinearLayout layout_my_comment = (LinearLayout) view.findViewById(R.id.layout_my_comment);
        layout_my_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView_myReply != null) {
                    System.out.println("textView_myReply.getTag():" + textView_myReply.getTag());
                    if (textView_myReply.getTag().equals("all")) {
                        page = 0;
                        commentAdapter2 = null;
                        loadCompletedCount = 0;
                        getMyReply();
                    } else {
                        page = 0;
                        commentAdapter2 = null;
                        loadCompletedCount = 0;
                        networkData("1");
                    }
                }
            }
        });
    }

    /**
     * 댓글 좋아요
     *
     * @param v
     */
    CommentList2 tempCommentList2;

    public void setCommentLike(View v) {
        final int position = (int) v.getTag();
        tempCommentList2 = (CommentList2) commentAdapter2.getItem(position);
        if (tempCommentList2.getIs_liked().equals("Y")) {//좋아요 취소
            //댓글(0), 서포트 전달예정(2), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5), 이벤트(6)
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("board", "0"));
            mParams.add(new BasicNameValuePair("srl", tempCommentList2.getReply_srl()));
            mParams = Utils.setSession(getActivity(), mParams);
            HttpRequest.setHttp1(getActivity(), URLAddress.LIKE_CANCEL(getActivity()), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {

                    if (Utils.getJsonData(result, "errcode").equals("2451")) {//성공
                        tempCommentList2.setIs_liked("N");
                        tempCommentList2.setTotal_likes(Utils.intToString(Utils.stringToInt(tempCommentList2.getTotal_likes()) - 1));
                        commentAdapter2.notifyDataSetChanged();
                    } else {
                        Utils.setToast(getActivity(), R.string.unlike_fail);
                    }
                }
            });

        } else {//좋아요
            //댓글(0), 서포트 전달예정(2), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5), 이벤트(6)
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("board", "0"));//1.0은 무조건 0으로
            mParams.add(new BasicNameValuePair("srl", tempCommentList2.getReply_srl()));
            mParams = Utils.setSession(getActivity(), mParams);
            HttpRequest.setHttp1(getActivity(), URLAddress.LIKE(getActivity()), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {

                    if (Utils.getJsonData(result, "errcode").equals("2401")) {//성공
                        tempCommentList2.setIs_liked("Y");
                        tempCommentList2.setTotal_likes(Utils.intToString(Utils.stringToInt(tempCommentList2.getTotal_likes()) + 1));
                        commentAdapter2.notifyDataSetChanged();
                    } else {
                        Utils.setToast(getActivity(), R.string.like_fail);
                    }
                }
            });
        }
    }

    public void reflash() {
        networkData("1");

    }

    /**
     * 내 댓글 가져오기
     */
    private void getMyReply() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
//        mParams.add(new BasicNameValuePair("total_replies", "0"));
        mParams.add(new BasicNameValuePair("size", "10"));
        mParams.add(new BasicNameValuePair("next", String.valueOf(page)));
        mParams.add(new BasicNameValuePair("pageable", "true"));
        mParams.add(new BasicNameValuePair("board", "1"));
        mParams.add(new BasicNameValuePair("srl", sSrl));
        HttpRequest.setHttp1(getActivity(), URLAddress.REPLY_MINE(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    textView_myReply.setText(getString(R.string.rightstar02));
                    textView_myReply.setTag("my");
                    List<Object> arraylist = new ArrayList<Object>();
                    JSONArray jsonArray_data = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                    for (int i = 0; i < jsonArray_data.length(); i++) {
                        String temp = jsonArray_data.getString(i);
                        CommentList2 commentList2 = new CommentList2();
                        commentList2.setNext_srl(Utils.getJsonData(temp, "next_srl"));
                        commentList2.setReply_srl(Utils.getJsonData(temp, "reply_srl"));
                        commentList2.setNick(Utils.getJsonData(temp, "nick"));
                        commentList2.setPic(Utils.getJsonData(temp, "pic"));
                        commentList2.setPic_base(Utils.getJsonData(temp, "pic_base"));
                        commentList2.setContent(Utils.getJsonData(temp, "text"));
                        commentList2.setTime(Utils.getJsonData(temp, "time"));
                        commentList2.setTotal_likes(Utils.getJsonData(temp, "total_likes"));
                        commentList2.setTotal_replies(Utils.getJsonData(temp, "total_replies"));
                        commentList2.setIs_secret(Utils.getJsonData(temp, "is_secret"));
                        commentList2.setIs_liked(Utils.getJsonData(temp, "is_liked"));
                        commentList2.setIs_mine(Utils.getJsonData(temp, "is_mine"));

                        arraylist.add(commentList2);
                    }
//                    CommentAdapter2 layout_my_comment = new CommentAdapter2(AlertDialogComment.this, getActivity(), arraylist, sSrl, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            switch (v.getId()) {
//
//                                case R.id.layout_like: {//댓글 좋아요 하기
//                                    setCommentLike(v);
//                                    break;
//                                }
//                            }
//                        }
//                    });
//                    listView.setAdapter(layout_my_comment);
//                    layout_my_comment.notifyDataSetChanged();

                    if (commentAdapter2 == null) {

                        commentAdapter2 = new CommentAdapter2(AlertDialogComment.this, getActivity(), arraylist, sSrl, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.layout_like: {//댓글 좋아요 하기
                                        setCommentLike(v);
                                    }
                                    break;
                                }
                            }
                        });
                        listView.setAdapter(commentAdapter2);
                    } else {
                        commentAdapter2.add(arraylist);
                    }
                    page += 1;

                }else{
                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    /**
     * 댓글 가져오기
     *
     * @param type
     */
    public void networkData(String type) {


        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("size", "10"));
        mParams.add(new BasicNameValuePair("next", String.valueOf(page)));
        mParams.add(new BasicNameValuePair("pageable", "true"));
        mParams.add(new BasicNameValuePair("board", type));
        mParams.add(new BasicNameValuePair("srl", sSrl));
        HttpRequest.setHttp1(getActivity(), URLAddress.REPLY_ALL(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                            textView_myReply.setText(getString(R.string.my_comment));
                            textView_myReply.setTag("all");

                            List<Object> arraylist_CommentList2 = new ArrayList<Object>();
                            JSONArray jsonArray_data = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                            textView_comment.setText(Utils.getJsonData(Utils.getJsonData(result, "data"), "total"));
                            for (int i = 0; i < jsonArray_data.length(); i++) {
                                String temp = jsonArray_data.getString(i);

                                CommentList2 commentList2 = new CommentList2();
                                commentList2.setNext_srl(Utils.getJsonData(temp, "next_srl"));
                                commentList2.setReply_srl(Utils.getJsonData(temp, "reply_srl"));
                                commentList2.setNick(Utils.getJsonData(temp, "nick"));
                                commentList2.setPic(Utils.getJsonData(temp, "pic"));
                                commentList2.setPic_base(Utils.getJsonData(temp, "pic_base"));
                                commentList2.setContent(Utils.getJsonData(temp, "text"));
                                commentList2.setTime(Utils.getJsonData(temp, "time"));
                                commentList2.setTotal_likes(Utils.getJsonData(temp, "total_likes"));
                                commentList2.setTotal_replies(Utils.getJsonData(temp, "total_replies"));
                                commentList2.setIs_secret(Utils.getJsonData(temp, "is_secret"));
                                commentList2.setIs_liked(Utils.getJsonData(temp, "is_liked"));
                                commentList2.setIs_mine(Utils.getJsonData(temp, "is_mine"));

                                arraylist_CommentList2.add(commentList2);
                            }


                            if (commentAdapter2 == null) {

                                commentAdapter2 = new CommentAdapter2(AlertDialogComment.this, getActivity(), arraylist_CommentList2, sSrl, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (v.getId()) {
                                            case R.id.layout_like: {//댓글 좋아요 하기
                                                setCommentLike(v);
                                            }
                                            break;
                                        }
                                    }
                                });
                                listView.setAdapter(commentAdapter2);
                            } else {
                                commentAdapter2.add(arraylist_CommentList2);
                            }
                            page += 1;
                        } else {
                            Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }
        );
    }

}