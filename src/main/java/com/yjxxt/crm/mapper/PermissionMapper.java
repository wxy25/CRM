package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;

import java.util.List;


public interface  PermissionMapper extends BaseMapper<Permission,Integer> {

    //统计当前角色的资源数量
    int countRoleModulesByRoleId(Integer roleId);

    //删除当前角色的资源信息
    int deleteRoleModuleByRoleId(Integer roleId);

    //获取当前角色所拥有的资源信息  根据Id
    List<Integer> selectModelByRoleId(Integer roleId);

    //查询用户所拥有的资源权限码
    List<String> queryUserHasRolesHasPermissions(Integer userId);


 
}