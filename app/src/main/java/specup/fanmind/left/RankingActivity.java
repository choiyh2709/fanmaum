package specup.fanmind.left;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.RankingAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.RankingVO;

/**
 * 랭킹 조회
 */
public class RankingActivity extends Activity {
    RadioButton radiobutton_projectRanking;
    RadioButton radiobutton_userRanking;
    ListView listView;
    RankingAdapter rankingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setAnalyticsTrackerScreenName(this, "Notice");
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_ranking);
        super.onCreate(savedInstanceState);
        setView();
        setNetwork1();

    }

    private void setView() {

        radiobutton_projectRanking = (RadioButton) findViewById(R.id.radiobutton_projectRanking);
        radiobutton_userRanking = (RadioButton) findViewById(R.id.radiobutton_userRanking);

        radiobutton_projectRanking.setOnClickListener(onClickListener);
        radiobutton_userRanking.setOnClickListener(onClickListener);

        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
    }


    private void setNetwork1() {
        listView.setAdapter(null);
        rankingAdapter = null;
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(RankingActivity.this, mParams);
        HttpRequest.setHttp1(RankingActivity.this, URLAddress.PROJECT_RANKING(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    List<Object> list_item = new ArrayList<Object>();
                    JSONArray jsonArray_items = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "rankings"));
                    if (jsonArray_items.length() > 0) {
                        for (int i = 0; i < jsonArray_items.length(); i++) {
                            JSONObject jsonObject_temp = jsonArray_items.getJSONObject(i);
                            RankingVO rankingVO = new RankingVO();
                            rankingVO.setMember_srl(jsonObject_temp.getString("member_srl"));
                            rankingVO.setNick(jsonObject_temp.getString("name"));
                            rankingVO.setPic(jsonObject_temp.getString("profile"));
                            rankingVO.setPic_base(jsonObject_temp.getString("profile_base"));
                            rankingVO.setRank(jsonObject_temp.getString("rank"));
                            rankingVO.setHeart(jsonObject_temp.getString("heart"));
                            //마음내역 입력

                            list_item.add(rankingVO);
                        }

                        rankingAdapter = new RankingAdapter(RankingActivity.this, list_item);
                        listView.setAdapter(rankingAdapter);
                    } else {
                        Utils.setToast(RankingActivity.this, R.string.no_data);
                    }


                } else {
                    Utils.setToast(RankingActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    private void setNetwork2() {
        listView.setAdapter(null);
        rankingAdapter = null;
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(RankingActivity.this, mParams);
        HttpRequest.setHttp1(RankingActivity.this, URLAddress.USER_RANKING(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    List<Object> list_item = new ArrayList<Object>();
                    JSONArray jsonArray_items = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "rankings"));
                    if (jsonArray_items.length() > 0) {
                        for (int i = 0; i < jsonArray_items.length(); i++) {
                            JSONObject jsonObject_temp = jsonArray_items.getJSONObject(i);
                            RankingVO rankingVO = new RankingVO();
                            rankingVO.setMember_srl(jsonObject_temp.getString("member_srl"));
                            rankingVO.setNick(jsonObject_temp.getString("nick"));
                            rankingVO.setPic(jsonObject_temp.getString("pic"));
                            rankingVO.setPic_base(jsonObject_temp.getString("pic_base"));
                            rankingVO.setRank(jsonObject_temp.getString("rank"));
                            rankingVO.setHeart(jsonObject_temp.getString("heart"));
                            list_item.add(rankingVO);
                        }

                        rankingAdapter = new RankingAdapter(RankingActivity.this, list_item);
                        listView.setAdapter(rankingAdapter);
                    } else {
                        Utils.setToast(RankingActivity.this, R.string.no_data);
                    }


                } else {
                    Utils.setToast(RankingActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.radiobutton_projectRanking: {
                    radiobutton_projectRanking.setChecked(true);
                    setNetwork1();
                    break;
                }
                case R.id.radiobutton_userRanking: {
                    setNetwork2();
                    radiobutton_userRanking.setChecked(true);
                    break;
                }
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

}
