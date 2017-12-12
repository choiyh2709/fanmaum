package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.pluslock.IgawPlusLock;
import com.igaworks.adpopcorn.pluslock.model.ResultModel;
import com.igaworks.adpopcorn.pluslock.net.IPlusLockResultCallback;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.DailyActivity;
import specup.fanmind.MainActivity;
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
import specup.fanmind.left.profile.ProfileActivity;
import specup.fanmind.main.login.LoginActivity2;
import specup.fanmind.main.setting.extra.CompanyCoverActivity;
import specup.fanmind.main.setting.extra.EtcActivity;
import specup.fanmind.main.setting.extra.FanClubCoverActivity;
import specup.fanmind.main.setting.extra.LawActivity;
import specup.fanmind.main.setting.lockscreen.LockScreenSettingActivity;


public class SettingActivity extends Activity implements OnTask {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        IgawCommon.startApplication(SettingActivity.this);
        setContentView(R.layout.fragment_setting);
        setView();
    }


    RelativeLayout mSettingLayout[] = new RelativeLayout[9];
    RelativeLayout layout_logout, layout_sign_out, layout_check_attend;
    TextView mTvVersion;
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;


    @Override
    protected void onResume() {
        super.onResume();
        IgawCommon.startSession(SettingActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IgawCommon.endSession();
    }

    private void setView() {
        mSettingLayout[0] = (RelativeLayout) findViewById(R.id.setting_layout01);
        mSettingLayout[1] = (RelativeLayout) findViewById(R.id.setting_layout02);//잠금화면 설정
        mSettingLayout[2] = (RelativeLayout) findViewById(R.id.setting_layout03);
        mSettingLayout[3] = (RelativeLayout) findViewById(R.id.setting_layout04);
        mSettingLayout[4] = (RelativeLayout) findViewById(R.id.setting_layout05);
        mSettingLayout[5] = (RelativeLayout) findViewById(R.id.setting_layout06);
        mSettingLayout[6] = (RelativeLayout) findViewById(R.id.setting_layout07);
        mSettingLayout[7] = (RelativeLayout) findViewById(R.id.setting_layout09);
        mSettingLayout[8] = (RelativeLayout) findViewById(R.id.setting_layout10);
        layout_logout = (RelativeLayout) findViewById(R.id.layout_logout);
        layout_sign_out = (RelativeLayout) findViewById(R.id.layout_sign_out);
        layout_check_attend = (RelativeLayout) findViewById(R.id.layout_check_attend);
        layout_logout.setOnClickListener(mClick);
        layout_sign_out.setOnClickListener(mClick);
        layout_check_attend.setOnClickListener(mClick);

        mTvVersion = (TextView) findViewById(R.id.setting_tv07);

        for (int i = 0; i < mSettingLayout.length; i++) {
            mSettingLayout[i].setOnClickListener(mClick);
        }

        try {
            PackageInfo i = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String mVersion = i.versionName;
            mTvVersion.setText("Ver." + mVersion);
        } catch (NameNotFoundException e) {
        }

        //		new Handler().postDelayed(new Runnable() {
        //			@Override
        //			public void run() {
        //				// TODO Auto-generated method stub
        //				((BaseActivity)BaseActivity.mBaseActivity).toggle();
        //			}
        //		}, 500);

    }

    OnClickListener mClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.setting_layout01:
                    if (FanMindSetting.getLOGIN_OK(SettingActivity.this)) {
                        startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                    } else {
                        Utils.showDialog(SettingActivity.this);
                    }
                    break;
                case R.id.setting_layout02: //잠금화면 설정
                    startActivity(new Intent(SettingActivity.this, LockScreenSettingActivity.class));
                    break;
//			case R.id.setting_layout03 :
//				break;
                case R.id.setting_layout04: //기타설정
                    startActivity(new Intent(SettingActivity.this, EtcActivity.class));
                    break;
                case R.id.setting_layout05: //팬마음 소개 및 제휴문의
                    startActivity(new Intent(SettingActivity.this, CompanyCoverActivity.class));
                    break;
                case R.id.setting_layout06://팬클럽 참여문의
                    startActivity(new Intent(SettingActivity.this, FanClubCoverActivity.class));
                    break;
                case R.id.setting_layout07://운영 정책
                    startActivity(new Intent(SettingActivity.this, LawActivity.class));
                    break;
                case R.id.layout_check_attend://출석체크하기
                    if (FanMindSetting.getLOGIN_OK(SettingActivity.this)) {
                        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                        mParams = Utils.setSession(SettingActivity.this, mParams);
                        HttpRequest.setHttp1(SettingActivity.this, URLAddress.MEMBER_ATTEND(), mParams,
                                new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        Intent intent = new Intent(SettingActivity.this, DailyActivity.class);
                                        intent.putExtra("result", result);
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        Utils.showDialog(SettingActivity.this);
                    }
                    break;
                case R.id.setting_layout09:
                    if (FanMindSetting.getLOGIN_OK(SettingActivity.this)) {
                        mParams = new ArrayList<NameValuePair>();
                        HttpRequest.setHttp(SettingActivity.this, mAsyncTask, mParams,
                                URLAddress.DAILY_TIME(SettingActivity.this),
                                AsyncTaskValue.DAILY_SELECT, SettingActivity.this);
                    } else {
                        Utils.showDialog(SettingActivity.this);
                    }
                    break;
                case R.id.setting_layout10:
                    startActivity(new Intent(SettingActivity.this, NoticeActivity.class));
                    break;
                case R.id.layout_logout:
                    if (FanMindSetting.getLOGIN_OK(SettingActivity.this)) {
                        String title = getString(R.string.profile06);
                        String content = getString(R.string.logout);
                        String left = getString(R.string.cancel);
                        String right = getString(R.string.confirmation);
                        mDialog = new CustomDialog(SettingActivity.this, title, content, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkPermission();
                                mDialog.dismiss();
                                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                FanMindSetting.setLogout(SettingActivity.this);
                            }
                        }, left, right);
                        mDialog.show();
                    } else {
                        Utils.showDialog(SettingActivity.this);
                    }
                    break;
                case R.id.layout_sign_out: {
                    String title = getString(R.string.alert);
                    String content = getString(R.string.signout_message);
                    String left = getString(R.string.cancel);
                    String right = getString(R.string.confirmation);
                    mDialog = new CustomDialog(SettingActivity.this, title, content, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            mParams = Utils.setSession(SettingActivity.this, mParams);
                            HttpRequest.setHttp1(SettingActivity.this, URLAddress.MEMBERS_SIGNOUT(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                                        if (FanMindSetting.getLOGIN_OK(SettingActivity.this)) {

                                            mDialog.dismiss();
                                            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            FanMindSetting.setLogout(SettingActivity.this);

                                            Utils.setToast(SettingActivity.this, Utils.getJsonData(result, "message"));
                                        } else {
                                            Utils.showDialog(SettingActivity.this);

                                        }
                                    } else {
                                        Utils.setToast(SettingActivity.this, Utils.getJsonData(result, "message"));
                                    }
                                }
                            });
                        }
                    }, left, right);
                    mDialog.show();
                }
                break;
            }
        }
    };
    CustomDialog mDialog;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack(View v) {
        onBack();
    }

    public void onBack() {
        ActivityManager.getInstance().deleteActivity(this);
//		if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//			((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.DAILY_SELECT_NUM) {
            if (Utils.getJsonData(result)) {
                checkTime(result);
            }
        }
    }

    private void checkTime(String key) {
        String time = null;
        try {
            JSONObject list = new JSONObject(key);
            time = list.getString("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
//		Intent intent = new Intent(this, DailyActivity.class);
//		intent.putExtra("key", time);
//		intent.putExtra("start", false);
//		startActivity(intent);
    }

    /***********************************************
     * pluslock
     ************************************************/
    private static final int REQUEST_READ_CONTACTS = 1;
    boolean isActivePlusLockScreen;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(SettingActivity.this);
            if (isActivePlusLockScreen) {
                IgawPlusLock.activateLockScreen(SettingActivity.this, true, iplusLockResultCallback);
            } else {
                IgawPlusLock.activateLockScreen(SettingActivity.this, false, iplusLockResultCallback);
            }
        } else {
            // 실행 할 코드
            Utils.setToast(SettingActivity.this, getString(R.string.request_permission));
        }
    }


    IPlusLockResultCallback iplusLockResultCallback = new IPlusLockResultCallback() {
        @Override
        public void onResult(ResultModel rm) {

            if (rm != null && rm.isResult()) { //서버에 요청한 결과가 true인 경우에 결과 처리
                if (isActivePlusLockScreen) {// On 시도가 성공한 경우, 서비스 시작.
                    IgawPlusLock.startLockScreenService(SettingActivity.this);
                    IgawPlusLock.setFlagDismissKeyguard(SettingActivity.this, false);
                } else { // OFF 시도가 성공한 경우, 서비스 종료.
                    IgawPlusLock.stopLockScreenService(SettingActivity.this);
                    IgawPlusLock.setFlagDismissKeyguard(SettingActivity.this, true);
                }
            } else {
                if (isActivePlusLockScreen) {
                    IgawPlusLock.activateLockScreen(SettingActivity.this, true, iplusLockResultCallback);
                } else {
                    IgawPlusLock.activateLockScreen(SettingActivity.this, false, iplusLockResultCallback);
                }
            }
        }
    };


    public void checkPermission() {
        FanMindSetting.setLOCKSCREEN(SettingActivity.this, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(SettingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);

        } else {

            isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(SettingActivity.this);

            if (isActivePlusLockScreen) {
                IgawPlusLock.activateLockScreen(SettingActivity.this, true, iplusLockResultCallback);
            } else {
                IgawPlusLock.activateLockScreen(SettingActivity.this, false, iplusLockResultCallback);
            }
        }
    }

}
