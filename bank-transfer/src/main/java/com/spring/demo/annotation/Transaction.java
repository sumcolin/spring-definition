package com.spring.demo.annotation;

import java.lang.annotation.*;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Transaction {

    public String value() default "";
}
