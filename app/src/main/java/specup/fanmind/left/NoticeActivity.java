package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.URLAddress;

/**
 * 전체 공지사항
 */
public class NoticeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Tracker t = ((FanMindApplication)getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Notice");
		t.send(new HitBuilders.AppViewBuilder().build());


		Utils.setAnalyticsTracker(getIntent(), this);

		ActivityManager.getInstance().addActivity(this);

		setContentView(R.layout.fragment_notice);
		isNoti = getIntent().getBooleanExtra("isNoti", false);
		setView();

	}


	WebView mWebView;
	boolean isNoti;


	private void setView(){
		mWebView =(WebView)findViewById(R.id.notice_webview);
		mWebView.loadUrl(URLAddress.NOTICE(this));

		mWebView.getSettings().setJavaScriptEnabled( true );
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient() );
	}

	public void onBack(View v){
		onBack();
	}

	public void onBack(){
		if(mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			if(isNoti){
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			ActivityManager.getInstance().deleteActivity(this);
//			if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//			((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
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

}
