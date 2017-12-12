package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.RightList;
import specup.fanmind.R;

public class RightAdapter2 extends BaseAdapter{
	private ArrayList<RightList> mList = null;
	private LayoutInflater layout_In = null;
	private OnClickListener mOnClickListener = null;
	private Context mContext ; 

	public RightAdapter2(Context context,  ArrayList<RightList> mList, OnClickListener onClickListener){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOnClickListener = onClickListener;
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
		if(convertView== null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_right2, null);
			holder.mLayout = (RelativeLayout)convertView.findViewById(R.id.listright_layout01);
			holder.img01 = (ImageView)convertView.findViewById(R.id.listright_img01);
			holder.img02 = (ImageView)convertView.findViewById(R.id.listright_img03);
			holder.img03 = (ImageButton)convertView.findViewById(R.id.listright_img04);
			holder.tv01 = (TextView)convertView.findViewById(R.id.listright_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listright_tv02);
			holder.btn01 = (Button)convertView.findViewById(R.id.listright_btn01);
			holder.mTopLayout = (RelativeLayout)convertView.findViewById(R.id.listright_toplayout);
			holder.mToptv = (TextView)convertView.findViewById(R.id.listright_toptv);

			convertView.setTag(holder);
		}else{
			holder =(Holder)convertView.getTag();
		}

		RightList rightList = mList.get(position);
		if(rightList!=null){

			if(rightList.isSelect()){
				holder.mLayout.setBackgroundResource(R.drawable.r_top_list_on);
			}else{
				holder.mLayout.setBackgroundResource(R.drawable.r_top_list);
			}

			if(rightList.isLike()){
				holder.img03.setImageResource(R.drawable.menu_like);
			}else{
				holder.img03.setImageResource(R.drawable.menu_dislike);
			}

			if(mList.get(position).getTitle() !=null){
				holder.mTopLayout.setVisibility(View.VISIBLE);
				holder.mToptv.setText(mList.get(position).getTitle());	
			}else{
				holder.mTopLayout.setVisibility(View.GONE);
			}

			if(position-1>=0){
				if(mList.get(position).getTitle() !=null){
					if(mList.get(position-1).getTitle().equals(mList.get(position).getTitle())){
						holder.mTopLayout.setVisibility(View.GONE);
					}else{
						holder.mTopLayout.setVisibility(View.VISIBLE);
					}
				}
			}

			holder.img02.setVisibility(View.GONE);
			
			holder.img01.setImageBitmap(null);
			ImageLoader.getInstance().displayImage(rightList.getPath(), holder.img01);

			
			holder.tv01.setText(rightList.getName());
			holder.tv02.setText(Utils.getMoney(rightList.getFan()));

			holder.img03.setTag(position);
			holder.img03.setOnClickListener(mOnClickListener);
			holder.img03.setFocusable(false);
			holder.img03.setFocusableInTouchMode(false);
			
			holder.btn01.setTag(position);
			holder.btn01.setOnClickListener(mOnClickListener);
		}
		return convertView;
	}

	public class Holder{
		ImageView img01, img02;
		ImageButton img03;
		TextView tv01, tv02, mToptv;
		Button btn01;
		RelativeLayout mLayout, mTopLayout;
	}
}
