package specup.fanmind.main.setting.extra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

/**
 *  팬마음 소개 및 제휴 문의
 */
public class CompanyCoverActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_companycover);
		setView();
	}
	
	
	private void setView(){
		Button mBtn =(Button)findViewById(R.id.companycover_btn01);
		mBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CompanyCoverActivity.this, CompanyActivity.class));
			}
		});
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
