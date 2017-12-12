package specup.fanmind.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.GoodsEmsVO;


public class DefaultAdapter2 extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;


    public DefaultAdapter2(Context context, List<Object> mList) {
        this.mList = mList;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(CommentList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        holder = new Holder();

        GoodsEmsVO tempGoodsEmsVO = (GoodsEmsVO) mList.get(position);

        convertView = layout_In.inflate(R.layout.list_default, null);

        holder.textview_text = (TextView) convertView.findViewById(R.id.textview_text);
        holder.textview_text.setText(tempGoodsEmsVO.getRegion()
                +"("+(mContext.getString(R.string.maum_num).replace("{VALUE}",tempGoodsEmsVO.getCost()) )+")("+(mContext.getString(R.string.mypoint_tv06).replace("{PRICE}",Utils.getMoney(tempGoodsEmsVO.getCost_won())))+")");
        return convertView;
    }

    public class Holder {
        TextView textview_text;
    }
}
