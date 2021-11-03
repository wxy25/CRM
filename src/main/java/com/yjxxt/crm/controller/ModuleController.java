package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;

import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    //只能进行授权的方法
//    @RequestMapping(value = "findModules")
//    @ResponseBody
//    public List<TreeDto> findModules(){
//        return moduleService.findModules();
//
//    }

    //可以授权，可以会看权限
    @RequestMapping(value = "findModules")
    @ResponseBody
    public List<TreeDto> findModules(Integer roleId){
        return moduleService.findModulesByRoleId(roleId);
    }


    @RequestMapping(value = "index")
    public String index(){
        return "/module/module";

    }

    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> list(){
        return moduleService.queryModules();
    }





}
