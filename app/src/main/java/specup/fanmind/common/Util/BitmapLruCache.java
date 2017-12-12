package specup.fanmind.common.Util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache{

	private static int getDefaultLruCacheSize() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		return cacheSize;
	}



	public BitmapLruCache(){
		super( getDefaultLruCacheSize() );
	}

	public BitmapLruCache(int maxSize) {
		super(maxSize);
	}


	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}




	@Override
	public Bitmap getBitmap(String url) {
		return get( url );
	}




	@Override
	public void putBitmap(String url, Bitmap bitmap ) {
		put( url, bitmap );
	}
}

