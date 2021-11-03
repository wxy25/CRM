package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {     //继承BaseQuery  使用里面的分页方法
    //客户名名称
    private String customerName;
    private String createMan;
    private String state;

    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String customerName, String createMan, String state) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
