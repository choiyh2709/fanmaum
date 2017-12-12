package specup.fanmind.left;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.IgawAdpopcorn;
import com.igaworks.adpopcorn.interfaces.IAdPOPcornEventListener;

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
import specup.fanmind.fanmindsetting.FanMindSetting;


public class SaveMindActivity extends Activity implements OnTask {

    Context mContext;
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_savemind);
        IgawCommon.startApplication(this);
        mContext = this;
    }

    private void checkSskey() {
        if (mContext == null) {
            Toast.makeText(mContext, "죄송합니다. 앱을 종료 후 다시 실행해주세요. 감사합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        insertSskey();
    }

    private void insertSskey() {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("sskey", FanMindSetting.getSESSION_KEY(mContext)));
        mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(mContext)));
        mParams.add(new BasicNameValuePair("nick", FanMindSetting.getNICK_NAME(mContext)));
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.SSKEY_INSERT(mContext),
                AsyncTaskValue.CHANGE_PHONE, this);
    }

    public void onSave(View v) {
        checkSskey();
    }

    public void onBuy(View v) {

        //구글 정책 대비용
        if(Utils.getLanquageLocal(this).equals("sr_RS")){
            startActivity(new Intent(mContext, BuyPointActivity.class));
        }else{
            startActivity(new Intent(mContext, BuyPointWeViewActivity.class));


//        String param = "";
//        param += "&sskey=" + FanMindSetting.getSESSION_KEY(this);
//        param += "&ssid=" + FanMindSetting.getEMAIL_ID(this);
//        param += "&locale=" + Utils.getLanquageLocal(this);
//        param += "&timezone=" + Utils.getTimeZone();
//
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        Uri u = Uri.parse( URLAddress.MEMBERS_PURCHASE() + param);
//        i.setData(u);
//        startActivity(i);

        }

    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.CHANGE_PHONE_NUM) {
            if (Utils.getJsonDataString(result).equals("1101")) {
                showAd();
            } else {
                Utils.setToast(this, R.string.networkerror);
            }
        }
    }

    private void showAd() {
        FanMindSetting.setSAVEMIND_EXIT(this, true);/*
        AdPOPcornStyler.themeStyle.themeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.textThemeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.rewardThemeColor = Color.parseColor("#e82b47");
        AdPOPcornStyler.themeStyle.rewardCheckThemeColor = Color.parseColor("#e82b47");*/
        IgawCommon.setUserId(FanMindSetting.getSESSION_KEY(this));
        String mind = getString(R.string.savemind);
        /*AdPOPcornStyler.offerwall.Title = mind;*/
        IgawAdpopcorn.openOfferWall(this);
        IgawAdpopcorn.setEventListener(this, new IAdPOPcornEventListener() {
            @Override
            public void OnClosedOfferWallPage() {
                // TODO Auto-generated method stub
                if (FanMindSetting.getSAVEMIND_EXIT(mContext)) {
                    mParams = new ArrayList<NameValuePair>();
                    mParams = Utils.setSession(mContext, mParams);
                    HttpRequest.setHttp(mContext, mAsyncTask, mParams, URLAddress.MAIN_PAGE(),
                            AsyncTaskValue.MAIN, SaveMindActivity.this);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        IgawCommon.startSession(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        IgawCommon.endSession();
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

    public void onBack(View v) {
        onBack();
    }

    private void onBack() {
        ActivityManager.getInstance().deleteActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }

        return super.onKeyDown(keyCode, event);
    }


}
