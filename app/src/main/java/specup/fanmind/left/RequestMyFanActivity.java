package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.RequestStarAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.SupportList;


public class RequestMyFanActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_requestmyfan);


		Tracker t = ((FanMindApplication)getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Request Star");
		t.send(new HitBuilders.AppViewBuilder().build());


		isNoti = getIntent().getBooleanExtra("isNoti", false);
		mParams = new ArrayList<NameValuePair>();
		mParams = Utils.setSession(this, mParams);
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.STAR_REQUEST_LIST(this),
                AsyncTaskValue.STAR_REQUEST, this);
		setView();
	}



	ListView mList;
	ArrayList<SupportList> mSupportList;
	RequestStarAdapter mRequestAdapter;
	RelativeLayout mBtn01, mBtn02;
	Button mBtn03;
	RelativeLayout mLayout;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	EditText mStarEt;
	int mPosition;
	boolean isNoti;

	private void setView(){
		mList =(ListView)findViewById(R.id.request_list);
		mBtn01=(RelativeLayout)findViewById(R.id.request_btn01);
		mBtn02=(RelativeLayout)findViewById(R.id.request_btn02);
		mBtn03=(Button)findViewById(R.id.request_btn03);
		mLayout=(RelativeLayout)findViewById(R.id.request_layout02);
		mStarEt = (EditText)findViewById(R.id.request_et01);

		mBtn01.setOnClickListener(mClick);
		mBtn02.setOnClickListener(mClick);
		mBtn03.setOnClickListener(mClick);

		mSupportList = new ArrayList<SupportList>();
		mRequestAdapter = new RequestStarAdapter(this, mSupportList, mClick);
		mList.setAdapter(mRequestAdapter);
	}

	OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.request_btn01 :
				if(FanMindSetting.getLOGIN_OK(RequestMyFanActivity.this)){
					Intent intent = new Intent(RequestMyFanActivity.this, NotifyFanMindActivity.class);
					intent.putExtra("kind", 1);
					startActivity(intent);
				}else{
//					Utils.showDialog(RequestMyFanActivity.this);
				}
				break;
			case R.id.request_btn02 :
				mLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.request_btn03 :
				if(FanMindSetting.getLOGIN_OK(RequestMyFanActivity.this)){
					requestStar();
				}else{
//					Utils.showDialog(RequestMyFanActivity.this);
				}
				break;
			case R.id.listrequest_hidelayout :  //리스트 하트버튼
				if(FanMindSetting.getLOGIN_OK(RequestMyFanActivity.this)){
					starVote(v);
				}else{
//					Utils.showDialog(RequestMyFanActivity.this);
				}
				break;
			}
		}
	};


    /**투표하기, 투표취소
   	 * @param v
   	 */
	private void starVote(View v){
		mPosition = (Integer)v.getTag();
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("standby_srl", mSupportList.get(mPosition).getSrl()));
		mParams = Utils.setSession(RequestMyFanActivity.this, mParams);
		if(mSupportList.get(mPosition).getWarning().equals("1")){
			HttpRequest.setHttp(RequestMyFanActivity.this, mAsyncTask, mParams, URLAddress.STAR_VOTE_CANCEL(RequestMyFanActivity.this),
                    AsyncTaskValue.STAR_VOTE_CANCEL, this);
		}else{
			int now = Integer.parseInt(mSupportList.get(mPosition).getNowMoney());
			int goal = Integer.parseInt(mSupportList.get(mPosition).getGoalMoney());
			if(goal == now){
				Utils.setToast(RequestMyFanActivity.this, R.string.request09);
			}else{
				HttpRequest.setHttp(RequestMyFanActivity.this, mAsyncTask, mParams, URLAddress.STAR_VOTE(RequestMyFanActivity.this),
						AsyncTaskValue.STAR_VOTE, this);
			}
		}
	}


    /**
   	 *내 스타 요청
   	 */
	private void requestStar(){
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("text", mStarEt.getText().toString().trim()));
		mParams = Utils.setSession(this, mParams);
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.STAR_ADD(this),
                AsyncTaskValue.STAR_ADD, this);
	}


	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.STAR_REQUEST_NUM){
			if(Utils.getJsonData(result)){
				getJsonData(result);
			}
		}else if(output== AsyncTaskValue.STAR_ADD_NUM){
			if(Utils.getJsonDataString(result).equals("900")){//��ǥ����
				Utils.setToast(this, R.string.request05);
			}else if(Utils.getJsonDataString(result).equals("5301")){ //�ߺ���ǥ
				Utils.setToast(this, R.string.request08);
			}
		}else if(output== AsyncTaskValue.STAR_VOTE_NUM){
			if(Utils.getJsonDataString(result).equals("5251")){
				mSupportList.get(mPosition).setWarning("1");
				String vote = mSupportList.get(mPosition).getNowMoney();
				int sum = Integer.parseInt(vote)+1;
				mSupportList.get(mPosition).setNowMoney(String.valueOf(sum));
				mRequestAdapter.notifyDataSetChanged();
//				Utils.setToast(this, R.string.request06);
			}
		}else if(output==AsyncTaskValue.STAR_VOTE_CANCEL_NUM){
			if(Utils.getJsonDataString(result).equals("5261")){
				mSupportList.get(mPosition).setWarning("0");
				String vote = mSupportList.get(mPosition).getNowMoney();
				int sum = Integer.parseInt(vote)-1;
				mSupportList.get(mPosition).setNowMoney(String.valueOf(sum));
				mRequestAdapter.notifyDataSetChanged();
//				Utils.setToast(this, R.string.request07);
			}
		}
	}

	private void getJsonData(String result){
		try{
			JSONObject json = new JSONObject(result);
			String list = json.getString("list");
			JSONArray jsonArray = new JSONArray(list);
			for(int i=0; i<jsonArray.length(); i++){
				String srl = jsonArray.getJSONObject(i).getString("standby_srl");
				String name = jsonArray.getJSONObject(i).getString("name");
				String thumbnail = jsonArray.getJSONObject(i).getString("thumbnail");
				//				thumbnail = thumbnail.substring(1, thumbnail.length());
				String thumbnail_base = jsonArray.getJSONObject(i).getString("thumbnail_base");
				String vote_goal = jsonArray.getJSONObject(i).getString("vote_goal");
				String vote_now = jsonArray.getJSONObject(i).getString("vote_now");
				String my_vote = jsonArray.getJSONObject(i).getString("my_vote");
				mSupportList.add(new SupportList(srl, name, thumbnail_base+thumbnail, vote_goal, vote_now, my_vote));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		mRequestAdapter.notifyDataSetChanged();
	}


	public void onBack(){//백버튼 눌렀을시
		if(mLayout.getVisibility() == View.VISIBLE){
			mStarEt.setText("");
			mLayout.setVisibility(View.GONE);
		}else{
			if(isNoti){
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			ActivityManager.getInstance().deleteActivity(this);
//			if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//			((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v){
		onBack();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

}
