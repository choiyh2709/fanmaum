package specup.fanmind.main.setting.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class CompanyActivity extends Activity implements OnTask {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_company);
		setView();
		
	}

	EditText mEt[] = new EditText[5];
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;

	private void setView(){
		mEt[0] = (EditText)findViewById(R.id.company_et01);
		mEt[1] = (EditText)findViewById(R.id.company_et02);
		mEt[2] = (EditText)findViewById(R.id.company_et03);
		mEt[3] = (EditText)findViewById(R.id.company_et04);
		mEt[4] = (EditText)findViewById(R.id.company_et05);

		Button mBtn = (Button)findViewById(R.id.company_btn01);
		mBtn.requestFocus();
		mBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.checkLength(mEt[0])){
					Utils.setToast(CompanyActivity.this, R.string.company11);
				}else if(Utils.checkLength(mEt[1])){
					Utils.setToast(CompanyActivity.this, R.string.company12);
				}else if(Utils.checkLength(mEt[2])){
					Utils.setToast(CompanyActivity.this, R.string.company13);
				}else if(Utils.checkLength(mEt[3])){
					Utils.setToast(CompanyActivity.this, R.string.company14);
				}else if(Utils.checkLength(mEt[4])){
					Utils.setToast(CompanyActivity.this, R.string.company15);
				}else{
					sendCompany();
				}
			}
		});
	}


	private void sendCompany(){
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("title", mEt[0].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("company", mEt[1].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("name", mEt[2].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("phone", mEt[3].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("text", mEt[4].getText().toString().trim()));
		HttpRequest.setHttp(CompanyActivity.this, mAsyncTask, mParams, URLAddress.CONTACT(CompanyActivity.this), AsyncTaskValue.CONTACT, this);
	}


	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.CONTACT_NUM){
			if(Utils.getJsonData(result)){
				Utils.setToast(CompanyActivity.this, R.string.setting08);
				ActivityManager.getInstance().deleteActivity(this);
			}
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
