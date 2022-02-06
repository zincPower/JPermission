package com.zinc.jpermission;

import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/4
 * @description
 */
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
