package com.photoselector.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.R;
import com.photoselector.model.PhotoModel;

/**
 * @author Aizaz AZ
 */

public class PhotoItem extends LinearLayout implements OnCheckedChangeListener, OnClickListener {

    private ImageView ivPhoto;
    private CheckBox cbPhoto;
    private onPhotoItemCheckedListener listener;
    private PhotoModel photo;
    private boolean isCheckAll;
    private onItemClickListener l;
    private int position;

    private PhotoItem(Context context) {
        super(context);
    }

    public PhotoItem(Context context, onPhotoItemCheckedListener listener) {
        this(context);
        LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this,
                true);
        this.listener = listener;


        ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);

        setOnClickListener(this);
        cbPhoto.setOnCheckedChangeListener(this); // CheckBox选中状态改变监听器
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isCheckAll) {
            listener.onCheckedChanged(photo, buttonView, isChecked); // 调用主界面回调函数
        }
        // 让图片变暗或者变亮
        if (isChecked) {
            setDrawingable();
            ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            ivPhoto.clearColorFilter();
        }
        photo.setChecked(isChecked);
    }

    /**
     * 设置路径下的图片对应的缩略图
     */
    public void setImageDrawable(final PhotoModel photo) {
        this.photo = photo;

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.image_loading)
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .resetViewBeforeLoading(false)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage("file://" + photo.getOriginalPath(), ivPhoto, imageOptions);
    }

    private void setDrawingable() {
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
    }

    @Override
    public void setSelected(boolean selected) {
        if (photo == null) {
            return;
        }
        isCheckAll = true;
        cbPhoto.setChecked(selected);
        isCheckAll = false;
    }

    public void setOnClickListener(onItemClickListener l, int position) {
        this.l = l;
        this.position = position;
    }

    /**
     * 图片Item选中事件监听器
     */
    public static interface onPhotoItemCheckedListener {
        public void onCheckedChanged(PhotoModel photoModel,
                                     CompoundButton buttonView, boolean isChecked);
    }

    /**
     * 图片点击事件
     */
    public interface onItemClickListener {
        public void onItemClick(PhotoModel photoModel, boolean isChecked);
    }

    @Override
    public void onClick(View v) {

        if (photo == null) {
            return;
        }

        isCheckAll = true;
        if (cbPhoto.isChecked()) {
            cbPhoto.setChecked(false);
        } else {
            cbPhoto.setChecked(true);

        }
        isCheckAll = false;


        if (!isCheckAll) {
            l.onItemClick(photo, cbPhoto.isChecked());
        }
        if (cbPhoto.isChecked()) {
            setDrawingable();
            ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            ivPhoto.clearColorFilter();
        }
        photo.setChecked(cbPhoto.isChecked());


    }
}
