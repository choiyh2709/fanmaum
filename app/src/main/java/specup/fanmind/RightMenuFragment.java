package specup.fanmind;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import specup.fanmind.adapter.ChannelStarBookmarkAdapter;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.ChannelStarVo;

public class RightMenuFragment extends Fragment implements TextWatcher {

    Context context;
    ChannelStarBookmarkAdapter channelStarBookmarkAdapter, channelStarBookmarkAdapter2;
    EditText editText_channel_name;
    ScrollView scrollView;
    private static String mName;
    private static String mIndex;
    private TextView tv_follow;
    OnApplySelectedListener mListener;
    //private int total_count, want_count, total_page_size, now_page;

    private int total_page_count, per_page, now_page, total_item_count;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnApplySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }

    }

    @Override
    public void onStart() {
        getNetwork();
        super.onStart();
    }

    public static RightMenuFragment newInstance(String param1, String param2) {
        RightMenuFragment fragment = new RightMenuFragment();
        Bundle args = new Bundle();
        args.putString(mName, param1);
        args.putString(mIndex, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        return inflater.inflate(R.layout.fragment_right, null);
    }

    private void getNetwork() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();


        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("page", "0"));
        mParams.add(new BasicNameValuePair("size", "400"));
        mParams.add(new BasicNameValuePair("keyword", ""));

        HttpRequest.setHttp1(getActivity(), URLAddress.STAR_ALL(), mParams, new OnTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    FanMindSetting.setStarList(context, Utils.getJsonData(result, "data"));
                    setData(FanMindSetting.getStarList(context));

                } else {
                    Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }

    private List<Object> list_star, list_all_star, sub_list;
    ListView celeb_book_list, celeb_book_list_all;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setData(String data) {
        list_star = new ArrayList<Object>();
        list_all_star = new ArrayList<Object>();
        try {
            //즐겨찾기 스타
            JSONArray jsonArrayData = new JSONArray(Utils.getJsonData(data, "items"));
            int count = jsonArrayData.length();
            for (int i = 0; i < count; i++) {

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
                list_all_star.add(channelStarVo);
            }
            initPage();
            channelStarBookmarkAdapter2 = new ChannelStarBookmarkAdapter(context, list_star, onClick2, false, null);
            celeb_book_list = (ListView) getActivity().findViewById(R.id.celeb_book_list);
            celeb_book_list.setAdapter(channelStarBookmarkAdapter2);
            channelStarBookmarkAdapter2.setListViewHeightBasedOnChildren(celeb_book_list);

            sub_list = list_all_star.subList(now_page*per_page,(now_page+1)*per_page);
            channelStarBookmarkAdapter = new ChannelStarBookmarkAdapter(context, list_all_star, onClick, true, sub_list);
            celeb_book_list_all = (ListView) getActivity().findViewById(R.id.celeb_book_list_all);
            celeb_book_list_all.setAdapter(channelStarBookmarkAdapter);
            channelStarBookmarkAdapter.setListViewHeightBasedOnChildren(celeb_book_list_all);
            celeb_book_list_all.setTextFilterEnabled(true);
            scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollViewPs = scrollView.getScrollY();
                    int scrollViewheight = scrollView.getChildAt(0).getBottom() - scrollView.getHeight();
                    if (scrollViewheight == scrollViewPs) {
                        if (now_page < 0) {
                            now_page = 0;
                        } else if (now_page >= total_page_count) {
                            //now_page = total_page_count;
                            return;
                        } else if (now_page < total_page_count) {
                            now_page++;
                        }
                        // 이미 1페이지
                        // (1 * 10) + 1 = 11
                        int startCount = (now_page*per_page)+1;
                        // (1 + 1) * 10 = 20
                        int endCount = (now_page+1)*per_page;

                        if (endCount > total_item_count) {
                            endCount = total_item_count;
                        }

                        channelStarBookmarkAdapter.addSubList(startCount,endCount);

                        channelStarBookmarkAdapter.setListViewHeightBasedOnChildren(celeb_book_list_all);
                    }
                }
            });
            editText_channel_name = (EditText) getActivity().findViewById(R.id.editText_channel_name);
            tv_follow = (TextView) getActivity().findViewById(R.id.tv_follow);
            editText_channel_name.addTextChangedListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_isBookmark: {// 즐겨찾기 , 해제하기 버튼
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        ChannelStarVo channelStarVo = null;
                        try {
                            for (int i = 0; i < list_all_star.size(); i++) {
                                if (list_all_star.get(i).equals(v.getTag())) {
                                    channelStarVo = (ChannelStarVo) v.getTag();
                                    break;
                                }
                            }
                            if (channelStarVo.getIs_added().equals("Y")) {
                                channelStarVo.setIs_added("N");
                                list_star.remove(channelStarVo);
                            } else {
                                channelStarVo.setIs_added("Y");
                                list_star.add(channelStarVo);
                            }
                            TreeSet<String> arraySet = new TreeSet<String>();
                            for (int i = 0; i < list_all_star.size(); i++) {
                                ChannelStarVo tempChannelStarVo = (ChannelStarVo) list_all_star.get(i);
                                if (tempChannelStarVo.getIs_added().equals("Y")) {
                                    arraySet.add(tempChannelStarVo.getStar_srl());
                                }
                            }

                            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                            mParams.add(new BasicNameValuePair("star", arraySet.toString().replace("[", "").replace("]", "")));

                            mParams = Utils.setSession(context, mParams);
                            HttpRequest.setHttp1(context, URLAddress.STAR_MODIFY(context), mParams, new OnTask() {
                                        @Override
                                        public void onTask(int output, String result) throws JSONException {
                                            if (Utils.getJsonData(result, "errcode").equals("900")) {
                                                channelStarBookmarkAdapter.notifyDataSetChanged();
                                                channelStarBookmarkAdapter2.notifyDataSetChanged();
                                                channelStarBookmarkAdapter2.setListViewHeightBasedOnChildren(celeb_book_list);
                                            } else if (Utils.getJsonData(result, "errcode").equals("950")) {
                                                Utils.setToast(context, getString(R.string.bookmark_one) + " ( " + Utils.getJsonData(result, "errcode") + " )");
                                            }
                                        }
                                    }
                            );
                        } catch (Exception e) {
                        }
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
                }
                default: {
                    ChannelStarVo channelStarVo = null;
                    try {
                        for (int i = 0; i < list_all_star.size(); i++) {
                            if (list_all_star.get(i).equals(v.getTag())) {
                                channelStarVo = (ChannelStarVo) v.getTag();
                                mName = channelStarVo.getName();
                                mIndex = channelStarVo.getStar_srl();

                                StarSetting.setProjectSTAR_SELECT_INDEX(context, mIndex);
                                StarSetting.setSTAR_SELECT_NAME(context, mName);
                                StarSetting.setNewsfeedSTAR_SELECT_INDEX(context, mIndex);

                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText_channel_name.getWindowToken(), 0);
                                mListener.changeDefaultSrar("");
                                editText_channel_name.setText("");
                                MainActivity.mtitle_main.setText(mName);
                                getActivity().onBackPressed();

                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    View.OnClickListener onClick2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_isBookmark: {// 즐겨찾기 , 해제하기 버튼

                    if (list_star != null) {
                        ChannelStarVo channelStarVo = null;
                        int count = list_star.size();
                        for (int i = 0; i < count; i++) {
                            if (list_star.get(i).equals(v.getTag())) {
                                channelStarVo = (ChannelStarVo) v.getTag();
                                break;
                            }
                        }
                        if (channelStarVo.getIs_added().equals("Y")) {
                            channelStarVo.setIs_added("N");
                            list_star.remove(channelStarVo);
                        } else {
                        }
                        TreeSet<String> arraySet = new TreeSet<String>();
                        try {
                            for (int i = 0; i < count; i++) {
                                ChannelStarVo tempChannelStarVo = (ChannelStarVo) list_star.get(i);
                                if (tempChannelStarVo.getIs_added().equals("Y")) {
                                    arraySet.add(tempChannelStarVo.getStar_srl());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                        mParams.add(new BasicNameValuePair("star", arraySet.toString().replace("[", "").replace("]", "")));

                        mParams = Utils.setSession(getActivity(), mParams);
                        HttpRequest.setHttp1(getActivity(), URLAddress.STAR_MODIFY(getActivity()), mParams, new OnTask() {
                                    @Override
                                    public void onTask(int output, String result) throws JSONException {
                                        if (Utils.getJsonData(result, "errcode").equals("900")) {
                                            channelStarBookmarkAdapter.notifyDataSetChanged();
                                            channelStarBookmarkAdapter2.notifyDataSetChanged();
                                            channelStarBookmarkAdapter2.setListViewHeightBasedOnChildren(celeb_book_list);
                                        } else if (Utils.getJsonData(result, "errcode").equals("950")) {
                                            Utils.setToast(getActivity(), getString(R.string.bookmark_one) + " ( " + Utils.getJsonData(result, "errcode") + " )");
                                        }
                                    }
                                }
                        );
                    }
                    break;
                }
                default: {
                    ChannelStarVo channelStarVo = null;
                    try {
                        for (int i = 0; i < list_all_star.size(); i++) {
                            if (list_all_star.get(i).equals(v.getTag())) {
                                channelStarVo = (ChannelStarVo) v.getTag();
                                mName = channelStarVo.getName();
                                mIndex = channelStarVo.getStar_srl();

                                StarSetting.setProjectSTAR_SELECT_INDEX(context, mIndex);
                                StarSetting.setSTAR_SELECT_NAME(context, mName);
                                StarSetting.setNewsfeedSTAR_SELECT_INDEX(context, mIndex);

                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText_channel_name.getWindowToken(), 0);
                                mListener.changeDefaultSrar("");
                                editText_channel_name.setText("");
                                MainActivity.mtitle_main.setText(mName);
                                getActivity().onBackPressed();

                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText_channel_name.getText().length() == 0) {
            scrollView.setScrollY(0);
            channelStarBookmarkAdapter.reset();
            channelStarBookmarkAdapter.setListViewHeightBasedOnChildren(celeb_book_list_all);
        } else {
            int height = celeb_book_list.getHeight();
            int height2 = tv_follow.getHeight();
            int total_height = height + height2;
            scrollView.setScrollY(total_height);
            Filter itemFilter = channelStarBookmarkAdapter.getFilter();
            itemFilter.filter(s);
        }
    }

    public interface OnApplySelectedListener {
        public void changeDefaultSrar(String event);
    }

    private void initPage() {
        per_page = 10;
        now_page = 0;
        total_item_count = list_all_star.size();
        total_page_count = (int) Math.ceil(total_item_count / per_page);
    }
}

