package specup.fanmind.main.tab4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.UserPageMyAttendedAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.ProjectVO;

/**
 * 나의 참여 현황
 */
public class UserPageMyAttendedActivity extends Activity {

    JSONObject jsonObjectData;
    JSONArray jsonArray_attended;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_user_page_my_attended);

        try {
            jsonObjectData = new JSONObject(getIntent().getStringExtra("jsonObjectData"));
            jsonArray_attended = new JSONArray(getIntent().getStringExtra("jsonArray_attended"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setView();
//        setData();


    }

    @Override
    protected void onResume() {
        getNetwork();
        super.onResume();
    }

    private void getNetwork() {


        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("member_srl", Utils.getJsonData(jsonObjectData.toString(), "member_srl")));
        mParams.add(new BasicNameValuePair("size", "100"));
        mParams.add(new BasicNameValuePair("next", "0"));

        HttpRequest.setHttp1(this, URLAddress.MEMBER_PROJECT_ATTENDS(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    setData(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                } else {
                    Utils.setToast(UserPageMyAttendedActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }


    private void setData(String tempJsonArrayData) {
        try {

            JSONArray jsonArray = new JSONArray(tempJsonArrayData);
            //관심 있는 프로젝트
            List<Object> projectList1 = new ArrayList<Object>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemJSONObject = null;

                itemJSONObject = jsonArray.getJSONObject(i);

                ProjectVO projectVO = new ProjectVO();
                projectVO.setProject_srl(itemJSONObject.getString("project_srl"));
                projectVO.setTitle(itemJSONObject.getString("title"));
                projectVO.setSlogun(itemJSONObject.getString("slogun"));
                projectVO.setThumbnail(itemJSONObject.getString("thumbnail"));
                projectVO.setThumbnail_base(itemJSONObject.getString("thumbnail_base"));
                projectVO.setBegin_time(itemJSONObject.getString("begin_time"));
                projectVO.setClose_time(itemJSONObject.getString("close_time"));
                projectVO.setHeart_goal(itemJSONObject.getString("heart_goal"));
                projectVO.setHeart_now(itemJSONObject.getString("heart_now"));
                projectVO.setTotal_attends(itemJSONObject.getString("total_attends"));

                projectVO.setHost(Utils.getJsonData(itemJSONObject.toString(), "host"));
                projectVO.setPayment_user_point(Utils.getJsonData(itemJSONObject.toString(), "used_point"));
                projectVO.setPayment_is_paid(Utils.getJsonData(itemJSONObject.toString(), "is_paid"));
                projectVO.setPayment_address_pri(Utils.getJsonData(itemJSONObject.toString(), "address_pri"));
                projectVO.setPayment_address_ext(Utils.getJsonData(itemJSONObject.toString(), "address_ext"));
                projectVO.setPayment_time(Utils.getJsonData(itemJSONObject.toString(), "paid_time"));
                projectVO.setPayment_name(Utils.getJsonData(itemJSONObject.toString(), "name"));
                projectVO.setPayment_order_srl(Utils.getJsonData(itemJSONObject.toString(), "order_srl"));
                projectVO.setPayment_mobile(Utils.getJsonData(itemJSONObject.toString(), "mobile"));
                projectVO.setPayment_email(Utils.getJsonData(itemJSONObject.toString(), "email"));
                projectVO.setPayment_is_extra(Utils.getJsonData(itemJSONObject.toString(), "is_extra"));
                projectVO.setPayment_note(Utils.getJsonData(itemJSONObject.toString(), "note"));
                projectVO.setPayment_note_extra(Utils.getJsonData(itemJSONObject.toString(), "note_extra"));

                projectList1.add(projectVO);
            }


            UserPageMyAttendedAdapter userPageMyAttendedAdapter = new UserPageMyAttendedAdapter(this, projectList1);
            listView.setAdapter(userPageMyAttendedAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setView() {
        listView = (ListView) findViewById(R.id.listView);


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
