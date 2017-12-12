package specup.fanmind.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;

public class Step2Fragment extends Fragment implements OnTask {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_step2, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    Button mBtn[] = new Button[3];
    RelativeLayout mLayout[] = new RelativeLayout[3];
    EditText mNickName, mRecommand;
    String mEmail, mPwd, mPhone, mJoinCode;
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    TextView mBirth, mStar, mWhere, mCheckNick;
    boolean isNickName;
    String mGender = "F", mMarried = "N";
    String mDeviceID;

    private void setView() {
        StarSetting.setSTAR_LIST_INDEX(getActivity(), "");
        StarSetting.setSTAR_LIST(getActivity(), "");
        ((MakeAccountActivity) MakeAccountActivity.mMakeAccount).mTitle.setText(R.string.makeaccount02);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mEmail = bundle.getString("id");
            mPwd = bundle.getString("pwd");
            mPhone = bundle.getString("phone");
            mJoinCode = bundle.getString("joincode");
            mDeviceID = bundle.getString("imei");
        }

        mNickName = (EditText) getActivity().findViewById(R.id.step2_et01);
        mRecommand = (EditText) getActivity().findViewById(R.id.step2_et02);

        mBtn[0] = (Button) getActivity().findViewById(R.id.step2_btn02);
        mBtn[1] = (Button) getActivity().findViewById(R.id.step2_btn03);
        mBtn[2] = (Button) getActivity().findViewById(R.id.step2_btn06);

        mLayout[0] = (RelativeLayout) getActivity().findViewById(R.id.step2_btn01);
        mLayout[1] = (RelativeLayout) getActivity().findViewById(R.id.step2_btn04);
        mLayout[2] = (RelativeLayout) getActivity().findViewById(R.id.step2_btn05);

        mBirth = (TextView) getActivity().findViewById(R.id.step2_tv04_01);
        mBirth.setText(Utils.getTime().subSequence(0, 4));
        mWhere = (TextView) getActivity().findViewById(R.id.step2_tv08_01);
        mStar = (TextView) getActivity().findViewById(R.id.step2_tv07_01);
        mCheckNick = (TextView) getActivity().findViewById(R.id.step2_tv03);

        for (int i = 0; i < mBtn.length; i++) {
            mBtn[i].setOnClickListener(mClick);
            mLayout[i].setOnClickListener(mClick);
        }

