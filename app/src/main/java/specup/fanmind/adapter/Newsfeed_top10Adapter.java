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
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.NewsfeedVO;


public class Newsfeed_top10Adapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public Newsfeed_top10Adapter(Context context, List<Object> list) {
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
        // TODO Auto-generated method stub
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layout_In.inflate(R.layout.list_newsfeed_top10, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            NewsfeedVO newsfeedVO = ((NewsfeedVO) mList.get(position));
            String[] images = newsfeedVO.getImages();
            if (images != null && images.length > 0)
                HttpRequest.glideImage(mContext,images[0], holder.imageView);
            if (newsfeedVO.getTitle().equals("")) {
                holder.title.setText(newsfeedVO.getText());

            } else {
                holder.title.setText(newsfeedVO.getTitle());

            }
//            String closeDate = newsfeedVO.getDeadDate();
//            String sToday = Utils.getTime();
//            int change = Utils.GetDifferenceOfDate(closeDate, sToday);
//            if(change == 0 ){
//                holder.dDay.setText("TODAY");
//
//            }else if(change>0){
//                holder.dDay.setText("D-"+change);
//            }else{
//                int temp = Math.abs(change);
//                holder.dDay.setText("D+"+temp);
//
//            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    public class Holder {
        ImageView imageView;
        TextView title, dDay;
    }
}
