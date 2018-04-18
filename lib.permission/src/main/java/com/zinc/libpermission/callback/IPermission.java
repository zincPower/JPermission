package com.zinc.libpermission.callback;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public interface IPermission {

    //授权
    public void ganted();

    //被拒绝，点击了不再提示
    public void denied(int requestCode, List<String> denyList);

    //取消授权
    public void canceled(int requestCode);

}
