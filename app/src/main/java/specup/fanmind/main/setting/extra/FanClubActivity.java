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

public class FanClubActivity extends Activity implements OnTask {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_fanclub);
		setView();
	}
	
	
	EditText mEt[] = new EditText[5];
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	
	private void setView(){
		mEt[0] = (EditText)findViewById(R.id.fanclub_et01);
		mEt[1] = (EditText)findViewById(R.id.fanclub_et02);
		mEt[2] = (EditText)findViewById(R.id.fanclub_et03);
		mEt[3] = (EditText)findViewById(R.id.fanclub_et04);
		mEt[4] = (EditText)findViewById(R.id.fanclub_et05);
		
		Button mBtn = (Button)findViewById(R.id.fanclub_btn01);
		mBtn.requestFocus();
		mBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.checkLength(mEt[0])){
					Utils.setToast(FanClubActivity.this, R.string.company21);
				}else if(Utils.checkLength(mEt[1])){
					Utils.setToast(FanClubActivity.this, R.string.company22);
				}else if(Utils.checkLength(mEt[2])){
					Utils.setToast(FanClubActivity.this, R.string.company13);
				}else if(Utils.checkLength(mEt[3])){
					Utils.setToast(FanClubActivity.this, R.string.company14);
				}else if(Utils.checkLength(mEt[4])){
					Utils.setToast(FanClubActivity.this, R.string.company15);
				}else{
					sendCompany();
				}
			}
		});
	}
	
	private void sendCompany(){
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("star_name", mEt[0].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("club_name", mEt[1].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("master_name", mEt[2].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("phone", mEt[3].getText().toString().trim()));
		mParams.add(new BasicNameValuePair("text", mEt[4].getText().toString().trim()));
		
		HttpRequest.setHttp(FanClubActivity.this, mAsyncTask, mParams, URLAddress.FAN_CLUB(FanClubActivity.this), AsyncTaskValue.FAN_CLUB, this);
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.FAN_CLUB_NUM){
			if(Utils.getJsonData(result)){
				Utils.setToast(FanClubActivity.this, R.string.setting08);
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
