package specup.fanmind.adapter;

import android.content.Context;
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
import specup.fanmind.vo.UserInformationVO;


public class HomeFragment_detail_attendFanAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public HomeFragment_detail_attendFanAdapter(Context context, List<Object> list) {
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
        convertView = layout_In.inflate(R.layout.list_assist_user, null);
        holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
        UserInformationVO userInformationVO = (UserInformationVO) mList.get(position);
        HttpRequest.getNetworkCircleImage(mContext,userInformationVO.getPic_base() + userInformationVO.getPic(), holder.imageView);
        holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
        holder.textView_time = (TextView) convertView.findViewById(R.id.textView_time);
        holder.textView_maum = (TextView) convertView.findViewById(R.id.textView_maum);
        holder.textView_nickName.setText(userInformationVO.getNick().toString());
        holder.textView_time.setText(userInformationVO.getTime().toString());
        holder.textView_maum.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", Utils.getMoney(userInformationVO.getHeart().toString()) ));
        return convertView;
    }

    public class Holder {
        ImageView imageView;
        TextView textView_nickName;
        TextView textView_time;
        TextView textView_maum;
    }
}
