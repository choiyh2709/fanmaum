package specup.fanmind.common.StaggeredGridView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.vo.TimeLineList;

public class STGVAdapter extends BaseAdapter {
	private Context mContext;
//	DataSet mData = new DataSet();
//	private ArrayList<Item> mItems = new ArrayList<Item>();
//	private int newPos = 19;
	ArrayList<TimeLineList> mList;
	public STGVAdapter(Context context, ArrayList<TimeLineList> List) {
		mContext = context;
		mList = List;
//		getMoreItem();
	}

//	public void getMoreItem() {
//		for (int i = 0; i < 20; i++) {
//			Item item = new Item();
//			item.url = mData.url[i];
//			item.width = mData.width[i];
//			item.height = mData.height[i];
//			mItems.add(item);
//		}
//	}
//
//	public void getNewItem() {
//		Item item = new Item();
//		item.url = mData.url[newPos];
//		item.width = mData.width[newPos];
//		item.height = mData.height[newPos];
//		mItems.add(0, item);
//		newPos = (newPos - 1) % 19;
//	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		final TimeLineList item = mList.get(position);

		if (convertView == null) {
			Holder holder = new Holder();
			view = View.inflate(mContext, R.layout.cell_stgv, null);
			holder.img_content = (STGVImageView) view.findViewById(R.id.img_content);
			holder.img_content.setTag(position);
			holder.tv_like = (TextView) view.findViewById(R.id.stgv_tv01);
			holder.tv_letter = (TextView) view.findViewById(R.id.stgv_tv02);
			holder.mLikeImg= (ImageView)view.findViewById(R.id.stgv_img01);
			view.setTag(holder);
		} else {
			view = convertView;
		}

		final Holder holder = (Holder) view.getTag();

		/**
		 * StaggeredGridView has bugs dealing with child TouchEvent
		 * You must deal TouchEvent in the child view itself
		 **/
		 holder.img_content.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 int position = Integer.parseInt(v.getTag().toString());
//				 if((TimeLineMonthActivity)TimeLineMonthActivity.mTimeLineDetail !=null){
//					 TimeLineMonthActivity.mTimeLineDetail.onPush(v);
//				 }
			 }
		 });


		 
			String url = item.getImg();
			Log.e("Timeurl", url);
			
		if(Integer.parseInt(item.getHeight()) !=0){
			holder.img_content.mHeight = Integer.parseInt(item.getHeight());	
		}else{
			holder.img_content.mHeight =1;
		}
		 
		if(Integer.parseInt(item.getWidth()) !=0){
			holder.img_content.mWidth = Integer.parseInt(item.getWidth());	
		}else{
			holder.img_content.mWidth  =1;
		}
		 
		 
		 
		 if(item.getMyLike().equals("0")){
			 holder.mLikeImg.setBackgroundResource(R.drawable.timeline_like);
		 }else{
			 holder.mLikeImg.setBackgroundResource(R.drawable.timeline_like_on);
		 }
		 
		 holder.tv_like.setText(item.getLikeCnt());
		 holder.tv_letter.setText(item.getReplyCnt());
		 
		 ImageLoader.getInstance().displayImage(item.getImg(), holder.img_content, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
//				Bitmap round = ImageRound.getRoundedCornerBitmap(arg2, Utils.getDP(mContext, 1000));
//				holder.img_content.setImageBitmap(round);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		 return view;
	}

	class Holder {
		STGVImageView img_content;
		TextView tv_like, tv_letter;
		ImageView mLikeImg;
	}

}
