package com.springmvc.demo.controller;

import com.springmvc.demo.service.DemoService;
import com.springmvc.framework.annotations.IAutowired;
import com.springmvc.framework.annotations.IController;
import com.springmvc.framework.annotations.IRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/28
 */
@IController
@IRequestMapping("/demo")
public class MyController {

    @IAutowired(value = "demoService")
    private DemoService demoService;

    @IRequestMapping("/query")
    public String query(HttpServletRequest req, HttpServletResponse response,String name){
        return demoService.getName(name);
    }
}
