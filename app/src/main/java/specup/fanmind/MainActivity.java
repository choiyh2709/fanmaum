package specup.fanmind;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.igaworks.IgawCommon;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.left.EventActivity;
import specup.fanmind.left.EventDetailActivity;
import specup.fanmind.left.MovieActivity;
import specup.fanmind.left.MyPointActivity;
import specup.fanmind.left.NoticeActivity;
import specup.fanmind.left.RequestMyFanActivity;
import specup.fanmind.left.profile.ProfileActivity;
import specup.fanmind.main.setting.lockscreen.LockScreenButtonImageActivity;
import specup.fanmind.main.setting.lockscreen.LockScreenImageActivity;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.main.tab1.ProjectFragment;
import specup.fanmind.main.tab1.SelectStarListActivity;
import specup.fanmind.main.tab2_.NewsFeedDetailActivity;
import specup.fanmind.main.tab2_.NewsFeedFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RightMenuFragment.OnApplySelectedListener {
    public static ViewPager viewPager_main;
    public static MainActivity mMainActivity;
    private Toolbar toolbar;
    public static TextView mtitle_main;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    protected List<Fragment> fragments;

    @Override
    protected void onResume() {
        getNetwork();
        IgawCommon.startSession(MainActivity.this);
        getCheckMemberAttend();
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        IgawCommon.endSession();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        frescoSet();
        setContentView(R.layout.activity_main);
        IgawCommon.startApplication(MainActivity.this);
        mMainActivity = MainActivity.this;
        //로그인 후 메인화면인지 검사.
        try {
            if (getIntent().getBooleanExtra("registration", false)) {
                AlertDialogRecommend dialog = new AlertDialogRecommend(this);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setView();
        setNavigationView();
        app_firstcheck(); // 최초 실행시 연예인 선택
    }

    public void app_firstcheck() {

        if (IntroActivity.app_first.equals("Y")) {
            Intent intent = new Intent(MainActivity.this, SelectStarListActivity.class);
            startActivityForResult(intent, 0);
            IntroActivity.app_first = "N";
        }else {
            setMainViewPager();
        }
    }

    /**
     * 출석체크
     */
    private void getCheckMemberAttend() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.MEMBER_ATTEND(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(Utils.getJsonData(result, "data"), "is_rewarded").equals("Y")) {
                    Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                    intent.putExtra("result", result);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {
            getAlramData();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void showBanner(String url, final String type, final String srl) {

        final RelativeLayout mBanner = (RelativeLayout) findViewById(R.id.banner_layout);
        ImageView mBannerImage = (ImageView) findViewById(R.id.banner_img);

        ImageLoader.getInstance().displayImage(url, mBannerImage, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1,
                                        FailReason arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1,
                                          Bitmap arg2) {
                // TODO Auto-generated method stub
                mBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }
        });

        mBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (type.equals("BS")) {
                    String bs[] = srl.split("/");
                    Intent support = new Intent(MainActivity.this, ProjectDetailActivity.class);
                    support.putExtra("srl", bs[1]);
                    support.putExtra("star_srl", bs[0]);
                    support.putExtra("isNoti2", true);
                    startActivity(support);
                } else if (type.equals("BE")) {
                    Intent event = new Intent(MainActivity.this,
                            EventDetailActivity.class);
                    event.putExtra("srl", srl);
                    startActivity(event);
                } else if (type.equals("BN")) {
                    Intent notice = new Intent(MainActivity.this,
                            NoticeActivity.class);
                    startActivity(notice);
                } else if (type.equals("BU")) {
                    Uri uri = Uri.parse(srl);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.setPackage("com.android.browser");
                    startActivity(it);
                }
                mBanner.setVisibility(View.GONE);
                FanMindSetting.setBANNER(getApplicationContext(), false);
            }
        });
    }


    private void setView() {
        viewPager_main = (ViewPager) findViewById(R.id.viewPager_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mtitle_main = (TextView) findViewById(R.id.tv_title_main);
        mtitle_main.setText(StarSetting.getSTAR_SELECT_NAME(this));
        if(StarSetting.getSTAR_SELECT_NAME(this).equals("")){
            mtitle_main.setText(getString(R.string.all));
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
    }

    private void getNetwork() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.MAIN_PAGE(), mParams,
                new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                            if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                                FanMindSetting.setIsLinked(MainActivity.this, true);
                            } else {
                                FanMindSetting.setIsLinked(MainActivity.this, false);
                            }


                            getJsonData(Utils.getJsonData(result, "data"));
                            Utils.clearFrag(MainActivity.this);
                            setNavigationView();
                        }
                    }
                });
    }


    private void setNavigationView() {
        try {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            LeftMenuFragment2 mFrag = new LeftMenuFragment2();
            RightMenuFragment mFrag2 = new RightMenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.replace(R.id.menu_r_frame, mFrag2);
            t.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
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
            FanMindSetting.setMEMBER_SRL(this, memver_srl);
            FanMindSetting.setNICK_NAME(this, nick);
            FanMindSetting.setMY_HEART(this, my_heart);
            FanMindSetting.setMY_PIC(this, pic_base + pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
        else {
            finish();
            System.exit(0);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * main page 셋팅
     */

    RadioButton buttonItem1;
    RadioButton buttonItem2;
    public void setStarInformation(String param){

    }
    private void setMainViewPager() {


        buttonItem1 = (RadioButton) findViewById(R.id.button_item1);
        buttonItem2 = (RadioButton) findViewById(R.id.button_item2);
        buttonItem1.setOnClickListener(onBottomButtonClick);
        buttonItem2.setOnClickListener(onBottomButtonClick);

        buttonItem1.setChecked(true);


        fragments = new ArrayList<Fragment>();
        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(this, mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        mParams1.add(new BasicNameValuePair("star", null));
        mParams1.add(new BasicNameValuePair("order", "1"));
        mParams1.add(new BasicNameValuePair("insight", "N"));

        // 0번 fragment 프로젝트
        fragments.add(new ProjectFragment());
        // 1번 fragment 뉴스피드
        fragments.add(new NewsFeedFragment());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(MainActivity.this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                // fragment 아이템을 가져옴
                return fragments.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
            }


            public int getItemPosition(Object object) {
                // POSITION_NONE으로 인해 ViewPager는 destoryItem()이 호출되어 fragment가 삭제 된것으로 판단하게 되어 onCreateView가 호출되어 다시 그리는 방식
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                //fragment의 크기를 가져옴
                return fragments.size();
            }
        };
        viewPager_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {//툴바와 하단 버튼의 상태를 바꿔준다
                switch (position) {
                    case 0: {
                        buttonItem1.setChecked(true);
                    }
                    break;
                    case 1: {
                        buttonItem2.setChecked(true);
                    }
                    break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager_main.setAdapter(adapter);
        viewPager_main.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_profile: {
                if (FanMindSetting.getLOGIN_OK(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, LockScreenButtonImageActivity.class));
                } else {
                    Utils.showDialog(MainActivity.this);
                }
            }
            break;
            case R.id.profile:
                if (FanMindSetting.getLOGIN_OK(this)) {
                    // ((BaseActivity)getActivity()).toggle();
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    Utils.showDialog(this);
                }
                break;
            case R.id.myPoint:
                // showLogin(9);
                if (FanMindSetting.getLOGIN_OK(this)) {
                    startActivity(new Intent(this, MyPointActivity.class));
                } else {
                    Utils.showDialog(this);
                }
                break;
            case R.id.freeMaumCharge:
                break;
            case R.id.button_star: {
                drawer.openDrawer(GravityCompat.END);
            }
            break;

        }
    }

    View.OnClickListener onBottomButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_item1: {
                    buttonItem1.setChecked(true);
                    viewPager_main.setCurrentItem(0);
                }
                break;
                case R.id.button_item2: {
                    buttonItem2.setChecked(true);
                    viewPager_main.setCurrentItem(1);
                }
                break;
            }
        }
    };


    private void getAlramData() {
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        boolean isNoti = intent.getBooleanExtra("noti", false);
        String board = intent.getStringExtra("board");
        String srl = intent.getStringExtra("srl");
        String replysrl = intent.getStringExtra("replysrl");
        String star_srl = intent.getStringExtra("star_srl");
        String reply_on = getIntent().getStringExtra("reply_on");
        String member_srl = getIntent().getStringExtra("member_srl");

        //메뉴 선택
        switch (index) {
            case 0: {
                viewPager_main.setCurrentItem(0, true);
                Log.d("idx?",index+"");
                break;
            }
            case 1: {
                viewPager_main.setCurrentItem(1, true);
                Log.d("idx?",index+"");
                break;
            }
            case 2: {
                viewPager_main.setCurrentItem(2, true);
                Log.d("idx?",index+"");
                break;
            }
            case 3: {
                viewPager_main.setCurrentItem(3, true);
                Log.d("idx?",index+"");
                break;
            }
        }


        if (index == 4) {
            Intent movie = new Intent(this, MovieActivity.class);
            movie.putExtra("isLock", true);
            startActivity(movie);
        } else if (index == 5) {
            Intent event = new Intent(this, EventActivity.class);
            event.putExtra("isNoti", true);
            startActivity(event);
        } else if (index == 6) {
            Intent request = new Intent(this, RequestMyFanActivity.class);
            request.putExtra("isNoti", true);
            startActivity(request);
        } else if (index == 9) {
            Intent lock = new Intent(this, LockScreenImageActivity.class);
            lock.putExtra("isNoti", true);
            startActivity(lock);
        } else if (index == 12) {
            // showAdv();
            if (!isNoti) {
//                showAd();
//                fragmentReplace(0);
            } else {
                Intent intent1 = null;
                if (board.equals("1")) {//프로젝트
                    intent1 = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("isNoti", true);
                    intent1.putExtra("srl", srl);
                    intent1.putExtra("replysrl", replysrl);
                    intent1.putExtra("member_srl", member_srl);
                    startActivity(intent1);
                } else if (board.equals("11")) {//이벤트
                    intent1 = new Intent(getApplicationContext(), EventDetailActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("isNoti", true);
                    intent1.putExtra("srl", srl);
                    intent1.putExtra("replysrl", replysrl);
                    intent1.putExtra("member_srl", member_srl);
                    startActivity(intent1);
                } else if (board.equals("4")) {//뉴스피드
                    intent1 = new Intent(getApplicationContext(), NewsFeedDetailActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("isNoti", true);
                    intent1.putExtra("srl", srl);
                    intent1.putExtra("replysrl", replysrl);
                    intent1.putExtra("member_srl", member_srl);
                    startActivity(intent);

                } else if (board.equals("7")) {//답글
                    if (reply_on.equals("1")) {//프로젝트
                        intent1 = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent1.putExtra("isNoti", true);
                        intent1.putExtra("srl", srl);
                        intent1.putExtra("reply_on", reply_on);
                        intent1.putExtra("replysrl", replysrl);
                        intent1.putExtra("member_srl", member_srl);
                        startActivity(intent1);
                    } else if (reply_on.equals("11")) {//이벤트
                        intent1 = new Intent(getApplicationContext(), EventDetailActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent1.putExtra("isNoti", true);
                        intent1.putExtra("srl", srl);
                        intent1.putExtra("reply_on", reply_on);
                        intent1.putExtra("replysrl", replysrl);
                        intent1.putExtra("member_srl", member_srl);
                        startActivity(intent1);
                    }
                }


            }
        } else if (index == 13) { // ��������
            Intent notice = new Intent(this, NoticeActivity.class);
            notice.putExtra("isNoti", isNoti);
            startActivity(notice);
        } else if (index == 14) { // �̺�Ʈ
            Intent event = new Intent(this, EventDetailActivity.class);
            event.putExtra("isNoti", isNoti);
            event.putExtra("srl", srl);
            startActivity(event);
        } else if (index == 15) { // ����Ʈ ������
//            Intent support = new Intent(this, SupportIngDetailActivity.class);
//            support.putExtra("isNoti", isNoti);
//            support.putExtra("srl", srl);
//            support.putExtra("star_srl", star_srl);
//            startActivity(support);
        } else {
            if (FanMindSetting.getBANNER(this)) {
                String info[] = FanMindSetting.getBANNER_INFO(this).split(",");
                showBanner(info[0], info[1], info[2]);
            }
//            fragmentReplace(index);
        }
//        if (!FanMindSetting.getHELP_POPUP(this))
//            setHelpPopup();


        //초기화
        intent.putExtra("index", -1);
    }
    private static ArrayList sMemoryTrimmable;
    public static ArrayList getMemoryTrimmable() {
        return sMemoryTrimmable;
    }

    private void frescoSet(){
        sMemoryTrimmable = new ArrayList<>();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setDownsampleEnabled(true)
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        sMemoryTrimmable.add(trimmable);
                        Log.d("brycegao", "registerMemoryTrimmable size: " + sMemoryTrimmable.size());
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                        sMemoryTrimmable.remove(trimmable);
                        Log.d("brycegao", "unRegisterMemoryTrimmable size: " + sMemoryTrimmable.size());
                    }
                })
                .build();
        Fresco.initialize(this, config);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (sMemoryTrimmable != null) {
            for (int i = 0; i < sMemoryTrimmable.size(); i++) {
                MemoryTrimmable a = (MemoryTrimmable) sMemoryTrimmable.get(i);
                a.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
            }
            sMemoryTrimmable.removeAll(new ArrayList<>());
        }
    }

    @Override
    public void changeDefaultSrar(String event) {
        Log.d("onCatagoryApplySelected","true");
        ProjectFragment projectFragment = (ProjectFragment)fragments.get(0);
        projectFragment.changeDefaultSrar();

        NewsFeedFragment newsFeedFragment = (NewsFeedFragment)fragments.get(1);
        newsFeedFragment.changeDefaultSrar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == 0) {
            setMainViewPager();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
