package specup.fanmind.main.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

/**
 * 트위터 이메일 정보 받기 화면.
 */
public class IdResistrationEmailNickName extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Button mButton_login;
    private AutoCompleteTextView mEmailView;
    private EditText edittext_nick;
    FragmentPagerAdapter adapter;
    Intent intent;

    public static ViewPager viewPager_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_login_email);
        intent = getIntent();
        setView();
    }


    private void setView() {
        mButton_login = (Button) findViewById(R.id.email_sign_in_button);
        mButton_login.setOnClickListener(this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        edittext_nick = (EditText) findViewById(R.id.nick);

    }


    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


    @Override
    public void onClick(View v) {
        mButton_login.setTextColor(Color.BLACK);

        switch (v.getId()) {
            case R.id.email_sign_in_button:
                mButton_login.setTextColor(Color.RED);

                String tempString = intent.getStringExtra("sns_name");

                if (tempString.equals("twitter")) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", mEmailView.getText().toString().trim());
                        jsonObject.put("nick", edittext_nick.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("return", jsonObject.toString());
                    this.setResult(RESULT_OK, intent);
                    this.finish();
                } else if (tempString.equals("facebook")) {
                    try {
                        JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonData"));
                        jsonObject.put("email", mEmailView.getText().toString().trim());
                        jsonObject.put("nick", edittext_nick.getText().toString().trim());
                        intent.putExtra("return", jsonObject.toString());
                        this.setResult(RESULT_OK, intent);
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (tempString.equals("naver")) {
                    try {
                        JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonData"));
                        jsonObject.put("email", mEmailView.getText().toString().trim());
                        jsonObject.put("nick", edittext_nick.getText().toString().trim());
                        intent.putExtra("return", jsonObject.toString());
                        this.setResult(RESULT_OK, intent);
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
        }
    }


}

