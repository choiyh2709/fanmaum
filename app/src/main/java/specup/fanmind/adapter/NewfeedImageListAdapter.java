package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import specup.fanmind.R;
import specup.fanmind.vo.ProductVO;


public class NewfeedImageListAdapter extends BaseAdapter {
    private String[] arrayImage;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private ProductVO getViewProductVO;

    public NewfeedImageListAdapter(Context context, String[] arrayImage, View.OnClickListener onClickListener) {
        mContext = context;
        this.arrayImage = arrayImage;
        this.onClickListener = onClickListener;
    }


    @Override
    public int getCount() {
        return arrayImage.length;
    }

    @Override
    public Object getItem(int position) {
        return arrayImage[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    Holder holder;
    Bitmap mBitMap = null;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layout.inflate(R.layout.newfeed_images, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.imageView.setTag(position);
            holder.imageView.setOnClickListener(onClickListener);
            String string_url = arrayImage[position];


            if (!ImageLoader.getInstance().isInited()) {
                ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(mContext);
                ImageLoader.getInstance().init(config);
            }
            int sub = string_url.lastIndexOf("/");

            ImageLoader.getInstance().displayImage(string_url, holder.imageView, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    mBitMap = arg2;
                    Drawable result = new BitmapDrawable(mContext.getResources(), mBitMap);
                    float multiplier = 1;
                    multiplier = (float) mContext.getResources().getDimension(R.dimen.display_width) / (float) result.getIntrinsicWidth();

                    int width = (int) (result.getIntrinsicWidth() * multiplier);
                    int height = (int) (result.getIntrinsicHeight() * multiplier);
                    result.setBounds(0, 0, width, height);
                    ((ImageView)arg1).setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    ((ImageView)arg1).setImageDrawable(result);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                }
            });
            convertView.setTag(holder);
        } else {
            holder =(Holder) convertView.getTag();
        }


        return convertView;
    }


    public class Holder {
        ImageView imageView;
    }
}
