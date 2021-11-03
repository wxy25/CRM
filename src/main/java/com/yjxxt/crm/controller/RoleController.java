package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "list")
    @ResponseBody
   // @RequiredPermission(code = "60")    后端角色认证
    ////角色的条件查询以及 分页
    public Map<String,Object> list(RoleQuery roleQuery){
        return roleService.findRoleByParam(roleQuery);
    }


    @RequestMapping(value = "findRoles")
    @ResponseBody
    //查询所有角色信息
    public List<Map<String,Object>> sayRoles(Integer userId){
       return roleService.findRoles(userId);
    }

    @RequestMapping(value = "index")
    public String index(){
        return "/role/role";
    }


    //角色信息添加，修改和添加的共用页面
    @RequestMapping(value = "toAddOrUpdate")
    public String addOrUpdate(Model model,Integer roleId){
        //判断是否有id
        if(roleId!=null){
            Role role = roleService.selectByPrimaryKey(roleId);
            //把带有id的数据存储进去
            model.addAttribute("role",role);
        }
        //返回到页面  视图页面
        return "/role/add_update";

    }

    //角色信息添加
    @RequestMapping(value = "save")
    @ResponseBody
    public ResultInfo save(Role role){
        roleService.addRole(role);
        return success("角色添加成功");
    }


    //角色信息修改  编辑
    @RequestMapping(value = "update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.changeRole(role);
        return success("角色修改成功");
    }


    @RequestMapping(value = "delete")
    @ResponseBody
    //删除
    public ResultInfo delete(Role role){
        roleService.removeRoleById(role);
        return success("删除成功!");
    }


    @RequestMapping(value = "toRoleGrantPage")
    //授权页面
    public String toRoleGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    //角色模块的授权
    @RequestMapping(value = "addGrant")
    @ResponseBody
    public ResultInfo grant(Integer roleId,Integer[] mids){
        roleService.addGarant(roleId,mids);
        return success("授权成功了");
    }









}
