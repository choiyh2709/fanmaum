package specup.fanmind;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.igaworks.adpopcorn.pluslock.IgawPlusLock;
import com.igaworks.adpopcorn.pluslock.model.ResultModel;
import com.igaworks.adpopcorn.pluslock.net.IPlusLockResultCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.AlertDialogStaradd;
import specup.fanmind.left.CashOutFragment;
import specup.fanmind.left.EventActivity;
import specup.fanmind.left.MovieActivity;
import specup.fanmind.left.MyCuponFragment;
import specup.fanmind.left.MyPointActivity;
import specup.fanmind.left.NoticeActivity;
import specup.fanmind.left.NotifyFanMindActivity;
import specup.fanmind.left.RankingActivity;
import specup.fanmind.left.SaveMindActivity;
import specup.fanmind.left.SaveMindFragment;
import specup.fanmind.left.ScheduelFragment;
import specup.fanmind.left.SettingActivity;
import specup.fanmind.left.profile.ProfileActivity;
import specup.fanmind.main.setting.lockscreen.LockScreenButtonImageActivity;
import specup.fanmind.main.tab4.UserPageActivity2;
import specup.fanmind.main.tab4.UserPageMyAttendedActivity;

public class LeftMenuFragment2 extends Fragment implements OnTask {

    Context context;

    public static int mLeftBtnNumber = -1;
    public static LeftMenuFragment2 mLeftContext;
    RelativeLayout mLayout[] = new RelativeLayout[6];
    RelativeLayout mFreeMaumCharge, mMyPoint,mInfo;
    LinearLayout mNewArea;
    Button button_projectStart;
    Button mBtn[] = new Button[1];
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    public TextView mNickTv, mMindTv,mProfile0,mProfile1,mProfile2,mProfile3;
    ImageView mProfile,mArrow;
    int mCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        return inflater.inflate(R.layout.fragment_left2, null);
    }

    @Override
    public void onResume() {
        setView();
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {

        }
//		if(!BaseActivity.isRestart){
//			setView();
//		}

    }

//	Context mContext;
//	public LeftMenuFragment(Context context){
//		Log.e("LeftMenuFragment", "LeftMenuFragment");
//		mContext = context;
//	}

