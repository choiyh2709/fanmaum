package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.vo.MovieList;


public class MovieAdapter extends BaseAdapter{
	private ArrayList<MovieList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;

	public MovieAdapter(Context context,  ArrayList<MovieList> mList){
		this.mList = mList;
		mContext = context;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void add(MovieList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public MovieList getItem(int position) {
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
			convertView = layout_In.inflate(R.layout.list_movie, null);
			holder.mLayout=(RelativeLayout)convertView.findViewById(R.id.movielist_layout01);
			holder.img1 = (ImageView)convertView.findViewById(R.id.movielist_img01);
			holder.tv01 = (TextView)convertView.findViewById(R.id.movielist_tv01);
			holder.tv03 = (TextView)convertView.findViewById(R.id.movielist_tv02);
			holder.tv02 = (TextView)convertView.findViewById(R.id.movielist_tv03);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		MovieList movieList = mList.get(position);
		if(movieList!=null){
			ImageLoader.getInstance().displayImage(movieList.getImagePath(), holder.img1);

			holder.tv01.setText(movieList.getName());
			holder.tv02.setText(movieList.getDate());
//			int duration = movieList.getDuration();
//			int mDay    = duration / (60 * 60 * 24);
//			int mHours   = (duration - mDay * 60 * 60 * 24) / (60 * 60); 
//			int mMinute = (duration - mDay * 60 * 60 * 24 - mHours * 3600) / 60; 
//			int mSecond = duration % 60;
//			String mHour = Utils.chageTime(mHours);
//			String mMin = Utils.chageTime(mMinute);
//			String mSec = Utils.chageTime(mSecond);
//			if(mHour.equals("00")) mHour="";
//			else mHour= mHour+":";
//			holder.tv03.setText(mHour+mMin+":"+mSec);

		}
		return convertView;
	}

	public class Holder{
		ImageView img1;
		TextView tv01, tv02, tv03;
		RelativeLayout mLayout;
	}
}
