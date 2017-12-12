package specup.fanmind.left.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import specup.fanmind.AlertAddressSearch;
import specup.fanmind.R;
import specup.fanmind.common.PullToZoom.pulltozoomview.PullToZoomScrollViewEx;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CircleBitmapDisplayer;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.login.OAuthID;
import specup.fanmind.main.setting.lockscreen.LockScreenButtonImageActivity;
import specup.fanmind.main.tab0.AlertAddressWebView;
import specup.fanmind.main.tab0.ProjectPayment2;
import twitter4j.auth.AccessToken;


public class ProfileActivity extends ActionBarActivity implements OnTask {
    public static enum fanMind_LoginAuthorizationType {email, naver, facebook, twitter}

    private PullToZoomScrollViewEx scrollView;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        mProfileActivity = ProfileActivity.this;
        setContentView(R.layout.activity_profile);

        setView();
    }

    Button mBtnSetting;
    RelativeLayout layout_address;
    EditText edittext_nickName;
    TextView textview_address;
    ImageView mProfile;
    public static ProfileActivity mProfileActivity;


    private void pullToZoomListView() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.profile_content_view, null, false);

        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setHeaderView(headView);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    private void setView() {


        pullToZoomListView();
        mProfile = (ImageView) findViewById(R.id.profile_img01);
        mProfile.setOnClickListener(mClick);
        edittext_nickName = (EditText) findViewById(R.id.edittext_nickName);
        edittext_nickName.setText(FanMindSetting.getNICK_NAME(this));

        textview_address = (TextView) findViewById(R.id.textview_address);
        textview_address.setText(FanMindSetting.getADDRESS(this).equals("null") ? "" : FanMindSetting.getADDRESS(this));

        layout_address = (RelativeLayout) findViewById(R.id.layout_address);
        layout_address.setOnClickListener(onClick);

        Button button_facebook = (Button) findViewById(R.id.button_facebook);
        Button button_twitter = (Button) findViewById(R.id.button_twitter);
        Button button_naver = (Button) findViewById(R.id.button_naver);
        Button button_completed = (Button) findViewById(R.id.button_completed);
        button_facebook.setOnClickListener(onClick);
        button_twitter.setOnClickListener(onClick);
        button_naver.setOnClickListener(onClick);
        button_completed.setOnClickListener(onClick);

        if (!Utils.getLanquageLocal(this).equals("ko_KR")) {
            button_naver.setVisibility(View.GONE);
        }

        RelativeLayout layout_password = (RelativeLayout) findViewById(R.id.layout_password);
        layout_password.setOnClickListener(mClick);

//        mEmailTv.setText(FanMindSetting.getEMAIL_ID(this));

//        mBtnSetting.setOnClickListener(mClick);

        setProfile();
    }


    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.profile_img01:
                    setProfileImage();
                    break;
                case R.id.layout_password:
                    showChangePwd();
                    break;
            }
        }
    };

    //비밀번호 변경
    private void showChangePwd() {
        startActivity(new Intent(ProfileActivity.this, ChangePwdActivity.class));
    }

    //비밀번호 찾기
    private void showFindPwd() {
        startActivity(new Intent(ProfileActivity.this, FindPwdActivity.class));
    }

    ///프로필 이미지.
    private void setProfileImage() {
        startActivity(new Intent(ProfileActivity.this, LockScreenButtonImageActivity.class));
    }

    //핸드폰번호 변경
    private void showChangePhone() {
        startActivity(new Intent(ProfileActivity.this, ChangePhoneActivity.class));
    }

    public void setProfile() {
        if (FanMindSetting.getMY_PIC(this).contains("null"))
            mProfile.setBackgroundResource(R.drawable.profile_basic01);
        else {
            DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.icon_128)
                    .showImageOnLoading(R.drawable.icon_128)
                    .showImageForEmptyUri(R.drawable.icon_128)
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .displayer(new CircleBitmapDisplayer())
                    .build();
            ImageLoader.getInstance().displayImage(FanMindSetting.getMY_PIC(this), mProfile, imageOptions);
            ImageLoader.getInstance().displayImage(FanMindSetting.getMY_PIC(this), (ImageView) findViewById(R.id.profile_background));

        }
    }


    CustomDialog mDialog;


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mProfileActivity = null;
    }

    public void onBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        super.onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {

            case SEARCH_ADDRESS_ACTIVITY:
                try{
                    String intentData = data.getExtras().getString("data");
                    if (intentData != null) {
                        ((TextView) findViewById(R.id.textview_address)).setText(intentData);
                        FanMindSetting.setADDRESS(ProfileActivity.this, intentData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String save_accessToken;
    private CallbackManager callbackManager;
    private specup.fanmind.common.http.AsyncTask mAsyncTask = null;

    AlertAddressSearch alertAddressSearch;
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.layout_address: {
                    Intent intent = new Intent(ProfileActivity.this,AlertAddressWebView.class);
                    startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
                   /* alertAddressSearch = new AlertAddressSearch(ProfileActivity.this, new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            alertAddressSearch.getDialog().cancel();

                            final String tempNickName = edittext_nickName.getText().toString();
                            final String address = (String) resultValue;

                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            mParams = Utils.setSession(ProfileActivity.this, mParams);
                            mParams.add(new BasicNameValuePair("address", address));
                            HttpRequest.setHttp1(ProfileActivity.this, URLAddress.UPDATE_ADDRESS(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {

                                        ((TextView) findViewById(R.id.textview_address)).setText(address);
                                        FanMindSetting.setADDRESS(ProfileActivity.this, address);
                                        Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message"));
                                    } else {
                                        Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "resultcode") + " )");
                                    }
                                }
                            });
                            return null;
                        }
                    });
                    alertAddressSearch.show(getSupportFragmentManager(), "alertAddressSearch");*/
                    break;
                }

                case R.id.button_completed: {

                    final String tempNickName = edittext_nickName.getText().toString();
                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                    mParams = Utils.setSession(ProfileActivity.this, mParams);
                    mParams.add(new BasicNameValuePair("nick", tempNickName));
                    HttpRequest.setHttp1(ProfileActivity.this, URLAddress.UPDATE_NICKNAME(), mParams, new OnTask() {
                        @Override
                        public void onTask(int output, String result) throws JSONException {
                            if (Utils.getJsonData(result, "code").equals("UPDATED")) {

                                FanMindSetting.setNICK_NAME(ProfileActivity.this, tempNickName);
                                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message"));
                            } else {
                                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "resultcode") + " )");
                            }
                        }
                    });
                }
                break;

                /**
                 * 페이스북 로그인
                 */
                case R.id.button_facebook: {
                    callbackManager = OAuthID.onFacebookLogin(ProfileActivity.this, new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {

                            try {
                                JSONObject jsonObject = (JSONObject) resultValue;
                                String code = jsonObject.getString("code");
                                save_accessToken = jsonObject.getString("accessToken");

                                if (code.equals("OK")) {
                                    GraphRequest request = GraphRequest.newMeRequest(
                                            OAuthID.StaticloginResult.getAccessToken(),
                                            new GraphRequest.GraphJSONObjectCallback() {
                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {
                                                    requestServer(fanMind_LoginAuthorizationType.facebook, object.toString());

                                                }
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email,picture");
                                    request.setParameters(parameters);
                                    request.executeAsync();
                                } else {
                                    Utils.setSnackBar(ProfileActivity.this, edittext_nickName, code);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
                }
                break;


                /**
                 * 네이버 로그인
                 */
                case R.id.button_naver: {
                    OAuthID.onNaverLogin(ProfileActivity.this, new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            try {
                                OAuthID.onNaverLogout(ProfileActivity.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {

                                JSONObject jsonObject = (JSONObject) resultValue;
                                String code = jsonObject.getString("code");
                                save_accessToken = jsonObject.getString("accessToken");


                                if (code.equals("OK") || code.equals("bearer")) {
                                    //유저 정보 요청.
                                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                                    mParams.add(new BasicNameValuePair("Authorization", code + " " + save_accessToken));

                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("Authorization", code + " " + save_accessToken);
                                    HttpRequest.setHttpSetHeader(ProfileActivity.this, hashMap, mAsyncTask, null, URLAddress.LOGIN_Naver(), AsyncTaskValue.NAVER_LOGIN, ProfileActivity.this, true);

                                } else {
                                    Utils.setSnackBar(ProfileActivity.this, edittext_nickName, code);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    });
                }
                break;
                case R.id.button_twitter: {
                    OAuthID.twitter_login(ProfileActivity.this, OAuthID.PROFILE_PAGE);
                }
                break;
            }

        }
    };

    @Override
    public void onTask(int output, String result) throws JSONException {

//sns 로그인 결과
        if (output == AsyncTaskValue.NAVER_LOGIN_NUM) {
            if (Utils.getJsonData(result, "resultcode").equals("00")) {
                requestServer(fanMind_LoginAuthorizationType.naver, Utils.getJsonData(result, "response"));

            } else {
                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "resultcode") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_NUM)

        {
            if (Utils.getJsonData(result, "email") != null) {

                requestServer(fanMind_LoginAuthorizationType.facebook, result);
            } else {
                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, ProfileActivity.this.getString(R.string.no_email));
            }


//sns 로그인 후 팬마음 서버에 전송
        } else if (output == AsyncTaskValue.NAVER_LOGIN_RESULT_NUM)

        {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {

                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, getString(R.string.connect_completed));

            } else {
                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_RESULT_NUM)

        {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {

                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, getString(R.string.connect_completed));

            } else {
                Utils.setSnackBar(ProfileActivity.this, edittext_nickName, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }
        }
    }

    /**
     * 팬마음 서버에 전송
     *
     * @param temp
     * @param responseData
     */
    private fanMind_LoginAuthorizationType mFanMind_LoginAuthorizationType;
    private String mResult;

    private void requestServer(fanMind_LoginAuthorizationType temp, String responseData) {

        mFanMind_LoginAuthorizationType = temp;
        mResult = responseData;

        switch (temp) {
            case naver: {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(ProfileActivity.this, mParams);
                mParams.add(new BasicNameValuePair("service", temp.name()));
                mParams.add(new BasicNameValuePair("auth_token", save_accessToken));
                mParams.add(new BasicNameValuePair("auth_value", Utils.getJsonData(responseData, "id")));
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("email", Utils.getJsonData(responseData, "email"));
                    jsonObject1.put("service_pic", Utils.getJsonData(responseData, "profile_image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));

                HttpRequest.setHttp(ProfileActivity.this, mAsyncTask, mParams, URLAddress.MEMBER_UPDATE_LINKAGE(), AsyncTaskValue.NAVER_LOGIN_RESULT, this);
            }
            break;


            case facebook: {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(ProfileActivity.this, mParams);
                mParams.add(new BasicNameValuePair("service", temp.name()));
                mParams.add(new BasicNameValuePair("auth_token", save_accessToken));
                mParams.add(new BasicNameValuePair("auth_value", Utils.getJsonData(responseData, "id")));
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("email", Utils.getJsonData(responseData, "email"));
                    jsonObject1.put("service_pic", Utils.getJsonData(Utils.getJsonData(Utils.getJsonData(responseData, "picture"), "data"), "url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));// 페이스북은 없을경우 처리 해야함.;

                HttpRequest.setHttp(ProfileActivity.this, mAsyncTask, mParams, URLAddress.MEMBER_UPDATE_LINKAGE(), AsyncTaskValue.FACEBOOK_LOGIN_RESULT, this);

            }
            break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Uri uri = intent.getData();

        if (uri != null && uri.toString().startsWith("fanmaum3://twitterresult.com")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        String oauth_verifier = uri.getQueryParameter("oauth_verifier");
                        AccessToken accessToken = OAuthID.twitter.getOAuthAccessToken(OAuthID.requestToken, oauth_verifier);
                        OAuthID.twitter.setOAuthAccessToken(accessToken);


                        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                        mParams = Utils.setSession(ProfileActivity.this, mParams);
                        mParams.add(new BasicNameValuePair("service", "twitter"));
                        mParams.add(new BasicNameValuePair("auth_token", oauth_verifier));
                        mParams.add(new BasicNameValuePair("auth_value", FanMindSetting.getEMAIL_ID(ProfileActivity.this)) );
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("email", FanMindSetting.getEMAIL_ID(ProfileActivity.this));
                            jsonObject1.put("service_pic", "https://twitter.com/" + accessToken.getUserId() + "/profile_image?size=original");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));// 페이스북은 없을경우 처리 해야함.;

                        HttpRequest.setHttp(ProfileActivity.this, mAsyncTask, mParams, URLAddress.MEMBER_UPDATE_LINKAGE(), AsyncTaskValue.FACEBOOK_LOGIN_RESULT,ProfileActivity.this);


                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(ProfileActivity.this, getString(R.string.sns_aleady_update));
                            }
                        });
                    }
                }
            }.start();
        } else {
            onCreate(getIntent().getExtras());
        }

    }
}
