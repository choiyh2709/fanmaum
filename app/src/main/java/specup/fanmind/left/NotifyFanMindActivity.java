package specup.fanmind.left;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

import java.util.Hashtable;
import java.util.Map;

import specup.fanmind.R;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.fanmindsetting.FanMindSetting;

/**
 * 팬마음 공유하기
 */
public class NotifyFanMindActivity extends Activity {

    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

    private TextView mText[] = new TextView[5];
    private RelativeLayout mLayout[] = new RelativeLayout[4];
    //    UiLifecycleHelper uiHelper;
    private int mKind = 0;
    String sSrl, sKakao, sStarSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_newfeed);
        setView();

        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Invite Friend Menu");
        t.send(new HitBuilders.AppViewBuilder().build());


//        uiHelper = new UiLifecycleHelper(this, null);
//        uiHelper.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mKind = intent.getIntExtra("kind", 0);
        sSrl = intent.getStringExtra("srl");
        sStarSrl = intent.getStringExtra("star_srl");
        sKakao = intent.getStringExtra("kakao");
    }


    private void setView() {
        mText[0] = (TextView) findViewById(R.id.pop_tv01);
        mText[1] = (TextView) findViewById(R.id.pop_tv02);
        mText[2] = (TextView) findViewById(R.id.pop_tv03);
        mText[3] = (TextView) findViewById(R.id.pop_tv04);
        mText[4] = (TextView) findViewById(R.id.pop_tv05);

        mLayout[0] = (RelativeLayout) findViewById(R.id.pop_layout02);
        mLayout[1] = (RelativeLayout) findViewById(R.id.pop_layout03);
        mLayout[2] = (RelativeLayout) findViewById(R.id.pop_layout04);
        mLayout[3] = (RelativeLayout) findViewById(R.id.pop_layout05);

        mLayout[0].setOnClickListener(mClick);
        mLayout[1].setOnClickListener(mClick);
        mLayout[2].setOnClickListener(mClick);
        mLayout[3].setOnClickListener(mClick);

        mText[0].setText(R.string.left07);
        mText[1].setText(R.string.notify01);
        mText[2].setText(R.string.notify02);
        mText[3].setText(R.string.notify03);
        mText[4].setText(R.string.notify04);
    }

    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.pop_layout02: //카카오톡 링크
                    sendKakaoTalkLink();
                    break;
                case R.id.pop_layout03: //라인
                    sendLine();
                    break;
                case R.id.pop_layout04://카카오 스토리
                    try {
                        sendPostingLink(v);
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.pop_layout05://페이스북
                    mFacebook();
                    break;
            }
        }
    };


    private void sendLine() {
        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);

        String lineComment = "";
        if (appInstalled(NotifyFanMindActivity.this, "jp.naver.line.android")) {
            if (mKind == 0) {
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("LeftMenu Line")
                        .build());

                lineComment = getString(R.string.line01).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
            } else if (mKind == 1) {

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("Request Star Line")
                        .build());


                lineComment = getString(R.string.line02).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
            } else if (mKind == 2) {

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("Support Line")
                        .build());


                lineComment = getString(R.string.line03).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
            }
            sendTextToLine(NotifyFanMindActivity.this, lineComment);
        } else {
            goDownload();
        }
    }


    /**
     * Line 텍스트 보내기.
     *
     * @param context
     * @param lineComment
     */
    public static void sendTextToLine(Context context, String lineComment) {
        try {
            String lineString = "line://msg/text/" + lineComment;
            Intent intent = Intent.parseUri(lineString, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 라인 다운로드 연결.
     */
    private void goDownload() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("market://details?id=jp.naver.line.android"));
        startActivity(i);
    }


    /**
     * 카카오스토리 다운로드 연결.
     */
    private void goKakaoStoryDownload() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("market://details?id=com.kakao.story"));
        startActivity(i);
    }


    /**
     * 라인 어플 확인
     *
     * @param context
     * @param uri     패키지명
     * @return
     */
    public boolean appInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }


    private boolean checkLogin() {
        // 로그인 여부 확인
//        Session session = Session.getActiveSession();
//        if (session == null)
//            return false;
//
//        if (!session.isOpened())
//            return false;

        return true;
    }


    private void mFacebook() {
        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);

        String pic = "";
        if (mKind == 0) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("LeftMenu Facebook")
                    .build());

            pic = "http://app.fanmaum.com/images/fm1.jpeg";
        } else if (mKind == 1) {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("Request Star Facebook")
                    .build());

            pic = "http://app.fanmaum.com/images/fm2.jpeg";
        } else {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("Support Facebook")
                    .build());

            pic = sKakao;
        }
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("팬마음")
                    .setContentDescription(getString(R.string.line03))
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=specup.fanmind"))
                    .setImageUrl(Uri.parse(pic))
                            .build();
            ShareDialog shareDialog = new ShareDialog(this);
            FacebookSdk.sdkInitialize(getApplicationContext());
            shareDialog.show(linkContent);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


    //    	private final String imageSrc = "http://dn.api1.kage.kakao.co.kr/14/dn/btqaWmFftyx/tBbQPH764Maw2R6IBhXd6K/o.jpg";
    private String imageSrc = null;


    private void sendKakaoTalkLink() {
        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            if (mKind == 0) {//메인 서포트

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("LeftMenu Kakao Link")
                        .build());

                imageSrc = "http://app.fanmaum.com/images/fm1.jpeg";
                kakaoTalkLinkMessageBuilder.addText(getString(R.string.kakao01).replace("{NICK}", FanMindSetting.getNICK_NAME(this)));
                kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                        new AppActionBuilder()
                                .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("main=1111").setMarketParam("referrer=kakaotalklink").build())
                                .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                );

            } else if (mKind == 1) {//스타 요청.

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("Request Star Kakao Link")
                        .build());

                imageSrc = "http://app.fanmaum.com/images/fm2.jpeg";
                kakaoTalkLinkMessageBuilder.addText(getString(R.string.kakao02).replace("{NICK}", FanMindSetting.getNICK_NAME(this)));
                kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                        new AppActionBuilder()
                                .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("star=1111").setMarketParam("referrer=kakaotalklink").build())
                                .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                );
            } else if (mKind == 2) { //서포트

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Invite Friend")
                        .setAction("Press Button")
                        .setLabel("Support Kakao Link")
                        .build());

                imageSrc = sKakao;
                kakaoTalkLinkMessageBuilder.addText(getString(R.string.kakao03).replace("{NICK}", FanMindSetting.getNICK_NAME(this)));
                kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                        new AppActionBuilder()
                                .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("support=" + sSrl + "/" + sStarSrl).setMarketParam("referrer=kakaotalklink").build())
                                .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                );
            }

            kakaoTalkLinkMessageBuilder.addImage(imageSrc, 740, 740);

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        uiHelper.onDestroy();
    }

    // Recommened Charset UTF-8
    private String encoding = "UTF-8";

    public void sendPostingLink(View v) throws NameNotFoundException {
        Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
        urlInfoAndroid.put("title", "팬마음 친구초대하기");
        urlInfoAndroid.put("desc", "팬마음 : market://details?id=specup.fanmind");
        String msg = "";

        Tracker t = ((FanMindApplication) getApplication()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);

        if (mKind == 0) {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("LeftMenu Kakao Story")
                    .build());

            urlInfoAndroid.put("imageurl", new String[]{"http://app.fanmaum.com/images/fm1.jpeg"});
            msg = getString(R.string.kakao01).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
        } else if (mKind == 1) {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("Request Star Kakao Story")
                    .build());

            urlInfoAndroid.put("imageurl", new String[]{"http://app.fanmaum.com/images/fm2.jpeg"});
            msg = getString(R.string.kakao02).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
        } else if (mKind == 2) {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Invite Friend")
                    .setAction("Press Button")
                    .setLabel("Support Kakao Story")
                    .build());

            urlInfoAndroid.put("imageurl", new String[]{sKakao});
            msg = getString(R.string.kakao03).replace("{NICK}", FanMindSetting.getNICK_NAME(this));
        }
        // Recommended: Use application context for parameter.
        StoryLink storyLink = StoryLink.getLink(getApplicationContext());

        // check, intent is available.
        if (!storyLink.isAvailableIntent()) {
            goKakaoStoryDownload();
            return;
        }

        /**
         * @param activity
         * @param post (message or url)
         * @param appId
         * @param appVer
         * @param appName
         * @param encoding
         * @param urlInfoArray
         */
        storyLink.openKakaoLink(this,
                /*"market://details?id=jp.naver.line.android"*/msg,
                getPackageName(),
                getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
                "팬마음",
                encoding,
                urlInfoAndroid);
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

}
