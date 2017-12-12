package specup.fanmind.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.ProductVO;


public class HomeFragment_detail_products_pageAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    View.OnClickListener onClickListener;
    List<Object> list;

    public HomeFragment_detail_products_pageAdapter(Context context, List<Object> list, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.viewpager_product, container, false);
        view.setOnClickListener( onClickListener);

        ((LinearLayout)view.findViewById(R.id.layout_title)).setClickable(false);

        TextView textView_benefit1 = (TextView) view.findViewById(R.id.textView_benefit1);
        TextView textView_benefit1_1 = (TextView) view.findViewById(R.id.textView_benefit1_1);
        TextView textView_benefit2 = (TextView) view.findViewById(R.id.textView_benefit2);
        TextView textView_benefit3 = (TextView) view.findViewById(R.id.textView_benefit3);
        TextView textView_benefit4 = (TextView) view.findViewById(R.id.textView_benefit4);
        TextView icon = (TextView) view.findViewById(R.id.icon);


        ProductVO productVO = (ProductVO) list.get(position);

        int amount_now = 0;
        int amount_max = 0;
        try {
            amount_now = Integer.valueOf(productVO.getAmount_now());
            amount_max = Integer.valueOf(productVO.getAmount_max());
        }catch (Exception e){
            e.printStackTrace();
        }
        int tempAmount = amount_max - amount_now;
        if(amount_max != 0 && tempAmount <= 0){
            icon.setVisibility(View.VISIBLE);
        }
        textView_benefit1.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(productVO.getPoint())));
        textView_benefit1_1.setText("("+mContext.getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(Utils.getMaum2Money(mContext,productVO.getPoint())))+")");
        textView_benefit2.setText(mContext.getString(R.string.goods_amount).replace("{NOW}", productVO.getAmount_now()));
        textView_benefit3.setText(productVO.getComponent());
        if(!productVO.getNote().equals("null")) textView_benefit4.setText(productVO.getNote());
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
