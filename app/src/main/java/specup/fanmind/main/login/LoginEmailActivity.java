package specup.fanmind.main.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gcm.GCMRegistrar;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.fanmindsetting.FanMindSetting;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Button mButton_login, mButton_memberReg;
    FragmentPagerAdapter adapter;


    public static ViewPager viewPager_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_login2);
        setView();
        setLoginViewPager();
        registGCM();
    }


    private void setView() {
        viewPager_main = (ViewPager) findViewById(R.id.viewPager_main);

        mButton_login = (Button) findViewById(R.id.radioButton_login);
        mButton_login.setOnClickListener(this);
        mButton_memberReg = (Button) findViewById(R.id.radioButton_memberReg);
        mButton_memberReg.setOnClickListener(this);

    }

    private void registGCM() {
        try {
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            String regId = GCMRegistrar.getRegistrationId(this);

            if (regId.equals(""))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
                GCMRegistrar.register(this, "489712448482");
            else {
                FanMindSetting.setGCM_ID(this, regId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


    /**
     * login page 셋팅
     */
    private void setLoginViewPager() {

        final List<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();

        fragments.add(new IdLoginFragment());
        fragments.add(new IdResistrationFragment());

        mButton_login.setTextColor(Color.RED);

        adapter = new FragmentPagerAdapter(LoginEmailActivity.this.getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {

                return fragments.get(position);
            }


            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mButton_login.setTextColor(Color.BLACK);
                mButton_memberReg.setTextColor(Color.BLACK);

                switch (position) {

                    case 0: {
                        mButton_login.setTextColor(Color.RED);

                    }
                    break;
                    case 1: {
                        mButton_memberReg.setTextColor(Color.RED);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager_main.setAdapter(adapter);


    }


    @Override
    public void onClick(View v) {
        mButton_login.setTextColor(Color.BLACK);
        mButton_memberReg.setTextColor(Color.BLACK);

        switch (v.getId()) {
            case R.id.radioButton_login:
                mButton_login.setTextColor(Color.RED);
                viewPager_main.setCurrentItem(0);
                break;
            case R.id.radioButton_memberReg:
                mButton_memberReg.setTextColor(Color.RED);
                viewPager_main.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.getItem(viewPager_main.getCurrentItem()).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        switch (viewPager_main.getCurrentItem()) {
            case 0: {
                IdLoginFragment item = (IdLoginFragment) adapter.getItem(0);
                item.twitterResult(intent);
            }
            break;
            case 1: {
                IdResistrationFragment item = (IdResistrationFragment) adapter.getItem(1);
                item.twitterResult(intent);
            }
            break;
        }


        super.onNewIntent(intent);
    }
}

