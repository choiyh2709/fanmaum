package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;

public class WhereLiveAdapter extends BaseAdapter{
	private ArrayList<String> mList = null;
	private LayoutInflater layout_In = null;

	public WhereLiveAdapter(Context context,  ArrayList<String> mList){
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void add(String data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public String getItem(int position) {
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
			convertView = layout_In.inflate(R.layout.list_star, null);
			holder.tv01 =(TextView)convertView.findViewById(R.id.where_tv01);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		String where = mList.get(position);
		if(where!=null){
			holder.tv01.setText(where);
		}
		return convertView;
	}

	public class Holder{
		TextView tv01;
	}
}
