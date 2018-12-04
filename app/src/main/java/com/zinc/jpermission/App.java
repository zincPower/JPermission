package com.zinc.jpermission;

import android.app.Application;

import com.zinc.libpermission.utils.JPermissionHelper;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/4
 * @description
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JPermissionHelper.injectContext(this);

    }
}
