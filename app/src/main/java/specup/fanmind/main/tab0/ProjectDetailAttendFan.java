package specup.fanmind.main.tab0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.HomeFragment_detail_attendFanAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.UserInformationVO;

/**
 * 참여한 팬
 */
public class ProjectDetailAttendFan extends Activity {

    JSONObject total_Json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_detail_attend_fan);
        ActivityManager.getInstance().addActivity(this);
        setView();

    }

    private void setView() {
        RadioButton button_item0 = (RadioButton) findViewById(R.id.button_item0);
        RadioButton button_item1 = (RadioButton) findViewById(R.id.button_item1);
        button_item0.setOnClickListener(onBottomButtonClick);
        button_item1.setOnClickListener(onBottomButtonClick);
        getNetworkData("1");
    }

    View.OnClickListener onBottomButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_item0: {
                    getNetworkData("1");
                }
                break;
                case R.id.button_item1: {
                    getNetworkData("2");

                }
            }
        }
    };


    private void getNetworkData(String sortType) {
        final ListView listView = (ListView) findViewById(R.id.list);

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(ProjectDetailAttendFan.this, mParams);
        mParams.add(new BasicNameValuePair("next", "0"));
        mParams.add(new BasicNameValuePair("size", "100"));
        mParams.add(new BasicNameValuePair("project_srl", getIntent().getStringExtra("project_srl")));
        mParams.add(new BasicNameValuePair("order", sortType));//정렬 (1 : 최신순, 2 : 최대적립순)
        HttpRequest.setHttp1(ProjectDetailAttendFan.this, URLAddress.ATTENDS_FAN(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    List<Object> list_item = new ArrayList<Object>();
                    JSONArray jsonArray_items = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "items"));
                    if (jsonArray_items.length() > 0) {
                        for (int i = 0; i < jsonArray_items.length(); i++) {
                            JSONObject jsonObject_temp = jsonArray_items.getJSONObject(i);
                            UserInformationVO userInformationVO = new UserInformationVO();
                            userInformationVO.setMember_srl(jsonObject_temp.getString("member_srl"));
                            userInformationVO.setNick(jsonObject_temp.getString("nick"));
                            userInformationVO.setPic(jsonObject_temp.getString("pic"));
                            userInformationVO.setPic_base(jsonObject_temp.getString("pic_base"));
                            userInformationVO.setHeart(jsonObject_temp.getString("heart"));
                            userInformationVO.setTime(jsonObject_temp.getString("time"));
                            list_item.add(userInformationVO);
                        }

                        HomeFragment_detail_attendFanAdapter homeFragment_detail_attendFanAdapter = new HomeFragment_detail_attendFanAdapter(ProjectDetailAttendFan.this, list_item);
                        listView.setAdapter(homeFragment_detail_attendFanAdapter);
                    } else {
                        Utils.setToast(ProjectDetailAttendFan.this, R.string.no_data);
                    }


                } else {
                    Utils.setToast(ProjectDetailAttendFan.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });

    }

    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


}
