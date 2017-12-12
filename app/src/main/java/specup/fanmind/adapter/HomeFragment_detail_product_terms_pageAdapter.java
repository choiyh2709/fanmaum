package specup.fanmind.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.ProductVO;


public class HomeFragment_detail_product_terms_pageAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    List<Object> list;

    public HomeFragment_detail_product_terms_pageAdapter(Context context, List<Object> list) {
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

        View view = mLayoutInflater.inflate(R.layout.viewpager_product_terms, container, false);
        TextView textView_benefit1 = (TextView) view.findViewById(R.id.textView_benefit1);
        TextView textView_benefit2 = (TextView) view.findViewById(R.id.textView_benefit2);
        TextView textView_benefit3 = (TextView) view.findViewById(R.id.textView_benefit3);
        TextView textView_benefit4 = (TextView) view.findViewById(R.id.textView_benefit4);
        TextView icon = (TextView) view.findViewById(R.id.icon);

        ProductVO productVO = (ProductVO) list.get(position);

        String closeDate = productVO.getClose_time();
        String sToday = Utils.getTime();
        int change = 100;
        try {
            change = Utils.GetDifferenceOfDate(closeDate, sToday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (change <= 0) {
            icon.setVisibility(View.VISIBLE);
        }

        textView_benefit1.setText(productVO.getName());
        textView_benefit2.setText(productVO.getBegin_time() + " ~ " + productVO.getClose_time());
        textView_benefit3.setText(productVO.getComponent());
        textView_benefit4.setText(productVO.getNote());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
