package com.eclipse.myaop.core;

import com.eclipse.myaop.annotation.After;
import com.eclipse.myaop.annotation.Around;
import com.eclipse.myaop.annotation.Before;
import com.eclipse.myaop.annotation.Throwing;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

public class AopProxy implements MethodInterceptor {

    Object aopObject;

    //反射
    Method beforeMethod;
    Method afterMethod;
    Method throwingMethod;
    Method aroundMethod;
    Map<Integer, Method> interceptorMethods = new HashMap<>();

    boolean isIneterceptorAll = false;

    public AopProxy() {

    }

    public void setEnhancerMethods(Object aopObject) {
        for (Method method : aopObject.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            }
            else if(method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            }
            else if (method.isAnnotationPresent(Around.class)) {
                aroundMethod = method;
            }
            else if (method.isAnnotationPresent(Throwing.class)) {
                throwingMethod = method;
            }
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        final int hashCode = method.hashCode();

        //before-around-after-throw
        if(isIneterceptorAll || interceptorMethods.containsKey(hashCode)) {
            final JoinPoint joinPoint = new JoinPoint(method, objects);
            Object ret = null;
            try {
                invokeMethod(joinPoint, beforeMethod);
                if (aroundMethod != null) {
                    ret = invokeMethod(new ProceedingJointPoint(o, methodProxy, method, objects), aroundMethod);
                } else {
                    ret = methodProxy.invokeSuper(o, objects);
                }
                return ret;
            } catch (Throwable e) {
                e.printStackTrace();
                invokeMethod(e, throwingMethod);
                return ret;
            } finally {
                invokeMethod(joinPoint, afterMethod);
            }
        }
        return methodProxy.invokeSuper(o, objects);
    }

    private Object invokeMethod(Object joinPoint, Method method) throws Throwable {
        Object ret = null;
        if(method!=null) {
            if(method.getParameterTypes().length>0) {
                if(!method.getParameterTypes()[0].equals(JoinPoint.getClass())) {
                    throw new IllegalArgumentException("The method parameter type is not the same as the method parameter type");
                }
                ret = method.invoke(aopObject, joinPoint);
                return ret;
            }else {
                ret = method.invoke(aopObject);
                return ret;
            }
        }

        return null;
    }


}
