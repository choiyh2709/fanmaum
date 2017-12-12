package specup.fanmind.left;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
import specup.fanmind.fanmindsetting.StarSetting;


public class ScheduelAddActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_schedueladd);
		setView();
	}

	Button mConfirmBtn;
	TextView mDateTitle;
	TextView fHour, fMin, sHour, sMin;
	boolean fboolean, sboolean;
	Button fBtn, sBtn;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	EditText mEtTitle, mEtText;
	String mDate, sToday;



	private void setView(){

		mEtTitle =(EditText)findViewById(R.id.schadd_et01);
		mEtText =(EditText)findViewById(R.id.schadd_et02);


		fBtn =(Button)findViewById(R.id.schadd_btn05);
		sBtn=(Button)findViewById(R.id.schadd_btn10);

		mConfirmBtn =(Button)findViewById(R.id.schadd_btn11);
		mDateTitle =(TextView)findViewById(R.id.schadd_tv01);
		fHour =(TextView)findViewById(R.id.schadd_tv04);
		fMin =(TextView)findViewById(R.id.schadd_tv05);
		sHour =(TextView)findViewById(R.id.schadd_tv07);
		sMin =(TextView)findViewById(R.id.schadd_tv08);

		mConfirmBtn.requestFocus();

		Intent intent = getIntent();
		mDate = intent.getStringExtra("date");
		
		String syear = mDate.substring(0, 4);
		String smonth = mDate.substring(5, 7);
		String sday = mDate.substring(8, 10);
		
		
		if(smonth.startsWith("0")) smonth = smonth.substring(1);
		if(sday.startsWith("0")) sday = sday.substring(1);
		String reDate = syear+"."+smonth+"."+sday;
		mDateTitle.setText(reDate);
		
		sToday = Utils.getCalTime();
		String hour =  sToday.substring(8, 10);
		String min = sToday.substring(10, 12);

		int mHour = Integer.parseInt(hour);
		int mMin = Integer.parseInt(min);
		fMin.setText(Utils.changeCal(String.valueOf(mMin)));
		sMin.setText(Utils.changeCal(String.valueOf(mMin)));		

		setTime(mHour);
	}

	private void setTime(int hour){
		int mhour = hour;
		if(hour>11){
			fboolean = true;
			if(hour !=12){
				if(hour !=0){
					hour = hour -12;
				}else{
					hour= 12;
				}
			}
		}

		fHour.setText(Utils.changeCal(String.valueOf(hour)));
		if(fboolean) fBtn.setBackgroundResource(R.drawable.h1200_pm);
		else fBtn.setBackgroundResource(R.drawable.h1200_am);

		if(mhour+1!=24){
			if(mhour+1>11){
				sboolean = true;
			}
			if(mhour+1>12){
				mhour = mhour-11;
			}else{
				mhour = mhour+1;
			}
		}else{
			mhour =12; 
		}

		sHour.setText(Utils.changeCal(String.valueOf(mhour)));

		if(sboolean) sBtn.setBackgroundResource(R.drawable.h1200_pm);
		else sBtn.setBackgroundResource(R.drawable.h1200_am);

	}

	private void setfNoon(){
		fboolean = !fboolean;
		if(fboolean) fBtn.setBackgroundResource(R.drawable.h1200_pm);
		else fBtn.setBackgroundResource(R.drawable.h1200_am);
	}

	private void setsNoon(){
		sboolean = !sboolean;
		if(sboolean) sBtn.setBackgroundResource(R.drawable.h1200_pm);
		else sBtn.setBackgroundResource(R.drawable.h1200_am);
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.schadd_btn01 :
			setHourUp(true);
			break;
		case R.id.schadd_btn02 :
			setHourDown(true);
			break;
		case R.id.schadd_btn03 :
			setMinUp(true);
			break;
		case R.id.schadd_btn04 :
			setMinDown(true);
			break;
		case R.id.schadd_btn05 :
			setfNoon();
			break;
		case R.id.schadd_btn06 :
			setHourUp(false);
			break;
		case R.id.schadd_btn07 :
			setHourDown(false);
			break;
		case R.id.schadd_btn08 :
			setMinUp(false);
			break;
		case R.id.schadd_btn09 :
			setMinDown(false);
			break;
		case R.id.schadd_btn10	:
			setsNoon();
			break;
		case R.id.schadd_btn11 :
			if(Utils.checkLength(mEtTitle)){
				Utils.setToast(ScheduelAddActivity.this, R.string.company11);
			}else if(Utils.checkLength(mEtText)){
				Utils.setToast(ScheduelAddActivity.this, R.string.company15);
			}else{
				sendSch();
			}
			break;
		}
	}

	@Override
	public void onTask(int output, String result) {
		if(output== AsyncTaskValue.SCHDUEL_INFORM_NUM){
			if (Utils.getJsonData(result)) {
				Utils.setToast(ScheduelAddActivity.this, R.string.schadd01);
				finish();
			}
		}
	}

    /**
   	 * 제보하기
   	 */
	private void sendSch(){
		int ftime = Integer.parseInt(fHour.getText().toString());
		int stime = Integer.parseInt(sHour.getText().toString());
		if(fboolean) ftime = ftime + 12;
		if(sboolean) stime = stime +12;

		if(ftime==24) ftime =0;
		if(stime==24) stime=0;
		
		mDate = Utils.dotMinus(mDate	);
		String year = mDate.substring(0, 4);
		String month = mDate.substring(4, 6);
		String day = mDate.substring(6, 8);
		String ahour = Utils.changeCal(String.valueOf(ftime));
		String amin = fMin.getText().toString();
		String bhour = Utils.changeCal(String.valueOf(stime));
		String bmin = sMin.getText().toString();
		String sec= sToday.substring(12, 14);

		String afull = year+"-"+month+"-"+day+" "+ahour+":"+amin+":"+sec;
		String bfull = year+"-"+month+"-"+day+" "+bhour+":"+bmin+":"+sec;

		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("title", mEtTitle.getText().toString().trim()));
		mParams.add(new BasicNameValuePair("begin_time", afull));
		mParams.add(new BasicNameValuePair("end_time", bfull));
		mParams.add(new BasicNameValuePair("text", mEtText.getText().toString().trim()));
		mParams.add(new BasicNameValuePair("star_srl", StarSetting.getSTAR_SELECT_INDEX(this)));
		mParams = Utils.setSession(this, mParams);
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.SCHDULE_INFORM(this),
                AsyncTaskValue.SCHDUEL_INFORM, this);
	}


    /** 시간 올리기
   	 * @param isFirst 시작 true 끝 false
   	 */
	private void setHourUp(boolean isFirst){
		String hour = "";
		if(isFirst){
			hour = fHour.getText().toString().trim();
		}else{
			hour = sHour.getText().toString().trim();
		}
		int time = Integer.parseInt(hour);
		if (time ==12) {
			time = 1;
		}else{
			time = time+1;
		}
		if(isFirst){
			fHour.setText(Utils.changeCal(String.valueOf(time)));
		}else{
			sHour.setText(Utils.changeCal(String.valueOf(time)));
		}
	}


    /**시간 내리기
   	 * @param isFirst 시작 true 끝 false
   	 */
	private void setHourDown(boolean isFirst){
		String hour = "";
		if(isFirst){
			hour = fHour.getText().toString().trim();
		}else{
			hour = sHour.getText().toString().trim();
		}
		int time = Integer.parseInt(hour);
		if (time ==1) {
			time = 12;
		}else{
			time = time-1;
		}
		if(isFirst){
			fHour.setText(Utils.changeCal(String.valueOf(time)));
		}else{
			sHour.setText(Utils.changeCal(String.valueOf(time)));
		}
	}


    /**분 올리기
   	 * @param isFirst 시작 true 끝 false
   	 */
	private void setMinUp(boolean isFirst){
		String min = "";
		if(isFirst){
			min = fMin.getText().toString().trim();
		}else{
			min = sMin.getText().toString().trim();
		}
		int time = Integer.parseInt(min);
		if (time ==59) {
			time = 0;
		}else{
			time = time+1;
		}
		if(isFirst){
			fMin.setText(Utils.changeCal(String.valueOf(time)));
		}else{
			sMin.setText(Utils.changeCal(String.valueOf(time)));
		}
	}


    /** 분내리기
   	 * @param isFirst 시작 true 끝 false
   	 */
	private void setMinDown(boolean isFirst){
		String hour = "";
		if(isFirst){
			hour = fMin.getText().toString().trim();
		}else{
			hour = sMin.getText().toString().trim();
		}
		int time = Integer.parseInt(hour);
		if (time ==0) {
			time = 59;
		}else{
			time = time-1;
		}
		if(isFirst){
			fMin.setText(Utils.changeCal(String.valueOf(time)));
		}else{
			sMin.setText(Utils.changeCal(String.valueOf(time)));
		}
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
