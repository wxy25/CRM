package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    //营销机会管理
    private SaleChanceService saleChanceService;

    //用户个人信息
    @Autowired
    private UserService userService;

    //登录页面
    @RequestMapping(value = "index")
    public String index(){
        return "saleChance/sale_chance";
    }



    //营销机会查询
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> sayList(SaleChanceQuery saleChanceQuery){
        //调用方法
        Map<String, Object> map = saleChanceService.querySaleChanceByParams(saleChanceQuery);
        //map -- >json
        return map;
    }

    //添加 or 修改   访问的页面,通过判断是否带有id还判断是添加还是修改
    @RequestMapping(value = "addOrUpdateSaleChancePage")
    public String addOrUpdate(Integer id, Model model){

        //判断
        if (id!=null){
            //通过id查询用户信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance",saleChance);
        }
        //带着页面获取的用户操作的数据进行跳转
        return "saleChance/add_update";
    }


    //营销机会添加管理
    @RequestMapping(value = "save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        //获取用户登录的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //创建人
        saleChance.setCreateMan(trueName);
        //添加操作
        saleChanceService.addSaleChance(saleChance);
        //返回目标对象
        return success("营销机会添加成功了");
    }


    //营销机会修改管理
    @RequestMapping(value = "update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        //更新营销机会
        saleChanceService.updateSaleChance(saleChance);
        //返回目标对象
        return success("营销机会修改成功");
    }

    //营销机会管理批量删除
    @RequestMapping(value = "dels")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){
        //执行service层方法
        saleChanceService.removeSaleChanceIds(ids);
        //返回结果
        return success("营销机会批量删除成功!");
    }




}
