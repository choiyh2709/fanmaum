package specup.fanmind.main.tab0;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.HomeFragment_detail_delivery_international_ListAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;

/**
 * 배송 정보
 */
public class ProjectDetailDeliveryImformation extends Activity {

    String total_Json;
    ListView listView_delivery_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_detail_delivery_information);
        ActivityManager.getInstance().addActivity(this);

        total_Json = getIntent().getStringExtra("total_Json");
        setView();

        getNetworkInfo();

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ((ScrollView) findViewById(R.id.scrollView)).requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    private void setView() {
        ((TextView) findViewById(R.id.textView_default_delivery_value)).setText(getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getJsonData(total_Json, "ship_cost")));
        ((TextView) findViewById(R.id.textView_jeju_delivery_value)).setText(getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getJsonData(total_Json, "ship_cost_extra")));
        ((TextView) findViewById(R.id.textView_over_cost_delivery)).setText(getString(R.string.over_cost_delivery).replace("{TOTAL}", Utils.getJsonData(total_Json, "ship_over_unit")));
        ((TextView) findViewById(R.id.textView_over_cost_delivery_value)).setText(getString(R.string.over_cost_delivery_value).replace("{PRICE}", Utils.getJsonData(total_Json, "ship_over_cost")));
        spinner = (Spinner) findViewById(R.id.spinner_international);
        listView_delivery_cost = (ListView) findViewById(R.id.listView_delivery_cost);
        LinearLayout layout_international = (LinearLayout) findViewById(R.id.layout_international);
        if ( Utils.getJsonData(total_Json.toString(), "is_ship_global").equals("Y")) {

            layout_international.setVisibility(View.VISIBLE);
        }

    }


    Spinner spinner;

    private void getNetworkInfo() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("project_srl", Utils.getJsonData(total_Json.toString(), "project_srl")));
        mParams.add(new BasicNameValuePair("qunatity", "1"));
        mParams.add(new BasicNameValuePair("product_srl", "55"));//삭제 예정.
        mParams.add(new BasicNameValuePair("location", Utils.getLanquageLocal(ProjectDetailDeliveryImformation.this)));


        HttpRequest.setHttp1(this, URLAddress.PROJECT_ATTEND_CHECK(), mParams, new OnTask() {
            @Override
            public void onTask(int output, final String result) throws JSONException {
                if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                    final JSONArray internationalDeliveryImformation = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "locations"));

                    final List<String> arrayDataName = new ArrayList<String>();
                    for (int i = 1; i < internationalDeliveryImformation.length(); i++) {
                        JSONObject tempjsonObject = internationalDeliveryImformation.getJSONObject(i);
                        arrayDataName.add(tempjsonObject.getString("location"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProjectDetailDeliveryImformation.this, android.R.layout.simple_spinner_item, arrayDataName);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            deliveryCost(result);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
//                    deliveryCost(result);

                } else {
                    Utils.setToast(ProjectDetailDeliveryImformation.this,  Utils.getJsonData(result, "message"));
                }
            }
        });

    }

    private void deliveryCost(String result) {
        try {
            final JSONArray internationalDeliveryImformation = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "locations"));


            List<Object> arrayList = new ArrayList<Object>();

            for (int i = 1; i < internationalDeliveryImformation.length(); i++) {
                JSONObject tempjsonObject = null;
                tempjsonObject = internationalDeliveryImformation.getJSONObject(i);
                if (tempjsonObject.getString("location").equals( spinner.getAdapter().getItem(spinner.getSelectedItemPosition()))) {
                    for (int j = 1; j < 11; j++) {
                        int int_point = Integer.valueOf(Utils.getJsonData(tempjsonObject.toString(),"point"));
                        int int_currency = Integer.valueOf(Utils.getJsonData(tempjsonObject.toString(),"currency"));

                        arrayList.add(new BasicNameValuePair(String.valueOf(j), (getString(R.string.maum_num).replace("{VALUE}",Utils.getMoney(String.valueOf(int_point*j)) )
                                +" ("+(getString(R.string.mypoint_tv06).replace("{PRICE}",Utils.getMoney(String.valueOf(int_currency*j) )))+")")) );

                    }

                    break;
                }
            }
            HomeFragment_detail_delivery_international_ListAdapter homeFragment_detail_delivery_international_listAdapter =
                    new HomeFragment_detail_delivery_international_ListAdapter(ProjectDetailDeliveryImformation.this, arrayList);
            listView_delivery_cost.setAdapter(homeFragment_detail_delivery_international_listAdapter);
            listView_delivery_cost.setOnTouchListener(onTouchListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
    }


}
