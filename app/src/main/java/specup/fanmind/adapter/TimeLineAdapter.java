package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.SupportList;
import specup.fanmind.R;


public class TimeLineAdapter extends BaseAdapter{
	private ArrayList<SupportList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	private String mItems[];

	public TimeLineAdapter(Context context,  ArrayList<SupportList> mList){
		this.mList = mList;
		mContext = context;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(Utils.getLocal(mContext, "English")){
			mItems = context.getResources().getStringArray(R.array.month);
		}
	}


	public void add(SupportList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public SupportList getItem(int position) {
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
			convertView = layout_In.inflate(R.layout.list_timeline, null);
			holder.tv01 =(TextView)convertView.findViewById(R.id.listtime_tv01);
			holder.img =(ImageView)convertView.findViewById(R.id.listtime_img);
			holder.img01 =(ImageView)convertView.findViewById(R.id.listtime_img01);
			holder.img03 =(ImageView)convertView.findViewById(R.id.listtime_img03);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		SupportList support = mList.get(position);

		if(support!=null){
			if(support.getThumImgPath().equals("0")){
				holder.img.setVisibility(View.VISIBLE);
				holder.img01.setImageBitmap(null);
			}else{
				holder.img.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage(support.getTitle(), holder.img01);
				
			}

			if(support.getText().equals("00")){
				holder.tv01.setText(mContext.getString(R.string.timelinealbum));
			}else{
				if(Utils.getLocal(mContext, "English")){
					String idx = mItems[Integer.parseInt(support.getText())-1];
					String month = mContext.getString(R.string.timeline01).replace("{MONTH}", idx);
					holder.tv01.setText(month);	
				}else{
					String month = mContext.getString(R.string.timeline01).replace("{MONTH}", String.valueOf(Integer.parseInt(support.getText())));
					holder.tv01.setText(month);	
				}
			}


			if(position+1 == mList.size()){
				holder.img03.setVisibility(View.VISIBLE);
			}else{
				holder.img03.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	public class Holder{
		TextView tv01;
		ImageView img01, img03, img;
	}
}
