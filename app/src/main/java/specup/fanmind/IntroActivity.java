package specup.fanmind;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.appevents.AppEventsLogger;
import com.igaworks.IgawCommon;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
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
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.left.EventDetailActivity;
import specup.fanmind.main.tab0.ProjectDetailActivity;


public class IntroActivity extends AppCompatActivity implements OnTask {


    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    public static Context mIntroContext;
    String mServerVer, mVersion, force_update; // appver => server, mversion => appver
    public static String app_first = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        IgawCommon.startApplication(IntroActivity.this);
        Fabric.with(this,new Crashlytics());
        Fabric.with(this,new Answers());
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable ignored) {
        }

        ActivityManager.getInstance().allEndActivity();
        mIntroContext = IntroActivity.this;

        Utils.setAnalyticsTracker(getIntent(), this);

        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("appver", Utils.getAppVersion(this)));
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.SERVER, AsyncTaskValue.SERVER, this);

        try {
            PackageInfo i = getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
            mVersion = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

    }


    @Override
    public void onTask(int output, String result) {
// TODO Auto-generated method stub
        if (output == AsyncTaskValue.SERVER_NUM) {
            if (result.length() != 0) {
                if (Utils.getJsonDataString2(result).equals("1001")) {
                    getJsonBase(result);
                } else {
                    Utils.setToast(IntroActivity.this, R.string.server);
                }
            } else {
                Utils.setToast(IntroActivity.this, R.string.server);
            }
        } else if (output == AsyncTaskValue.MAIN_NUM) {

            if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    FanMindSetting.setIsLinked(this, true);
                } else {
                    FanMindSetting.setIsLinked(this, false);
                }

                getJsonData(Utils.getJsonData(result, "data"));
            } else {
                FanMindSetting.setLogout(this);
                Utils.setToast(this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }


    private void getJsonData(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String mystar = json.getString("my_star");
            String nick = json.getString("nick");
            String pic = json.getString("pic");
            String pic_base = json.getString("pic_base");
            String my_heart = json.getString("my_heart");
            String memver_srl = json.getString("member_srl");
            StarSetting.setSTAR_SELECT_INDEX(this, mystar);
            FanMindSetting.setNICK_NAME(this, nick);
            FanMindSetting.setMEMBER_SRL(this, memver_srl);
            FanMindSetting.setMY_HEART(this, my_heart);
            FanMindSetting.setMY_PIC(this, pic_base + pic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                dispatchIntent();
            }
        }, 1500);
    }


    private void getJsonBase(String result) {
        try {
//            JSONObject json = new JSONObject(result);
            String url = Utils.getJsonData(Utils.getJsonData(result, "data"), "base");

            mServerVer = Utils.getJsonData(Utils.getJsonData(result, "data"), "appver");
            force_update = Utils.getJsonData(Utils.getJsonData(result, "data"), "force_update");
            FanMindSetting.setBASE_URL(this, url);
            FanMindApplication.setString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int currentVer = Integer.valueOf(mVersion.replaceAll("\\.", ""));
        int serverVer = Integer.valueOf(mServerVer.replaceAll("\\.", ""));
        // if(!version.equals(currentVersion))
        if (currentVer < serverVer) {
            if (force_update.equals("Y")) {
                showDialog2(this);
            } else {
                showDialog(this);
            }
        } else {
            notGrade();
        }
    }


    private void notGrade() {
        if (FanMindSetting.getAPP_FIRST(IntroActivity.this)) {
            sendStart();
        } else {
            if (FanMindSetting.getLOGIN_OK(this)) {
                mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(this, mParams);

                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.MAIN_PAGE(), AsyncTaskValue.MAIN, this);
            } else {
                sendTab();
            }
        }
    }


    private void sendStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                startActivity(new Intent(IntroActivity.this,
                        StartActivity.class));
                app_first = "Y";
                finish();
            }
        }, 2000);
    }

    private void sendTab() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                FanMindSetting.setMY_PIC(IntroActivity.this, "null");
                FanMindSetting.setMY_HEART(IntroActivity.this, "0");
                FanMindSetting.setNICK_NAME(IntroActivity.this, "GUEST");
                dispatchIntent();
            }
        }, 2000);
    }


    private void dispatchIntent() {
        final Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith("fanmaumRun://fanmaumRun.com")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        String project_srl = uri.getQueryParameter("project_srl");
                        if (project_srl != null) {
                            Intent intent = new Intent(IntroActivity.this, ProjectDetailActivity.class);
                            intent.putExtra("srl", project_srl);
                            startActivity(intent);
                            finish();
                        }
                        String event_srl = uri.getQueryParameter("event_srl");
                        if (event_srl != null) {
                            Intent intent = new Intent(IntroActivity.this, EventDetailActivity.class);
                            intent.putExtra("srl", event_srl);
                            startActivity(intent);
                            finish();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            int index = getIntent().getIntExtra("index", 0);
            String board = getIntent().getStringExtra("board");
            String srl = getIntent().getStringExtra("srl");
            String replysrl = getIntent().getStringExtra("replysrl");
            String star_srl = getIntent().getStringExtra("star_srl");
            String reply_on = getIntent().getStringExtra("reply_on");
            boolean noti = getIntent().getBooleanExtra("noti", false);  // 노티
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("board", board);
            intent.putExtra("noti", noti);
            intent.putExtra("srl", srl);
            intent.putExtra("star_srl", star_srl);
            intent.putExtra("replysrl", replysrl);
            intent.putExtra("reply_on", reply_on);
            intent.putExtra("member_srl", getIntent().getStringExtra("member_srl"));
            intent.putExtra("app_first","N");
            startActivity(intent);
            finish();
        }

    }


    CustomDialog mDialog;

    public void showDialog(Context context) {
        String title = context.getString(R.string.update01);
        String content = context.getString(R.string.update02);
        String left = context.getString(R.string.update03);
        String right = context.getString(R.string.update04);
        mDialog = new CustomDialog(context, title, content,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mDialog.dismiss();
                        notGrade();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Utils.appDawn(getApplicationContext());
            }
        }, left, right);
        mDialog.show();
        mDialog.setCancelable(false);
    }

    public void showDialog2(Context context) {
        String title = context.getString(R.string.update01);
        String content = context.getString(R.string.update02);
        String left = context.getString(R.string.update04);
        mDialog = new CustomDialog(context, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Utils.appDawn(getApplicationContext());
            }
        }, left, false);
        mDialog.show();
        mDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        AppEventsLogger.activateApp(this);
        super.onResume();
        IgawCommon.startSession(IntroActivity.this);
    }

    @Override
    protected void onPause() {
        AppEventsLogger.deactivateApp(this);
        super.onPause();
        IgawCommon.endSession();
    }


}