//	public LeftMenuFragment(){
//		super();
//		Log.e("LeftMenuFrag", "LeftMenuFrag");
//		BaseActivity.isRestart = true;
//	}

    private void setView() {
        mLeftContext = LeftMenuFragment2.this;
        mProfile = (ImageView) getActivity().findViewById(R.id.left_profile);
        mFreeMaumCharge = (RelativeLayout) getActivity().findViewById(R.id.freeMaumCharge);
        mNewArea = (LinearLayout) getActivity().findViewById(R.id.newArea);
        mInfo = (RelativeLayout) getActivity().findViewById(R.id.nav_top);
        mMyPoint = (RelativeLayout) getActivity().findViewById(R.id.myPoint);
        mNickTv = (TextView) getActivity().findViewById(R.id.left_tv01);
        mNickTv.setOnClickListener(mClick);
        mArrow = (ImageView) getActivity().findViewById(R.id.icon_arrow);

        mMindTv = (TextView) getActivity().findViewById(R.id.left_tv02);
        mLayout[0] = (RelativeLayout) getActivity().findViewById(R.id.left_layout01);
        mLayout[1] = (RelativeLayout) getActivity().findViewById(R.id.left_layout02);
        mLayout[2] = (RelativeLayout) getActivity().findViewById(R.id.left_layout03);
        mLayout[3] = (RelativeLayout) getActivity().findViewById(R.id.left_layout04);
        mLayout[4] = (RelativeLayout) getActivity().findViewById(R.id.left_layout05);
        mLayout[5] = (RelativeLayout) getActivity().findViewById(R.id.left_layout06);
        button_projectStart = (Button) getActivity().findViewById(R.id.button_projectStart);
        mProfile0 = (TextView) getActivity().findViewById(R.id.my_profile0);
        mProfile1 = (TextView) getActivity().findViewById(R.id.my_profile1);
        mProfile2 = (TextView) getActivity().findViewById(R.id.my_profile2);
        mProfile3 = (TextView) getActivity().findViewById(R.id.my_profile3);

        mBtn[0] = (Button) getActivity().findViewById(R.id.left_btn01);

        for (int i = 0; i < mLayout.length; i++) {
            mLayout[i].setOnClickListener(mClick);
        }
        mBtn[0].setOnClickListener(mClick);
        button_projectStart.setOnClickListener(mClick);
        mProfile.setOnClickListener(mClick);
        mFreeMaumCharge.setOnClickListener(mClick);
        mMyPoint.setOnClickListener(mClick);
        mInfo.setOnClickListener(mClick);
        mProfile0.setOnClickListener(mClick);
        mProfile1.setOnClickListener(mClick);
        mProfile2.setOnClickListener(mClick);
        mProfile3.setOnClickListener(mClick);


        if (FanMindSetting.getLOGIN_OK(getActivity())) {
            mNickTv.setText(FanMindSetting.getNICK_NAME(getActivity()));
            mMindTv.setText(Utils.getMoney(FanMindSetting.getMY_HEART(getActivity())));
        } else {
            mNickTv.setText(R.string.guest01);
            mMindTv.setText(R.string.guest02);
        }
        setProfile();
    }


    CustomDialog mDialog;

    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {

                case R.id.freeMaumCharge:
                    checkSskey();
                    break;


                case R.id.left_profile:
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        getActivity().startActivity(new Intent(getActivity(), LockScreenButtonImageActivity.class));
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.left_layout01: //랭킹
                    getActivity().startActivity(new Intent(getActivity(), RankingActivity.class));
                    break;
                case R.id.left_layout02:// 이벤트

                    getActivity().startActivity(new Intent(getActivity(), EventActivity.class));

                    break;
                case R.id.left_layout03:  //연예인 추가
                    AlertDialogStaradd dialog = new AlertDialogStaradd(getActivity());
                    dialog.show(getFragmentManager(), "dialog");

                    break;
                case R.id.left_layout04: //팬마음 공유하기

                    if (FanMindSetting.getLOGIN_OK(getActivity())) {

                        Tracker t = ((FanMindApplication) getActivity()
                                .getApplication())
                                .getTracker(FanMindApplication.TrackerName.APP_TRACKER);
                        t.send(new HitBuilders.EventBuilder()
                                .setCategory("LeftMenu").setAction("Press Button")
                                .setLabel("LeftMenu Invite Friend").build());

                        Intent intent = new Intent(getActivity(), NotifyFanMindActivity.class);
                        intent.putExtra("kind", 0);
                        startActivity(intent);
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.left_layout05:  // 설정
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.left_layout06:  // 공지사항
                    getActivity().startActivity(new Intent(getActivity(), NoticeActivity.class));
                    break;


                case R.id.button_projectStart:// 프로젝트 등록
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fanmaum.com/projects/make")));

                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
//                case R.id.left_layout07:
//                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
//
//                        Tracker t = ((FanMindApplication) getActivity().getApplication()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);
//                        t.send(new HitBuilders.EventBuilder().setCategory("LeftMenu").setAction("Press Button").setLabel("LeftMenu Invite Friend").build());
//
//                        Intent intent = new Intent(getActivity(), NotifyFanMindActivity.class);
//                        intent.putExtra("kind", 0);
//                        startActivity(intent);
//                    } else {
//                        Utils.showDialog(getActivity());
//                    }
//                    break;
//                case R.id.left_layout08: // 설정가기
//                    // fragmentReplace(5);
//                    Intent intent = new Intent(getActivity(), SettingActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
//                case R.id.left_layout09:
//                    fragmentReplace(6);
//                    break;
//                case R.id.left_layout10:
//                    // showAd();
//                    checkSskey();
//                    // fragmentReplace(7);
//                    break;
                case R.id.nav_top: // 내정보 보기
                    if(FanMindSetting.getLOGIN_OK(getActivity())) {
                        if (mCount == 0) {
                            mNewArea.setVisibility(View.VISIBLE);
                            mCount++;
                            mArrow.setBackgroundResource(R.drawable.left_info_arrow);
                        } else {
                            mNewArea.setVisibility(View.GONE);
                            mCount--;
                            mArrow.setBackgroundResource(R.drawable.left_info_arrow_on);
                        }
                    }else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.myPoint: // 적립내역보기.
                    // showLogin(9);
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        getActivity().startActivity(
                                new Intent(getActivity(), MyPointActivity.class));
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.left_btn01:
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        getActivity().startActivity(
                                new Intent(getActivity(),
                                        LockScreenButtonImageActivity.class));
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    // startActivity(new Intent(getActivity(),
                    // GVCalendarActivity.class));
                    break;
//                case R.id.left_layout11:
//                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
//                        mParams = new ArrayList<NameValuePair>();
//                        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams,
//                                URLAddress.DAILY_TIME(getActivity()),
//                                AsyncTaskValue.DAILY_SELECT, LeftMenuFragment2.this);
//                    } else {
//                        Utils.showDialog(getActivity());
//                    }
//                    break;
                case R.id.my_profile0://나의 참여 현황
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        getNetwork();
                        mArrow.setBackgroundResource(R.drawable.left_info_arrow_on);
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.my_profile1://내 페이지
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        getActivity().startActivity(
                                new Intent(getActivity(),
                                        UserPageActivity2.class));
                        mArrow.setBackgroundResource(R.drawable.left_info_arrow_on);
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                case R.id.my_profile2://프로필 설정
                    showProfile();
                    mArrow.setBackgroundResource(R.drawable.left_info_arrow_on);
                    break;

                case R.id.my_profile3: {//로그아웃

                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        String title = getString(R.string.profile06);
                        String content = getString(R.string.logout);
                        String left = getString(R.string.cancel);
                        String right = getString(R.string.confirmation);
                        mDialog = new CustomDialog(getActivity(), title, content, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkPermission();
                                mDialog.dismiss();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                FanMindSetting.setLogout(getActivity());
                            }
                        }, left, right);
                        mDialog.show();
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                }
            }
        }
    };

    private void insertSskey() {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("sskey", FanMindSetting
                .getSESSION_KEY(getActivity())));
        mParams.add(new BasicNameValuePair("id", FanMindSetting
                .getEMAIL_ID(getActivity())));
        mParams.add(new BasicNameValuePair("nick", FanMindSetting
                .getNICK_NAME(getActivity())));
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams,
                URLAddress.SSKEY_INSERT(getActivity()), AsyncTaskValue.SSKEY_INSERT,
                LeftMenuFragment2.this);
    }

    private static final int REQUEST_READ_CONTACTS = 1;
    boolean isActivePlusLockScreen;

    public void checkPermission() {
        try {
            FanMindSetting.setLOCKSCREEN(getActivity(), false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);

            } else {

                isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(getActivity());

                if (isActivePlusLockScreen) {
                    IgawPlusLock.activateLockScreen(getActivity(), true, iplusLockResultCallback);
                } else {
                    IgawPlusLock.activateLockScreen(getActivity(), false, iplusLockResultCallback);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void  getNetwork() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(getContext(), mParams);

        HttpRequest.setHttp1(getActivity(), URLAddress.MAIN_PAGE(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                            if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                                FanMindSetting.setIsLinked(getActivity(), true);
                            } else {
                                FanMindSetting.setIsLinked(getActivity(), false);
                            }

                            //개설한 프로젝트
                            try {
                                JSONObject jsonObject_projects = new JSONObject(Utils.getJsonData(Utils.getJsonData(result, "data"), "projects"));
                                JSONArray jsonArray_attended = jsonObject_projects.getJSONArray("attended");

                                Intent intent = new Intent(getContext(), UserPageMyAttendedActivity.class);
                                intent.putExtra("jsonObjectData", Utils.getJsonData(result, "data"));
                                intent.putExtra("jsonArray_attended", jsonArray_attended.toString());
                                startActivity(intent);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }

        );
    }

    IPlusLockResultCallback iplusLockResultCallback = new IPlusLockResultCallback() {
        @Override
        public void onResult(ResultModel rm) {

            if (rm != null && rm.isResult()) { //서버에 요청한 결과가 true인 경우에 결과 처리
                if (isActivePlusLockScreen) {// On 시도가 성공한 경우, 서비스 시작.
                    IgawPlusLock.startLockScreenService(getActivity());
                    IgawPlusLock.setFlagDismissKeyguard(getActivity(), false);
                } else { // OFF 시도가 성공한 경우, 서비스 종료.
                    IgawPlusLock.stopLockScreenService(getActivity());
                    IgawPlusLock.setFlagDismissKeyguard(getActivity(), true);
                }
            } else {
                if (isActivePlusLockScreen) {
                    IgawPlusLock.activateLockScreen(getActivity(), true, iplusLockResultCallback);
                } else {
                    IgawPlusLock.activateLockScreen(getActivity(), false, iplusLockResultCallback);
                }
            }
        }
    };

    private void checkSskey() {
        if (FanMindSetting.getLOGIN_OK(getActivity())) {
            startActivity(new Intent(getActivity(), SaveMindActivity.class));
        } else {
            Utils.showDialog(getActivity());
        }
    }

    // 프로필
    private void showProfile() {
        if (FanMindSetting.getLOGIN_OK(getActivity())) {
            // ((BaseActivity)getActivity()).toggle();
            getActivity().startActivity(new Intent(getActivity(), ProfileActivity.class));
        } else {
            Utils.showDialog(getActivity());
        }
    }

    // 스케줄
    private void showScheduel() {
        fragmentReplace(0);
//        ((BaseActivity) context).toggle();
    }

    // 동영상
    private void showMovie() {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        getActivity().startActivity(intent);
        // ((BaseActivity)getActivity()).toggle();
    }

    // 이벤트
    private void showEvent() {
        fragmentReplace(1);
//        ((BaseActivity) context).toggle();
    }

    // 내연예인요청
    private void showRequest() {
        fragmentReplace(3);
    }

    // 캐시아웃
    private void showCashOut() {
        fragmentReplace(4);
//        ((BaseActivity) context).toggle();
    }

    /// 공지사항
    private void showNotice() {
        fragmentReplace(5);
//        ((BaseActivity) context).toggle();
    }

    // 설정
    private void showSetting() {
        fragmentReplace(6);
//        ((BaseActivity) context).toggle();
    }

    // 내 쿠폰 바로가기
    private void showMyCupon() {
        fragmentReplace(8);
//        ((BaseActivity) context).toggle();
    }

    // 마음적립하러가기
    private void showSaveMind() {
        fragmentReplace(9);
//        ((BaseActivity) context).toggle();
    }

    /**
     * 로그인 팝업
     *
     * @param index 프래그먼트 번호
     */
    private void showLogin(int index) {
        if (FanMindSetting.getLOGIN_OK(getActivity())) {
            fragmentReplace(index);
        } else {
            Utils.showDialog(getActivity());
        }
    }

    /**
     * 팬마음 LEFT메뉴
     *
     * @reqNewFragmentIndex 0  스케쥴
     * @reqNewFragmentIndex 1  이벤트
     * @reqNewFragmentIndex 2  내연예인요청
     * @reqNewFragmentIndex 3  캐시아웃
     * @reqNewFragmentIndex 4  공지사항
     * @reqNewFragmentIndex 5  설정
     * @reqNewFragmentIndex 6  쿠폰함바로가기
     * @reqNewFragmentIndex 7  마음적립하기
     * @reqNewFragmentIndex 8  프로필설정
     * @reqNewFragmentIndex 9  내마음 적립포인트보기S
     * @reqNewFragmentIndex 10 프로필설정 이미지
     */
    public void fragmentReplace(int reqNewFragmentIndex) {
        if (mLeftBtnNumber != reqNewFragmentIndex) {
            Utils.clearFrag(getActivity());
            Fragment newFragment = null;
            newFragment = getFragment(reqNewFragmentIndex);
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment, "LeftMenu" + String.valueOf(reqNewFragmentIndex));
            transaction.commitAllowingStateLoss();
            mLeftBtnNumber = reqNewFragmentIndex;
//            if (((MainActivity) MainActivity.mMainContext) != null) {
//                ((MainActivity) MainActivity.mMainContext).mImageTitle.setVisibility(View.GONE);
//                (MainActivity) MainActivity.mMainContext).setTabBackground(-1);
//                ((MainActivity) MainActivity.mMainContext).mBtnNumber = -1;
//                ((MainActivity) MainActivity.mMainContext).mTextTitle.setVisibility(View.VISIBLE);
//            }
        }
//        ((BaseActivity) context).toggle();
    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;
        switch (idx) {
            case 0:
                // 스케줄
                newFragment = new ScheduelFragment();
                break;
            case 1:
                // 이벤트
                // newFragment = new EventActivity();
                break;
            case 2:
                // 연예인 추가
                // newFragment = new RequestMyFanActivity();
                break;
            case 3:
                // 캐시아웃
                newFragment = new CashOutFragment();
                break;
            case 4:
                // 공지사항
                // newFragment = new NoticeActivity();
                break;
            case 5:
                // 설정
                // newFragment = new SettingFragment();
                break;
            case 6:
                // 쿠폰함 바로가기
                newFragment = new MyCuponFragment();
                break;
            case 7:
                // 마음적립하러가기
                newFragment = new SaveMindFragment();
                break;
            case 8:
                // 프로필설정
                // newFragment = new ProfileFragment();
                break;
            case 9:
                // 내 마음 적립포인트보기
                // newFragment = new MyPointActivity();
                break;
            case 10:
                // 버튼 이미지 설정.
                // newFragment = new LockScreenButtonImageFragment();
                break;
            default:
                break;
        }
        return newFragment;
    }

    /**
     * 프로필설정.
     */
    public void setProfile() {
        if (FanMindSetting.getMY_PIC(getActivity()).contains("null"))
            mProfile.setBackgroundResource(R.drawable.profile_basic01);
        else
            ImageLoader.getInstance().displayImage(
                    FanMindSetting.getMY_PIC(getActivity()), mProfile);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mLeftContext = null;
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.MAIN_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonData(result);
            }
        } else if (output == AsyncTaskValue.SSKEY_INSERT_NUM) {
            if (Utils.getJsonDataString(result).equals("1101")) {
                //showAd();
            } else {
                Utils.setToast(getActivity(), R.string.networkerror);
            }
        } else if (output == AsyncTaskValue.DAILY_SELECT_NUM) {
            if (Utils.getJsonData(result)) {
                checkTime(result);
            }
        }
    }

