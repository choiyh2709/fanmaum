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
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.LockScreenImage;


public class LockScreenImageDelAdapter extends BaseAdapter{
	private ArrayList<LockScreenImage> mList = null;
	private LayoutInflater layout_In = null;
	private boolean[] isCheckedConfrim;
	boolean isImage;
	BitmapLruCache mLru;
	Context mContext;
	
	public LockScreenImageDelAdapter(Context context,  ArrayList<LockScreenImage> mList, int size, boolean image){
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isCheckedConfrim = new boolean[size];
		isImage = image;
		mLru = new BitmapLruCache();
		mContext = context;
	}

	// CheckBox를 모두 선택하는 메서드
	public void setAllChecked(boolean ischeked) {
		int tempSize = isCheckedConfrim.length;
		for(int a=0 ; a<tempSize ; a++){
			isCheckedConfrim[a] = ischeked;
		}
	}

	public void setChecked(int position) {
		isCheckedConfrim[position] = !isCheckedConfrim[position];
	}


	public ArrayList<Integer> getChecked(){
		int tempSize = isCheckedConfrim.length;
		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
		for(int b=0 ; b<tempSize ; b++){
			if(isCheckedConfrim[b]){
				mArrayList.add(b);
			}
		}
		return mArrayList;
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
		if(isImage){
			convertView = layout_In.inflate(R.layout.list_lockscreenimagedel, null);
		}else{
			convertView = layout_In.inflate(R.layout.list_lockscreenbuttondel, null);
		}
		holder.img1 = (ImageView)convertView.findViewById(R.id.listlockimagedel_image01);
		holder.img2 = (ImageView)convertView.findViewById(R.id.listlockimagedel_image02);
		holder.img3 = (ImageView)convertView.findViewById(R.id.listlockimagedel_image03);
		convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		LockScreenImage lockList = mList.get(position);
		if(lockList!=null){
			
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
			
			if(isCheckedConfrim[position]){
				holder.img2.setVisibility(View.VISIBLE);
				holder.img3.setVisibility(View.VISIBLE);
			}else{
				holder.img2.setVisibility(View.GONE);
				holder.img3.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public class Holder{
		ImageView img1, img2, img3;
	}
	
}
