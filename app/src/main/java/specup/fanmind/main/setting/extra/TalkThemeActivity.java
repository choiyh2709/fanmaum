package specup.fanmind.main.setting.extra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;

public class TalkThemeActivity extends Activity{

	private RelativeLayout mLayout[] = new RelativeLayout[3];
	EditText mEt[] = new EditText[3];
	TextView mText[] = new TextView[3];
	boolean isFirst;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_theme);
		setView();
	}

	private void setView(){
		mLayout[0] = (RelativeLayout)findViewById(R.id.theme_layout02);
		mLayout[1] = (RelativeLayout)findViewById(R.id.theme_hidelayout01);
		mLayout[2] = (RelativeLayout)findViewById(R.id.theme_hide2layout01);
		mEt[0] =(EditText)findViewById(R.id.theme_et01);
		mEt[1] =(EditText)findViewById(R.id.theme_hideet01);
		mEt[2] =(EditText)findViewById(R.id.theme_hide2et01);
		mText[0] = 	 (TextView)findViewById(R.id.theme_tv02);
		mText[1] = 	 (TextView)findViewById(R.id.theme_tv03);
		mText[2] = 	 (TextView)findViewById(R.id.theme_hide2tv01);

		Button mBtnCancel =(Button)findViewById(R.id.theme_btn01);
		Button mBtnOK =(Button)findViewById(R.id.theme_btn02);

		mEt[2].addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()!=0){
					mText[2].setText(s.toString());
				}
			}
		});

		Intent intent =getIntent();
		isFirst = intent.getBooleanExtra("first", false);
		if(isFirst){ //처음에 듣고싶은 호칭 고를때
			mBtnCancel.setVisibility(View.GONE);
			mBtnOK.setBackgroundResource(R.drawable.pop_btn03);
			mText[0].setText(R.string.theme03);
			mLayout[0].setVisibility(View.GONE);
			mLayout[1].setVisibility(View.GONE);
			mLayout[2].setVisibility(View.VISIBLE);
		}else{

		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public void onClick(View v){
		if(v.getId() == R.id.theme_btn01){
			finish();
		}else{
			if(isFirst){
				if(Utils.checkLength(mEt[2])){
					Utils.setToast(this, R.string.notok08);
				}else{
//					FanMindSetting.setTALK_NAME(this, mEt[2].getText().toString());
					setResult(RESULT_OK);
					finish();
				}
			}
		}
	}

}
