package specup.fanmind.main.tab2_;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.CommentAdapter;
import specup.fanmind.adapter.NewfeedImageListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.AdapterLinearLayout;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.NoticeActivity2;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.NewsfeedVO;

/**
 * 뉴스피드 상세
 */
public class NewsFeedDetailActivity extends Activity implements OnTask, OnScrollListener, AdapterView.OnItemClickListener {

    ListView mList;
    TextView mNickName, mDate, mTitle, mContents, mLikeCnt;
    AdapterLinearLayout mImageLayout;

    //프로필 이미지, 추가되는 이미지.
    ImageView mProfile, mLoading;
    ImageView mImageView;

    //코멘트 리스트.
    ArrayList<CommentList> mCommentList;
    //댓글 아답터
    CommentAdapter mComment;

    //서버통신에 필요한 정보.
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    Button mUploadBtn;
    EditText mEtInput;
    int mReplyCount, mLike;
    boolean isLoader, mLockListView, isReply, isNoti;
    View mFooter;

    int mImagePostion = 0, mImageScale = 0, mExtra;
    RelativeLayout.LayoutParams mLayoutParam;
    String mImagePath[], mBoard = "4", rMode = "A", mReplyBoard = "0";
    //    LinearLayout mBtnLike, mBtnReply;
    ImageView imageview_like;
    TextView textView_reply;
    //sMode => 뉴스피드 모드, mMode => 댓글 모드
    String sSrl, sTitle, sText, sNick, sPic, sTime, sLikes, sMyLike, sReplyCnt, sAlarm;
    NewsfeedVO newsfeedVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.fragment_newsfeeddetail);

        Utils.setAnalyticsTrackerScreenName(this, "NewsFeed Detail");

        Intent intent = getIntent();
        newsfeedVO = intent.getParcelableExtra("newsfeedVO");
        if (newsfeedVO != null) {
            sSrl = newsfeedVO.getNewsfeed_srl();
            setView();
        } else {// 푸쉬일경우.
            sSrl = intent.getStringExtra("srl");
            getNetwork_newsfeed_get();
        }

        isReply = intent.getBooleanExtra("reply", false);
        isNoti = intent.getBooleanExtra("isNoti", false);