/*    private void showAd() {
        AdPOPcornStyler.themeStyle.themeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.textThemeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.rewardThemeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.rewardCheckThemeColor = Color.parseColor("#e82b47");
        IgawCommon.setUserId(FanMindSetting.getSESSION_KEY(getActivity()));
        String mind = getString(R.string.savemind);
        AdPOPcornStyler.offerwall.Title = mind;
        IgawAdpopcorn.openOfferWall(getActivity());
        IgawAdpopcorn.setEventListener(getActivity(), new IAdPOPcornEventListener() {
            @Override
            public void OnClosedOfferWallPage() {
                // TODO Auto-generated method stub
                if (FanMindSetting.getSAVEMIND_EXIT(getActivity())) {
                    mParams = new ArrayList<NameValuePair>();
                    mParams = Utils.setSession(getActivity(), mParams);
                    HttpRequest.setHttp(getActivity(), mAsyncTask, mParams,
                            URLAddress.MAIN_PAGE(),
                            AsyncTaskValue.MAIN, LeftMenuFragment2.this);
                }
            }
        });
    }*/

    private void getJsonData(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String my_heart = json.getString("my_heart");
            FanMindSetting.setMY_HEART(getActivity(), my_heart);
            setMind(Utils.getMoney(my_heart));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMind(String my_heart) {
        mMindTv.setText(my_heart);
    }

    private void checkTime(String key) {
        String time = null;
        try {
            JSONObject list = new JSONObject(key);
            time = list.getString("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("123", time);
//        Intent intent = new Intent(getActivity(), DailyActivity.class);
//        intent.putExtra("key", time);
//        intent.putExtra("start", false);
//        getActivity().startActivity(intent);
    }

}
