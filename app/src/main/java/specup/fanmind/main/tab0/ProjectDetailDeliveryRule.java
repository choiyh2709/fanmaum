package specup.fanmind.main.tab0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

/**
 * 교환 / 환불 규정
 */
public class ProjectDetailDeliveryRule extends Activity {

    JSONObject total_Json = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_detail_delivery_rule);
        ActivityManager.getInstance().addActivity(this);

        try {
            total_Json = new JSONObject(getIntent().getStringExtra("total_Json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


}
