package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.fanmindsetting.FanMindSetting;

public class BuyPointCompleteActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_buypointcomplete);
		
		TextView text = (TextView)findViewById(R.id.payaccount_tv01);
		text.setText(getString(R.string.buypointcomplete01).replace("{ID}", FanMindSetting.getEMAIL_ID(this)));
	}
	
	public void onClick(View v){//마음내역 확인
		startActivity(new Intent(this, MyPointActivity.class));
		ActivityManager.getInstance().deleteActivity2(this);
	}
	public void onClick2(View v){//이전으로 가기
		ActivityManager.getInstance().deleteActivity2(this);
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
