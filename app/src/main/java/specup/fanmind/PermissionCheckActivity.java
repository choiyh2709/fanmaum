package specup.fanmind;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import specup.fanmind.common.Util.Utils;

public class PermissionCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_check);

        findViewById(R.id.permission_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(PermissionCheckActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //허용의 경우
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK,intent);
            finish();
        } else {
            // 실행 할 코드
            Utils.setToast(PermissionCheckActivity.this, getString(R.string.request_permission));
        }
    }
}
