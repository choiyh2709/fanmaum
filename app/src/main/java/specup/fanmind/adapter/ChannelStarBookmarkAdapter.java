package specup.fanmind.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.ChannelStarVo;

/**
 * 채널 - 채널 즐겨찾기
 */
public class ChannelStarBookmarkAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private List<Object> mList_all = null;
    private List<Object> mfilterList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private WeakReference<ImageView> imageViewWeakReference;
    private ChannelStarVo channelStarVo;


    public ChannelStarBookmarkAdapter(Context context, List<Object> mList, View.OnClickListener onClickListener) {
        this.mList = mList;
        this.mfilterList = this.mList;
        mContext = context;
        this.onClickListener = onClickListener;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ChannelStarBookmarkAdapter(Context context, List<Object> mList, View.OnClickListener onClickListener, boolean isAll , List<Object> subList) {
        if (isAll){
            this.mList_all = new ArrayList<Object>(mList);
            this.mList = subList;
        }else {
            this.mList = mList;
        }
        this.mfilterList = this.mList;
        mContext = context;
        this.onClickListener = onClickListener;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mfilterList != null) {
            return mfilterList.size();
        }
        return 0;
    }

    public void addSubList(int from, int to) {
        List<Object> list_1 = new ArrayList<Object>(this.mList_all.subList(from, to));
        this.mList.addAll(list_1);
        reset();
    }

    @Override
    public Object getItem(int position) {
        return mfilterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        channelStarVo = (ChannelStarVo) mfilterList.get(position);
        Holder holder;

        if (convertView == null) {
            convertView = layout_In.inflate(R.layout.list_r_channel_bookmark, null);

            holder = new Holder();
            holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_starName);
            holder.textView_fanNumber = (TextView) convertView.findViewById(R.id.textView_fanNumber);
            holder.button_isBookmark = (ImageView) convertView.findViewById((R.id.button_isBookmark));
            holder.listcomment_profile = (SimpleDraweeView) convertView.findViewById(R.id.listcomment_profile);
            holder.text_layout = (LinearLayout) convertView.findViewById(R.id.text_layout);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.textView_nickName.setText(channelStarVo.getName());
        holder.textView_fanNumber.setText(channelStarVo.getFan_cnt());

        if (channelStarVo.getIs_added().equals("Y")) {
            holder.button_isBookmark.setBackgroundResource(R.drawable.right_heart_on);
        } else {
            holder.button_isBookmark.setBackgroundResource(R.drawable.right_heart_off);
        }
        imageViewWeakReference = new WeakReference<ImageView>(holder.listcomment_profile);
        HttpRequest.frescoCircleImage(mContext, Uri.parse(channelStarVo.getIcon_base() + channelStarVo.getIcon()), (SimpleDraweeView) imageViewWeakReference.get());
        holder.button_isBookmark.setOnClickListener(onClickListener);
        holder.text_layout.setOnClickListener(onClickListener);
        holder.listcomment_profile.setOnClickListener(onClickListener);
        holder.button_isBookmark.setTag(channelStarVo);
        holder.text_layout.setTag(channelStarVo);
        holder.listcomment_profile.setTag(channelStarVo);

        return convertView;
    }


    public static CustomDialog mDialog;

    public class Holder {
        SimpleDraweeView listcomment_profile;
        TextView textView_nickName, textView_fanNumber;
        ImageView button_isBookmark;
        LinearLayout text_layout;
    }

    public void reset(){
        mfilterList = mList;
        notifyDataSetChanged();
    }

    private ItemFilter mFilter = new ItemFilter();

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString();
            FilterResults results = new FilterResults();
            final List<Object> list = mList_all;
            ArrayList<Object> nlist = new ArrayList<Object>();
            ChannelStarVo filterableObject;

            for (int i = 0; i < list.size(); i++) {
                filterableObject = (ChannelStarVo) list.get(i);
                if (filterableObject.getName().toString().toUpperCase().contains(filterString.toUpperCase())) {
                    nlist.add(filterableObject);
                }else{
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mfilterList = (ArrayList<Object>) results.values;
            notifyDataSetChanged();
        }

    }
    //리스트뷰의 아이템 갯수를 가져와 높이를 설정한다
    public static void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }

        int totalHeight = 0;
        int desiredwidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),View.MeasureSpec.AT_MOST);
        int count = listAdapter.getCount();
        for (int i = 0; i<count; i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(desiredwidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
