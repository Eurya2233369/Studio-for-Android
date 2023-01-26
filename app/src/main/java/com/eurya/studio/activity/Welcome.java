package com.eurya.studio.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eurya.studio.MainActivity;
import com.eurya.studio.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.permissionx.guolindev.PermissionX;

/**
 * @autuor Eurya QiuZhu
 * @time 2023.1.16
 * @emali 2644635373@qq.com
 */
public class Welcome extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_welcome);
    PermissionX.init(this)
        .permissions(
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        .request(
            (allGranted, grantedList, deniedList) -> {
              if (allGranted) {
                new Handler(Looper.getMainLooper())
                    .postDelayed(
                        () -> {
                          startActivity(new Intent(this, MainActivity.class));
                          finish();
                        },
                        1000);
              } else {
                new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.app_permission_required)
                    .setMessage(R.string.app_permission_cannot_run)
                    .setPositiveButton(
                        getString(R.string.app_exit),
                        (dia, witch) -> {
                          finish();
                        })
                    .create()
                    .show();
              }
            });
  }
}
