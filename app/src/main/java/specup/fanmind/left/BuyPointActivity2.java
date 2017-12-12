package specup.fanmind.left;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.billing.IabHelper;
import specup.fanmind.billing.IabResult;
import specup.fanmind.billing.Inventory;
import specup.fanmind.billing.Purchase;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;



public class BuyPointActivity2 extends Activity implements OnTask {

	String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmMAWGl488Fi0S4dwThH/K+abawS4EzsGcae+URGBlBrfzlFRf9F51jP0pStk5+k5OBueH+H81vwzw45F8MOKXyPGx9UM2+pSH2PrRtSvfodO5LW28KIE9IkmSFZ5YYzsjxbgn+HnOukFiH1GNrE3iA5KUV6mB10ggjXJOg7fP7kPx+BUDY1xByQhobSST5D+MtCMlq3Ev10jVG4VWYdZ1q/fgxOH606mPTH11FfR9gI+i/MhQxwinDVLBxZDbnd6pRoLwAeFNWvXqzuZiQHNxZc9HYjwWio8AiEsdbPM6QhKW7H9nWQ32zOu8wMXFdOMXz3KlTCQ4DeG8qZaw2S2NwIDAQAB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Tracker t = ((FanMindApplication)getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("BuyPoint Detail");
		t.send(new HitBuilders.AppViewBuilder().build());

        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);


//        bindService(new Intent(
//				"com.android.vending.billing.InAppBillingService.BIND"),
//				mServiceConn, Context.BIND_AUTO_CREATE);

		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_buypoint2);
		setView();
	}

	TextView mPrice, mProduct;
	RelativeLayout mLayout[];
	String mPoint;
	String ITEM_ID;
	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	String strPoint ="1";


	private void setView(){
		mLayout = new RelativeLayout[]{
				(RelativeLayout)findViewById(R.id.buypoint2_layout03),
				(RelativeLayout)findViewById(R.id.buypoint2_layout04),
				(RelativeLayout)findViewById(R.id.buypoint2_layout05),
				(RelativeLayout)findViewById(R.id.buypoint2_layout06),
		};

		TextView info01 = (TextView)findViewById(R.id.buypoint2_tv05);
		TextView info02 = (TextView)findViewById(R.id.buypoint2_tv06);

		mProduct = (TextView)findViewById(R.id.buypoint2_product);
		mPrice = (TextView)findViewById(R.id.buypoint2_price);
		mPoint = getIntent().getStringExtra("product");


		if(!Utils.getLocal(this, getString(R.string.korean))){
			mLayout[0].setVisibility(View.GONE);
			mLayout[1].setVisibility(View.GONE);
			mLayout[2].setVisibility(View.GONE);
			info01.setVisibility(View.GONE);
			info02.setVisibility(View.GONE);
			inAppSetting();
		}else{
			if(mPoint.equals(getString(R.string.buyitem01)) || mPoint.equals(getString(R.string.buyitem02))){
				mLayout[0].setVisibility(View.GONE);
			}
			mLayout[3].setVisibility(View.GONE);
		}


		String price= null;
		if(mPoint.equals(getString(R.string.buyitem01))){
			price = getString(R.string.buyprice01);
			ITEM_ID = "fanmaum11";
		}else if(mPoint.equals(getString(R.string.buyitem02))){
			price = getString(R.string.buyprice02);
			ITEM_ID = "fanmaum12";
		}else if(mPoint.equals(getString(R.string.buyitem03))){
			price = getString(R.string.buyprice03);
			ITEM_ID = "fanmaum13";
		}else if(mPoint.equals(getString(R.string.buyitem04))){
			price = getString(R.string.buyprice04);
			ITEM_ID = "fanmaum14";
		}else if(mPoint.equals(getString(R.string.buyitem05))){
			price = getString(R.string.buyprice05);
			ITEM_ID = "fanmaum15";
		}else if(mPoint.equals(getString(R.string.buyitem06))){
			price = getString(R.string.buyprice06);
			ITEM_ID = "fanmaum16";
		}

		mProduct.setText(mPoint);
		mPrice.setText(price);

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


	public void onBack(View v){
		onBack();
	}

	private void addPayLog(String tradeid, long millisecond){
		if(tradeid == null || millisecond == 0){
			Toast.makeText(this, R.string.inapperror, Toast.LENGTH_LONG).show();
			return;
		}
		if(!tradeid.startsWith("12999763169054705758.") && !tradeid.startsWith("GPA.")){
			Toast.makeText(this, R.string.inapperror, Toast.LENGTH_LONG).show();
			return;
		}

		String dateString= DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(millisecond)).toString();
		mParams = new ArrayList<NameValuePair>();
		mParams = Utils.setSession(this, mParams);
		mParams.add(new BasicNameValuePair("point", strPoint));
		mParams.add(new BasicNameValuePair("tradeid", tradeid));
		mParams.add(new BasicNameValuePair("etc", dateString));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_INSERT(this),
                AsyncTaskValue.DAILY_INSERT, this);
	}

	public void onBuy(View v){
		Intent intent = new Intent(this, WebViewActivity.class);
		switch(v.getId()){
		case R.id.buypoint2_layout03 :
			intent.putExtra("point", mPoint);
			intent.putExtra("sort", 0);
			break;
		case R.id.buypoint2_layout04 :
			intent.putExtra("point", mPoint);
			intent.putExtra("sort", 1);
			break;
		case R.id.buypoint2_layout05 :
			intent.putExtra("point", mPoint);
			intent.putExtra("sort", 2);
			break;
		}
		startActivity(intent);
	}

	public void onInApp(View v){
		Tracker t = ((FanMindApplication)getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.send(new HitBuilders.EventBuilder()
		.setCategory("BuyPoint")
		.setAction("Press Button")
		.setLabel("Pay InApp")
		.build());

		try{
			mHelper.launchPurchaseFlow(this, ITEM_ID, RC_REQUEST,
					mPurchaseFinishedListener);
		}catch(NullPointerException e){
			Log.d("#@#", "Billing  : NullPointerException " + e.getMessage());

		}catch(IllegalStateException  e){
			Log.d("#@#", "Billing  : IllegalStateException " + e.getMessage());
			Utils.setToast(this, R.string.inapplogin);
		}
	}

	private void onBack(){
		ActivityManager.getInstance().deleteActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
		}

		return super.onKeyDown(keyCode, event);
	}


	IabHelper mHelper;
	static final String TAG = "BuyPointActivity2";
	static final int RC_REQUEST = 10001;

    /**
     * ?몄빋寃곗젣
     */
	private void inAppSetting(){
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				if (!result.isSuccess()) {
					Log.d("startSetup", result.toString());
					return;
				}
				// Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}


	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if (result.isFailure()) {
				Log.d("inventory", result.toString());
				return;
			}
			Log.e(TAG, "Query inventory was successful.");
			Purchase gasPurchase = inventory.getPurchase(ITEM_ID);
			if (gasPurchase != null) {
				Log.d(TAG, "We have gas. Consuming it.");
				mHelper.consumeAsync(inventory.getPurchase(ITEM_ID), mConsumeFinishedListener);
				return;
			}

		}
	};

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isFailure()) {
				return;
			}
			if (purchase.getSku().equals(ITEM_ID)) {
				if(ITEM_ID.equals("fanmaum11")){
					strPoint ="1";
				}else if(ITEM_ID.equals("fanmaum12")){
					strPoint = "2";
				}else if(ITEM_ID.equals("fanmaum13")){
					strPoint = "3";
				}else if(ITEM_ID.equals("fanmaum14")){
					strPoint = "4";
				}else if(ITEM_ID.equals("fanmaum15")){
					strPoint = "5";
				}else if(ITEM_ID.equals("fanmaum16")){
					strPoint = "6";
				}
				//				SendConsumeResult(purchase, result);
				addPayLog(purchase.getOrderId(), purchase.getPurchaseTime());
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
		}
	};



	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
			if(result.isSuccess()){
				Log.d(TAG, "End consumption success.");
			}
		}
	};

	protected void SendConsumeResult(Purchase purchase, IabResult result) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("Result", result.getResponse());
			if (purchase != null) {
				jsonObj.put("OrderId", purchase.getOrderId());
				jsonObj.put("Sku", purchase.getSku());
				jsonObj.put("purchaseData", purchase.getOriginalJson());
				jsonObj.put("signature", purchase.getSignature());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if(requestCode==RC_REQUEST){
			if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
				// not handled, so handle it ourselves (here's where you'd
				// perform any handling of activity results not related to in-app
				// billing...
				super.onActivityResult(requestCode, resultCode, data);
			}
			else {
				Log.e(TAG, "onActivityResult handled by IABUtil.");
			}
		}
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		Log.e("123", result);
		if(output == AsyncTaskValue.DAILY_INSERT_NUM){
			if(Utils.getJsonDataString(result).equals("1101")){
				getJsonInsert(result);
			}
		}else if(output== AsyncTaskValue.DAILY_ADDPOINT_NUM){
			if(Utils.getJsonData(result)){
				getJsonPoint(result);
			}
		}
	}

	String mPay_srl=null;
	private void getJsonInsert(String result) {
		// TODO Auto-generated method stub
		try{
			JSONObject list = new JSONObject(result);
			String mlist = list.getString("list");
			JSONObject jsonarray = new JSONObject(mlist);
			mPay_srl =jsonarray.getString("idx");
		}catch(Exception e){
			e.printStackTrace();
		}

		mParams = new ArrayList<NameValuePair>();
		mParams = Utils.setSession(this, mParams);
		mParams.add(new BasicNameValuePair("pay_srl", mPay_srl));
		mParams.add(new BasicNameValuePair("point", strPoint));
		HttpRequest.setHttp(this, mAsyncTask, mParams, URLAddress.PAY_ACCOUNT_ADDPOINT(this), AsyncTaskValue.DAILY_ADDPOINT, this);
	}

	private void getJsonPoint(String result) {
		// TODO Auto-generated method stub
		try{
			JSONObject list = new JSONObject(result);
			String my_heart = list.getString("my_heart");
			FanMindSetting.setMY_HEART(this, my_heart);
//			if(((LeftMenuFragment)LeftMenuFragment.mLeftContext) !=null)
//				((LeftMenuFragment)LeftMenuFragment.mLeftContext).mMindTv.setText(Utils.getMoney(my_heart));
		}catch(Exception e){
			e.printStackTrace();
		}
		//			String a =  getString(R.string.dailysave).replace("{POINT}", strPoint);
		//			Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
		//				Utils.setToast(this, R.string.daily);
		notiSave(strPoint);
	}

	private void notiSave(String point){//�� ��Ƽ.
		if(strPoint.equals("1")) point ="600";
		else if(strPoint.equals("2")) point ="1,500";
		else if(strPoint.equals("3")) point ="3,500";
		else if(strPoint.equals("4")) point ="7,000";
		else if(strPoint.equals("5")) point ="17,000";
		else if(strPoint.equals("6")) point ="35,000";

		String maum = getString(R.string.paysave).replace("{POINT}", point);
		FanMindSetting.setPUSH_STAY(getApplicationContext(), true);
		NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle(getString(R.string.app_name))
		.setContentText(maum)
		.setSmallIcon(R.drawable.icon_128)
		.setAutoCancel(true);

		Intent intent = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setContentIntent(pendingIntent);


		notification.setDefaults(Notification.DEFAULT_VIBRATE);
		notification.setLights(Color.RED, 3000, 3000);
		notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification.build());
	}

	IInAppBillingService mService;
	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mServiceConn != null) {
			unbindService(mServiceConn);
		}
	}

}
