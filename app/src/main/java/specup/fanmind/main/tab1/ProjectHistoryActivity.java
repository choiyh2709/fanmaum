package specup.fanmind.main.tab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.ProjectListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.vo.ProjectVO;

/**
 * 프로젝트  히스토리
 */
public class ProjectHistoryActivity extends Activity {

    ListView mList;
    ProjectListAdapter mProjectListAdapter;
    List<Object> mSupportList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "Project History");

        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_history_project);

        setView();
        getNetworkProject();
    }

    private void setView() {

        mList = (ListView) findViewById(R.id.list);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectVO projectVO = (ProjectVO) parent.getAdapter().getItem(position);
                goProjectDetail(projectVO);
            }
        });

    }

    /**
     * //스타 프로젝트 리스트
     * <p/>
     * * 정렬방식 (1 : 인기순, 2 : 최신순, 3 : 마감임박, 4 : 최다참여자, 5 : 최고 금액)
     */
    void getNetworkProject() {
        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(this, mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        mParams1.add(new BasicNameValuePair("star", getIntent().getStringExtra("select_star")));
        mParams1.add(new BasicNameValuePair("order", "2"));
        mParams1.add(new BasicNameValuePair("insight", "Y"));
        mParams1.add(new BasicNameValuePair("closed", "Y"));
        HttpRequest.setHttp1(this, URLAddress.PROJECT_ALL(), mParams1, new OnTask() {

            @Override
            public void onTask(int output, String result) throws JSONException {

                String data = Utils.getJsonData(result, "data");
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getJsonData1(Utils.getJsonData(data, "items"));
                } else {
                    mList.setAdapter(null);
                    Utils.setToast(ProjectHistoryActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }

            }
        });
    }

    private void goProjectDetail(ProjectVO projectVO) {

        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("srl", projectVO.getProject_srl());
        intent.putExtra("isHistory", "Y");
        startActivity(intent);
    }


    private void getJsonData1(String result) {
        try {
            if (mSupportList == null)
                mSupportList = new ArrayList<Object>();
            mSupportList.removeAll(mSupportList);

            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject JsonObject_project = jsonArray.getJSONObject(i);
                    ProjectVO projectVO = new ProjectVO();
                    projectVO.setProject_srl(JsonObject_project.getString("project_srl"));
                    projectVO.setTitle(JsonObject_project.getString("title"));
                    projectVO.setSlogun(JsonObject_project.getString("slogun"));
                    projectVO.setThumbnail(JsonObject_project.getString("thumbnail"));
                    projectVO.setThumbnail_base(JsonObject_project.getString("thumbnail_base"));
                    projectVO.setBegin_time(JsonObject_project.getString("begin_time"));
                    projectVO.setClose_time(JsonObject_project.getString("close_time"));
                    projectVO.setHeart_goal(JsonObject_project.getString("heart_goal"));
                    projectVO.setHeart_now(JsonObject_project.getString("heart_now"));
                    projectVO.setTotal_attends(JsonObject_project.getString("total_attends"));
                    projectVO.setReview_srl(JsonObject_project.getString("review_srl"));
                    projectVO.setStar_srl(JsonObject_project.getString("star_srl"));
                    projectVO.setStar_name(JsonObject_project.getString("star_name"));
                    projectVO.setHost(JsonObject_project.getString("host"));
                    mSupportList.add(projectVO);
                }

                mProjectListAdapter = new ProjectListAdapter(this, mSupportList);
                mList.setAdapter(mProjectListAdapter);
            } else {
                mList.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}

