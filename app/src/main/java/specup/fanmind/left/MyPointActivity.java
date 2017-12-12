package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.MyPointAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.RightList;


public class MyPointActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ActivityManager.getInstance().addActivity(this);
		mMyPointContext = MyPointActivity.this;
		
		setContentView(R.layout.fragment_mypoint);
		mParams = new ArrayList<NameValuePair>();
		mParams = Utils.setSession(this, mParams);
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.MY_POINT(this),
                AsyncTaskValue.MYPOINT, this);

		setView();
	}

	//서버통신에 필요한 정보.
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	public TextView mMyPoint;

	ArrayList<String> mParentList;
	ArrayList<ArrayList<RightList>> mChildList;

	ExpandableListView mList;
	MyPointAdapter mMyPointAdapter;
	public static MyPointActivity mMyPointContext;
	
	private void setView(){

		mMyPoint = (TextView)findViewById(R.id.mypoint_tv02);
		mMyPoint.setText(Utils.getMoney(FanMindSetting.getMY_HEART(this)));


		mParentList = new ArrayList<String>();
		mChildList = new ArrayList<ArrayList<RightList>>();
		mMyPointAdapter = new MyPointAdapter(this, mParentList, mChildList);

		mList =(ExpandableListView)findViewById(R.id.mypoint_list);
		mList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				if(groupPosition ==0){
					if(mChildList.get(groupPosition).get(childPosition).getReady().equals("1")){
//						Intent intent = new Intent(MyPointActivity.this, SupportDeliveryDetailActivity.class);
//						intent.putExtra("srl", mChildList.get(groupPosition).get(childPosition).getStarsrl());
//						intent.putExtra("star_srl", mChildList.get(groupPosition).get(childPosition).getStar());
//						intent.putExtra("mypoint", true);
//						startActivity(intent);
					}else{
						if(mChildList.get(groupPosition).get(childPosition).getPath().equals("1")){
//							Intent intent = new Intent(MyPointActivity.this, SupportIngDetailActivity.class);
//							intent.putExtra("srl", mChildList.get(groupPosition).get(childPosition).getStarsrl());
//							intent.putExtra("star_srl", mChildList.get(groupPosition).get(childPosition).getStar());
//							intent.putExtra("isNoti2", true);
//							startActivity(intent);
						}else{
							Intent intent = new Intent(MyPointActivity.this, EventDetailActivity.class);
							intent.putExtra("srl", mChildList.get(groupPosition).get(childPosition).getStarsrl());
							intent.putExtra("star_srl", mChildList.get(groupPosition).get(childPosition).getStar());
							intent.putExtra("mypoint", true);
							startActivity(intent);
						}
					}
				}

				return false;
			}
		});

		mList.setAdapter(mMyPointAdapter);
		mList.setGroupIndicator(null);
		mList.setChildIndicator(null);
		mList.setChildDivider(null);
		mList.setDividerHeight(0);

	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.MYPOINT_NUM){
			if(Utils.getJsonData(result)){
				getJsonData(result);
			}
		}else if(output == AsyncTaskValue.PAY_ACCOUNT_NUM){
			if(Utils.getJsonData(result)){
				getJsonPayData(result);
			}
		}
	}

	private void getJsonPayData(String result){
		try{
			JSONObject json = new JSONObject(result);
			String list = json.getString("list");
			JSONArray jsonArray = new JSONArray(list);
			mParentList.add(getString(R.string.mypoint_tv03));
			mChildList.add(new ArrayList<RightList>());
			for(int i=0; i<jsonArray.length(); i++){
				String board = jsonArray.getJSONObject(i).getString("regdate");
				String srl = jsonArray.getJSONObject(i).getString("paytype");
				String heart = jsonArray.getJSONObject(i).getString("point");
				String title = jsonArray.getJSONObject(i).getString("id");
				String is_ready = jsonArray.getJSONObject(i).getString("idx");
				String star_srl = jsonArray.getJSONObject(i).getString("mobilid");
				mChildList.get(1).add(new RightList(board, srl, heart, title, is_ready, star_srl));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		mMyPointAdapter.notifyDataSetChanged();
	}

	private void getJsonData(String result){
		try{
			JSONObject json = new JSONObject(result);
			String list = json.getString("list");
			JSONArray jsonArray = new JSONArray(list);
			mParentList.add(getString(R.string.mypoint_tv02));
			mChildList.add(new ArrayList<RightList>());
			for(int i=0; i<jsonArray.length(); i++){
				String board = jsonArray.getJSONObject(i).getString("board");
				String srl = jsonArray.getJSONObject(i).getString("srl");
				String heart = jsonArray.getJSONObject(i).getString("heart");
				String title = jsonArray.getJSONObject(i).getString("title");
				String is_ready = jsonArray.getJSONObject(i).getString("is_ready");
				String star_srl = jsonArray.getJSONObject(i).getString("star_srl");
				mChildList.get(0).add(new RightList(board, srl, heart, title, is_ready, star_srl));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		mMyPointAdapter.notifyDataSetChanged();

		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("id", FanMindSetting.getEMAIL_ID(this)));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_LOG(this),
				AsyncTaskValue.PAY_ACCOUNT, this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			ActivityManager.getInstance().deleteActivity(this);
//			if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//				((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v){
		ActivityManager.getInstance().deleteActivity(this);	
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMyPointContext=null;
	}
}
