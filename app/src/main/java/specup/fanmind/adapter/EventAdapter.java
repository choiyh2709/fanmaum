package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.EventList;
import specup.fanmind.vo.EventVO;


public class EventAdapter extends BaseAdapter {
    private ArrayList<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sToday;

    public EventAdapter(Context context, ArrayList<Object> mList) {
        mContext = context;
        this.mList = mList;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sToday = Utils.getTime();
    }


    public void add(EventList data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        EventVO event = (EventVO) mList.get(position);
        holder = new Holder();
        convertView = layout_In.inflate(R.layout.list_event, null);
        holder.img01 = (ImageView) convertView.findViewById(R.id.listevent_img01);
//        holder.img02 = (ImageView) convertView.findViewById(R.id.listevent_img02);
        holder.img03 = (ImageView) convertView.findViewById(R.id.listevent_img03);
        holder.tv01 = (TextView) convertView.findViewById(R.id.listevent_tv01);
        holder.tv02 = (TextView) convertView.findViewById(R.id.listevent_tv03);
        holder.tv03 = (TextView) convertView.findViewById(R.id.listevent_tv04);


//        holder.img02.setVisibility(View.GONE);
        holder.tv01.setText(event.getTitle());
        String end = event.getClose_time();
        int change = Utils.GetDifferenceOfDate(end, sToday);
        if (change > 0) {
            holder.tv02.setText(mContext.getString(R.string.event03));
            holder.tv03.setText(mContext.getString(R.string.listsupport04).replace("{DAY}", String.valueOf(change)));
        } else if (change == 0) {
            holder.tv02.setText(mContext.getString(R.string.event03));
            //					holder.tv03.setText(mContext.getString(R.string.listsupport04).replace("{DAY}", "D - Day"));
            holder.tv03.setText("D - Day");
        } else if (change < 0) {
            holder.tv02.setText(mContext.getString(R.string.event04));
            holder.tv03.setText(mContext.getString(R.string.deadline2));
//            holder.img02.setVisibility(View.VISIBLE);
        }
        HttpRequest.glideImage(mContext, event.getThumbnail_base() + event.getThumbnail(), holder.img01);

        convertView.setTag(holder);

        return convertView;
    }

    public class Holder {
        ImageView img01,  img03;
        TextView tv01, tv02, tv03;
    }
}
