package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.RightList;


public class MyPointAdapter2 extends BaseAdapter{
	private ArrayList<RightList> mList = null;
	private LayoutInflater layout_In = null;
	Context mContext;

	public MyPointAdapter2(Context context,  ArrayList<RightList> mList){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		Holder holder ;
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_mypoint, null);
			holder.tv01 = (TextView)convertView.findViewById(R.id.listmypoint_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listmypoint_tv02);
			convertView.setTag(holder);
		}else{
			holder= (Holder)convertView.getTag();
		}

		RightList rightList = mList.get(position);
		if(rightList!=null){
			holder.tv01.setText(rightList.getFan()); //타이틀
			holder.tv02.setText(Utils.getMoney(rightList.getName())); //포인트
		}
		return convertView;
	}

	public class Holder
	{
		ImageView img01;
		TextView tv01, tv02;
		Button btn01;
	}
}
