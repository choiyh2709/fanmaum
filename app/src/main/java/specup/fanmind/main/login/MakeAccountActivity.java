package specup.fanmind.main.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.igaworks.IgawCommon;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;

public class MakeAccountActivity extends FragmentActivity{

	public static Context mMakeAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActivityManager.getInstance().addActivity2(this);
		
		setContentView(R.layout.activity_makeaccount);
		setView();

	}

	public TextView mTitle;
	private void setView(){
		mMakeAccount = MakeAccountActivity.this;
		mTitle = (TextView)findViewById(R.id.makeaccount_tv01);
		showStep1();
	}


	//Back Button 클릭시
	public void onBack(View v){
		if(getSupportFragmentManager().getBackStackEntryCount()!=0){
			getSupportFragmentManager().popBackStack();
			mTitle.setText(R.string.makeaccount01);
		}else{
			finish();
		}
	}

	//회원가입 1단계
	private void showStep1(){
		Step1Fragment step1 = new Step1Fragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(step1);
		ft.replace(R.id.makeaccount_frame, step1, "step1");
		ft.commitAllowingStateLoss();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode != RESULT_OK){
			return;
		}

		if(requestCode== Utils.WHERE_LIVE || requestCode == Utils.STAR_LIST || requestCode == Utils.BIRTH_YEAR){
			Fragment frag =getSupportFragmentManager().findFragmentByTag("step2");
			frag.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityManager.getInstance().deleteActivity2(this);
		mMakeAccount = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Fragment currentFrag =  getSupportFragmentManager().findFragmentById(R.id.makeaccount_frame);
			if(currentFrag.getTag() !=null){
				if(currentFrag.getTag().equals("step2")){
					mTitle.setText(R.string.makeaccount01);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IgawCommon.startSession(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		IgawCommon.endSession();
	}
	
}

