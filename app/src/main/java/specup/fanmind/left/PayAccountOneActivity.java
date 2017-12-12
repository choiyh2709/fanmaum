package specup.fanmind.left;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class PayAccountOneActivity extends Activity implements OnTask {
	
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	TextView mText[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActivityManager.getInstance().addActivity(this);
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_SELECT_ONE(this),
                AsyncTaskValue.PAY_ACCOUNT, this);
		
		setContentView(R.layout.activity_payaccountone);
		setView();
	}

	private void setView(){
		mText = new TextView[]{
				(TextView)findViewById(R.id.payaccountone_tv01),
				(TextView)findViewById(R.id.payaccountone_tv02),
				(TextView)findViewById(R.id.payaccountone_tv03),
				(TextView)findViewById(R.id.payaccountone_tv04),
				(TextView)findViewById(R.id.payaccountone_tv05),
				(TextView)findViewById(R.id.payaccountone_tv06),
				(TextView)findViewById(R.id.payaccountone_tv07)
		};
	}
	
	
	
	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		Log.e("123", result);
		if(output == AsyncTaskValue.PAY_ACCOUNT_NUM){
			if(Utils.getJsonData(result)){
				getJsonData(result);
			}
		}else if(output == AsyncTaskValue.DAILY_INSERT_NUM){
			if(Utils.getJsonData(result)){
				Utils.setToast(this, R.string.payaccountcancel05);
				ActivityManager.getInstance().deleteActivity(this);
			}
		}
	}
	
	String mPrice, mDate, mBankCode, mName, mAccountInfo, mIDX; 
	private void getJsonData(String result) {
		// TODO Auto-generated method stub
		try{
			JSONObject list = new JSONObject(result);
			String mlist = list.getString("list");
			JSONObject jsonarray = new JSONObject(mlist);
			mIDX = jsonarray.getString("idx");
			mPrice =jsonarray.getString("price");
			mDate=jsonarray.getString("regdate");
			mBankCode = jsonarray.getString("bankcode");
			mName = jsonarray.getString("uname");
			mAccountInfo = jsonarray.getString("accountno");
		}catch(Exception e){
			e.printStackTrace();
		}
		setInfo();
	}
	
	private void setInfo(){
		String title =null;
		if(mPrice.equals("5500")){
			title = getString(R.string.buyitem03);
		}else if(mPrice.equals("11000")){
			title = getString(R.string.buyitem04);
		}else if(mPrice.equals("22000")){
			title = getString(R.string.buypoint10);
		}else if(mPrice.equals("33000")){
			title = getString(R.string.buypoint11);
		}else if(mPrice.equals("55000")){
			title = getString(R.string.buyitem06);
		}else if(mPrice.equals("1100")){
			title = getString(R.string.buyitem01);
		}else if(mPrice.equals("2500")){
			title = getString(R.string.buyitem02);
		}else if(mPrice.equals("27500")){
			title = getString(R.string.buyitem05);
		}
		
		
		String product = getString(R.string.mypoint_tv05).replace("{NUMBER}", title);
		String price = getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(mPrice));
		
//		mText[0].setText(title);
		mText[4].setText(product);
		mText[5].setText(price);
		
		int year = Integer.parseInt(mDate.substring(0, 4));
		int month = Integer.parseInt(mDate.substring(5, 7));
		int day = Integer.parseInt(mDate.substring(8, 10));
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
		Calendar today = Calendar.getInstance ();
		today.set(year, month-1, day);
		today.add(Calendar.DATE, 3);
		Date t = today.getTime();
		String future = sdf.format(t);
		
		int reyear = Integer.parseInt(future.substring(0, 4));
		int remonth = Integer.parseInt(future.substring(4, 6));
		int reday = Integer.parseInt(future.substring(6, 8));
		
		String date = getString(R.string.mypoint_tv07).replace("{YEAR}", String.valueOf(reyear))
				.replace("{MONTH}", String.valueOf(remonth)).replace("{DAY}", String.valueOf(reday));
		mText[6].setText(date);
		
		if(mBankCode.equals("03")){
			mText[1].setText(getString(R.string.bank01));
		}else if(mBankCode.equals("04")){
			mText[1].setText(getString(R.string.bank02));
		}else if(mBankCode.equals("20")){
			mText[1].setText(getString(R.string.bank03));
		}else if(mBankCode.equals("26")){
			mText[1].setText(getString(R.string.bank04));
		}else if(mBankCode.equals("81")){
			mText[1].setText(getString(R.string.bank05));
		}else if(mBankCode.equals("11")){
			mText[1].setText(getString(R.string.bank06));
		}else if(mBankCode.equals("71")){
			mText[1].setText(getString(R.string.bank07));
		}
		
		mText[2].setText(mAccountInfo);
		mText[3].setText(getString(R.string.specupad));
		
	}
	
	public void onBack(View v){
		onBack();
	}
	
	private void onBack(){
		ActivityManager.getInstance().deleteActivity(this);
	}
	
	public void onDel(View v){
		onCancle();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
		}

		return super.onKeyDown(keyCode, event);
	}

	CustomDialog mCustomDialog;
	private void onCancle(){
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
				delAccount();
				mCustomDialog.dismiss();
			}
		}, lBtnText, rBtnText);
		mCustomDialog.show();
	}
	
	private void delAccount(){
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
		mParams.add(new BasicNameValuePair("idx", mIDX));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_DEL(this),
				AsyncTaskValue.DAILY_INSERT, this);
	}
	
}
