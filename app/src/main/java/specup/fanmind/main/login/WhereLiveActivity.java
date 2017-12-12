package specup.fanmind.main.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.igaworks.IgawCommon;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.adapter.StarListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.RightList;

public class WhereLiveActivity extends Activity{

	ListView mList;
	ArrayList<RightList> mStringList;
	StarListAdapter mWhereLive;
	String items[], mSi, mGu;
	boolean isSec = false, isBirth=false;
	TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity2(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_wherelive);
		setView();
	}

	private void setView(){
		mTitle =(TextView)findViewById(R.id.wherelive_tv01);
		mList = (ListView)findViewById(R.id.wherelive_list01);
		mStringList = new ArrayList<RightList>();
		mWhereLive = new StarListAdapter(this, mStringList);

		Intent intent = getIntent();
		isBirth = intent.getBooleanExtra("isBirth", false);

		if(!isBirth){
			if(Utils.getLocal(this, getString(R.string.korean))){
				items = getResources().getStringArray(R.array.region);
			}else{
				items =new String[]{"中国",
						          "香港",
				                  "日本",
				                  "대한민국",
				                  "澳门",
				                  "台湾",
				                  "Afghanistan",
				                  "Argentina",
				                  "Australia",
				                  "Austria",
				                  "Bangladesh",
				                  "Belgium",
				                  "Brazil",
				                  "Bulgaria",
				                  "Cambodia",
				                  "Cameroon",
				                  "Canada",
				                  "Chile",
				                  "Colombia",
				                  "Croatia",
				                  "Cuba",
				                  "Czech Republic",
				                  "Denmark",
				                  "Finland",
				                  "France",
				                  "Georgia",
				                  "Germany",
				                  "Ghana",
				                  "Great Britain",
				                  "Greece",
				                  "Guam",
				                  "Guatemala",
				                  "Haiti",
				                  "Honduras",
				                  "Hungary",
				                  "Iceland",
				                  "India",
				                  "Indonesia",
				                  "Iran (Islamic Republic of)",
				                  "Iraq",
				                  "Ireland",
				                  "Israel",
				                  "Italy",
				                  "Jamaica",
				                  "Jordan",
				                  "Kazakhstan",
				                  "Kenya",
				                  "Kuwait",
				                  "Kyrgyzstan",
				                  "Lao, People's Democratic Republic",
				                  "Latvia",
				                  "Lebanon",
				                  "Liberia",
				                  "Libya",
				                  "Luxembourg",
				                  "Macedonia, Rep. of",
				                  "Madagascar",
				                  "Malaysia",
				                  "Maldives",
				                  "Mali",
				                  "Malta",
				                  "Martinique",
				                  "Mexico",
				                  "Moldova, Republic of",
				                  "Monaco",
				                  "Mongolia",
				                  "Morocco",
				                  "Mozambique",
				                  "Myanmar, Burma",
				                  "Namibia",
				                  "Nauru",
				                  "Nepal",
				                  "Netherlands",
				                  "New Caledonia",
				                  "New Zealand",
				                  "Nicaragua",
				                  "Niger",
				                  "Nigeria",
				                  "Niue",
				                  "Northern Mariana Islands",
				                  "Norway",
				                  "Oman",
				                  "Pakistan",
				                  "Palau",
				                  "Papua New Guinea",
				                  "Paraguay",
				                  "Peru",
				                  "Philippines",
				                  "Poland",
				                  "Portugal",
				                  "Romania",
				                  "Russian Federation",
				                  "Rwanda",
				                  "Saudi Arabia",
				                  "Senegal",
				                  "Serbia",
				                  "Singapore",
				                  "Slovakia (Slovak Republic)",
				                  "Slovenia",
				                  "South Africa",
				                  "Spain",
				                  "Swaziland",
				                  "Sweden",
				                  "Switzerland",
				                  "Syria, Syrian Arab Republic",
				                  "Tanzania; officially the United Republic of Tanzania",
				                  "Thailand",
				                  "Tibet",
				                  "Togo",
				                  "Turkey",
				                  "Uganda",
				                  "Ukraine",
				                  "United Arab Emirates",
				                  "United Kingdom",
				                  "United States",
				                  "Uruguay",
				                  "Uzbekistan",
				                  "Vanuatu",
				                  "Vatican City State (Holy See)",
				                  "Venezuela",
				                  "Vietnam",
				                  "Yemen",
				                  "Zambia",
				                  "Zimbabwe"
				};
			}
			mTitle.setText(R.string.step07);	
		}else{
			String today = Utils.getTime();
			int year = Integer.parseInt(today.substring(0, 4));
			items = new String[100];
			for(int i=0; i<100; i++){
				items[i] = String.valueOf((year-10)-i);
			}
			mTitle.setText(R.string.step06);
		}

		for(int i=0; i<items.length; i++){
			mStringList.add(new RightList(items[i], false));
		}

		mList.setAdapter(mWhereLive);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mStringList.get(position).isDel()){
					mStringList.get(position).setDel(false);
				}else{
					for(int i=0; i<mStringList.size(); i++){
						mStringList.get(i).setDel(false);	
					}
					mStringList.get(position).setDel(true);
				}
				mWhereLive.notifyDataSetChanged();
			}
		});
	}

	public void onOK(View v){
		int position = -1;
		for(int i=0; i<mStringList.size(); i++){
			if(mStringList.get(i).isDel()){
				position = i;
				break;
			}
		}
		if(position !=-1){
			if(isBirth){
				setResult(RESULT_OK, getIntent().putExtra("birthyear", mStringList.get(position).getName()));
				ActivityManager.getInstance().deleteActivity2(this);
				finish();
			}else{
				if(!isSec){
					if(Utils.getLocal(this, getString(R.string.korean))){
						isSec = true;
						getDetail(mStringList.get(position).getName());
					}else{
						setResult(RESULT_OK, getIntent().putExtra("wherelive", mStringList.get(position).getName()));
						ActivityManager.getInstance().deleteActivity2(this);
						finish();
					}
				}else{
					mGu = mStringList.get(position).getName();
					setResult(RESULT_OK, getIntent().putExtra("wherelive", mSi+" "+mGu));
					ActivityManager.getInstance().deleteActivity2(this);
					finish();
				}
			}
		}else{
			Utils.setToast(this, R.string.step09);
		}
	}

	/**
	 * 구시군 얻어오기
	 * @param address 시,도이름
	 */
	private void getDetail(String address){
		mTitle.setText(R.string.step08);
		mSi = address;
		if(address.equals(items[0])){
			items =  getResources().getStringArray(R.array.seoul);
		}else if(address.equals(items[1])){
			items =  getResources().getStringArray(R.array.kyungki);
		}else if(address.equals(items[2])){
			items =  getResources().getStringArray(R.array.inchon);
		}else if(address.equals(items[3])){
			items =  getResources().getStringArray(R.array.pusan);
		}else if(address.equals(items[4])){
			items =  getResources().getStringArray(R.array.daejun);
		}else if(address.equals(items[5])){
			items =  getResources().getStringArray(R.array.daegu);
		}else if(address.equals(items[6])){
			items =  getResources().getStringArray(R.array.ulsan);
		}else if(address.equals(items[7])){
			items =  getResources().getStringArray(R.array.sejong);
		}else if(address.equals(items[8])){
			items =  getResources().getStringArray(R.array.gawngju);
		}else if(address.equals(items[9])){
			items =  getResources().getStringArray(R.array.gangwon);
		}else if(address.equals(items[10])){
			items =  getResources().getStringArray(R.array.chungbuk);
		}else if(address.equals(items[11])){
			items =  getResources().getStringArray(R.array.chungnam);
		}else if(address.equals(items[12])){
			items =  getResources().getStringArray(R.array.kyungbuk);
		}else if(address.equals(items[13])){
			items =  getResources().getStringArray(R.array.kyungnam);
		}else if(address.equals(items[14])){
			items =  getResources().getStringArray(R.array.junbuk);
		}else if(address.equals(items[15])){
			items =  getResources().getStringArray(R.array.junnam);
		}else if(address.equals(items[16])){
			items =  getResources().getStringArray(R.array.jeju);
		}

		mStringList.removeAll(mStringList);
		for(int i=0; i<items.length; i++){
			mStringList.add(new RightList(items[i], false));
		}
		mWhereLive.notifyDataSetChanged();
		mList.setSelection(0);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
