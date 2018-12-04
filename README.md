# JPermission
Android（安卓）基于注解的6.0权限动态申请

# 支持的场景
1. activity
2. fragment
3. service
4. 自定义view
5. 无上下文的类中(需要通过 JPermissionHelper 注入context)

# 如何接入
1、在项目的gradle中添加如下代码
```
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.2' //添加这一行
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }		//添加这一行
    }
}
```
>值得一提：如果你的工程里gradle版本是3.0.0以上，请使用aspectjx：1.1.0以上版本，否则会报**Required: PROJECT, SUB_PROJECTS, EXTERNAL_LIBRARIES. Found: EXTERNAL_LIBRARIES, PROJECT, PROJECT_LOCAL_DEPS, SUB_PROJECTS, SUB_PROJECTS_LOCAL_DEPS**

aspectjx历史版本查看地址：https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx/blob/master/CHANGELOG.md

2、在app的module中增加如下
代码
```
apply plugin: 'com.android.application'
apply plugin: 'android-aspectjx' //添加这一行
```
在依赖中增加：
```
implementation 'com.github.zincPower:JPermission:0.5'
```

# 简单使用
1、在需要请求权限的方法加上注解 @Permission，请求权限可以多个，如下
```
//requestCode可设可不设，框架自带默认值。在取消和拒绝回调中，会将这个值返回，用于各自请求逻辑处理
@Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 100)
private void requestOnePermission() {
    //do something
}
```
2、编写取消和拒绝（即点击了“不再提示”）回调，如下：
```
//只需加上注解 @PermissionCanceled 和 参数类型为 CancelInfo 的一个参数即可
@PermissionCanceled()
private void cancel(CancelInfo cancelInfo) {
   //do something when the permission was canceled.
}
//只需加上注解 @PermissionDenied 和 参数类型为 DenyInfo 的一个参数即可
@PermissionDenied()
private void deny(DenyInfo denyInfo) {
   //do something when the permission was denied.
}
```
值得一提：如果被拒绝，可以弹一个对话框，询问是否要前往系统权限页面让用户自己手动开启。如果需要的话，可以通过以下代码前往（对话框自行解决，本框架不包含）：
```
//前往开启权限的界面
JPermissionUtil.goToMenu(context);
```

# 无上下文的类中如何使用
1、需要通过 JPermissionHelper 提供一个context，**当然不注入也能正常使用，但只能在有上下文的类中使用**，注入代码如下
```
JPermissionHelper.injectContext(this);
```
> 温馨提示：为了避免内存泄漏，请于Application中注入

2、无上下文的类中如下编写(用法与第一点“简单使用”一样)，只需要在需要的地方调用对应的方法。例如调用此处的requestInNormalClass方法即可
```
public class NoneContext {

    private final String TAG = NoneContext.class.getSimpleName();

    @Permission(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 200)
    public void requestInNormalClass() {
        Log.i(TAG, "请求定位权限成功，200");
    }

    @PermissionCanceled()
    private void cancel(CancelInfo cancelInfo) {
        Log.i(TAG, "cancel:" + cancelInfo.getRequestCode());
    }

    @PermissionDenied()
    private void deny(DenyInfo denyInfo) {
        Log.i(TAG, "deny [code:" + denyInfo.getRequestInfo() + " ; deny:" + denyInfo.getDeniedPermissions() + "]");
    }

}
```

# 高级使用
1、请求manifest中的所有权限（主要用于app开启时，进行一次权限请求）
```
//不需要回调监听
JPermissionUtil.requestAllPermission(this);

//需要回调监听
JPermissionUtil.requestAllPermission(this, new IPermission() {
            @Override
            public void ganted() {
                Log.i(JPermissionHelper.TAG, "ganted====》申请manifest的全部");
            }

            @Override
            public void denied(int requestCode, List<String> denyList) {
                Log.i(JPermissionHelper.TAG, "denied====》申请manifest的全部{code=" + requestCode + ";denyList=" + denyList + "}");
            }

            @Override
            public void canceled(int requestCode) {
                Log.i(JPermissionHelper.TAG, "canceled===》申请manifest的全部{code= " + requestCode + "}");
            }
        });
```
某些特殊情况，初始请求中比较敏感（例如：读取手机短信 或 定位权限 等），可以使用以下代码进行剔除初始请求中所包含的权限
```
List<String> excluePermission = new ArrayList<>();
excluePermission.add(Manifest.permission.CAMERA);
excluePermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//不需要回调
JPermissionUtil.requestAllPermission(this, excluePermission);

//需要回调
JPermissionUtil.requestAllPermission(this, excluePermission, new IPermission() {
    @Override
    public void ganted() {
        
    }

    @Override
    public void denied(int requestCode, List<String> denyList) {

    }

    @Override
    public void canceled(int requestCode) {

    }
});

```

2、配置前往的系统权限页，框架自带了默认的系统权限页，但如需根据不同品牌进行个性化设置，可在代码增加如下代码
```
//第一个参数为品牌，框架会根据Build.MANUFACTURER进行匹配（大小写均可）
//第二个参数为需要处理的类class文件，需要实现IMenu接口
JPermissionUtil.setManuFacturer("genymotion", MyTestGenymotionMenu.class);
```

3、设置有回调值的取消和拒绝回调。如果请求权限时，填了requestCode=200，则取消或拒绝时会调用requestCode相同（此处即为200）的方法。
```
@PermissionCanceled(requestCode = 200)
private void cancelCode200(CancelInfo cancelInfo){
    Toast.makeText(this, "cancel__200", Toast.LENGTH_SHORT).show();
}

@PermissionDenied(requestCode = 200)
private void denyCode200(DenyInfo denyInfo){
    Toast.makeText(this, "deny__200", Toast.LENGTH_SHORT).show();
}
```
值得一提：调用了requestCode相同的方法，同时也会调用不设置requestCode的方法。

4、service和fragment中均可使用
