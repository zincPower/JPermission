package com.zinc.jpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.callback.IPermission;
import com.zinc.libpermission.utils.JPermissionHelper;
import com.zinc.libpermission.utils.JPermissionUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_all).setOnClickListener(this);
        findViewById(R.id.btn_all_exclude).setOnClickListener(this);
        findViewById(R.id.btn_one_permission).setOnClickListener(this);
        findViewById(R.id.btn_two_permission).setOnClickListener(this);
        findViewById(R.id.btn_request_200).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, new MyFragment());
        transaction.commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                requestAll();
                break;
            case R.id.btn_all_exclude:
                requestAllExclue();
                break;
            case R.id.btn_one_permission:
                requestOnePermission();
                break;
            case R.id.btn_two_permission:
                requestTwoPermission();
                break;
            case R.id.btn_request_200:
                requestRequest200();
                break;
            case R.id.btn_service:
                requestService();
                break;
        }
    }

    private void requestService() {
        Intent intent =new Intent(this,MyService.class);
        startService(intent);
    }

    @Permission(value = {Manifest.permission.ACCESS_FINE_LOCATION},requestCode = 200)
    private void requestRequest200() {
        Toast.makeText(this, "请求定位权限成功，200", Toast.LENGTH_SHORT).show();
    }

    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    private void requestTwoPermission() {
        Toast.makeText(this, "请求两个权限成功（写和相机）", Toast.LENGTH_SHORT).show();
    }

    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void requestOnePermission() {
        Toast.makeText(this, "请求一个权限成功（写权限）", Toast.LENGTH_SHORT).show();
    }

    private void requestAllExclue() {
        List<String> excluePermission = new ArrayList<>();
        excluePermission.add(Manifest.permission.CAMERA);
        excluePermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        JPermissionUtil.requestAllPermission(this, excluePermission);
    }

    private void requestAll() {
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
    }

    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    private void requestWrite() {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled(requestCode = 200)
    private void cancelCode200(CancelInfo cancelInfo){
        Toast.makeText(this, "cancel__200", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled()
    private void cancel(CancelInfo cancelInfo) {
        Log.i(JPermissionHelper.TAG, "writeCancel: " + cancelInfo.getRequestCode());
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny(DenyInfo denyInfo) {
        Log.i(JPermissionHelper.TAG, "writeDeny: code:" + denyInfo.getRequestInfo() + " ; deny:" + denyInfo.getDeniedPermissions());
        Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();

        //前往开启权限的界面
        JPermissionUtil.goToMenu(this);
    }

}
