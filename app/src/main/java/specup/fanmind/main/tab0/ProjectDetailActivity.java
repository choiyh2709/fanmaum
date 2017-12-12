package specup.fanmind.main.tab0;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.HomeFragment_detail_orderProjectAdapter;
import specup.fanmind.adapter.HomeFragment_detail_preview_hListAdapter;
import specup.fanmind.adapter.HomeFragment_detail_preview_pageAdapter;
import specup.fanmind.adapter.HomeFragment_detail_product_terms_pageAdapter;
import specup.fanmind.adapter.HomeFragment_detail_products_pageAdapter;
import specup.fanmind.adapter.HomeFragment_detail_together_hListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CirclePageIndicator;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.HorizontalListView;
import specup.fanmind.common.Util.ProjectStack;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.EventDetailActivity;
import specup.fanmind.left.NoticeActivity2;
import specup.fanmind.left.profile.ProfileActivity;
import specup.fanmind.main.login.OAuthID;
import specup.fanmind.main.tab2_.NewsFeedImageActivity;
import specup.fanmind.main.tab4.UserPageActivity;
import specup.fanmind.vo.AttendsVO;
import specup.fanmind.vo.MovieList;
import specup.fanmind.vo.ProductVO;
import specup.fanmind.vo.ProjectStackVO;
import specup.fanmind.vo.SupportList;
import twitter4j.Status;
import twitter4j.auth.AccessToken;

/**
 * 프로젝트 상세
 */
public class ProjectDetailActivity extends YouTubeBaseActivity implements OnTask, YouTubePlayer.OnInitializedListener {

    //서버통신에 필요한 정보.
    List<NameValuePair> mParams;
    EditText mEtMind;
    //상세보기시 변화는 텍스트뷰의 값
    TextView mTotalMind, mTitle, textView_slogun, mDate, mContents, mPeoPle, mPercent, mBarPercent, mDday, mDeliveryDate;
    TextView mNowMind, mWarningTv;
    WebView webview_description;


    //이미지 추가되는 레이아웃
    LinearLayout mImageLayout;
    //이미지 추가되는 레이아웃의 파람.
    RelativeLayout.LayoutParams mLayoutParam;
    //이미지가 나오는 이미지뷰
    ImageView iamge_main;
    //퍼센테이지를 보여주는 씨크바
    SeekBar mSeekBar;
    //글쓰기 업로드 버튼, 내댓글보기, 전체댓글보기, 내친구에게 보내기
    Button mUploadBtn, mShowReply, mSendMind, mShareBtn, mWarningBtn;
    //추가적으로 로딩 블린 값
    boolean mLockListView, isMore, isKakao, isNoti;

    String support_srl, sSrl, isLike, starSrl, isFeeded;
    String event_srl, event_end_date;
    int count;// 반복문에 사용


    JSONObject JsonObject_Host;
    JSONArray JsonObject_assists;
//    JSONArray JsonObject_attends;

    CheckBox checkBox_isLick;

    YouTubePlayerView youTubeView;
    YouTubePlayer mPlayer;
    boolean isFullScreen;

    ViewPager viewPager_preview;
    String preview_imagePath[];
    String sHearNow;

    String following;
    ImageView image_following;
    private static ProjectStack projectStack;
    TextView textView_comment, textview_snsSharingTotal;

