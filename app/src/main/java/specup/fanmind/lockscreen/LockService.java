package specup.fanmind.lockscreen;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class LockService extends Service{
	NotificationManager notiManager;
	LockScreenReciver mReceiver;
	NotificationCompat.Builder notification;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		mReceiver = new LockScreenReciver();	
		registerReceiver(mReceiver, filter);
		
	}


	
	
//	@SuppressWarnings("deprecation")
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId){
//		super.onStartCommand(intent, flags, startId);
//
//		if(intent != null){
//			if(intent.getAction()==null){
//				if(mReceiver==null){
//					IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//					mReceiver = new LockScreenReciver();					
//					registerReceiver(mReceiver, filter);
//				}
//			}
//		}
//		
//		
//		notiManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//		if(Build.VERSION.SDK_INT>17){
//			Intent  gointent = new Intent(getApplicationContext(), IntroActivity.class);
//			gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
//	                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, gointent, PendingIntent.FLAG_UPDATE_CURRENT);
//			
//			Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_fanmind);
//			notification = new NotificationCompat.Builder(getApplicationContext())
//			.setPriority(NotificationCompat.PRIORITY_MIN)
//			.setContentTitle("팬마음")
//			.setContentText("최초의 연예인 무료 서포트 앱")
//			.setSmallIcon(R.drawable.icon_128)
//			.setContentIntent(pendingIntent)
//			.setWhen(0);
//			
//			notification.build();
//		
//			startForeground(R.string.notification, notification.build());
//		}else{
//			startForeground(R.string.notification, new Notification());
//		}
//		
//		return START_STICKY;
//	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			if(intent.getAction()==null){
				if(mReceiver==null){
					IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
					filter.addAction(Intent.ACTION_SCREEN_ON);
					mReceiver = new LockScreenReciver();					
					registerReceiver(mReceiver, filter);
				}
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		if(mReceiver != null){
			unregisterReceiver(mReceiver);
//			stopForeground(true);
//			stopSelf();
		}
	}
}
