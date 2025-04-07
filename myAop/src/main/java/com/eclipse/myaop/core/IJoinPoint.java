package com.eclipse.myaop.core;

import java.lang.annotation.Annotation;

public interface IJoinPoint {
    Object[] getArgs();
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    default Object invoke() throws Throwable {
        return null;
    }

    default Object invoke(Object[] args) throws Throwable {
        return null;
    }

}
