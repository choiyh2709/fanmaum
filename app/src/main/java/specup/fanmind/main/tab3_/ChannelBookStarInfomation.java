package specup.fanmind.main.tab3_;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import specup.fanmind.R;
import specup.fanmind.adapter.Newsfeed_AllAdapter;
import specup.fanmind.adapter.ProjectListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.ExpandableHeightGridView;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.main.tab2_.NewsFeedDetailActivity;
import specup.fanmind.main.tab2_.NewsFeedWriteActivity;
import specup.fanmind.vo.ChannelStarVo;
import specup.fanmind.vo.NewsfeedVO;
import specup.fanmind.vo.ProjectVO;

/**
 * 연예인 정보
 */
public class ChannelBookStarInfomation extends AppCompatActivity {


    private ChannelStarVo channelStarVo;
    private Button button_isBookmark, button_sub_all, newsfeed_write;
    private TreeSet<String> treeset_bookmarkStar;

    private List<Object> mSupportList;
    private ListView listView_project;
    private ProjectListAdapter mProjectListAdapter;
    private ExpandableHeightGridView gridView_post;
    private ScrollView scroll_gridView_post;

    private int beforeScrollBottom = 0;
    private RadioGroup radiogroup_project;
    private Newsfeed_AllAdapter newsfeed_AllAdapter;

    private String sortValue = "2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_channel_infomation);

