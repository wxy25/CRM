package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {
    //用户名
    private String userName;

    //邮箱
    private String email;

    //电话
    private String phone;

    public UserQuery() {
    }

    public UserQuery(String userName, String email, String phone) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
