package com.spring.demo.service;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
public interface TransferService {


   public void transfer(String fromCardNo, String toCardNo, int transferMoney) throws Exception;
}
