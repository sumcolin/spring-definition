package com.springmvc.framework.annotations;

import java.lang.annotation.*;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ISecurity {

    String [] value() default "";
}
