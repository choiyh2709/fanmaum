package specup.fanmind.main.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;

public class IdResistrationFragmentDialog extends DialogFragment implements OnTask {

    View view;
    Button mButton_login_facebook, mButton_login_naver;


    private String save_email;
    private CallbackManager callbackManager;


    private static final int REQUEST_READ_CONTACTS = 0;
    private specup.fanmind.common.http.AsyncTask mAsyncTask = null;

    // UI references.
    private TextView mEmailView;
    private EditText mNickName, mPasswordView, mPasswordComform;
    private Button mEmailSignInButton;


    private Context mContext;
    private String mResult;
    private IdResistrationFragment.fanMind_LoginAuthorizationType mFanMind_LoginAuthorizationType;


    public IdResistrationFragmentDialog(Context context, String result, IdResistrationFragment.fanMind_LoginAuthorizationType fanMind_LoginAuthorizationType) {
        mContext = context;
        mResult = result;
        mFanMind_LoginAuthorizationType = fanMind_LoginAuthorizationType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.fragment_id_registration_dialog, null);

        setView();

        return view;
    }


    /**
     * focusChange 되었을때 사용
     */
    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            switch (v.getId()) {
                case R.id.nick: {
                    if (!hasFocus) {
                        if (!Utils.checkLength(mNickName)) {
                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            mParams = new ArrayList<NameValuePair>();
                            mParams.add(new BasicNameValuePair("nick", mNickName.getText().toString().trim()));
                            HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.NICKNAME_CHECK(getActivity()), AsyncTaskValue.LOGIN_NICK_CHECK, IdResistrationFragmentDialog.this);
                        }
                    }
                }
                break;
                case R.id.password: {
                    if (!hasFocus) {
                        if (!Utils.checkLength(mPasswordView)) {
                            if (Utils.isPassword(mPasswordView.getText().toString().trim())) {
                            } else {
                            }
                        }
                    }
                }
                break;
                case R.id.passwordComform: {
                    if (!hasFocus) {
                        if (!Utils.checkLength(mPasswordComform)) {
                            if (mPasswordView.getText().toString().trim().equals(mPasswordComform.getText().toString().trim())) {
                            } else {
                            }
                        }
                    }
                }
                break;
            }
        }
    };


    private void setView() {

        mNickName = (EditText) view.findViewById(R.id.nick);
        mNickName.setOnFocusChangeListener(focusChangeListener);

        mEmailView = (TextView) view.findViewById(R.id.email);
        mEmailView.setText(Utils.getJsonData(mResult, "email"));

        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnFocusChangeListener(focusChangeListener);

        mPasswordComform = (EditText) view.findViewById(R.id.passwordComform);
        mPasswordComform.setOnFocusChangeListener(focusChangeListener);


        mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    /**
     * 서버에 가입요청
     */
    private void attemptRegistration() {
        if (mAsyncTask != null) {
            return;
        }

        // Reset errors.
        mNickName.setError(null);
        mPasswordView.setError(null);
        mPasswordComform.setError(null);


        String nickName = mNickName.getText().toString();
        String password = mPasswordView.getText().toString().trim();
        String psswordComform = mPasswordComform.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (Utils.checkLength(mNickName)) {
            mNickName.setError(getString(R.string.blank06));
            focusView = mNickName;
            cancel = true;
        }
        if (mPasswordView.getText().length() > 0) {
            if (Utils.checkLength(mPasswordView)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            } else if (Utils.checkLength(mPasswordComform)) {
                mPasswordComform.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordComform;
                cancel = true;
            } else if (!password.equals(psswordComform)) {
                mPasswordComform.setError(getString(R.string.account07));
                focusView = mPasswordComform;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //// TODO: 2016-03-17
            requestServer(mFanMind_LoginAuthorizationType, mResult);

        }
    }


    @Override
    public void onTask(int output, String result) {


        if (output == AsyncTaskValue.LOGIN_NICK_CHECK_NUM) {
            mNickName.setError(null);
            if (result.contains("1251")) {
                Utils.setSnackBar(getActivity(), mNickName, getString(R.string.account03));
            } else if (result.contains("910")) {
                mNickName.setError(getString(R.string.nickNameShort));
                mNickName.requestFocus();
            } else if (result.contains("1252")) {
                mNickName.setError(getString(R.string.account04));
                mNickName.requestFocus();
            } else {
                mNickName.setError(Html.fromHtml(Utils.getJsonData(result, "error_string")));
            }

        } else if (output == AsyncTaskValue.LOGIN_JOIN_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {

                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }

        } else if (output == AsyncTaskValue.MAIN_NUM) {
            if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    FanMindSetting.setIsLinked(getActivity(), true);
                } else {
                    FanMindSetting.setIsLinked(getActivity(), false);
                }


                getJsonDataMyinfo(Utils.getJsonData(result, "data"));
            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }


//sns 로그인 결과
        } else if (output == AsyncTaskValue.NAVER_LOGIN_NUM) {

            if (Utils.getJsonData(result, "resultcode").equals("00")) {
                requestServer(IdResistrationFragment.fanMind_LoginAuthorizationType.naver, Utils.getJsonData(result, "response"));
            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "resultcode") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_NUM) {
            if (Utils.getJsonData(result, "email") != null) {
                requestServer(IdResistrationFragment.fanMind_LoginAuthorizationType.facebook, result);
            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, getActivity().getString(R.string.no_email));
            }


//sns 로그인 후 팬마음 서버에 전송
        } else if (output == AsyncTaskValue.NAVER_LOGIN_RESULT_NUM) {

            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {

                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_RESULT_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {

                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }
        }
    }


    private void setFanMindSetting(String result) {

        FanMindSetting.setLOGIN_OK(getActivity(), true);
        FanMindSetting.setAPP_FIRST(getActivity(), false);
        FanMindSetting.setSESSION_KEY(getActivity(), getJsonData1(result));
        FanMindSetting.setEMAIL_ID(getActivity(), save_email);

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
        mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.MAIN_PAGE(), AsyncTaskValue.MAIN, this);
    }

    private void getJsonDataMyinfo(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String mystar = json.getString("my_star");
            String nick = json.getString("nick");
            String pic_base = json.getString("pic_base");
            String pic = json.getString("pic");
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("registration", true);
        startActivity(intent);
        getActivity().finish();
    }


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
        mDialog = new CustomDialog(getActivity(), title, recontent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, btn);
        mDialog.show();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getActivity().getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

    }


    /**
     * 팬마음 서버에 전송
     *
     * @param temp
     * @param responseData
     */
    private void requestServer(IdResistrationFragment.fanMind_LoginAuthorizationType temp, String responseData) {

        switch (temp) {

            case naver: {
                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                mParams.add(new BasicNameValuePair("os_flag", "A"));
                mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));
                mParams.add(new BasicNameValuePair("nick", mNickName.getText().toString().trim()));
                mParams.add(new BasicNameValuePair("service", temp.name()));
                mParams.add(new BasicNameValuePair("auth_token", IdResistrationFragment.save_accessToken));
                mParams.add(new BasicNameValuePair("auth_value", Utils.getJsonData(responseData, "id")));
                mParams.add(new BasicNameValuePair("id", Utils.getJsonData(responseData, "email")));
                if (mPasswordView.getText().length() > 0) {
                    mParams.add(new BasicNameValuePair("passwd", Utils.getMD5Hash(mEmailView.getText().toString().trim() + mPasswordView.getText().toString().trim())));
                    mParams.add(new BasicNameValuePair("repeat", Utils.getMD5Hash(mEmailView.getText().toString().trim() + mPasswordView.getText().toString().trim())));
                }

                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("email", Utils.getJsonData(responseData, "email"));
                    jsonObject1.put("service_pic", Utils.getJsonData(responseData, "profile_image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));

                save_email = mEmailView.getText().toString().trim();
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_JOIN(), AsyncTaskValue.LOGIN_JOIN, this);
            }
            break;


            case facebook: {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                mParams.add(new BasicNameValuePair("os_flag", "A"));
                mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));
                mParams.add(new BasicNameValuePair("nick", mNickName.getText().toString().trim()));
                mParams.add(new BasicNameValuePair("service", temp.name()));
                mParams.add(new BasicNameValuePair("auth_token", IdResistrationFragment.save_accessToken));
                mParams.add(new BasicNameValuePair("auth_value", Utils.getJsonData(responseData, "id")));
                mParams.add(new BasicNameValuePair("id", Utils.getJsonData(responseData, "email")));
                if (mPasswordView.getText().length() > 0) {
                    mParams.add(new BasicNameValuePair("passwd", Utils.getMD5Hash(mEmailView.getText().toString().trim() + mPasswordView.getText().toString().trim())));
                    mParams.add(new BasicNameValuePair("repeat", Utils.getMD5Hash(mEmailView.getText().toString().trim() + mPasswordView.getText().toString().trim())));
                }
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("email", Utils.getJsonData(responseData, "email"));
                    jsonObject1.put("service_pic", Utils.getJsonData(Utils.getJsonData(Utils.getJsonData(responseData, "picture"), "data"), "url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));// 페이스북은 없을경우 처리 해야함.;

                save_email = mEmailView.getText().toString().trim();
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_JOIN(), AsyncTaskValue.LOGIN_JOIN, this);

            }
            break;
        }
    }


    private String getJsonData1(String result) {
        JSONObject json = null;
        String joinCode = null;
        try {
            json = new JSONObject(result);
            String jsonData = json.getString("data");
            JSONObject json1 = new JSONObject(jsonData);
            joinCode = json1.getString("sskey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joinCode;
    }
}
