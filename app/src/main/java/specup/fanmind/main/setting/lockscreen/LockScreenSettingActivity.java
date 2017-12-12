package specup.fanmind.main.setting.lockscreen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.pluslock.IgawPlusLock;
import com.igaworks.adpopcorn.pluslock.model.ResultModel;
import com.igaworks.adpopcorn.pluslock.net.IPlusLockResultCallback;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;

/**
 * 잠금화면 설정
 */
public class LockScreenSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        IgawCommon.startApplication(LockScreenSettingActivity.this);
        setContentView(R.layout.fragment_locksreen);
        setView();
    }
    @Override
      protected void onResume() {
          super.onResume();
          IgawCommon.startSession(LockScreenSettingActivity.this);
      }

      @Override
      protected void onPause() {
          super.onPause();
          IgawCommon.endSession();
      }


    RelativeLayout mLockScreenLayout[] = new RelativeLayout[2];
    Button mBtn[] = new Button[3];
    boolean isResult[] = new boolean[3];
    boolean isActivePlusLockScreen;

    private void setView() {
        mLockScreenLayout[0] = (RelativeLayout) findViewById(R.id.lockscreen_layout02);
//		mLockScreenLayout[1] = (RelativeLayout)findViewById(R.id.lockscreen_layout04);
        mBtn[0] = (Button) findViewById(R.id.lockscreen_btn01);
        IgawCommon.setUserId(FanMindSetting.getSESSION_KEY(LockScreenSettingActivity.this));
        isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(LockScreenSettingActivity.this);
        setLockScreen(mBtn[0], isActivePlusLockScreen);

//		mBtn[1] = (Button)findViewById(R.id.lockscreen_btn02);
//		mBtn[2] = (Button)findViewById(R.id.lockscreen_btn03);

        isResult[0] = FanMindSetting.getLOCKSCREEN(this);
//		isResult[1] = FanMindSetting.getRANDOM_IMAGE(this);
//		isResult[2] = FanMindSetting.getTALK_MSG(this);

        setLockScreen(mBtn[0], isResult[0]);
//		setLockScreen(mBtn[1], isResult[1]);
//		setLockScreen(mBtn[2], isResult[2]);

        mLockScreenLayout[0].setOnClickListener(mClick);
//		mLockScreenLayout[1].setOnClickListener(mClick);
        mBtn[0].setOnClickListener(mClick);
//		mBtn[1].setOnClickListener(mClick);
//		mBtn[2].setOnClickListener(mClick);
    }

    /***********************************************
     * pluslock
     ************************************************/
    IPlusLockResultCallback iplusLockResultCallback = new IPlusLockResultCallback() {
        @Override
        public void onResult(ResultModel rm) {

            if (rm != null && rm.isResult()) { //서버에 요청한 결과가 true인 경우에 결과 처리
                if (isActivePlusLockScreen) {// On 시도가 성공한 경우, 서비스 시작.
                    IgawPlusLock.startLockScreenService(LockScreenSettingActivity.this);
                    IgawPlusLock.setFlagDismissKeyguard(LockScreenSettingActivity.this, false);
                } else { // OFF 시도가 성공한 경우, 서비스 종료.
                    IgawPlusLock.stopLockScreenService(LockScreenSettingActivity.this);
                    IgawPlusLock.setFlagDismissKeyguard(LockScreenSettingActivity.this, true);
                }
            } else {
                if (isActivePlusLockScreen) {
                    IgawPlusLock.activateLockScreen(LockScreenSettingActivity.this, true, iplusLockResultCallback);
                } else {
                    IgawPlusLock.activateLockScreen(LockScreenSettingActivity.this, false, iplusLockResultCallback);
                }
            }
        }
    };

    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.lockscreen_btn01:
                    if (FanMindSetting.getLOGIN_OK(LockScreenSettingActivity.this)) {
                        isActivePlusLockScreen = !isActivePlusLockScreen;
                        setLockScreen(mBtn[0], isActivePlusLockScreen);
                        FanMindSetting.setLOCKSCREEN(LockScreenSettingActivity.this, isActivePlusLockScreen);
                        if (isActivePlusLockScreen) {
                            IgawPlusLock.activateLockScreen(LockScreenSettingActivity.this, true, iplusLockResultCallback);
                        } else {
                            IgawPlusLock.activateLockScreen(LockScreenSettingActivity.this, false, iplusLockResultCallback);
                        }
                    } else {
                        Utils.showDialog(LockScreenSettingActivity.this);
                    }

//				팬마음 락스크린
//				FanMindSetting.setLOCKSCREEN(LockScreenSettingActivity.this, !isResult[0]);
//							setLockScreen(mBtn[0], FanMindSetting.getLOCKSCREEN(LockScreenSettingActivity.this));
//							isResult[0] = !isResult[0];
//
//
//				if(FanMindSetting.getLOCKSCREEN(LockScreenSettingActivity.this))
//					startService(new Intent(LockScreenSettingActivity.this, LockService.class));
//				else
//					stopService(new Intent(LockScreenSettingActivity.this, LockService.class));
//				break;

//			case R.id.lockscreen_layout02 :
////				fragmentReplace(0);
//				startActivity(new Intent(LockScreenSettingActivity.this, LockScreenImageActivity.class));
//				break;
//			case R.id.lockscreen_btn02 :
//				FanMindSetting.setRANDOM_IMAGE(LockScreenSettingActivity.this, !isResult[1]);
//				setLockScreen(mBtn[1], FanMindSetting.getRANDOM_IMAGE(LockScreenSettingActivity.this));
//				isResult[1] = !isResult[1];
//				break;
//			case R.id.lockscreen_layout04 :
////				fragmentReplace(1);
//				startActivity(new Intent(LockScreenSettingActivity.this, LockScreenButtonImageActivity.class));
//
//				break;
//			case R.id.lockscreen_btn03 :
//				FanMindSetting.setTALK_MSG(LockScreenSettingActivity.this, !isResult[2]);
//				setLockScreen(mBtn[2], FanMindSetting.getTALK_MSG(LockScreenSettingActivity.this));
//				isResult[2] = !isResult[2];
//				break;
            }
        }
    };


	/*public void fragmentReplace(int reqNewFragmentIndex) {
        Fragment newFragment = null;
		newFragment = getFragment(reqNewFragmentIndex);
		final FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.frame, newFragment, "lockscreen"+String.valueOf(reqNewFragmentIndex));
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}*/

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;
        switch (idx) {
            case 0:
                //잠금화면 설정
//			newFragment = new LockScreenImageActivity();
                break;
            case 1:
                //잠금화면 버튼 설정
//			newFragment = new LockScreenButtonImageFragment();
                break;
            default:
                break;
        }
        return newFragment;
    }


    /**
     * 잠금화면 설정에서 세팅
     *
     * @param btn   버튼
     * @param isSet false, true 이미지
     */
    private void setLockScreen(Button btn, boolean isSet) {
        if (isSet) {
            btn.setBackgroundResource(R.drawable.swich_on);
        } else {
            btn.setBackgroundResource(R.drawable.swich_off);
        }
    }

    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().deleteActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
