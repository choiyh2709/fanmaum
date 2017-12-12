package specup.fanmind.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import specup.fanmind.R;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.NewsfeedVO;

/**
 * Created by Fanmaum on 2017-01-24.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<Object> mList;
    private int itemLayout;
    private Context mContext;

    public MyRecyclerAdapter(Context context, List<Object> list,int itemLayout) {
        this.mList = list;
        this.mContext = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        NewsfeedVO newsfeedVO = ((NewsfeedVO) mList.get(position));

        Uri uri = Uri.parse(newsfeedVO.getPic_base() + newsfeedVO.getPic());
        HttpRequest.frescoCircleImage(mContext,uri,holder.simpleDraweeView);
        String[] images = newsfeedVO.getImages();
        if (images != null && images.length > 0) {
            HttpRequest.frescoImage(Uri.parse(images[0]), holder.imageView);
        } else {
            holder.imageView.setImageResource(android.R.color.transparent);
        }
        if (newsfeedVO.getTitle().equals("")) {
            holder.title.setText(newsfeedVO.getText());
        } else {
            holder.title.setText(newsfeedVO.getTitle());
        }
        holder.textView_nickName.setText(newsfeedVO.getNick());
        holder.starName.setText(newsfeedVO.getName());
        holder.textView_like.setText(newsfeedVO.getLikes());
        holder.textView_comment.setText(newsfeedVO.getReply_cnt());

        if (newsfeedVO.getIs_liked().equals("Y")) {
            holder.textView_like.setBackgroundResource(R.drawable.c1000_poster_llike_active);
        } else {
            holder.textView_like.setBackgroundResource(R.drawable.c1000_poster_llike_normal);
        }
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        if(mList != null){
           return mList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public void add(Object data) {
        mList.addAll((List<Object>) data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public SimpleDraweeView imageView,simpleDraweeView;
        public TextView textView_nickName, title, content, starName, textView_like, textView_comment;

        public ViewHolder(View itemView){
            super(itemView);

            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.imageView_user);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            textView_nickName = (TextView) itemView.findViewById(R.id.textView_nickName);
            starName = (TextView) itemView.findViewById(R.id.starName);
            textView_like = (TextView) itemView.findViewById(R.id.textView_like);
            textView_comment = (TextView) itemView.findViewById(R.id.textView_comment);
        }
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
