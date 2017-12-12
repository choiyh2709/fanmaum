package specup.fanmind.main.tab0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

import specup.fanmind.R;
import specup.fanmind.adapter.HomeFragment_detail_product_terms_pageAdapter;
import specup.fanmind.adapter.ProjectPaymentProductAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.AdapterLinearLayout;
import specup.fanmind.common.Util.CirclePageIndicator;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.BuyPointWeViewActivity;
import specup.fanmind.vo.GoodsEmsVO;
import specup.fanmind.vo.ProductVO;

/**
 * 프로젝트 참여(결제 1)
 */
public class ProjectPayment extends Activity {

    JSONObject total_Json;
    ProjectPaymentProductAdapter projectPaymentProductAdapter;
    AdapterLinearLayout adapterLinearLayout;
    TextView textView_exchange_money;
    EditText edittext_free_maum;
    TextView textView_sumCost,tv_min_hearts;
    private String min_hearts;
//    CheckBox checkbox_free_maum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_payment);
        ActivityManager.getInstance().addActivity(this);

        try {
            total_Json = new JSONObject(getIntent().getStringExtra("total_Json"));
            min_hearts = total_Json.get("heart_min").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getNetworkInfo();
        setView();
    }

    private void setFreeMaum() {
        if (projectPaymentProductAdapter != null)
            projectPaymentProductAdapter.setSelectItemIndex(-1);

        String string_maum = edittext_free_maum.getText().toString();
        if (string_maum.equals("")) {
            textView_exchange_money.setText(getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMaum2Money(ProjectPayment.this, "0")));
            textView_sumCost.setText(getString(R.string.maum_num).replace("{VALUE}", "0"));
        } else {
            textView_exchange_money.setText(getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMaum2Money(ProjectPayment.this, edittext_free_maum.getText().toString())));
            textView_sumCost.setText(getString(R.string.maum_num).replace("{VALUE}", string_maum));
        }
    }


    private void setView() {

        //보유마음
        TextView textView_myMaum = (TextView) findViewById(R.id.textView_myMaum);
        textView_myMaum.setText(getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(FanMindSetting.getMY_HEART(this))));
        // 총합
        textView_sumCost = (TextView) findViewById(R.id.textView_sumCost);
        textView_sumCost.setText(getString(R.string.maum_num).replace("{VALUE}", "0"));


        //마음 보내기
//        checkbox_free_maum = (CheckBox) findViewById(R.id.checkbox_free_maum);
        tv_min_hearts = (TextView) findViewById(R.id.tv_min_hearts);
        String temp = tv_min_hearts.getText().toString();
        String temp2 = temp.replace("100",min_hearts);
        tv_min_hearts.setText(temp2);
        edittext_free_maum = (EditText) findViewById(R.id.edittext_free_maum);
        edittext_free_maum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    checkbox_free_maum.setChecked(true);
                    setFreeMaum();
                }
                return false;
            }
        });
        textView_exchange_money = (TextView) findViewById(R.id.textView_exchange_money);
        textView_exchange_money.setText(getString(R.string.mypoint_tv06).replace("{PRICE}", "0"));
