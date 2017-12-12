package specup.fanmind.main.tab3_;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.ChannelStarListAdapter;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.ChannelStarVo;

/**
 * 연예인
 */
public class ChannelFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channel, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();

    }

    @Override
    public void onResume() {
        getNetwork();
        super.onResume();
    }

    TextView textView;
    GridView gridLayout;

    private void setView() {
        textView = (TextView) getActivity().findViewById(R.id.textView_totalChannel);
        textView.setText(Html.fromHtml(getString(R.string.bookmark_channel)));

        gridLayout = (GridView) getActivity().findViewById(R.id.gridView_channelList);
    }


    private void getNetwork() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("page", "0"));
        mParams.add(new BasicNameValuePair("size", "400"));
        mParams.add(new BasicNameValuePair("keyword", ""));

        HttpRequest.setHttp1(getActivity(), URLAddress.STAR_ALL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    setData(Utils.getJsonData(result, "data"));


                    //                    setData(Utils.getJsonData(result, "data"));
                } else {
                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });


    }

    List<Object> list_star;

    private void setData(String data) {
        list_star = new ArrayList<Object>();
        try {
            //채널 더 찾기 아이콘 삽입
            ChannelStarVo channelPlus = new ChannelStarVo();
            channelPlus.setName(getString(R.string.channel_plus));
            list_star.add(channelPlus);

            //즐겨찾기 스타
            JSONArray jsonArrayData = new JSONArray(Utils.getJsonData(data, "items"));
            for (int i = 0; i < jsonArrayData.length(); i++) {

                ChannelStarVo channelStarVo = new ChannelStarVo();
                JSONObject json = jsonArrayData.getJSONObject(i);
                channelStarVo.setStar_srl(json.getString("star_srl"));
                channelStarVo.setName(json.getString("name"));
                channelStarVo.setIcon(json.getString("icon"));
                channelStarVo.setIcon_base(json.getString("icon_base"));
                channelStarVo.setFan_cnt(json.getString("fan_cnt"));
                channelStarVo.setIs_added(json.getString("is_added"));

                if (json.getString("is_added").equals("Y")) {
                    list_star.add(channelStarVo);
                }


            }
            ChannelStarListAdapter channelStarListAdapter = new ChannelStarListAdapter(getActivity(), list_star);
            gridLayout.setAdapter(channelStarListAdapter);
            textView.setText(Html.fromHtml(getString(R.string.bookmark_channel).replace("{VALUE}", String.valueOf(list_star.size()-1))));
            gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {

                        if (FanMindSetting.getLOGIN_OK(getActivity())) {
                            getActivity().startActivity(new Intent(getActivity().getApplicationContext(), ChannelBookmarkActivity.class));
                        } else {
                            Utils.showDialog(getActivity());
                        }

                    } else {
                        ChannelStarVo channelStarVo = (ChannelStarVo) list_star.get(position);
                        Intent intent = new Intent(getActivity(), ChannelBookStarInfomation.class);
                        intent.putExtra("ChannelStarVo", channelStarVo);
                        startActivity(intent);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.timeline_btn01) {
            }
        }
    };


}
