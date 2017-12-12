package specup.fanmind.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.vo.SupportList;
import specup.fanmind.R;

public class RequestStarAdapter extends BaseAdapter{
	private ArrayList<SupportList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	private OnClickListener mOnClickListener = null;

	public RequestStarAdapter(Context context,  ArrayList<SupportList> mList, OnClickListener click){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOnClickListener = click;
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
			convertView = layout_In.inflate(R.layout.list_request, null);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.listrequest_hidelayout);
			holder.img01 = (ImageView)convertView.findViewById(R.id.listrequest_img01);
			holder.img02 = (ImageView)convertView.findViewById(R.id.listrequest_img02);
			holder.img03 = (ImageView)convertView.findViewById(R.id.listrequest_img);

			holder.tv01 = (TextView)convertView.findViewById(R.id.listrequest_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listrequest_tv02);
			holder.tv03 = (TextView)convertView.findViewById(R.id.listrequest_tv03);
			holder.seekbar = (SeekBar)convertView.findViewById(R.id.listrequest_seekbar);
			convertView.setTag(holder);
		}else{
			holder =(Holder)convertView.getTag();
		}

		SupportList support = mList.get(position);
		if(support !=null){
//			String rank = mContext.getString(R.string.request01).replace("{NUMBER}", String.valueOf(position+1));
//			holder.tv01.setText(Html.fromHtml("<font color=#e82b47>"+rank+"</font>"+" "+support.getTitle()));
			holder.tv01.setText(support.getTitle());
			int current = Integer.parseInt(support.getGoalMoney())-Integer.parseInt(support.getNowMoney());
			String content1 = String.valueOf(current);
			String content2=mContext.getString(R.string.request04);
			holder.tv02.setText(Html.fromHtml("<font color=#e82b47>"+content1+"</font>"+content2));
			String cur = mContext.getString(R.string.request03).replace("{NUMBER}", support.getNowMoney())
					.replace("{GOAL}", support.getGoalMoney());
			holder.tv03.setText(cur);

			ImageLoader.getInstance().displayImage(support.getThumImgPath(), holder.img01);

			if(support.getWarning().equals("1")){
				holder.img02.setBackgroundResource(R.drawable.heart_icon_on);
			}else{
				holder.img02.setBackgroundResource(R.drawable.heart_icon_off);
			}

			holder.layout.setTag(position);
			//			holder.img02.setTag(position);
			holder.layout.setOnClickListener(mOnClickListener);

			holder.seekbar.setFocusable(false);
			int progress = 0;
			if(!support.getGoalMoney().equals("0")){
				progress = (Integer.parseInt(support.getNowMoney()) *100) /Integer.parseInt(support.getGoalMoney());
			}
			holder.seekbar.setProgress(progress);
			holder.seekbar.setFocusableInTouchMode(false);
			//			holder.seekbar.setThumb(null);
			holder.seekbar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		}		

		if(position+1 == mList.size()){
			holder.img03.setVisibility(View.VISIBLE);
		}else{
			holder.img03.setVisibility(View.GONE);
		}


		return convertView;
	}

	public class Holder{
		ImageView img01, img02, img03;
		TextView tv01, tv02, tv03;
		SeekBar seekbar;
		RelativeLayout layout;
	}

	public void setChangeList(Object object) {
		for (Integer i = 0; i < mList.size(); i++) {

			Object tempObject = mList.get(i);
			if (tempObject.equals(object)) {
				mList.set(i, (SupportList) object) ;
			}
		}
		notifyDataSetChanged();
	}
}
