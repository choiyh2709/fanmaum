package specup.fanmind.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.SupportList;


public class HomeFragment_orderProjectAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;


    public HomeFragment_orderProjectAdapter(Context context, List<Object> list) {
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

    Holder holder;
    Handler handler = new Handler();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            holder = new Holder();
            convertView = layout_In.inflate(R.layout.list_home_order_project, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.dDay = (TextView) convertView.findViewById(R.id.dDay);
            final SupportList supportList = ((SupportList) mList.get(position));
            holder.title.setText(supportList.getTitle());

            HttpRequest.glideImage(mContext,((SupportList) mList.get(position)).getThumImgPath(),holder.imageView);
            String closeDate = supportList.getDeadDate();
            String sToday = Utils.getTime();
            int change = Utils.GetDifferenceOfDate(closeDate, sToday);
            if (change == 0)

            {
                holder.dDay.setText("TODAY");

            } else if (change > 0)

            {
                holder.dDay.setText("D-" + change);
            } else

            {
                int temp = Math.abs(change);
                holder.dDay.setText(mContext.getString(R.string.deadline2));
                ((SupportList) mList.get(position)).setIsHistory("Y");
            }

            convertView.setTag(holder);
        }

        return convertView;
    }

    public class Holder {
        ImageView imageView;
        TextView title, dDay;
    }
}

