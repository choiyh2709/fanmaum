package specup.fanmind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gcm.GCMBaseIntentService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.EventDetailActivity;
import specup.fanmind.left.MyPointActivity;
import specup.fanmind.left.NoticeActivity;
import specup.fanmind.main.tab0.ProjectDetailActivity;
import specup.fanmind.main.tab2_.NewsFeedDetailActivity;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    public static final String SEND_ID = "489712448482";
    String mType, mBoard, mSrl, mMsg, mReplySrl, member_srl, reply_on;
    String mImg_Base, mImg, mStar_Srl, mUrl;

    public GCMIntentService() {
        this(SEND_ID);
    }

    public GCMIntentService(String senderId) {
        super(senderId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle b = intent.getExtras();

        for (String key : b.keySet()) {
            if (key.equals("message")) {
                Object value = b.get(key);
                mMsg = value.toString();
            } else if (key.equals("srl")) {
                Object value = b.get(key);
                mSrl = value.toString();
            } else if (key.equals("type")) {
                Object value = b.get(key);
                mType = value.toString();
            } else if (key.equals("reply_srl")) {
                Object value = b.get(key);
                mReplySrl = value.toString();
            } else if (key.equals("board")) {
                Object value = b.get(key);
                mBoard = value.toString();
            } else if (key.equals("img_base")) {
                Object value = b.get(key);
                mImg_Base = value.toString();
            } else if (key.equals("img")) {
                Object value = b.get(key);
                mImg = value.toString();
            } else if (key.equals("star_srl")) {
                Object value = b.get(key);
                mStar_Srl = value.toString();
            } else if (key.equals("url")) {
                Object value = b.get(key);
                mUrl = value.toString();
            } else if (key.equals("member_srl")) {
                Object value = b.get(key);
                member_srl = value.toString();
            } else if (key.equals("reply_on")) {
                Object value = b.get(key);
                reply_on = value.toString();
            }
        }

        Log.e("onMessage", b.toString());
        if (mType != null) {
            if (mType.equals("R")) { // 댓글 푸쉬
                if (FanMindSetting.getLOGIN_OK(getApplicationContext())) {
                    if (FanMindSetting.getALERT_MYNEWS(getApplicationContext())) {
                        notiReply();
                    }
                }
            } else if (mType.equals("N")) {// 공지사항 푸쉬
                if (FanMindSetting.getALERT_FANMIND(getApplicationContext())) {
                    if (mImg == null) {
                        notiNotice(null);
                    } else {
                        graphHandler.sendEmptyMessage(0);
                    }
                }
            } else if (mType.equals("E")) {// 이벤트 푸쉬
                if (FanMindSetting.getALERT_FANMIND(getApplicationContext())) {
                    if (mImg == null) {
                        notiEvent(null);
                    } else {
                        graphHandler.sendEmptyMessage(0);
                    }
                }
            } else if (mType.equals("S")) {// 서포트 푸쉬
                if (FanMindSetting.getALERT_FANMIND(getApplicationContext())) {
                    if (FanMindSetting.getLOGIN_OK(this)) {
                        if (mImg == null) {
                            notiSupport(null);
                        } else {
                            graphHandler.sendEmptyMessage(0);
                        }
                    }
                }
            } else if (mType.equals("W")) {// 적립 푸쉬
                if (FanMindSetting.getALERT_SAVE(getApplicationContext())) {
                    if (FanMindSetting.getLOGIN_OK(this)) {
                        notiSave();
                    }
                }
            } else if (mType.equals("BU")) {// 배너 웹
                graphHandler.sendEmptyMessage(1);
            } else if (mType.equals("BS")) {// 배너서포트
                graphHandler.sendEmptyMessage(1);
            } else if (mType.equals("BE")) {// 배너 이벤트
                graphHandler.sendEmptyMessage(1);
            } else if (mType.equals("BN")) {// 배너 공지사항
                graphHandler.sendEmptyMessage(1);
            } else if (mType.equals("P")) {// 구매하기
                graphHandler.sendEmptyMessage(2);
            }
        }
    }

    private void bannerPush() {
        if (mType.equals("BS")) {
            mSrl = mStar_Srl + "/" + mSrl;
        } else if (mType.equals("BU")) {
            mSrl = mUrl;
        }
        String info = mImg_Base + mImg + "," + mType + "," + mSrl;

        if (((MainActivity) MainActivity.mMainActivity) == null) {
            FanMindSetting.setBANNER_INFO(getApplicationContext(), info);
            FanMindSetting.setBANNER(getApplicationContext(), true);
        } else {
            ((MainActivity) MainActivity.mMainActivity).showBanner(
                    mImg_Base + mImg, mType, mSrl);
        }
    }


    private void notiSupport(Bitmap banner) {
        PendingIntent pendingIntent;
        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent gointent = new Intent(getApplicationContext(),
                    IntroActivity.class);
            gointent.putExtra("noti", true);
            gointent.putExtra("index", 15);
            gointent.putExtra("srl", mSrl);
            gointent.putExtra("star_srl", mStar_Srl);
            gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, gointent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    ProjectDetailActivity.class);
            intent.putExtra("srl", mSrl);
            intent.putExtra("star_srl", mStar_Srl);
            intent.putExtra("isNoti2", true);
            intent.putExtra("index", 15);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String msg[] = mMsg.split(":");
        String title = msg[0];
        String message = msg[1];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()).setSmallIcon(R.drawable.icon_128)
                .setContentTitle(title).setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        if (banner != null) {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle(title);
            style.setSummaryText(message);
            style.bigPicture(banner);
            builder.setStyle(style);
        }

        builder.setContentIntent(pendingIntent);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void notiEvent(Bitmap banner) {
        PendingIntent pendingIntent;
        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent gointent = new Intent(getApplicationContext(),
                    IntroActivity.class);
            gointent.putExtra("noti", true);
            gointent.putExtra("index", 14);
            gointent.putExtra("srl", mSrl);
            gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, gointent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    EventDetailActivity.class);
            intent.putExtra("srl", mSrl);
            intent.putExtra("index", 14);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String msg[] = mMsg.split(":");
        String title = msg[0];
        String message = msg[1];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()).setSmallIcon(R.drawable.icon_128)
                .setContentTitle(title).setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        if (banner != null) {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle(title);
            style.setSummaryText(message);
            style.bigPicture(banner);
            builder.setStyle(style);
        }

        builder.setContentIntent(pendingIntent);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void notiNotice(Bitmap banner) {
        PendingIntent pendingIntent;
        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent gointent = new Intent(getApplicationContext(), IntroActivity.class);
            gointent.putExtra("noti", true);
            gointent.putExtra("index", 13);
            gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, gointent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    NoticeActivity.class);
            intent.putExtra("index", 13);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String msg[] = mMsg.split(":");
        String title = msg[0];
        String message = msg[1];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()).setSmallIcon(R.drawable.icon_128)
                .setContentTitle(title).setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        if (banner != null) {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle(title);
            style.setSummaryText(message);
            style.bigPicture(banner);
            builder.setStyle(style);
        }

        builder.setContentIntent(pendingIntent);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void notiSave() {// �� ��Ƽ.
        FanMindSetting.setPUSH_STAY(getApplicationContext(), true);
        String msg[] = mMsg.split(":");
        //		msg[0] = getString(R.string.app_name);
        //		msg[1] = getString(R.string.dailysave).replace("{POINT}", msg[1]);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle(msg[0])
                .setContentText(msg[1]).setSmallIcon(R.drawable.icon_128)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        notification.setDefaults(Notification.DEFAULT_VIBRATE);
        notification.setLights(Color.RED, 3000, 3000);
        notification.setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }

    private void notiReply() {// ��� ��Ƽ.
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_128);
        FanMindSetting.setPUSH_STAY(getApplicationContext(), true);
        String msg[] = mMsg.split(":");
        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle(msg[0])
                .setContentText(msg[1]).setSmallIcon(R.drawable.icon_128)
                .setAutoCancel(true);

        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent gointent = new Intent(getApplicationContext(), IntroActivity.class);
            gointent.putExtra("noti", true);
            gointent.putExtra("srl", mSrl);
            gointent.putExtra("board", mBoard);
            gointent.putExtra("index", 12);
            gointent.putExtra("replysrl", mReplySrl);
            gointent.putExtra("member_srl", member_srl);
            gointent.putExtra("reply_on", reply_on);
            gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 1, gointent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
        } else {
            if (mBoard.equals("1")) {//프로젝트
                Intent intent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isNoti", true);
                intent.putExtra("srl", mSrl);
                intent.putExtra("replysrl", mReplySrl);
                intent.putExtra("member_srl", member_srl);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
            } else if (mBoard.equals("11")) {//이벤트
                Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isNoti", true);
                intent.putExtra("srl", mSrl);
                intent.putExtra("replysrl", mReplySrl);
                intent.putExtra("member_srl", member_srl);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
            }else if (mBoard.equals("4")) {//뉴스피드
                Intent intent = new Intent(getApplicationContext(), NewsFeedDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isNoti", true);
                intent.putExtra("srl", mSrl);
                intent.putExtra("replysrl", mReplySrl);
                intent.putExtra("member_srl", member_srl);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

            } else if (mBoard.equals("7")) {//답글
                if (reply_on.equals("1")) {//프로젝트
                    Intent intent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("isNoti", true);
                    intent.putExtra("srl", mSrl);
                    intent.putExtra("reply_on", reply_on);
                    intent.putExtra("replysrl", mReplySrl);
                    intent.putExtra("member_srl", member_srl);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);
                } else if (reply_on.equals("11")) {//이벤트
                    Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("isNoti", true);
                    intent.putExtra("srl", mSrl);
                    intent.putExtra("reply_on", reply_on);
                    intent.putExtra("replysrl", mReplySrl);
                    intent.putExtra("member_srl", member_srl);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);
                }
            }
        }
        notification.setDefaults(Notification.DEFAULT_VIBRATE);
        notification.setLights(Color.RED, 3000, 3000);
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d(tag, "onError. errorId : " + errorId);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        Log.e(tag, "onRegistered :" + regId);
        // CoupleRecipeSetting.setGCM_ID(GCMIntentService.this, regId);
        FanMindSetting.setGCM_ID(GCMIntentService.this, regId);
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(tag, "onUnregistered. regId : " + regId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.d(tag, "onRecoverableError. errorId : " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    public Handler graphHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // ��������
                    ImageView mImage = new ImageView(getApplicationContext());
                    String url = mImg_Base + mImg;
                    ImageLoader.getInstance().displayImage(url, mImage,
                            new ImageLoadingListener() {

                                @Override
                                public void onLoadingStarted(String arg0, View arg1) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onLoadingFailed(String arg0, View arg1,
                                                            FailReason arg2) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onLoadingComplete(String arg0,
                                                              View arg1, Bitmap arg2) {
                                    // TODO Auto-generated method stub
                                    if (mType.equals("E")) {
                                        notiEvent(arg2);
                                    } else if (mType.equals("N")) {
                                        notiNotice(arg2);
                                    } else if (mType.equals("S")) {
                                        notiSupport(arg2);
                                    }
                                }

                                @Override
                                public void onLoadingCancelled(String arg0,
                                                               View arg1) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    break;
                case 1:
                    bannerPush();
                    break;
                case 2:
                    notiSave(mMsg);
                    break;
            }
        }
    };

    private void notiSave(String point) {// �� ��Ƽ.
        FanMindSetting.setMY_HEART(this, mSrl);
        if (((MyPointActivity) MyPointActivity.mMyPointContext) != null)
            ((MyPointActivity) MyPointActivity.mMyPointContext).mMyPoint.setText(Utils.getMoney(mSrl));

        PendingIntent pendingIntent;

        String maum = getString(R.string.paysave).replace("{POINT}", point);
        FanMindSetting.setPUSH_STAY(getApplicationContext(), true);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(maum).setSmallIcon(R.drawable.icon_128)
                .setAutoCancel(true);

        if (((MainActivity) MainActivity.mMainActivity) == null) {
            Intent gointent = new Intent(getApplicationContext(),
                    IntroActivity.class);
            gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, gointent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent();
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        notification.setContentIntent(pendingIntent);
        notification.setDefaults(Notification.DEFAULT_VIBRATE);
        notification.setLights(Color.RED, 3000, 3000);
        notification.setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }

}