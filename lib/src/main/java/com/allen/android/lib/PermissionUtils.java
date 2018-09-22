package com.allen.android.lib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.allen.android.lib.rom.HuaweiUtils;
import com.allen.android.lib.rom.MeizuUtils;
import com.allen.android.lib.rom.MiuiUtils;
import com.allen.android.lib.rom.OppoUtils;
import com.allen.android.lib.rom.QikuUtils;
import com.allen.android.lib.rom.RomUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyong on 2018/9/21.
 */
public class PermissionUtils {

  public static final String TAG = "PermissionUtils";

  /**
   * 检查悬浮窗权限
   *
   * @param context
   * @return
   */
  public boolean checkFloatWindowPermission(Context context) {
    //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
    if (Build.VERSION.SDK_INT < 23) {
      if (RomUtils.checkIsMiuiRom()) {
        return MiuiUtils.checkFloatWindowPermission(context);
      } else if (RomUtils.checkIsMeizuRom()) {
        return MeizuUtils.checkFloatWindowPermission(context);
      } else if (RomUtils.checkIsHuaweiRom()) {
        return HuaweiUtils.checkFloatWindowPermission(context);
      } else if (RomUtils.checkIs360Rom()) {
        return QikuUtils.checkFloatWindowPermission(context);
      } else if (RomUtils.checkIsOppoRom()) {
        return OppoUtils.checkFloatWindowPermission(context);
      }
    }
    return commonROMPermissionCheck(context);
  }

  public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
    List<String> permisstions = new ArrayList<>();
    permisstions.add(permission);
    return hasPermission(context, permisstions);
  }

  /**
   * 系统层的权限判断
   *
   * @param context     上下文
   * @param permissions 申请的权限 Manifest.permission.READ_CONTACTS
   * @return 是否有权限 ：其中有一个获取不了就是失败了
   */
  public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
    for (String permission : permissions) {
      String op = AppOpsManagerCompat.permissionToOp(permission);
      if (TextUtils.isEmpty(op)) continue;
      int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
      if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
      result = ContextCompat.checkSelfPermission(context, permission);
      if (result != PackageManager.PERMISSION_GRANTED) return false;
    }
    return true;
  }

  /**
   * 跳转到权限设置界面
   *
   * @param context
   */
  public static void toPermissionSetting(Context context) throws NoSuchFieldException, IllegalAccessException {
    if (Build.VERSION.SDK_INT < 23) {
      if (RomUtils.checkIsMiuiRom()) {
        MiuiUtils.applyMiuiPermission(context);
      } else if (RomUtils.checkIsMeizuRom()) {
        MeizuUtils.applyPermission(context);
      } else if (RomUtils.checkIsHuaweiRom()) {
        HuaweiUtils.applyPermission(context);
      } else if (RomUtils.checkIs360Rom()) {
        QikuUtils.applyPermission(context);
      } else if (RomUtils.checkIsOppoRom()) {
        OppoUtils.applyOppoPermission(context);
      }
    }
    if (RomUtils.checkIsMeizuRom()) {
      MeizuUtils.applyPermission(context);
    } else {
      if (Build.VERSION.SDK_INT >= 23) {
        RomUtils.commonROMPermissionApplyInternal(context);
      }
    }
  }

  private static boolean commonROMPermissionCheck(Context context) {
    //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
    if (RomUtils.checkIsMeizuRom()) {
      return MeizuUtils.checkFloatWindowPermission(context);
    } else {
      Boolean result = true;
      if (Build.VERSION.SDK_INT >= 23) {
        try {
          Class clazz = Settings.class;
          Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
          result = (Boolean) canDrawOverlays.invoke(null, context);
        } catch (Exception e) {
          Log.e(TAG, Log.getStackTraceString(e));
        }
      }
      return result;
    }
  }
}
