package specup.fanmind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;


public class StartActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Utils.makeDir(this);
    }

    CustomDialog mDialog;

    public void onStart(View v) {

        if (getIntent().getBooleanExtra("logout", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            FanMindSetting.setLogout(this);

        }else{
            welcomDialog() ;
        }
    }


    private void welcomDialog() {
        String title = getString(R.string.welcome01);
        String content = getString(R.string.welcome02);
        String btn = getString(R.string.confirmation);
        mDialog = new CustomDialog(this, title, content,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        FanMindSetting.setAPP_FIRST(StartActivity.this, false);

                        startActivity(intent);
                        mDialog.dismiss();
                        finish();
                    }
                }, btn);
        mDialog.show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

}
