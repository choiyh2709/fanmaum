package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.DynamicViewHolder;
import specup.fanmind.vo.RightList;

public class StarListAdapter extends BaseAdapter{
	private ArrayList<RightList> mList = null;
	private LayoutInflater layout_In = null;
    private DynamicViewHolder dynamicViewHolder;


	public StarListAdapter(Context context,  ArrayList<RightList> mList){
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicViewHolder = new DynamicViewHolder();

	}


	public void add(RightList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public RightList getItem(int position) {
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
        RelativeLayout img01 = null;
        TextView tv01 = null;
        convertView = layout_In.inflate(R.layout.list_star, null);
        img01 = dynamicViewHolder.get(convertView, R.id.star_img01);
        tv01 = dynamicViewHolder.get(convertView, R.id.star_tv01);

        RightList star = mList.get(position);
        if (star != null) {
            if (star.isDel()) img01.setBackgroundResource(R.drawable.pop_list_on);
            else img01.setBackgroundResource(R.drawable.pop_list);
            tv01.setText(star.getName());
        }
        return convertView;
    }

}
