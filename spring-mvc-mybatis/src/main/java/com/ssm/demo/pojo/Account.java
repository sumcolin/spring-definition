package com.ssm.demo.pojo;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
public class Account {

    private String name;

    private int money;

    private String cardNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", cardNo='" + cardNo + '\'' +
                '}';
    }
}
