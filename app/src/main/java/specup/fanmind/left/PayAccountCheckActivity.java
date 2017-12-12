package specup.fanmind.left;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.PayAccountAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.SupportList;


public class PayAccountCheckActivity extends Activity implements OnTask {

	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	ListView mList;
	ArrayList<SupportList> mArrayList;
	PayAccountAdapter mPayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Tracker t = ((FanMindApplication)getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Pay Account Check");
		t.send(new HitBuilders.AppViewBuilder().build());

		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_payaccount);
		
		mParams = new ArrayList<NameValuePair>();
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.DAILY_TIME(this), AsyncTaskValue.DAILY_SELECT, this);
		
		setView();
	}


	private void setView(){
		mArrayList = new ArrayList<SupportList>();
		mList = (ListView)findViewById(R.id.payaccount_list);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v){
		onBack();
	}

	private void onBack(){
		ActivityManager.getInstance().deleteActivity(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}


	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output== AsyncTaskValue.DAILY_SELECT_NUM){
			if(Utils.getJsonData(result)){
				checkTime(result);
			}
		}else if(output == AsyncTaskValue.PAY_ACCOUNT_NUM){
			if(Utils.getJsonData(result)){
				getJsonData(result);
			}
		}else if(output== AsyncTaskValue.DAILY_INSERT_NUM){
			if(Utils.getJsonData(result)){
				Utils.setToast(this, R.string.payaccountcancel05);
				mParams = new ArrayList<NameValuePair>();
				mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
				mParams.add(new BasicNameValuePair("date", mFuture));
				HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_SELECT_ALL(this),
						AsyncTaskValue.PAY_ACCOUNT, this);
			}
		}
	}
	
	String mFuture;
	private void getJsonData(String result) {
		// TODO Auto-generated method stub
		mArrayList.removeAll(mArrayList);
		try{
			JSONObject list = new JSONObject(result);
			String mlist = list.getString("list");
			JSONArray jsonarray = new JSONArray(mlist);
			for(int i=0; i<jsonarray.length(); i++){
				String idx =jsonarray.getJSONObject(i).getString("idx");
				String Price =jsonarray.getJSONObject(i).getString("price");
				String Date=jsonarray.getJSONObject(i).getString("regdate");
				String BankCode = jsonarray.getJSONObject(i).getString("bankcode");
				String Name = jsonarray.getJSONObject(i).getString("uname");
				String AccountInfo = jsonarray.getJSONObject(i).getString("accountno");
				mArrayList.add(new SupportList(idx, Price, Date, BankCode, Name, AccountInfo));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		mPayAdapter = new PayAccountAdapter(this, mArrayList, mClick);
		mList.setAdapter(mPayAdapter);
	}
	
	private void checkTime(String key){
		String time =null;
		try{
			JSONObject list = new JSONObject(key);
			time = list.getString("0");
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.e("123", time);
		int year = Integer.parseInt(time.substring(0, 4));
		int month = Integer.parseInt(time.substring(4, 6));
		int day = Integer.parseInt(time.substring(6, 8));
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);
		Calendar today = Calendar.getInstance ();
		today.set(year, month-1, day);
		today.add(Calendar.DATE, -3);
		Date t = today.getTime();
		mFuture = sdf.format(t);

		mArrayList = new ArrayList<SupportList>();
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
		mParams.add(new BasicNameValuePair("date", mFuture));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_SELECT_ALL(this),
				AsyncTaskValue.PAY_ACCOUNT, this);
	}

	OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position =(Integer)v.getTag();
			onCancle(position);
		}
	};
	
	CustomDialog mCustomDialog;
	private void onCancle(final int position){
		String title = getString(R.string.payaccountcancel01);
		String content = getString(R.string.payaccountcancel02);
		String  lBtnText = getString(R.string.payaccountcancel03);
		String rBtnText = getString(R.string.payaccountcancel04);
		
		mCustomDialog = new CustomDialog(this, title, content, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomDialog.dismiss();
			}
		}, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delAccount(position);
				mCustomDialog.dismiss();
			}
		}, lBtnText, rBtnText);
		mCustomDialog.show();
	}
	
	private void delAccount(int position){
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
		mParams.add(new BasicNameValuePair("idx", mArrayList.get(position).getSrl()));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_DEL(this),
				AsyncTaskValue.DAILY_INSERT, this);
	}
}
