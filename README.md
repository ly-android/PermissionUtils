## how to use it?
1. Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.ly-android:PermissionUtils:1.0.0'
	}
```
3. user PermisstionUtils

```java
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
```
## android 跳转到权限设置界面
>我们知道在6.0之后，android的一些权限需要动态获取，网上很多封装好的动态获取权限框架，如[RxPermisstion](https://github.com/tbruyelle/RxPermissions),[PermissionsDispatcher](https://github.com/permissions-dispatcher/PermissionsDispatcher),[easypermissions](https://github.com/googlesamples/easypermissions) 

**如果项目targetSdkVersion<23,使用它们判断是否授权，会出现在设置中关闭权限，还能判断已经授权，由于国产手机都定制了Rom，需要使用原生api来判断,当然也可以用PermissionChecker.checkSelfPermission**
```java
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
```
**如果没有权限，主动去申请，也会出现弹不出权限允许的对话框，这时候需要跳转到权限设置界面怎么办？**
>这时候希望重新去打开该权限，那么问题来了，Android厂家定制的room五花八门，很多时候却发现找不到权限管理的入口。为了解决这一问题，如果我们应用中直接提供权限管理入口给用户，是不是会很方便的解决用户这一困扰呢?经过一番研究，整理出了大部分国产手机直接打开权限管理界面的方法：

**贴出部分代码**
```java
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
                FloatWindowManager.commonROMPermissionApplyInternal(context);
            }
        }
    }
```
