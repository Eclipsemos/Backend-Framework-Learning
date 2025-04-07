package com.eclipse.myaop.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JoinPoint implements IJoinPoint{

    protected final Method method;
    private Object[] args;

    public JoinPoint(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }


    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}
