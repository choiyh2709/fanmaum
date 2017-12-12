package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.ProductVO;

/**
 * 결재페이지 용
 */
public class ProjectPaymentProductAdapter2 extends BaseAdapter {
    private List<Object> mList = null;
    private Context mContext;
    private ProductVO getViewProductVO;

    public ProjectPaymentProductAdapter2(Context context, List<Object> list,TextView textView_sumCost) {
        mContext = context;
        mList = list;
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

        //select item
        holder.icon2 = (CheckBox) convertView.findViewById(R.id.icon2);
        holder.icon.setVisibility(View.GONE);
        holder.icon2.setVisibility(View.GONE);


        holder.textView_benefit1.setText(mContext.getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(getViewProductVO.getPoint())));
        holder.textView_benefit2.setText(mContext.getString(R.string.goods_amount).replace("{NOW}", getViewProductVO.getAmount_now()));
        holder.textView_benefit2.setVisibility(View.GONE);
        holder.textView_benefit3.setText(getViewProductVO.getComponent());
        holder.textView_benefit4.setText(getViewProductVO.getNote());


        return convertView;
    }


    public class Holder {
        CheckBox icon2;
        TextView textView_benefit1, textView_benefit2, textView_benefit3, textView_benefit4, icon;

        LinearLayout layout_payment_detail;
    }

}
