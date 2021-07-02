package com.springmvc.demo.controller;

import com.springmvc.demo.service.DemoService;
import com.springmvc.framework.annotations.IAutowired;
import com.springmvc.framework.annotations.IController;
import com.springmvc.framework.annotations.IRequestMapping;
import com.springmvc.framework.annotations.ISecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/28
 */
@IController
@IRequestMapping("/demo")
@ISecurity({"kangkang"})
public class MyController {

    @IAutowired(value = "demoService")
    private DemoService demoService;

    @IRequestMapping("/query")
    @ISecurity("michal")
    public String query(HttpServletRequest req, HttpServletResponse response,String username){
        return demoService.getName(username);
    }

    @IRequestMapping("/query02")
    public String query02(HttpServletRequest req, HttpServletResponse response,String username){
        return demoService.getName(username);
    }
}
