package com.spring.demo.dao;

import com.spring.demo.pojo.Account;


/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
public interface AccountDao {

    public int updateAccountByCardNo(Account account) throws Exception;


    Account queryAccountByCardNo(String cardNo) throws Exception;
}
