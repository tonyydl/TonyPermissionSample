package com.example.tonyyang.tonypermissionsample;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int RC_CAMERA = 0x00001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getCameraPermissionsBtn = findViewById(R.id.get_camera_permissions_btn);
        getCameraPermissionsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cameraPermissionTask();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @AfterPermissionGranted(RC_CAMERA)
    private void cameraPermissionTask() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Log.i(TAG, "Already have permission, do the thing");
        } else {
            Log.i(TAG, "Do not have permissions, request them now");
            /**
             * 使用EasyPermissions.requestPermissions向使用者索取權限(perms)，
             * 在這個method是向使用者索取CAMERA Permission，
             * 還有設定RationaleDialog的文字敘述，
             * 在這個例子是使用預設的外觀，不設置positive button和negative button
             * 在使用者拒絕PermissionDialog至少一次時，會出現RationaleDialog
             * Android Permission System告訴他們應該顯示RationaleDialog才是最佳的實踐
             * https://github.com/googlesamples/easypermissions/issues/69
             */
            EasyPermissions.requestPermissions(this, getString(R.string.need_camera_permission_description),
                    RC_CAMERA, perms);
        }
    }

}
