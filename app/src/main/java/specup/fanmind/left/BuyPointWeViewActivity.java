package specup.fanmind.left;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;


public class BuyPointWeViewActivity extends Activity implements OnTask {

    Context mContext;
    RelativeLayout mLayout, infoLayout;
    private WebView webView;
    boolean isISP_call= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "BuyPoint");
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_buypoint_webview);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);//자바스크립트 이용
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (url != null && url.startsWith("market://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                if (url != null && url.startsWith("https://fanmaum.com/members/thanks")) {
                    startActivity(new Intent(BuyPointWeViewActivity.this, BuyPointCompleteActivity.class));
                    finish();
                }
                if (url != null) {
                    if (url.startsWith("ispmobile:")) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            isISP_call = true;
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException ex) {
                            isISP_call = false;
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp"));
                            startActivity(intent);
                        }
                        return true;
                    } else if (url.contains("market://")
                            || url.endsWith(".apk")
                            || url.contains("http://market.android.com")
                            || url.contains("vguard")
                            || url.contains("v3mobile")
                            || url.contains("ansimclick")
                            || url.endsWith("ansimclick")
                            || url.toLowerCase().contains("vguardstart")
                            || url.contains("lottesmartpay://")
                            || url.contains("mvaccine")
                            || url.contains("smhyundaiansimclick://")
                            || url.contains("smshinhanansimclick://")
                            || url.contains("smshinhancardusim://")
                            || url.contains("hanaansim://")
                            || url.contains("citiansimmobilevaccine://")
                            || url.contains("droidxantivirus")
                            || url.contains("http://m.ahnlab.com/kr/site/download")
                            || url.contains("ilkansimmobilevaccine://")
                            || url.contains("mpocketansimclick://")
                            || url.contains("cloudpay") // �ϳ�SKī��
                            || url.contains("ispmobile://")
                            || url.contains("shinhan-sr-ansimclick://")
                            || url.contains("SAMSUNG")
                            || url.contains("smartwall://")
                            || url.contains("intmoney") // Ƽ�Ӵ� �ξ�
                            || url.contains("smartxpay") // ������ü(smartXpay)
                            || url.contains("market://details?id=com.shcard.smartpay")
                            || url.contains("appfree://")) {
                        Intent intent = null;
                        // 인텐트 정합성 체크 : 2014 .01추가
                        try {
                            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        } catch (URISyntaxException ex) {
                            Log.e("Browser", "Bad URI " + url + ":" + ex.getMessage());
                            return false;
                        }
                        try {
                            boolean retval = true;
                                                /*
                                                 * chrome 버젼 방식
                                                 * 기존 'APPNAME://DATA' 형식의 URL 대신 사용되는 신규 커스텀URL
                                                 * 호출할 앱의 패키지명과 DATA를 함께 전달하여 앱이 있을 경우 앱 호출, 없을 경우 패키지 명으로 마켓 검색 띄움
                                                 */
                            if (url.startsWith("intent")) { // chrome 버젼 방식
                                // 앱설치 체크를 합니다.
                                if (getPackageManager().resolveActivity(intent, 0) == null) {
                                    String packagename = intent.getPackage();
                                    if (packagename != null) {
                                        Uri uri = Uri.parse("market://search?q=pname:" + packagename);
                                        intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                        retval = true;
                                    }
                                } else {
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setComponent(null);
                                    try {
                                        if (startActivityIfNeeded(intent, -1)) {
                                            retval = true;
                                        }
                                    } catch (ActivityNotFoundException ex) {
                                        retval = false;
                                    }
                                }
                            } else { // 구 방식
                                Uri uri = Uri.parse(url);
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                retval = true;
                            }
                            return retval;
                        } catch (ActivityNotFoundException e) {
                            Log.e("error ===>", e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        view.loadUrl(url);
                        return true;
                    }
                }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient());


        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        String param = "";
        param += "&sskey=" + FanMindSetting.getSESSION_KEY(this);
        param += "&ssid=" + FanMindSetting.getEMAIL_ID(this);
        param += "&locale=" + Utils.getLanquageLocal(this);
        param += "&timezone=" + Utils.getTimeZone();

        webView.loadUrl(URLAddress.MEMBERS_PURCHASE() + param);
//
//        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(URLAddress.MEMBERS_PURCHASE() + param));
//        startActivity(i);
//

    }

    public void onBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        if (webView.canGoBack())   //뒤로갈페이지가있으면
            webView.goBack();
        else
            finish();
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
    public void onTask(int output, String result) {

    }


    public void onSeePay(View v) {
        Intent intent = new Intent(this, PayAccountCheckActivity.class);
        startActivity(intent);
    }
}
