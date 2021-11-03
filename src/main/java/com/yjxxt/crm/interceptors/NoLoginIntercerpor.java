package com.yjxxt.crm.interceptors;

import com.github.pagehelper.util.StringUtil;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//非法访问拦截   禁止跳转非权限页面
public class NoLoginIntercerpor extends HandlerInterceptorAdapter {

    @Autowired
    //引入suerService
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

        //判断当前用户是否登录    用户Id不能为空，且数据库有此条数据
        if (userId ==null || null == userService.selectByPrimaryKey(userId)){
            throw new NoLoginException("用户未登录");
        }
        return true;
    }
}
