package com.zinc.libpermission.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.zinc.libpermission.JPermissionActivity;
import com.zinc.libpermission.callback.IPermission;
import com.zinc.libpermission.menu.Default;
import com.zinc.libpermission.menu.OPPO;
import com.zinc.libpermission.menu.VIVO;
import com.zinc.libpermission.menu.base.IMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */
public class JPermissionUtil {

    private static HashMap<String, Class<? extends IMenu>> permissionMenu = new HashMap<>();

    private static final String MANUFACTURER_DEFAULT = "Default";//默认

    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "letv";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, Default.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPO.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVO.class);
    }

    /**
     * 用于扩展设置支持品牌的类，brand是品牌，clazz是实现类，必须继承{@link IMenu}
     *
     * @param brand
     * @param clazz
     */
    public static void setManuFacturer(String brand, Class<? extends IMenu> clazz) {
        permissionMenu.put(brand.toLowerCase(), clazz);
    }

    /**
     * 前往权限设置菜单的工具类
     *
     * @param context
     */
    public static void goToMenu(Context context) {
        Class<? extends IMenu> clazz = permissionMenu.get(Build.MANUFACTURER.toLowerCase());
        if (clazz == null) {
            clazz = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            IMenu iMenu = clazz.newInstance();

            Intent menuIntent = iMenu.getMenuIntent(context);
            if (menuIntent == null) return;
            context.startActivity(menuIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求 manifest 中的权限，可以设置回调
     *
     * @param context
     * @param iPermission
     */
    public static void requestAllPermission(Context context, IPermission iPermission) {
        //如果小于 6.0 不进行逻辑处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] manifestPermission = JPermissionHelper.getManifestPermission(context);
        JPermissionActivity
            .permissionRequest(context,
                manifestPermission,
                JPermissionHelper.DEFAULT_REQUEST_CODE,
                iPermission);
    }

    /**
     * 请求manifest中的权限。如果需要回调，可以使用{@link JPermissionUtil#requestAllPermission(Context, IPermission)}填写第二个参数。
     *
     * @param context
     */
    public static void requestAllPermission(Context context) {
        requestAllPermission(context, new IPermission() {
            @Override
            public void ganted() {
                Log.i(JPermissionHelper.TAG, "ganted() Empty achieve！");
            }

            @Override
            public void denied(int requestCode, List<String> denyList) {
                Log.i(JPermissionHelper.TAG, "denied() Empty achieve！");
            }

            @Override
            public void canceled(int requestCode) {
                Log.i(JPermissionHelper.TAG, "canceled() Empty achieve！");
            }
        });
    }

    /**
     * 请求manifest中的权限，可以剔除一些权限。如果需要回调，可以使用{@link JPermissionUtil#requestAllPermission(Context, List, IPermission)}填写第三个参数。
     *
     * @param context
     * @param excluedPermission
     */
    public static void requestAllPermission(Context context, List<String> excluedPermission) {
        requestAllPermission(context, excluedPermission, new IPermission() {
            @Override
            public void ganted() {
                Log.i(JPermissionHelper.TAG, "ganted() Empty achieve！");
            }

            @Override
            public void denied(int requestCode, List<String> denyList) {
                Log.i(JPermissionHelper.TAG, "denied() Empty achieve！");
            }

            @Override
            public void canceled(int requestCode) {
                Log.i(JPermissionHelper.TAG, "canceled() Empty achieve！");
            }
        });
    }

    /**
     * 请求manifest中的权限，可以剔除一些权限和设置回调
     *
     * @param context
     * @param excludePermission
     * @param iPermission
     */
    public static void requestAllPermission(Context context, List<String> excludePermission, IPermission iPermission) {
        // 如果小于 6.0 不进行逻辑处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] manifestPermission = JPermissionHelper.getManifestPermission(context);

        if (manifestPermission == null) {
            return;
        }

        List<String> tempPer = new ArrayList<>();
        for (String s : manifestPermission) {
            if (!excludePermission.contains(s)) {
                tempPer.add(s);
            }
        }

        if (tempPer.size() <= 0) {
            return;
        }

        JPermissionActivity.permissionRequest(
            context,
            tempPer.toArray(new String[tempPer.size()]),
            JPermissionHelper.DEFAULT_REQUEST_CODE,
            iPermission
        );
    }

}
