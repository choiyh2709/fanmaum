package specup.fanmind.main.tab1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.StarListAdapter;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.RightList;


public class SelectStarListActivity extends Activity implements OnTask {

    ListView mList, mList02;
    StarListAdapter mStarAdapter;
    StarListAdapter mStarAdapter2;
    ArrayList<RightList> mStarList;
    ArrayList<RightList> mSearchList;
    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;
    TextView mTitle, mStarTv;

    // private ProgressDialog mProgress;

    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_starlist);

        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable ignore) {
        }

        setView();
        mParams = new ArrayList<NameValuePair>();
        HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOGIN_STAR_LIST(this), AsyncTaskValue.LOGIN_STAR, this);
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
        if (output == AsyncTaskValue.LOGIN_STAR_NUM) {
            if (Utils.getJsonData(result))
                getJsonData(result);
        }
    }

    private void getJsonData(String result) {
        // TODO Auto-generated method stub
        int mCount = 0;
        try {
            JSONObject list = new JSONObject(result);
            String mlist = list.getString("list");
            JSONArray jsonarray = new JSONArray(mlist);
            mStarList.add(new RightList("", getString(R.string.all), false));
            int count = jsonarray.length();
            for (int i = 0; i < count; i++) {
                JSONObject json = jsonarray.getJSONObject(i);
                String star_idx = json.getString("star_srl");
                String star_name = json.getString("name");
                mCount++;
                mStarList.add(new RightList(star_idx, star_name, false));
            }
            String name = getString(R.string.firststar01);
            mStarList.add(new RightList("", name, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCount < 6) {
            for (int i = mCount; i < 6; i++) {
                mStarList.add(new RightList("", "", false));
            }
        }
        mList.setVisibility(View.VISIBLE);
        mList02.setVisibility(View.GONE);
        mStarTv.setVisibility(View.GONE);
        mList.setAdapter(mStarAdapter);

    }

    boolean isLogout;

    private void setView() {
        isLogout = getIntent().getBooleanExtra("logout", false);

        RelativeLayout mCancel = (RelativeLayout) findViewById(R.id.starlist_btn01);
        mCancel.setVisibility(View.GONE);

        RelativeLayout mOK = (RelativeLayout) findViewById(R.id.starlist_btn02);
        mOK.setBackgroundResource(R.drawable.pop_btn03);
        TextView mTvOK = (TextView) findViewById(R.id.starlist_tv04);
        mTvOK.setText(R.string.starlist01);

        mEdit = (EditText) findViewById(R.id.starlist_et);

        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mList.setVisibility(View.VISIBLE);
                    mList02.setVisibility(View.GONE);
                    mStarTv.setVisibility(View.GONE);
                }
                searchStar();
            }
        });

        mEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchStar();
                    return true;
                }
                return false;
            }
        });

        mTitle = (TextView) findViewById(R.id.starlist_tv01);
        mTitle.setText(R.string.choice05);
        mStarTv = (TextView) findViewById(R.id.starlist_listtv);
        mList = (ListView) findViewById(R.id.starlist_list01);
        mList02 = (ListView) findViewById(R.id.starlist_list02);

        mStarList = new ArrayList<RightList>();
        mSearchList = new ArrayList<RightList>();
        mStarAdapter = new StarListAdapter(this, mStarList);
        mStarAdapter2 = new StarListAdapter(this, mSearchList);
        mList02.setAdapter(mStarAdapter2);

        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String str = getString(R.string.firststar01);
                if (mStarList.get(position).getName().equals(str)) {
                    Uri uri = Uri.parse(URLAddress.FANMIND_STARURL);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } else {
                    if (!mStarList.get(position).getName().equals("")) {
                        selectItem(position);
                    }
                }
            }
        });

        mList02.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String str = getString(R.string.firststar01);
                if (mSearchList.get(position).getName().equals(str)) {
                    Uri uri = Uri.parse(URLAddress.FANMIND_STARURL);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } else {
                    if (!mSearchList.get(position).getName().equals("")) {
                        selectSearchItem(position);
                    }
                }
            }
        });

    }

    public void onSearch(View v) {
        searchStar();
    }

    private void searchStar() {
        if (Utils.checkLength(mEdit)) {
            Utils.setToast(this, R.string.startlist03);
        } else {
/*            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);*/
            boolean isYes = false;
            int mCount = 0;
            mSearchList.removeAll(mSearchList);
            for (int i = 0; i < mStarList.size(); i++) {
                if (mStarList.get(i).getName().toLowerCase()
                        .contains(mEdit.getText().toString().toLowerCase())) {
                    isYes = true;
                    mCount++;
                    String path = mStarList.get(i).getPath();
                    String name = mStarList.get(i).getName();
                    boolean select = mStarList.get(i).isDel();
                    mSearchList.add(new RightList(path, name, select));
                }
            }
            for (int i = mCount; i < 6; i++) {
                mSearchList.add(new RightList("", "", false));
            }
            if (isYes) {
                mList.setVisibility(View.GONE);
                mList02.setVisibility(View.VISIBLE);
                mStarTv.setVisibility(View.GONE);
            } else {
                mList.setVisibility(View.GONE);
                mList02.setVisibility(View.GONE);
                mStarTv.setVisibility(View.VISIBLE);
            }
            mStarAdapter2.notifyDataSetChanged();
        }
    }

    /**
     * 리스트를 클릭시.
     *
     * @param position 위치.
     */
    private void selectItem(int position) {
        boolean isSelect = mStarList.get(position).isDel();
        if (isSelect) {
            mStarList.get(position).setDel(false);
        } else {
            for (int i = 0; i < mStarList.size(); i++) {
                mStarList.get(i).setDel(false);
            }
            mStarList.get(position).setDel(true);
        }
        mStarAdapter.notifyDataSetChanged();
    }

    /**
     * 리스트를 클릭시.
     *
     * @param position 위치.
     */
    private void selectSearchItem(int position) {
        boolean isSelect = mSearchList.get(position).isDel();
        if (isSelect) {
            mSearchList.get(position).setDel(false);
        } else {
            for (int i = 0; i < mSearchList.size(); i++) {
                mSearchList.get(i).setDel(false);
            }
            mSearchList.get(position).setDel(true);
            for (int i = 0; i < mStarList.size(); i++) {
                if (mStarList.get(i).getName()
                        .equals(mSearchList.get(position).getName())) {
                    mStarList.get(i).setDel(true);
                } else {
                    mStarList.get(i).setDel(false);
                }
            }
            mStarAdapter.notifyDataSetChanged();
        }
        mStarAdapter2.notifyDataSetChanged();
    }

    /**
     * 취소, 확인버튼
     *
     * @param v
     */
    public void onClick(View v) {
        if (v.getId() == R.id.starlist_btn02) {
            // if(mList02.getVisibility()==View.VISIBLE){
            // setOK(mSearchList);
            // }else{
            setOK(mStarList);
            // }
        }
    }

    // Ȯ�ι�ư ��������.
    private void setOK(ArrayList<RightList> list) {
        String mName = "", mIndex = "";
        boolean isNum = false;
        int count = list.size();
        for (int i = 0; i < count; i++) {
            isNum = list.get(i).isDel();
            if (isNum) {
                mName = mName + list.get(i).getName();
                mIndex = mIndex + list.get(i).getPath();
                Log.d("Check",mName+","+mIndex);
                break;
            }
        }

        if (mName.length() == 0) {
            Toast.makeText(this, R.string.rightstartlist2, Toast.LENGTH_SHORT).show();
        } else {

            if (!mIndex.equals("")) {
                StarSetting.setSTAR_LIST_INDEX(this, mIndex);
                StarSetting.setSTAR_LIST(this, mName);
                StarSetting.setSTAR_SELECT_INDEX(this, mIndex);
                StarSetting.setSTAR_SELECT_NAME(this, mName);
                StarSetting.setProjectSTAR_SELECT_INDEX(this,mIndex);
                StarSetting.setNewsfeedSTAR_SELECT_INDEX(this,mIndex);
                MainActivity.mtitle_main.setText(mName);
            }

            if (isLogout) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                FanMindSetting.setLOGIN_OK(this, false);
            } else {
                Intent intent = new Intent();
                intent.putExtra("starIndex", mIndex);
                intent.putExtra("starName", mName);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
