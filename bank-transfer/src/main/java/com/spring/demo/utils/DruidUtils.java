package com.spring.demo.utils;

import com.alibaba.druid.pool.DruidDataSource;


/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
// druid 数据库链接池
public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource = new DruidDataSource();

    static {
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://xxxx/spring");
        druidDataSource.setUsername("xxx");
        druidDataSource.setPassword("xxx");
    }

    public static DruidDataSource getInstance(){
        return druidDataSource;
    }

}
