package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.vo.CommentList;


public class AlertDialog_Adapter extends BaseAdapter {
    private ArrayList<Object> mList = null;
    private Context mContext;
    private String selectSortValue;

    public AlertDialog_Adapter(Context context, ArrayList<Object> list, String selectSortValue) {
        mContext = context;
        mList = list;
        this.selectSortValue = selectSortValue;
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
            convertView = layout.inflate(R.layout.list_alertdialog, null);
            holder.text = (TextView) convertView.findViewById(R.id.textView);
            holder.text.setText((CharSequence) mList.get(position));

            if (selectSortValue.equals(mList.get(position))) {
                holder.text.setSelected(true);
            } else {
            }

                    convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        return convertView;
    }

    public class Holder {
        TextView text;
    }
}
