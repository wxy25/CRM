package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //<!--  根据用户名查询用户信息-->
    User selectUserByName(String userName);

    @MapKey("")   //为解决下面的Map类型报红
    //    查询所有销售人员
    List<Map<String,Object>> selectSales();


}