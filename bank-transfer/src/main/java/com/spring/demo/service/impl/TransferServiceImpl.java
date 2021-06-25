package com.spring.demo.service.impl;

import com.spring.demo.annotation.Transaction;
import com.spring.demo.dao.AccountDao;
import com.spring.demo.factory.BeanFactory;
import com.spring.demo.pojo.Account;
import com.spring.demo.service.TransferService;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
public class TransferServiceImpl implements TransferService {

    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    @Override
    @Transaction
    public void transfer(String fromCardNo, String toCardNo, int transferMoney) throws Exception{

            // 查询用户银行卡余额
            Account fromAccount = accountDao.queryAccountByCardNo(fromCardNo);
            Account toAccount = accountDao.queryAccountByCardNo(toCardNo);

            // 转帐逻辑实现
            fromAccount.setMoney(fromAccount.getMoney() - transferMoney);
            toAccount.setMoney(toAccount.getMoney() + transferMoney);

            accountDao.updateAccountByCardNo(fromAccount);
            int c = 1 / 0;
            accountDao.updateAccountByCardNo(toAccount);
//            TransactionManager.getInstance().commit();
//        } catch (Exception e) {
//            try {
//                TransactionManager.getInstance().rollback();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//            e.printStackTrace();
//        }
    }
}
