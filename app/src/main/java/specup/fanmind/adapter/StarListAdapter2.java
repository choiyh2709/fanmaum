package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.DynamicViewHolder;
import specup.fanmind.common.Util.MarqueeTextView;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.main.tab1.ProjectFragment;
import specup.fanmind.vo.RightList;

/**
 * 프로젝트 텝의 탑 스타 list
 */
public class StarListAdapter2 extends BaseAdapter {
    private ArrayList<RightList> mList = null;
    private LayoutInflater layout_In = null;
    private DynamicViewHolder dynamicViewHolder;
    private Context mContext;

    public StarListAdapter2(Context context, ArrayList<RightList> mList) {

        mContext = context;
        this.mList = mList;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicViewHolder = new DynamicViewHolder();
    }

    public void add(RightList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public RightList getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return Long.valueOf(mList.get(position).getStarsrl());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layout_In.inflate(R.layout.list_star2, null);
        ImageView img01 = dynamicViewHolder.get(convertView, R.id.icon);
        MarqueeTextView tv01 = dynamicViewHolder.get(convertView, R.id.starName);
        RelativeLayout layout_background = dynamicViewHolder.get(convertView, R.id.layout_background);

        RightList item = mList.get(position);
        switch (position) {
            case 0: {
                img01.setBackgroundResource(R.drawable.all);
                tv01.setText(item.getName());
            }
            break;
            case 1: {
                img01.setBackgroundResource(R.drawable.bookmark);
                tv01.setText(item.getName());
            }
            break;
            default: {
                HttpRequest.getNetworkCircleImage(mContext,item.getPath(), img01);
                layout_background.setBackgroundResource(R.drawable.a1000_7_custom_menu_circle_normal);
                tv01.setText(item.getName());
            }
        }

        if (ProjectFragment.select_star.equals(item.getStarsrl())) {
            tv01.setTextColor(Color.RED);
        }

//        RightList star = mList.get(position);
//        if (star != null) {
//            if (star.isDel()) img01.setBackgroundResource(R.drawable.a1000_7_custom_menu_circle_normal);
//            else img01.setBackgroundResource(R.drawable.pop_list);
//            tv01.setText(star.getName());
//        }
        return convertView;
    }

}
