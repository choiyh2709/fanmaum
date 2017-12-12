package specup.fanmind.main.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.igaworks.IgawCommon;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.StarListAdapter;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.RightList;


public class StarListActivity extends Activity implements OnTask {

	ListView mList, mList02;
	StarListAdapter mStarAdapter;
	StarListAdapter mStarAdapter2;
	ArrayList<RightList> mStarList;
	ArrayList<RightList> mSearchList;

	int mNumber;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	TextView mSelect, mStarTv;
	EditText mEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_starlist);
		setView();
	}


	private void setView(){
		mStarTv =(TextView)findViewById(R.id.starlist_listtv);
		mList02=(ListView)findViewById(R.id.starlist_list02);

		mParams = new ArrayList<NameValuePair>();
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.LOGIN_STAR_LIST(this), AsyncTaskValue.LOGIN_STAR, this);

		mSearchList = new ArrayList<RightList>();


		mEdit = (EditText)findViewById(R.id.starlist_et);
		mEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==0){
					mList.setVisibility(View.VISIBLE);
					mList02.setVisibility(View.GONE);
					mStarTv.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});



		mSearchList = new ArrayList<RightList>();
		mStarAdapter2 = new StarListAdapter(this, mSearchList);
		mList02.setAdapter(mStarAdapter2);


		mList =(ListView)findViewById(R.id.starlist_list01);
		mSelect =(TextView)findViewById(R.id.starlist_tv02);

		mStarList = new ArrayList<RightList>();
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String str = getString(R.string.firststar01);
				if(mStarList.get(position).getName().equals(str)){
					Uri uri = Uri.parse(URLAddress.FANMIND_STARURL);
					Intent it  = new Intent(Intent.ACTION_VIEW,uri);
					startActivity(it);
				}else{
					if(!mStarList.get(position).getName().equals("")){
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
				if(mSearchList.get(position).getName().equals(str)){
					Uri uri = Uri.parse(URLAddress.FANMIND_STARURL);
					Intent it  = new Intent(Intent.ACTION_VIEW,uri);
					startActivity(it);
				}else{
					if(!mSearchList.get(position).getName().equals("")){
						selectSearchItem(position);
					}
				}
			}
		});

		mEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchStar();
					return true;
				}
				return false;
			}
		});
	}

    /**  리스트 서치를 클릭시.
   	 * @param position 위치.
   	 */
	private void selectSearchItem(int position){
		boolean isSelect = mSearchList.get(position).isDel();
		if(isSelect){
			mSearchList.get(position).setDel(false);
			for(int i=0; i<mStarList.size(); i++){
				if(mStarList.get(i).getName().equals(mSearchList.get(position).getName())){
					mStarList.get(i).setDel(false);
					mStarAdapter.notifyDataSetChanged();
				}
			}
		}else{
			mNumber =1;
			for(int i=0; i<mStarList.size(); i++){
				boolean isNum = mStarList.get(i).isDel();
				if(isNum) mNumber++;
			}
			if(mNumber<6){
				mSearchList.get(position).setDel(true);
				for(int i=0; i<mStarList.size(); i++){
					if(mStarList.get(i).getName().equals(mSearchList.get(position).getName())){
						mStarList.get(i).setDel(true);
						mStarAdapter.notifyDataSetChanged();
					}
				}
			}else{
				mNumber = 5;
				Toast.makeText(StarListActivity.this, R.string.startlist, Toast.LENGTH_SHORT).show();
			}
		}
		mStarAdapter2.notifyDataSetChanged();
	}


    /** 체크된상태 보여주기
   	 * @param position mStarList의 위치
   	 * @param isShow 체크된 상태
   	 */
	private void showCheck(int position, boolean isShow){
		mSelect.setText(getString(R.string.startlist02).replace("{NUMBER}", String.valueOf(mNumber)));
		mStarList.get(position).setDel(isShow);
		mStarAdapter.notifyDataSetChanged();
	}


    /** 리스트를 클릭시.
   	 * @param position 위치.
   	 */
	private void selectItem(int position){
		boolean isSelect = mStarList.get(position).isDel();
		if(isSelect){
			mNumber = mNumber-1;
			mSelect.setText(getString(R.string.startlist02).replace("{NUMBER}", String.valueOf(mNumber)));
			mStarList.get(position).setDel(false);
			mStarAdapter.notifyDataSetChanged();
		}else{
			mNumber =1;
			for(int i=0; i<mStarList.size(); i++){
				boolean isNum = mStarList.get(i).isDel();
				if(isNum) mNumber++;
			}
			if(mNumber<6){
				boolean isCheck = mStarList.get(position).isDel();
				showCheck(position, !isCheck);
			}else{
				mNumber = 5;
				Toast.makeText(StarListActivity.this, R.string.startlist, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onClick(View v){
		if(v.getId() == R.id.starlist_btn01){
			finish();
		}else if(v.getId() == R.id.starlist_btn02){
//			if(mList02.getVisibility()==View.VISIBLE){
//				setOK(mSearchList);	
//			}else{
				setOK(mStarList);
//			}
		}
	}


	public void onSearch(View v){
		searchStar();
	}

	private void searchStar(){
		if(Utils.checkLength(mEdit)){
			Utils.setToast(this, R.string.startlist03);
		}else{
			InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
			boolean isYes = false;
			int mCount=0;
			mSearchList.removeAll(mSearchList);
			for(int i=0; i<mStarList.size(); i++){
				if(mStarList.get(i).getName().toLowerCase().contains(mEdit.getText().toString().toLowerCase())){
					isYes = true;
					mCount++;
					String path = mStarList.get(i).getPath();
					String name = mStarList.get(i).getName();
					boolean del = mStarList.get(i).isDel();
					mSearchList.add(new RightList(path, name, del));
				}
			}
			for(int i=mCount; i<6; i++){
				mSearchList.add(new RightList("", "", false));
			}
			if(isYes){
				mList.setVisibility(View.GONE);
				mList02.setVisibility(View.VISIBLE);
				mStarTv.setVisibility(View.GONE);
			}else{
				mList.setVisibility(View.GONE);
				mList02.setVisibility(View.GONE);
				mStarTv.setVisibility(View.VISIBLE);
			}
			mStarAdapter2.notifyDataSetChanged();
		}
	}


    /**
   	 * 확인버튼 눌렀을시.
   	 */
	private void setOK(ArrayList<RightList> list){
		String mName =""; String mIdex=""; boolean isNum=false;
		for(int i=0; i<list.size(); i++){
			isNum = list.get(i).isDel();
			if(isNum){
				mIdex = mIdex+list.get(i).getPath()+",";
				mName =  mName+list.get(i).getName()+",";
			}
		}

		if(mName.length()==0){
			Toast.makeText(this, R.string.rightstartlist2, Toast.LENGTH_SHORT).show();
		}else{
			StarSetting.setSTAR_LIST(this, mName.substring(0, mName.length()-1));
			StarSetting.setSTAR_LIST_INDEX(this, mIdex.substring(0, mIdex.length()-1));
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.LOGIN_STAR_NUM){
			if(Utils.getJsonData(result))
				getJsonData(result);
		}
	}


	private void getJsonData(String result){
		int mCount=0;
		boolean isAgain = false, isHere=false; String mName[]=null;
		if(StarSetting.getSTAR_LIST_INDEX(this).length()==0){
			isAgain = false;
			mSelect.setText(getString(R.string.startlist02).replace("{NUMBER}", String.valueOf(0)));
		}else{
			isAgain = true;
			mName = StarSetting.getSTAR_LIST_INDEX(this).split(",");
			mSelect.setText(getString(R.string.startlist02).replace("{NUMBER}", String.valueOf(mName.length)));
			mNumber = mName.length;
		}
		try{
			JSONObject list = new JSONObject(result);
			String mlist = list.getString("list");
			JSONArray jsonarray = new JSONArray(mlist);
			for(int i=0; i<jsonarray.length(); i++){
				JSONObject json = jsonarray.getJSONObject(i);
				String star_idx = json.getString("star_srl");
				String star_name = json.getString("name");
				if(isAgain){
					for(int j=0; j<mName.length; j++){
						if(mName[j].equals(star_idx)){
							isHere = true;
							break;
						}else{
							isHere = false;
						}
					}
				}
				mCount++;
				mStarList.add(new RightList(star_idx, star_name, isHere));
			}
			String name = getString(R.string.firststar01);
			mStarList.add(new RightList("", name, false));
		}catch(Exception e){
			e.printStackTrace();
		}

		if(mCount<6){
			for(int i=mCount; i<6; i++){
				mStarList.add(new RightList("", "", false));
			}
		}
		mStarAdapter = new StarListAdapter(this, mStarList);
		mList.setAdapter(mStarAdapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IgawCommon.startSession(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		IgawCommon.endSession();
	}
}
