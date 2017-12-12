package specup.fanmind.left;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.GoogleAnalytics;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.URLAddress;

/**
 * 공지사항 detail
 */
public class NoticeActivityDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "NoticeDetail");
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_notice_detail);
        setView();

    }


    private void setView() {

        WebView webview_notice = (WebView)findViewById(R.id.webview_notice);
        webview_notice.getSettings().setJavaScriptEnabled(true);//자바스크립트 이용
        webview_notice.setWebViewClient(new WebViewClient());
//        webview_notice.loadUrl("http://www.naver.com");
        webview_notice.loadUrl(URLAddress.NOTICE_DETAIL(getIntent().getStringExtra("project_srl"),getIntent().getStringExtra("position")));



    }

    public void onBack(View v) {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        super.onBackPressed();
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

}
