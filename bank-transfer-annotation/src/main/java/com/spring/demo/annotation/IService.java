package com.spring.demo.annotation;

import java.lang.annotation.*;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/22
 *
 * Service注解标识
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IService {

    String value() default "";
}
