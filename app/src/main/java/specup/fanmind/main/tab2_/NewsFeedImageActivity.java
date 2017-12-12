package specup.fanmind.main.tab2_;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.BitmapSave;
import specup.fanmind.common.Util.TouchImageView;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.media.SingleMediaScanner;

public class NewsFeedImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_newsfeedimage);
        setView();
    }

    String sImgPath[];
    String mUrl[];
    int mPosition;
    ViewPager mPager;
    ImageAdapter mAdapter;
    Bitmap mBitMap;

    private void setView() {
        mUrl = getIntent().getStringArrayExtra("image");
        mPosition = getIntent().getIntExtra("position", 0);
        boolean isShow = getIntent().getBooleanExtra("support", false);
        Button mDown = (Button) findViewById(R.id.newsfeedimage_btn01);
        if (isShow) mDown.setVisibility(View.GONE);

        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ImageAdapter(this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPosition);
    }


    public void onDown(View v) {
        ImageView mHideImage = new ImageView(this);
        Utils.makeDir(this);
        int nowPosition = mPager.getCurrentItem();
        String url = mUrl[nowPosition];
        int sub = url.lastIndexOf("/");
        final String fileName = url.substring(sub + 1);
        ImageLoader.getInstance().displayImage(url, mHideImage, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                mBitMap = arg2;
                if (mBitMap != null) {
                    String strFilePath = Environment.getExternalStorageDirectory().toString() + "/Fanmaum";
                    BitmapSave.SaveBitmapToFileCache(NewsFeedImageActivity.this, mBitMap, strFilePath, "/" + fileName + ".jpg");
                    Utils.setScannerFile(NewsFeedImageActivity.this, strFilePath, fileName + ".jpg");
                    File file = new File(strFilePath+ "/" + fileName + ".jpg");
                    new SingleMediaScanner(NewsFeedImageActivity.this, file);
                    Utils.setToast(NewsFeedImageActivity.this, R.string.okdownload);
                } else {
                    Utils.setToast(NewsFeedImageActivity.this, R.string.timelineerror);
                }
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }
        });
    }


    public class ImageAdapter extends PagerAdapter {
        private LayoutInflater mInflater;
        Context mContext;

        public ImageAdapter(Context con) {
            super();
            mInflater = LayoutInflater.from(con);
            mContext = con;
        }

        @Override
        public int getCount() {
            return mUrl.length;
        }    //여기서는 2개만 할 것이다.

        //뷰페이저에서 사용할 뷰객체 생성/등록
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView mImage = new TouchImageView(mContext);
            ImageLoader.getInstance().displayImage(mUrl[position], mImage);
            container.addView(mImage, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return mImage;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().deleteActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

}
