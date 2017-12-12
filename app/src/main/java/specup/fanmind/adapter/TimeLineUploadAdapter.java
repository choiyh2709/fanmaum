package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.BitmapLruCache;
import specup.fanmind.common.Util.BitmapOption;
import specup.fanmind.vo.LockScreenImage;

public class TimeLineUploadAdapter extends BaseAdapter{
	private ArrayList<LockScreenImage> mList = null;
	private LayoutInflater layout_In = null;
	BitmapLruCache mLru;
	
	public TimeLineUploadAdapter(Context context,  ArrayList<LockScreenImage> mList){
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLru = new BitmapLruCache();
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
			convertView = layout_In.inflate(R.layout.list_timelineupload, null);
			holder.img1 = (ImageView)convertView.findViewById(R.id.listtimelineupload_image01);
			holder.img2 = (ImageView)convertView.findViewById(R.id.listtimelineupload_image02);
			convertView.setTag(holder);
		}else{
			holder =(Holder)convertView.getTag();
		}

		LockScreenImage lockList = mList.get(position);
		if(lockList!=null){
			if(lockList.isFirst()){
				holder.img1.setImageResource(R.drawable.o1110_btn03);
			}else{
				Bitmap getBit = mLru.get(lockList.getPath());
				if(getBit == null){
					Bitmap image = BitmapFactory.decodeFile(lockList.getPath());
					image = BitmapOption.resizeBitmapImage(image, 1000);
					ExifInterface exif = null;
					try {
						exif = new ExifInterface(lockList.getPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
					int exifDegree = BitmapOption.exifOrientationToDegrees(exifOrientation);
					image = BitmapOption.rotate(image, exifDegree);
					holder.img1.setImageBitmap(image);
					mLru.put(lockList.getPath(), image);
				}else{
					holder.img1.setImageBitmap(getBit);	
				}
				
				holder.img2.setTag(position);
			}
			
			if(lockList.isDel()){
				holder.img2.setVisibility(View.VISIBLE);
			}else{
				holder.img2.setVisibility(View.GONE);
			}
			
		}
		return convertView;
	}

	public class Holder{
		ImageView img1, img2;
	}
}