//        checkbox_free_maum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    setFreeMaum();
//                    if (projectPaymentProductAdapter != null)
//                        projectPaymentProductAdapter.setSelectItemIndex(-1);
//                }
//            }
//        });
        edittext_free_maum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setFreeMaum();
            }
        });


        //상품구성2
        try {
            JSONArray jsonArray4 = total_Json.getJSONArray("products");
            List<Object> productList = new ArrayList<Object>();
            for (int i = 0; i < jsonArray4.length(); i++) {
                ProductVO productVO = new ProductVO();

                try {
                    JSONObject jsonObject = jsonArray4.getJSONObject(i);
                    productVO.setSrl(Utils.getJsonData(jsonObject.toString(), "product_srl"));
                    productVO.setName(Utils.getJsonData(jsonObject.toString(), "name"));
                    productVO.setComponent(Utils.getJsonData(jsonObject.toString(), "component"));
                    productVO.setNote(Utils.getJsonData(jsonObject.toString(), "note"));
                    productVO.setPoint(Utils.getJsonData(jsonObject.toString(), "point"));
                    productVO.setAmount_max(Utils.getJsonData(jsonObject.toString(), "amount_max"));
                    productVO.setAmount_now(Utils.getJsonData(jsonObject.toString(), "amount_now"));
                    productVO.setShip_weight(Utils.getJsonData(total_Json.toString(), "ship_weight"));
                    productVO.setIs_ship_global(Utils.getJsonData(total_Json.toString(), "is_ship_global"));
                    productList.add(productVO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //상품 아이템 구성.
            projectPaymentProductAdapter = new ProjectPaymentProductAdapter(this, productList, onClickListener, textView_sumCost);
            adapterLinearLayout = (AdapterLinearLayout) findViewById(R.id.goodsList);
            adapterLinearLayout.setOrientation(LinearLayout.VERTICAL);
            adapterLinearLayout.setAdapter(projectPaymentProductAdapter);
        } catch (Exception e) {
            ((LinearLayout) findViewById(R.id.layout_goods)).setVisibility(View.GONE);
            e.printStackTrace();
        }

        //기간 특전
        try {
            JSONArray jsonArray2 = total_Json.getJSONArray("product_terms");

            if (jsonArray2.length() > 0) {
                ((LinearLayout) findViewById(R.id.layout_benefit)).setVisibility(View.VISIBLE);
                List<Object> productList = new ArrayList<Object>();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    ProductVO productVO = new ProductVO();
                    try {
                        JSONObject jsonObject = jsonArray2.getJSONObject(i);
                        productVO.setSrl(Utils.getJsonData(jsonObject.toString(), "product_srl"));
                        productVO.setName(Utils.getJsonData(jsonObject.toString(), "name"));
                        productVO.setComponent(Utils.getJsonData(jsonObject.toString(), "component"));
                        productVO.setNote(Utils.getJsonData(jsonObject.toString(), "note"));
                        productVO.setBegin_time(Utils.getJsonData(jsonObject.toString(), "begin_time"));
                        productVO.setClose_time(Utils.getJsonData(jsonObject.toString(), "close_time"));
                        productList.add(productVO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ViewPager viewPager_benefit = (ViewPager) findViewById(R.id.viewPager_benefit);
                HomeFragment_detail_product_terms_pageAdapter mHomeFragment_detail_product_terms_pageAdapter = new HomeFragment_detail_product_terms_pageAdapter(getApplicationContext(), productList);
                viewPager_benefit.setAdapter(mHomeFragment_detail_product_terms_pageAdapter);
                CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator1);
                circlePageIndicator.setViewPager(viewPager_benefit);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBack(View v) {
        onBackPressed();
    }

    /**
     * [다음 ] 버튼 눌렸을때
     *
     * @param v
     */
    CustomDialog customDialog;

    public void onNext(View v) {
        int sumcost = Utils.stringToInt(textView_sumCost.getText().toString().replace(getString(R.string.maum), "").trim().replace(",", ""));
        int myHeart = Utils.stringToInt(FanMindSetting.getMY_HEART(this));
        int point_min =Utils.stringToInt( Utils.getJsonData(total_Json.toString(),"point_min") );

        if( sumcost < point_min){//최소 참여마음 체크
            Utils.setToast(ProjectPayment.this, getString(R.string.point_min));
            return;
        }


        if ((projectPaymentProductAdapter ==null || projectPaymentProductAdapter.getSelectItemIndex() == -1  )) {// 마음 보내기를 선택했을경우

            if (sumcost <= myHeart) {


                ProductVO productVO = new ProductVO();
                productVO.setGoodsMax("1");
                productVO.setPoint(edittext_free_maum.getText().toString());
                productVO.setGoods_total_cost(edittext_free_maum.getText().toString());

                Intent intent = new Intent(getApplicationContext(), ProjectPayment2.class);
                intent.putExtra("ProductVo", productVO);
                intent.putExtra("total_Json", total_Json.toString());
                startActivity(intent);
                finish();
            } else {
                //마음 충전 얼럿창.
                customDialog = new CustomDialog(this, getString(R.string.not_enough_title), getString(R.string.not_enough_content)
                        , new View.OnClickListener() {//취소
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                }
                        , new View.OnClickListener() {//적립로 이동
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProjectPayment.this, BuyPointWeViewActivity.class));
                    }
                }
                        , getString(R.string.cancel)
                        , getString(R.string.not_enough_comform));
                customDialog.show();
            }
        } else if (projectPaymentProductAdapter != null && (projectPaymentProductAdapter.getSelectItemIndex() != -1 && sumcost > 0)) {// 구성 상품을 선택했을경우
            if (sumcost <= myHeart) {
                Intent intent = new Intent(getApplicationContext(), ProjectPayment2.class);
                intent.putExtra("ProductVo", (ProductVO) projectPaymentProductAdapter.getItem(projectPaymentProductAdapter.getSelectItemIndex()));
                intent.putExtra("total_Json", total_Json.toString());
                Log.d("total_Json",total_Json.toString());
                startActivity(intent);
                finish();
            } else {

                //마음 충전 얼럿창.
                customDialog = new CustomDialog(this, getString(R.string.not_enough_title), getString(R.string.not_enough_content)
                        , new View.OnClickListener() {//취소
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                }
                        , new View.OnClickListener() {//적립로 이동
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProjectPayment.this, BuyPointWeViewActivity.class));
                    }
                }
                        , getString(R.string.cancel)
                        , getString(R.string.not_enough_comform));
                customDialog.show();
            }

        } else {
            Utils.setToast(ProjectPayment.this, getString(R.string.confirm_goods));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.getInstance().deleteActivity(this);
        finish();
    }

    ProductVO amount_valueProduct;
    ProductVO delivery_valueProduct;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.textView_amount_value: {// 구매수량
                    amount_valueProduct = (ProductVO) projectPaymentProductAdapter.getItem(projectPaymentProductAdapter.getSelectItemIndex());

                    try {
                        amount_valueProduct.setShip_over_unit(total_Json.getString("ship_over_unit"));
                        amount_valueProduct.setShip_over_cost(total_Json.getString("ship_over_cost"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AlertDialogPaymentProductMaximum alertDialogPaymentProductMaximum = new AlertDialogPaymentProductMaximum(ProjectPayment.this, amount_valueProduct, getString(R.string.goods_total), new ResultInterface() {

                        @Override
                        public Integer onResult(Object resultValue) {
                            int int_resultValue = (int) resultValue;

                            amount_valueProduct.setGoodsMax(String.valueOf(int_resultValue + 1));

                            projectPaymentProductAdapter.notifyDataSetChanged();
                            return null;
                        }
                    });
                    alertDialogPaymentProductMaximum.show(getFragmentManager(), "alertDialogPaymentProductMaximum");
                    break;
                }


                case R.id.textView_delivery_value: {// 배송정보
                    if (FanMindSetting.getLOGIN_OK(ProjectPayment.this)) {
                        delivery_valueProduct = (ProductVO) projectPaymentProductAdapter.getItem(projectPaymentProductAdapter.getSelectItemIndex());

                        try {
                            delivery_valueProduct.setShip_cost(total_Json.getString("ship_cost"));
                            delivery_valueProduct.setIs_ship_cost_extra(total_Json.getString("is_ship_cost_extra"));
                            delivery_valueProduct.setShip_cost_extra(total_Json.getString("ship_cost_extra"));
                            delivery_valueProduct.setShip_weight(total_Json.getString("ship_weight"));

                            AlertDialogPaymentDeliveryImformation alertDialogPaymentDeliveryImformation = new AlertDialogPaymentDeliveryImformation(
                                    ProjectPayment.this
                                    , delivery_valueProduct
                                    , getString(R.string.goods_delivery_address)
                                    , FanMindApplication.arrayCountryData
                                    , new ResultInterface() {
                                @Override
                                public Integer onResult(Object resultValue) {

                                    GoodsEmsVO goodsEmsVO = (GoodsEmsVO) FanMindApplication.arrayCountryData.get((int) resultValue);
                                    delivery_valueProduct.setGoodsDelivery(String.valueOf(resultValue));
                                    projectPaymentProductAdapter.notifyDataSetChanged();
                                    return null;
                                }
                            });
                            alertDialogPaymentDeliveryImformation.show(getFragmentManager(), "alertDialogPaymentDeliveryImfocost");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Utils.showDialog(ProjectPayment.this);
                    }
                    break;
                }
            }
        }
    };

    /**
     * ems 정보 수집.
     */
    public static JSONArray internationalDeliveryImformation;

    private void getNetworkInfo() {
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(this, mParams);
        mParams.add(new BasicNameValuePair("project_srl", Utils.getJsonData(total_Json.toString(), "project_srl")));
        mParams.add(new BasicNameValuePair("qunatity", "1"));
        mParams.add(new BasicNameValuePair("product_srl", "55"));//삭제 예정.
        mParams.add(new BasicNameValuePair("location", Utils.getLanquageLocal(ProjectPayment.this)));

        HttpRequest.setHttp1(this, URLAddress.PROJECT_ATTEND_CHECK(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, final String result) throws JSONException {

                        if (Utils.getJsonData(result, "code").equals("SUCCESS")) {

                            internationalDeliveryImformation = new JSONArray(Utils.getJsonData(Utils.getJsonData(result, "data"), "locations"));

                            FanMindApplication.arrayCountryData = new ArrayList<Object>();

                            GoodsEmsVO goodsEmsVO = new GoodsEmsVO();
                            goodsEmsVO.setRegion(String.valueOf(getString(R.string.default_delivery)));
                            goodsEmsVO.setCost(total_Json.getString("ship_cost"));
                            goodsEmsVO.setCost_won(Utils.getMaum2Money(ProjectPayment.this, Utils.getJsonData(total_Json.toString(),"ship_cost")));
                            FanMindApplication.arrayCountryData.add(goodsEmsVO);

                            if (total_Json.getString("is_ship_cost_extra").equalsIgnoreCase("Y")) {
                                GoodsEmsVO goodsEmsVO1 = new GoodsEmsVO();
                                goodsEmsVO1.setRegion(String.valueOf(getString(R.string.jeju_delivery)));
                                goodsEmsVO1.setCost(total_Json.getString("ship_cost_extra"));
                                goodsEmsVO1.setCost_won(Utils.getMaum2Money(ProjectPayment.this, total_Json.getString("ship_cost_extra")));
                                FanMindApplication.arrayCountryData.add(goodsEmsVO1);
                            }

                            if (Utils.getJsonData(total_Json.toString(), "is_ship_global").equals("Y")) {

                                for (int i = 1; i < internationalDeliveryImformation.length(); i++) {
                                    JSONObject tempjsonObject = internationalDeliveryImformation.getJSONObject(i);
                                    GoodsEmsVO goodsEmsVO2 = new GoodsEmsVO();
                                    goodsEmsVO2.setRegion(tempjsonObject.getString("location"));
                                    goodsEmsVO2.setCost(tempjsonObject.getString("point"));
                                    goodsEmsVO2.setCost_won(tempjsonObject.getString("currency"));
                                    FanMindApplication.arrayCountryData.add(goodsEmsVO2);
                                }
                            }


                        }
                    }
                }
        );
    }
}