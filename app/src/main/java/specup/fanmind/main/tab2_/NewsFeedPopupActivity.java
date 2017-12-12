package specup.fanmind.main.tab2_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

public class NewsFeedPopupActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity2(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_newfeed);
		setView();
	}

	String mSrl, mAlarm;
	boolean isMe, isNewsFeed, isTimeLine;
	//0이면 메시지 형식변경.
	int mWhere;

	private void setView(){
		Intent intent = getIntent();
		mAlarm = intent.getStringExtra("alarm");
		isMe = intent.getBooleanExtra("isMe", false);
		isNewsFeed = intent.getBooleanExtra("newsfeed", false);
		isTimeLine  = intent.getBooleanExtra("timeline", false);
		RelativeLayout mLayout[] = new RelativeLayout[4];

		mLayout[0] =(RelativeLayout)findViewById(R.id.pop_layout02);
		mLayout[1] =(RelativeLayout)findViewById(R.id.pop_layout03);
		mLayout[2] =(RelativeLayout)findViewById(R.id.pop_layout04);
		mLayout[3] =(RelativeLayout)findViewById(R.id.pop_layout05);

		TextView mText01 = (TextView)findViewById(R.id.pop_tv01);
		TextView mText02 = (TextView)findViewById(R.id.pop_tv02);
		TextView mText03 = (TextView)findViewById(R.id.pop_tv03);
		TextView mText04 = (TextView)findViewById(R.id.pop_tv04);
		TextView mText05 = (TextView)findViewById(R.id.pop_tv05);
		
	
		
		if(isMe){
			if(isTimeLine) mText01.setText(R.string.timeline00);
			else mText01.setText(R.string.newfeed00);
			if(mAlarm.equals("0")){
				if(isTimeLine) mText02.setText(R.string.timeline02);
				else mText02.setText(R.string.newfeed02);
			}else{
				if(isTimeLine) mText02.setText(R.string.timeline03);
				else mText02.setText(R.string.newfeed01);
			}
			mText03.setText(R.string.newfeed04);
			mText05.setText(R.string.newfeed05);
			if(isTimeLine) mLayout[1].setVisibility(View.GONE);
			mLayout[2].setVisibility(View.GONE);
		}else{
			if(isTimeLine) mText01.setText(R.string.timeline04);
			else mText01.setText(R.string.newfeed06);
			
			mLayout[0].setVisibility(View.GONE);
			mLayout[1].setVisibility(View.GONE);
			mLayout[2].setVisibility(View.GONE);
			mText05.setText(R.string.newfeed03);
		}

		for(int i=0; i<mLayout.length; i++){
			mLayout[i].setOnClickListener(mNewsFeedClick);
		}
	}

	OnClickListener mThememClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.pop_layout02 :
				break;
			case R.id.pop_layout03 :
				break;
			case R.id.pop_layout04 :
				break;
			case R.id.pop_layout05 :
				break;
			}
		}
	};


	OnClickListener mNewsFeedClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.pop_layout02: //알림켜기 끄기
                    setResult(RESULT_OK, getIntent().putExtra("kind", 0));
                    finish();
                    break;
                case R.id.pop_layout03: //게시물 수정하기
                    setResult(RESULT_OK, getIntent().putExtra("kind", 1));
                    finish();
                    break;
                case R.id.pop_layout04:
                    break;
                case R.id.pop_layout05://신고하기, 삭제하기
                    setResult(RESULT_OK, getIntent().putExtra("kind", 2));
                    finish();
                    break;
            }
            ActivityManager.getInstance().deleteActivity2(NewsFeedPopupActivity.this);
        }
    };
}
