package specup.fanmind.common.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapOption {

	
	
    /** 비트맵 회전
   	 * @param bitmap 비트맵
   	 * @param degrees 회전 각도.
   	 * @return
   	 */
	public static Bitmap rotate(Bitmap bitmap, int degrees){
		if(degrees != 0 && bitmap != null){
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, 
					(float) bitmap.getHeight() / 2);
			try{
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				if(bitmap != converted){
					bitmap.recycle();
					bitmap = converted;
				}
			}catch(OutOfMemoryError ex){
                Log.e("메모리 부족", "부족");
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
			}
		}
		return bitmap;
	}
	
    /** 회전 각도 구하기.
   	 * @param exifOrientation 들어오는 각도.
   	 * @return
   	 */
	public static int exifOrientationToDegrees(int exifOrientation){
		if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
			Log.e("90", "90");
			return 90;
		}else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
			Log.e("180", "180");
			return 180;
		}else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
			Log.e("270", "270");
			return 270;
		}
		return 0;
	}
	
	
	public static void changeRotate(String imagePath) {
		Bitmap bitmap = null;
		FileOutputStream out = null;
		try {
			File f = new File(imagePath);
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			if (angle == 0) {
				return;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;

			Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
					null, options);
			bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			out = new FileOutputStream(new File(imagePath));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			if (bmp != null) {
				bmp.recycle();
			}

		} catch (IOException e) {
		} catch (OutOfMemoryError oom) {
		} finally {
//			IOUtils.closeQuietly(out);
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}
	
	
	/** �̹���������¡
	 * @param source ��Ʈ��
	 * @param maxResolution �ִ���̰�
	 * @return
	 */
	public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution){
		int width = source.getWidth();
		int height = source.getHeight();
		int newWidth = width;
		int newHeight = height;
		float rate = 0.0f;

		if(width > height){
			if(maxResolution < width){
				rate = maxResolution / (float) width;
				newHeight = (int) (height * rate);
				newWidth = maxResolution;
			}
		}else{
			if(maxResolution < height){
				rate = maxResolution / (float) height;
				newWidth = (int) (width * rate);
				newHeight = maxResolution;
			}
		}

		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	}
}
