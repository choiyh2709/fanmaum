package specup.fanmind.common.Util;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import specup.fanmind.R;


public class DialogUtil {

    private Dialog mProgress;
    AnimationDrawable animation ;
    ImageView imgView;
    public void showProgress(Context context) {
        if (mProgress == null) {
            try {

                mProgress = new Dialog(context);
                mProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                imgView = new ImageView(context);
                imgView.setImageResource(R.anim.progress_loading);
                mProgress.setContentView(imgView);
                imgView.post(new Runnable() {
                    public void run() {
                        if(animation == null) animation = (AnimationDrawable) imgView.getDrawable();
                        animation.stop();
                        animation.start();
                    }
                });

                mProgress.setCancelable(false);
                mProgress.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissProgress() {
        if (mProgress != null) {
            try {
                if (mProgress.isShowing()) {
                    try {
                        mProgress.dismiss();
                        mProgress = null;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}