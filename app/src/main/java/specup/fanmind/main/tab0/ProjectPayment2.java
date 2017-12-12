package specup.fanmind.main.tab0;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.GoodsEmsVO;
import specup.fanmind.vo.ProductVO;

/**
 * 프로젝트 참여(결제 2page)
 */
public class ProjectPayment2 extends AppCompatActivity {

    ProductVO productVO;
    JSONObject total_Json;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    AlertAddressSearch alertAddressSearch;

    TextView textView_phoneNumber1, textView_phoneNumber2, textView_phoneNumber3, textView_email;
    EditText edittext_addess1, edittext_addess2, textView_name, attends_etc1, attends_etc2;
    LinearLayout layout_zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_payment2);
        ActivityManager.getInstance().addActivity(this);

        try {
            productVO = getIntent().getParcelableExtra("ProductVo");
            total_Json = new JSONObject(getIntent().getStringExtra("total_Json"));
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

        //선택하신 상품 및 특전 확인.
        LinearLayout view = (LinearLayout) findViewById(R.id.goodsList);
        LinearLayout layout_goods = (LinearLayout) findViewById(R.id.layout_goods);
        LinearLayout layout_freeSend = (LinearLayout) findViewById(R.id.layout_freeSend);

        //상품
        if (productVO.getSrl() != null) {

            LayoutInflater mLayoutInflater0 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout tempLinearLayout0 = (LinearLayout) mLayoutInflater0.inflate(R.layout.list_payment_goods0, null, false);
            TextView textView_benefit1 = (TextView) tempLinearLayout0.findViewById(R.id.textView_benefit1);
            TextView textView_benefit2 = (TextView) tempLinearLayout0.findViewById(R.id.textView_benefit2);
            TextView textView_benefit3 = (TextView) tempLinearLayout0.findViewById(R.id.textView_benefit3);
            TextView textView_benefit4 = (TextView) tempLinearLayout0.findViewById(R.id.textView_benefit4);
            textView_benefit1.setText(getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(productVO.getPoint())));
            textView_benefit2.setText(getString(R.string.goods_amount).replace("{NOW}", productVO.getAmount_now()));
            textView_benefit3.setText(productVO.getComponent());
            textView_benefit4.setText(productVO.getNote());

            view.addView(tempLinearLayout0);

        } else {
            layout_goods.setVisibility(View.GONE);
            layout_freeSend.setVisibility((View.VISIBLE));
        }

        //특전
        try {
            JSONArray jsonArray2 = total_Json.getJSONArray("product_terms");
            for (int i = 0; i < jsonArray2.length(); i++) {

                ProductVO productVO = new ProductVO();
                productVO.setSrl(jsonArray2.getJSONObject(i).getString("term_srl"));
                productVO.setName(jsonArray2.getJSONObject(i).getString("name"));
                productVO.setComponent(jsonArray2.getJSONObject(i).getString("component"));
                productVO.setNote(jsonArray2.getJSONObject(i).getString("note"));
                productVO.setBegin_time(jsonArray2.getJSONObject(i).getString("begin_time"));
                productVO.setClose_time(jsonArray2.getJSONObject(i).getString("close_time"));

                Log.d("기간특전",productVO.getComponent());
                String closeDate = productVO.getClose_time();
                String sToday = Utils.getTime();
                int change = Utils.GetDifferenceOfDate(closeDate, sToday);
                if (change >= 0) {

                    LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout tempLinearLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.list_payment_goods1, null, false);
                    //구분선
                    ImageView imageView = (ImageView) tempLinearLayout.findViewById(R.id.imageView_line);
                    imageView.setBackgroundResource(R.drawable.short_line);
                    //특전 추가
                    TextView textView_title1 = (TextView) tempLinearLayout.findViewById(R.id.textView_title);
                    textView_title1.setTextColor(Color.parseColor("#000000"));
                    TextView textView_sub1 = (TextView) tempLinearLayout.findViewById(R.id.textView_content);
                    textView_sub1.setTextColor(Color.parseColor("#c0c0c0"));
                    textView_title1.setText(productVO.getName());
                    textView_sub1.setText(productVO.getComponent());

                    view.addView(tempLinearLayout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //마음 내역=========================
        //구매수량
        TextView textView_goods_total = (TextView) findViewById(R.id.goods_total);
        textView_goods_total.setText(productVO.getGoodsMax());
        //총 상품금액

        int goodsCost = Utils.stringToInt(productVO.getGoodsMax()) * Utils.stringToInt(productVO.getPoint());
        TextView textView_goods_total_value = (TextView) findViewById(R.id.goods_total_value);
        textView_goods_total_value.setText(getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(String.valueOf(goodsCost))));

        //배송비
        TextView textView_delivery_value = (TextView) findViewById(R.id.delivery_value);

        GoodsEmsVO goodsEmsVO = null;

        if (productVO.getSrl() != null) {
            goodsEmsVO = (GoodsEmsVO) FanMindApplication.arrayCountryData.get(Utils.stringToInt(productVO.getGoodsDelivery()));
        } else {//후원하기 일때
            goodsEmsVO = new GoodsEmsVO();
        }

        int tempDeliveryCost = Utils.stringToInt(goodsEmsVO.getCost());
        int tempOverCost = Utils.stringToInt(productVO.getGoods_ship_over_cost_());

        tempDeliveryCost += tempOverCost;
        textView_delivery_value.setText(getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(String.valueOf(tempDeliveryCost))));

        /**************************
         * 후원하기
         ************************/
        //총 필요 마음
        TextView textView_sum_maum = (TextView) findViewById(R.id.sum_maum);
        textView_sum_maum.setText(getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(Utils.intToString(tempDeliveryCost + goodsCost))));//goods maum
        TextView textView_payment_total_maum = (TextView) findViewById(R.id.payment_total_maum);
        textView_payment_total_maum.setText(getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(Utils.intToString(tempDeliveryCost + goodsCost))));//free maum

        Button searchAddress = (Button) findViewById(R.id.button_attends_address_search);
        if (searchAddress != null) {
            searchAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectPayment2.this,AlertAddressWebView.class);
                    startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