    private boolean isReview;// 후기였을때


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "Project Detail");
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_project_detail);


        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        onLoader(savedInstanceState);

    }

    /**
     * finish project setting
     */
    private void setReviewProject() {
        ((TextView) findViewById(R.id.textView_projectTitle)).setText(getString(R.string.project_result2));
        ((TextView) findViewById(R.id.textView_projectDelivery)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textView_deadlineValue)).setVisibility(View.GONE);
        Button button_project_join = (Button) findViewById(R.id.button_project_join);
        button_project_join.setText(R.string.review_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    boolean isNoti2;
    String isHistory = "N";//히스토리 용

    public void onLoader(Bundle savedInstanceState) {
        Intent intent = getIntent();

        try {
            String tempString = "N";
            tempString = intent.getStringExtra("isHistory");
            if (tempString != null) isHistory = tempString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isHistory.equals("Y")) {
            setReviewProject();
        }

        isNoti = intent.getBooleanExtra("isNoti", false);
        isKakao = intent.getBooleanExtra("kakao", false);
        isNoti2 = intent.getBooleanExtra("isNoti2", false);

        sSrl = intent.getStringExtra("srl");
        sSrl = intent.getStringExtra("reply_on") != null ? intent.getStringExtra("reply_on") : sSrl; //댓글 푸쉬 였을때 사용

        if (savedInstanceState == null) {

            projectStack = new ProjectStack();
            ProjectStackVO projectStackVO = new ProjectStackVO();
            projectStackVO.setSrl(sSrl);

            projectStackVO.setIsHistory(isHistory);
            projectStack.push(projectStackVO);
        }

        event_srl = intent.getStringExtra("event_srl");
        event_end_date = intent.getStringExtra("event_end_date");
        support_srl = intent.getStringExtra("project_srl");
        starSrl = intent.getStringExtra("star_srl");


        setView();
    }

    @Override
    protected void onResume() {


        if (isNoti || isKakao) {
            mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("project_srl", sSrl));
            mParams.add(new BasicNameValuePair("star_srl", starSrl));
            mParams = Utils.setSession(this, mParams);
        } else {
            if (isNoti2) {
                mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("project_srl", sSrl));
                mParams.add(new BasicNameValuePair("star_srl", starSrl));
                mParams = Utils.setSession(this, mParams);
            } else {
                mParams = Utils.showDetail(this, mParams, "project_srl", sSrl);
            }
        }
        HttpRequest.setHttp1(this, URLAddress.SUPPORT_LIST_DETAIL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (!Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    if (isNoti) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    Utils.setToast(getApplicationContext(), R.string.supportdel01);
                    finish();
                } else {
                    getJsonDetail(result);
                }
            }
        });

        //attends
        getNetwork();
        super.onResume();
    }

    private void getNetwork() {

        //참여한팬
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("size", "500"));
        mParams.add(new BasicNameValuePair("project_srl", sSrl));
        mParams.add(new BasicNameValuePair("order", "2"));

        HttpRequest.setHttp1(this, URLAddress.PROJECT_ATTENDS(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {
                if (Utils.getJsonData(Utils.getJsonData(result, "data"), "total").compareTo("0") > 0) {

                    ((TextView) findViewById(R.id.textView_joinFanValue)).setText(getString(R.string.joinFan_value).replace("{VALUE}", Utils.getJsonData(Utils.getJsonData(result, "data"), "total")));

                    try {
                        final JSONArray JsonJSONArray_attends = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));

                        if (JsonJSONArray_attends.length() > 0) {
                            ((LinearLayout) findViewById(R.id.layout_joinFan)).setVisibility(View.VISIBLE);
                            ((LinearLayout) findViewById(R.id.layout_joinFan)).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailAttendFan.class);
                                    intent.putExtra("project_srl", sSrl);
                                    startActivity(intent);

                                }
                            });
                            List<Object> listAttends = new ArrayList<Object>();

                            count = JsonJSONArray_attends.length();
                            for (int i = 0; i < count; i++) {
                                try {
                                    AttendsVO attendsVO = new AttendsVO();

                                    attendsVO.setMember_srl(JsonJSONArray_attends.getJSONObject(i).getString("member_srl"));
                                    attendsVO.setNick(JsonJSONArray_attends.getJSONObject(i).getString("nick"));
                                    attendsVO.setPic(JsonJSONArray_attends.getJSONObject(i).getString("pic"));
                                    attendsVO.setPic_base(JsonJSONArray_attends.getJSONObject(i).getString("pic_base"));

                                    listAttends.add(attendsVO);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            HorizontalListView hList_AssistImage = (HorizontalListView) findViewById(R.id.hList_joinFanImage);
                            HomeFragment_detail_together_hListAdapter mHomeFragment_detail_assist_hListAdapter = new HomeFragment_detail_together_hListAdapter(ProjectDetailActivity.this, (listAttends));
                            hList_AssistImage.setAdapter(mHomeFragment_detail_assist_hListAdapter);
                            hList_AssistImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                                    try {
                                        intent.putExtra("JsonObject_attends", JsonJSONArray_attends.getJSONObject(position).toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    ((TextView) findViewById(R.id.textView_joinFanValue)).setText(getString(R.string.joinFan_value).replace("{VALUE}", "0"));
                }
            }
        });

    }


    private void setView() {
        mLockListView = true;
        iamge_main = (ImageView) findViewById(R.id.iamge_main);

        mTitle = (TextView) findViewById(R.id.textView_title);
        textView_slogun = (TextView) findViewById(R.id.textView_slogun);
        mDate = (TextView) findViewById(R.id.textView_projectTime);
        mDeliveryDate = (TextView) findViewById(R.id.textView_projectDelivery);

        mTotalMind = (TextView) findViewById(R.id.supportingdetail_tv06);
        mPeoPle = (TextView) findViewById(R.id.supportingdetail_tv09);
        mPercent = (TextView) findViewById(R.id.supportingdetail_tv11);
        mBarPercent = (TextView) findViewById(R.id.supportingdetail_tv04);
        mDday = (TextView) findViewById(R.id.supportingdetail_tv08);
        webview_description = (WebView) findViewById(R.id.webView_description);
        mSeekBar = (SeekBar) findViewById(R.id.supportingdetail_seekbar);
        mImageLayout = (LinearLayout) findViewById(R.id.supportingdetail_imageLayout);
        mShareBtn = (Button) findViewById(R.id.supportingdetail_btn03);
        mShowReply = (Button) findViewById(R.id.supportingdetail_btn04);


        mEtMind = (EditText) findViewById(R.id.supportingdetail_et02);
        mSendMind = (Button) findViewById(R.id.supportingdetail_btn01);
        mUploadBtn = (Button) findViewById(R.id.supportingdetail_btn02);
        checkBox_isLick = (CheckBox) findViewById(R.id.checkbox_like);
        checkBox_isLick.setTag(false);


        //관심갖기 로그인 체크
        LinearLayout checkbox_blank = (LinearLayout) findViewById(R.id.checkbox_blank);
        checkbox_blank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(ProjectDetailActivity.this);
            }
        });

        if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
            checkbox_blank.setVisibility(View.GONE);
        } else {
            checkbox_blank.setVisibility(View.VISIBLE);
        }


        checkBox_isLick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(ProjectDetailActivity.this, mParams);
                mParams.add(new BasicNameValuePair("project_srl", sSrl));

                switch (buttonView.getId()) {
                    case R.id.checkbox_like: {
                        if ((boolean) checkBox_isLick.getTag()) {
                            if (isChecked) {
                                HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.PROJECT_FEED(), mParams, new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        Utils.setSnackBar(ProjectDetailActivity.this, checkBox_isLick, getString(R.string.interest_success));
                                    }
                                });
                            } else {
                                HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.PROJECT_UNFEED(), mParams, new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        Utils.setSnackBar(ProjectDetailActivity.this, checkBox_isLick, getString(R.string.interest_cancel));
                                    }
                                });
                            }
                        }

                    }
                    break;
                }

            }
        });


        try {
            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            youTubeView.initialize(Utils.DEVELOPER_KEY, this);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        ((TextView) findViewById(R.id.snsSharing10maum)).setText(Html.fromHtml(getResources().getString(R.string.sns_sharing_10maum)));

        mWarningBtn = (Button) findViewById(R.id.supportingdetail_btn22);
        mWarningTv = (TextView) findViewById(R.id.supportingdetail_tv22_01);


    }

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


    CustomDialog mDialog;

    @Override
    public void onTask(int output, String result) {
        if (output == AsyncTaskValue.SUPPORT_SEND_MIND_NUM) {
            if (Utils.getJsonDataString(result).equals("2251")) {
                getJsonDataMind(result);
            }
        }
    }

    JSONObject total_Json = null;

    private void getJsonDetail(String result) {

        try {
            total_Json = new JSONObject(Utils.getJsonData(result, "data"));

            String sTitle = total_Json.getString("title");
            String[] tempArray = sTitle.split("&&");
            sTitle = tempArray[0];

            String thum = total_Json.getString("thumbnail");
            String thumbnail_base = total_Json.getString("thumbnail_base");
            JSONArray jsonArray = total_Json.getJSONArray("attends");
            String sSupportCnt = String.valueOf(jsonArray.length());
            isFeeded = total_Json.getString("is_feeded");
            isLike = total_Json.getString("is_liked");
            sHearNow = total_Json.getString("heart_now");

            JsonObject_Host = total_Json.getJSONObject("host");
            JsonObject_assists = total_Json.getJSONArray("assists");
//            JsonObject_attends = total_Json.getJSONArray("attends");

            mTitle.setText(sTitle);
            textView_slogun.setText(Html.fromHtml(total_Json.getString("slogun")));
            HttpRequest.glideImage(this,thumbnail_base + thum, iamge_main);
            String sdate = Utils.chageDate(total_Json.getString("begin_time"));
            String ddate = Utils.chageDate(total_Json.getString("close_time"));
            String date = getString(R.string.project_time).replace("{SDATE}", sdate).replace("{DDATE}", ddate);
            String deliveryDate = getString(R.string.project_delivery_time).replace("{DATE}", ddate);
            mDate.setText(date);
            mDeliveryDate.setText(deliveryDate);

            //host setting
            ((TextView) findViewById(R.id.textView_hostNickName)).setText(Utils.getJsonData(JsonObject_Host.toString(), "name"));
            ((TextView) findViewById(R.id.textView_hostNickName2)).setText(Utils.getJsonData(JsonObject_Host.toString(), "name"));

            HttpRequest.getNetworkCircleImage(this ,Utils.getJsonData(JsonObject_Host.toString(), "profile_base") + Utils.getJsonData(JsonObject_Host.toString(), "profile"), (ImageView) findViewById(R.id.imageView_profile));
            HttpRequest.getNetworkCircleImage(this, Utils.getJsonData(JsonObject_Host.toString(), "profile_base") + Utils.getJsonData(JsonObject_Host.toString(), "profile"), (ImageView) findViewById(R.id.imageView_profile2));
            ((TextView) findViewById(R.id.textView_total_followers)).setText(getString(R.string.total_following).replace("{TOTAL}", Utils.getJsonData(JsonObject_Host.toString(), "total_followers").equals("") ? "0" : Utils.getJsonData(JsonObject_Host.toString(), "total_followers")));

            if (isFeeded.equals("Y")) {
                checkBox_isLick.setChecked(true);
            } else {
                checkBox_isLick.setChecked(false);
            }

            checkBox_isLick.setTag(true);
            //project imformation values
            ((TextView) findViewById(R.id.textView_maumValue)).setText(getString(R.string.maum_value).replace("{VALUE}", Utils.getMoney(total_Json.getString("heart_now"))));
            ((TextView) findViewById(R.id.textView_deadlineValue)).setText(getString(R.string.deadline_value).replace("{VALUE}", String.valueOf(Utils.GetDifferenceOfDate(total_Json.getString("close_time"), Utils.getTime()))));

            //seekbar
            double sum = Utils.getPercent(total_Json.getString("heart_now"), total_Json.getString("heart_goal"));


            mSeekBar.setThumb(null);
            mSeekBar.setFocusableInTouchMode(false);
            mSeekBar.setFocusable(false);
            mSeekBar.setProgress((int) sum);
            mSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            mBarPercent.setText(String.valueOf((int) sum));
            if (total_Json.getString("heart_goal").equals("0")) {
                ((RelativeLayout) findViewById(R.id.supportingdetail_layout03)).setVisibility(View.GONE);
            }

            try {
                //서버에서 id 받아와야함.
//                String youtubeid = "ulPvZFgoGYk";

                String youtubeId = Utils.getJsonData(total_Json.toString(), "video").replace("https://youtu.be/", "");
                if (isHistory.equals("N")) {
                    if (youtubeId.length() > 0 && !youtubeId.equals("null")) {
                        youTubeView.setVisibility(View.VISIBLE);
                        if (mPlayer != null) {
                            try {
                                mPlayer.cueVideo(youtubeId);
                            } catch (IllegalStateException ise) {
                                //do nothing probably device go rotated
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            webview_description.getSettings().setDomStorageEnabled(true);
            webview_description.getSettings().setJavaScriptEnabled(true);


            webview_description.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

            });
            webview_description.setWebChromeClient(new WebChromeClient());
            if (!isReview) {//일반
                webview_description.loadUrl(URLAddress.PROJECT_DETAIL(Utils.getJsonData(total_Json.toString(), "project_srl"), ((int) getResources().getDimension(R.dimen.display_width)) / 3));
            } else {//후기
                webview_description.loadUrl(URLAddress.PROJECT_AFFTER_DETAIL(Utils.getJsonData(total_Json.toString(), "project_srl"), ((int) getResources().getDimension(R.dimen.display_width)) / 3));
            }

            //preview
            try {
                JSONArray jsonArray1 = total_Json.getJSONArray("preview");
                if (jsonArray1.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_preview)).setVisibility(View.VISIBLE);
                    viewPager_preview = (ViewPager) findViewById(R.id.viewPager_preview);

                    List<Object> imageList = new ArrayList<Object>();

                    preview_imagePath = new String[jsonArray1.length()];
                    count = jsonArray1.length();
                    for (int i = 0; i < count; i++) {
                        MovieList imageItem = new MovieList();
                        try {
                            String pic = jsonArray1.getJSONObject(i).getString("pic");
                            String pic_base = jsonArray1.getJSONObject(i).getString("pic_base");
                            if (!pic.equals("null")) {
                                imageItem.setImagePath(pic_base + pic);
                                preview_imagePath[i] = pic_base + pic;
                            }

                            imageList.add(imageItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    HomeFragment_detail_preview_pageAdapter mHomeFragment_detail_previewAdapter = new HomeFragment_detail_preview_pageAdapter(getApplicationContext(), imageList, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getTag();

                            Intent intent = new Intent(ProjectDetailActivity.this, NewsFeedImageActivity.class);
                            intent.putExtra("image", preview_imagePath);
                            intent.putExtra("position", position);
                            startActivity(intent);

                        }
                    });
                    viewPager_preview.setAdapter(mHomeFragment_detail_previewAdapter);
                    viewPager_preview.setCurrentItem(0);

                    //preview selectbox
                    HorizontalListView hList_previewImageList = (HorizontalListView) findViewById(R.id.hList_previewImageList);
                    hList_previewImageList.setOnItemSelectedListener(onItemSelectedListener);
                    HomeFragment_detail_preview_hListAdapter mHomeFragment_detail_preview_hListAdapter = new HomeFragment_detail_preview_hListAdapter(this, (imageList));
                    hList_previewImageList.setAdapter(mHomeFragment_detail_preview_hListAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //기간 특전
            try {
                JSONArray jsonArray2 = total_Json.getJSONArray("product_terms");

                if (jsonArray2.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_benefit)).setVisibility(View.VISIBLE);
                    List<Object> productList = new ArrayList<Object>();
                    count = jsonArray2.length();
                    for (int i = 0; i < count; i++) {
                        ProductVO productVO = new ProductVO();
                        try {
                            JSONObject jsonObject = jsonArray2.getJSONObject(i);
                            productVO.setSrl(Utils.getJsonData(jsonObject.toString(), "term_srl"));
                            productVO.setName(Utils.getJsonData(jsonObject.toString(), "name"));
                            productVO.setComponent(Utils.getJsonData(jsonObject.toString(), "component"));
                            productVO.setNote(Utils.getJsonData(jsonObject.toString(), "note"));
                            productVO.setBegin_time(Utils.getJsonData(jsonObject.toString(), "begin_time"));
                            productVO.setClose_time(Utils.getJsonData(jsonObject.toString(), "close_time"));

                            Log.d("기간 특전",productVO.getBegin_time()+","+productVO.getClose_time());

                            productList.add(productVO);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ViewPager viewPager_benefit = (ViewPager) findViewById(R.id.viewPager_benefit);
                    HomeFragment_detail_product_terms_pageAdapter mHomeFragment_detail_product_terms_pageAdapter = new HomeFragment_detail_product_terms_pageAdapter(getApplicationContext(), productList);
                    viewPager_benefit.setAdapter(mHomeFragment_detail_product_terms_pageAdapter);
                    CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator1);
                    circlePageIndicator.setViewPager(viewPager_benefit);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //상품 구성
            try {
                JSONArray jsonArray3 = total_Json.getJSONArray("products");
                if (jsonArray3.length() > 0) {

                    LinearLayout layout_goods = (LinearLayout) findViewById(R.id.layout_goods);
                    layout_goods.setVisibility(View.VISIBLE);
                    layout_goods.setOnClickListener(onClickListener);

                    List<Object> productTermsList = new ArrayList<Object>();
                    count = jsonArray3.length();
                    for (int i = 0; i < count; i++) {
                        ProductVO productVO = new ProductVO();
                        try {
                            productVO.setSrl(jsonArray3.getJSONObject(i).getString("product_srl"));
//                            productVO.setName(jsonArray3.getJSONObject(i).getString("name"));
                            productVO.setComponent(jsonArray3.getJSONObject(i).getString("component"));
                            productVO.setNote(jsonArray3.getJSONObject(i).getString("note"));
                            productVO.setPoint(jsonArray3.getJSONObject(i).getString("point"));
                            productVO.setAmount_max(jsonArray3.getJSONObject(i).getString("amount_max"));
                            productVO.setAmount_now(jsonArray3.getJSONObject(i).getString("amount_now"));

                            productTermsList.add(productVO);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ViewPager viewPager_goods = (ViewPager) findViewById(R.id.viewPager_goods);
                    HomeFragment_detail_products_pageAdapter mHomeFragment_detail_product_pageAdapter = new HomeFragment_detail_products_pageAdapter(getApplicationContext(), productTermsList, onClickListener);
                    viewPager_goods.setAdapter(mHomeFragment_detail_product_pageAdapter);
                    CirclePageIndicator circlePageIndicator1 = (CirclePageIndicator) findViewById(R.id.indicator2);
                    circlePageIndicator1.setViewPager(viewPager_goods);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //팬되기
            try {
                following = JsonObject_Host.getString("relation").trim();
                image_following = (ImageView) findViewById(R.id.image_following);
                if (!following.equals("me")) {

                    if (following.equals("following") || following.equals("friend")) {
                        image_following.setBackgroundResource(R.drawable.a1100_5_like_active2);
                    } else {
                        image_following.setBackgroundResource(R.drawable.a1100_5_like_normal2);
                    }

                    LinearLayout layout_following = (LinearLayout) findViewById(R.id.layout_following);
                    layout_following.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (following.equals("following") || following.equals("friend")) {//팬이였을때
                                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                                try {
                                    mParams.add(new BasicNameValuePair("follow_srl", JsonObject_Host.getString("member_srl")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mParams = Utils.setSession(getApplicationContext(), mParams);

                                HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.UNFOLLOWING(), mParams, new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                            following = Utils.getJsonData(Utils.getJsonData(result, "data"), "relation");
                                            image_following.setBackgroundResource(R.drawable.a1100_5_like_normal2);
                                            ((TextView) findViewById(R.id.textView_total_followers)).setText(getString(R.string.total_following).replace("{TOTAL}", Utils.getJsonData(Utils.getJsonData(result, "data"), "total_followers")));
                                        } else {
                                            Utils.setSnackBar(ProjectDetailActivity.this, image_following, Utils.getJsonData(result, "message"));
                                        }

                                    }
                                });
                            } else {//팬이 아닐때
                                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                                try {
                                    mParams.add(new BasicNameValuePair("follow_srl", JsonObject_Host.getString("member_srl")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mParams = Utils.setSession(getApplicationContext(), mParams);

                                HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.FOLLOWING(), mParams, new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                            following = Utils.getJsonData(Utils.getJsonData(result, "data"), "relation");
                                            image_following.setBackgroundResource(R.drawable.a1100_5_like_active2);
                                            ((TextView) findViewById(R.id.textView_total_followers)).setText(getString(R.string.total_following).replace("{TOTAL}", Utils.getJsonData(Utils.getJsonData(result, "data"), "total_followers")));
                                        } else {
                                            Utils.setSnackBar(ProjectDetailActivity.this, image_following, Utils.getJsonData(result, "message"));
                                        }

                                    }
                                });
                            }
                        }
                    });

                }
                textview_snsSharingTotal = (TextView) findViewById(R.id.snsSharingTotal);
                textview_snsSharingTotal.setText(Utils.getJsonData(total_Json.toString(), "total_shares"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //게시자 팬되기
                String string_fan_twitter = Utils.getJsonData(JsonObject_Host.toString(), "twitter");
                String string_fan_facebook = Utils.getJsonData(JsonObject_Host.toString(), "facebook");
                String string_fan_instagram = Utils.getJsonData(JsonObject_Host.toString(), "instagram");

                if (string_fan_twitter.length() > 0) {
                    ImageView ImageView_fan_twitter = (ImageView) findViewById(R.id.fan_twitter);
                    ImageView_fan_twitter.setVisibility(View.VISIBLE);
                    ImageView_fan_twitter.setOnClickListener(onclickEvent);
                }

                if (string_fan_facebook.length() > 0) {
                    ImageView ImageView_fan_facebook = (ImageView) findViewById(R.id.fan_facebook);
                    ImageView_fan_facebook.setVisibility(View.VISIBLE);
                    ImageView_fan_facebook.setOnClickListener(onclickEvent);
                }
                if (string_fan_instagram.length() > 0) {
                    ImageView ImageView_fan_instagram = (ImageView) findViewById(R.id.fan_instagram);
                    ImageView_fan_instagram.setVisibility(View.VISIBLE);
                    ImageView_fan_instagram.setOnClickListener(onclickEvent);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //함께하는 사람들
            try {
                if (JsonObject_assists.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_together)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.layout_together)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailAssistUser.class);
                            intent.putExtra("project_srl", sSrl);
                            intent.putExtra("JsonObject_assists", JsonObject_assists.toString());
                            startActivity(intent);
                        }
                    });
                    List<Object> listAssists = new ArrayList<Object>();

                    count = JsonObject_assists.length();
                    for (int i = 0; i < count; i++) {
                        AttendsVO imageItem = new AttendsVO();
                        try {
                            String pic = JsonObject_assists.getJSONObject(i).getString("pic");
                            String pic_base = JsonObject_assists.getJSONObject(i).getString("pic_base");
                            if (!pic.equals("null")) {
                                imageItem.setPic_base(pic_base);
                                imageItem.setPic(pic);
                            }

                            listAssists.add(imageItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    HorizontalListView hList_togetherImage = (HorizontalListView) findViewById(R.id.hList_togetherImage);
                    HomeFragment_detail_together_hListAdapter mHomeFragment_detail_together_hListAdapter = new HomeFragment_detail_together_hListAdapter(this, (listAssists));
                    hList_togetherImage.setAdapter(mHomeFragment_detail_together_hListAdapter);

                    hList_togetherImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AttendsVO attendsVO = (AttendsVO) parent.getItemAtPosition(position);
                            Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                            try {
                                intent.putExtra("JsonObject_attends", JsonObject_assists.getJSONObject(position).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }


            //sns 공유
            try {
                ((Button) findViewById(R.id.button_login_facebook)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                            OAuthID.shareFacebook(ProjectDetailActivity.this, total_Json.toString(), callbackManager, 0);
                        } else {
                            Utils.showDialog(ProjectDetailActivity.this);
                        }
                    }
                });

                ((Button) findViewById(R.id.button_twitter)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                            OAuthID.shareTwitter(ProjectDetailActivity.this, 0);
                        } else {
                            Utils.showDialog(ProjectDetailActivity.this);
                        }

                    }
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //이런 프로젝트
            try {
                JSONArray jsonArray_OrderProject = total_Json.getJSONArray("others");

                if (jsonArray_OrderProject.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_recommendProject)).setVisibility(View.VISIBLE);

                    List<Object> projectList = new ArrayList<Object>();

                    count = jsonArray_OrderProject.length();
                    for (int i = 0; i < count; i++) {
                        /** project content
                         * @param srl 번호
                         * @param title 타이틀
                         * @param thum 이미지 url
                         * @param goal 목표요청수
                         * @param vote 현재요청수
                         * @param begin_time 시작일
                         * @param close_time 종료일
                         */
                        try {
                            JSONObject itemJSONObject = jsonArray_OrderProject.getJSONObject(i);

                            SupportList supportList = new SupportList(
                                    itemJSONObject.getString("project_srl")
                                    , itemJSONObject.getString("title")
                                    , itemJSONObject.getString("thumbnail_base") + itemJSONObject.getString("thumbnail")
                                    , itemJSONObject.getString("heart_goal")
                                    , itemJSONObject.getString("heart_now")
                                    , itemJSONObject.getString("begin_time")
                                    , itemJSONObject.getString("close_time")
                            );

                            projectList.add(supportList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    HorizontalListView listView_star = (HorizontalListView) findViewById(R.id.hList_recommendProject);
                    HomeFragment_detail_orderProjectAdapter homeFragment_detail_orderProjectAdapter = new HomeFragment_detail_orderProjectAdapter(this, (projectList));
                    listView_star.setAdapter(homeFragment_detail_orderProjectAdapter);
                    listView_star.setOnItemSelectedListener(onItemSelectedListener1);

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            // 좋아요
            try {

                //좋아요 로그인 처리.
                LinearLayout project_like_blank = (LinearLayout) findViewById(R.id.project_like_blank);
                if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                    project_like_blank.setVisibility(View.GONE);
                } else {
                    project_like_blank.setVisibility(View.VISIBLE);
                }
                project_like_blank.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.showDialog(ProjectDetailActivity.this);
                    }
                });


                project_like = (CheckBox) findViewById((R.id.project_like));
                project_like.setChecked(Utils.getJsonData(total_Json.toString(), "is_liked").equals("Y") ? true : false);
                project_like.setText(Utils.getJsonData(total_Json.toString(), "total_likes"));
                project_like.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!project_like.isChecked()) {// false
                            HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.PROJECT_UNLIKE(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                        project_like.setText(Utils.getJsonData(Utils.getJsonData(result.toString(), "data"), "total_likes"));
                                    } else {
                                        Utils.setSnackBar(ProjectDetailActivity.this, project_like, Utils.getJsonData(result, "message"));
                                    }
                                }
                            });
                        } else {
                            HttpRequest.setHttp1(ProjectDetailActivity.this, URLAddress.PROJECT_LIKE(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                        project_like.setText(Utils.getJsonData(Utils.getJsonData(result.toString(), "data"), "total_likes"));
                                    } else {
                                        Utils.setSnackBar(ProjectDetailActivity.this, project_like, Utils.getJsonData(result, "message"));
                                    }
                                }
                            });
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            //댓글
            try {
                LinearLayout layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
                textView_comment = (TextView) findViewById(R.id.textView_comment);
                textView_comment.setText(Utils.getJsonData(total_Json.toString(), "total_replies"));
                layout_comment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityManager.fragmentManager = getFragmentManager();
                        AlertDialogComment alertDialogComment = new AlertDialogComment(ProjectDetailActivity.this, sSrl, Utils.getJsonData(total_Json.toString(), "total_likes"), textView_comment, callbackManager, total_Json.toString());
                        alertDialogComment.show(getFragmentManager(), "comment");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            //, Q&A
            Button button_qna = (Button) findViewById(R.id.button_qna);
            button_qna.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.fragmentManager = getFragmentManager();
                    AlertDialogQnA alertDialogQnA = new AlertDialogQnA(ProjectDetailActivity.this, sSrl);
                    alertDialogQnA.show(getFragmentManager(), "alertDialogQnA");
                }
            });


            //프로젝트  참여하기
            Button button_project_join = (Button) findViewById(R.id.button_project_join);
            final String review_srl = Utils.getJsonData(total_Json.toString(), "review_srl");

            if (isReview) {
                button_project_join.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            } else {// 후기 가기
                button_project_join.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((review_srl.equals("null")) || (review_srl.equals(""))) {
                            if (isHistory.equals("Y")) {
                                String title = getString(R.string.alert);
                                String content = getString(R.string.before_review);
                                String left = getString(R.string.confirmation);
                                mDialog = new CustomDialog(ProjectDetailActivity.this, title, content, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                }, left);
                                mDialog.show();
                            } else {
                                if (FanMindSetting.getLOGIN_OK(ProjectDetailActivity.this)) {
                                    // ((BaseActivity)getActivity()).toggle();
                                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectPayment.class);
                                    intent.putExtra("total_Json", total_Json.toString());
                                    startActivity(intent);
                                } else {
                                    Utils.showDialog(ProjectDetailActivity.this);
                                }
                            }

                        } else {
                            Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailActivity.class);
                            intent.putExtra("review", true);
                            intent.putExtra("srl", sSrl);

                            ProjectStackVO projectStackVO = new ProjectStackVO();
                            projectStackVO.setSrl(sSrl);
                            projectStackVO.setReview(true);
                            projectStack.push(projectStackVO);

                            startActivity(intent);
                        }
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    CheckBox project_like;
    private OnClickListener onclickEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fan_twitter: {
                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.twitter.com/" + Utils.getJsonData(JsonObject_Host.toString(), "twitter"));
                    startActivity(intent);
                }
                break;
                case R.id.fan_facebook: {
                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.facebook.com/" + Utils.getJsonData(JsonObject_Host.toString(), "facebook"));
                    startActivity(intent);
                }
                break;
                case R.id.fan_instagram: {
                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.instagram.com/" + Utils.getJsonData(JsonObject_Host.toString(), "instagram"));
                    startActivity(intent);
                }
                break;
            }
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Uri uri = intent.getData();

        if (uri != null && uri.toString().startsWith("fanmaum://twitterresult.com")) {
            Utils.setShareSnsIndex(ProjectDetailActivity.this, Utils.getJsonData(total_Json.toString(), "project_srl"), "twitter");
            new Thread() {
                @Override
                public void run() {
                    try {
                        String oauth_verifier = uri.getQueryParameter("oauth_verifier");
                        AccessToken acToken = OAuthID.twitter.getOAuthAccessToken(OAuthID.requestToken, oauth_verifier);

                        OAuthID.sns_message = Utils.getJsonData(total_Json.toString(), "title")
                                + " " + Utils.getJsonData(total_Json.toString(), "slogun")
                                + " " + URLAddress.NEW_SERVER + "/projects/view/" + Utils.getJsonData(total_Json.toString(), "project_srl")
                                + "/" + Utils.getLanquageLocal(ProjectDetailActivity.this)
                                + "/" + Utils.getGenerateString()
                        ;
                        Status status = OAuthID.twitter.updateStatus(OAuthID.sns_message);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(ProjectDetailActivity.this, OAuthID.sns_message);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(ProjectDetailActivity.this, getString(R.string.sns_aleady_update));
                            }
                        });
                    }
                }
            }.start();
        } else {
            onCreate(getIntent().getExtras());
        }

        isReview = intent.getBooleanExtra("review", false);//후기 구분자
        Button button_project_join = (Button) findViewById(R.id.button_project_join);
        if (isReview) {
            button_project_join.setText(R.string.go_project);
        } else {
            if (isHistory.equals("Y")) {
                button_project_join.setText(R.string.review_detail);

            } else {
                button_project_join.setText(R.string.project_join);
            }
        }


    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            itemSelectAction(parent, position);

            viewPager_preview.setCurrentItem(position);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void itemSelectAction(final AdapterView<?> parent, final int position) {
        count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            ((LinearLayout) parent.getChildAt(i).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#eceeee"));
        }
        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.RED);
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#eceeee"));
                    }
                }
        );
    }

    private void itemSelectAction1(final AdapterView<?> parent, final int position) {
        count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            ((LinearLayout) parent.getChildAt(i).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.RED);
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
        );
    }

    public CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, responseCode, data);
            if (responseCode == RESULT_OK) {
                Utils.setShareSnsIndex(this, Utils.getJsonData(total_Json.toString(), "project_srl"), "facebook");
                Toast.makeText(this, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getJsonDataMind(String result) {
        String myheart = "";
        String send_heart = "";
        String sSupportCnt = "";
        String sHearGoal = "";


        try {
            JSONObject json = new JSONObject(result);
            myheart = json.getString("my_heart");
            sHearNow = json.getString("heart_now");
            send_heart = json.getString("send_heart");
            sSupportCnt = json.getString("support_cnt");
            sSupportCnt = json.getString("heart_goal");
            if (send_heart.equals("0"))
                Utils.setToast(this, R.string.overmind);
            FanMindSetting.setMY_HEART(this, myheart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double sum = Utils.getPercent(sHearNow, sHearGoal);

        int extra = Integer.parseInt(sHearGoal) - Integer.parseInt(sHearNow);
        mPercent.setText(getString(R.string.listsupport01).replace("{ENTRY}", Utils.getMoney(String.valueOf(extra))));
        mBarPercent.setText(String.valueOf((int) sum));
        mSeekBar.setProgress((int) sum);
        mPeoPle.setText(getString(R.string.listsupportdetail02).replace("{PEOPLE}", Utils.getMoney(sSupportCnt)));
        mNowMind.setText(FanMindSetting.getMY_HEART(this));
        Utils.setToast(this, R.string.listsupport09);
        if (!FanMindSetting.getREVIEW_POPUP(this)) {
            FanMindSetting.setREVIEW_POPUP(this, true);
            review();
        }

    }

//

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onKeyBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    public void onBack(View v) {
        onBackPressed();
    }


    /**
     * 리뷰팝업
     */
    private void review() {
        String title = getString(R.string.review01);
        String content = getString(R.string.review02);
        String left = getString(R.string.update03);
        String right = getString(R.string.review03);
        showDialog(this, title, content, left, right);
    }

    public void showDialog(Context context, String title, String content, String left, String right) {
        mDialog = new CustomDialog(context, title, content, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                String address = "market://details?id=specup.fanmind&reviewid=0";
                Uri uri = Uri.parse(address);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        }, left, right);
        mDialog.show();
    }

    public void onGoEvent(View v) {
        Intent intent = new Intent(this, EventDetailActivity.class);

        intent.putExtra("srl", event_srl);
        intent.putExtra("end", event_end_date);
        startActivity(intent);
    }


    //youtube
    @Override
    public void onDestroy() {

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            mPlayer.setFullscreen(false);
        } else {
            ActivityManager.getInstance().deleteActivity(this);
            if (projectStack != null) {
                ProjectStackVO projectStackValue = projectStack.pop();
                if (projectStackValue != null) {
                    Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailActivity.class);
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
        super.onBackPressed();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        mPlayer = youTubePlayer;
        mPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean _isFullScreen) {
                isFullScreen = _isFullScreen;
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        mPlayer = null;
    }


    public void onClickDeliveryImformation(View v) {

        Intent intent = new Intent(this, ProjectDetailDeliveryImformation.class);
        intent.putExtra("total_Json", total_Json.toString());
        startActivity(intent);

    }

    public void onClickDeliveryRule(View v) {
        Intent intent = new Intent(this, ProjectDetailDeliveryRule.class);
        intent.putExtra("total_Json", total_Json.toString());
        startActivity(intent);
    }


    AdapterView.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            itemSelectAction1(parent, position);

            SupportList supportList = (SupportList) ((HorizontalListView) parent).getAdapter().getItem(position);
            Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailActivity.class);
            intent.putExtra("srl", supportList.getSrl());
            ProjectStackVO projectStackVO = new ProjectStackVO();
            projectStackVO.setSrl(supportList.getSrl());
            projectStack.push(projectStackVO);
            setIntent(intent);
            startActivity(intent);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 프로젝트 알림 버튼
     *
     * @param v
     */
    public void onNotice(View v) {
        if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
            Intent intent = new Intent(this, NoticeActivity2.class);
            intent.putExtra("total_Json", total_Json.toString());
            startActivity(intent);
        } else {
            Utils.showDialog(ProjectDetailActivity.this);
        }


    }

    public void onShare(View v) {

        if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
            FragmentManager fm = getFragmentManager();
            AlertDialogShare dialog = new AlertDialogShare(this, total_Json.toString(), callbackManager);
            dialog.show(fm, "dialog1");
        } else {
            Utils.showDialog(ProjectDetailActivity.this);
        }

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                Intent intent = new Intent(ProjectDetailActivity.this, ProjectPayment.class);
                intent.putExtra("total_Json", total_Json.toString());
                startActivity(intent);
            } else {
                Utils.showDialog(ProjectDetailActivity.this);
            }

        }
    };

    @Override protected void onSaveInstanceState(Bundle outState)
    {
         //super.onSaveInstanceState(outState);
    }

}

