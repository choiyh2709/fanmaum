package specup.fanmind.common.Util;

import android.app.Activity;
import android.app.FragmentManager;

import java.util.ArrayList;

import specup.fanmind.R;


public class ActivityManager {

	private static ActivityManager sInstance;
	private ArrayList<Activity> mActivityArr;
    public static FragmentManager fragmentManager;

	private ActivityManager(){
		mActivityArr = new ArrayList<Activity>();
	}
	
	public static ActivityManager getInstance(){
		if(ActivityManager.sInstance == null){
			synchronized (ActivityManager.class) {
				if(sInstance == null){
					sInstance = new ActivityManager();
				}
				
			}
		}
		return sInstance;
	}
	
    /**
   	 * @param activity : 액티비티가 있는지 조사
   	 * @return : true->존재
   	 */
	public boolean isActivity(Activity activity){
		for(Activity chkActivity : mActivityArr){
			if(chkActivity == activity){
				return true;
			}
		}
		return false;
	}
	
    /**
   	 * @param activity : onCreate에서 추가
   	 */
	public void addActivity(Activity activity){
		if(!isActivity(activity)){
			mActivityArr.add(activity);
			activity.overridePendingTransition(R.anim.slid_left, R.anim.hold);
		}
	}
	
	public void addActivity2(Activity activity){
		if(!isActivity(activity)){
			mActivityArr.add(activity);
		}
	}
	
	
	public void deleteActivity(Activity activity){
		if(isActivity(activity)){
			activity.finish();
			activity.overridePendingTransition(R.anim.hold, R.anim.slid_out);
			mActivityArr.remove(activity);
		}
	}
	
	public void deleteActivity2(Activity activity){
		if(isActivity(activity)){
			activity.finish();
			mActivityArr.remove(activity);
		}
	}
	
	
	public void allEndActivity(){
		for(Activity activity : mActivityArr){
			activity.finish();
		}
	}

}
