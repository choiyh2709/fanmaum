package specup.fanmind.main.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;

public class Step1Fragment extends Fragment implements OnTask {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_step1, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setView();
	}

	Button mBtn[] = new Button[1];
	EditText mEt[] = new EditText[4];
	TextView mText[] = new TextView[3];

	boolean isEmail = false, isCerti=false, isPwd = false;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	String mPhoneNumber, mJoinCode;
	String mDeviceID=null;
		
	private void setView(){
		
		((MakeAccountActivity)MakeAccountActivity.mMakeAccount).mTitle.setText(R.string.makeaccount01);

		mText[0] = (TextView)getActivity().findViewById(R.id.step1_tv03);
		mText[1] = (TextView)getActivity().findViewById(R.id.step1_tv03_01);
		mText[2] = (TextView)getActivity().findViewById(R.id.step1_tv03_02);

		//		String fullSend = getString(R.string.step03);
		//		String before = fullSend.substring(0, fullSend.length()-1);
		//		String after = fullSend.substring(fullSend.length()-1, fullSend.length());
		//		mText[3].setText(Html.fromHtml("<u><font color=#0024ff>"+before+"</font></u>"+after));

		mBtn[0] = (Button)getActivity().findViewById(R.id.step1_btn03);

		mEt[0] =(EditText)getActivity().findViewById(R.id.step1_et01);
		mEt[1] =(EditText)getActivity().findViewById(R.id.step1_et02);
		mEt[2] =(EditText)getActivity().findViewById(R.id.step1_et03);
		mEt[3] =(EditText)getActivity().findViewById(R.id.step1_et04);
		mBtn[0].requestFocus();

		mEt[0].setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(!Utils.checkLength(mEt[0])){ 	//포커스 변경시 이메일 체크
						if(Utils.isEmailValid(mEt[0].getText().toString().trim())){
							mParams = new ArrayList<NameValuePair>();
							mParams.add(new BasicNameValuePair("id", mEt[0].getText().toString().trim()));
							HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_CHECK_ID(getActivity()),
                                    AsyncTaskValue.LOGIN_EMAIL_CHECK, Step1Fragment.this);
						}else{
							Utils.setToast(getActivity(), R.string.notok04);
						}
					}
				}
			}
		});

		mEt[1].setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(!Utils.checkLength(mEt[1])){ //비밀번호 일치 or 불일치
						if(Utils.isPassword(mEt[1].getText().toString().trim())){
							mText[1].setText(R.string.account06);
							mText[1].setTextColor(Color.parseColor("#0085ec"));
							isPwd =true;
						}else{
							mText[1].setText(R.string.account05);
							mText[1].setTextColor(Color.parseColor("#ff0000"));
							isPwd =false;
						}
						mText[1].setVisibility(View.VISIBLE);
					}
				}
			}
		});

		mEt[2].setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(!Utils.checkLength(mEt[2])){ //��й�ȣ ��ġ or ����ġ
						if(mEt[1].getText().toString().trim().equals(mEt[2].getText().toString().trim())){
							mText[2].setText(R.string.account08);
							mText[2].setTextColor(Color.parseColor("#0085ec"));
						}else{
							mText[2].setText(R.string.account07);
							mText[2].setTextColor(Color.parseColor("#ff0000"));
						}
						mText[2].setVisibility(View.VISIBLE);
					}
				}
			}
		});

		mBtn[0].setOnClickListener(mClick);
		//		mBtn[1].setOnClickListener(mClick);
		//		mBtn[2].setOnClickListener(mClick);
		//		mText[3].setOnClickListener(mClick);

	}

	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			//			case R.id.step1_btn01 :
			//				//������ȣ �߼�.
			//				certSend();
			//				break;
			//			case R.id.step1_btn02 :
			//				//����Ȯ��
			//				certConfirm();
			//				break;
			case R.id.step1_btn03 :
				//�����ܰ�
				goStep2();
