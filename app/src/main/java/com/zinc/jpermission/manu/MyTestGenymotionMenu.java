package com.zinc.jpermission.manu;

import android.content.Context;
import android.content.Intent;

import com.zinc.libpermission.menu.base.IMenu;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public class MyTestGenymotionMenu implements IMenu {

    @Override
    public Intent getMenuIntent(Context context) {
        Intent intent = new Intent(context, MyTestMenu.class);

        return intent;
    }

}
