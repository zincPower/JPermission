package com.zinc.jpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.callback.IPermission;
import com.zinc.libpermission.utils.JPermissionUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

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
        findViewById(R.id.btn_req_in_base).setOnClickListener(this);
        findViewById(R.id.btn_req_in_none_context).setOnClickListener(this);
        findViewById(R.id.view_group_req_in_view).setOnClickListener(this);

        // JPermissionUtil.requestAllPermission(this);

        //设置各自品牌的系统权限页
//        JPermissionUtil.setManuFacturer("genymotion", MyTestGenymotionMenu.class);

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
            case R.id.btn_req_in_base:
                requestTheBaseReq();
                break;
            case R.id.btn_req_in_none_context:
                requestInNoneContext();
                break;
            case R.id.view_group_req_in_view:
                requestInView();
                break;
        }
    }

    private void requestInView() {
    }

    private void requestInNoneContext() {
        NoneContext noneContext = new NoneContext();
        noneContext.requestInNormalClass();
    }

    private void requestTheBaseReq() {
        requestInBase();
    }

    private void requestService() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Permission(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 200)
    private void requestRequest200() {
        Log.i(TAG, "请求定位权限成功，200");
    }

    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void requestTwoPermission() {
        Log.i(TAG, "请求两个权限成功（写和相机）");
        Toast.makeText(this, "请求两个权限成功", Toast.LENGTH_LONG).show();
    }

    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void requestOnePermission() {
        Log.i(TAG, "请求一个权限成功（写权限）");
    }

    private void requestAllExclue() {
        List<String> excluePermission = new ArrayList<>();
        excluePermission.add(Manifest.permission.CAMERA);
        JPermissionUtil.requestAllPermission(this, excluePermission, new IPermission() {
            @Override
            public void ganted() {
                Log.i(TAG, "ganted====》申请manifest的全部");
            }

            @Override
            public void denied(int requestCode, List<String> denyList) {
                Log.i(TAG,
                        "denied====》申请manifest的全部{code=" + requestCode + ";denyList=" + denyList +
                                "}");
            }

            @Override
            public void canceled(int requestCode) {
                Log.i(TAG, "canceled===》申请manifest的全部{code= " + requestCode + "}");
            }
        });
    }

    private void requestAll() {
        JPermissionUtil.requestAllPermission(this, new IPermission() {
            @Override
            public void ganted() {
                Log.i(TAG, "ganted====》申请manifest的全部");
            }

            @Override
            public void denied(int requestCode, List<String> denyList) {
                Log.i(
                        TAG,
                        "denied====》申请manifest的全部{\n" +
                                "   code=" + requestCode + ";\n" +
                                "   denyList=" + denyList + "\n" +
                                "}");
            }

            @Override
            public void canceled(int requestCode) {
                Log.i(TAG, "canceled===》申请manifest的全部{code= " + requestCode + "}");
            }
        });
    }

    @PermissionCanceled(requestCode = 200)
    private void cancelCode200(CancelInfo cancelInfo) {
        Log.i(TAG, "cancel__200");
    }

    @PermissionDenied(requestCode = 200)
    private void denyCode200(DenyInfo denyInfo) {
        Log.i(TAG, "deny__200");
    }

    @PermissionCanceled()
    private void cancel(CancelInfo cancelInfo) {
        Log.i(TAG, "cancel:" + cancelInfo.getRequestCode());
        Toast.makeText(this, "权限取消", Toast.LENGTH_LONG).show();
    }

    @PermissionDenied()
    private void deny(DenyInfo denyInfo) {
        Log.i(TAG, "deny [code:" + denyInfo.getRequestInfo() + " ; deny:" +
                denyInfo.getDeniedPermissions() + "]");
        Toast.makeText(this, "权限拒绝", Toast.LENGTH_LONG).show();
        //前往开启权限的界面
        // JPermissionUtil.goToMenu(this);
    }

}
