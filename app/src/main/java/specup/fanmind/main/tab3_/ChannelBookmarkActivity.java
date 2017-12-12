package specup.fanmind.main.tab3_;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import specup.fanmind.R;
import specup.fanmind.adapter.ChannelStarBookmarkAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.RecycleUtils;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.ChannelStarVo;

/**
 * 채널 즐겨찾기( 채널 =  연예인 )
 */
public class ChannelBookmarkActivity extends Activity implements TextWatcher {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_channel_bookmark);

        setView();
        getNetwork();
    }

    private void getNetwork() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("page", "0"));
        mParams.add(new BasicNameValuePair("size", "400"));
        mParams.add(new BasicNameValuePair("keyword", ""));

        HttpRequest.setHttp1(this, URLAddress.STAR_ALL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getNetwork2(Utils.getJsonData(result, "data"));
                } else {
                    Utils.setToast(ChannelBookmarkActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    private void getNetwork2(final String data1) {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);

        HttpRequest.setHttp1(this, URLAddress.MY_PAGE(ChannelBookmarkActivity.this), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {
                if (Utils.getJsonData(result, "errcode").equals("900")) {
                    setData(data1, Utils.getJsonData(result, "star"));
                } else {
                    Utils.setToast(ChannelBookmarkActivity.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    ChannelStarBookmarkAdapter channelStarBookmarkAdapter;
    List<Object> list_star;

    private void setData(String data1, String data) {
        list_star = new ArrayList<Object>();

        try {
            JSONArray jsonArray_star = null;
            if (!data.equals("")) {
                jsonArray_star = new JSONArray(data);
            }
            //즐겨찾기 스타
            JSONArray jsonArrayData = new JSONArray(Utils.getJsonData(data1, "items"));
            for (int i = 0; i < jsonArrayData.length(); i++) {

                ChannelStarVo channelStarVo = new ChannelStarVo();
                JSONObject json = jsonArrayData.getJSONObject(i);
                channelStarVo.setStar_srl(json.getString("star_srl"));
                channelStarVo.setName(json.getString("name"));
                channelStarVo.setIcon(json.getString("icon"));
                channelStarVo.setIcon_base(json.getString("icon_base"));
                channelStarVo.setFan_cnt(json.getString("fan_cnt"));
                channelStarVo.setIs_added(json.getString("is_added"));

                if (jsonArray_star != null)
                    for (int j = 0; j < jsonArray_star.length(); j++) {
                        JSONObject jsonObject = jsonArray_star.getJSONObject(j);
                        if (channelStarVo.getStar_srl().equals(jsonObject.getString("star_srl"))) {
                            channelStarVo.setIs_added("Y");
                            break;
                        }
                    }

                list_star.add(channelStarVo);
            }
            channelStarBookmarkAdapter = new ChannelStarBookmarkAdapter(this, list_star, onClick);

            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(channelStarBookmarkAdapter);
            listView.setTextFilterEnabled(true);
            editText_channel_name = (EditText) findViewById(R.id.editText_channel_name);
            editText_channel_name.addTextChangedListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ListView listView;
    EditText editText_channel_name;

    private void setView() {

    }


    OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_isBookmark: {// 즐겨찾기 , 해제하기 버튼

                    ChannelStarVo channelStarVo = null;
                    for (int i = 0; i < list_star.size(); i++) {
                        if (list_star.get(i).equals(v.getTag())) {
                            channelStarVo = (ChannelStarVo) v.getTag();
                            break;
                        }
                    }
                    if (channelStarVo.getIs_added().equals("Y")) {
                        channelStarVo.setIs_added("N");
                    } else {
                        channelStarVo.setIs_added("Y");
                    }
                    TreeSet<String> arraySet = new TreeSet<String>();
                    for (int i = 0; i < list_star.size(); i++) {
                        ChannelStarVo tempChannelStarVo = (ChannelStarVo) list_star.get(i);
                        if (tempChannelStarVo.getIs_added().equals("Y")) {
                            arraySet.add(tempChannelStarVo.getStar_srl());
                        }
                    }

                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                    mParams.add(new BasicNameValuePair("star", arraySet.toString().replace("[", "").replace("]", "")));

                    mParams = Utils.setSession(ChannelBookmarkActivity.this, mParams);
                    HttpRequest.setHttp1(ChannelBookmarkActivity.this, URLAddress.STAR_MODIFY(ChannelBookmarkActivity.this), mParams, new OnTask() {
                                @Override
                                public void onTask(int output, String result) throws JSONException {
                                    if (Utils.getJsonData(result, "errcode").equals("900")) {
                                        channelStarBookmarkAdapter.notifyDataSetChanged();
                                    } else if (Utils.getJsonData(result, "errcode").equals("950")) {
                                        Utils.setToast(ChannelBookmarkActivity.this, getString(R.string.bookmark_one) + " ( " + Utils.getJsonData(result, "errcode") + " )");
                                    }
                                }
                            }
                    );
                }
                break;

            }
        }
    };

    public void onBack(View v) {
        onBackPressed();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().deleteActivity(this);
        super.onBackPressed();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText_channel_name.getText().length() == 0) {
            channelStarBookmarkAdapter.reset();

        } else {
            Filter itemFilter = channelStarBookmarkAdapter.getFilter();
            itemFilter.filter(s);
        }
    }

    @Override
    protected void onPause() {
        listView=null;
        editText_channel_name=null;
        list_star=null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
        super.onDestroy();
    }
}
