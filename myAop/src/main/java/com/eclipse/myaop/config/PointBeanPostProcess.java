package com.eclipse.myaop.config;


import com.eclipse.myaop.annotation.Aop;
import com.eclipse.myaop.core.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PointBeanPostProcess implements BeanPostProcessor {
    Map<String, Object> jointPointPathMap;
    Map<Class, Object> jointPointAnnotationMap;

    public PointBeanPostProcess() {
        jointPointPathMap = new HashMap<>();
        jointPointAnnotationMap = new HashMap<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> beanClass = bean.getClass();
        final Aop aop = beanClass.getAnnotation(Aop.class);
        if(aop!=null) {
            final String jointPath = aop.jointPath();
            //
            if(!jointPath.equals("")) {
                jointPointPathMap.put(jointPath, bean);
            } else {
                jointPointAnnotationMap.put(aop.joinAnnotationClass(), bean);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> beanClass = bean.getClass();
        final String path = beanClass.getName();

        for (String s:jointPointPathMap.keySet()) {
            if (path.startsWith(s)) {
                return ProxyFactory.tryBuild(bean, jointPointPathMap.get(s));
            }
        }
        for (Object aopObejct:jointPointAnnotationMap.values()) {
            return ProxyFactory.tryBuild(bean, aopObejct);
        }
        return bean;
    }
}
