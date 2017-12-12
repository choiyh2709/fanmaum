package com.photoselector.ui;
/**
 * @author Aizaz AZ
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.photoselector.R;
import com.photoselector.domain.PhotoSelectorDomain;
import com.photoselector.model.AlbumModel;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoItem.onItemClickListener;
import com.photoselector.ui.PhotoItem.onPhotoItemCheckedListener;
import com.photoselector.util.AnimationUtil;
import com.photoselector.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;

/**
 * @author Aizaz AZ
 */
public class PhotoSelectorActivity extends Activity implements
        onItemClickListener, onPhotoItemCheckedListener, OnItemClickListener,
        OnClickListener {

    public static final int SINGLE_IMAGE = 1;
    public static final String KEY_MAX = "key_max";
    private int MAX_IMAGE;

    public static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_CAMERA = 1;

    public static String RECCENT_PHOTO = null;

    private GridView gvPhotos;
    private ListView lvAblum;
    private Button btnOk;
    private TextView tvAlbum, tvPreview, tvTitle;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoSelectorAdapter photoAdapter;
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutAlbum;
    private ArrayList<PhotoModel> selected;
    private TextView tvNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RECCENT_PHOTO = getResources().getString(R.string.recent_photos);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 鍘绘帀鏍囬鏍�
        setContentView(R.layout.activity_photoselector);

        if (getIntent().getExtras() != null) {
            MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 10);
        }

        initImageLoader();

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        selected = new ArrayList<PhotoModel>();

        tvTitle = (TextView) findViewById(R.id.tv_title_lh);
        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        btnOk = (Button) findViewById(R.id.btn_right_lh);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        tvAlbum.setText(getResources().getString(R.string.recent_photos));
        tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvNumber.setText("(0 / " + MAX_IMAGE + ")");

        btnOk.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        tvPreview.setOnClickListener(this);

        photoAdapter = new PhotoSelectorAdapter(getApplicationContext(), new ArrayList<PhotoModel>(), CommonUtils.getWidthPixels(this),
                this, this, this);
        gvPhotos.setAdapter(photoAdapter);

        albumAdapter = new AlbumAdapter(getApplicationContext(),
                new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);

        findViewById(R.id.bv_back_lh).setOnClickListener(this); // 杩斿洖

        photoSelectorDomain.getReccent(reccentListener); // 鏇存柊鏈�繎鐓х墖
        photoSelectorDomain.updateAlbum(this, albumListener); // 璺熸柊鐩稿唽淇℃伅
    }

    private void initImageLoader() {
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_picture_loading)
                .showImageOnFail(R.drawable.ic_picture_loadfailed)
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true).considerExifParams(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(400, 400)
                // default = device screen dimensions
                .diskCacheExtraOptions(400, 400, null)
                .threadPoolSize(5)
                // default Thread.NORM_PRIORITY - 1
                .threadPriority(Thread.NORM_PRIORITY)
                // default FIFO
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
//				// default
//				.diskCache(
//						new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
//								this, true)))
                // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // default
                .imageDownloader(new BaseImageDownloader(this))
                // default
                .imageDecoder(new BaseImageDecoder(false))
                // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // default
                .defaultDisplayImageOptions(imageOptions).build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_right_lh) {
            if (selected.size() > MAX_IMAGE) {
                Toast.makeText(this, String.format(getString(R.string.max_img_limit_reached), MAX_IMAGE), Toast.LENGTH_SHORT).show();
                photoAdapter.notifyDataSetChanged();
                return;
            }

            ok(); // 閫夊畬鐓х墖
        } else if (v.getId() == R.id.tv_album_ar)
            album();
        else if (v.getId() == R.id.tv_preview_ar)
            priview();
        else if (v.getId() == R.id.tv_camera_vc)
            catchPicture();
        else if (v.getId() == R.id.bv_back_lh)
            finish();
    }

    /**
     * 鎷嶇収
     */
    private void catchPicture() {
        CommonUtils.launchActivityForResult(this, new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            PhotoModel photoModel = new PhotoModel(CommonUtils.query(
                    getApplicationContext(), data.getData()));
            if (selected.size() > MAX_IMAGE) {
                Toast.makeText(
                        this,
                        String.format(
                                getString(R.string.max_img_limit_reached),
                                MAX_IMAGE), Toast.LENGTH_SHORT).show();
                photoModel.setChecked(false);
                photoAdapter.notifyDataSetChanged();
            } else {
                if (!selected.contains(photoModel)) {
                    selected.add(photoModel);
                }
            }
            ok();
        }
    }

    /**
     * 瀹屾垚
     */
    private void ok() {
        if (selected.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("photos", selected);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    /**
     * 棰勮鐓х墖
     */
    private void priview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", selected);
        CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
    }

    private void album() {
        if (layoutAlbum.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /**
     * 寮瑰嚭鐩稿唽鍒楄〃
     */
    private void popAlbum() {
        layoutAlbum.setVisibility(View.VISIBLE);
        new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(layoutAlbum);
    }

    /**
     * 闅愯棌鐩稿唽鍒楄〃
     */
    private void hideAlbum() {
        new AnimationUtil(getApplicationContext(), R.anim.translate_down)
                .setLinearInterpolator().startAnimation(layoutAlbum);
        layoutAlbum.setVisibility(View.GONE);
    }

    /**
     * 娓呯┖閫変腑鐨勫浘鐗�
     */
    private void reset() {
        selected.clear();
        tvNumber.setText("(0 / " + MAX_IMAGE + ")");
        tvPreview.setEnabled(false);
    }

    @Override
    /** 鐐瑰嚮鏌ョ湅鐓х墖 */
    public void onItemClick(PhotoModel photoModel, boolean isChecked) {
        if (isChecked) {
            if (!selected.contains(photoModel))
                selected.add(photoModel);
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
            if (selected.size() <= 0) {
                tvPreview.setEnabled(false);
            }
        }
        tvNumber.setText("(" + selected.size() + " / " + MAX_IMAGE + ")");

        if (selected.isEmpty()) {
            tvPreview.setEnabled(false);
            tvPreview.setText(getString(R.string.preview));
        }
    }

    @Override
    /** 鐓х墖閫変腑鐘舵�鏀瑰彉涔嬪悗 */
    public void onCheckedChanged(PhotoModel photoModel,
                                 CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!selected.contains(photoModel)) {
                selected.add(photoModel);
            }
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
        }
        tvNumber.setText("(" + selected.size() + " / " + MAX_IMAGE + ")");

        if (selected.isEmpty()) {
            tvPreview.setEnabled(false);
            tvPreview.setText(getString(R.string.preview));
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutAlbum.getVisibility() == View.VISIBLE) {
            hideAlbum();
        } else
            super.onBackPressed();
    }

    @Override
    /** 鐩稿唽鍒楄〃鐐瑰嚮浜嬩欢 */
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        AlbumModel albumModel = (AlbumModel) parent.getItemAtPosition(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
            if (i == position)
                album.setCheck(true);
            else
                album.setCheck(false);
        }
        albumAdapter.notifyDataSetChanged();
        hideAlbum();
        tvAlbum.setText(albumModel.getName());

        // 鏇存柊鐓х墖鍒楄〃
        if (albumModel.getName().equals(getResources().getString(R.string.recent_photos))) {
            albumModel.setName(getResources().getString(R.string.recent_photos));
            photoSelectorDomain.getReccent(reccentListener);

        } else
            photoSelectorDomain.getAlbum(albumModel.getName(), reccentListener); // 鑾峰彇閫変腑鐩稿唽鐨勭収鐗�
    }

    /**
     * 鑾峰彇鏈湴鍥惧簱鐓х墖鍥炶皟
     */
    public interface OnLocalReccentListener {
        public void onPhotoLoaded(List<PhotoModel> photos);
    }

    /**
     * 鑾峰彇鏈湴鐩稿唽淇℃伅鍥炶皟
     */
    public interface OnLocalAlbumListener {
        public void onAlbumLoaded(List<AlbumModel> albums);
    }

    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(List<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };

    private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            for (PhotoModel model : photos) {
                if (selected.contains(model)) {
                    model.setChecked(true);
                }
            }
            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0); // 婊氬姩鍒伴《绔�
            // reset(); //--keep selected photos

        }
    };
}