        channelStarVo = getIntent().getParcelableExtra("ChannelStarVo");
        getNetwork();
        setView();


    }


    /**
     * 즐겨찾기 스타
     */
    private void getNetwork() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.MY_PAGE(ChannelBookStarInfomation.this), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {

                if (Utils.getJsonData(result, "errcode").equals("900")) {
                    setData(Utils.getJsonData(result, "star"));
                } else {
                    Utils.setToast(ChannelBookStarInfomation.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }
            }
        });
    }


    private void setData(String data1) {
        try {
            JSONArray jsonArray_star = new JSONArray(data1);
            treeset_bookmarkStar = new TreeSet<String>();

            for (int j = 0; j < jsonArray_star.length(); j++) {
                JSONObject jsonObject = jsonArray_star.getJSONObject(j);
                treeset_bookmarkStar.add(jsonObject.getString("star_srl"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * project sort
     * 연예인 고유번호(NULL : 전체 연예인, 0 : 내가 즐겨찾기한 연예인)
     * 정렬방식 (1 : 인기순, 2 : 최신순, 3 : 마감임박, 4 : 최다참여자, 5 : 최고 금액)
     */
    String[] arrayString;

    private void setProjectSort() {

        ResultInterface resultInterface = new ResultInterface() {
            @Override
            public Integer onResult(Object resultValue) {
                button_sub_all.setText(arrayString[(int) resultValue]);

                int sort = (int) resultValue;
                sortValue = String.valueOf(sort + 1);
                arrayString = null;
                switch (radiogroup_project.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_project: {//프로젝트

                        arrayString = new String[]{
                                getString(R.string.newsfeed_popularity)
                                , getString(R.string.newsfeed_Newest)
                                , getString(R.string.newsfeed_dead_line)
                                , getString(R.string.newsfeed_max_member)
                        };
                        getNetworkProject();
                    }
                    break;
                    case R.id.radiobutton_post: {//포스트
                        arrayString = new String[]{getString(R.string.sort_recommend)
                                , getString(R.string.new_sort)
                                , getString(R.string.sort_comment)
                                , getString(R.string.sort_like)
                        };
                        initList();
                        getNetwork3();
                    }
                    break;
                }

                return null;
            }
        };

        ChannelBookStarInfomation_AlertDialogSort channelBookStarInfomation_alert = new ChannelBookStarInfomation_AlertDialogSort(ChannelBookStarInfomation.this, getString(R.string.sort_type), resultInterface, arrayString, button_sub_all.getText().toString());
        channelBookStarInfomation_alert.show(getSupportFragmentManager(), "dialog");
    }


    private void initList() {
        scroll_gridView_post.setScrollY(0);
        page = 0;
        beforeScrollBottom = 0;
        newsfeed_AllAdapter = null;
    }

    private void setView() {

        arrayString = new String[]{
                getString(R.string.newsfeed_popularity)
                , getString(R.string.newsfeed_Newest)
                , getString(R.string.newsfeed_dead_line)
                , getString(R.string.newsfeed_max_member)
        };

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.imageview_profile);
        int overlayColor = getResources().getColor(R.color.white);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(overlayColor, 1.0f);
        roundingParams.setRoundAsCircle(true);
        Uri uri = Uri.parse(channelStarVo.getIcon_base() + channelStarVo.getIcon());
        simpleDraweeView.setImageURI(uri);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        ((TextView) findViewById(R.id.navi_tv01)).setText(channelStarVo.getName());
        ((TextView) findViewById(R.id.textView_starName)).setText(channelStarVo.getName());
        ((TextView) findViewById(R.id.textView_fanNumber)).setText(getString(R.string.total_following).replace("{TOTAL}", channelStarVo.getFan_cnt()));


        button_isBookmark = (Button) findViewById(R.id.button_isBookmark);
        button_isBookmark.setOnClickListener(onClick);
        setButton();

        button_sub_all = (Button) findViewById(R.id.button_sub_all);
        button_sub_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProjectSort();
            }
        });

        listView_project = (ListView) findViewById(R.id.listView_project);
        gridView_post = (ExpandableHeightGridView) findViewById(R.id.gridView_post);

        scroll_gridView_post = (ScrollView) findViewById(R.id.scroll_gridView_post);
        newsfeed_write = (Button) findViewById(R.id.newsfeed_write);
        newsfeed_write.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FanMindSetting.getLOGIN_OK(ChannelBookStarInfomation.this)) {

                    Intent intent = new Intent(ChannelBookStarInfomation.this, NewsFeedWriteActivity.class);
                    startActivityForResult(intent, Utils.NEWSFEED_WRITE);
                } else {
                    Utils.showDialog(ChannelBookStarInfomation.this);
                }
            }
        });
        scroll_gridView_post.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollY = scroll_gridView_post.getScrollY(); //for verticalScrollView
                int currentScrollBottom = gridView_post.getBottom();
                if (currentScrollBottom > beforeScrollBottom) {
                    if (currentScrollBottom - 1500 > 0 && currentScrollBottom - 1500 < scrollY) {
                        beforeScrollBottom = currentScrollBottom;
                        getNetwork3();
                    }
                }
            }
        });
        radiogroup_project = (RadioGroup) findViewById(R.id.radiogroup_project);
        radiogroup_project.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton_project: {//프로젝트
                        arrayString = new String[]{
                                getString(R.string.newsfeed_popularity)
                                , getString(R.string.newsfeed_Newest)
                                , getString(R.string.newsfeed_dead_line)
                                , getString(R.string.newsfeed_max_member)
                        };

                        sortValue = "2";
                        button_sub_all.setText(arrayString[1]);

                        listView_project.setVisibility(View.VISIBLE);
                        scroll_gridView_post.setVisibility(View.GONE);
                        newsfeed_write.setVisibility(View.GONE);
                        getNetworkProject();
                        break;
                    }
                    case R.id.radiobutton_post: {//포스트
                        arrayString = new String[]{getString(R.string.sort_recommend)
                                , getString(R.string.new_sort)
                                , getString(R.string.sort_comment)
                                , getString(R.string.sort_like)
                        };


                        sortValue = "2";
                        button_sub_all.setText(arrayString[1]);

                        listView_project.setVisibility(View.GONE);
                        scroll_gridView_post.setVisibility(View.VISIBLE);
                        newsfeed_write.setVisibility(View.VISIBLE);
                        initList();
                        getNetwork3();
                        break;
                    }
                }
            }
        });


        getNetworkProject();

    }

    private void setButton() {
        if (channelStarVo.getIs_added().equals("Y")) {
            button_isBookmark.setBackgroundResource(R.drawable.d1100_bookmark_active);
            button_isBookmark.setTextColor(Color.WHITE);
            button_isBookmark.setText(getString(R.string.bookmark_off));
        } else {
            button_isBookmark.setBackgroundResource(R.drawable.d1100_bookmark_normal);
            button_isBookmark.setTextColor(Color.RED);
            button_isBookmark.setText(getString(R.string.bookmark));
        }
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            NewsfeedVO newsfeedVO = (NewsfeedVO) parent.getAdapter().getItem(position);
            String newsfeeed = newsfeedVO.getNewsfeed_srl();
            Intent newsfeed = new Intent(getApplicationContext(), NewsFeedDetailActivity.class);
            newsfeed.putExtra("srl", newsfeeed);
            newsfeed.putExtra("newsfeedVO", (Parcelable) newsfeedVO);

            startActivity(newsfeed);
        }
    };


    OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_isBookmark: {// 즐겨찾기 , 해제하기 버튼

                    if (channelStarVo.getIs_added().equals("Y")) {
                        //즐겨찾기 해제
                        treeset_bookmarkStar.remove(channelStarVo.getStar_srl());

                    } else {
                        //즐겨찾기 요청
                        treeset_bookmarkStar.add(channelStarVo.getStar_srl());
                    }


                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                    mParams.add(new BasicNameValuePair("star", treeset_bookmarkStar.toString().replace("[", "").replace("]", "")));

                    mParams = Utils.setSession(ChannelBookStarInfomation.this, mParams);
                    HttpRequest.setHttp1(ChannelBookStarInfomation.this, URLAddress.STAR_MODIFY(ChannelBookStarInfomation.this), mParams, new OnTask() {
                        @Override
                        public void onTask(int output, String result) throws JSONException {
                            if (Utils.getJsonData(result, "errcode").equals("900")) {
                                treeset_bookmarkStar = null;
                                if (channelStarVo.getIs_added().equals("Y")) {
                                    //즐겨찾기 해제
                                    channelStarVo.setIs_added("N");

                                } else {
                                    //즐겨찾기 요청
                                    channelStarVo.setIs_added("Y");
                                }

                                setButton();

                                getNetwork();

                            } else if (Utils.getJsonData(result, "errcode").equals("950")) {
                                Utils.setToast(ChannelBookStarInfomation.this, getString(R.string.bookmark_one) + " ( " + Utils.getJsonData(result, "errcode") + " )");
                            } else {
                                Utils.setToast(ChannelBookStarInfomation.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "errcode") + " )");
                            }
                        }
                    });
                }
                break;
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


    /**
     * //스타 프로젝트 리스트
     * <p/>
     * * 정렬방식 (1 : 인기순, 2 : 최신순, 3 : 마감임박, 4 : 최다참여자, 5 : 최고 금액)
     */
    void getNetworkProject() {
        List<NameValuePair> mParams1 = new ArrayList<NameValuePair>();
        mParams1 = Utils.setSession(ChannelBookStarInfomation.this, mParams1);
        mParams1.add(new BasicNameValuePair("page", "0"));
        mParams1.add(new BasicNameValuePair("size", "100"));
        mParams1.add(new BasicNameValuePair("star", channelStarVo.getStar_srl()));
        mParams1.add(new BasicNameValuePair("order", sortValue));
        HttpRequest.setHttp1(ChannelBookStarInfomation.this, URLAddress.PROJECT_ALL(), mParams1, new OnTask() {

            @Override
            public void onTask(int output, String result) throws JSONException {

                String data = Utils.getJsonData(result, "data");
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    getJsonData1(Utils.getJsonData(data, "items"));
                } else {
                    Utils.setToast(ChannelBookStarInfomation.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                }

            }
        });


    }

    private void getJsonData1(String result) {
        try {
            if (mSupportList == null)
                mSupportList = new ArrayList<Object>();
            mSupportList.removeAll(mSupportList);

            JSONArray jsonArray = new JSONArray(result);
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

            mProjectListAdapter = new ProjectListAdapter(ChannelBookStarInfomation.this, mSupportList);


            listView_project.setAdapter(mProjectListAdapter);

            ListAdapter listAdapter = listView_project.getAdapter();

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView_project);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView_project.getLayoutParams();
            params.height = totalHeight + (listView_project.getDividerHeight() * (listAdapter.getCount() - 1));
            listView_project.setLayoutParams(params);


            listView_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProjectVO projectVO = (ProjectVO) parent.getAdapter().getItem(position);

                    Intent intent = new Intent(ChannelBookStarInfomation.this, ProjectDetailActivity.class);
                    intent.putExtra("srl", projectVO.getProject_srl());
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int page = 0;

    /**
     * 뉴스피드 목록
     * <p/>
     * 연예인 고유번호(NULL : 전체 연예인, 0 : 내가 즐겨찾기한 연예인)
     * 정렬방식 (1 : 인기순, 2 : 최신순, 3 : 마감임박, 4 : 최다참여자, 5 : 최고 금액)
     */
    private void getNetwork3() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(ChannelBookStarInfomation.this, mParams);
        mParams.add(new BasicNameValuePair("page", String.valueOf(page)));
        mParams.add(new BasicNameValuePair("size", "20"));
        mParams.add(new BasicNameValuePair("star", channelStarVo.getStar_srl()));
        mParams.add(new BasicNameValuePair("order", sortValue));


        HttpRequest.setHttp1(ChannelBookStarInfomation.this, URLAddress.NEWSFEED_ALL(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                            setProjectAll(Utils.getJsonData(result, "data"));
                        } else {
                            Utils.setToast(ChannelBookStarInfomation.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }
        );
    }

    private void setProjectAll(String data) {
        try {
            List<Object> newsfeedList = new ArrayList<Object>();

            JSONArray jsonArray = new JSONArray(Utils.getJsonData(data, "items"));

            if (jsonArray.length() > 0) {
                page += 1;

                for (int i = 0; i < jsonArray.length(); i++) {
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


                if (newsfeed_AllAdapter == null) {
                    newsfeed_AllAdapter = new Newsfeed_AllAdapter(ChannelBookStarInfomation.this, newsfeedList);
                    gridView_post.setAdapter(newsfeed_AllAdapter);
                } else {
                    newsfeed_AllAdapter.add(newsfeedList);

                }
                gridView_post.setOnItemClickListener(onItemClickListener);
                gridView_post.setExpanded(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
