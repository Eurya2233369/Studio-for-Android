package com.eurya.studio.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eurya.studio.MainActivity;
import com.eurya.studio.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.permissionx.guolindev.PermissionX;
import java.util.ArrayList;
import java.util.List;

/**
 * @autuor Eurya QiuZhu
 * @date 2023.1.16
 * @emali 2644635373@qq.com
 */
 
public class Welcome extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    PermissionX.init(this)
        .permissions(getPermission())
        .request(
            (allGranted, grantedList, deniedList) -> {
              if (allGranted) {
                startMain();
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

  private List<String> getPermission() {
    List<String> permission = new ArrayList<>();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      permission.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
    }
    permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    return permission;
  }

  private void startMain() {
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              startActivity(new Intent(this, MainActivity.class));
              finish();
            },
            1000);
  }
}
