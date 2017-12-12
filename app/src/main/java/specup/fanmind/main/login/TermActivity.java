package specup.fanmind.main.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.igaworks.IgawCommon;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;

public class TermActivity extends Activity{

	TextView mTerm01, mTerm02;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity2(this);
		setContentView(R.layout.activity_term);
		mTerm02= (TextView)findViewById(R.id.term_tv03);
		if(Utils.getLocal(this, getString(R.string.china))){
			mTerm02.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public void onClose(View v){
		ActivityManager.getInstance().deleteActivity2(this);
		finish();
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
