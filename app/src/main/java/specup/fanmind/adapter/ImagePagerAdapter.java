package specup.fanmind.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;

import specup.fanmind.common.Util.Utils;

public class ImagePagerAdapter extends PagerAdapter {
	private LayoutInflater mInflater;
	Context mContext;
	private String[] cover;
	private OnClickListener mOnClickListener = null;
	int mPosition;

	public ImagePagerAdapter(Context con, String[] Res, OnClickListener onClickListener, int position) {
		super();
		mInflater = LayoutInflater.from(con);
		mContext = con;
		cover = Res;
		mOnClickListener = onClickListener;
		mPosition = position;
	}

	@Override
	public int getCount() {
		return cover.length / 2;
	} 	//여기서는 2개만 할 것이다.

	//뷰페이저에서 사용할 뷰객체 생성/등록

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// View v = mInflater.inflate(R.layout.viewpager_image, null);
		RelativeLayout mRoot = new RelativeLayout(mContext);
		LayoutParams mRootParam = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mRoot.setLayoutParams(mRootParam);
		ImageView mImage = new ImageView(mContext);
		LayoutParams mPamas = new LayoutParams(Utils.getDP(mContext, 310),
				Utils.getDP(mContext, 320));

		// RelativeLayout.LayoutParams mPamas = new
		// RelativeLayout.LayoutParams(Utils.getDP(mContext, 210),
		// Utils.getDP(mContext, 220));

		mPamas.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mImage.setId(position);
		mImage.setTag(mPosition);
		mImage.setLayoutParams(mPamas);
		mImage.setScaleType(ScaleType.CENTER_CROP);
		mImage.setOnClickListener(mOnClickListener);
		String base = cover[position * 2].substring(2, cover[position * 2].length() - 1);
		String base_url = cover[(position * 2) + 1].substring(1, cover[(position * 2) + 1].length() - 2);
		String url = base + base_url;
		url = url.replace("\\", "");
		// url = "http://app.fanmaum.com/images/newsfeed_img.jpg";
		ImageLoader.getInstance().displayImage(url, mImage);

		mRoot.addView(mImage);
		container.addView(mRoot);
		return mRoot;
	}

	//뷰 객체 삭제.
	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}

	// instantiateItem메소드에서 생성한 객체를 이용할 것인지
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
}
