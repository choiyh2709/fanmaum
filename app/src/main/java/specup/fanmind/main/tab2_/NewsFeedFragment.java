package specup.fanmind.main.tab2_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.MyRecyclerAdapter;
import specup.fanmind.adapter.Newsfeed_AllAdapter;
import specup.fanmind.common.Util.EndlessRecyclerOnScrollListener;
import specup.fanmind.common.Util.RecycleUtils;
import specup.fanmind.common.Util.RecyclerItemClickListener;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.main.tab1.AlertDialogSort;
import specup.fanmind.main.tab1.SelectStarListActivity;
import specup.fanmind.vo.NewsfeedVO;

/**
 * 뉴스피드 메인
 */
public class NewsFeedFragment extends Fragment {


    private View mView;
    private Button newsfeed_write;
    private String starindex = "";
    private String sortValue = "2";//최신순
    //private ScrollView scroll_gridView_post;
    private RecyclerView gridView_post;

    //private int beforeScrollBottom = 0;
    public static Newsfeed_AllAdapter newsfeed_AllAdapter;
    public static MyRecyclerAdapter myRecyclerAdapter;
    public static List<Object> newsfeedList;
    private int page = 0;


    private Button button_sub_all, button_sub_popula;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_newsfeed, null);
        }
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(getActivity(), "NewsFeed");

        setView();
    }

    private void setView() {
        if (mView == null) {
            //getLayoutInflater를 통해 fragment_newsfeed 레이아웃을 담음
            mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_newsfeed, null);
        }

        button_sub_popula = (Button) mView.findViewById(R.id.button_sub_popula);
        button_sub_all = (Button) mView.findViewById(R.id.button_sub_all);
        button_sub_popula.setOnClickListener(onClickListener);
        button_sub_all.setText(StarSetting.getSTAR_SELECT_NAME(getContext()));
        button_sub_all.setOnClickListener(onClickListener);

        //선택되어진 연예인 인덱스
        starindex = StarSetting.getNewsfeedSTAR_SELECT_INDEX(getActivity());

        gridView_post = (RecyclerView) getActivity().findViewById(R.id.gridview_project);
        gridView_post.setAdapter(new MyRecyclerAdapter(getActivity().getApplicationContext(),newsfeedList,R.layout.list_newsfeed_all));
        gridView_post.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));
        gridView_post.setItemAnimator(new DefaultItemAnimator());
        gridView_post.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), gridView_post ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        NewsfeedVO newsfeedVO = (NewsfeedVO) myRecyclerAdapter.getItem(position);
                        Intent newsfeed = new Intent(getActivity().getApplicationContext(), NewsFeedDetailActivity.class);

                        newsfeed.putExtra("newsfeedVO", (Parcelable) newsfeedVO);

                        startActivityForResult(newsfeed, Utils.NEWSFEED_DETAIL);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        gridView_post.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                Log.d("onLoadMore",currentPage+"");
                page++;
                getNetwork3();
            }
        });
        newsfeed_write = (Button) getActivity().findViewById(R.id.newsfeed_write);
        newsfeed_write.setOnClickListener(onClickListener);

        getNetwork3();
    }


    private void initList() {
        page = 0;
        //beforeScrollBottom = 0;
        if (myRecyclerAdapter != null) {
            if(newsfeedList != null) {
                newsfeedList.clear();
            }
            myRecyclerAdapter.notifyDataSetChanged();
            myRecyclerAdapter = null;
        }
    }

    /*
    2017-01-26 brian park<brian.park@specupad.com>
    - Call : RightMenuFragement.java -> MainActivity.java(changeDefaultSrar) -> NewsFeedFragment.java(changeDefaultSrar)
    - Desc : 기본 설정된 스타의 정보를 가져온 후 API콜 날려서 리스트 갱신한다.
     */
    public void changeDefaultSrar(){
        initList();
        setView();
    }

    /**
     * 인기 프로젝트 리스트
     */
