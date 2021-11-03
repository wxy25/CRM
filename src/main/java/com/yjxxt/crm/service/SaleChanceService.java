package com.yjxxt.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {



    //营销机会查询
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        //实例化对象
        Map<String,Object> map = new HashMap<>();
        //实例化分页单位
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        //开始分页
        PageInfo<SaleChance> plist = new PageInfo<>(selectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        //返回Map
        return map;
    }

    //营销机会添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state   0，1
        //未分配
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }
        //已经分配
        if (StringUtils.isNoneBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //设定默认值 state，devResult(  0--未开发  1--开发中  2--开发成功了   3--开发失败了)
        // createDate(创建时间)  updateDate(修改时间)  分配之间
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);
        //添加是否成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败");
    }

    //营销机会修改
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //1、参数校验
        //通过id查询记录
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        //判断是否为空
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        //校验基础参数
        checkSaleChanceParam(temp.getCustomerName(),temp.getLinkMan(),temp.getLinkPhone());
        //2、设置相关参数
        //原来 未分配       temp.getAssignMan() ：数据库查询到的         saleChance.getAssignMan()  :ajax发送过来的
        if(StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNoneBlank(saleChance.getAssignMan())){
            //分配状态  1：已分配
            saleChance.setState(1);
            //开发结果
            saleChance.setDevResult(1);
            //分配时间  当前时间
            saleChance.setAssignTime(new Date());
        }
        //已分配
        if (StringUtils.isNoneBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            //分配状态  0：未分配
            saleChance.setState(0);
            //开发结果
            saleChance.setDevResult(0);
            //分配人
            saleChance.setAssignMan("");
            //分配时间  当前时间
            saleChance.setAssignTime(null);
        }
        //设定默认值 修改时间
        saleChance.setUpdateDate(new Date());
        //3、执行更新，判断结果
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败");
    }

    //营销机会批量删除
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanceIds(Integer[] ids){
        //判断条件
        AssertUtil.isTrue(ids == null || ids.length == 0,"请选择要删除的数据");
        //执行操作
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"批量删除失败!");
    }


    /**
     * //客户名非空
     *         //联系人非空
     *         //联系电话非空  11位的合法的手机号
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    //校验
    private void checkSaleChanceParam(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名称名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系人电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }




}