/*                    alertAddressSearch = new AlertAddressSearch(ProjectPayment2.this, new ResultInterface() {
                        @Override
                        public Integer onResult(Object resultValue) {
                            alertAddressSearch.getDialog().cancel();
                            edittext_addess1.setText(((String) resultValue).trim());
                            return null;
                        }
                    });
                    alertAddressSearch.show(getSupportFragmentManager(), "alertAddressSearch");*/
                }
            });
        }

        if (!Utils.getLanquageLocal(this).equals("ko_KR")) {
            edittext_addess1.setEnabled(true);
            assert searchAddress != null;
            searchAddress.setVisibility(View.GONE);

        }
        TextView textView_final_payment = (TextView) findViewById(R.id.final_payment);
        textView_final_payment.setText(getString(R.string.maum_num).replace("{VALUE}", productVO.getGoods_total_cost()));
        TextView textView_final_payment2 = (TextView) findViewById(R.id.final_payment2);
        textView_final_payment2.setText("(" + getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(Utils.getMaum2Money(ProjectPayment2.this, productVO.getGoods_total_cost()))) + ")");

        // 사용 후 남은 마음

        int remainMaum = Utils.stringToInt(FanMindSetting.getMY_HEART(ProjectPayment2.this)) - (tempDeliveryCost + goodsCost);
        ((TextView) findViewById(R.id.my_maum)).setText(getString(R.string.maum_num).replace("{VALUE}", (Utils.getMoney(String.valueOf(remainMaum)))));
        ((TextView) findViewById(R.id.my_maum_won)).setText("(" + getString(R.string.maum_num).replace("{VALUE}", (Utils.getMoney(Utils.getMaum2Money(ProjectPayment2.this, String.valueOf(remainMaum))))) + ")");
        ((TextView) findViewById(R.id.my_maum2)).setText(getString(R.string.maum_num).replace("{VALUE}", (Utils.getMoney(String.valueOf(remainMaum)))));
        ((TextView) findViewById(R.id.my_maum2_won)).setText("(" + getString(R.string.mypoint_tv06).replace("{PRICE}", (Utils.getMoney(Utils.getMaum2Money(ProjectPayment2.this, String.valueOf(remainMaum))))) + ")");

        ((TextView) findViewById(R.id.join_comment)).setText(Html.fromHtml(getString(R.string.join_comment)));

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

        if (productVO.getSrl() != null) {
            textView_name.setError(null);
            textView_phoneNumber1.setError(null);
            textView_phoneNumber2.setError(null);
            textView_phoneNumber3.setError(null);
            textView_email.setError(null);
            edittext_addess1.setError(null);
            edittext_addess2.setError(null);
//            textView_payment_type.setError(null);

            String phoneNumber = textView_phoneNumber1.getText().toString() + textView_phoneNumber2.getText().toString() + textView_phoneNumber3.getText().toString();
            if (Utils.checkLength(textView_name)) {
                textView_name.setError(getString(R.string.please_input_data));
                textView_name.requestFocus();
            } else if (!Utils.isEmailValid(textView_email.getText().toString())) {
                textView_email.setError(getString(R.string.please_input_data));
                textView_email.requestFocus();
            } else if (Utils.checkLength(edittext_addess1)) {
                edittext_addess1.setError(getString(R.string.please_input_data));
                edittext_addess1.requestFocus();
            } else if (Utils.checkLength(edittext_addess2)) {
                edittext_addess2.setError(getString(R.string.please_input_data));
                edittext_addess2.requestFocus();
            }
//            else if (Utils.checkLength(textView_payment_type)) {
//                textView_payment_type.setError(getString(R.string.please_input_data));
//            }
            else {

                List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                mParams = Utils.setSession(ProjectPayment2.this, mParams);
                mParams.add(new BasicNameValuePair("project_srl", Utils.getJsonData(total_Json.toString(), "project_srl")));
                mParams.add(new BasicNameValuePair("product_srl", productVO.getSrl()));
                mParams.add(new BasicNameValuePair("quantity", productVO.getGoodsMax()));

                int tempInt = Integer.parseInt(productVO.getGoodsDelivery());
                String strLocation = "DOMESTIC";
                if (tempInt == 0) {
                    strLocation = "DOMESTIC";
                } else if (tempInt == 1) {
                    strLocation = "DOMESTIC_EXTRA";
                } else {
                    strLocation = ((GoodsEmsVO) FanMindApplication.arrayCountryData.get(tempInt)).getRegion();
                }
                mParams.add(new BasicNameValuePair("location", strLocation));

                mParams.add(new BasicNameValuePair("point_support", ""));
                mParams.add(new BasicNameValuePair("point_use", productVO.getGoods_total_cost()));

                HttpRequest.setHttp1(ProjectPayment2.this, URLAddress.PROJECT_ATTEND(), mParams, new OnTask() {
                            @Override
                            public void onTask(int output, String result) throws JSONException {

                                if (Utils.getJsonData(result, "code").equals("ATTENDED")) {

                                    //배송정보 입력
                                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                                    mParams = Utils.setSession(ProjectPayment2.this, mParams);
                                    mParams.add(new BasicNameValuePair("order_srl", Utils.getJsonData(Utils.getJsonData(result, "data"), "order_srl")));
                                    mParams.add(new BasicNameValuePair("name", textView_name.getText().toString()));
                                    mParams.add(new BasicNameValuePair("mobile", textView_phoneNumber1.getText().toString() + textView_phoneNumber2.getText().toString() + textView_phoneNumber3.getText().toString()));
                                    mParams.add(new BasicNameValuePair("email", textView_email.getText().toString().trim()));
                                    mParams.add(new BasicNameValuePair("is_global", Utils.getJsonData(total_Json.toString(), "is_ship_global").equals("null") ? "N" : Utils.getJsonData(total_Json.toString(), "is_ship_global")));
                                    String[] addrPri = edittext_addess1.getText().toString().split(", ");
                                    if(addrPri.length == 2) {
                                        mParams.add(new BasicNameValuePair("address_pri", addrPri[1]));
                                        mParams.add(new BasicNameValuePair("zipcode", addrPri[0]));
                                    }else {
                                        mParams.add(new BasicNameValuePair("address_pri", edittext_addess1.getText().toString()));
                                    }
                                    mParams.add(new BasicNameValuePair("address_sec", edittext_addess2.getText().toString()));

                                    mParams.add(new BasicNameValuePair("note", attends_etc1.getText().toString()));
                                    mParams.add(new BasicNameValuePair("note_extra", attends_etc2.getText().toString()));
                                    HttpRequest.setHttp1(ProjectPayment2.this, URLAddress.PROJECT_ATTEND_MODIFY(), mParams, new OnTask() {
                                                @Override
                                                public void onTask(int output, String result) throws JSONException {

                                                    if (Utils.getJsonData(result, "code").equals("UPDATED")) {
                                                        //결제 후 이동
                                                        Intent intent = new Intent(getApplicationContext(), ProjectPayment3.class);
                                                        intent.putExtra("total_Json", total_Json.toString());
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Utils.setToast(ProjectPayment2.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                                                    }
                                                }
                                            }
                                    );

                                } else {
                                    Utils.setToast(ProjectPayment2.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                                }
                            }
                        }
                );


            }
        } else {//후원하기

            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams = Utils.setSession(ProjectPayment2.this, mParams);
            mParams.add(new BasicNameValuePair("project_srl", Utils.getJsonData(total_Json.toString(), "project_srl")));
            mParams.add(new BasicNameValuePair("point_support", productVO.getGoods_total_cost()));
            mParams.add(new BasicNameValuePair("point_use", productVO.getGoods_total_cost()));

            HttpRequest.setHttp1(ProjectPayment2.this, URLAddress.PROJECT_ATTEND(), mParams, new OnTask() {
                        @Override
                        public void onTask(int output, String result) throws JSONException {

                            if (Utils.getJsonData(result, "code").equals("ATTENDED")) {
                                //결제 후 이동
                                Intent intent = new Intent(getApplicationContext(), ProjectPayment3.class);
                                intent.putExtra("total_Json", total_Json.toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Utils.setToast(ProjectPayment2.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                            }
                        }
                    }
            );

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {

            case SEARCH_ADDRESS_ACTIVITY:
                try{
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        edittext_addess1.setText(data);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;


        }
    }
}