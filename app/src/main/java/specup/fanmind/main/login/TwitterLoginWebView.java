package specup.fanmind.main.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

/**
 * 트위터에 사용.
 */
public class TwitterLoginWebView extends Activity {
    private WebView webView;
    final Handler handler = new Handler();

    public static final String CUSTOM_INTENT = "com.fanmaum";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        ActivityManager.getInstance().addActivity(this);

        String url = getIntent().getStringExtra("url");
        final int pageType = getIntent().getIntExtra("pageType",0);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);//자바스크립트 이용
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http://twitterresult.com")) {
                    Intent intent = new Intent();
                    intent.setAction(CUSTOM_INTENT);
                    sendBroadcast(intent);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    if(pageType == OAuthID.LOGIN_PAGE){
                        intent.setData(Uri.parse(url.replace("http", "fanmaum2")));
                    }else if(pageType == OAuthID.PROFILE_PAGE){
                        intent.setData(Uri.parse(url.replace("http", "fanmaum3")));
                    }

                    startActivity(intent);
                    ActivityManager.getInstance().deleteActivity(TwitterLoginWebView.this);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        if (webView.canGoBack())   //뒤로갈페이지가있으면
            webView.goBack();
        else
            finish();
    }

    //메세지 받아서 처리하는 부분
    private class WebFormCheck {
        public void setMessage(final String arg) {
            handler.post(new Runnable() {
                public void run() {
                    Log.d("forminsert", "setMessage(" + arg + ")");
                    if (arg.equals("festival")) {
                        //페스티벌 Activity 호출
                    }
                }
            });
        }
    }


}
