package com.spring.demo.utils;

import com.spring.demo.annotation.IAutowired;
import com.spring.demo.annotation.IComponent;

import java.sql.SQLException;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/18
 */
@IComponent(value = "transactionManager")
public class TransactionManager {

    @IAutowired(value = "connectionUtils")
    private ConnectionUtils connectionUtils;

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }


}
