package specup.fanmind.left;

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
import specup.fanmind.adapter.Project_noticeAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.NoticeVO;

/**
 * 공지사항 (project)
 */
public class NoticeActivity2 extends Activity {

    JSONObject jsonObject_project_information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "Notice");
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_notice);

        try {
            jsonObject_project_information = new JSONObject(getIntent().getStringExtra("total_Json"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setView();

    }


    private void setView() {

        final ListView listView_notice = (ListView) findViewById(R.id.listView_notice);

        //project alram 가져오기기
       List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("project_srl", Utils.getJsonData(jsonObject_project_information.toString(), "project_srl")));
        mParams.add(new BasicNameValuePair("next", "0"));
        mParams.add(new BasicNameValuePair("size", "100"));

        HttpRequest.setHttp1(this, URLAddress.NOTICE_ALL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                String string_data = Utils.getJsonData(result, "data");
                String item = Utils.getJsonData(string_data, "items");

                JSONArray jsonArray_item = new JSONArray(item);

                if (jsonArray_item.length() > 0) {
                    List<Object> list = new ArrayList<Object>();
                    for (int i = 0; i < jsonArray_item.length(); i++) {
                        NoticeVO noticeVO = new NoticeVO();

                        JSONObject jsonObject_item = jsonArray_item.getJSONObject(i);
                        noticeVO.setNext_srl(jsonObject_item.getString("next_srl"));
                        noticeVO.setNotice_srl(jsonObject_item.getString("notice_srl"));
                        noticeVO.setSubject(jsonObject_item.getString("subject"));
                        noticeVO.setWritten_time(jsonObject_item.getString("written_time"));
                        noticeVO.setmEvent_srl(Utils.intToString(jsonArray_item.length() - i));

                        list.add(noticeVO);
                    }


                    Project_noticeAdapter project_noticeAdapter = new Project_noticeAdapter(NoticeActivity2.this, list);
                    listView_notice.setAdapter(project_noticeAdapter);
                }else{
                    Utils.setToast(NoticeActivity2.this, getString(R.string.no_data));
                }


            }
        });
        listView_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NoticeActivityDetail.class);
                intent.putExtra("project_srl", Utils.getJsonData(jsonObject_project_information.toString(), "project_srl"));
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);

            }
        });

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
