package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.vo.AddressVO;
import specup.fanmind.vo.CommentList;


public class Alert_address_search_listAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private Context mContext;

    public Alert_address_search_listAdapter(Context context, List<Object> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;
        if (convertView == null) {
            holder = new Holder();

            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layout.inflate(R.layout.list_alert_address_search, null);
            holder.textView_zipcode = (TextView) convertView.findViewById(R.id.textView_zipcode);
            holder.textView_new_address = (TextView) convertView.findViewById(R.id.textView_new_address);
            holder.textView_old_address = (TextView) convertView.findViewById(R.id.textView_old_address);

            AddressVO addressVO = (AddressVO)(mList.get(position));
            holder.textView_zipcode.setText(addressVO.getZipcode());
            holder.textView_new_address.setText(addressVO.getNewAddress());
            holder.textView_old_address.setText(addressVO.getOldAddress());

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        return convertView;
    }

    public class Holder {
        TextView textView_zipcode, textView_new_address, textView_old_address;
    }
}
