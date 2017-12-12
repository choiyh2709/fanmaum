package specup.fanmind.main.tab0;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.CommentAdapter3;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.CommentList2;

/**
 * QnA
 */
public class AlertDialogQnA extends DialogFragment {

    View view;
    Context context;
    String sSrl;
    List<Object> arraylist_CommentList2;
    ListView listView;

    public AlertDialogQnA(Context context, String sSrl) {
        this.context = context;
        this.sSrl = sSrl;
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
        view = inflater.inflate(R.layout.alertdialog_qna, container);

        setView();

        networkData("9");

        return view;
    }

    private void setView() {

        ImageView button_back = (ImageView) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);


        //공개,비공개
        final CheckBox checkbox_secret = (CheckBox) view.findViewById(R.id.checkbox_secret);
        checkbox_secret.setChecked(false);

        //댓글 입력
        final EditText input_comment = (EditText) view.findViewById(R.id.input_comment);
        Button button_send = (Button) view.findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//댓글 입력

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("board", "9"));
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
                                    networkData("9");
                                } else {
                                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                                }
                                input_comment.setText("");

                            }
                        }
                );
            }
        });


    }

    /**
     * 종류(1 : 프로젝트 댓글, 3 : 프로젝트 후기 댓글, 4 : 뉴스피드 댓글, 5 : 타임라인 이미지 댓글, 6 : 이벤트 댓글, 8 : 프로젝트 공지사항 댓글)
     */
    public void networkData(String type) {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("total_replies", "0"));
//        mParams.add(new BasicNameValuePair("size", "10"));
        mParams.add(new BasicNameValuePair("size", "100"));
        mParams.add(new BasicNameValuePair("board", type));
        mParams.add(new BasicNameValuePair("srl", sSrl));
        HttpRequest.setHttp1(getActivity(), URLAddress.REPLY_ALL(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                            arraylist_CommentList2 = new ArrayList<Object>();
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

                                arraylist_CommentList2.add(commentList2);
                            }


                            CommentAdapter3 commentAdapter3 = new CommentAdapter3(AlertDialogQnA.this, context, arraylist_CommentList2, sSrl, new View.OnClickListener() {
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
                            listView.setAdapter(commentAdapter3);
                        } else {
                            Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }

        );
    }

    /**
     * 댓글 좋아요
     *
     * @param v
     */
    public void setCommentLike(View v) {
        final int position = (int) v.getTag();
        if (((CommentList2) arraylist_CommentList2.get(position)).getIs_liked().equals("Y")) {//좋아요 취소

            //댓글(0), 서포트 전달예정(2), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5), 이벤트(6)
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("board", "9"));
            mParams.add(new BasicNameValuePair("srl", sSrl));
            mParams = Utils.setSession(getActivity(), mParams);
            HttpRequest.setHttp1(getActivity(), URLAddress.LIKE_CANCEL(getActivity()), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {

                    if (Utils.getJsonData(result, "errcode").equals("2451")) {//성공
                        networkData("9");
                    } else {
                        Utils.setToast(getActivity(), R.string.unlike_fail);
                    }
                }
            });

        } else {//좋아요

            //댓글(0), 서포트 전달예정(2), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5), 이벤트(6)
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("board", "9"));
            mParams.add(new BasicNameValuePair("srl", sSrl));
            mParams = Utils.setSession(getActivity(), mParams);
            HttpRequest.setHttp1(getActivity(), URLAddress.LIKE(getActivity()), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {

                    if (Utils.getJsonData(result, "errcode").equals("2451")) {//성공
                        networkData("9");
                    } else {
                        Utils.setToast(getActivity(), R.string.like_fail);
                    }
                }
            });
        }
    }

    public void reflash() {
        networkData("9");

    }
}