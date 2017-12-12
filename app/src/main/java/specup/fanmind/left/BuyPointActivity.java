package specup.fanmind.left;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.OnTask;


public class BuyPointActivity extends Activity implements OnTask {

    Context mContext;
    RelativeLayout mLayout, infoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setAnalyticsTrackerScreenName(this, "BuyPoint");
        ActivityManager.getInstance().addActivity(this);

        mContext = this;
        setContentView(R.layout.activity_buypoint);
        setView();
    }

    private void setView() {

        mLayout = (RelativeLayout) findViewById(R.id.buypoint_layout07);
        infoLayout = (RelativeLayout) findViewById(R.id.buypoint_layout08);
        if (!Utils.getLocal(this, getString(R.string.korean))) {//외국어
            mLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.GONE);
        } else {//한국어
            ((RelativeLayout)findViewById(R.id.buypoint_layout_2500)).setVisibility(View.GONE);
            ((RelativeLayout)findViewById(R.id.buypoint_layout_50000)).setVisibility(View.GONE);

        }
    }


    public void onBuy(View v) {
        Intent intent = new Intent(this, BuyPointActivity2.class);
        switch (v.getId()) {
            case R.id.buypoint_layout_1000:
                intent.putExtra("product", getString(R.string.buyitem01));
                break;
            case R.id.buypoint_layout_2500:
                intent.putExtra("product", getString(R.string.buyitem02));
                break;
            case R.id.buypoint_layout_5000:
                intent.putExtra("product", getString(R.string.buyitem03));
                break;
            case R.id.buypoint_layout_10000:
                intent.putExtra("product", getString(R.string.buyitem04));
                break;
            case R.id.buypoint_layout_25000:
                intent.putExtra("product", getString(R.string.buyitem05));
                break;
            case R.id.buypoint_layout_50000:
                intent.putExtra("product", getString(R.string.buyitem06));
                break;
        }
        startActivity(intent);
    }

    public void onBack(View v) {
        onBack();
    }


    private void onBack() {
        ActivityManager.getInstance().deleteActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }

        return super.onKeyDown(keyCode, event);
    }



    AsyncTask mAsyncTask;
    List<NameValuePair> mParams;


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

    @Override
    public void onTask(int output, String result) {

    }


    public void onSeePay(View v) {
        Intent intent = new Intent(this, PayAccountCheckActivity.class);
        startActivity(intent);
    }
}
