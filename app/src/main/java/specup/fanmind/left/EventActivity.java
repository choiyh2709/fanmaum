package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.EventAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.EventVO;

public class EventActivity extends Activity implements OnTask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.fragment_event);
        setView();

        Utils.setAnalyticsTrackerScreenName(this, "Event");

        isNoti = getIntent().getBooleanExtra("isNoti", false);
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("page ", "0"));//임의값
        mParams.add(new BasicNameValuePair("size", "500"));//임의값
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.EVENT_LIST2(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                String data = Utils.getJsonData(result, "data");
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getJsonData(data);
                } else {
                    Utils.setToast(EventActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }

            }
        });
    }


    List<NameValuePair> mParams;
    AsyncTask mAsyncTask;
    ArrayList<Object> mEvent;
    EventAdapter mEventAdapter;
    ListView mListView;
    boolean isNoti;

    private void setView() {
        mListView = (ListView) findViewById(R.id.event_list);
        mEvent = new ArrayList<Object>();
        mEventAdapter = new EventAdapter(this, mEvent);
        mListView.setAdapter(mEventAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                boolean isEnd = false;
                String end = ((EventVO)mEvent.get(position)).getClose_time();
                String sToday = Utils.getTime();
                int change = Utils.GetDifferenceOfDate(end, sToday);
                if (change < 0) {
                    isEnd = true;
                }
                Intent intent = new Intent(EventActivity.this, EventDetailActivity.class);
                intent.putExtra("EventVO", (EventVO)mEvent.get(position));
                intent.putExtra("srl", ( (EventVO)mEvent.get(position)).getEvent_srl());
				intent.putExtra("end", isEnd);
				intent.putExtra("support_srl", ((EventVO)mEvent.get(position)).getProject_srl());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.EVENT_LIST_NUM) {
            if (Utils.getJsonData(result)) {
                getJsonData(result);
            }
        }
    }

    private void getJsonData(String result) {
        try {

            JSONObject json = new JSONObject(result);
            String list = json.getString("items");
            mEvent.clear();
            JSONArray jsonArray = new JSONArray(list);
            for (int i = 0; i < jsonArray.length(); i++) {
                EventVO eventVO = new EventVO();
                eventVO.setEvent_srl(jsonArray.getJSONObject(i).getString("event_srl"));
                eventVO.setStar_srl(jsonArray.getJSONObject(i).getString("star_srl"));
                eventVO.setProject_srl( jsonArray.getJSONObject(i).getString("project_srl"));
                eventVO.setThumbnail(jsonArray.getJSONObject(i).getString("thumbnail"));
                eventVO.setThumbnail_base(jsonArray.getJSONObject(i).getString("thumbnail_base"));
                eventVO.setTitle(jsonArray.getJSONObject(i).getString("title"));
                eventVO.setPresent(jsonArray.getJSONObject(i).getString("present"));
                eventVO.setBegin_time(jsonArray.getJSONObject(i).getString("begin_time"));
                eventVO.setClose_time(jsonArray.getJSONObject(i).getString("close_time"));

                mEvent.add(eventVO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEventAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack(View v) {
        onBackPress();
    }

    private void onBackPress() {
        if (isNoti) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        ActivityManager.getInstance().deleteActivity(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

}
