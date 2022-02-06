package com.zinc.jpermission;

import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/3
 * @description
 */
public class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";

    @Permission(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 500)
    protected void requestInBase() {
        Toast.makeText(this, "请求定位权限成功，500", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled(requestCode = 500)
    private void cancelCode500(CancelInfo cancelInfo) {
        Log.i(TAG, "base: cancel__500");
    }

    @PermissionDenied(requestCode = 500)
    private void denyCode500(DenyInfo denyInfo) {
        Log.i(TAG, "base: deny__500");
    }

}
