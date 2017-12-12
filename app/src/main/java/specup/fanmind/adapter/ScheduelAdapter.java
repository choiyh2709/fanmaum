package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.RightList;
import specup.fanmind.R;

public class ScheduelAdapter extends BaseAdapter{
	private ArrayList<RightList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	private String sToday;

	public ScheduelAdapter(Context context,  ArrayList<RightList> mList){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sToday = Utils.getTime();
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
		Holder holder;
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_sch, null);
			holder.tv01 = (TextView)convertView.findViewById(R.id.sch_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.sch_tv02);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		
		RightList right = mList.get(position);
		if(right!=null){
			holder.tv01.setText(Utils.getSchduel(right.getName()));
			holder.tv02.setText(right.getPath());
		}
		return convertView;
	}

	public class Holder{
		TextView tv01, tv02;
	}
}
