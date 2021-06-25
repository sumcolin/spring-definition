package com.spring.demo.annotation;

import java.lang.annotation.*;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/22
 *
 * 自动注入
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IAutowired {

    String value() default "";
}
