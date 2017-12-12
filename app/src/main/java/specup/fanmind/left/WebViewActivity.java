package specup.fanmind.left;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.net.URISyntaxException;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.URLAddress;


public class WebViewActivity extends Activity{

	WebView mWebView;
	int mSort;
	final Activity myApp = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_webview);
		setView();
	}

	private void setView(){
		mSort = getIntent().getIntExtra("sort", 0);
		mWebView= (WebView)findViewById(R.id.webview);
		if(mSort ==0){
			Tracker t = ((FanMindApplication)getApplication()).getTracker(
					FanMindApplication.TrackerName.APP_TRACKER);
			t.setScreenName("Pay Account");
			t.send(new HitBuilders.AppViewBuilder().build());
		}else if(mSort ==1){
			Tracker t = ((FanMindApplication)getApplication()).getTracker(
					FanMindApplication.TrackerName.APP_TRACKER);
			t.setScreenName("Pay Mobile");
			t.send(new HitBuilders.AppViewBuilder().build());
		}else if(mSort==2){
			Tracker t = ((FanMindApplication)getApplication()).getTracker(
					FanMindApplication.TrackerName.APP_TRACKER);
			t.setScreenName("Pay Card");
			t.send(new HitBuilders.AppViewBuilder().build());
		}


		String point = getIntent().getStringExtra("point");
		String param = getIntent().getDataString();
		String app_path = "fanmaum";

		if( param==null || param.equals("")){
			mWebView.loadUrl(URLAddress.getPayURL(this, mSort, point.replace(",", "")));
//			mWebView.loadUrl(URLAddress.getPayURL(this, mSort, "3700"));
			Log.e("123", URLAddress.getPayURL(this, mSort, point));
		}else{
			if(param.startsWith(app_path)){
				param = param.substring(app_path.length()+11);
				Log.e("param", param);
				mWebView.loadUrl(param);
			}
		}

		mWebView.getSettings().setJavaScriptEnabled( true );
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically (true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.getSettings().setSupportZoom(false);

		mWebView.setWebViewClient(new WebViewClientClass());
		mWebView.setWebChromeClient(new Alert());
	}

	private class Alert extends WebChromeClient{
		@Override
		public boolean onCreateWindow(WebView view, boolean dialog,
				boolean userGesture, Message resultMsg) {
			// TODO Auto-generated method stub
			return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result){
			new AlertDialog.Builder(myApp)
			.setTitle(R.string.alert)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                            if (mSort == 0) ActivityManager.getInstance().deleteActivity(myApp);
                        }
                    })
			.setCancelable(false)
			.create()
			.show();

			return true;
		};

		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
			new AlertDialog.Builder(myApp)
			.setTitle(R.string.alert)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
			.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
			.setCancelable(false)
			.create()
			.show();
			return true;
		};
	}

	boolean isISP_call= false;
	private class WebViewClientClass extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			if(mSort ==2){
				if(url!=null){
					if(url.startsWith("ispmobile:")){
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						try{
							isISP_call = true;
							startActivityForResult(intent, 0);
						}catch(ActivityNotFoundException ex){
							isISP_call = false;
							intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp"));
							startActivity(intent);
						}
						return true;
					}else if(url.contains("market://")
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
							|| url.contains("appfree://"))
					{
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
					}else{
						view.loadUrl(url);
						return true;
					}
				}
			}else if(mSort ==1){
				if(url.equals("https:///bill.fanmaum.com/billing/close.php")){
					ActivityManager.getInstance().deleteActivity(WebViewActivity.this);
				}
			}
			return true;
		}



		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			Log.e("onPageStarted", url);
			if(mSort ==0){
				if(url.equals("https://bill.fanmaum.com/billing/va_okurl.php")){
					startActivityForResult(new Intent(WebViewActivity.this, PayAccountCompleteActivity.class), Utils.PAY_ACCOUNT);
				}
			}else if(mSort ==1){
				if(url.equals("https://bill.fanmaum.com/billing/mc_okurl.php")){
					startActivity(new Intent(WebViewActivity.this, BuyPointCompleteActivity.class));
					finish();
				}
			}else if(mSort ==2){
				if(url.equals("https://bill.fanmaum.com/billing/result.php")){
					startActivity(new Intent(WebViewActivity.this, BuyPointCompleteActivity.class));
					finish();
				}else if(url.equals("https://bill.fanmaum.com/billing/cfail.php")){
					ActivityManager.getInstance().deleteActivity(WebViewActivity.this);
				}
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

//���� ������å�������� ����
//		@Override
//		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {   
//			handler.proceed(); // SSL 에러가 발생해도 계속 진행!
//		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mWebView.canGoBack()){
				mWebView.goBack();
				return true;
			}else{
				ActivityManager.getInstance().deleteActivity(this);
			}
		}
		return super.onKeyDown(keyCode, event);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode != RESULT_OK){
			return;
		}

		if(requestCode == Utils.PAY_ACCOUNT){
			startActivity(new Intent(this, PayAccountOneActivity.class));
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
