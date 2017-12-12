package specup.fanmind.main.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.igaworks.IgawCommon;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

public class LoginActivity extends Activity implements OnTask {

    EditText mEmailEdit, mPwdEdit;
    EditText mFindID, mFindPW;
    View mLoginLayout[] = new View[3];
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    TextView mTv[] = new TextView[1];
    Button mBtn[] = new Button[3];
    TextView mText01, mText02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);

        setView();
        registGCM();
    }

    private void setView() {
        mEmailEdit = (EditText) findViewById(R.id.login_et01);
        mPwdEdit = (EditText) findViewById(R.id.login_et02);
        mLoginLayout[0] = (View) findViewById(R.id.makeentry);
        mLoginLayout[1] = (View) findViewById(R.id.findid);
        mLoginLayout[2] = (View) findViewById(R.id.findpw);
        mFindID = (EditText) findViewById(R.id.findid_et01);
        mFindPW = (EditText) findViewById(R.id.findpw_et01);

        mBtn[0] = (Button) findViewById(R.id.login_btn02);
        mBtn[1] = (Button) findViewById(R.id.login_btn03);
        mBtn[2] = (Button) findViewById(R.id.login_btn04);

        mText01 = (TextView) mLoginLayout[0].findViewById(R.id.entry_tv03);
        mText02 = (TextView) mLoginLayout[0].findViewById(R.id.entry_tv04);

        if (Utils.getLocal(this, "English")) {
            mText01.setVisibility(View.GONE);
            mText02.setText(getString(R.string.entry03));
        } else {
            mText01.setText(getString(R.string.entry03));
            mText02.setVisibility(View.GONE);
        }

        setVisible(0);
    }

    private void setBackground(int array) {
        mBtn[0].setBackgroundResource(R.drawable.join_tap01);
        mBtn[1].setBackgroundResource(R.drawable.join_tap02);
        mBtn[2].setBackgroundResource(R.drawable.join_tap03);
        if (array == 0) {
            mBtn[0].setBackgroundResource(R.drawable.join_tap01_on);
        } else if (array == 1) {
            mBtn[1].setBackgroundResource(R.drawable.join_tap02_on);
        } else if (array == 2) {
            mBtn[2].setBackgroundResource(R.drawable.join_tap03_on);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn01:
                //로그인 요청
                requestLogin();
                break;
            case R.id.login_btn02:
                //회원가입만 보이게
                setVisible(0);
                break;
            case R.id.login_btn03:
                //아이디 찾기만 보이게
                setVisible(1);
                break;
            case R.id.login_btn04:
                //비밀번호 찾기만 보이게
                setVisible(2);
                break;
            case R.id.entry_btn01:
                //회원가입
                startActivity(new Intent(this, MakeAccountActivity.class));
                break;
            case R.id.findid_btn01:
                //아이디찾기
                findID();
                break;
            case R.id.findpw_btn01:
                //비밀번호찾기
                if (!Utils.checkLength(mFindPW)) {
                    mParams = new ArrayList<NameValuePair>();
                    mParams.add(new BasicNameValuePair("id", mFindPW.getText().toString().trim()));
                    HttpRequest.setHttp(LoginActivity.this, mAsyncTask, mParams, URLAddress.LOGIN_FIND_PW(this), AsyncTaskValue.LOGIN_FINDPW, this);
                } else {
                    Toast.makeText(this, R.string.blank01, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.entry_tv02:
                startActivity(new Intent(LoginActivity.this, TermActivity.class));
                break;
        }
    }


    //Layout 보이기.
    private void setVisible(int index) {
        for (int i = 0; i < mLoginLayout.length; i++) {
            mLoginLayout[i].setVisibility(View.GONE);
        }
        mFindID.setText("");
        mFindPW.setText("");
        mLoginLayout[index].setVisibility(View.VISIBLE);
        setBackground(index);
    }

    private void findID() {
        if (!Utils.checkLength(mFindID)) {
            if (Utils.isCellphone(mFindID.getText().toString().trim())) {
                mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("phone", mFindID.getText().toString().trim()));
                HttpRequest.setHttp(LoginActivity.this, mAsyncTask, mParams, URLAddress.LOGIN_FIND_ID(this), AsyncTaskValue.LOGIN_FINDID, this);
            } else {
                Toast.makeText(this, R.string.notok06, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.blank10, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLogin() {
        if (Utils.checkLength(mEmailEdit)) {
            Toast.makeText(this, R.string.blank01, Toast.LENGTH_SHORT).show();
        } else if (Utils.checkLength(mPwdEdit)) {
            Toast.makeText(this, R.string.blank02, Toast.LENGTH_SHORT).show();
        } else if (!Utils.isEmailValid(mEmailEdit.getText().toString().trim())) {
            Toast.makeText(this, R.string.notok04, Toast.LENGTH_SHORT).show();
        } else {
            //			Toast.makeText(this, FanMindSetting.getGCM_ID(this), Toast.LENGTH_LONG).show();'
            mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("id", mEmailEdit.getText().toString().trim()));
            mParams.add(new BasicNameValuePair("passwd", mPwdEdit.getText().toString().trim()));
            mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(this)));
            mParams.add(new BasicNameValuePair("os_flag", "A"));
            HttpRequest.setHttp(LoginActivity.this, mAsyncTask, mParams, URLAddress.LOGIN(), AsyncTaskValue.LOGIN, this);
        }
    }


    private void registGCM() {
        try {
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            final String regId = GCMRegistrar.getRegistrationId(this);
            if (regId.equals(""))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
                GCMRegistrar.register(this, "489712448482");
            else {
                //			CoupleRecipeSetting.setGCM_ID(IntroActivity.this, regId);
                FanMindSetting.setGCM_ID(LoginActivity.this, regId);
                Log.e("==============", regId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.LOGIN_NUM) {
            if (result.contains("1101")) {//성공
                FanMindSetting.setLOGIN_OK(this, true);
                FanMindSetting.setAPP_FIRST(LoginActivity.this, false);
                FanMindSetting.setEMAIL_ID(this, mEmailEdit.getText().toString().trim());
                FanMindSetting.setSESSION_KEY(this, getJsonData(result));
                mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(this, mParams);
                HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.MAIN_PAGE(), AsyncTaskValue.MAIN, this);
            } else {//실패
                Toast.makeText(this, R.string.login02, Toast.LENGTH_SHORT).show();
            }
        } else if (output == AsyncTaskValue.LOGIN_FINDID_NUM) {
            if (Utils.getJsonData(result)) {
                getShowID(result);
            } else {
                Toast.makeText(this, R.string.login04, Toast.LENGTH_SHORT).show();
            }
        } else if (output == AsyncTaskValue.LOGIN_FINDPW_NUM) {
            if (result.contains("1801")) {  //성공
                Toast.makeText(this, R.string.login05, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.login06, Toast.LENGTH_SHORT).show();
            }
        } else if (output == AsyncTaskValue.MAIN_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonDataMyinfo(result);
            }
        }
    }


    /**
     * ID보여주는 다이알로그
     *
     * @param result
     */
    private void getShowID(String result) {
        String joinCode = null;
        try {
            JSONObject json = new JSONObject(result);
            String list = json.getString("list");
            JSONObject jsonArray = new JSONObject(list);
            joinCode = jsonArray.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        showDialog(joinCode);
    }


    CustomDialog mDialog;

    private void showDialog(String content) {
        String title = getString(R.string.findid02);
        String btn = getString(R.string.confirmation);
        String recontent = getString(R.string.findid03).replace("{ID}", content);
        mDialog = new CustomDialog(this, title, recontent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, btn);
        mDialog.show();
    }


    private String getJsonData(String result) {
        String joinCode = null;
        try {
            JSONObject json = new JSONObject(result);
            joinCode = json.getString("sskey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joinCode;
    }

    private void getJsonDataMyinfo(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String mystar = json.getString("my_star");
            String nick = json.getString("nick");
            String pic_base = json.getString("pic_base");
            String pic = json.getString("pic");
            String my_heart = json.getString("my_heart");
            String memver_srl = json.getString("member_srl");
            FanMindSetting.setMEMBER_SRL(this, memver_srl);
            FanMindSetting.setNICK_NAME(this, nick);
            FanMindSetting.setMY_HEART(this, my_heart);
            FanMindSetting.setMY_PIC(this, pic_base + pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, R.string.login01, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ActivityManager.getInstance().deleteActivity(this);
    }


}
