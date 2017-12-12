package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;


public class ScheduelDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_schedueldetail);
		setView();
	}
	
	
	TextView mDate, mTime, mTitle, mText;
	
	private void setView(){
		
		mDate= (TextView)findViewById(R.id.schdetail_tv01);
		mTime= (TextView)findViewById(R.id.schdetail_tv02);
		mTitle= (TextView)findViewById(R.id.schdetail_tv03);
		mText= (TextView)findViewById(R.id.schdetail_tv04);
		
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		String title =  intent.getStringExtra("title");
		String text =  intent.getStringExtra("text");
		String start =  intent.getStringExtra("start");
		String end =  intent.getStringExtra("end");
		
		String astart = Utils.getSchduel(start);
		String bstart = Utils.getSchduel(end);
		String fullTime= astart+" ~ "+bstart;
		
		mDate.setText(date);
		mTime.setText(fullTime);
		mTitle.setText(title);
		mText.setText(text);
	}
	
	public void onClick(View v){
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
