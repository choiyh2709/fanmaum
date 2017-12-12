package specup.fanmind.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import specup.fanmind.fanmindsetting.FanMindSetting;

public class LockScreenReciver extends BroadcastReceiver {

    public static boolean isCalling;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state != null) {
            if (state.equals("OFFHOOK") || state.equals("RINGING")) {
                isCalling = true;
            } else if (state.equals("IDLE")) {
                isCalling = false;
            }
        }

        if (!isCalling) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (LockScreenActivity2.mLockScreen == null) {
                    Intent intent11 = new Intent(context, LockScreenActivity2.class);
                    intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent11);
                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (LockScreenActivity2.mLockScreen == null) {
                    Intent intent11 = new Intent(context, LockScreenActivity2.class);
                    intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent11);
                }
            } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                if (FanMindSetting.getLOCKSCREEN(context)) {
                    Intent serviceIntent = new Intent(context, LockService.class);
                    context.startService(serviceIntent);

                    if (LockScreenActivity2.mLockScreen == null) {
                        Intent intent11 = new Intent(context, LockScreenActivity2.class);
                        intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent11);
                    }
                }
            }
        }
    }
}
