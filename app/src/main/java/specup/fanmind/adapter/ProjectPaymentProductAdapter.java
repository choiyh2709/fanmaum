package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.GoodsEmsVO;
import specup.fanmind.vo.ProductVO;


public class ProjectPaymentProductAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private TextView textView_sumCost;
    private ProductVO getViewProductVO;
//    private CheckBox checkbox_free_maum;

    public ProjectPaymentProductAdapter(Context context, List<Object> list, View.OnClickListener onClickListener, TextView textView_sumCost) {
        mContext = context;
        mList = list;
        this.onClickListener = onClickListener;
        this.textView_sumCost = textView_sumCost;
//        this.checkbox_free_maum = checkbox_free_maum;
    }


    public void add(CommentList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    Holder holder;

    private int selectItemIndex = -1;
    private View convertView;

    public int getSelectItemIndex() {
        return selectItemIndex;
    }

    public void setSelectItemIndex(int selectItem) {
        selectItemIndex = selectItem;

        if (selectItemIndex == -1) {
            for (int i = 0; i < mList.size(); i++) {
                ProductVO checkedChangedProductVO = (ProductVO) getItem(i);
                checkedChangedProductVO.setIsSelected("false");
            }
        }
        notifyDataSetChanged();
        ;
    }


    private void openLayoutAnimation(final LinearLayout tempLinearLayout) {
        if (tempLinearLayout.getVisibility() == View.GONE) {
            Animation animation = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.ani_scale_view);
            tempLinearLayout.startAnimation(animation);
            tempLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void closeLayoutAnimation(final LinearLayout tempLinearLayout) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.ani_scale_unview);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tempLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tempLinearLayout.startAnimation(animation);
    }


    /**
     * 구성상품 선택 초기화
     * @param position
     */
    private void setmList(int position) {
        //
        for (int i = 0; i < getCount(); i++) {
            ProductVO onClickProductVO = (ProductVO) getItem(i);
            if (position == i) {
                if (onClickProductVO.getIsSelected() == null || onClickProductVO.getIsSelected().equals("true")) {
                    onClickProductVO.setIsSelected("false");

                    selectItemIndex = -1;
                    textView_sumCost.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", "0"));

                } else {
                    onClickProductVO.setIsSelected("true");
                    selectItemIndex = position;
                }

            } else {

                onClickProductVO.setIsSelected("false");
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        holder = new Holder();

        this.convertView = convertView;
        getViewProductVO = (ProductVO) mList.get(position);

        LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layout.inflate(R.layout.viewpager_product, null);

        holder.textView_benefit1 = (TextView) convertView.findViewById(R.id.textView_benefit1);
        holder.textView_benefit2 = (TextView) convertView.findViewById(R.id.textView_benefit2);
        holder.textView_benefit3 = (TextView) convertView.findViewById(R.id.textView_benefit3);
        holder.textView_benefit4 = (TextView) convertView.findViewById(R.id.textView_benefit4);
        holder.icon = (TextView) convertView.findViewById(R.id.icon);

        holder.layout_payment_detail = ((LinearLayout) convertView.findViewById(R.id.layout_payment_detail));
        holder.layout_bottom = ((LinearLayout) convertView.findViewById(R.id.layout_bottom));

        //select item
        holder.icon2 = (CheckBox) convertView.findViewById(R.id.icon2);
        holder.icon2.setTag(holder.layout_payment_detail);
        holder.icon2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout linearLayout = (LinearLayout) buttonView.getTag();
                ProductVO checkedChangedProductVO = (ProductVO) getItem(position);

                if (isChecked) {

//                    checkbox_free_maum.setChecked(false);
                    if (selectItemIndex == position) {
                        if (checkedChangedProductVO.getIsSelected().equals("true")) {
                            linearLayout.setVisibility(View.VISIBLE);
                        } else {
                            openLayoutAnimation(linearLayout);
                        }
                    } else {
                        closeLayoutAnimation(linearLayout);
                    }

                } else {
                    closeLayoutAnimation(linearLayout);
                }
            }
        });
        holder.icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmList(position);
                notifyDataSetChanged();
            }
        });


        holder.layout_title = ((LinearLayout) convertView.findViewById(R.id.layout_title));
        holder.layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.icon2.isChecked()) {
                    holder.icon2.setChecked(false);
                } else {
                    holder.icon2.setChecked(true);
                }

                setmList(position);

                notifyDataSetChanged();
            }
        });

        if (getViewProductVO.getIsSelected().equals("true")) {
            holder.layout_bottom.setBackgroundResource(R.drawable.payment_list_bottom);
            holder.icon2.setChecked(true);
        } else {
            holder.layout_bottom.setBackgroundResource(R.drawable.a1100_2_benefit_background_bottom_normal);
            holder.icon2.setChecked(false);
        }


        // 몇명 참여 했는지
        try {
            int amount_now = 0;
            int amount_max = 0;
            try {
                amount_now = Utils.stringToInt(getViewProductVO.getAmount_now());
                amount_max = Utils.stringToInt(getViewProductVO.getAmount_max());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int tempAmount = amount_max - amount_now;

            if (amount_max != 0 && tempAmount <= 0) {
                holder.icon.setVisibility(View.VISIBLE);
            } else {
                holder.icon2.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.textView_benefit1.setText(mContext.getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(getViewProductVO.getPoint())));
        holder.textView_benefit2.setText(mContext.getString(R.string.goods_amount).replace("{NOW}", getViewProductVO.getAmount_now()));
        holder.textView_benefit3.setText(getViewProductVO.getComponent());
        holder.textView_benefit4.setText(getViewProductVO.getNote());


        //구매수량 클릭
        try {
            TextView textView_amount_value = (TextView) convertView.findViewById(R.id.textView_amount_value);
            textView_amount_value.setText(getViewProductVO.getGoodsMax() == null ? "0" : getViewProductVO.getGoodsMax());

            textView_amount_value.setTag(getViewProductVO);
            textView_amount_value.setOnClickListener(onClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //배송정보 클릭
            TextView textView_delivery_value = (TextView) convertView.findViewById(R.id.textView_delivery_value);
            textView_delivery_value.setOnClickListener(onClickListener);
            if (getViewProductVO.getGoodsDelivery() != null) {
                int int_resultValue = Utils.stringToInt(getViewProductVO.getGoodsDelivery());
                GoodsEmsVO goodsEmsVO = (GoodsEmsVO) FanMindApplication.arrayCountryData.get(int_resultValue);
                textView_delivery_value.setText(goodsEmsVO.getRegion());

                textView_delivery_value.setTag(getViewProductVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //추가배송비
            TextView textView_add_deilvey_cost = (TextView) convertView.findViewById(R.id.textView_add_deilvey_cost);
            if (getViewProductVO.getGoodsMax() != null) {
                int selectGoodsMax = Utils.stringToInt(getViewProductVO.getGoodsMax());
                int ship_over_unit = Utils.stringToInt(getViewProductVO.getShip_over_unit());

                int resultOverUnit = selectGoodsMax - ship_over_unit;

                if (resultOverUnit > 0) {
                    int ship_over_cost = Utils.stringToInt(getViewProductVO.getShip_over_cost());

                    int overCost = resultOverUnit * ship_over_cost;
                    getViewProductVO.setGoods_ship_over_cost_(String.valueOf(overCost));
                    textView_add_deilvey_cost.setText(String.valueOf(overCost));
                } else {
                    getViewProductVO.setGoods_ship_over_cost_("0");
                    textView_add_deilvey_cost.setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            //총합.==============================
            if (selectItemIndex > -1) {
                try {
                    ProductVO selectItemIndexProductVO = (ProductVO) getItem(selectItemIndex);

                    int goods_cost = 0;
                    int goods_Max = 0;
                    String deliveryIndex = "0";
                    int deliveryCost = 0;
                    int overCost = 0;
                    int totalCost = 0;

                    try {
                        goods_cost = Utils.stringToInt(selectItemIndexProductVO.getPoint());
                        goods_Max = Utils.stringToInt(selectItemIndexProductVO.getGoodsMax());
                        deliveryIndex = selectItemIndexProductVO.getGoodsDelivery();
                        if (deliveryIndex != null) {
                            if (deliveryIndex.equals("0")) {
                                deliveryCost = Utils.stringToInt(selectItemIndexProductVO.getShip_cost());
                            } else if (deliveryIndex.equals("1")) {
                                deliveryCost = Utils.stringToInt(selectItemIndexProductVO.getShip_cost_extra());
                            } else {
                                GoodsEmsVO goodsEmsVO = (GoodsEmsVO) FanMindApplication.arrayCountryData.get(Utils.stringToInt(deliveryIndex));
                                deliveryCost = Utils.stringToInt(goodsEmsVO.getCost());

                            }
                        }
                        overCost = Utils.stringToInt(selectItemIndexProductVO.getGoods_ship_over_cost_());
                        totalCost = (goods_cost * goods_Max) + deliveryCost + overCost;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getViewProductVO.setGoods_total_cost(String.valueOf(totalCost));
                    textView_sumCost.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(String.valueOf(totalCost))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    public class Holder {
        CheckBox icon2;
        TextView textView_benefit1, textView_benefit2, textView_benefit3, textView_benefit4, icon;

        LinearLayout layout_payment_detail, layout_title, layout_bottom;
    }


//    private int getDeliveryCost(String result) {
//
//        int returnValue = 0;
//        try {
//            final JSONArray internationalDeliveryImformation = ProjectPayment.internationalDeliveryImformation;;
//
//            List<Object> arrayList = new ArrayList<Object>();
//            for (int i = 0; i < internationalDeliveryImformation.length(); i++) {
//                JSONObject tempjsonObject = null;
//                    tempjsonObject = internationalDeliveryImformation.getJSONObject(i);
//                    if (tempjsonObject.getString("region").equals( result )) {
//
//                        if(tempjsonObject.getString("weight").equals(getViewProductVO.getShip_weight() )){
//                            returnValue = Integer.valueOf(tempjsonObject.getString("cost"));
//                        }
//                    }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return returnValue;
//    }
}
