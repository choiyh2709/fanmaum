package specup.fanmind.main.setting.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.fanmindsetting.FanMindSetting;

public class EtcActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_etc);
		setView();
	}


	Button mBtn[] = new Button[3];
	boolean isAlert[] = new boolean[3];

	private void setView(){
		
		mBtn[0] = (Button)findViewById(R.id.etc_btn01);
		mBtn[1] = (Button)findViewById(R.id.etc_btn02);
		mBtn[2] = (Button)findViewById(R.id.etc_btn03);

		isAlert[0] = FanMindSetting.getALERT_MYNEWS(this);
		isAlert[1] = FanMindSetting.getALERT_FANMIND(this);
		isAlert[2] = FanMindSetting.getALERT_SAVE(this);
		for(int i=0; i<mBtn.length; i++){
			setEtc(mBtn[i], isAlert[i]);
			mBtn[i].setOnClickListener(mClick);
		}

	}

	OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.etc_btn01 :
				FanMindSetting.setALERT_MYNEWS(EtcActivity.this, !isAlert[0]);
				setEtc(mBtn[0], FanMindSetting.getALERT_MYNEWS(EtcActivity.this));
				isAlert[0] = !isAlert[0];
				break;
			case R.id.etc_btn02 :
				FanMindSetting.setALERT_FANMIND(EtcActivity.this, !isAlert[1]);
				setEtc(mBtn[1], FanMindSetting.getALERT_FANMIND(EtcActivity.this));
				isAlert[1] = !isAlert[1];
				break;
			case R.id.etc_btn03 :
				FanMindSetting.setALERT_SAVE(EtcActivity.this, !isAlert[2]);
				setEtc(mBtn[2], FanMindSetting.getALERT_SAVE(EtcActivity.this));
				isAlert[2] = !isAlert[2];
				break;
			}
		}
	};


    /**잠금화면 설정에서 세팅
   	 * @param btn 버튼
   	 * @param isSet false, true 이미지
   	 */
	private void setEtc(Button btn, boolean isSet){
		if(isSet) btn.setBackgroundResource(R.drawable.swich_on);
		else btn.setBackgroundResource(R.drawable.swich_off);
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
