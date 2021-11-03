package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    //引入mapper
    @Autowired(required = false)
    private RoleMapper roleMapper;

    //引入permissionMapper
    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    //引入moduleMapper
    @Autowired(required = false)
    private ModuleMapper moduleMapper;



    //查询所有角色信息
    public List<Map<String,Object>> findRoles(Integer userId){
        return roleMapper.selectRoles(userId);
    }

    //角色的条件查询以及 分页
    public Map<String,Object> findRoleByParam(RoleQuery roleQuery){
        //实例化对象
        Map<String,Object> map = new HashMap<>();
        //实例化分页单位
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        //开始分页
        PageInfo<Role> rlist = new PageInfo<>(selectByParams(roleQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",rlist.getTotal());
        map.put("data",rlist.getList());
        //返回Map
        return map;
    }


    /**
     * 一、验证
     *      1、角色名不能为空
     *      2、角色名唯一
     * 二、设置默认值
     *      1、is_vaild
     *      2、createDate
     *      3、updateDate
     * 三、添加是否成功
     *
     * @param role
     */
    //角色的添加
    public void addRole(Role role){
        //角色名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        //角色名唯一
        Role temp =  roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp !=null,"该用户名以及存在");
        //默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        //添加是否成功
        AssertUtil.isTrue(insertHasKey(role)<1,"添加失败了");
    }


    /**
     * 一、验证
     *      1、ID验证 验证当前对象是否存在
     *      2、角色名不能为空
     *      3、角色名唯一
     * 二、设置默认值     *
     *      1、updateDate
     * 三、添加是否成功
     *
     * @param role
     */
    //角色的修改
    public void changeRole(Role role){
        //ID验证   验证当前对象是否存在
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null,"当前角色不存在");

        //角色名唯一
        Role temp2 = roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp2!=null && !(role.getId().equals(role.getId())),"角色以及存在");

        //默认值
        role.setUpdateDate(new Date());

        //添加是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"修改失败了");


    }


    /**
     * 一、验证
     * 二、默认值
     * 三、是否删除成功
     * @param role
     */
    //角色的删除
    public void removeRoleById(Role role) {
        //
//        Id是否存在
        AssertUtil.isTrue(role.getId() == null || selectByPrimaryKey(role.getId()) == null,"请选择需要删除的数据");

        //默认值
        role.setIsValid(0);
        role.setUpdateDate(new Date());

        //是否删除成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败");

    }


    /**
     * 授权  统计当前有多少资源  先删除 在添加
     * @param roleId
     * @param mids
     *  需要提交  要添加事务
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //角色的授权
    public void addGarant(Integer roleId,Integer[]  mids){
        AssertUtil.isTrue(roleId == null || roleMapper.selectByPrimaryKey(roleId) == null,"请选择角色");
        AssertUtil.isTrue(mids == null || mids.length == 0,"请至少选择一个资源");

        //统计当前角色的资源数量
        int count = permissionMapper.countRoleModulesByRoleId(roleId);
        //删除当前角色的资源信息
        if (count>0) AssertUtil.isTrue(permissionMapper.deleteRoleModuleByRoleId(roleId) != count, "角色资源分配失败");

        //t_permission roleId mid
        List<Permission> plist = new ArrayList<>();

        if (mids!=null && mids.length>0){
            //遍历mids
            for (Integer mid : mids) {
                //实例化对象
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                //权限码
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                plist.add(permission);
            }
        }
        //是否授权成功
        AssertUtil.isTrue(permissionMapper.insertBatch(plist)!=plist.size(),"授权失败");
    }







}
