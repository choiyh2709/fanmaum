package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.CommentList;
import specup.fanmind.vo.RankingVO;

/**
 * 랭킹 조회
 */
public class RankingAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private List<Object> mfilterList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public RankingAdapter(Context context, List<Object> list) {
        this.mList = list;
        this.mfilterList = mList;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(CommentList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mfilterList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        holder = new Holder();
        convertView = layout_In.inflate(R.layout.list_ranking, null);
        holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
        RankingVO rankingVO = (RankingVO) mList.get(position);
        HttpRequest.getNetworkCircleImage(mContext,rankingVO.getPic_base() + rankingVO.getPic(), holder.imageView);
        holder.textView_nickName = (TextView) convertView.findViewById(R.id.textView_nickName);
        holder.textView_ranking = (TextView) convertView.findViewById(R.id.textView_ranking);
        holder.textView_maum = (TextView) convertView.findViewById(R.id.textView_maum);
        holder.textView_nickName.setText(rankingVO.getNick().toString());
        //마음내역 입력
        holder.textView_maum.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", rankingVO.getHeart().toString()));
        holder.textView_ranking.setText(rankingVO.getRank());
        return convertView;
    }

    public class Holder {

        ImageView imageView;
        TextView textView_nickName;
        TextView textView_maum;
        TextView textView_ranking;
    }

    public void reset() {
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

            final List<Object> list = mList;

            //            int count = list.size();
            ArrayList<Object> nlist = new ArrayList<Object>();

            RankingVO filterableObject;

            for (int i = 0; i < list.size(); i++) {
                filterableObject = (RankingVO) list.get(i);
                if (filterableObject.getNick().toString().toUpperCase().contains(filterString.toUpperCase())) {
                    nlist.add(filterableObject);
                } else {
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
}
