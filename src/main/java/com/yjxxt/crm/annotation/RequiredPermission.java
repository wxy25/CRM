package com.yjxxt.crm.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//自定义注解
public @interface RequiredPermission {
    String code() default "";
}
