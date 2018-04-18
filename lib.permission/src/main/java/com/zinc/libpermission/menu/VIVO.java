package com.zinc.libpermission.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.zinc.libpermission.menu.base.IMenu;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public class VIVO implements IMenu {

    @Override
    public Intent getMenuIntent(Context context) {
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null && Build.VERSION.SDK_INT < 23) {
            context.startActivity(appIntent);
            return null;
        }
        Intent vIntent = new Intent();
        vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        vIntent.setAction(Settings.ACTION_SETTINGS);
        return vIntent;
    }

}
