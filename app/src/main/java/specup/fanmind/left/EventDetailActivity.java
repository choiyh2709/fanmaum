package specup.fanmind.left;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.EventCommentAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.ProjectStack;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.login.OAuthID;
import specup.fanmind.main.tab2_.NewsFeedImageActivity;
import specup.fanmind.vo.CommentList2;
import specup.fanmind.vo.ProjectStackVO;
import twitter4j.Status;
import twitter4j.auth.AccessToken;


public class EventDetailActivity extends Activity implements OnTask {

    //서버통신에 필요한 정보.
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;

    String mSrl, mBoard = "6", mMode = "A";
    View mHeader, mFooter;
    TextView mTitle, mDate, mLikeCount, textView_share, textView_result_view;
    ListView mList;
    String support_srl, starSrl;
    //코멘트 리스트.
    ArrayList<CommentList2> mCommentList;
    EventCommentAdapter mComment;
    int mReplyCount;
    ImageView mLoading;
    LinearLayout mImageLayout;
    boolean isNoti;
    RelativeLayout mHideLayout;
    EditText mEtMind, mEtInput;
    TextView mTotalMind, mExtraMind, mNowMind, mWarningTv, mPeoPle, mMyMind;
    Button mUploadBtn, mSendMind, mLikeBtn, mWarningBtn;
    boolean mLockListView, isMore, isLoader, isEnd;
    JSONObject total_json;
    Handler handler = new Handler();
    private static ProjectStack projectStack;
    private boolean isReview;
    private String isHistory;

