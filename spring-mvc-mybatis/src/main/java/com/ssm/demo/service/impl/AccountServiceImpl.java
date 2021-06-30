package com.ssm.demo.service.impl;

import com.ssm.demo.mapper.AccountMapper;
import com.ssm.demo.pojo.Account;
import com.ssm.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> queryAccountList() throws Exception {
        return  accountMapper.queryAccountList();
    }
}
