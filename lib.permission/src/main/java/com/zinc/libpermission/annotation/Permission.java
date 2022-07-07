package com.zinc.libpermission.annotation;

import com.zinc.libpermission.utils.JPermissionHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        // ElementType.ANNOTATION_TYPE,
        // ElementType.CONSTRUCTOR,
        // ElementType.FIELD,
        // ElementType.LOCAL_VARIABLE,
        // ElementType.PACKAGE,
        // ElementType.PARAMETER,
        // ElementType.TYPE,
        // ElementType.TYPE_PARAMETER,
        // ElementType.TYPE_USE
})
public @interface Permission {

    String[] value();

    int requestCode() default JPermissionHelper.DEFAULT_REQUEST_CODE;

}
