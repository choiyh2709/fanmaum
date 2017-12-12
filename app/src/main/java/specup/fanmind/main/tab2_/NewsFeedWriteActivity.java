package specup.fanmind.main.tab2_;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import specup.fanmind.FirstStarListActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.BitmapOption;
import specup.fanmind.common.Util.CustomEditText;
import specup.fanmind.common.Util.DialogUtil;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.ByteArrayBody;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;


public class NewsFeedWriteActivity extends Activity implements CustomEditText.OnTextLengthListener {

    CustomEditText mEtText;
    ImageView mImage[] = new ImageView[6];
    Button mImageHide[] = new Button[6];
    RelativeLayout mLayout[] = new RelativeLayout[6];
    Button mBtn[] = new Button[6];
    File mImagePath[] = new File[6];
    int mPosition;
    private DialogUtil dialogUtil;
    TextView mWordCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_newsfeedwrite);

        Tracker t = ((FanMindApplication) getApplication()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);
        t.setScreenName("NewsFeed Write");
        t.send(new HitBuilders.AppViewBuilder().build());

        setView();


    }


    private void setView() {
        mWordCheck = (TextView) findViewById(R.id.write_text);

        mLayout[0] = (RelativeLayout) findViewById(R.id.write_btn01);
        mLayout[1] = (RelativeLayout) findViewById(R.id.write_btn02);
        mLayout[2] = (RelativeLayout) findViewById(R.id.write_btn03);
        mLayout[3] = (RelativeLayout) findViewById(R.id.write_btn04);
        mLayout[4] = (RelativeLayout) findViewById(R.id.write_btn05);
        mLayout[5] = (RelativeLayout) findViewById(R.id.write_btn06);

        mImageHide[0] = (Button) findViewById(R.id.write_one1);
        mImageHide[1] = (Button) findViewById(R.id.write_two1);
        mImageHide[2] = (Button) findViewById(R.id.write_three1);
        mImageHide[3] = (Button) findViewById(R.id.write_four1);
        mImageHide[4] = (Button) findViewById(R.id.write_five1);
        mImageHide[5] = (Button) findViewById(R.id.write_six1);

        mImage[0] = (ImageView) findViewById(R.id.write_one);
        mImage[1] = (ImageView) findViewById(R.id.write_two);
        mImage[2] = (ImageView) findViewById(R.id.write_three);
        mImage[3] = (ImageView) findViewById(R.id.write_four);
        mImage[4] = (ImageView) findViewById(R.id.write_five);
        mImage[5] = (ImageView) findViewById(R.id.write_six);

        mBtn[0] = (Button) findViewById(R.id.write_close01);
        mBtn[1] = (Button) findViewById(R.id.write_close02);
        mBtn[2] = (Button) findViewById(R.id.write_close03);
        mBtn[3] = (Button) findViewById(R.id.write_close04);
        mBtn[4] = (Button) findViewById(R.id.write_close05);
        mBtn[5] = (Button) findViewById(R.id.write_close06);

        mEtText = (CustomEditText) findViewById(R.id.write_et01);
        mEtText.setOnTextLengthListener(this);

        mEtText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.write_et01) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() >= 1000) {
                    mEtText.setText(s.toString().substring(0, s.length() - 1));
                    mEtText.setSelection(s.length() - 1);
                    Utils.setToast(NewsFeedWriteActivity.this, R.string.newsfeed_write);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        for (int i = 0; i < mImage.length; i++) {
            mImage[i].setVisibility(View.GONE);
            mBtn[i].setVisibility(View.GONE);
            mImageHide[i].setVisibility(View.GONE);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_btn01:
                getGallary(0);
                break;
            case R.id.write_btn02:
                getGallary(1);
                break;
            case R.id.write_btn03:
                getGallary(2);
                break;
            case R.id.write_btn04:
                getGallary(3);
                break;
            case R.id.write_btn05:
                getGallary(4);
                break;
            case R.id.write_btn06:
                getGallary(5);
                break;
            case R.id.write_close01:
                delImage(0);
                break;
            case R.id.write_close02:
                delImage(1);
                break;
            case R.id.write_close03:
                delImage(2);
                break;
            case R.id.write_close04:
                delImage(3);
                break;
            case R.id.write_close05:
                delImage(4);
                break;
            case R.id.write_close06:
                delImage(5);
                break;
            case R.id.write_upload:
                boolean isHere = false;
                for (int i = 0; i < mImage.length; i++) {
                    if (mBtn[i].getVisibility() == View.VISIBLE) {
                        isHere = true;
                        break;
                    } else {
                        isHere = false;
                    }
                }

                if (StarSetting.getNewsfeedSTAR_SELECT_INDEX(this).equals("") || StarSetting.getNewsfeedSTAR_SELECT_INDEX(this).equals("0")) {
                    Intent intent = new Intent(this, FirstStarListActivity.class);
                    startActivityForResult(intent, 0);

                } else {
                    if (isHere)
                        upLoadImage();
                    else {
                        if (Utils.checkLength(mEtText)) {
                            Utils.setToast(this, R.string.upload03);
                        } else {
                            upLoadImage();
                        }
                    }
                }
                break;
        }
    }

    //이미지 삭제시 셋팅
    private void delImage(int position) {
        mImage[position].setImageBitmap(null);
        mImage[position].setVisibility(View.GONE);
        mBtn[position].setVisibility(View.GONE);
        mImageHide[position].setVisibility(View.GONE);
    }

    //갤러리에서 이미지 가져오기.
    private void getGallary(int position) {
        mPosition = position;
        int getMax = 0;
        for (int i = 0; i < mImage.length; i++) {
            if (mImage[i].getVisibility() == View.GONE) {
                getMax++;
            }
        }

        Intent intent = new Intent(this, PhotoSelectorActivity.class);
        intent.putExtra(PhotoSelectorActivity.KEY_MAX, getMax);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, Utils.NEWSFEED_UPLOAD);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Utils.NEWSFEED_UPLOAD) {

            List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
            for (int i = 0; i < photos.size(); i++) {
                getImage(photos.get(i).getOriginalPath());
            }

        } else if (requestCode == 0) {
            StarSetting.setNewsfeedSTAR_SELECT_INDEX(this, data.getStringExtra("starIndex"));
            StarSetting.setNewsfeedSTAR_SELECT_NAME(this, data.getStringExtra("starName"));
            upLoadImage();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //갤러리에서 이미지 가져오기.
    private void getImage(String imgURL) {
        try {
            if (imgURL != null) {
                for (int i = 0; i < mImage.length; i++) {
                    if (mImage[i].getVisibility() == View.GONE) {
                        mPosition = i;
                        break;
                    }
                }

                mImagePath[mPosition] = new File(imgURL);
                Bitmap image = BitmapFactory.decodeFile(mImagePath[mPosition].toString());
                image = BitmapOption.resizeBitmapImage(image, 1000);
                ExifInterface exif = new ExifInterface(mImagePath[mPosition].toString());
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = BitmapOption.exifOrientationToDegrees(exifOrientation);
                image = BitmapOption.rotate(image, exifDegree);
                mImage[mPosition].setImageBitmap(image);
                mImage[mPosition].setVisibility(View.VISIBLE);
                mBtn[mPosition].setVisibility(View.VISIBLE);
                mImageHide[mPosition].setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //URI를 통하여 이미지 가져오기.
    private void getImage(Uri imgUri) {
        String[] IMAGE_PROJECTION = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns._ID,};
        try {
            Cursor cursorImages = getContentResolver().query(imgUri, IMAGE_PROJECTION, null, null, null);
            if (cursorImages != null && cursorImages.moveToLast()) {

                for (int i = 0; i < mImage.length; i++) {
                    if (mImage[i].getVisibility() == View.GONE) {
                        mPosition = i;
                        break;
                    }
                }

                mImagePath[mPosition] = new File(cursorImages.getString(0));
                cursorImages.close();
                Bitmap image = BitmapFactory.decodeFile(mImagePath[mPosition].toString());
                image = BitmapOption.resizeBitmapImage(image, 1000);
                ExifInterface exif = new ExifInterface(mImagePath[mPosition].toString());
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = BitmapOption.exifOrientationToDegrees(exifOrientation);
                image = BitmapOption.rotate(image, exifDegree);
                mImage[mPosition].setImageBitmap(image);
                mImage[mPosition].setVisibility(View.VISIBLE);
                mBtn[mPosition].setVisibility(View.VISIBLE);
                mImageHide[mPosition].setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MultipartEntity mReqEntity;
    ByteArrayBody bab;

    private void upLoadImage() {
        int mCount = 0;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            mReqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            mReqEntity.addPart("star_srl", new StringBody(StarSetting.getNewsfeedSTAR_SELECT_INDEX(this)));
            for (int i = 0; i < mImage.length; i++) {
                if (mImage[i].getVisibility() == View.VISIBLE) {
                    BitmapOption.changeRotate(mImagePath[i].toString());
                    options.inSampleSize = 2;
                    Bitmap rebm = BitmapFactory.decodeFile(mImagePath[i].toString(), options);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    rebm.compress(CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    bab = new ByteArrayBody(data, String.valueOf(i) + ".jpg");
                    String param1 = "img_file_" + String.valueOf(mCount + 1);
                    mReqEntity.addPart(param1, bab);

                    mReqEntity.addPart("img_file_" + String.valueOf(mCount + 1) + "_img_ext", new StringBody("jpg"));

                    mCount++;
                }
            }
            mReqEntity.addPart("text", new StringBody(mEtText.getText().toString().trim()));
            mReqEntity.addPart("img_cnt", new StringBody(String.valueOf(mCount)));
            mReqEntity.addPart("sskey", new StringBody(FanMindSetting.getSESSION_KEY(this)));
            mReqEntity.addPart("ssid", new StringBody(FanMindSetting.getEMAIL_ID(this)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Async().execute("upload");
    }

    String mResult;

    private class Async extends android.os.AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            if (params[0].equals("upload")) {

                mResult = HttpRequest.upLoad(URLAddress.NEWSFEED_WRITE(NewsFeedWriteActivity.this), mReqEntity);
                if (mResult == null) {
                    publishProgress(0);
                } else {
                    publishProgress(1);
                }
            }
            return 1;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            if (dialogUtil == null) {
                dialogUtil = new DialogUtil();
                dialogUtil.showProgress(NewsFeedWriteActivity.this);
            }

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);

            if (values[0] == 0) {
                Utils.setToast(NewsFeedWriteActivity.this, R.string.networkerror);
            } else {
                if (!mResult.contains("errcode") && !mResult.contains("900")) {
                    Utils.setToast(NewsFeedWriteActivity.this, R.string.networkerror);
                } else {
                    if (Utils.getJsonData(mResult)) {
                        Utils.setToast(NewsFeedWriteActivity.this, R.string.upload06);
                        setResult(RESULT_OK);
                        ActivityManager.getInstance().deleteActivity(NewsFeedWriteActivity.this);
                    } else {
                        Utils.setToast(NewsFeedWriteActivity.this, R.string.upload05);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            if (dialogUtil != null) {
                dialogUtil.dismissProgress();
                dialogUtil = null;
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().deleteActivity(NewsFeedWriteActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTextLength(int length) {
        // TODO Auto-generated method stub
        String mWord = getResources().getString(R.string.newsfeed_writelength).replace("{LENGTH}",
                String.valueOf(length));
        mWordCheck.setText(mWord);
        Log.e("123", "123");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
