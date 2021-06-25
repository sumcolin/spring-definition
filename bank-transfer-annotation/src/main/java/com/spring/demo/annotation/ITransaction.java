package com.spring.demo.annotation;

import java.lang.annotation.*;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/19
 *
 * 事物注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ITransaction {

    public String value() default "";
}
