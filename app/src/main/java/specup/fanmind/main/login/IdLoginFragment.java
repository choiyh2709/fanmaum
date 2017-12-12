package specup.fanmind.main.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.pluslock.IgawPlusLock;
import com.igaworks.adpopcorn.pluslock.model.ResultModel;
import com.igaworks.adpopcorn.pluslock.net.IPlusLockResultCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog2;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import twitter4j.auth.AccessToken;

import static android.Manifest.permission.READ_CONTACTS;

public class IdLoginFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnTask {

    private enum fanMind_LoginAuthorizationType {email, naver, facebook, twitter}

    View view;
    Button mEmailSignInButton, mButton_login_facebook, mButton_login_naver, button_login_twitter;
    CallbackManager callbackManager;
    private static final int REQUEST_READ_CONTACTS = 0;

    private specup.fanmind.common.http.AsyncTask mAsyncTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private String save_accessToken;
    private String save_email;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_id_login, null);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }


    private void setView() {

        mButton_login_facebook = (Button) view.findViewById(R.id.button_login_facebook);
        mButton_login_naver = (Button) view.findViewById(R.id.button_login_naver);
        button_login_twitter = (Button) view.findViewById(R.id.button_login_twitter);
        mButton_login_facebook.setOnClickListener(onClick);
        mButton_login_naver.setOnClickListener(onClick);
        button_login_twitter.setOnClickListener(onClick);

        if (!Utils.getLanquageLocal(getActivity()).equals("ko_KR")) {
            mButton_login_naver.setVisibility(View.GONE);
        }


        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        ((TextView) view.findViewById(R.id.textview_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getActivity());
            }
        });

    }

    CustomDialog2 mDialog;

    public void showDialog(final Activity context) {
        String title = context.getString(R.string.profile04);
        String content = context.getString(R.string.login08);
        String left = context.getString(R.string.cancel);
        String right = context.getString(R.string.confirmation);
        mDialog = new CustomDialog2(context, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchPassword(mDialog.getEditTextContent());
                mDialog.dismiss();
            }
        }, left, right);
        mDialog.show();
    }

    public void searchPassword(String id) {
        if (Utils.isEmailValid(id)) {
            List<NameValuePair> mParams;
            mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("id", id));
            HttpRequest.setHttp1(getActivity(), URLAddress.LOGIN_FIND_PW2(), mParams, new OnTask() {

                @Override
                public void onTask(int output, String result) throws JSONException {
                    Toast.makeText(getActivity(), Utils.getJsonData(result, "message"), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.notok04, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        cancel = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        cancel = false;
        super.onResume();
    }

    boolean cancel = false;

    private void attemptLogin() {
        if (mAsyncTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        View focusView = null;
        cancel = false;
        if (Utils.checkLength(mEmailView)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (Utils.checkLength(mPasswordView)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!Utils.isEmailValid(mEmailView.getText().toString().trim())) {
            mEmailView.setError(getString(R.string.notok04));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {

            requestServer(fanMind_LoginAuthorizationType.email, null);

            //          showProgress(true);

        }
    }


    /**
     * 팬마음 서버에 전송
     *
     * @param temp
     * @param responseData
     */
    private void requestServer(fanMind_LoginAuthorizationType temp, String responseData) {

        switch (temp) {
            case email: {
                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                mParams.add(new BasicNameValuePair("os_flag", "A"));
                mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));

                mParams.add(new BasicNameValuePair("id", mEmailView.getText().toString().trim()));
                mParams.add(new BasicNameValuePair("passwd", Utils.getMD5Hash(mEmailView.getText().toString().trim() + mPasswordView.getText().toString().trim())));

                save_email = mEmailView.getText().toString().trim();
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN(), AsyncTaskValue.LOGIN, this);
            }
            break;

            case naver: {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                mParams.add(new BasicNameValuePair("os_flag", "A"));
                mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));

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
                mParams.add(new BasicNameValuePair("extra", Utils.getJsonData(responseData, "email")));

//                save_email = Utils.getJsonData(responseData, "email");
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN(), AsyncTaskValue.NAVER_LOGIN_RESULT, this);
            }
            break;


            case facebook: {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                mParams.add(new BasicNameValuePair("os_flag", "A"));
                mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));

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

//                save_email = Utils.getJsonData(responseData, "email");
                HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN(), AsyncTaskValue.FACEBOOK_LOGIN_RESULT, this);

            }
            break;


        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 14) {
            getLoaderManager().initLoader(0, null, this);
        } else if (Build.VERSION.SDK_INT >= 8) {
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        //허용의 경우
        else if (requestCode == REQUEST_READ_CONTACTS2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(getActivity());
            if (isActivePlusLockScreen) {
                IgawPlusLock.activateLockScreen(getActivity(), true, iplusLockResultCallback);
            } else {
                IgawPlusLock.activateLockScreen(getActivity(), false, iplusLockResultCallback);
            }
        } else {
            // 실행 할 코드
            Utils.setToast(getActivity(), getString(R.string.request_permission));
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void setFanMindSetting(String result) {
        checkPermission();
        FanMindSetting.setAPP_FIRST(getActivity(), false);
        FanMindSetting.setSESSION_KEY(getActivity(), getJsonData1(result));
        FanMindSetting.setLOGIN_OK(getActivity(), true);
        FanMindSetting.setEMAIL_ID(getActivity(), save_email);
        FanMindSetting.setADDRESS(getActivity(), "");

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
        mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.MAIN_PAGE(), AsyncTaskValue.MAIN, this);
    }


    @Override
    public void onTask(int output, String result) {
        if (output == AsyncTaskValue.LOGIN_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {//성공
                setFanMindSetting(result);
            } else {//실패
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }

//sns 로그인 결과
        } else if (output == AsyncTaskValue.NAVER_LOGIN_NUM) {

            if (Utils.getJsonData(result, "resultcode").equals("00")) {
                requestServer(fanMind_LoginAuthorizationType.naver, Utils.getJsonData(result, "response"));
            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "resultcode") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_NUM) {
            if (Utils.getJsonData(result, "email") != null) {
                requestServer(fanMind_LoginAuthorizationType.facebook, result);
            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, getActivity().getString(R.string.no_email));
            }


//sns 로그인 후 팬마음 서버에 전송
        } else if (output == AsyncTaskValue.NAVER_LOGIN_RESULT_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {
                save_email = Utils.getJsonData(Utils.getJsonData(result, "data"), "id");
                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }


        } else if (output == AsyncTaskValue.FACEBOOK_LOGIN_RESULT_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {
                save_email = Utils.getJsonData(Utils.getJsonData(result, "data"), "id");
                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }
        } else if (output == AsyncTaskValue.MAIN_NUM) {
            if (Utils.getJsonData2(result)) {
                getJsonDataMyinfo(result);
            }
        } else if (output == AsyncTaskValue.TWITTER_LOGIN_RESULT_NUM) {
            if (Utils.getJsonData(result, "code").equals("MEMBER_SIGNED_IN")) {
                save_email = Utils.getJsonData(Utils.getJsonData(result, "data"), "id");
                setFanMindSetting(result);

            } else {
                Utils.setSnackBar(getActivity(), mEmailSignInButton, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }
        }

    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                /**
                 * 페이스북 로그인
                 */
                case R.id.button_login_facebook: {
                    callbackManager = OAuthID.onFacebookLogin(getActivity(), new ResultInterface() {
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
                                    Utils.setSnackBar(getActivity(), mEmailSignInButton, code);
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
                case R.id.button_login_naver: {
                    OAuthID.onNaverLogin(getActivity(), new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            try {
                                OAuthID.onNaverLogout(getActivity());
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
                                    HttpRequest.setHttpSetHeader(getActivity(), hashMap, mAsyncTask, null, URLAddress.LOGIN_Naver(), AsyncTaskValue.NAVER_LOGIN, IdLoginFragment.this, true);

                                } else {
                                    Utils.setSnackBar(getActivity(), mEmailSignInButton, code);
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
                 * 트위터 로그인
                 */
                case R.id.button_login_twitter: {
                    OAuthID.twitter_login(getActivity(), OAuthID.LOGIN_PAGE);
                }
                break;
            }

        }
    };

    private void getJsonDataMyinfo(String result) {
        try {
            JSONObject json = new JSONObject(Utils.getJsonData(result, "data"));
            String mystar = json.getString("my_star");
            String nick = json.getString("nick");
            String pic_base = json.getString("pic_base");
            String pic = json.getString("pic");
            String my_heart = json.getString("my_heart");
            String address = json.getString("address");

            StarSetting.setSTAR_SELECT_INDEX(getActivity(), mystar);
            FanMindSetting.setNICK_NAME(getActivity(), nick);
            FanMindSetting.setMY_HEART(getActivity(), my_heart);
            FanMindSetting.setMY_PIC(getActivity(), pic_base + pic);
            FanMindSetting.setADDRESS(getActivity(), address);


            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
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

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    public void twitterResult(Intent intent) {

        final Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith("fanmaum2://twitterresult.com")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        String oauth_verifier = uri.getQueryParameter("oauth_verifier");

                        AccessToken accessToken = OAuthID.twitter.getOAuthAccessToken(OAuthID.requestToken, oauth_verifier);
                        OAuthID.twitter.setOAuthAccessToken(accessToken);

                        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                        mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(getActivity())));
                        mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
                        mParams.add(new BasicNameValuePair("os_flag", "A"));
                        mParams.add(new BasicNameValuePair("app_id", FanMindSetting.getGCM_ID(getActivity())));

                        mParams.add(new BasicNameValuePair("id", mEmailView.getText().toString().trim()));
                        mParams.add(new BasicNameValuePair("service", "twitter"));
                        mParams.add(new BasicNameValuePair("auth_token", oauth_verifier));
                        mParams.add(new BasicNameValuePair("auth_value", String.valueOf(accessToken.getUserId())));
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("email", mEmailView.getText().toString());
                            jsonObject1.put("service_pic", "https://twitter.com/" + accessToken.getUserId() + "/profile_image?size=original");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mParams.add(new BasicNameValuePair("extra", jsonObject1.toString()));// 페이스북은 없을경우 처리 해야함.;
                        save_email = mEmailView.getText().toString().trim();
                        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN(), AsyncTaskValue.TWITTER_LOGIN_RESULT, IdLoginFragment.this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
//        }
    }

    /***********************************************
     * pluslock
     ************************************************/
    private static final int REQUEST_READ_CONTACTS2 = 1;
    boolean isActivePlusLockScreen;

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


    public void checkPermission() {
        FanMindSetting.setLOCKSCREEN(getActivity(), true);
        IgawCommon.setUserId(FanMindSetting.getSESSION_KEY(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS2);

        } else {

            isActivePlusLockScreen = FanMindSetting.getLOCKSCREEN(getActivity());
            System.out.println(":" + view);

            if (isActivePlusLockScreen) {
                IgawPlusLock.activateLockScreen(getActivity(), true, iplusLockResultCallback);
            } else {
                IgawPlusLock.activateLockScreen(getActivity(), false, iplusLockResultCallback);
            }
        }
    }


}
