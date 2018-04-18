package com.zinc.libpermission.bean;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description 被取消授权的信息
 */

public class CancelInfo {

    private int requestCode;

    public CancelInfo(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