//								showStep2();
				break;
				//			case R.id.step1_tv09 :
				//				//������ȣ ��߼�.
				//				certSend();
				//				break;
			}
		}
	};


	//ȸ���� 2�ܰ�
	private void showStep2(){
		Step2Fragment step2 = new Step2Fragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("id", mEt[0].getText().toString().trim());
		bundle.putString("pwd", mEt[1].getText().toString().trim());
		bundle.putString("phone", mEt[3].getText().toString().trim());
		bundle.putString("imei", mDeviceID);
		bundle.putString("joincode", mJoinCode);
		step2.setArguments(bundle);
		ft.addToBackStack(null);
		ft.add(R.id.makeaccount_frame, step2, "step2");
		ft.commitAllowingStateLoss();
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		if(output == AsyncTaskValue.LOGIN_EMAIL_CHECK_NUM){
			if(result.contains("1201")){
				mText[0].setText(getString(R.string.account01));
				mText[0].setTextColor(Color.parseColor("#0085ec"));
				isEmail = true;
			}else if(result.contains("1202")){
				mText[0].setText(getString(R.string.account02));
				mText[0].setTextColor(Color.parseColor("#ff0000"));
				isEmail = false;
			}
			mText[0].setVisibility(View.VISIBLE);
		}else if(output == AsyncTaskValue.LOGIN_CERTI01_NUM){
			if(result.contains("1301")){
				Utils.setToast(getActivity(), R.string.certi01);
				mPhoneNumber = mEt[3].getText().toString().trim();
			}else if(result.contains("1302")){
				Utils.setToast(getActivity(), R.string.certi03);
			}else{
				Utils.setToast(getActivity(), R.string.certi02);
			}
		}else if(output == AsyncTaskValue.LOGIN_CERTI02_NUM){
			if(result.contains("1401")){
				Utils.setToast(getActivity(), R.string.notok05);
				isCerti = true;
				mJoinCode =  getJsonData(result);
			}else{
				Utils.setToast(getActivity(), R.string.notok02);
			}
		}else if(output == AsyncTaskValue.LOGIN_JOIN_NUM){
			if(Utils.getJsonDataString(result).equals("1302")){
				Utils.setToast(getActivity(), R.string.certi03);			
			}else if(Utils.getJsonDataString(result).equals("1101")){
				TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
				mDeviceID = telephonyManager.getDeviceId();
				if(mDeviceID ==null || mDeviceID.equals("0") || mDeviceID.contains("")){
					mDeviceID = Build.SERIAL;
				}
				showStep2();		
//				mParams = new ArrayList<NameValuePair>();
//				mParams.add(new BasicNameValuePair("imei", mDeviceID));
//				Utils.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.CHECK_DEVICE(getActivity()),
//						AsyncTaskValue.TIME_LINE, this);
			}
		}else if(output == AsyncTaskValue.TIME_LINE_NUM){
			Log.e(this.getClass().getSimpleName(), result);
			if(Utils.getJsonDataString(result).equals("1302")){
				Utils.setToast(getActivity(), R.string.certi04);			
			}else if(Utils.getJsonDataString(result).equals("1101")){
				showStep2();
			}
		}
	}


	private void goStep2(){
		if(Utils.checkLength(mEt[0])){
			Utils.setToast(getActivity(), R.string.blank01);
		}else if(Utils.checkLength(mEt[1])){
			Utils.setToast(getActivity(), R.string.blank02);
		}else if(Utils.checkLength(mEt[2])){
			Utils.setToast(getActivity(), R.string.blank03);
		}/*else if(mEt[3].getText().toString().trim().length()<10 || mEt[3].getText().toString().trim().length()>11){
			Utils.setToast(getActivity(), R.string.blank04);
		}else if(Utils.checkLength(mEt[4])){
			Utils.setToast(getActivity(), R.string.blank05);
		}*/else if(!isEmail){
			Utils.setToast(getActivity(), R.string.blank11);
		}else if(!Utils.isPassword(mEt[1].getText().toString().trim())){
			Utils.setToast(getActivity(), R.string.account05);
		}else if(!mEt[1].getText().toString().trim().equals(mEt[2].getText().toString().trim())){
			mBtn[0].setFocusable(true);
			mBtn[0].setFocusableInTouchMode(true);
			mBtn[0].requestFocus();
			Utils.setToast(getActivity(), R.string.notok01);
			mBtn[0].setFocusable(false);
			mBtn[0].setFocusableInTouchMode(false);
			mEt[2].requestFocus();
		}/*else if(!isCerti){
			Utils.setToast(getActivity(), R.string.notok02);
		}else if(!mPhoneNumber.equals(mEt[3].getText().toString().trim())){
			Utils.setToast(getActivity(), R.string.notok03);
		}*/else{
			//			showStep2();
			mParams = new ArrayList<NameValuePair>();
			mParams.add(new BasicNameValuePair("phone", mEt[3].getText().toString().trim()));
			HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.CHECK_PHONE(getActivity()),
					AsyncTaskValue.LOGIN_JOIN, this);
		}
	}

	private void certSend(){
		if(!Utils.checkLength(mEt[3])){
			if(Utils.isCellphone(mEt[3].getText().toString().trim())){
				mParams = new ArrayList<NameValuePair>();
				mParams.add(new BasicNameValuePair("phone", mEt[3].getText().toString().trim()));
				HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_SEND_CERT(getActivity()),
                        AsyncTaskValue.LOGIN_CERTI01, Step1Fragment.this);
			}else{
				Utils.setToast(getActivity(), R.string.notok06);
			}	
		}else{
			Utils.setToast(getActivity(), R.string.blank10);
		}
	}

	private void certConfirm(){
		if(!Utils.checkLength(mEt[4])){
			if(mPhoneNumber.equals(mEt[3].getText().toString().trim())){
				mParams = new ArrayList<NameValuePair>();
				mParams.add(new BasicNameValuePair("cert_code", mEt[4].getText().toString().trim()));
				mParams.add(new BasicNameValuePair("phone", mEt[3].getText().toString().trim()));
				HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.LOGIN_CHECK_CERT(getActivity()),
                        AsyncTaskValue.LOGIN_CERTI02, Step1Fragment.this);
			}else{
				Utils.setToast(getActivity(), R.string.notok03);
			}
		}else{
			Utils.setToast(getActivity(), R.string.blank05);
		}
	}

	private String getJsonData(String result){
		String joinCode = null;
		try{
			JSONObject json = new JSONObject(result);
			joinCode = json.getString("join_code");
		}catch(Exception e){
			e.printStackTrace();
		}
		return joinCode;
	}

}