/*    private void getNetwork() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);

        HttpRequest.setHttp1(getActivity(), URLAddress.NEWSFEED_TOP10(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                            setTop10Data(Utils.getJsonData(result, "data"));
                        } else {
                            Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }

                    }
                }
        );
    }*/

    //HorizontalListView listView_popular_list;

    /*private void setTop10Data(String data) {
        try {
            List<Object> newsfeedList = new ArrayList<Object>();

            JSONArray jsonArray = new JSONArray(data);
            int count = jsonArray.length();

            for (int i = 0; i < count; i++) {
                JSONObject itemJSONObject = jsonArray.getJSONObject(i);

                NewsfeedVO newsfeedVO = new NewsfeedVO();
                newsfeedVO.setNewsfeed_srl(itemJSONObject.getString("newsfeed_srl"));
                newsfeedVO.setTitle(itemJSONObject.getString("title"));
                newsfeedVO.setText(itemJSONObject.getString("text"));
                newsfeedVO.setLikes(itemJSONObject.getString("likes"));
                newsfeedVO.setReply_cnt(itemJSONObject.getString("reply_cnt"));
                newsfeedVO.setTime(itemJSONObject.getString("time"));
                newsfeedVO.setAlarm(itemJSONObject.getString("alarm"));
                newsfeedVO.setMember_srl(itemJSONObject.getString("member_srl"));
                newsfeedVO.setNick(itemJSONObject.getString("nick"));
                newsfeedVO.setPic(itemJSONObject.getString("pic"));
                newsfeedVO.setPic_base(itemJSONObject.getString("pic_base"));
                newsfeedVO.setStar_srl(itemJSONObject.getString("star_srl"));
                newsfeedVO.setName(itemJSONObject.getString("name"));
                newsfeedVO.setIs_liked(itemJSONObject.getString("is_liked"));

                //image
                JSONArray imageArrayData = new JSONArray(itemJSONObject.getString("images"));
                int imageLength = imageArrayData.length();
                String[] stringArrya = new String[imageLength];
                for (int i1 = 0; i1 < imageLength; i1++) {
                    JSONObject jsonObject_imageUrl = imageArrayData.getJSONObject(i1);

                    stringArrya[i1] = jsonObject_imageUrl.getString("img_base") + jsonObject_imageUrl.getString("img");
                }
                newsfeedVO.setImages(stringArrya);


                newsfeedList.add(newsfeedVO);


                Newsfeed_top10Adapter newsfeed_top10Adapter = new Newsfeed_top10Adapter(getActivity(), newsfeedList);
                //listView_popular_list.setAdapter(newsfeed_top10Adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }*/

    /**
     * 뉴스피드 목록
     * <p/>
     * 연예인 고유번호(NULL : 전체 연예인, 0 : 내가 즐겨찾기한 연예인)
     * 정렬방식 (1 : 추천순, 2 : 최신순, 3 : 댓글순, 4 : 좋아요순)
     */
    private void getNetwork3() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(getActivity(), mParams);
        mParams.add(new BasicNameValuePair("page", String.valueOf(page)));
        mParams.add(new BasicNameValuePair("size", "19"));
        mParams.add(new BasicNameValuePair("star", starindex));
        mParams.add(new BasicNameValuePair("order", sortValue));


        HttpRequest.setHttp1(getActivity(), URLAddress.NEWSFEED_ALL(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                            setProjectAll(Utils.getJsonData(result, "data"));
                        } else {
                            Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == 0) {
            starindex = data.getStringExtra("starIndex");
            button_sub_all.setText(data.getStringExtra("starName"));

            StarSetting.setNewsfeedSTAR_SELECT_INDEX(getActivity(), starindex);
            StarSetting.setNewsfeedSTAR_SELECT_NAME(getActivity(), data.getStringExtra("starName"));

            initList();
            getNetwork3();
        } else if (requestCode == Utils.NEWSFEED_WRITE) {

            starindex = StarSetting.getNewsfeedSTAR_SELECT_INDEX(getActivity());
            button_sub_all.setText(StarSetting.getNewsfeedSTAR_SELECT_NAME(getActivity()));
            initList();
            getNetwork3();
        } else if (requestCode == Utils.NEWSFEED_DETAIL) {
            initList();
            getNetwork3();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setProjectAll(String data) {
        try {
            newsfeedList = new ArrayList<Object>();

            JSONArray jsonArray = new JSONArray(Utils.getJsonData(data, "items"));

            if (jsonArray.length() > 0) {
                int count = jsonArray.length();
                for (int i = 0; i < count; i++) {
                    JSONObject itemJSONObject = jsonArray.getJSONObject(i);

                    NewsfeedVO newsfeedVO = new NewsfeedVO();

                    newsfeedVO.setNewsfeed_srl(itemJSONObject.getString("newsfeed_srl"));
                    newsfeedVO.setTitle(itemJSONObject.getString("title"));
                    newsfeedVO.setText(itemJSONObject.getString("text"));
                    newsfeedVO.setLikes(itemJSONObject.getString("likes"));
                    newsfeedVO.setReply_cnt(itemJSONObject.getString("reply_cnt"));
                    newsfeedVO.setTime(itemJSONObject.getString("time"));
                    newsfeedVO.setAlarm(itemJSONObject.getString("alarm"));
                    newsfeedVO.setMember_srl(itemJSONObject.getString("member_srl"));
                    newsfeedVO.setNick(itemJSONObject.getString("nick"));
                    newsfeedVO.setPic(itemJSONObject.getString("pic"));
                    newsfeedVO.setPic_base(itemJSONObject.getString("pic_base"));
                    newsfeedVO.setStar_srl(itemJSONObject.getString("star_srl"));
                    newsfeedVO.setName(itemJSONObject.getString("name"));
                    newsfeedVO.setIs_liked(itemJSONObject.getString("is_liked"));

                    //image
                    JSONArray imageArrayData = new JSONArray(itemJSONObject.getString("images"));
                    int imageLength = imageArrayData.length();
                    String[] stringArrya = new String[imageLength];
                    for (int i1 = 0; i1 < imageLength; i1++) {
                        JSONObject jsonObject_imageUrl = imageArrayData.getJSONObject(i1);

                        stringArrya[i1] = jsonObject_imageUrl.getString("img_base") + jsonObject_imageUrl.getString("img");
                    }
                    newsfeedVO.setImages(stringArrya);
                    newsfeedList.add(newsfeedVO);
                }
                if (myRecyclerAdapter == null) {
                    myRecyclerAdapter = new MyRecyclerAdapter(getActivity(), newsfeedList,R.layout.list_newsfeed_all);
                    gridView_post.setAdapter(myRecyclerAdapter);

                } else {
                    myRecyclerAdapter.add(newsfeedList);
                }
               /* gridView_project.setOnItemClickListener(onItemClickListener);
                gridView_project.setExpanded(true);*/
                //if (page == 0) scroll_gridView_post.setScrollY(0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                //case R.id.hScrollview_popular_list:
                case R.id.gridview_project: {
                    NewsfeedVO newsfeedVO = (NewsfeedVO) parent.getAdapter().getItem(position);
                    Intent newsfeed = new Intent(getActivity().getApplicationContext(), NewsFeedDetailActivity.class);

                    newsfeed.putExtra("newsfeedVO", (Parcelable) newsfeedVO);

                    startActivityForResult(newsfeed, Utils.NEWSFEED_DETAIL);

                    break;
                }

            }
        }
    };

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Log.d("fffffffff","onStoponStoponStoponStoponStoponStoponStoponStop");
        super.onStop();
        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        Log.d("fffffffff","onDestroyViewonDestroyViewonDestroyViewonDestroyView");
        RecycleUtils.recursiveRecycle(getActivity().getWindow().getDecorView());
        super.onDestroyView();
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }if(newsfeedList != null){
            newsfeedList = null;
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.button_sub_popula: {
                    final String[] arrayString = {getActivity().getString(R.string.sort_recommend)
                            , getActivity().getString(R.string.new_sort)
                            , getActivity().getString(R.string.sort_comment)
                            , getActivity().getString(R.string.sort_like)
                    };


                    ResultInterface resultInterface = new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            initList();
                            button_sub_popula.setText(arrayString[(int) resultValue]);
                            sortValue = String.valueOf(((int) resultValue) + 1);
                            getNetwork3();
                            return null;
                        }
                    };

                    AlertDialogSort dialog = new AlertDialogSort(getActivity(), getActivity().getString(R.string.sort_type), resultInterface, arrayString, button_sub_popula.getText().toString());
                    dialog.show(getFragmentManager(), "dialog");
                }
                break;
                case R.id.button_sub_all: {//연예인 선택
                    Intent intent = new Intent(getActivity(), SelectStarListActivity.class);
                    intent.putExtra("intent","NF");
                    startActivityForResult(intent, 0);
                }
                break;

                case R.id.newsfeed_write:
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {

                        Intent intent = new Intent(getActivity(), NewsFeedWriteActivity.class);
                        startActivityForResult(intent, Utils.NEWSFEED_WRITE);
                    } else {
                        Utils.showDialog(getActivity());
                    }
                    break;
            }
        }
    };
}
