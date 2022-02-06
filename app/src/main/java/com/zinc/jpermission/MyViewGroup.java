package com.zinc.jpermission;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;

import androidx.annotation.Nullable;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/4
 * @description
 */
public class MyViewGroup extends LinearLayout {

    private final String TAG = MyViewGroup.class.getSimpleName();

    public MyViewGroup(Context context) {
        this(context, null, 0);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Button button = new Button(context);
        button.setText("ViewGroup 中的 view");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestInNormalClass();
            }
        });
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(params);
        addView(button);
    }

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
        Log.i(TAG, "deny [code:" + denyInfo.getRequestInfo() + " ; deny:" +
                denyInfo.getDeniedPermissions() + "]");
    }
}