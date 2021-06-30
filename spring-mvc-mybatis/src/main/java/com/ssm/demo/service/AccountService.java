package com.ssm.demo.service;

import com.ssm.demo.pojo.Account;

import java.util.List;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
public interface AccountService {

    List<Account> queryAccountList() throws Exception;
}
