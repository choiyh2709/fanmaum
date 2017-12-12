package specup.fanmind.main.tab4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.UserPageAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.HorizontalListView;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.main.tab0.ProjectDetailWebView;
import specup.fanmind.vo.SupportList;

/**
 * 개인 페이지 ( 나)
 */
public class UserPageActivity2 extends Activity {

    View view;
    TextView navi_tv01, nickName, total_followingUser, textView_totalFollowers;
    ImageView mProfile, profile_background;
    LinearLayout layout_followers, layout_followingUser;
    HorizontalListView hListView_hostedProject, hListView_feededProject;
    ScrollView scroll_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_user_page2);

        setView();


    }

    @Override
    protected void onResume() {
        getNetwork();
        super.onResume();
    }

    private void getNetwork() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);

        HttpRequest.setHttp1(this, URLAddress.MAIN_PAGE(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                    if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                        FanMindSetting.setIsLinked(UserPageActivity2.this, true);
                    } else {
                        FanMindSetting.setIsLinked(UserPageActivity2.this, false);
                    }


                    setData(Utils.getJsonData(result, "data"));
                } else {
                    Utils.setToast(UserPageActivity2.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    JSONObject jsonObjectData;

    private void setData(String data) {
        try {
            jsonObjectData = new JSONObject(data);

            //닉네임
            navi_tv01.setText(jsonObjectData.getString("nick"));
            nickName.setText(jsonObjectData.getString("nick"));

            //user image
            String imageUrl = jsonObjectData.getString("pic_base") + jsonObjectData.getString("pic");
            HttpRequest.getNetworkCircleImage(this,imageUrl, mProfile);
            ImageLoader.getInstance().displayImage(imageUrl, profile_background);

            try {
                String linkages = Utils.getJsonData(jsonObjectData.toString(), "linkages");
                JSONArray jsonArray = new JSONArray(linkages);

                for (int i = 0; i < jsonArray.length(); i++) {
                    if (Utils.getJsonData(jsonArray.getString(i), "service").equals("facebook")) {
                        ImageView ImageView_fan_facebook = (ImageView) findViewById(R.id.fan_facebook);
                        ImageView_fan_facebook.setTag(Utils.getJsonData(jsonArray.getString(i), "auth_value"));
                        ImageView_fan_facebook.setVisibility(View.VISIBLE);
                        ImageView_fan_facebook.setOnClickListener(onclickEvent);
                    } else if (Utils.getJsonData(jsonArray.getString(i), "service").equals("twitter")) {
                        ImageView ImageView_fan_twitter = (ImageView) findViewById(R.id.fan_twitter);
                        ImageView_fan_twitter.setTag(Utils.getJsonData(jsonArray.getString(i), "auth_value"));
                        ImageView_fan_twitter.setVisibility(View.VISIBLE);
                        ImageView_fan_twitter.setOnClickListener(onclickEvent);
                    } else if (Utils.getJsonData(jsonArray.getString(i), "service").equals("twitter")) {
                        ImageView ImageView_fan_instagram = (ImageView) findViewById(R.id.fan_instagram);
                        ImageView_fan_instagram.setTag(Utils.getJsonData(jsonArray.getString(i), "auth_value"));
                        ImageView_fan_instagram.setVisibility(View.VISIBLE);
                        ImageView_fan_instagram.setOnClickListener(onclickEvent);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //팬수
            total_followingUser.setText(jsonObjectData.getString("total_followings"));
            textView_totalFollowers.setText(jsonObjectData.getString("total_followers"));
            //개설한 프로젝트
            try {
                JSONObject jsonObject_projects = new JSONObject(Utils.getJsonData(jsonObjectData.toString(), "projects"));

                //개설한 프로젝트
                List<Object> projectList = new ArrayList<Object>();
                JSONArray jsonArray_hosted = jsonObject_projects.getJSONArray("hosted");
                if (jsonArray_hosted.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_hostedProject)).setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray_hosted.length(); i++) {
                        JSONObject itemJSONObject = jsonArray_hosted.getJSONObject(i);

                        SupportList supportList = new SupportList(
                                itemJSONObject.getString("project_srl")
                                , itemJSONObject.getString("title")
                                , itemJSONObject.getString("thumbnail_base") + itemJSONObject.getString("thumbnail")
                                , itemJSONObject.getString("heart_goal")
                                , itemJSONObject.getString("heart_now")
                                , itemJSONObject.getString("begin_time")
                                , itemJSONObject.getString("close_time")
                        );

                        projectList.add(supportList);
                    }

                    UserPageAdapter uerUserPageAdapter = new UserPageAdapter(UserPageActivity2.this, projectList);
                    hListView_hostedProject.setAdapter(uerUserPageAdapter);
                }
                //관심 있는 프로젝트
                List<Object> projectList1 = new ArrayList<Object>();
                JSONArray jsonArray_attended = jsonObject_projects.getJSONArray("feeded");
                if (jsonArray_attended.length() > 0) {
                    ((LinearLayout) findViewById(R.id.layout_feededProject)).setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray_attended.length(); i++) {
                        JSONObject itemJSONObject = jsonArray_attended.getJSONObject(i);

                        SupportList supportList = new SupportList(
                                itemJSONObject.getString("project_srl")
                                , itemJSONObject.getString("title")
                                , itemJSONObject.getString("thumbnail_base") + itemJSONObject.getString("thumbnail")
                                , itemJSONObject.getString("heart_goal")
                                , itemJSONObject.getString("heart_now")
                                , itemJSONObject.getString("begin_time")
                                , itemJSONObject.getString("close_time")
                        );

                        projectList1.add(supportList);
                    }
                    UserPageAdapter uerUserPageAdapte1 = new UserPageAdapter(UserPageActivity2.this, projectList1);
                    hListView_feededProject.setAdapter(uerUserPageAdapte1);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private OnClickListener onclickEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fan_twitter: {
                    Intent intent = new Intent(UserPageActivity2.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.twitter.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
                case R.id.fan_facebook: {
                    Intent intent = new Intent(UserPageActivity2.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.facebook.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
                case R.id.fan_instagram: {
                    Intent intent = new Intent(UserPageActivity2.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.instagram.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
            }
        }
    };


    private void setView() {

        scroll_view = (ScrollView) findViewById(R.id.scroll_view);


        navi_tv01 = (TextView) findViewById(R.id.navi_tv01);
        nickName = (TextView) findViewById(R.id.nickName);
        //프로필 이미지
        mProfile = (ImageView) findViewById(R.id.profile_img01);
        profile_background = (ImageView) findViewById(R.id.profile_background);

        total_followingUser = (TextView) findViewById(R.id.total_followingUser);


        layout_followers = (LinearLayout) findViewById(R.id.layout_followers);
        textView_totalFollowers = (TextView) findViewById(R.id.textView_totalFollowers);
        layout_followingUser = (LinearLayout) findViewById(R.id.layout_followingUser);
//        layout_chatting = (LinearLayout) findViewById(R.id.layout_chatting);

        Button button_attended_project = (Button) findViewById(R.id.button_attended_project);

        layout_followingUser.setOnClickListener(onClick);
        layout_followers.setOnClickListener(onClick);
//        layout_chatting.setOnClickListener(onClick);
        button_attended_project.setOnClickListener(onClick);

        hListView_hostedProject = (HorizontalListView) findViewById(R.id.listView_hostedProject);
        hListView_feededProject = (HorizontalListView) findViewById(R.id.listView_feededProject);

        hListView_hostedProject.setOnItemSelectedListener(onItemSelectedListener1);
        hListView_feededProject.setOnItemSelectedListener(onItemSelectedListener1);


    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            scroll_view.requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            parent.getAdapter().getItem(position);

        }
    };


    OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_followers: {
                    if (Utils.stringToInt(Utils.getJsonData(jsonObjectData.toString(), "total_followers")) > 0) {
                        Intent intent = new Intent(getApplicationContext(), UserPageLikeUserActivity.class);
                        intent.putExtra("jsonObjectData", jsonObjectData.toString());
                        startActivity(intent);
                    }

                }
                break;
                case R.id.layout_followingUser: {
                    if (Utils.stringToInt(Utils.getJsonData(jsonObjectData.toString(), "total_followings")) > 0) {
                        Intent intent = new Intent(getApplicationContext(), UserPageMyLikeUserActivity.class);
                        intent.putExtra("jsonObjectData", jsonObjectData.toString());
                        startActivity(intent);
                    }
                }
                break;
//                case R.id.layout_chatting: {
//                    Utils.setToast(getApplicationContext(), getString(R.string.ready));
//                }
//                break;
                case R.id.button_attended_project: {//나의 프로젝트 참여 현황보기
                    //개설한 프로젝트
                    try {
                        JSONObject jsonObject_projects = new JSONObject(Utils.getJsonData(jsonObjectData.toString(), "projects"));
                        JSONArray jsonArray_attended = jsonObject_projects.getJSONArray("attended");

                        Intent intent = new Intent(getApplicationContext(), UserPageMyAttendedActivity.class);
                        intent.putExtra("jsonObjectData", jsonObjectData.toString());
                        intent.putExtra("jsonArray_attended", jsonArray_attended.toString());
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };

    public void onBack(View v) {
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        super.onBackPressed();
    }


    AdapterView.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            itemSelectAction1(parent, position);

            SupportList supportList = (SupportList) ((HorizontalListView) parent).getAdapter().getItem(position);
            Intent intent = new Intent(UserPageActivity2.this, ProjectDetailActivity.class);
            intent.putExtra("srl", supportList.getSrl());
            setIntent(intent);
            startActivity(intent);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Handler handler = new Handler();

    private void itemSelectAction1(final AdapterView<?> parent, final int position) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            ((LinearLayout) parent.getChildAt(i).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.RED);
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((LinearLayout) parent.getChildAt(position).findViewById(R.id.linear_selectBox)).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
        );
    }

}
