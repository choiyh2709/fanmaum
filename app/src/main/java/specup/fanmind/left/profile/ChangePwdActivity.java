package specup.fanmind.left.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;


public class ChangePwdActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_changepwd);
		setView();
	}

	EditText mEtNow, mEtNew1, mEtNew2;
	CustomDialog mCustom;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;

	private void setView(){
		mEtNow = (EditText)findViewById(R.id.changepwd_et01);
		mEtNew1 = (EditText)findViewById(R.id.changepwd_et02);
		mEtNew2 = (EditText)findViewById(R.id.changepwd_et03);

		Button mCancel =(Button)findViewById(R.id.changepwd_btn01);
		Button mOk =(Button)findViewById(R.id.changepwd_btn02);

		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onFinish();
			}
		});

		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager mInputMethodManager = (InputMethodManager)ChangePwdActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(mEtNow.getWindowToken(), 0);
				sendCheck();
			}
		});
	}


    /**
   	 *확인 버튼 눌렀을시
   	 */
	private void sendCheck(){
		String title = getString(R.string.changepwd06);
		if(Utils.checkLength(mEtNow)){
			Utils.setToast(this, R.string.changepwd1);
		}else if(Utils.checkLength(mEtNew1)){
			Utils.setToast(this, R.string.changepwd2);
		}else if(Utils.checkLength(mEtNew2)){
			Utils.setToast(this, R.string.blank03);
		}else if(mEtNew1.getText().toString().trim().length() <5){
			Utils.setToast(this, R.string.changepwd3);
		}else if(!Utils.isPassword(mEtNew1.getText().toString().trim())){
			Utils.setToast(this, R.string.changepwd4);
		}else if(!mEtNew1.getText().toString().trim().equals(mEtNew2.getText().toString().trim())){
			String content = getString(R.string.failimage02);
			notokPopup(title, content);
		}else{
			mParams = new ArrayList<NameValuePair>();
			mParams.add(new BasicNameValuePair("old", mEtNow.getText().toString()));
			mParams.add(new BasicNameValuePair("new", mEtNew1.getText().toString()));
			mParams = Utils.setSession(this, mParams);
			HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.CHANGE_PWD(this),
                    AsyncTaskValue.CHANGE_PWD, this);
		}
	}

	@Override
	public void onTask(int output, String result) {
		if(output == AsyncTaskValue.CHANGE_PWD_NUM){
			if(result.contains("4401")){ //비밀번호 변경 성공
				getJsonData(result);
			}else if(result.contains("4402")){//현재 비밀번호 틀림
				String title = getString(R.string.changepwd06);
				String content = getString(R.string.failimage01);
				notokPopup(title, content);
			}
		}
	}

	private void getJsonData(String result){
		try{
			JSONObject json = new JSONObject(result);
			String sskey = json.getString("sskey");
			Utils.setToast(this, R.string.changepwd5);
			FanMindSetting.setSESSION_KEY(this, sskey);
			FanMindSetting.setPASSWORD(this, mEtNew1.getText().toString().trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		onFinish();
	}

	CustomDialog mCustomDialog;
	private void notokPopup(String title, String content){
		String lBtnText= getString(R.string.confirmation);
		mCustomDialog = new CustomDialog(ChangePwdActivity.this, title, content, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomDialog.dismiss();
			}
		}, lBtnText);
		mCustomDialog.show();
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
