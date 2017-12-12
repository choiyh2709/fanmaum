package specup.fanmind.left.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;


public class FindPwdActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_findpwd);
		setView();
	}
	
	@Override
	public void onTask(int output, String result) {
		if(output == AsyncTaskValue.LOGIN_FINDPW_NUM){
			if(Utils.getJsonDataString(result).equals("1801")){
//				Utils.setToast(this, R.string.login05);
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
				Utils.setToast(this, R.string.againlogin);
				ActivityManager.getInstance().allEndActivity();
				finish();
			}else if(Utils.getJsonDataString(result).equals("1802")){//가입되지 않은 아이디
				Utils.setToast(this, R.string.login06);
			}
		}
		// TODO Auto-generated method stub
	}

	EditText mFindPwd;
	AsyncTask mTask;
	List<NameValuePair> mParams;

	private void setView(){
		mFindPwd = (EditText)findViewById(R.id.findpwd_et01);
		Button mCancel =(Button)findViewById(R.id.findpwd_btn01);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onFinish();
			}
		});

		Button mOK = (Button)findViewById(R.id.findpwd_btn02);
		mOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findPW();
			}
		});
	}

	private void findPW(){
		if(Utils.checkLength(mFindPwd)){
			Utils.setToast(this, R.string.blank01);
		}else if(!Utils.isEmailValid(mFindPwd.getText().toString())){
			Utils.setToast(this, R.string.notok04);
		}else{
			mParams = new ArrayList<NameValuePair>();
			mParams.add(new BasicNameValuePair("id", mFindPwd.getText().toString().trim()));
			HttpRequest.setHttp(this, mTask, mParams, URLAddress.LOGIN_FIND_PW(this),
                    AsyncTaskValue.LOGIN_FINDPW, FindPwdActivity.this);
		}
	}

	private void onFinish(){
		ActivityManager.getInstance().deleteActivity(this);
	}
	
	public void onBack(View v){
		onFinish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