        mNickName.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (!Utils.checkLength(mNickName)) {
                        mParams = new ArrayList<NameValuePair>();
                        mParams.add(new BasicNameValuePair("nick", mNickName.getText().toString().trim()));
                        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.NICKNAME_CHECK(getActivity()),
                                AsyncTaskValue.LOGIN_NICK_CHECK, Step2Fragment.this);
                    }
                }
            }
        });


    }


    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.step2_btn01:
                    mBtn[1].setFocusable(true);
                    mBtn[1].setFocusableInTouchMode(true);
                    mBtn[1].requestFocus();
                    goWhereBirth(true, Utils.BIRTH_YEAR);
                    break;
                case R.id.step2_btn02:
                    //여자 or 남자
                    if (mGender.equals("F")) {
                        mBtn[0].setBackgroundResource(R.drawable.swich_man);
                        mGender = "M";
                    } else {
                        mBtn[0].setBackgroundResource(R.drawable.swich_woman);
                        mGender = "F";
                    }
                    break;
                case R.id.step2_btn03:
                    //기혼 or 미혼
                    if (mMarried.equals("Y")) {
                        mBtn[1].setBackgroundResource(R.drawable.swich_married);
                        mMarried = "N";
                    } else {
                        mBtn[1].setBackgroundResource(R.drawable.swich_single);
                        mMarried = "Y";
                    }
                    break;
                case R.id.step2_btn04:
                    startActivityForResult(new Intent(getActivity(), StarListActivity.class), Utils.STAR_LIST);
                    break;
                case R.id.step2_btn05:
                    goWhereBirth(false, Utils.WHERE_LIVE);
                    break;
                case R.id.step2_btn06:
                    addEntry();
                    break;
            }
        }
    };


    /**
     * 생년월일, 사는곳 팝업
     *
     * @param isBirth     생일 버튼인가?
     * @param requestCode 액티비티 코드
     */
    private void goWhereBirth(boolean isBirth, int requestCode) {
        Intent intent = new Intent(getActivity(), WhereLiveActivity.class);
        intent.putExtra("isBirth", isBirth);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == Utils.WHERE_LIVE) {
            String where = data.getStringExtra("wherelive");
            mWhere.setText(where);
        } else if (requestCode == Utils.STAR_LIST) {
            String star = StarSetting.getSTAR_LIST(getActivity());
            if (star.length() != 0) mStar.setText(star);
            else mStar.setText(R.string.step11);
            Log.e("123", StarSetting.getSTAR_LIST_INDEX(getActivity()));
        } else if (requestCode == Utils.BIRTH_YEAR) {
            String birth = data.getStringExtra("birthyear");
            mBirth.setText(birth);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.LOGIN_JOIN_NUM) {
            Log.e(this.getClass().getSimpleName(), result);
            if (result.contains("1601") || result.contains("1602")) {
                FanMindSetting.setAPP_FIRST(getActivity(), false);
                FanMindSetting.setSESSION_KEY(getActivity(), getJsonData(result));
                FanMindSetting.setEMAIL_ID(getActivity(), mEmail);
                FanMindSetting.setLOGIN_OK(getActivity(), true);
                if (result.contains("1602")) {
                    Toast.makeText(getActivity(), R.string.notrecommend, Toast.LENGTH_LONG).show();
                }
                mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(getActivity(), mParams);
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.MAIN_PAGE(), AsyncTaskValue.MAIN, this);
            } else if (result.contains("1603")) {
                Utils.setToast(getActivity(), R.string.notnickname);
            } else if (Utils.getJsonDataString(result).equals("1302")) {
                Utils.setToast(getActivity(), R.string.certi03);
            }
        } else if (output == AsyncTaskValue.LOGIN_NICK_CHECK_NUM) {
            if (result.contains("1251")) {
                mCheckNick.setText(R.string.account03);
                isNickName = true;
            } else {
                mCheckNick.setText(R.string.account04);
                isNickName = false;
            }
            mCheckNick.setVisibility(View.VISIBLE);
        } else if (output == AsyncTaskValue.MAIN_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonDataMyinfo(result);
            }
        }
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
            String pic = json.getString("pic");
            String pic_base = json.getString("pic_base");
            String my_heart = json.getString("my_heart");
            StarSetting.setSTAR_SELECT_INDEX(getActivity(), mystar);
            FanMindSetting.setNICK_NAME(getActivity(), nick);
            FanMindSetting.setMY_HEART(getActivity(), my_heart);
            FanMindSetting.setMY_PIC(getActivity(), pic_base + pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), R.string.login01, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
        getActivity().finish();
    }


    private void addEntry() {
        mBtn[1].setFocusable(true);
        mBtn[1].setFocusableInTouchMode(true);
        mBtn[1].requestFocus();
        if (!isNickName) {
            Utils.setToast(getActivity(), R.string.notok07);
        } else if (mStar.getText().toString().equals(getString(R.string.step11))) {
            Utils.setToast(getActivity(), R.string.notdel);
        } else if (mWhere.getText().toString().equals(getString(R.string.step11))) {
            Utils.setToast(getActivity(), R.string.blank13);
        } else {
            setEntry();
        }
    }

    private void setEntry() {
        mParams = new ArrayList<NameValuePair>();

        mParams.add(new BasicNameValuePair("id", mEmail));
        mParams.add(new BasicNameValuePair("passwd", mPwd));
        mParams.add(new BasicNameValuePair("nick", mNickName.getText().toString().trim()));
        mParams.add(new BasicNameValuePair("phone", mPhone));
        mParams.add(new BasicNameValuePair("birthyear", mBirth.getText().toString().trim()));
        mParams.add(new BasicNameValuePair("gender", mGender));
        mParams.add(new BasicNameValuePair("married", mMarried));
        mParams.add(new BasicNameValuePair("star", StarSetting.getSTAR_LIST_INDEX(getActivity())));
        mParams.add(new BasicNameValuePair("address", mWhere.getText().toString().trim()));
        //		mParams.add(new BasicNameValuePair("join_code", "156478"));
        mParams.add(new BasicNameValuePair("imei", mDeviceID));
        mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));
        mParams.add(new BasicNameValuePair("os_flag", "A"));

        if (mRecommand.getText().toString().trim().length() != 0) {
            mParams.add(new BasicNameValuePair("friend_nick", mRecommand.getText().toString().trim()));
        }
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_JOIN(),
                AsyncTaskValue.LOGIN_JOIN, this);
    }


}
