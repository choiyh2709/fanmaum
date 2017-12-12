package specup.fanmind.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.NewsfeedVO;


public class Newsfeed_AllAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public Newsfeed_AllAdapter(Context context, List<Object> list) {
        this.mList = list;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(Object data) {
        mList.addAll((List<Object>) data);
        notifyDataSetChanged();
        ;
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
        NewsfeedVO newsfeedVO = ((NewsfeedVO) mList.get(position));
        if (convertView == null) {
            holder = new Holder();

            convertView = layout_In.inflate(R.layout.list_newsfeed_all, null);
            holder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.imageView_user);
            Uri uri = Uri.parse(newsfeedVO.getPic_base() + newsfeedVO.getPic());
            HttpRequest.frescoCircleImage(mContext,uri,holder.simpleDraweeView);

            holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.imageView);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
            holder.starName = (TextView) convertView.findViewById(R.id.starName);
            holder.textView_like = (TextView) convertView.findViewById(R.id.textView_like);
            holder.textView_comment = (TextView) convertView.findViewById(R.id.textView_comment);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String[] images = newsfeedVO.getImages();
        if (images != null && images.length > 0)
            HttpRequest.frescoImage(Uri.parse(images[0]),holder.imageView);
        if (newsfeedVO.getTitle().equals("")) {
            holder.title.setText(newsfeedVO.getText());
        } else {
            holder.title.setText(newsfeedVO.getTitle());
        }

//        holder.content.setText(newsfeedVO.getText());
        holder.textView_nickName.setText(newsfeedVO.getNick());
        holder.starName.setText(newsfeedVO.getName());
        holder.textView_like.setText(newsfeedVO.getLikes());
        holder.textView_comment.setText(newsfeedVO.getReply_cnt());

        if (newsfeedVO.getIs_liked().equals("Y")) {
            holder.textView_like.setBackgroundResource(R.drawable.c1000_poster_llike_active);
        } else {
            holder.textView_like.setBackgroundResource(R.drawable.c1000_poster_llike_normal);
        }


        return convertView;
    }

    public class Holder {
        SimpleDraweeView imageView,simpleDraweeView;
        TextView textView_nickName, title, content, starName, textView_like, textView_comment;
    }

    public void setChangeList(Object object) {
          for (Integer i = 0; i < mList.size(); i++) {

              Object tempObject = mList.get(i);
              if (tempObject.equals(object)) {
                  mList.set(i, object) ;
              }
          }
          notifyDataSetChanged();
      }
}
