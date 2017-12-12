package specup.fanmind.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.DynamicViewHolder;
import specup.fanmind.common.Util.MarqueeTextView;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.ChannelStarVo;
import specup.fanmind.vo.RightList;

/**
 * 채널 - 스타 리스트
 */
public class ChannelStarListAdapter extends BaseAdapter {

    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private DynamicViewHolder dynamicViewHolder;
    private Context mContext;
    private String select;

    public ChannelStarListAdapter(Context context, List<Object> mList) {

        mContext = context;
        this.mList = mList;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicViewHolder = new DynamicViewHolder();
        select = StarSetting.getSTAR_SELECT_INDEX(mContext);
    }

    public void add(RightList data) {
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
        convertView = layout_In.inflate(R.layout.list_star3, null);
        SimpleDraweeView img01 = dynamicViewHolder.get(convertView, R.id.icon);
        MarqueeTextView tv01 = dynamicViewHolder.get(convertView, R.id.starName);

        if (position == 0) {
            ChannelStarVo tempChannelStarVo = ((ChannelStarVo) mList.get(position));
            img01.setBackgroundResource(R.drawable.d1000_channel_more);
            tv01.setText(tempChannelStarVo.getName());
        } else {
            ChannelStarVo tempChannelStarVo = ((ChannelStarVo) mList.get(position));
            HttpRequest.frescoCircleImage(mContext, Uri.parse(tempChannelStarVo.getIcon_base() + tempChannelStarVo.getIcon()),img01);
            tv01.setText(tempChannelStarVo.getName());
        }

        return convertView;
    }

}
