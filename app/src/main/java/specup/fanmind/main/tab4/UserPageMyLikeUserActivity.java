package specup.fanmind.main.tab4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.HomeFragment_detail_assistUserAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.HorizontalListView;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.AttendsVO;

/**
 * 개인 페이지 내가 좋아하는 사람들
 */
public class UserPageMyLikeUserActivity extends Activity {

    View view;
    TextView navi_tv01, nickName, total_followers;
    ImageView mProfile, profile_background;
    HorizontalListView hListView_hostedProject, hListView_feededProject;
    LinearLayout layout_following, layout_followers, layout_chatting;

    JSONObject jsonObjectData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_user_page_my_liker);

        try {
            jsonObjectData = new JSONObject(getIntent().getStringExtra("jsonObjectData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setView();

        getNetwork();

    }

    private void getNetwork() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);
        try {
            mParams.add(new BasicNameValuePair("member_srl", jsonObjectData.getString("member_srl")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mParams.add(new BasicNameValuePair("size", "100"));
        mParams.add(new BasicNameValuePair("next", "0"));

        HttpRequest.setHttp1(this, URLAddress.MEMBER_FOLLOWINGS(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    setData(Utils.getJsonData(result, "data"));
                } else {
                    Utils.setToast(UserPageMyLikeUserActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });

        onResume();
    }


    private void setData(String data) {
        try {

            JSONObject jsonObject = new JSONObject(data);
            JSONArray JsonArray_attends = new JSONArray(Utils.getJsonData(jsonObject.toString(),"items"));
            if (JsonArray_attends.length() > 0) {
                List<Object> listAttends = new ArrayList<Object>();

                for (int i = 0; i < JsonArray_attends.length(); i++) {
                    try {
                        AttendsVO attendsVO = new AttendsVO();

                        attendsVO.setMember_srl(JsonArray_attends.getJSONObject(i).getString("member_srl"));
                        attendsVO.setNick(JsonArray_attends.getJSONObject(i).getString("nick"));
                        attendsVO.setPic(JsonArray_attends.getJSONObject(i).getString("pic"));
                        attendsVO.setPic_base(JsonArray_attends.getJSONObject(i).getString("pic_base"));

                        listAttends.add(attendsVO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ListView listView = (ListView) findViewById(R.id.listView);
                HomeFragment_detail_assistUserAdapter homeFragment_detail_assistUserAdapter = new HomeFragment_detail_assistUserAdapter(UserPageMyLikeUserActivity.this, listAttends);
                listView.setAdapter(homeFragment_detail_assistUserAdapter);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


    private void setView() {
//
//        navi_tv01 = (TextView) findViewById(R.id.navi_tv01);
//        navi_tv01.setText(getString(R.string.liker2).replace("{NICKNAME}", Utils.getJsonData(jsonObjectData.toString(), "nick")));


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
                case R.id.layout_following: {

                }
                break;
                case R.id.layout_followers: {

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


    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


}
