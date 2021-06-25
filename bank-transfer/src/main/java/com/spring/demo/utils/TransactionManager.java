package com.spring.demo.utils;

import java.sql.SQLException;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/18
 */
public class TransactionManager {


    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    //    private TransactionManager() {
//
//    }
//
//    private static TransactionManager transactionManager = new TransactionManager();
//
//    public static TransactionManager getInstance() {
//        return transactionManager;
//    }

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
