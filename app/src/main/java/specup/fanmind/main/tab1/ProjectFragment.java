package specup.fanmind.main.tab1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.IntroActivity;
import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.ProjectListAdapter;
import specup.fanmind.common.Util.RecycleUtils;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.vo.ProjectVO;

public class ProjectFragment extends Fragment {

    ListView mList;
    ProjectListAdapter mProjectListAdapter;
    List<Object> mSupportList;

    Button button_all_support, btn_ongoing, btn_closed, btn_addSupport;
    ScrollView project_scrollView;

    public static String select_star = "";
    private String sortList = "1";
    private String isHistory = "";
    private View mView;
    private LinearLayout project_newArea;
    private TextView tv_noSupport;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 레이아웃에 fragment_project.xml을 담음
        Fresco.initialize(getContext());
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_project, null);
        }
        return mView;
        //return inflater.inflate(R.layout.fragment_project, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Utils.setAnalyticsTrackerScreenName(getActivity(), "Support project");
        }
        setView();
    }

    @Override
    public void onResume() {
        Log.d("ProjectF","onResume");
        super.onResume();
    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ongoing: {
                    getNetworkProject();
                }
                break;
                case R.id.btn_closed: {
                    getNetworkProject_Closde();
                    if(project_newArea.getVisibility() == View.VISIBLE){
                        project_newArea.setVisibility(View.GONE);
                    }
                }
                break;
                case R.id.btn_all_support: {
                    Intent intent = new Intent(getContext(), ProjectAllActivity.class);
                    startActivity(intent);
                }
            }
        }
    };


    private void setView() {
        if (mView == null) {
            mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_project, null);
        }
        project_newArea = (LinearLayout) getActivity().findViewById(R.id.project_newArea);
        project_scrollView = (ScrollView) mView.findViewById(R.id.scrollview);
        button_all_support = (Button) mView.findViewById(R.id.btn_all_support);
        btn_ongoing = (Button) mView.findViewById(R.id.btn_ongoing);
        btn_closed = (Button) mView.findViewById(R.id.btn_closed);
        button_all_support.setOnClickListener(onClickListener);
        btn_ongoing.setOnClickListener(onClickListener);
        btn_closed.setOnClickListener(onClickListener);

        select_star = StarSetting.getProjectSTAR_SELECT_INDEX(getActivity());

        //button_sub_all.setText(StarSetting.getProjectSTAR_SELECT_NAME(getActivity()).equals("") ? getString(R.string.starlist01) : StarSetting.getProjectSTAR_SELECT_NAME(getActivity()));
        mList = (ListView) getActivity().findViewById(R.id.list);
