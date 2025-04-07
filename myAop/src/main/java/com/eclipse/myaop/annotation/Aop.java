package com.eclipse.myaop.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Aop {
    String jointPath() default "";

    Class<? extends Annotation> joinAnnotationClass() default Void.class;
}
