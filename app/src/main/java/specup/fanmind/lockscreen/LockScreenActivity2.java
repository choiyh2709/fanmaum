package specup.fanmind.lockscreen;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import specup.fanmind.IntroActivity;
import specup.fanmind.MainActivity;
import specup.fanmind.R;
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
import specup.fanmind.fanmindsetting.ImageSaveDB;
import specup.fanmind.vo.SupportList;

public class LockScreenActivity2 extends Activity implements OnTask {


    public static LockScreenActivity2 mLockScreen;
    ViewPager mPager, mAdPager;
    ImageView mLockBasicBtn, mLockPushBtn;
    ImageView mLock01, mLock01On;
    ImageView mLock02, mLock02On;
    ImageView mLock03, mLock03On;
    RelativeLayout mLayout, mTalkLayout, mHelpLayout, mHelpLayout02;
    ImageSaveDB mImageDb;
    SQLiteDatabase sqlDatabase;
    String imgPath[];
    LockImagePagerAdapter mLockImagePager;
    AdLockImagePagerAdapter mAdLockImagePager;

    int tDay, tAmPm, X, Y, mCenterBtnTop;
    LayoutParams mLockBasicBtnParams;
    boolean isOne, isTwo, isThree;
    boolean isAdLock, isPause, isTime;
    ImageView mWhite, mNetWork;

    ArrayList<SupportList> mBtnList;
    ArrayList<SupportList> mAdList;

    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    String mAPM, mDay, tDate = null, sTalk = "";
    TextView mDate1, mDate2, mTime, mNoon, mTalk;
    Timer mTimer;
    Calendar calendar = Calendar.getInstance();
    TextView mPointText;
    boolean isBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mLockScreen = LockScreenActivity2.this;

        Utils.setAnalyticsTrackerScreenName(this, "Mobile LockScreen");

        mAdList = new ArrayList<SupportList>();
        mBtnList = new ArrayList<SupportList>();
        isTime = false;