//        mList.setVerticalScrollBarEnabled(true);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectVO projectVO = (ProjectVO) parent.getAdapter().getItem(position);
                goProjectDetail(projectVO);
            }
        });

        getNetwork();
    }


    private void goProjectDetail(ProjectVO projectVO) {
        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
        intent.putExtra("srl", projectVO.getProject_srl());
        if(projectVO.getIsHistory()==null){
            projectVO.setIsHistory("Y");
        }
        intent.putExtra("isHistory", projectVO.getIsHistory());
        getActivity().startActivity(intent);
    }

    /*
    2017-01-26 brian park<brian.park@specupad.com>
    - Call : RightMenuFragement.java -> MainActivity.java(changeDefaultSrar) -> ProjectFragment.java(changeDefaultSrar)
    - Desc : 기본 설정된 스타의 정보를 가져온 후 API콜 날려서 리스트 갱신한다.
     */
    public void changeDefaultSrar(){
        setView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }

        if (requestCode == 0) {
            select_star = data.getStringExtra("starIndex");

            StarSetting.setProjectSTAR_SELECT_INDEX(getActivity(), select_star);
            StarSetting.setProjectSTAR_SELECT_NAME(getActivity(), data.getStringExtra("starName"));
            getNetworkProject();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*

     */
    public void getNetwork() {
        getNetworkProject();
    }

    /**
     * //스타 프로젝트 리스트
     * <p/>
     * * 정렬방식 (1 : 인기순, 2 : 최신순, 3 : 마감임박, 4 : 최다참여자, 5 : 최고 금액)
     */
    void getNetworkProject() {
        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(getActivity(), mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        mParams1.add(new BasicNameValuePair("star", select_star));
        mParams1.add(new BasicNameValuePair("order", sortList));
        if (isHistory.equals("Y")) {
            mParams1.add(new BasicNameValuePair("insight", "Y"));
            mParams1.add(new BasicNameValuePair("closed", "Y"));
        } else {
            mParams1.add(new BasicNameValuePair("insight", "N"));
        }


//        mParams1.add(new BasicNameValuePair("reviewed", isHistory));
        AsyncTask mAsyncTask = null;
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams1, URLAddress.PROJECT_ALL(), "Project", new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                String data = Utils.getJsonData(result, "data");
                Log.d("debug_choi",Utils.getJsonData(result, "code"));
                Log.d("debug_choi",data);
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getJsonData1(Utils.getJsonData(data, "items"));
                } else {
                    mList.setAdapter(null);
                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    void getNetworkProject_Closde() {
        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(getActivity(), mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        mParams1.add(new BasicNameValuePair("star", select_star));
        mParams1.add(new BasicNameValuePair("order", "2"));
        mParams1.add(new BasicNameValuePair("insight", "Y"));
        mParams1.add(new BasicNameValuePair("closed", "Y"));
        HttpRequest.setHttp1(getActivity(), URLAddress.PROJECT_ALL(), mParams1, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {

                String data = Utils.getJsonData(result, "data");
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getJsonData2(Utils.getJsonData(data, "items"));
                } else {
                    mList.setAdapter(null);
                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    private void getJsonData1(String result) {
        long startTime = System.currentTimeMillis();
        try {
            if (mSupportList == null)
                mSupportList = new ArrayList<Object>();

            JSONArray jsonArray = new JSONArray(result);
            int count = jsonArray.length();
            if (count > 0) {
                mSupportList.removeAll(mSupportList);
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
                project_newArea.setVisibility(View.GONE);
                mProjectListAdapter = new ProjectListAdapter(getActivity(), mSupportList);
                mList.setAdapter(mProjectListAdapter);
                ProjectListAdapter.setListViewHeightBasedOnChildren(mList);

            } else {
                Log.d("getJsonData1",mSupportList.size()+"");
                mSupportList = new ArrayList<Object>();
                mProjectListAdapter = new ProjectListAdapter(getActivity(), mSupportList);
                mList.setAdapter(mProjectListAdapter);
                ProjectListAdapter.setListViewHeightBasedOnChildren(mList);
                project_newArea.setVisibility(View.VISIBLE);
                setNewArea();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            Log.d("timecheck", "getJsonData1  :  " + (endTime - startTime) / 1000.0);
        }
    }

    private void getJsonData2(String result) {
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

                mProjectListAdapter = new ProjectListAdapter(getActivity(), mSupportList);
                mList.setAdapter(mProjectListAdapter);
                ProjectListAdapter.setListViewHeightBasedOnChildren(mList);
            } else {
                mList.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNewArea(){
        tv_noSupport = (TextView) getActivity().findViewById(R.id.noSupportText);
        String temp = getResources().getString(R.string.no_support,"test");
        String temp2 = temp.replace("celeb",MainActivity.mtitle_main.getText());
        tv_noSupport.setText(Html.fromHtml(temp2));

        btn_addSupport = (Button) getActivity().findViewById(R.id.btn_add_support);
        btn_addSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FanMindSetting.getLOGIN_OK(getActivity())) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fanmaum.com/projects/make")));

                } else {
                    Utils.showDialog(getActivity());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //구글 앱 로그 분석
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());

    }

    @Override
    public void onStop() {
        super.onStop();
        //구글 앱 로그 분석
        Log.d("ProjectF","onStop");
        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
    }

    @Override
    public void onDestroyView() {
        RecycleUtils.recursiveRecycle(getActivity().getWindow().getDecorView());
        super.onDestroyView();
        if (mView != null) {
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mView);
            }
        }
    }
}

