package com.springmvc.demo.service.impl;

import com.springmvc.demo.service.DemoService;
import com.springmvc.framework.annotations.IService;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/28
 */
@IService(value = "demoService")
public class DemoServiceImpl implements DemoService {

    @Override
    public String getName(String name) {
        return "Hello Spring mvc world! " + name;
    }
}
