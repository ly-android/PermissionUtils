package com.allen.android.permissionutils;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.allen.android.lib.PermissionUtils;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == 0) {
//      Toast.makeText(this, "有权限！", Toast.LENGTH_SHORT).show();
//    }
    if (PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      Toast.makeText(this, "有权限！", Toast.LENGTH_SHORT).show();
    }
    findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          PermissionUtils.toPermissionSetting(MainActivity.this);
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
