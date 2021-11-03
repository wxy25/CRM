package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {

    //引入service层的引用
    @Autowired
    private UserService userService;

    //用户登录
    @RequestMapping(value = "login")
    @ResponseBody     //json格式输出
    public ResultInfo say(User user){
        //结果信息
        ResultInfo resultInfo = new ResultInfo();
            UserModel userModel = userService.userLogin(user.getUserName(), user.getUserPwd());
            resultInfo.setResult(userModel);
        return resultInfo;
    }

    //修改密码
    @PostMapping(value = "updatePwd")
    @ResponseBody        //json格式输出
    public ResultInfo updatePwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPwd){
        //实例化ResultInfo
        ResultInfo resultInfo = new ResultInfo();
        //获取Cookie中的userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码操作
      //  try {
            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPwd);
//        }catch (ParamsException pe){
//            pe.printStackTrace();
//            resultInfo.setCode(pe.getCode());
//            resultInfo.setMsg(pe.getMsg());
//        }catch (Exception ex){
//            ex.printStackTrace();
//            resultInfo.setCode(500);
//            resultInfo.setMsg("操作失败");
//        }
        //返回
        return resultInfo;
    }


    //前端页面修改密码   其实还是调用上面修改密码的方法去校验
    @RequestMapping(value = "toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }

    //修改个人信息  前端页面
    @RequestMapping(value = "toSettingPage")
    public String setting(HttpServletRequest request){
        //获取用户的Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用方法   通过id获取当前对象
        User user = userService.selectByPrimaryKey(userId);
        //存储
        request.setAttribute("user",user);
        return "user/setting";
    }

    //修改个人信息，后端实现
    @PostMapping(value = "setting")
    @ResponseBody        //json格式输出
    public ResultInfo sayUpdate(User user){
        //实例化ResultInfo
        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据对象
        return resultInfo;
    }

    //    查询所有销售人员
    @RequestMapping(value = "sales")
    @ResponseBody
    public List<Map<String,Object>> findSales(){
        List<Map<String,Object>> list = userService.querySales();
        return list;
    }

    //分页查询  页面显示
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.findUserByParams(userQuery);
    }


    //用户管理  用户模块分页显示
    @RequestMapping(value = "index")
    public String index(){
        return "user/user";
    }

    //用户的添加
    @RequestMapping(value = "save")
    @ResponseBody        //json格式输出
    public ResultInfo save(User user){
        // ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.addUser(user);
        //返回目标数据对象
        return success("添加成功");
    }

    //用户的修改
    @RequestMapping(value = "update")
    @ResponseBody        //json格式输出
    public ResultInfo update(User user){
        //实例化ResultInfo
       // ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.changeUser(user);
        //返回目标数据对象
        return success("修改成功");
    }

    //  添加或者修改  共用一个页面
    @RequestMapping(value = "addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model){
        if (id !=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    //用户的批量删除
    @RequestMapping(value = "dels")
    @ResponseBody        //json格式输出
    public ResultInfo dels(Integer[] ids){
       //调用方法
        userService.deleteUserByIds(ids);
        //返回目标数据对象
        return success("删除成功");
    }




}
