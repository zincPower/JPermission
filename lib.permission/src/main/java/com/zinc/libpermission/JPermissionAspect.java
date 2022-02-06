package com.zinc.libpermission;

import android.content.Context;
import android.util.Log;

import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.callback.IPermission;
import com.zinc.libpermission.utils.JPermissionHelper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */
@Aspect
public class JPermissionAspect {
    private static final String TAG = JPermissionAspect.class.getSimpleName();

    public static JPermissionAspect aspectOf() {
        return new JPermissionAspect();
    }

    /**
     * 它在选择Jpoint的时候，把 @Permission 使用上了，并且携带有这个注解的API都是目标 JPoint
     * 接着，由于我们希望在函数中获取注解的信息，所有这里的 poincut 函数有一个参数，
     * 参数类型是 Permission，参数名为 permission
     * 这个参数我们需要在后面的 advice 里用上，所以 pointcut 还使用了 @annotation(permission) 这种方法来告诉
     * AspectJ，这个 permission 是一个注解
     */
    @Pointcut(
            "execution(@com.zinc.libpermission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }

    @Around("requestPermission(permission)")
    public void aroundJoinPoint(
            final ProceedingJoinPoint joinPoint,
            Permission permission
    ) throws Throwable {

        Context context = null;

        final Object object = joinPoint.getThis();

        Log.e(
                TAG,
                "aroundJoinPoint: \n" +
                        "kind: " + joinPoint.getKind() + "\n" +
                        "args: " + joinPoint.getArgs() + "\n" +
                        "signature: " + joinPoint.getSignature() + "\n" +
                        "source location: " + joinPoint.getSourceLocation() + "\n" +
                        "static part: " + joinPoint.getStaticPart() + "\n" +
                        "this: " + joinPoint.getThis() + "\n" +
                        "target: " + joinPoint.getTarget() + "\n" +
                        "class: " + joinPoint.getClass() + "\n" +
                        "short string: " + joinPoint.toShortString() + "\n" +
                        "long string: " + joinPoint.toLongString() + "\n"
        );

        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof android.view.View) {
            context = ((android.view.View) object).getContext();
        }

        if (context == null) {
            context = JPermissionHelper.getContext();
        }

        if (context == null || permission == null) {
            return;
        }

        JPermissionActivity.permissionRequest(context,
                permission.value(),
                permission.requestCode(),
                new IPermission() {
                    @Override
                    public void ganted() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void denied(int requestCode, List<String> denyList) {
                        //获取切面上下文的类型
                        Class<?> clz = object.getClass();

                        for (; ; ) {

                            if (clz == null) {
                                break;
                            }

                            String clazzName = clz.getCanonicalName();
                            if (clazzName.startsWith("java.") || clazzName.startsWith("android.")) {
                                break;
                            }

                            //获取类型中的方法
                            Method[] methods = clz.getDeclaredMethods();
                            if (methods == null) {
                                clz = clz.getSuperclass();
                                continue;
                            }

                            for (Method method : methods) {
                                //获取该方法是否有PermissionDenied注解
                                boolean isHasAnnotation =
                                        method.isAnnotationPresent(PermissionDenied.class);
                                if (isHasAnnotation) {
                                    method.setAccessible(true);

                                    //获取方法的参数类型，确定其可以回传
                                    Class<?>[] parameterTypes = method.getParameterTypes();

                                    //需要参数长度为1且类型需要为DenyInfo
                                    if (parameterTypes == null || parameterTypes.length != 1) {
                                        return;
                                    } else if (parameterTypes[0] != DenyInfo.class) {
                                        return;
                                    }

                                    DenyInfo denyInfo = new DenyInfo(requestCode, denyList);

                                    PermissionDenied annotation =
                                            method.getAnnotation(PermissionDenied.class);
                                    int annotationReqCode = annotation.requestCode();

                                    try {
                                        if (annotationReqCode ==
                                                JPermissionHelper.DEFAULT_REQUEST_CODE) {  //为默认值时，调用该注视方法
                                            method.invoke(object, denyInfo);
                                        } else if (annotationReqCode ==
                                                requestCode) {    //当设置的回调值与请求值相同时，调用
                                            method.invoke(object, denyInfo);
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            clz = clz.getSuperclass();
                        }
                    }

                    @Override
                    public void canceled(int requestCode) {

                        //获取切面上下文的类型
                        Class<?> clz = object.getClass();

                        for (; ; ) {

                            if (clz == null) {
                                break;
                            }

                            String clazzName = clz.getCanonicalName();
                            if (clazzName.startsWith("java.") || clazzName.startsWith("android.")) {
                                break;
                            }

                            //获取类型中的方法
                            Method[] methods = clz.getDeclaredMethods();
                            if (methods == null) {
                                clz = clz.getSuperclass();
                                continue;
                            }

                            for (Method method : methods) {
                                //获取该方法是否有PermissionCanceled注解
                                boolean isHasAnnotation =
                                        method.isAnnotationPresent(PermissionCanceled.class);
                                if (isHasAnnotation) {
                                    method.setAccessible(true);

                                    //获取方法的参数类型，确定其可以回传
                                    Class<?>[] parameterTypes = method.getParameterTypes();
                                    //需要参数长度为1且类型需要为CancelInfo
                                    if (parameterTypes == null || parameterTypes.length != 1) {
                                        return;
                                    } else if (parameterTypes[0] != CancelInfo.class) {
                                        return;
                                    }

                                    CancelInfo cancelInfo = new CancelInfo(requestCode);

                                    PermissionCanceled annotation =
                                            method.getAnnotation(PermissionCanceled.class);
                                    int annotationReqCode = annotation.requestCode();

                                    try {
                                        if (annotationReqCode ==
                                                JPermissionHelper.DEFAULT_REQUEST_CODE) {  //为默认值时，调用该注视方法
                                            method.invoke(object, cancelInfo);
                                        } else if (annotationReqCode ==
                                                requestCode) {    //当设置的回调值与请求值相同时，调用
                                            method.invoke(object, cancelInfo);
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            clz = clz.getSuperclass();
                        }
                    }
                });

    }

}