    private CheckBox checkbox_secret;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "Event Detail");
        Utils.setAnalyticsTracker(getIntent(), this);

        ActivityManager.getInstance().addActivity(this);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_eventdetail);

        mEtInput = (EditText) findViewById(R.id.input_comment);

        Intent intent = getIntent();
        isNoti = intent.getBooleanExtra("isNoti", false);
        isEnd = intent.getBooleanExtra("end", false);
        mSrl = intent.getStringExtra("srl");
        mSrl = intent.getStringExtra("reply_on") != null ? intent.getStringExtra("reply_on") : mSrl; //댓글 푸쉬 였을때 사용
        starSrl = intent.getStringExtra("star_srl");

        if (savedInstanceState == null) {
            projectStack = new ProjectStack();
            ProjectStackVO projectStackVO = new ProjectStackVO();
            projectStackVO.setSrl(mSrl);

            projectStack.push(projectStackVO);
        }

        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("event_srl", mSrl));
        HttpRequest.setHttp1(this, URLAddress.EVENT_DETAIL2(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                getJsonDetail(result);
            }
        });
    }

    private void getJsonDetail(String result) {
        try {
            if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                total_json = new JSONObject(Utils.getJsonData(result, "data"));
                sTitle = Utils.getJsonData(total_json.toString(), "title");
                sText = Utils.getJsonData(total_json.toString(), "present");
                String thumbnail = Utils.getJsonData(total_json.toString(), "thumbnail");
                String thumbnail_base = Utils.getJsonData(total_json.toString(), "thumbnail_base");
                sThumbNail = thumbnail_base + thumbnail;
                sBeginTime = Utils.getJsonData(total_json.toString(), "begin_time");
                sEndTime = Utils.getJsonData(total_json.toString(), "close_time");
                //            sSupportCnt = total_json.getString("support_cnt");
                sWarning = Utils.getJsonData(total_json.toString(), "warning");

                isHistory = Utils.getJsonData(total_json.toString(), "result").equals("") ? "N" : "Y";
            } else {
                Utils.setToast(EventDetailActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setViewN();
    }


    private void setViewN() { //일반 이벤트
        mLockListView = true;

        mList = (ListView) findViewById(R.id.eventdetail_list);
        mHeader = getLayoutInflater().inflate(R.layout.header_eventdetail, null);
        mTitle = (TextView) mHeader.findViewById(R.id.eventdetail_tv01);
        mDate = (TextView) mHeader.findViewById(R.id.eventdetail_tv02);
        mLikeCount = (TextView) mHeader.findViewById(R.id.eventdetail_tv06);
        mImageLayout = (LinearLayout) mHeader.findViewById(R.id.eventdetail_imageLayout);
        mFooter = getLayoutInflater().inflate(R.layout.footer_layout, null);
        mLoading = (ImageView) mFooter.findViewById(R.id.footer_image);

        mLikeBtn = (Button) mHeader.findViewById(R.id.eventdetail_btn01);
        textView_share = (TextView) mHeader.findViewById(R.id.textView_share);
        textView_result_view = (TextView) mHeader.findViewById(R.id.textView_result_view);

        // mTitle =(TextView)findViewById(R.id.eventdetail_tv01);
        // mDate =(TextView)findViewById(R.id.eventdetail_tv02);
        mList.addHeaderView(mHeader);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = TagToURL(((CommentList2) mComment.getItem(position - 1)).getContent());
                if (!url.equals("")) {
                    Intent intent = new Intent(EventDetailActivity.this, NewsFeedImageActivity.class);
                    String[] arrayUrl = new String[1];
                    arrayUrl[0] = url;
                    intent.putExtra("image", arrayUrl);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }


            }
        });
        mCommentList = new ArrayList<CommentList2>();
        mComment = new EventCommentAdapter(this, mCommentList, mClick);
        mList.setAdapter(mComment);
        mList.setOnScrollListener(scrollListener);
        checkbox_secret = (CheckBox) findViewById(R.id.checkbox_secret);

        setInfo();
        LoadMore();
    }

    public static String TagToURL(String tagStr) {
        String urlStr = "";

        try {
            int src_point, start_url, end_url;
            src_point = tagStr.indexOf("src", 1);

            start_url = tagStr.indexOf("http", src_point);
            end_url = tagStr.indexOf(" style", start_url);

            urlStr = tagStr.substring(start_url, end_url);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return urlStr;
    }


    public void onUpload(View v) {
        uploadReply();
    }

    public void onBack(View v) {
        onBack();
    }

    private void onBack() {
        if (isNoti) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        ActivityManager.getInstance().deleteActivity(this);

        if (projectStack != null) {
            ProjectStackVO projectStackValue = projectStack.pop();
            if (projectStackValue != null) {
                Intent intent = new Intent(EventDetailActivity.this, EventDetailActivity.class);
                intent.putExtra("srl", projectStackValue.getSrl());
                intent.putExtra("review", projectStackValue.isReview());
                intent.putExtra("isHistory", projectStackValue.getIsHistory());
                setIntent(intent);
                startActivity(intent);
            } else {

                finish();
            }
        }
    }


    private void uploadReply() {
        if (FanMindSetting.getLOGIN_OK(EventDetailActivity.this)) {
            if (mEtInput.length() != 0) {

                mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("board", "11"));
                mParams.add(new BasicNameValuePair("srl", Utils.getJsonData(total_json.toString(), "event_srl")));
                mParams.add(new BasicNameValuePair("text", mEtInput.getText().toString().trim()));
                mParams.add(new BasicNameValuePair("is_secret", checkbox_secret.isChecked() ? "Y" : "N"));
                mParams = Utils.setSession(this, mParams);
                HttpRequest.setHttp1(EventDetailActivity.this, URLAddress.REPLY_WRITE2(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {

                        if (Utils.getJsonData(result, "code").equals("REPLY_WROTE")) {
                            page = 0;
                            if (mCommentList != null)
                                mCommentList.removeAll(mCommentList);

                            LoadMore();
                        } else {
                            Utils.setToast(EventDetailActivity.this, Utils.getJsonData(result, "message"));
                        }
                        mEtInput.setText("");
                    }
                });

            } else {
                Utils.setToast(this, R.string.inputwindow);
            }
        } else {
            Utils.showDialog(this);
        }
    }

    /**
     * 댓글 신고하기
     *
     * @param v
     */
    private void onReplyReport(View v) {
        int position = (Integer) v.getTag();
        mParams = Utils.reportReply(this, mParams, mCommentList.get(position).getReply_srl());
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.REPLY_REPORT(this), AsyncTaskValue.REPLY_REPORT, this);
    }

    CustomDialog mDialog;

    public void showDialog(Context context, String title, String content, String left, String right, final View view,
                           final boolean isReport) {
        mDialog = new CustomDialog(context, title, content, new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReport) {
                    onReplyReport(view);
                } /*
                     * else{ delReply(view); }
					 */
                mDialog.dismiss();
            }
        }, left, right);
        mDialog.show();
    }

    /**
     * 댓글 신고하기 팝업
     */
    private void supportSingo(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
            String title = getString(R.string.report00);
            String content = getString(R.string.report01);
            String left = getString(R.string.cancel);
            String right = getString(R.string.confirmation);
            showDialog(this, title, content, left, right, v, true);
        } else {
//            Utils.showDialog(this);
        }
    }

    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
