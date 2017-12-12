package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.vo.CommentList;


public class HomeFragment_detail_delivery_international_ListAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public HomeFragment_detail_delivery_international_ListAdapter(Context context, List<Object> list) {
        this.mList = list;
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
        convertView = layout_In.inflate(R.layout.list_delivery_international, null);
        holder.textView_total = (TextView) convertView.findViewById(R.id.textView_total);
        holder.textView_total.setText(((BasicNameValuePair) mList.get(position)).getName() );
        holder.textView_deliveryCost = (TextView) convertView.findViewById(R.id.textView_deliveryCost);
        holder.textView_deliveryCost.setText(((BasicNameValuePair) mList.get(position)).getValue()  );
        return convertView;
    }

    public class Holder {
        TextView textView_total, textView_deliveryCost;
    }
}