//        String rSrl = intent.getStringExtra("replysrl");
//        if (isNoti) {//푸쉬일때.
//            mParams = new ArrayList<NameValuePair>();
//            mParams.add(new BasicNameValuePair("reply_srl", rSrl));
//            mParams = Utils.setSession(this, mParams);
//            HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.NEWS_EDIT(this), AsyncTaskValue.NEWS_EDIT, this);
//        }
//        else {
//            getNetwork();
//        }
    }

    /**
     * 뉴스피드 1개 가져옴.
     * <p/>
     */
    private void getNetwork_newsfeed_get() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("newsfeed_srl", sSrl));
        mParams.add(new BasicNameValuePair("star", ""));
        HttpRequest.setHttp1(this, URLAddress.NEWSFEED_GET(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                            JSONArray itemJSONArray = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                            try {
                                JSONObject itemJSONObject = itemJSONArray.getJSONObject(0);
                                newsfeedVO = new NewsfeedVO();
                                newsfeedVO.setNewsfeed_srl(itemJSONObject.getString("newsfeed_srl"));
                                newsfeedVO.setTitle(itemJSONObject.getString("title"));
                                newsfeedVO.setText(itemJSONObject.getString("text"));
                                newsfeedVO.setLikes(itemJSONObject.getString("likes"));
                                newsfeedVO.setReply_cnt(itemJSONObject.getString("reply_cnt"));
                                newsfeedVO.setTime(itemJSONObject.getString("time"));
                                newsfeedVO.setAlarm(itemJSONObject.getString("alarm"));
                                newsfeedVO.setMember_srl(itemJSONObject.getString("member_srl"));
                                newsfeedVO.setNick(itemJSONObject.getString("nick"));
                                newsfeedVO.setPic(itemJSONObject.getString("pic"));
                                newsfeedVO.setPic_base(itemJSONObject.getString("pic_base"));
                                newsfeedVO.setStar_srl(itemJSONObject.getString("star_srl"));
                                newsfeedVO.setName(itemJSONObject.getString("name"));
                                newsfeedVO.setIs_liked(itemJSONObject.getString("is_liked"));
                                //image
                                JSONArray imageArrayData = new JSONArray(itemJSONObject.getString("images"));
                                int imageLength = imageArrayData.length();
                                String[] stringArrya = new String[imageLength];
                                for (int i1 = 0; i1 < imageLength; i1++) {
                                    JSONObject jsonObject_imageUrl = imageArrayData.getJSONObject(i1);

                                    stringArrya[i1] = jsonObject_imageUrl.getString("img_base") + jsonObject_imageUrl.getString("img");
                                }
                                newsfeedVO.setImages(stringArrya);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            setView();
                        } else {
                            Utils.setToast(NewsFeedDetailActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }
        );
    }

    private void setView() {
        sTitle = newsfeedVO.getTime();
        sText = newsfeedVO.getText();
        sNick = newsfeedVO.getNick();
        String pic = newsfeedVO.getPic();
        String pic_base = newsfeedVO.getPic_base();
        sPic = pic_base + pic;
        sTime = newsfeedVO.getTime();
        mImagePath = newsfeedVO.getImages();
        sLikes = newsfeedVO.getLikes();
        sMyLike = newsfeedVO.getIs_liked();
        sReplyCnt = newsfeedVO.getReply_cnt();
        sAlarm = newsfeedVO.getAlarm();

        mLockListView = true;
        mList = (ListView) findViewById(R.id.newsfeeddetail_list);
        View header = getLayoutInflater().inflate(R.layout.header_newsfeeddetail, null);
        mNickName = (TextView) header.findViewById(R.id.newsfeeddetail_tv01);
        mDate = (TextView) header.findViewById(R.id.newsfeeddetail_tv02);
        mTitle = (TextView) header.findViewById(R.id.textView_title);
        mContents = (TextView) header.findViewById(R.id.newsfeeddetail_tv03);

        mLikeCnt = (TextView) header.findViewById(R.id.newsfeeddetail_tv04);

        if (mImagePath != null) {
            NewfeedImageListAdapter newfeedImageListAdapter = new NewfeedImageListAdapter(NewsFeedDetailActivity.this, mImagePath, mImageClick);
            mImageLayout = (AdapterLinearLayout) header.findViewById(R.id.newsfeeddetail_imageLayout);
            mImageLayout.setOrientation(LinearLayout.VERTICAL);
            mImageLayout.setAdapter(newfeedImageListAdapter);
        }

        mProfile = (ImageView) header.findViewById(R.id.newsfeeddetail_img);
//        mBtnLike = (LinearLayout) header.findViewById(R.id.layout_like);
        imageview_like = (ImageView) header.findViewById(R.id.imageview_like);
        textView_reply = (TextView) header.findViewById(R.id.textView_reply);
//        mBtnReply = (Button) header.findViewById(R.id.newsfeeddetail_btn03);

        mEtInput = (EditText) findViewById(R.id.supportingdetail_et01);
        mFooter = getLayoutInflater().inflate(R.layout.footer_layout, null);
        mLoading = (ImageView) mFooter.findViewById(R.id.footer_image);
        mUploadBtn = (Button) findViewById(R.id.supportingdetail_btn02);
        mUploadBtn.requestFocus();
        mList.addHeaderView(header);
        mCommentList = new ArrayList<CommentList>();
        mComment = new CommentAdapter(this, mCommentList, mClick);
        mList.setAdapter(mComment);
        mList.setOnScrollListener(this);
        mList.setOnItemClickListener(this);
        LoadMore("0");
        setInfo();
    }

    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.listcomment_tv06: //신고하기
                    //				onReplyReport(v);
                    supportSingo(v);
                    break;
                case R.id.listcomment_layout01: //좋아요
                    onReplyLike(v);
                    break;
                case R.id.listcomment_tv07: //삭제하기

                    deleteCommentDialog(v);
                    break;
            }
        }
    };

    /**
     * 삭제 다이얼로그
     */
    public void deleteCommentDialog(final View v1) {
        String title = getString(R.string.alert);
        String content = getString(R.string.replydel);
        String left = getString(R.string.confirmation);//확인
        String right = getString(R.string.cancel);//취소
        mDialog = new CustomDialog(NewsFeedDetailActivity.this, title, content, new View.OnClickListener() {//left
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
        mParams = Utils.delReply(this, mParams, mCommentList.get(del).getReplySrl());
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.REPLY_DELETE(this), AsyncTaskValue.REPLY_DELETE, this);
    }


    /**
     * 리플 좋아요
     *
     * @param v
     */
    private void onReplyLike(View v) {
        mLike = (Integer) v.getTag();
        if (mCommentList.get(mLike).getLike().equals("0")) {
            mParams = Utils.likeOK(this, mParams, mReplyBoard, mCommentList.get(mLike).getReplySrl());
            HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LIKE(this), AsyncTaskValue.REPLY_LIKE, this);
        } else {
            mParams = Utils.likeCancel(this, mParams, mReplyBoard, mCommentList.get(mLike).getReplySrl());
            HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LIKE_CANCEL(this), AsyncTaskValue.REPLY_LIKE_CANCEL, this);
        }
    }

    /**
     * 댓글 신고하기
     *
     * @param v
     */
    private void onReplyReport(View v) {
        int position = (Integer) v.getTag();
        mParams = Utils.reportReply(this, mParams, mCommentList.get(position).getReplySrl());
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.REPLY_REPORT(this),
                AsyncTaskValue.REPLY_REPORT, this);
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
            Utils.showDialog(this);
        }
    }


    /**
     * 댓글 로딩
     *
     * @param begin
     */
    private void LoadMore(String begin) {
        mParams = Utils.setReply(NewsFeedDetailActivity.this, mParams, mBoard, rMode, sSrl, begin);
        HttpRequest.setHttp(NewsFeedDetailActivity.this, mAsyncTask, mParams,
                URLAddress.REPLY_LIST(NewsFeedDetailActivity.this), AsyncTaskValue.REPLY_LIST, this);
    }

    private void setInfo() {
        if (sPic != null) ImageLoader.getInstance().displayImage(sPic, mProfile);
        else mProfile.setBackgroundResource(R.drawable.profile_basic01);

        mNickName.setText(sNick);
        mDate.setText(Utils.chageCommentDate(this, sTime));
        mTitle.setText((sText));

        mLikeCnt.setText(getString(R.string.likenumber).replace("{NUMBER}", Utils.getMoney(sLikes)));
        if (mImagePath.length > 0) {

        } else {
            mImageLayout.setVisibility(View.GONE);
        }

        if (sMyLike.equals("Y")) {
            imageview_like.setBackgroundResource(R.drawable.like_on);
        } else {
            imageview_like.setBackgroundResource(R.drawable.like_off);
        }
    }

    OnClickListener mImageClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int position = (Integer) v.getTag();
            Intent intent = new Intent(NewsFeedDetailActivity.this, NewsFeedImageActivity.class);
            intent.putExtra("image", mImagePath);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    };


    public void onSetting(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
            Intent intent = new Intent(NewsFeedDetailActivity.this, NewsFeedPopupActivity.class);
            intent.putExtra("newsfeed", true);
            intent.putExtra("alarm", sAlarm);
            if (sNick.equals(FanMindSetting.getNICK_NAME(NewsFeedDetailActivity.this))) {
                intent.putExtra("isMe", true);
            } else {
                intent.putExtra("isMe", false);
            }
            startActivityForResult(intent, Utils.NEWSFEED_SINGO_ALL);
        } else {
            Utils.showDialog(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Utils.NEWSFEED_SINGO_ALL) {
            mExtra = data.getIntExtra("kind", 0);
            if (mExtra == 0) {
                if (sAlarm.equals("1")) {//알람끄기
                    newsFeedAlram();
                } else {//알람켜기
                    newsFeedAlram();
                }
            } else if (mExtra == 1) { //게시글 수정
                newsFeedInert();
            } else if (mExtra == 2) {
                if (sNick.equals(FanMindSetting.getNICK_NAME(this))) { //게시물 삭제
                    newsFeedDel();
                } else { //아이디가 같지 않으면 신고하기
                    newsFeedSingo();
                }
            }
        } else if (requestCode == Utils.NEWSFEED_WRITE_INSERT) {
            setResult(RESULT_OK, getIntent().putExtra("del", true));
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void newsFeedInert() {
        Intent intent = new Intent(this, NewsFeedWriteInsertActivity.class);
        intent.putExtra("newssrl", sSrl);
        intent.putExtra("image", mImagePath);
        intent.putExtra("text", sText);
        startActivityForResult(intent, Utils.NEWSFEED_WRITE_INSERT);
    }

    @Override
    protected void onDestroy() {
        if (NewsFeedFragment.myRecyclerAdapter != null) NewsFeedFragment.myRecyclerAdapter.notifyDataSetChanged();
        super.onDestroy();
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.NEWSFEED_REPORT_NUM) {
            if (Utils.getJsonDataString(result).equals("3901")) {
                Utils.setToast(NewsFeedDetailActivity.this, R.string.reportok);
            } else if (Utils.getJsonDataString(result).equals("3902")) {
                Utils.setToast(NewsFeedDetailActivity.this, R.string.reportok);
            }
        } else if (output == AsyncTaskValue.NEWSFEED_ALARM_OFF_NUM) { //알람 끄기
            if (Utils.getJsonData(result)) {
                Utils.setToast(NewsFeedDetailActivity.this, R.string.alramoffok);
                sAlarm = "0";
            }
        } else if (output == AsyncTaskValue.NEWSFEED_ALARM_ON_NUM) {//알람 켜기
            if (Utils.getJsonData(result)) {
                Utils.setToast(NewsFeedDetailActivity.this, R.string.alramonok);
                sAlarm = "1";
            }
        } else if (output == AsyncTaskValue.NEWSFEED_DEL_NUM) {//삭제
            if (Utils.getJsonData(result)) {
                Log.e("123", "123Ok	");
                setResult(RESULT_OK, getIntent().putExtra("del", true));
                finish();
            }
        } else if (output == AsyncTaskValue.LIKE_NUM) {
            if (Utils.getJsonDataString(result).equals("2401")) {

                sMyLike = "Y";
                newsfeedVO.setIs_liked(sMyLike);
                NewsFeedFragment.myRecyclerAdapter.setChangeList(newsfeedVO);

                int count = Utils.subStringInt(sLikes) + 1;
                sLikes = String.valueOf(count);
                imageview_like.setBackgroundResource(R.drawable.like_on);
                mLikeCnt.setText(getString(R.string.likenumber).replace("{NUMBER}", Utils.getMoney(sLikes)));
                Utils.setToast(this, R.string.likeok);
            }
        } else if (output == AsyncTaskValue.LIKE_CANCEL_NUM) {
            if (Utils.getJsonDataString(result).equals("2451")) {

                sMyLike = "N";
                newsfeedVO.setIs_liked(sMyLike);
                NewsFeedFragment.myRecyclerAdapter.setChangeList(newsfeedVO);

                int count = Utils.subStringInt(sLikes) - 1;
                sLikes = String.valueOf(count);
                imageview_like.setBackgroundResource(R.drawable.like_off);
                mLikeCnt.setText(getString(R.string.likenumber).replace("{NUMBER}", Utils.getMoney(sLikes)));
                Utils.setToast(this, R.string.likecancel);
            }
        } else if (output == AsyncTaskValue.REPLY_WRITE_NUM) {
            if (Utils.getJsonData(result)) {
//				Utils.setToast(this, R.string.okreply);
                mCommentList.removeAll(mCommentList);
                mEtInput.setText("");
                LoadMore("0");
            }
        } else if (output == AsyncTaskValue.REPLY_LIST_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonData(result);
            }
        } else if (output == AsyncTaskValue.REPLY_LIKE_CANCEL_NUM) {
            if (Utils.getJsonDataString(result).equals("2451")) {
                mCommentList.get(mLike).setLike("0");
                int count = Utils.subStringInt(mCommentList.get(mLike).getLikeCount()) - 1;
                mCommentList.get(mLike).setLikeCount(String.valueOf(count));
                mComment.notifyDataSetChanged();
                Utils.setToast(this, R.string.likecancel);
            }
        } else if (output == AsyncTaskValue.REPLY_LIKE_NUM) {
            if (Utils.getJsonDataString(result).equals("2401")) {
                mCommentList.get(mLike).setLike("1");
                int count = Utils.subStringInt(mCommentList.get(mLike).getLikeCount()) + 1;
                mCommentList.get(mLike).setLikeCount(String.valueOf(count));
                mComment.notifyDataSetChanged();
                Utils.setToast(this, R.string.likeok);
            }
        } else if (output == AsyncTaskValue.REPLY_REPORT_NUM) {
            if (Utils.getJsonDataString(result).equals("2501")) {
                Utils.setToast(this, R.string.reportok);
            } else if (Utils.getJsonDataString(result).equals("2502")) {
                Utils.setToast(this, R.string.reportok);
            }
        } else if (output == AsyncTaskValue.REPLY_DELETE_NUM) {
            if (Utils.getJsonData(result)) {
                Utils.setToast(this, R.string.delok);
                mCommentList.removeAll(mCommentList);
                LoadMore("0");
            }
        }
//        else if (output == AsyncTaskValue.NEWS_EDIT_NUM) {
//            if (Utils.getJsonData(result)) {
//                getNetwork();
//            }
//        }
    }

    private void getJsonData(String result) {
        mReplyCount = 0;
        isLoader = false;
        try {
            JSONObject json = new JSONObject(result);
            String list = json.getString("list");
            JSONArray jsonArray = new JSONArray(list);

            newsfeedVO.setReply_cnt(String.valueOf(jsonArray.length()));
            NewsFeedFragment.myRecyclerAdapter.setChangeList(newsfeedVO);

            for (int i = 0; i < jsonArray.length(); i++) {
                mReplyCount++;
                String reply_srl = jsonArray.getJSONObject(i).getString("reply_srl");
                String id = jsonArray.getJSONObject(i).getString("id");
                String nick = jsonArray.getJSONObject(i).getString("nick");
                String pic = jsonArray.getJSONObject(i).getString("pic");
                String pic_base = jsonArray.getJSONObject(i).getString("pic_base");
                String time = jsonArray.getJSONObject(i).getString("time");
                String text = jsonArray.getJSONObject(i).getString("text");
                String likes = jsonArray.getJSONObject(i).getString("likes");
                String my_like = jsonArray.getJSONObject(i).getString("my_like");

                mCommentList.add(new CommentList(reply_srl, id, nick, pic_base + pic, time, text, likes, my_like));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mReplyCount == 20) isLoader = true;
        mComment.notifyDataSetChanged();
        mLockListView = false;
    }




    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        int count = totalItemCount - visibleItemCount;
        if (isLoader) {
            if (firstVisibleItem >= count && totalItemCount != 0
                    && mLockListView == false) {
                mLockListView = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        LoadMore(mCommentList.get(mCommentList.size() - 1).getReplySrl());
                        Utils.loading(mLoading);
                        Log.e("LoADING", "LOADING");
                    }
                }, 1000);
            }
        } else {
            mList.removeFooterView(mFooter);
            mFooter.setVisibility(View.GONE);
            if (mLockListView) {
                mList.addFooterView(mFooter);
                mFooter.setVisibility(View.VISIBLE);
            }
        }

    }


    CustomDialog mDialog;

    public void showDialog(Context context, String title, String content, String left, String right) {
        mDialog = new CustomDialog(context, title, content, new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                newsFeedAsyncTask();
            }
        }, left, right);
        mDialog.show();
    }

    /**
     * 뉴스피드 삭제하기 팝업
     */
    private void newsFeedDel() {
        String title = getString(R.string.newfeed05);
        String content = getString(R.string.newfeeddel);
        String left = getString(R.string.cancel);
        String right = getString(R.string.confirmation);
        showDialog(this, title, content, left, right);
    }


    /**
     * 뉴스피드 신고하기 팝업
     */
    private void newsFeedSingo() {
        String title = getString(R.string.report);
        String content = getString(R.string.report01);
        String left = getString(R.string.cancel);
        String right = getString(R.string.confirmation);
        showDialog(this, title, content, left, right);
    }

    /**
     * 뉴스피드 알림켜기 or 알림끄기
     */
    private void newsFeedAlram() {
        String title = getString(R.string.alram);
        String content = null;
        if (sAlarm.equals("0")) { //켜기
            content = getString(R.string.alram01);
        } else { //끄기
            content = getString(R.string.alram02);
        }
        String left = getString(R.string.cancel);
        String right = getString(R.string.confirmation);
        showDialog(this, title, content, left, right);
    }

    /**
     * 뉴스피드 신고하기 서버통신
     */
    private void newsFeedAsyncTask() {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("newsfeed_srl", sSrl));
        mParams = Utils.setSession(this, mParams);
        if (mExtra == 0) {
            if (sAlarm.equals("1")) {//게시글 알람 끄기
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.NEWSFEED_ALARM_OFF(this),
                        AsyncTaskValue.NEWSFEED_ALARM_OFF, this);
            } else {//게시글 알람 켜기
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.NEWSFEED_ALARM_ON(this),
                        AsyncTaskValue.NEWSFEED_ALARM_ON, this);
            }
        } else if (mExtra == 2) {
            if (sNick.equals(FanMindSetting.getNICK_NAME(this))) { //게시물 삭제
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.NEWSFEED_DEL(this),
                        AsyncTaskValue.NEWSFEED_DEL, this);
            } else { //게시글 신고하기
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.NEWSFEED_REPORT(this),
                        AsyncTaskValue.NEWSFEED_REPORT, this);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Back();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 백버튼
     *
     * @param v
     */
    public void onBack(View v) {
        Back();
    }

    private void Back() {
        if (isNoti) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mylike", sMyLike);
            intent.putExtra("likes", sLikes);
            setResult(RESULT_OK, intent);
        }
        ActivityManager.getInstance().deleteActivity(this);
    }


    /**
     * 좋아요, 좋아요취소
     *
     * @param v
     */
    public void onLike(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
            mParams = Utils.likeOK(this, mParams, "4", sSrl);
            if (sMyLike.equals("Y")) {
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LIKE_CANCEL(this), AsyncTaskValue.LIKE_CANCEL, this);
            } else {
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LIKE(this), AsyncTaskValue.LIKE, this);
            }
        } else {
            Utils.showDialog(this);
        }
    }


    /**
     * 전체댓글, 내댓글보기
     *
     * @param v
     */
    public void onReply(View v) {
        if (FanMindSetting.getLOGIN_OK(this)) {
            if (rMode.equals("A")) {
                rMode = "M";
                textView_reply.setText(getString(R.string.reply_all));
            } else {
                rMode = "A";
                textView_reply.setText(getString(R.string.reply_my));
            }
            mCommentList.removeAll(mCommentList);
            LoadMore("0");
        } else {
            Utils.showDialog(this);
        }
    }

    /**
     * 글 업로드
     */
    public void onUpload(View v) {
        uploadReply();
    }

    /**
     * 댓글쓰기
     */
    private void uploadReply() {
        if (FanMindSetting.getLOGIN_OK(this)) {
            if (mEtInput.length() != 0) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
                mParams = Utils.writeReply(this, mParams, mBoard, sSrl, mEtInput.getText().toString().trim());
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.REPLY_WRITE(this),
                        AsyncTaskValue.REPLY_WRITE, this);
            } else {
                Utils.setToast(this, R.string.inputwindow);
            }
        } else {
            Utils.showDialog(this);
        }
    }

    public void showDialog(Context context, String title, String content, String left, String right, final View view
            , final boolean isReport) {
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
                }/*else{
                    delReply(view);
				}*/
                mDialog.dismiss();
            }
        }, left, right);
        mDialog.show();
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String tempStr = mEtInput.getText().toString();
        mEtInput.setText(Utils.getColorNickName(tempStr + " @" + mCommentList.get(position - 1).getNickName().replace(" ", "_") + " "));
        mEtInput.setSelection(mEtInput.getText().toString().length());


    }


    /**
     * 프로젝트 알림 버튼
     *
     * @param v
     */
    public void onNotice(View v) {
        startActivity(new Intent(this, NoticeActivity2.class));
    }

    public void onShare(View v) {
//        FragmentManager fm = getFragmentManager();
//        AlertDialogShare dialog = new AlertDialogShare(this, sSrl);
//        dialog.show(fm, "dialog1");
    }


}
