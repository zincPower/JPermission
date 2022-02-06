package com.zinc.jpermission;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionHelper;
import com.zinc.libpermission.utils.JPermissionUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */
public class MyFragment extends Fragment {
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        button = view.findViewById(R.id.btn_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(v -> requestLocation());
    }

    @Permission(Manifest.permission.CAMERA)
    private void requestLocation() {
        Toast.makeText(getContext(), "Fragment中请求权限——通过", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny(DenyInfo denyInfo) {
        Log.i(JPermissionHelper.TAG,
                "Fragment中请求权限_writeDeny: code:" + denyInfo.getRequestInfo() + " ; deny:" +
                        denyInfo.getDeniedPermissions());
        Toast.makeText(getContext(), "Fragment中请求权限_deny", Toast.LENGTH_SHORT).show();

        // 前往开启权限的界面
        JPermissionUtil.goToMenu(getContext());
    }

    @PermissionCanceled()
    private void cancel(CancelInfo cancelInfo) {
        Log.i(JPermissionHelper.TAG, "Fragment中请求权限_writeCancel: " + cancelInfo.getRequestCode());
        Toast.makeText(getContext(), "Fragment中请求权限_cancel", Toast.LENGTH_SHORT).show();
    }
}
