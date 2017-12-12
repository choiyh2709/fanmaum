package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.BitmapLruCache;
import specup.fanmind.vo.LockScreenImage;

import specup.fanmind.common.Util.Utils;

public class LockScreenImageAdapter extends BaseAdapter{
	private ArrayList<LockScreenImage> mList = null;
	private LayoutInflater layout_In = null;
	Context mContext;
	BitmapLruCache mLru;
	public LockScreenImageAdapter(Context context,  ArrayList<LockScreenImage> mList){
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLru = new BitmapLruCache();
		mContext = context;
	}


	public void add(LockScreenImage data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public LockScreenImage getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_lockscreenimage, null);
			holder.img1 = (ImageView)convertView.findViewById(R.id.listlockimage_image01);
			holder.img2 = (ImageView)convertView.findViewById(R.id.listlockimage_image02);
			holder.img3 = (ImageView)convertView.findViewById(R.id.listlockimage_image03);
			holder.img4 = (ImageView)convertView.findViewById(R.id.listlockimage_image04);
			convertView.setTag(holder);
		}else{
			holder =(Holder)convertView.getTag();
		}

		LockScreenImage lockList = mList.get(position);
		if(lockList!=null){
			if(lockList.isFirst()){
				holder.img1.setImageResource(R.drawable.o1110_btn03);
				holder.img3.setVisibility(View.GONE);
				holder.img4.setVisibility(View.GONE);
			}else{
				Bitmap getBit = mLru.get(lockList.getPath());
				if(getBit==null){
					Bitmap bm = BitmapFactory.decodeFile(lockList.getPath()); 
					if(lockList.getPath() != null && bm != null){
						mLru.put(lockList.getPath(), bm);
						holder.img1.setImageBitmap(bm);
					}else{
						Utils.setToast(mContext, R.string.notimage);
					}
				}else{
					holder.img1.setImageBitmap(getBit);	
				}
				if(lockList.getCheck()==0){
					holder.img3.setVisibility(View.VISIBLE);
					holder.img4.setVisibility(View.VISIBLE);
				}else{
					holder.img3.setVisibility(View.GONE);
					holder.img4.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}

	public class Holder{
		ImageView img1, img2, img3, img4;
	}
}