//                case R.id.listcomment_tv06: // �Ű��ϱ�
//                    // onReplyReport(v);
//                    supportSingo(v);
//                    break;
                case R.id.layout_bottom: // 좋아요 /좋아요 취소
                    onReplyLike(v);
                    break;
                case R.id.textView_delete: // �����ϱ�
                    deleteCommentDialog(v);
                    break;
                case R.id.textView_comment_reply_all: //  댓글 보기
                    int position = (Integer) v.getTag();
                    CommentList2 commentList2 = mCommentList.get(position);
                    EventAlertCommentReply alertDialogCommentReply = new EventAlertCommentReply(EventDetailActivity.this, commentList2, (commentList2.getReply_srl()));
                    alertDialogCommentReply.show(getFragmentManager(), "eventCommentReply");
                    break;
            }
        }
    };

    int mLike;
    String mReplyBoard = "0";

    /**
     * 리플 좋아요
     *
     * @param v
     */
    CommentList2 tempCommentList;

    private void onReplyLike(View v) {
        mLike = (Integer) v.getTag();
        tempCommentList = mCommentList.get(mLike);
        if (tempCommentList.getIs_liked().equals("Y")) {

            mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("reply_srl", tempCommentList.getReply_srl()));
            mParams = Utils.setSession(this, mParams);
            HttpRequest.setHttp1(this, URLAddress.REPLY_UNLIKE(), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {
                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {//성공
                        tempCommentList.setIs_liked("N");
                        tempCommentList.setTotal_likes(Utils.getJsonData(Utils.getJsonData(result, "data"), "total_likes"));
                        mComment.notifyDataSetChanged();
                    } else {
                        Utils.setToast(EventDetailActivity.this, Utils.getJsonData(result, "message"));
                    }
                }
            });
        } else {
            mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("reply_srl", tempCommentList.getReply_srl()));
            mParams = Utils.setSession(this, mParams);
            HttpRequest.setHttp1(this, URLAddress.REPLY_LIKE(), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {
                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {//성공
                        tempCommentList.setIs_liked("Y");
                        tempCommentList.setTotal_likes(Utils.getJsonData(Utils.getJsonData(result, "data"), "total_likes"));
                        mComment.notifyDataSetChanged();
                    } else {
                        Utils.setToast(EventDetailActivity.this, Utils.getJsonData(result, "message"));
                    }
                }
            });
        }
    }

    /**
     * 삭제 다이얼로그
     */
    public void deleteCommentDialog(final View v1) {
        String title = getString(R.string.alert);
        String content = getString(R.string.replydel);
        String left = getString(R.string.confirmation);//확인
        String right = getString(R.string.cancel);//취소
        mDialog = new CustomDialog(EventDetailActivity.this, title, content, new View.OnClickListener() {//left
            @Override
            public void onClick(View v) {
                onReplyDel(v1);
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

    /**
     * 댓글 삭제
     *
     * @param v
     */
    private void onReplyDel(View v) {
        int del = (Integer) v.getTag();
        mParams = Utils.delReply(this, mParams, mCommentList.get(del).getReply_srl());
//        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.REPLY_DELETE(this), AsyncTaskValue.REPLY_DELETE, this);

        HttpRequest.setHttp1(this, URLAddress.REPLY_DELETE2(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("DELETED")) {//성공
                    page = 0;
                    if (mCommentList != null)
                        mCommentList.removeAll(mCommentList);

                    LoadMore();
                } else {
                    Utils.setToast(EventDetailActivity.this, Utils.getJsonData(result, "message"));
                }
            }
        });
    }

    private void setInfo() {
        mTitle.setText(sTitle);
        String sdate = Utils.chageDate(sBeginTime);
        String ddate = Utils.chageDate(sEndTime);

        String date = getString(R.string.listsupport08).replace("{SDATE}", sdate).replace("{DDATE}", ddate);
        mDate.setText(date);

        WebView webview_eventdetail = (WebView) findViewById(R.id.webview_eventdetail);
        webview_eventdetail.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

        });
        webview_eventdetail.setWebChromeClient(new WebChromeClient());

        if (isReview) {
            webview_eventdetail.loadUrl(URLAddress.EVENT_DETAIL_EVENTS_RESULT(mSrl, 340));
        } else {
            webview_eventdetail.loadUrl(URLAddress.EVENT_DETAIL_EVENTS(mSrl, 340));

        }

        textView_share.setOnClickListener(onClickListener);
        textView_result_view.setOnClickListener(onClickListener);


        try {
            if (isReview) {
                textView_result_view.setVisibility(View.GONE);
                TextView textView_title = (TextView) findViewById(R.id.title);
                textView_title.setText(R.string.event_result);

            } else {
                textView_result_view.setVisibility(View.VISIBLE);
                TextView textView_title = (TextView) findViewById(R.id.title);
                textView_title.setText(R.string.left03);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textView_share: {
                    if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                        FragmentManager fm = getFragmentManager();
                        EventAlertDialogShare dialog = new EventAlertDialogShare(EventDetailActivity.this, total_json.toString(), callbackManager);
                        dialog.show(fm, "dialog1");
                    } else {
                        Utils.showDialog(EventDetailActivity.this);
                    }
                }
                break;
                case R.id.textView_result_view: {

                    if (isHistory.equals("Y")) {
                        ProjectStackVO projectStackVO = new ProjectStackVO();
                        projectStackVO.setSrl(mSrl);
                        projectStackVO.setReview(true);
                        projectStackVO.setIsHistory(isHistory);
                        projectStack.push(projectStackVO);

                        Intent intent = new Intent(EventDetailActivity.this, EventDetailActivity.class);
                        intent.putExtra("review", true);
                        intent.putExtra("srl", mSrl);
                        intent.putExtra("isHistory", isHistory);
                        startActivity(intent);
                    } else {
                        String title = getString(R.string.alert);
                        String content = getString(R.string.before_review2);
                        String left = getString(R.string.confirmation);
                        mDialog = new CustomDialog(EventDetailActivity.this, title, content, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, left);
                        mDialog.show();
                    }


                }
            }
        }
    };


    public CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, responseCode, data);
            if (responseCode == RESULT_OK) {
                Utils.setEventShareSnsIndex(this, Utils.getJsonData(total_json.toString(), "event_srl"), "facebook");
                Toast.makeText(this, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.REPLY_LIST_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonDataReply(result);
            }
        } else if (output == AsyncTaskValue.LIKE_NUM) {
            if (Utils.getJsonDataString(result).equals("2401")) {
                sMylike = "1";
                int count = Utils.subStringInt(sLikes) + 1;
                sLikes = String.valueOf(count);
                mLikeBtn.setBackgroundResource(R.drawable.detail_btn06_on);
                mLikeCount.setText(getString(R.string.likenumber).replace("{NUMBER}", Utils.getMoney(sLikes)));
                Utils.setToast(this, R.string.likeok);
            }
        } else if (output == AsyncTaskValue.LIKE_CANCEL_NUM) {
            if (Utils.getJsonDataString(result).equals("2451")) {
                sMylike = "0";
                int count = Utils.subStringInt(sLikes) - 1;
                sLikes = String.valueOf(count);
                mLikeBtn.setBackgroundResource(R.drawable.detail_btn06);
                mLikeCount.setText(getString(R.string.likenumber).replace("{NUMBER}", Utils.getMoney(sLikes)));
                Utils.setToast(this, R.string.likecancel);
            }
        } else if (output == AsyncTaskValue.REPLY_WRITE_NUM) {
            if (Utils.getJsonData(result)) {
                // Utils.setToast(this, R.string.okreply);
                if (mCommentList != null)
                    mCommentList.removeAll(mCommentList);
                mEtInput.setText("");
                LoadMore();
            }
        } else if (output == AsyncTaskValue.REPLY_REPORT_NUM) {
            if (Utils.getJsonDataString(result).equals("2501")) {
                Utils.setToast(this, R.string.reportok);
            }
        } else if (output == AsyncTaskValue.REPLY_LIKE_CANCEL_NUM) {
            if (Utils.getJsonDataString(result).equals("2451")) {
                mCommentList.get(mLike).setIs_liked("Y");
                int count = Utils.subStringInt(mCommentList.get(mLike).getTotal_likes()) - 1;
                mCommentList.get(mLike).setTotal_likes(String.valueOf(count));
                mComment.notifyDataSetChanged();
                Utils.setToast(this, R.string.likecancel);
            }
        } else if (output == AsyncTaskValue.REPLY_LIKE_NUM) {
            if (Utils.getJsonDataString(result).equals("2401")) {
                mCommentList.get(mLike).setIs_liked("1");
                int count = Utils.subStringInt(mCommentList.get(mLike).getTotal_likes()) + 1;
                mCommentList.get(mLike).setTotal_likes(String.valueOf(count));
                mComment.notifyDataSetChanged();
                Utils.setToast(this, R.string.likeok);
            }
        } else if (output == AsyncTaskValue.SUPPORT_SEND_MIND_NUM) {
            if (Utils.getJsonDataString(result).equals("2251")) {
                getJsonDataMind(result);
            }
        }
    }

    private void getJsonDataMind(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String myheart = json.getString("my_heart");
            String sHearNow = json.getString("heart_now");
            String send_heart = json.getString("send_heart");
            String getHeart = mMyMind.getText().toString().substring(0, mMyMind.getText().toString().length() - 2);
            int sum = Integer.parseInt(getHeart) + Integer.parseInt(send_heart);
            sSupportCnt = json.getString("support_cnt");
            FanMindSetting.setMY_HEART(this, myheart);
//            if (((LeftMenuFragment) LeftMenuFragment.mLeftContext) != null)
//                ((LeftMenuFragment) LeftMenuFragment.mLeftContext).mMindTv.setText(Utils.getMoney(myheart));
            mMyMind.setText(getString(R.string.listsupport01).replace("{ENTRY}", Utils.getMoney(String.valueOf(sum))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // int count = Integer.parseInt(sSupportCnt)+1;
        // sSupportCnt = String.valueOf(count);
        mPeoPle.setText(getString(R.string.listsupportdetail02).replace("{PEOPLE}", Utils.getMoney(sSupportCnt)));
        mEtMind.setText("");
        mHideLayout.setVisibility(View.GONE);
        mNowMind.setText(FanMindSetting.getMY_HEART(this));
        Utils.setToast(this, R.string.listsupport09);
    }

    /**
     * 댓글 리스트 가져오기
     *
     * @param result
     */
    private void getJsonDataReply(String result) {
        mReplyCount = 0;
        isLoader = false;
//        mCommentList.removeAll(mCommentList);

        try {
            JSONArray jsonArray = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
            for (int i = 0; i < jsonArray.length(); i++) {
                CommentList2 commentList = new CommentList2();
                mReplyCount++;
                commentList.setReply_srl(jsonArray.getJSONObject(i).getString("reply_srl"));
                commentList.setNick(jsonArray.getJSONObject(i).getString("nick"));
                commentList.setPic(jsonArray.getJSONObject(i).getString("pic"));
                commentList.setPic_base(jsonArray.getJSONObject(i).getString("pic_base"));
                commentList.setTime(jsonArray.getJSONObject(i).getString("time"));
                commentList.setContent(jsonArray.getJSONObject(i).getString("text"));
                commentList.setTotal_likes(jsonArray.getJSONObject(i).getString("total_likes"));
                commentList.setIs_liked(jsonArray.getJSONObject(i).getString("is_liked"));
                commentList.setIs_mine(jsonArray.getJSONObject(i).getString("is_mine"));
                commentList.setIs_secret(jsonArray.getJSONObject(i).getString("is_secret"));
                mCommentList.add(commentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mReplyCount == 20)
            isLoader = true;
        page += 1;
        mComment.notifyDataSetChanged();
        mLockListView = false;
    }

    String sTitle, sBeginTime, sEndTime, sEnterHeart, sThumbNail, sSupportCnt, sText, sWarning, sType;
    String sLikes, sMylike, sImg;


    /**
     * 마음보내기
     *
     * @param v
     */
    public void onClick(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
            sendMind();
        } else {
//            Utils.showDialog(this);
        }
    }

    /**
     * 마음보내기
     */
    private void sendMind() {
        if (mHideLayout.getVisibility() == View.GONE) {
            mHideLayout.setVisibility(View.VISIBLE);
        } else {
            if (Utils.checkLength(mEtMind)) {
                Utils.setToast(this, R.string.supportdetail01);
            } else if (Utils.subStringInt(mEtMind.getText().toString()) == 0) {
                Utils.setToast(this, R.string.supportdetail02);
            } else if (Utils.subStringInt(mExtraMind.getText().toString()) >= 0) {
                mParams = Utils.sendMind(this, mParams, mBoard, mSrl, mEtMind.getText().toString());
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.SUPPORT_SEND_MIND(this),
                        AsyncTaskValue.SUPPORT_SEND_MIND, this);
            } else {
                Utils.setToast(this, R.string.notmind);
            }
        }
    }

    /**
     * 주의사항 더보기.
     *
     * @param v
     */
    public void onMore(View v) {
        if (isMore) {
            isMore = false;
            mWarningTv.setSingleLine(true);
            mWarningBtn.setBackgroundResource(R.drawable.r_more);
        } else {
            isMore = true;
            mWarningTv.setSingleLine(false);
            mWarningBtn.setBackgroundResource(R.drawable.r_more_on);
        }
    }

    /**
     * 내댓글보기, 전체댓글보기
     *
     * @param v
     */
    public void onReply(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
//            if (mMode.equals("A")) {
//                mMode = "M";
//                mShowReply.setBackgroundResource(R.drawable.detail_btn05);
//            } else {
//                mMode = "A";
//                mShowReply.setBackgroundResource(R.drawable.detail_btn04);
//            }
//            mCommentList.removeAll(mCommentList);
//            LoadMore("0");
//            mBtnUpload.requestFocus();
        } else {
//            Utils.showDialog(this);
        }
    }

    private int page = 0;
    private int loadCompletedCount = 0;

    /**
     * 댓글 로딩
     */
    private void LoadMore() {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", "11"));
        mParams.add(new BasicNameValuePair("srl", Utils.getJsonData(total_json.toString(), "event_srl")));
        mParams.add(new BasicNameValuePair("size", "10"));
        mParams.add(new BasicNameValuePair("next", String.valueOf(page)));
        mParams.add(new BasicNameValuePair("pageable", "true"));
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.REPLY_ALL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                getJsonDataReply(result);
            }
        });
    }

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if ((firstVisibleItem + visibleItemCount) == totalItemCount && loadCompletedCount != totalItemCount) {
                loadCompletedCount = totalItemCount;
                if (page != 0) LoadMore();
            }
        }
    };
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        int count = totalItemCount - visibleItemCount;
//        if (isLoader) {
//            if (firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false) {
//                mLockListView = true;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        LoadMore(mCommentList.get(mCommentList.size() - 1).getReply_srl());
////                        Utils.loading(mLoading);
//                    }
//                }, 1000);
//            }
//        } else {
//            mList.removeFooterView(mFooter);
//            mFooter.setVisibility(View.GONE);
//            if (mLockListView) {
//                mList.addFooterView(mFooter);
//                mFooter.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String tempStr = mEtInput.getText().toString();
//        mEtInput.setText(Utils.getColorNickName(tempStr + " @" + mCommentList.get(position - 1).getNick().replace(" ", "_") + " "));
//        mEtInput.setSelection(mEtInput.getText().toString().length());
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith("fanmaum1://twitterresult.com")) {

            new Thread() {
                @Override
                public void run() {
                    try {
                        String oauth_verifier = uri.getQueryParameter("oauth_verifier");
                        AccessToken acToken = OAuthID.twitter.getOAuthAccessToken(OAuthID.requestToken, oauth_verifier);

                        OAuthID.sns_message = Utils.getJsonData(total_json.toString(), "title")
                                + " " + Utils.getJsonData(total_json.toString(), "present")
                                + " " + URLAddress.NEW_SERVER + "/events/view/" + Utils.getJsonData(total_json.toString(), "event_srl")
                                + "/" + Utils.getLanquageLocal(EventDetailActivity.this)
                                + "/" + Utils.getGenerateString()
                        ;
                        Status status = OAuthID.twitter.updateStatus(OAuthID.sns_message);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(EventDetailActivity.this, OAuthID.sns_message);
                            }
                        });

                        Utils.setEventShareSnsIndex(EventDetailActivity.this, Utils.getJsonData(total_json.toString(), "event_srl"), "twitter");
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(EventDetailActivity.this, getString(R.string.sns_aleady_update));
                            }
                        });
                    }
                }
            }.start();
        } else {
            isReview = intent.getBooleanExtra("review", false);//후기 구분자
            isHistory = intent.getStringExtra("isHistory");
            onCreate(getIntent().getExtras());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mHideLayout != null) {
                if (mHideLayout.getVisibility() == View.VISIBLE) {
                    mHideLayout.setVisibility(View.GONE);
                    return true;
                } else {
                    onBack();
                }
            } else {
                onBack();
            }
        }

        return super.onKeyDown(keyCode, event);
    }


}
