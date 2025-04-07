package com.eclipse.myaop.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import()
public @interface EnableAop {
}
