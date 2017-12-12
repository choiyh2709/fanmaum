package specup.fanmind.common.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import specup.fanmind.R;

public class BitmapSave {

    /**비트맵을 저장
     * @param context
     * @param bitmap 비트맵
     * @param strFilePath 저장경로
     * @param filename 이미지 이름.
     */
	public  static void SaveBitmapToFileCache(Context context, Bitmap bitmap, String strFilePath,
            String filename) {
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            if(bitmap !=null) bitmap.compress(CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setToast(context, R.string.timelineerror);
        } finally {
            try {
            	if(out !=null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setToast(context, R.string.timelineerror);
            }
        }
    }
	
    /**비트맵을 저장
     * @param context
     * @param bitmap 비트맵
     * @param strFilePath 저장경로
     * @param filename 이미지 이름.
     */
	public  static void SaveBitmapToGallery(Activity context, Bitmap bitmap, String strFilePath,
            String filename) {
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Uri mCaptureUri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/Fanmaum/"+filename);
    	Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mCaptureUri, "image/*");
		intent.putExtra("crop", "true");
        // crop한 이미지를 저장할때 200x200 크기로 저장
        intent.putExtra("outputX", 400); // crop한 이미지의 x축 크기
        intent.putExtra("outputY", 700); // crop한 이미지의 y축 크기
        intent.putExtra("aspectX", 4); // crop 박스의 x축 비율
        intent.putExtra("aspectY", 7); // crop 박스의 y축 비율

		String dirPath = Environment.getExternalStorageDirectory().toString()+"/Fanmaum/test.png";
		File f = new File(dirPath);
		try {
			f.createNewFile();
		} catch (IOException ex) {
			Log.e("io", ex.getMessage());  
		}

		Uri uri = Uri.fromFile(f);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(intent, Utils.CROP_FROM_CAMERA_IMAGE);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
	
}
