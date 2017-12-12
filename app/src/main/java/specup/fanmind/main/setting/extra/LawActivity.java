package specup.fanmind.main.setting.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;

public class LawActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_law);
		setView();
	}


	ScrollView mScroll01, mScroll02;
	TextView mText02;
	private void setView(){
		mText02 = (TextView)findViewById(R.id.law_tv03);
		if(Utils.getLocal(this, getString(R.string.china))){
			mText02.setVisibility(View.INVISIBLE);
		}
		
		mScroll01 = (ScrollView)findViewById(R.id.law_scroll01);
		mScroll02 = (ScrollView)findViewById(R.id.law_scroll02);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(	Utils.getDP(this, 331),
				Utils.getDP(this, 195));
		mParams.gravity = Gravity.CENTER_HORIZONTAL;
		mParams.topMargin = Utils.getDP(this, 15);
		if(getResources().getDisplayMetrics().heightPixels != Utils.getDP(this, 640)){
			mScroll01.setLayoutParams(mParams);
			mScroll02.setLayoutParams(mParams);
		}
	}
	
	public void onBack(View v){
		ActivityManager.getInstance().deleteActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			ActivityManager.getInstance().deleteActivity(this);
		}
		return super.onKeyDown(keyCode, event);
	}

}
