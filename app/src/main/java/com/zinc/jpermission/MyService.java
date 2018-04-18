package com.zinc.jpermission;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionHelper;
import com.zinc.libpermission.utils.JPermissionUtil;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestCamera();

        return super.onStartCommand(intent, flags, startId);
    }

    @Permission(Manifest.permission.CAMERA)
    private void requestCamera() {
        Toast.makeText(getApplicationContext(),"SERVICE中请求权限——通过",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny(DenyInfo denyInfo) {
        Log.i(JPermissionHelper.TAG, "SERVICE中请求权限_writeDeny: code:" + denyInfo.getRequestInfo() + " ; deny:" + denyInfo.getDeniedPermissions());
        Toast.makeText(this, "SERVICE中请求权限_deny", Toast.LENGTH_SHORT).show();

        //前往开启权限的界面
        JPermissionUtil.goToMenu(this);
    }

    @PermissionCanceled()
    private void cancel(CancelInfo cancelInfo) {
        Log.i(JPermissionHelper.TAG, "SERVICE中请求权限_writeCancel: " + cancelInfo.getRequestCode());
        Toast.makeText(this, "SERVICE中请求权限_cancel", Toast.LENGTH_SHORT).show();
    }

}
