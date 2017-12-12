package specup.fanmind.left;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.fanmindsetting.FanMindSetting;

public class PayAccountCompleteActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payaccountcomplete);
		
		TextView text = (TextView)findViewById(R.id.payaccount_tv01);
		text.setText(getString(R.string.payaccount03).replace("{ID}", FanMindSetting.getEMAIL_ID(this)));
	}
	
	public void onClick(View v){
		setResult(RESULT_OK);
		finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
