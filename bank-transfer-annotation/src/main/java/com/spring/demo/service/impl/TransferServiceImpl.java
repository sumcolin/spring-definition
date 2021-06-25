package com.spring.demo.service.impl;

import com.spring.demo.annotation.IAutowired;
import com.spring.demo.annotation.IService;
import com.spring.demo.annotation.ITransaction;
import com.spring.demo.dao.AccountDao;
import com.spring.demo.pojo.Account;
import com.spring.demo.service.TransferService;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
@IService(value = "transferService")
public class TransferServiceImpl implements TransferService {

    @IAutowired(value = "accountDao")
    private AccountDao accountDao;

    @Override
    @ITransaction
    public void transfer(String fromCardNo, String toCardNo, int transferMoney) throws Exception{

            // 查询用户银行卡余额
            Account fromAccount = accountDao.queryAccountByCardNo(fromCardNo);
            Account toAccount = accountDao.queryAccountByCardNo(toCardNo);

            // 转帐逻辑实现
            fromAccount.setMoney(fromAccount.getMoney() - transferMoney);
            toAccount.setMoney(toAccount.getMoney() + transferMoney);

            accountDao.updateAccountByCardNo(fromAccount);
            int a = 1/0;
            accountDao.updateAccountByCardNo(toAccount);

    }
}
