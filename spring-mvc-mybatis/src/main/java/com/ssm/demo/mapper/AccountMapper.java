package com.ssm.demo.mapper;

import com.ssm.demo.pojo.Account;

import java.util.List;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
public interface AccountMapper {

    //  定义dao层接口方法--> 查询account表所有数据
    List<Account> queryAccountList() throws Exception;

}
