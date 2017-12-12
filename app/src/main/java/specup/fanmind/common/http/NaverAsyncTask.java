package specup.fanmind.common.http;

import android.content.Context;

import com.nhn.android.naverlogin.OAuthLogin;

/**
 * Created by DEV-06 on 2016-03-14.
 */
public class NaverAsyncTask extends android.os.AsyncTask {
    Context mContext;

    public NaverAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        final OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
        try {
//            mOAuthLoginModule.logoutAndDeleteToken(mContext);
            mOAuthLoginModule.logout(mContext);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
