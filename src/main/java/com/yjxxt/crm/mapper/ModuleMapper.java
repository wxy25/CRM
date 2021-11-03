package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的资源模块信息
    public List<TreeDto> selectModuleS();

    //查询所有资源
    List<Module> queryModules();
}