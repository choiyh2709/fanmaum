package specup.fanmind.main.tab0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

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
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.AttendsVO;
import specup.fanmind.vo.UserInformationVO;

/**
 * 함께하는 사람들
 */
public class ProjectDetailAssistUser extends Activity {

    JSONObject total_Json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_detail_assist_user);
        ActivityManager.getInstance().addActivity(this);

        setView();

    }

    private void setView() {

//서버 데이타 미비하여 전 화면에서 데이타 가져옴.
        // getNetworkData();


        try {
            JSONArray JsonArray_attends = new JSONArray(getIntent().getStringExtra("JsonObject_assists"));
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
                ListView listView = (ListView) findViewById(R.id.list);
                HomeFragment_detail_assistUserAdapter homeFragment_detail_assistUserAdapter = new HomeFragment_detail_assistUserAdapter(ProjectDetailAssistUser.this, listAttends);
                listView.setAdapter(homeFragment_detail_assistUserAdapter);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }

    private void getNetworkData() {
        final ListView listView = (ListView) findViewById(R.id.list);

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(ProjectDetailAssistUser.this, mParams);
        mParams.add(new BasicNameValuePair("next", "0"));
        mParams.add(new BasicNameValuePair("size", "100"));
        mParams.add(new BasicNameValuePair("project_srl", getIntent().getStringExtra("project_srl")));
        HttpRequest.setHttp1(ProjectDetailAssistUser.this, URLAddress.ASSIST_USER(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    List<Object> list_item = new ArrayList<Object>();
                    JSONArray jsonArray_items = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                    if (jsonArray_items.length() > 0) {
                        for (int i = 0; i < jsonArray_items.length(); i++) {
                            JSONObject jsonObject_temp = jsonArray_items.getJSONObject(i);
                            UserInformationVO userInformationVO = new UserInformationVO();
                            userInformationVO.setNick(jsonObject_temp.getString("nick"));
                            userInformationVO.setPic_base(jsonObject_temp.getString("pic_base"));
                            userInformationVO.setPic(jsonObject_temp.getString("pic"));
                            list_item.add(userInformationVO);
                        }

                        HomeFragment_detail_assistUserAdapter homeFragment_detail_assistUserAdapter = new HomeFragment_detail_assistUserAdapter(ProjectDetailAssistUser.this, list_item);
                        listView.setAdapter(homeFragment_detail_assistUserAdapter);
                    } else {
                        Utils.setToast(ProjectDetailAssistUser.this, R.string.no_data);
                    }


                } else {
                    Utils.setToast(ProjectDetailAssistUser.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });

    }

    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


}
