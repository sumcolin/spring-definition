package com.spring.demo.dao.impl;

import com.spring.demo.dao.AccountDao;
import com.spring.demo.pojo.Account;
import com.spring.demo.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
public class AccountDaoImpl implements AccountDao {

    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    // 通过更新账户信息
    @Override
    public int updateAccountByCardNo(Account account) throws Exception {

        // 获取数据库链接
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1, account.getMoney());
        preparedStatement.setString(2, account.getCardNo());
        int i = preparedStatement.executeUpdate();

        preparedStatement.close();
//        con.close();
        return i;

    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {

        Connection currentThreadConn = connectionUtils.getCurrentThreadConn();
        String sql = "select * from account where cardNo = ?";
        PreparedStatement preparedStatement = currentThreadConn.prepareStatement(sql);
        preparedStatement.setObject(1, cardNo);

        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        while (resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setMoney(resultSet.getInt("money"));
        }
        resultSet.close();
        preparedStatement.close();
        return account;
    }
}
