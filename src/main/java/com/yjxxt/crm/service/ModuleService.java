package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {


    @Resource
    //资源
    public ModuleMapper moduleMapper;

    //引入角色
    @Resource
    public PermissionMapper permissionMapper;


//    查询所有的资源信息
    public List<TreeDto> findModules(){
        return moduleMapper.selectModuleS();
    }


    public List<TreeDto> findModulesByRoleId(Integer roleId){

        //获取所有资源信息
        List<TreeDto> tlist = moduleMapper.selectModuleS();

        //获取当前角色所拥有的资源信息
        List<Integer> roleHasModuls =  permissionMapper.selectModelByRoleId(roleId);

        //遍历
        for(TreeDto td : tlist){
            //判断对比  checked = true;
            if (roleHasModuls.contains(td.getId())){
                //  能进来  说明当前角色 分配了该资源
                td.setChecked(true);
            }
        }
        return tlist;
    }

    public  Map<String, Object> queryModules(){
        //准备数据
        Map<String,Object> map = new HashMap<>();

        //查询所有资源
        List<Module> mlist =  moduleMapper.queryModules();

        map.put("code",0);
        map.put("msg","success");
        map.put("count",mlist.size());
        map.put("data",mlist);

        return map;
    }




}
