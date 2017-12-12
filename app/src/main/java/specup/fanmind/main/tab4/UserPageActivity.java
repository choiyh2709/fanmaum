package specup.fanmind.main.tab4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.UserPageAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.HorizontalListView;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.main.tab0.ProjectDetailWebView;
import specup.fanmind.vo.SupportList;

/**
 * 개인 페이지 ( 타)
 */
public class UserPageActivity extends Activity {

    View view;
    TextView navi_tv01, nickName, total_followingUser, total_followers;
    ImageView mProfile, profile_background;
    HorizontalListView hListView_hostedProject, hListView_feededProject;
    LinearLayout layout_following, layout_followers, layout_chatting;
    String following;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_user_page);
        getNetwork();

        setView();

    }


    /**
     * 회원정보
     */
    private void getNetwork() {

        try {
            JsonObject_attends = new JSONObject(getIntent().getStringExtra("JsonObject_attends"));

            List<NameValuePair> mParams = new ArrayList<NameValuePair>();

            mParams = Utils.setSession(this, mParams);
            mParams.add(new BasicNameValuePair("member_srl", JsonObject_attends.getString("member_srl")));
            HttpRequest.setHttp1(this, URLAddress.MEMBER_GET(), mParams, new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {
                    if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                        setData(Utils.getJsonData(result, "data"));
                    } else {
                        Utils.setToast(UserPageActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//
//        //개설한 프로젝트
//        try {
//            JSONArray jsonArray_OrderProject = total_Json.getJSONArray("others");
//
//            if (jsonArray_OrderProject.length() > 0) {
//                ((LinearLayout) findViewById(R.id.layout_recommendProject)).setVisibility(View.VISIBLE);
//
//                List<Object> projectList = new ArrayList<Object>();
//
//                for (int i = 0; i < jsonArray_OrderProject.length(); i++) {
//                    /** project content
//                     * @param srl 번호
//                     * @param title 타이틀
//                     * @param thum 이미지 url
//                     * @param goal 목표요청수
//                     * @param vote 현재요청수
//                     * @param begin_time 시작일
//                     * @param close_time 종료일
//                     */
//                    try {
//                        JSONObject itemJSONObject = jsonArray_OrderProject.getJSONObject(i);
//
//                        SupportList supportList = new SupportList(
//                                itemJSONObject.getString("project_srl")
//                                , itemJSONObject.getString("title")
//                                , itemJSONObject.getString("thumbnail_base") + itemJSONObject.getString("thumbnail")
//                                , itemJSONObject.getString("heart_goal")
//                                , itemJSONObject.getString("heart_now")
//                                , itemJSONObject.getString("begin_time")
//                                , itemJSONObject.getString("close_time")
//                        );
//
//                        projectList.add(supportList);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                HorizontalListView listView_star = (HorizontalListView) findViewById(R.id.hList_recommendProject);
//                HomeFragment_detail_orderProjectAdapter homeFragment_detail_orderProjectAdapter = new HomeFragment_detail_orderProjectAdapter(this, (projectList));
//                hListView_hostedProject.setAdapter(homeFragment_detail_orderProjectAdapter);
////                hListView_hostedProject.setOnItemSelectedListener(onItemSelectedListener1);
//
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//
////        hListView_hostedProject.setAdapter();
////        hListView_feededProject.setAdapter();
//
//        hListView_hostedProject.setOnItemClickListener(onItemClickListener);
//        hListView_feededProject.setOnItemClickListener(onItemClickListener);

    }

    private OnClickListener onclickEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fan_twitter: {
                    Intent intent = new Intent(UserPageActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.twitter.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
                case R.id.fan_facebook: {
                    Intent intent = new Intent(UserPageActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.facebook.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
                case R.id.fan_instagram: {
                    Intent intent = new Intent(UserPageActivity.this, ProjectDetailWebView.class);
                    intent.putExtra("url", "http://www.instagram.com/" + v.getTag());
                    startActivity(intent);
                }
                break;
            }
        }
    };

    JSONObject jsonObjectData;

    private void setData(String data) {
        try {

            jsonObjectData = new JSONObject(data);
            try {
                //닉네임
                navi_tv01.setText(jsonObjectData.getString("nick"));
                nickName.setText(jsonObjectData.getString("nick"));

                //user image
                String imageUrl = jsonObjectData.getString("pic_base") + jsonObjectData.getString("pic");
                HttpRequest.getNetworkCircleImage(this,imageUrl, mProfile);
                ImageLoader.getInstance().displayImage(imageUrl, profile_background);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //sns
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
//삭제예정
//                String string_fan_twitter = Utils.getJsonData(jsonArray.getString(0), "facebook");
//                String string_fan_facebook = Utils.getJsonData(jsonArray.getString(1), "twitter");
//                String string_fan_instagram = Utils.getJsonData(jsonArray.getString(2), "instagram");
//
//
//                if (string_fan_twitter.length() > 0) {
//                    ImageView ImageView_fan_twitter = (ImageView) findViewById(R.id.fan_twitter);
//                    ImageView_fan_twitter.setVisibility(View.VISIBLE);
//                    ImageView_fan_twitter.setOnClickListener(onclickEvent);
//                }
//                if (string_fan_facebook.length() > 0) {
//                    ImageView ImageView_fan_facebook = (ImageView) findViewById(R.id.fan_facebook);
//                    ImageView_fan_facebook.setVisibility(View.VISIBLE);
//                    ImageView_fan_facebook.setOnClickListener(onclickEvent);
//                }
//                if (string_fan_instagram.length() > 0) {
//                    ImageView ImageView_fan_instagram = (ImageView) findViewById(R.id.fan_instagram);
//                    ImageView_fan_instagram.setVisibility(View.VISIBLE);
//                    ImageView_fan_instagram.setOnClickListener(onclickEvent);
//                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //팬수
            total_followers.setText(jsonObjectData.getString("total_followers"));
            following = jsonObjectData.getString("relation");
            //개설한 프로젝트
            try {
                JSONObject jsonObject_projects = new JSONObject(Utils.getJsonData(jsonObjectData.toString(), "projects"));

                //개설한 프로젝트
                List<Object> projectList = new ArrayList<Object>();
                JSONArray jsonArray_hosted = jsonObject_projects.getJSONArray("hosted");
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

                UserPageAdapter uerUserPageAdapter = new UserPageAdapter(UserPageActivity.this, projectList);
                hListView_hostedProject.setAdapter(uerUserPageAdapter);

                //관심 있는 프로젝트
                List<Object> projectList1 = new ArrayList<Object>();
                JSONArray jsonArray_attended = jsonObject_projects.getJSONArray("feeded");
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

                UserPageAdapter uerUserPageAdapte1 = new UserPageAdapter(UserPageActivity.this, projectList1);
                hListView_feededProject.setAdapter(uerUserPageAdapte1);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    JSONObject JsonObject_attends;

    private void setView() {


        navi_tv01 = (TextView) findViewById(R.id.navi_tv01);
        nickName = (TextView) findViewById(R.id.nickName);
        //프로필 이미지
        mProfile = (ImageView) findViewById(R.id.profile_img01);
        profile_background = (ImageView) findViewById(R.id.profile_background);

        total_followers = (TextView) findViewById(R.id.textView_totalFollowers);

        layout_followers = (LinearLayout) findViewById(R.id.layout_followers);
        layout_following = (LinearLayout) findViewById(R.id.layout_following);

        layout_following.setOnClickListener(onClick);
        layout_followers.setOnClickListener(onClick);


        hListView_hostedProject = (HorizontalListView) findViewById(R.id.listView_hostedProject);
        hListView_feededProject = (HorizontalListView) findViewById(R.id.listView_feededProject);

        hListView_hostedProject.setOnItemSelectedListener(onItemSelectedListener1);
        hListView_feededProject.setOnItemSelectedListener(onItemSelectedListener1);

    }

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
                    Intent intent = new Intent(getApplicationContext(), UserPageLikeUserActivity.class);

                    intent.putExtra("JsonObject_attends", JsonObject_attends.toString());
                    intent.putExtra("jsonObjectData", jsonObjectData.toString());
                    startActivity(intent);
                }
                break;
                case R.id.layout_following: {

                        if (following.equals("following") || following.equals("friend")) {//팬이였을때
                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            try {
                                mParams.add(new BasicNameValuePair("follow_srl", JsonObject_attends.getString("member_srl")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mParams = Utils.setSession(getApplicationContext(), mParams);

                            HttpRequest.setHttp1(UserPageActivity.this, URLAddress.UNFOLLOWING(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                        following = Utils.getJsonData(Utils.getJsonData(result, "data"), "relation");
                                        total_followers.setText(Utils.getJsonData(Utils.getJsonData(result, "data"), "total_followers"));
                                    } else {
                                        Utils.setSnackBar(UserPageActivity.this, null, Utils.getJsonData(result, "message"));
                                    }

                                }
                            });
                        } else {//팬이 아닐때
                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            try {
                                mParams.add(new BasicNameValuePair("follow_srl", JsonObject_attends.getString("member_srl")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mParams = Utils.setSession(getApplicationContext(), mParams);

                            HttpRequest.setHttp1(UserPageActivity.this, URLAddress.FOLLOWING(), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                        following = Utils.getJsonData(Utils.getJsonData(result, "data"), "relation");
                                        total_followers.setText(Utils.getJsonData(Utils.getJsonData(result, "data"), "total_followers"));
                                    } else {
                                        Utils.setSnackBar(UserPageActivity.this, null, Utils.getJsonData(result, "message"));
                                    }

                                }
                            });
                        }
                    }
                break;
//                case R.id.layout_chatting: {
//                    Utils.setToast(getApplicationContext(), getString(R.string.ready));
//                }
//                break;
                case R.id.button_attended_project: {

                }
                break;

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
            Intent intent = new Intent(UserPageActivity.this, ProjectDetailActivity.class);
            intent.putExtra("srl", supportList.getSrl());
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
