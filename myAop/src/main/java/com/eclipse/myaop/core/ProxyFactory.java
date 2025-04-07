package com.eclipse.myaop.core;

import com.eclipse.myaop.annotation.Aop;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ProxyFactory {
    public static <T> T get(Object target, AopProxy aop) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(aop);
        return (T) enhancer.create();
    }

    public static Object tryBuild(Object targetObject, Object aopObject) {
        boolean isRetAopProxy = false;
        final AopProxy aopProxy = new AopProxy();
        final Aop aop = aopObject.getClass().getAnnotation(Aop.class);
        aopProxy.aopObject = aopObject;

        aopProxy.setEnhancerMethods(aopObject);
        if(!Strings.isBlank(aop.jointPath())) {
            aopProxy.isIneterceptorAll = true;
        }
        else {
            final Class<? extends Annotation> aopAnnotation = aop.joinAnnotationClass();
            final Class<?> targetObjectClass = targetObject.getClass();

            if(targetObjectClass.isAnnotationPresent(aopAnnotation)) {
                aopProxy.isIneterceptorAll = true;
                isRetAopProxy = true;
            } else {
                for(Method method : targetObjectClass.getMethods()) {
                    if(method.isAnnotationPresent(aopAnnotation)) {
                        isRetAopProxy = true;
                        aopProxy.interceptorMethods.put(method.hashCode(), method);
                    }
                }
            }
        }
        if(isRetAopProxy) {
            return get(targetObject, aopProxy);
        } else {
            return targetObject;
        }
    }
}