        if (Utils.isOnline(this)) {

            onLoad();
        } else {
            FanMindSetting.setLOCKSCREEN_AD(this, false);
        }

    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.LOCKSCREEN_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonData(result);
            }
        } else if (output == AsyncTaskValue.LOCKPOINT_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonPoint(result);
            }
        }
    }

    private void onLoad() {
        mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOCKSCREEN(this), AsyncTaskValue.LOCKSCREEN, this);
    }


    private void getJsonPoint(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String quantity = json.getString("quantity");
            String my_heart = json.getString("my_heart");
            if (FanMindSetting.getALERT_SAVE(this)) {
                notiSave(quantity);
            }
            FanMindSetting.setMY_HEART(this, my_heart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int position = mAdPager.getCurrentItem();
        String type = mAdList.get(position).getThumImgPath();
        String url = mAdList.get(position).getTitle();

        if (type.equals("A")) {
            goFanMaum(12);
        } else if (type.equals("E")) {
            sSrl = url;
            goFanMaum(14);
        } else if (type.equals("S")) {
            String[] array = url.split("/");
            sStarSrl = array[0];
            sSrl = array[1];
            goFanMaum(15);
        } else if (type.equals("N")) {//공지사항
            goFanMaum(13);
        } else {
            goBrowser(url);
        }
    }

    private void getJsonData(String result) {
        try {
            mAdList.removeAll(mAdList);
            mBtnList.removeAll(mBtnList);
            mAdList = new ArrayList<SupportList>();
            mBtnList = new ArrayList<SupportList>();

            JSONObject json = new JSONObject(result);
            String lock_list = json.getString("lock_list");
            String home_list = json.getString("home_list");
            JSONArray jsonArray = new JSONArray(lock_list);
            JSONArray jsonArray2 = new JSONArray(home_list);
            for (int i = 0; i < jsonArray.length(); i++) {
                String lock_srl = jsonArray.getJSONObject(i).getString("lock_srl");
                String img = jsonArray.getJSONObject(i).getString("img");
                String img_base = jsonArray.getJSONObject(i).getString("img_base");
                String url = jsonArray.getJSONObject(i).getString("url");
                String type = jsonArray.getJSONObject(i).getString("type");
                String hit = jsonArray.getJSONObject(i).getString("hit_heart");
                mAdList.add(new SupportList(lock_srl, img_base + img, url, type, hit));
            }

            for (int i = 0; i < jsonArray2.length(); i++) {
                String home_srl = jsonArray2.getJSONObject(i).getString("home_srl");
                String img = jsonArray2.getJSONObject(i).getString("img");
                String img_base = jsonArray2.getJSONObject(i).getString("img_base");
                sTalk = jsonArray2.getJSONObject(i).getString("text");
                mBtnList.add(new SupportList(home_srl, img_base + img));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setView();
    }

    private void setView() {
        isAdLock = FanMindSetting.getLOCKSCREEN_AD(this);
        mPopupLayout = (RelativeLayout) findViewById(R.id.popup_layout);
        mTime = (TextView) findViewById(R.id.lockscreen_tv01);
        mNoon = (TextView) findViewById(R.id.lockscreen_tv02);
        mDate1 = (TextView) findViewById(R.id.lockscreen_tv03);
        mDate2 = (TextView) findViewById(R.id.lockscreen_tv05);
        mTalk = (TextView) findViewById(R.id.lockscreen_tv04);
        mPointText = (TextView) findViewById(R.id.lockscreen_point);

        mHelpLayout = (RelativeLayout) findViewById(R.id.lock_help_layout);
        mHelpLayout02 = (RelativeLayout) findViewById(R.id.lock_help_layout02);

        mTalkLayout = (RelativeLayout) findViewById(R.id.lock_layout02);
        mAdPager = (ViewPager) findViewById(R.id.adlockscreen_pager);
        mLockBasicBtn = (ImageView) findViewById(R.id.lock_center);
        LayoutParams lockParam = new LayoutParams(Utils.getDP(this, 90),
                Utils.getDP(this, 90));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        lockParam.leftMargin = (deviceWidth - Utils.getDP(this, 90)) / 2;
        lockParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lockParam.bottomMargin = Utils.getDP(this, 18);
        mLockBasicBtn.setLayoutParams(lockParam);
        mLockBasicBtn.setScaleType(ScaleType.FIT_XY);
        mLockBasicBtn.setVisibility(View.VISIBLE);

        mPager = (ViewPager) findViewById(R.id.lockscreen_pager);
        mLayout = (RelativeLayout) findViewById(R.id.lock_layout01);
        mImageDb = new ImageSaveDB(this);

        mLockPushBtn = (ImageView) findViewById(R.id.lock_realcenter);

        mLockBasicBtnParams = (LayoutParams) mLockPushBtn.getLayoutParams();

        mLock01 = (ImageView) findViewById(R.id.lock_btn01);
        mLock01On = (ImageView) findViewById(R.id.lock_btn01_on);

        mLock02 = (ImageView) findViewById(R.id.lock_btn02);
        mLock02On = (ImageView) findViewById(R.id.lock_btn02_on);

        mLock03 = (ImageView) findViewById(R.id.lock_btn03);
        mLock03On = (ImageView) findViewById(R.id.lock_btn03_on);

        mWhite = (ImageView) findViewById(R.id.lock_white);
        mAdPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                showLockImage();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        mLockBasicBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                X = (int) event.getRawX();
                Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        lockImageTouchDown(v);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lockImageTouchMove();
                        break;
                    case MotionEvent.ACTION_UP:
                        lockImageTouchUp();
                        break;
                }
                return true;
            }
        });

        long seed = System.nanoTime();
        Collections.shuffle(mAdList, new Random(seed));

        //���� �̹���
        mAdLockImagePager = new AdLockImagePagerAdapter(this);
        mAdPager.setAdapter(mAdLockImagePager);
        mAdPager.setOffscreenPageLimit(mAdList.size());
        showLockImage();

        if (mBtnList.size() == 0) {
            mLockBasicBtn.setBackgroundResource(R.drawable.lock_btn04);
        } else {
            ImageLoader.getInstance().displayImage(mBtnList.get(0).getText(), mLockBasicBtn);
        }

        if (!Utils.isOnline(this)) {
            mLockBasicBtn.setBackgroundResource(R.drawable.lock_btn04);
            mTalkLayout.setVisibility(View.GONE);
        }


        //		if(isAdLock){
        mLayout.setBackgroundColor(Color.TRANSPARENT);
        mAdPager.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.GONE);
        mLayout.setVisibility(View.VISIBLE);
        mTalkLayout.setVisibility(View.GONE);
        mLockPushBtn.setVisibility(View.GONE);
        mLock03.setBackgroundResource(R.drawable.lock_btn05_2);
        mPointText.setVisibility(View.VISIBLE);
        if (FanMindSetting.getLOGIN_OK(this)) {
            if (mAdList.size() > 0) {
                if (!mAdList.get(0).getNowMoney().equals("0"))
                    mPointText.setText("+" + mAdList.get(0).getNowMoney());
                else
                    mPointText.setText("");
            } else {
                mPointText.setText("");
            }
        } else {
            mPointText.setText("");
        }
        mLock02.setVisibility(View.GONE);
        //		}else{
        //			if(!FanMindSetting.getLOCKSCREEN_HELP(this)){
        //				mHelpLayout.setVisibility(View.VISIBLE);
        //			}
        //			mLayout.setBackgroundResource(R.drawable.lock_bg);
        //			showTalk();
        //			mAdPager.setVisibility(View.GONE);
        //			mPager.setVisibility(View.VISIBLE);
        //			mPointText.setVisibility(View.GONE);
        //		}

        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);
        isTime = true;
        getImagePath();

        if (!FanMindSetting.getLOCK_POPUP(this)) {
            FanMindSetting.setLOCK_POPUP(this, true);
            //			showFirst();
        }

    }


    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            showTime();
        }
    };

    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }

    private void showTime() {
        tDay = calendar.get(calendar.DAY_OF_WEEK);
        tAmPm = calendar.get(calendar.AM_PM);
        tDate = Utils.getCalTime2();
        switch (tDay) {
            case 1:
                mDay = getString(R.string.locktime01);
                break;
            case 2:
                mDay = getString(R.string.locktime02);
                break;
            case 3:
                mDay = getString(R.string.locktime03);
                break;
            case 4:
                mDay = getString(R.string.locktime04);
                break;
            case 5:
                mDay = getString(R.string.locktime05);
                break;
            case 6:
                mDay = getString(R.string.locktime06);
                break;
            case 7:
                mDay = getString(R.string.locktime07);
        }

        if (tAmPm == calendar.AM) {
            mAPM = getString(R.string.locktime08);
        } else {
            mAPM = getString(R.string.locktime09);
        }

        String month = tDate.substring(4, 6);
        String day = tDate.substring(6, 8);
        String hour = tDate.substring(8, 10);
        if (hour.startsWith("0")) hour = hour.substring(1);
        if (month.startsWith("0")) month = month.substring(1);
        if (day.startsWith("0")) day = day.substring(1);

        String min = tDate.substring(10, 12);

        mTime.setText(hour + ":" + min);
        mNoon.setText(mAPM);
        //		mDate1.setText(mDay);
        if (Utils.getLocal(this, "English")) {
            String items[] = getResources().getStringArray(R.array.month);
            String allDate = items[Integer.parseInt(month) - 1].substring(0, 3);
            mDate2.setText(allDate + " " + day + " " + mDay);
        } else {
            String allDate = getString(R.string.lock01).replace("{MONTH}", month).replace("{DAY}", day);
            mDate2.setText(allDate + " " + mDay);
        }
    }

    private void showTalk() {
        if (FanMindSetting.getTALK_MSG(this)) {
            if (sTalk.equals("")) mTalkLayout.setVisibility(View.VISIBLE);
            else mTalkLayout.setVisibility(View.GONE);
        } else {
            mTalkLayout.setVisibility(View.GONE);
        }
    }

    public void onHelp(View v) {
        mHelpLayout.setVisibility(View.GONE);
        FanMindSetting.setLOCKSCREEN_HELP(this, true);
    }

    public void onHelpAd(View v) {
        mHelpLayout02.setVisibility(View.GONE);
        FanMindSetting.setLOCKSCREEN_HELPAD(this, true);
    }


    private void lockImageTouchDown(View v) {
        mLock02.setVisibility(View.GONE);
        //		if(isAdLock){//광고중일때
        mLayout.setBackgroundResource(R.drawable.lock_bg);
        mLockPushBtn.setVisibility(View.VISIBLE);
        showLockImagemove();
        mTalkLayout.setVisibility(View.GONE);
        mLock03.setBackgroundResource(R.drawable.lock_btn05);
        //		}else{//광고가 아닐때
        //			mLayout.setBackgroundResource(R.drawable.lock_bg);
        //			mLock01.setBackgroundResource(R.drawable.lock_btn01);
        //			mLock01On.setBackgroundResource(R.drawable.lock_btn01_on);
        //			mLock03.setBackgroundResource(R.drawable.lock_btn05);
        //			showTalk();
        //			mLayout.setVisibility(View.VISIBLE);
        //		}

        LayoutParams lParams = (LayoutParams) v.getLayoutParams();
        mLockBasicBtnParams.leftMargin = X - Utils.getDP(this, 45);
        mLockBasicBtnParams.topMargin = Y - Utils.getDP(this, 70);
        mLockBasicBtnParams.bottomMargin = -250;
        mLockPushBtn.setLayoutParams(mLockBasicBtnParams);
        Log.e("TAG", "X = " + String.valueOf(X) + " / Y = " + String.valueOf(Y));
        mCenterBtnTop = mLockBasicBtn.getTop();
    }


    private void lockImageTouchMove() {
        mLockBasicBtnParams.leftMargin = X - Utils.getDP(this, 45);
        mLockBasicBtnParams.bottomMargin = -250;
        mLockBasicBtnParams.rightMargin = -250;
        mLockBasicBtnParams.topMargin = Y - Utils.getDP(this, 70);
        mLockPushBtn.setLayoutParams(mLockBasicBtnParams);

        int aLeft01 = (int) mLock01.getX();
        int bLeft01 = (int) mLock01.getY();
        int aLeft02 = (int) mLock02.getX();
        int bLeft02 = (int) mLock02.getY();
        int aRight01 = (int) mLock03.getX();
        int bRight01 = (int) mLock03.getY();

        if (((mLockBasicBtnParams.leftMargin >= aLeft01 - Utils.getDP(this, 30)) &&
                (mLockBasicBtnParams.leftMargin <= aLeft01 + Utils.getDP(this, 25)))
                && ((mLockBasicBtnParams.topMargin >= bLeft01 - Utils.getDP(this, 30)) &&
                (mLockBasicBtnParams.topMargin <= bLeft01 + Utils.getDP(this, 25)))) {

            mLockPushBtn.setVisibility(View.INVISIBLE);
            mLock01.setVisibility(View.GONE);
            mLock01On.setVisibility(View.VISIBLE);
            isOne = true;

        } else if (((mLockBasicBtnParams.leftMargin >= aLeft02 - Utils.getDP(this, 30)) &&
                (mLockBasicBtnParams.leftMargin <= aLeft02 + Utils.getDP(this, 25)))
                && ((mLockBasicBtnParams.topMargin >= bLeft02 - Utils.getDP(this, 30)) &&
                (mLockBasicBtnParams.topMargin <= bLeft02 + Utils.getDP(this, 45)))) {
            mLockPushBtn.setVisibility(View.INVISIBLE);
            mLock02.setVisibility(View.GONE);
            mLock02On.setVisibility(View.GONE);
            isTwo = true;
        } else if (((mLockBasicBtnParams.leftMargin <= aRight01 + Utils.getDP(this, 25)) &&
                (mLockBasicBtnParams.leftMargin >= aRight01 - Utils.getDP(this, 30)))
                && ((mLockBasicBtnParams.topMargin >= bRight01 - Utils.getDP(this, 30)) &&
                (mLockBasicBtnParams.topMargin <= bRight01 + Utils.getDP(this, 25)))) {
            mLockPushBtn.setVisibility(View.INVISIBLE);
            mLock03.setVisibility(View.GONE);
            mLock03On.setVisibility(View.VISIBLE);
            isThree = true;
        } else {
            //			if(isAdLock){
            mLayout.setBackgroundResource(R.drawable.lock_bg);
            showLockImagemove();
            //			}else{
            //				mLayout.setBackgroundResource(R.drawable.lock_bg);
            //				mLock01.setVisibility(View.VISIBLE);
            //				mLock03.setVisibility(View.VISIBLE);
            //			}
            mLockPushBtn.setVisibility(View.VISIBLE);
            mLock02.setVisibility(View.GONE);
            mLock01On.setVisibility(View.GONE);
            mLock01.setVisibility(View.VISIBLE);
            mLock03.setVisibility(View.VISIBLE);
            mLock02On.setVisibility(View.GONE);
            mLock03On.setVisibility(View.GONE);
            isTwo = false;
            isOne = false;
            isThree = false;
        }


    }


    private void lockImageTouchUp() {
        showLockImage();
        int value = mCenterBtnTop + Utils.getDP(this, 40);

        mLock01.setVisibility(View.VISIBLE);
        mLock01On.setVisibility(View.GONE);
        mLock03.setVisibility(View.VISIBLE);
        mLock03On.setVisibility(View.GONE);
        mLock02.setVisibility(View.GONE);
        mLock02On.setVisibility(View.GONE);
        //
        //		if(!isAdLock){//광고중일때
        //			showTalk();
        //			mLayout.setBackgroundResource(R.drawable.lock_bg);
        //			mLayout.setVisibility(View.GONE);
        //			mLock01.setBackgroundResource(R.drawable.lock_btn03);
        //			mLock01On.setBackgroundResource(R.drawable.lock_btn03_on);
        //		}else{//광고가 아닐때

        mLayout.setBackgroundColor(Color.TRANSPARENT);
        mTalkLayout.setVisibility(View.GONE);
        mLock03.setBackgroundResource(R.drawable.lock_btn05_2);
        mLockPushBtn.setVisibility(View.GONE);
        //		}

        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);

        if (isOne) {

            //			if(isAdLock){ //광고가 아닐때
            Log.e("isAdLock", "true");
            webView();
            //			}else{ //광고일때
            //				Log.e("isAdLock", "false");
            //				goFanMaum(0);

            //			}

            onLoad();
        } else if (isTwo) {
            changePager();
        } else if (isThree) {

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("Finish")
                    .build());

            ActivityManager.getInstance().allEndActivity();
            finish();
        }


    }

    boolean isNetwork = false;

    private void changePager() {
        if (Utils.isOnline(this)) {
            if (isAdLock) { //광고일때
                mLayout.setBackgroundResource(R.drawable.lock_bg);
                mPager.setVisibility(View.VISIBLE);
                mAdPager.setVisibility(View.GONE);
                mLock01.setBackgroundResource(R.drawable.lock_btn03);
                mLock01On.setBackgroundResource(R.drawable.lock_btn03_on);
                mLayout.setVisibility(View.GONE);
                showTalk();
                isAdLock = false;
                mPointText.setVisibility(View.GONE);
            } else {//광고가 아닐때
                if (!FanMindSetting.getLOCKSCREEN_HELPAD(this)) {
                    mHelpLayout02.setVisibility(View.VISIBLE);
                }

                mLayout.setBackgroundColor(Color.TRANSPARENT);
                long seed = System.nanoTime();
                Collections.shuffle(mAdList, new Random(seed));
                mAdLockImagePager = new AdLockImagePagerAdapter(this);
                mAdPager.setAdapter(mAdLockImagePager);

                showLockImage();

                mTalkLayout.setVisibility(View.GONE);
                mLayout.setVisibility(View.VISIBLE);
                mLockPushBtn.setVisibility(View.GONE);
                mPager.setVisibility(View.GONE);
                mAdPager.setVisibility(View.VISIBLE);
                isAdLock = true;
                mLock03.setBackgroundResource(R.drawable.lock_btn05_2);
                mPointText.setVisibility(View.VISIBLE);
            }

            FanMindSetting.setLOCKSCREEN_AD(this, isAdLock);
            mWhite.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mWhite.setVisibility(View.GONE);
                    if (!Utils.isOnline(LockScreenActivity2.this)) {
                        mTalkLayout.setVisibility(View.GONE);
                    }
                }
            }, 50);
        } else {
            isNetwork = !isNetwork;
            if (FanMindSetting.getRANDOM_IMAGE(this)) {
                if (imgPath[0] == null) {
                    //				getResources().openRawResource(id)
                    imgPath = new String[1];
                    mLockImagePager = new LockImagePagerAdapter(this, shuffle(imgPath), true, isNetwork);
                } else {
                    mLockImagePager = new LockImagePagerAdapter(this, shuffle(imgPath), false, isNetwork);
                }
            } else {
                if (imgPath[0] == null) {
                    //				getResources().openRawResource(id)
                    imgPath = new String[1];
                    mLockImagePager = new LockImagePagerAdapter(this, imgPath, true, isNetwork);
                } else {
                    mLockImagePager = new LockImagePagerAdapter(this, imgPath, false, isNetwork);
                }
            }
            mPager.setAdapter(mLockImagePager);
            mWhite.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mWhite.setVisibility(View.GONE);
                    if (!Utils.isOnline(LockScreenActivity2.this)) {
                        mTalkLayout.setVisibility(View.GONE);
                    }
                }
            }, 50);


        }
    }

    //이미지 가져오기.
    private void getImagePath() {
        sqlDatabase = mImageDb.getReadableDatabase();
        try {
            sqlDatabase.beginTransaction();
            String query = "select * from image where imageType='0' AND imageCheck='0'";
            Cursor cursor = sqlDatabase.rawQuery(query, null);
            if (cursor.getCount() != 0) {
                imgPath = new String[cursor.getCount()];
                int mCount = 0;
                while (cursor.moveToNext()) {
                    imgPath[mCount] = cursor.getString(1);
                    mCount++;
                }
            }
            sqlDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlDatabase.endTransaction();
            mImageDb.close();
        }

        if (FanMindSetting.getRANDOM_IMAGE(this)) {
            if (imgPath == null) {
                //				getResources().openRawResource(id)
                imgPath = new String[1];
                mLockImagePager = new LockImagePagerAdapter(this, shuffle(imgPath), true, false);
            } else {
                mLockImagePager = new LockImagePagerAdapter(this, shuffle(imgPath), false, false);
            }
        } else {
            if (imgPath == null) {
                //				getResources().openRawResource(id)
                imgPath = new String[1];
                mLockImagePager = new LockImagePagerAdapter(this, imgPath, true, false);
            } else {
                mLockImagePager = new LockImagePagerAdapter(this, imgPath, false, false);
            }
        }
        mPager.setAdapter(mLockImagePager);
    }

    //String 배열 섞기
    private static String[] shuffle(String[] result) {
        // TODO Auto-generated method stub
        String temp;
        int seed;
        for (int i = 0; i < result.length; i++) {
            seed = (int) (Math.random() * result.length);
            temp = result[i];
            result[i] = result[seed];
            result[seed] = temp;
        }
        return result;
    }

    public class AdLockImagePagerAdapter extends PagerAdapter {
        Context mContext;

        public AdLockImagePagerAdapter(Context con) {
            super();
            mContext = con;
        }

        @Override
        public int getCount() {
            return mAdList.size();
        }    //여기서는 2개만 할 것이다.

        //뷰페이저에서 사용할 뷰객체 생성/등록
        @Override
        public Object instantiateItem(View pager, int position) {
            ImageView imgView = new ImageView(mContext);
            LinearLayout.LayoutParams imgLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

            ImageLoader.getInstance().displayImage(mAdList.get(position).getText(), imgView);
            imgView.setLayoutParams(imgLayout);
            imgView.setScaleType(ScaleType.FIT_XY);
            ((ViewPager) pager).addView(imgView, 0);
            return imgView;
        }

        //뷰 객체 삭제.
        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        // instantiateItem메소드에서 생성한 객체를 이용할 것인지
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }


    public class LockImagePagerAdapter extends PagerAdapter {
        Context mContext;
        String mPath[];
        Bitmap bm;
        boolean isZero;
        boolean isAd;

        public LockImagePagerAdapter(Context con, String path[], boolean zero, boolean ad) {
            super();
            mContext = con;
            mPath = path;
            isZero = zero;
            isAd = ad;
        }

        @Override
        public int getCount() {
            return mPath.length;
        }    //���⼭�� 2���� �� ���̴�.

        //뷰페이저에서 사용할 뷰객체 생성/등록
        @Override
        public Object instantiateItem(View pager, int position) {
            ImageView imgView = new ImageView(mContext);
            LinearLayout.LayoutParams imgLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            if (isZero) {
                if (!isAd) bm = BitmapFactory.decodeResource(getResources(), R.drawable.lock_default);
                else bm = BitmapFactory.decodeResource(getResources(), R.drawable.network);
            } else {
                bm = BitmapFactory.decodeFile(mPath[position]);
            }
            imgView.setImageBitmap(bm);
            imgView.setScaleType(ScaleType.FIT_XY);
            imgView.setLayoutParams(imgLayout);
            ((ViewPager) pager).addView(imgView, 0);
            return imgView;
        }

        //뷰 객체 삭제.
        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        // instantiateItem메소드에서 생성한 객체를 이용할 것인지
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mLockScreen = null;
    }

    /**
     * 서포트 보내기
     *
     * @param index HOME (0), 프로젝트(1),  뉴스피드(2), 연예인(3)
     */
    private void goFanMaum(int index) {
        Tracker t = ((FanMindApplication) getApplication()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);
        if (index == 0) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("FanMaum HOME")
                    .build());
        } else if (index == 1) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("FanMaum PROJECT")
                    .build());
        } else if (index == 2) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("FanMaum NEWSFEED")
                    .build());
        } else if (index == 5) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("FanMaum ENTERTAINER")
                    .build());
        } else if (index == 12) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("LockScreen")
                    .setAction("Press Button")
                    .setLabel("FanMaum Maum Ad")
                    .build());
        }

        FanMindSetting.setSAVEMIND_EXIT(this, false);

        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent intent = new Intent(this, IntroActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("noti", true);
            intent.putExtra("srl", sSrl);
            intent.putExtra("star_srl", sStarSrl);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("noti", true);
            intent.putExtra("index", index);
            intent.putExtra("srl", sSrl);
            intent.putExtra("star_srl", sStarSrl);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }


    private void showLockImagemove() {
        if (mAdList.size() == 0) {
            return;
        }
        int position = mAdPager.getCurrentItem();
        Log.e("1", String.valueOf(position));
        if (mAdList.get(position).getThumImgPath().equals("B")) {//블로그
            mLock01.setBackgroundResource(R.drawable.blog_btn);
        } else if (mAdList.get(position).getThumImgPath().equals("F")) { // 페이스북
            mLock01.setBackgroundResource(R.drawable.lock_btn07);
        } else if (mAdList.get(position).getThumImgPath().equals("T")) {//트위터
            mLock01.setBackgroundResource(R.drawable.twitter_btn);
        } else if (mAdList.get(position).getThumImgPath().equals("K")) {//카스
            mLock01.setBackgroundResource(R.drawable.kakao_btn);
        } else if (mAdList.get(position).getThumImgPath().equals("A")) {//앱 광고
            mLock01.setBackgroundResource(R.drawable.lock_btn09);
        } else if (mAdList.get(position).getThumImgPath().equals("W")) {//웹
            mLock01.setBackgroundResource(R.drawable.lock_btn08);
        } else if (mAdList.get(position).getThumImgPath().equals("S")) {//앱 서포트
            mLock01.setBackgroundResource(R.drawable.lock_btn01);
        } else if (mAdList.get(position).getThumImgPath().equals("E")) {//앱 이벤트
            mLock01.setBackgroundResource(R.drawable.lock_event);
        } else if (mAdList.get(position).getThumImgPath().equals("N")) {//앱 공지사항
            mLock01.setBackgroundResource(R.drawable.lock_notice);
        }
    }


    private void showLockImage() {
        if (mAdList.size() == 0) {
            return;
        }
        int position = mAdPager.getCurrentItem();
        if (FanMindSetting.getLOGIN_OK(this)) {
            if (!mAdList.get(position).getNowMoney().equals("0"))
                mPointText.setText("+" + mAdList.get(position).getNowMoney());
            else
                mPointText.setText("");
        } else {
            mPointText.setText("");
        }

        Log.e("2", String.valueOf(position));
        if (mAdList.get(position).getThumImgPath().equals("B")) {//블로그
            mLock01.setBackgroundResource(R.drawable.blog_btn_02);
            mLock01On.setBackgroundResource(R.drawable.blog_btn_on);
        } else if (mAdList.get(position).getThumImgPath().equals("F")) { // 페이스북
            mLock01.setBackgroundResource(R.drawable.lock_btn07_02);
            mLock01On.setBackgroundResource(R.drawable.lock_btn07_on);
        } else if (mAdList.get(position).getThumImgPath().equals("T")) {//트위터
            mLock01.setBackgroundResource(R.drawable.twitter_btn_02);
            mLock01On.setBackgroundResource(R.drawable.twitter_btn_on);
        } else if (mAdList.get(position).getThumImgPath().equals("K")) {//카스
            mLock01.setBackgroundResource(R.drawable.kakao_btn_02);
            mLock01On.setBackgroundResource(R.drawable.kakao_btn_on);
        } else if (mAdList.get(position).getThumImgPath().equals("A")) {//앱 광고
            mLock01.setBackgroundResource(R.drawable.lock_btn09_02);
            mLock01On.setBackgroundResource(R.drawable.lock_btn09_on);
        } else if (mAdList.get(position).getThumImgPath().equals("W")) {//웹
            mLock01.setBackgroundResource(R.drawable.lock_btn08_02);
            mLock01On.setBackgroundResource(R.drawable.lock_btn08_on);
        } else if (mAdList.get(position).getThumImgPath().equals("S")) {//앱 서포트
            mLock01.setBackgroundResource(R.drawable.lock_btn01_02);
            mLock01On.setBackgroundResource(R.drawable.lock_btn01_on);
        } else if (mAdList.get(position).getThumImgPath().equals("E")) {//앱 이벤트
            mLock01.setBackgroundResource(R.drawable.lock_event_02);
            mLock01On.setBackgroundResource(R.drawable.lock_event_on);
        } else if (mAdList.get(position).getThumImgPath().equals("N")) {//앱 공지사항
            mLock01.setBackgroundResource(R.drawable.lock_notice_02);
            mLock01On.setBackgroundResource(R.drawable.lock_notice_on);
        }
    }

    String sSrl, sStarSrl;

    private void webView() {
        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);

        int position = mAdPager.getCurrentItem();
        String type = mAdList.get(position).getThumImgPath();
        String url = mAdList.get(position).getTitle();
        if (FanMindSetting.getLOGIN_OK(this)) {
            if (!mAdList.get(position).getNowMoney().equals("") && !mAdList.get(position).getNowMoney().equals("0")) {
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("LockScreen")
                        .setAction("Press Button")
                        .setLabel("WebView Ad")
                        .build());

                mParams = Utils.setSession(this, mParams);
                mParams.add(new BasicNameValuePair("lock_srl", mAdList.get(position).getSrl()));

                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOCKSCREEN_HIT(this), AsyncTaskValue.LOCKPOINT, this);
            } else {
                if (type.equals("A")) {
                    goFanMaum(12);
                } else if (type.equals("E")) {
                    sSrl = url;
                    goFanMaum(14);
                } else if (type.equals("S")) {
                    String[] array = url.split("/");
                    sStarSrl = array[0];
                    sSrl = array[1];
                    goFanMaum(15);
                } else if (type.equals("N")) {//공지사항
                    goFanMaum(13);
                } else {
                    goBrowser(url);
                }
            }
        } else {
            if (type.equals("A")) {
                goFanMaum(12);
            } else if (type.equals("E")) {
                sSrl = url;
                goFanMaum(14);
            } else if (type.equals("S")) {
                String[] array = url.split("/");
                sStarSrl = array[0];
                sSrl = array[1];
                goFanMaum(15);
            } else if (type.equals("N")) {//공지사항
                goFanMaum(13);
            } else {
                goBrowser(url);
            }
        }
    }

    private void goBrowser(String url) {

        Tracker t = ((FanMindApplication) getApplication()).getTracker(
                FanMindApplication.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder()
                .setCategory("LockScreen")
                .setAction("Press Button")
                .setLabel("WebView Ad")
                .build());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        browserIntent.setDataAndType(uri, "text/html");
        isBrowser = true;
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        browserIntent.setData(uri);
        startActivity(browserIntent);
    }

    @Override
    protected void onUserLeaveHint() {
        // TODO Auto-generated method stub
        Log.e("onUserLeaveHint", "onUserLeaveHint");
        if (!isPause) {
            if (!isBrowser) {
                finish();
            }
        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onResume() {

        if (isTime) {
            if (mTimer == null) {
                mTimer = new Timer();
                MainTimerTask timerTask = new MainTimerTask();
                mTimer.schedule(timerTask, 500, 3000);
            }
        }
        isPause = false;
        isBrowser = false;
        super.onResume();
        Log.e("onResume", "onResume");
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onPause();
    }


    RelativeLayout mPopupLayout;
    boolean isPopup;

    public void onPopup(View v) {

        if (mPopupLayout == null) {
            mPopupLayout = (RelativeLayout) findViewById(R.id.popup_layout);
        }
        if (!isPopup) {
            isPopup = true;
            mPopupLayout.setVisibility(View.VISIBLE);
        } else {
            isPopup = false;
            mPopupLayout.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_layout:
                isPopup = false;
                mPopupLayout.setVisibility(View.GONE);
                break;
            case R.id.menu_layout01:
                closePopup();
                break;
            case R.id.menu_layout02:
                goFanMaum(0);
                break;
            case R.id.menu_layout03:
                goFanMaum(1);
                break;
            case R.id.menu_layout04:
                goFanMaum(2);
                break;
            case R.id.menu_layout05:
                goFanMaum(3);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ���ȭ�� ���� �˾�
     */
    private void closePopup() {
        String title = getString(R.string.close01);
        String content = getString(R.string.close02);
        String left = getString(R.string.start03);
        String right = getString(R.string.close03);
        showDialog(this, title, content, left, right);
    }

    CustomDialog mDialog;

    public void showDialog(Context context, String title, String content, String left, String right) {
        mDialog = new CustomDialog(context, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracker t = ((FanMindApplication) getApplication()).getTracker(
                        FanMindApplication.TrackerName.APP_TRACKER);

                t.send(new HitBuilders.EventBuilder()
                        .setCategory("LockScreen")
                        .setAction("Press Button")
                        .setLabel("LockScreen OFF")
                        .build());
                FanMindSetting.setLOCKSCREEN(LockScreenActivity2.this, false);
                stopService(new Intent(LockScreenActivity2.this, LockService.class));
                mDialog.dismiss();
                finish();
            }
        }, left, right);
        mDialog.show();
    }


    /**
     * ���ȭ�� �̵�
     */
    private void showFirst() {
        String title = getString(R.string.lock02);
        String content = getString(R.string.lock03);
        String left = getString(R.string.start03);
        String right = getString(R.string.lock04);
//		showFirstDialog(this, title, content, left, right);
    }

//	public void showFirstDialog(Context context, String title, String content, String left, String right){
//		mDialog = new CustomDialog(context, title, content, new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mDialog.dismiss();
//			}
//		}, new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				isPause = true;
//				Intent intent = new Intent(LockScreenActivity2.this, LockScreenImageActivity.class);
//				intent.putExtra("lock", true);
//				startActivityForResult(intent, Utils.LOCK);
//				mDialog.dismiss();
//			}
//		}, left, right, 16);
//		mDialog.show();
//	}


    private void notiSave(String point) {//�� ��Ƽ.
        FanMindSetting.setPUSH_STAY(getApplicationContext(), true);
        String maum = getString(R.string.dailysave).replace("{POINT}", point);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(maum)
                .setSmallIcon(R.drawable.icon_128)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        notification.setDefaults(Notification.DEFAULT_VIBRATE);
        notification.setLights(Color.RED, 3000, 3000);
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Utils.LOCK) {
            imgPath = null;
            getImagePath();
        }

        super.onActivityResult(requestCode, resultCode, data);
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


}
