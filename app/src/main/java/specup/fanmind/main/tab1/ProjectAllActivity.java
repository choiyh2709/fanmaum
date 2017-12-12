package specup.fanmind.main.tab1;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.ProjectListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.vo.ProjectVO;

import static specup.fanmind.main.tab1.ProjectFragment.select_star;


public class ProjectAllActivity extends AppCompatActivity {

    ListView mList;
    ProjectListAdapter mProjectListAdapter;
    List<Object> mSupportList;

    Button button_sub_all, button_sub_popula;
    private String sortList = "1";
    private String isHistory = "";

    private int mCount_All=0;
    final String[] save_All = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all);

        setView();
    }

    public void onBack(View v) {
        onBack();
    }

    public void onBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        ActivityManager.getInstance().deleteActivity(this);
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
		}
		return super.onKeyDown(keyCode, event);
	}

    private void setView(){

        button_sub_all = (Button) findViewById(R.id.button_sub_all);
        button_sub_popula = (Button) findViewById(R.id.button_sub_popula);
        button_sub_all.setOnClickListener(onClickListener);
        button_sub_popula.setOnClickListener(onClickListener);

        mList = (ListView) findViewById(R.id.list);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectVO projectVO = (ProjectVO) parent.getAdapter().getItem(position);

                goProjectDetail(projectVO);
            }
        });

        getNetworkProject();
    }

    void getNetworkProject() {

        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(this, mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        if(mCount_All == 0) {
            mParams1.add(new BasicNameValuePair("star", ""));
        }else {
            mParams1.add(new BasicNameValuePair("star", select_star));
        }
        mParams1.add(new BasicNameValuePair("order", sortList));
        if(isHistory.equals("Y")){
            mParams1.add(new BasicNameValuePair("insight", "Y"));
            mParams1.add(new BasicNameValuePair("closed", "Y"));
        }else{
            mParams1.add(new BasicNameValuePair("insight", "N"));
        }

        AsyncTask mAsyncTask = null;
        HttpRequest.setHttp(this, mAsyncTask, mParams1, URLAddress.PROJECT_ALL(), "Project", new OnTask() {
                @Override
                public void onTask(int output, String result) throws JSONException {
                    String data = Utils.getJsonData(result, "data");
                    save_All[0] = data;
                    if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                        getJsonData1(Utils.getJsonData(data, "items"));
                    } else {
                        mList.setAdapter(null);
                        Utils.setToast(getApplicationContext(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                    }
                }
            });
            mCount_All++;
    }

    private void getJsonData1(String result) {
        try {
            if (mSupportList == null)
                mSupportList = new ArrayList<Object>();
            mSupportList.removeAll(mSupportList);

            JSONArray jsonArray = new JSONArray(result);
            int count = jsonArray.length();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
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
                    projectVO.setIsHistory(isHistory);
                    if (isHistory.equals("Y")) {
                        int change = Utils.GetDifferenceOfDate(projectVO.getClose_time(), Utils.getTime());
                        if (change < 0) mSupportList.add(projectVO);
                    } else {
                        mSupportList.add(projectVO);

                    }
                }

                mProjectListAdapter = new ProjectListAdapter(this, mSupportList);
                mList.setAdapter(mProjectListAdapter);
                ProjectListAdapter.setListViewHeightBasedOnChildren(mList);
            } else {
                mList.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goProjectDetail(ProjectVO projectVO) {

        Intent intent = new Intent(ProjectAllActivity.this, ProjectDetailActivity.class);
        intent.putExtra("srl", projectVO.getProject_srl());
        intent.putExtra("isHistory", projectVO.getIsHistory());
        startActivity(intent);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.button_sub_popula: {

                    final String[] arrayString = {getString(R.string.popular), getString(R.string.new_sort), getString(R.string.deadline), getString(R.string.top_members)
                    };
                    ResultInterface resultInterface = new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            button_sub_popula.setText(arrayString[(int) resultValue]);
                            sortList = String.valueOf(((int) resultValue) + 1);
                            getNetworkProject();
                            return null;
                        }
                    };

                    FragmentManager fm = getSupportFragmentManager();
                    AlertDialogSort dialog = new AlertDialogSort(ProjectAllActivity.this, getString(R.string.sort_type), resultInterface, arrayString, button_sub_popula.getText().toString());
                    dialog.show(fm, "dialog");
                }
                break;
                case R.id.button_sub_all: {//연예인 선택
                    Intent intent = new Intent(ProjectAllActivity.this, SelectStarListActivity.class);
                    intent.putExtra("intent","PA");
                    startActivityForResult(intent, 0);
                }
                break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0) {
            select_star = data.getStringExtra("starIndex");
            button_sub_all.setText(data.getStringExtra("starName"));

            getNetworkProject();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
