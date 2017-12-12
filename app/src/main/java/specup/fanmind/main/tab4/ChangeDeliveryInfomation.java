package specup.fanmind.main.tab4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.AlertAddressSearch;
import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.GoodsEmsVO;
import specup.fanmind.vo.ProjectVO;

/**
 * 배송정보 변경
 */
public class ChangeDeliveryInfomation extends AppCompatActivity {

    ProjectVO projectVO;
    AlertAddressSearch alertAddressSearch;

    TextView textView_phoneNumber1, textView_phoneNumber2, textView_phoneNumber3, textView_email, textView_payment_type;
    EditText edittext_addess1, edittext_addess2, textView_name, attends_etc1, attends_etc2;
    LinearLayout layout_zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_delivery_infomation);
        ActivityManager.getInstance().addActivity(this);
        getNetworkInfo();
        try {
            projectVO = getIntent().getParcelableExtra("projectVO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setView();
    }


    private void setView() {
        layout_zipcode = (LinearLayout) findViewById(R.id.layout_zipcode);
        textView_name = (EditText) findViewById(R.id.attends_name);
        textView_phoneNumber1 = (TextView) findViewById(R.id.attends_phone1);
        textView_phoneNumber2 = (TextView) findViewById(R.id.attends_phone2);
        textView_phoneNumber3 = (TextView) findViewById(R.id.attends_phone3);
        textView_email = (TextView) findViewById(R.id.attends_email);
        edittext_addess1 = (EditText) findViewById(R.id.textView_attends_address);
        edittext_addess2 = (EditText) findViewById(R.id.edittext_attends_address);
        attends_etc1 = (EditText) findViewById(R.id.attends_etc1);
        attends_etc2 = (EditText) findViewById(R.id.attends_etc2);

        //참여자 이름
        textView_name.setText(projectVO.getPayment_name());
        //전화번호
        try {
            String tempMobileNumber = projectVO.getPayment_mobile();
            textView_phoneNumber1.setText(tempMobileNumber.substring(0, 3));
            textView_phoneNumber2.setText(tempMobileNumber.substring(3, 7));
            textView_phoneNumber3.setText(tempMobileNumber.substring(7, 11));

        } catch (Exception e) {
            textView_phoneNumber1.setText("");
            textView_phoneNumber2.setText("");
            textView_phoneNumber3.setText("");
        }
        //email
        textView_email.setText(projectVO.getPayment_email());
        // 주소
        edittext_addess1.setText(projectVO.getPayment_address_pri());
        edittext_addess2.setText(projectVO.getPayment_address_ext());
        //비고
        attends_etc1.setText(projectVO.getPayment_note());
        attends_etc2.setText(projectVO.getPayment_note_extra());

        Button searchAddress = (Button) findViewById(R.id.button_attends_address_search);
        if (searchAddress != null) {
            searchAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertAddressSearch = new AlertAddressSearch(ChangeDeliveryInfomation.this, new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            alertAddressSearch.getDialog().cancel();
                            edittext_addess1.setText(((String) resultValue).trim());
                            return null;
                        }
                    });
                    alertAddressSearch.show(getSupportFragmentManager(), "alertAddressSearch");
                }
            });
        }
    }


    public void onBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.getInstance().deleteActivity(this);
        finish();
    }

    public void onbuy(View v) {

        textView_name.setError(null);
        textView_phoneNumber1.setError(null);
        textView_phoneNumber2.setError(null);
        textView_phoneNumber3.setError(null);
        textView_email.setError(null);
        edittext_addess1.setError(null);
        edittext_addess2.setError(null);

        String phoneNumber = textView_phoneNumber1.getText().toString() + textView_phoneNumber2.getText().toString() + textView_phoneNumber3.getText().toString();
        if (Utils.checkLength(textView_name)) {
            textView_name.setError(getString(R.string.please_input_data));
            textView_name.requestFocus();
        } else if (!Utils.isCellphone(phoneNumber)) {
            textView_phoneNumber1.setError(getString(R.string.please_input_data));
            textView_phoneNumber1.requestFocus();
        } else if (!Utils.isEmailValid(textView_email.getText().toString())) {
            textView_email.setError(getString(R.string.please_input_data));
            textView_email.requestFocus();
        } else if (Utils.checkLength(edittext_addess1)) {
            edittext_addess1.setError(getString(R.string.please_input_data));
            edittext_addess1.requestFocus();
        } else if (Utils.checkLength(edittext_addess2)) {
            edittext_addess2.setError(getString(R.string.please_input_data));
            edittext_addess2.requestFocus();
        } else {

//수정하기 진행
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams = Utils.setSession(ChangeDeliveryInfomation.this, mParams);
            mParams.add(new BasicNameValuePair("order_srl", projectVO.getPayment_order_srl()));
            mParams.add(new BasicNameValuePair("name", textView_name.getText().toString()));
            mParams.add(new BasicNameValuePair("mobile", textView_phoneNumber1.getText().toString()+textView_phoneNumber2.getText().toString()+textView_phoneNumber3.getText().toString()));
            mParams.add(new BasicNameValuePair("email", textView_email.getText().toString().trim()));
            mParams.add(new BasicNameValuePair("is_global", projectVO.getPayment_is_global().equals("null")?"N":projectVO.getPayment_is_global()));
            mParams.add(new BasicNameValuePair("address_pri", edittext_addess1.getText().toString()));
            mParams.add(new BasicNameValuePair("address_sec", edittext_addess2.getText().toString()));
            mParams.add(new BasicNameValuePair("note", attends_etc1.getText().toString()));
            mParams.add(new BasicNameValuePair("note_extra", attends_etc2.getText().toString()));



            HttpRequest.setHttp1(ChangeDeliveryInfomation.this, URLAddress.PROJECT_ATTEND_MODIFY(), mParams, new OnTask() {
                        @Override
                        public void onTask(int output, String result) throws JSONException {

                            if (Utils.getJsonData(result, "code").equals("ATTENDED")) {
                                Utils.setToast(ChangeDeliveryInfomation.this, getString(R.string.modification_completed));
                            } else {
                                Utils.setToast(ChangeDeliveryInfomation.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                            }
                        }
                    }
            );
        }
    }

    /**
     * ems 정보 수집.
     */
    public static JSONArray internationalDeliveryImformation;

    private void getNetworkInfo() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        HttpRequest.setHttp1(this, URLAddress.EMS(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, final String result) throws JSONException {

                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                            internationalDeliveryImformation = new JSONArray(Utils.getJsonData(result, "data"));

                            FanMindApplication.arrayCountryData = new ArrayList<Object>();

                            GoodsEmsVO goodsEmsVO = new GoodsEmsVO();
                            goodsEmsVO.setRegion(String.valueOf(getString(R.string.default_delivery)));
                            FanMindApplication.arrayCountryData.add(goodsEmsVO);

                            if (projectVO.getPayment_is_extra() != null && projectVO.getPayment_is_extra().equalsIgnoreCase("Y")) {
                                GoodsEmsVO goodsEmsVO1 = new GoodsEmsVO();
                                goodsEmsVO1.setRegion(String.valueOf(getString(R.string.jeju_delivery)));
                                FanMindApplication.arrayCountryData.add(goodsEmsVO1);
                            }

                            if (projectVO.getPayment_is_global().equals("Y")) {


                                for (int i = 0; i < internationalDeliveryImformation.length(); i++) {
                                    JSONObject tempjsonObject = internationalDeliveryImformation.getJSONObject(i);
                                    if (tempjsonObject.getString("weight").equals("1")) {
                                        GoodsEmsVO goodsEmsVO2 = new GoodsEmsVO();
                                        goodsEmsVO2.setRegion(tempjsonObject.getString("region"));
                                        goodsEmsVO2.setWeight(tempjsonObject.getString("weight"));
                                        goodsEmsVO2.setCost(tempjsonObject.getString("cost"));
                                        FanMindApplication.arrayCountryData.add(goodsEmsVO2);
                                    }
                                }
                            }


                        }
                    }
                }
        );
    }

}