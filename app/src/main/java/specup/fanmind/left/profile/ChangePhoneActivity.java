package specup.fanmind.left.profile;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class ChangePhoneActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_changephone);
		setView();
	}
	
	
	TextView mTvReSend;
	Button mBtn[] = new Button[4];
	EditText mPhoneEt, mCertiEt;
	List<NameValuePair> mParams;
	AsyncTask mAsyncTask;
	String mPhoneNumber;
	boolean isOK;
	
	private void setView(){
		mTvReSend = (TextView)findViewById(R.id.changephone_tv05);
		String fullSend = getString(R.string.step03);
		String before = fullSend.substring(0, fullSend.length()-1);
		String after = fullSend.substring(fullSend.length()-1, fullSend.length());
		mTvReSend.setText(Html.fromHtml("<u><font color=#2274b6>"+before+"</font></u>"+after));
		
		mBtn[0] = (Button)findViewById(R.id.changephone_btn01);
		mBtn[1] = (Button)findViewById(R.id.changephone_btn02);
		mBtn[2] = (Button)findViewById(R.id.changephone_btn03);
		mBtn[3] = (Button)findViewById(R.id.changephone_btn04);
		
		mPhoneEt = (EditText)findViewById(R.id.changephone_et01);
		mCertiEt = (EditText)findViewById(R.id.changephone_et02);
		
		for(int i=0; i<mBtn.length; i++){
			mBtn[i].setOnClickListener(mClick);
		}
		
	}
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.changephone_btn01 :
				//인증번호 발송.
				sendPhone();
				break;
			case R.id.changephone_btn02 :
				//인증번호 확인
				certiConfirm();
				break;
			case R.id.changephone_btn03 :
				//등록
				changePhone();
				break;
			case R.id.changephone_btn04 :
				//취소
				onFinish();
				break;
			}
		}
	};
	
	
	
	private void certiConfirm(){
		if(!Utils.checkLength(mCertiEt)){
			mParams = new ArrayList<NameValuePair>();
			mParams.add(new BasicNameValuePair("cert_code", mCertiEt.getText().toString().trim()));
			mParams.add(new BasicNameValuePair("phone", mPhoneEt.getText().toString().trim()));
			HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOGIN_CHECK_CERT(this),
                    AsyncTaskValue.LOGIN_CERTI02, this);
		}else{
			Utils.setToast(this, R.string.blank05);
		}
	}
	
	
	private void sendPhone(){
		if(!Utils.checkLength(mPhoneEt)){
			if(Utils.isCellphone(mPhoneEt.getText().toString().trim())){
				mParams = new ArrayList<NameValuePair>();
				mParams.add(new BasicNameValuePair("phone", mPhoneEt.getText().toString().trim()));
				HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOGIN_SEND_CERT(this),
						AsyncTaskValue.LOGIN_CERTI01, this);
			}else{
				Utils.setToast(this, R.string.notok06);
			}
		}else{
			Utils.setToast(this, R.string.blank04);
		}
	}
	
	private void changePhone(){
		if(isOK){
			mParams = new ArrayList<NameValuePair>();
			mParams.add(new BasicNameValuePair("new", mPhoneEt.getText().toString().trim()));
			mParams = Utils.setSession(this, mParams);
			HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.CHANGE_PHONE(this),
					AsyncTaskValue.CHANGE_PHONE, this);
		}else{
			Utils.setToast(this, R.string.notok02);
		}
	}
	
	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.LOGIN_CERTI01_NUM){
			if(result.contains("1301")){
				Utils.setToast(this, R.string.certi01);
				mPhoneNumber = mPhoneEt.getText().toString().trim();
			}else if(result.contains("1302")){//발송.
				Utils.setToast(this, R.string.certi03);
			}else{
				Utils.setToast(this, R.string.certi02);
			}
		}else if(output == AsyncTaskValue.LOGIN_CERTI02_NUM){
			if(result.contains("1401")){ //성공
				Utils.setToast(this, R.string.notok05);
				isOK = true;
			}else{
				Utils.setToast(this, R.string.notok02);
				isOK = false;
			}
		}else if(output == AsyncTaskValue.CHANGE_PHONE_NUM){
			if(result.contains("4501")){//성공
				Utils.setToast(this, R.string.changephone02);
			}else if(result.contains("4502")){//중복
				Utils.setToast(this, R.string.certi03);
			}else{//인증되지 않음
				Utils.setToast(this, R.string.notok02);
			}
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
