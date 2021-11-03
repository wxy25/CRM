package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    //引用dao层  mapper
    @Resource
    private UserMapper userMapper;

    //引用dao层  UserRoleMapper
    @Resource
    private UserRoleMapper userRoleMapper;



    //对登录进行处理
    public UserModel userLogin(String userName,String userPwd){
        //对输入的账号密码进行判断  是否符合格式
        checkUserLoginParam(userName,userPwd);
        //用户是否存在，通过查询数据库
        User temp =  userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp == null,"用户不存在");
        //用户的密码是否正确，拿用户传进来的密码和上面从数据库查到的用户的密码经行equest比较
        checkUserPwd(userPwd,temp.getUserPwd());
        //返回目标对象
        return builderUserInfo(temp);
    }

    /**
     *
     * @param temp 当前登录对象
     * @return
     */
    //返回目标对象
    private UserModel builderUserInfo(User temp) {
        UserModel userModel = new UserModel();
        //为用户ID进行加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(temp.getId()));
        userModel.setUserName(temp.getUserName());
        userModel.setTrueName(temp.getTrueName());
        return userModel;
    }


    /**
     * //对输入的账号密码进行判断  是否符合格式
     * @param userName   账号
     * @param userPwd    密码
     */
    //对输入的账号密码进行非空校验  经行参数校验
    private void checkUserLoginParam(String userName, String userPwd) {
        //用户非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }


    /**
     * //判断密码是否正确
     * @param userPwd   用户输入的密码
     * @param userPwd1  数据库查出来的密码
     */
    //判断密码是否正确
    private void checkUserPwd(String userPwd, String userPwd1) {
        //对用户输入的密码进行加密
        userPwd = Md5Util.encode(userPwd);
        //与数据库里查询到的密码进行比对

        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");

    }

    /**
     *
     * @param userId            用户ID
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     * @param confirmPassword   确认密码
     */
    //修改密码
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        //通过userId获取user对象
        User user = userMapper.selectByPrimaryKey(userId);
        //参数校验
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);
        //设置新密码（用户输入新密码）  加密后添加进去
        user.setUserPwd(Md5Util.encode(newPassword));
        //执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     *
     * @param user             当前用户对象
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     * @param confirmPwd   确认密码
     */
    //修改密码参数校验
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPwd) {
        //user对象，非空校验
        AssertUtil.isTrue(user==null,"用户未登录或不存在");
        //原始密码  非空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //判断原始密码与数据库密码是否一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        //新密码非空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        //新密码与原始密码的校验（不能一致）
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能与原始密码相同");
        //确认密码非空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //确认密码是否与新密码相同
        AssertUtil.isTrue((!newPassword.equals(confirmPwd)),"新密码与确认密码不一致");

    }

//    查询所有销售人员
    public List<Map<String,Object>>  querySales(){
        return userMapper.selectSales();
    }

    /**
     *
     * @param userQuery
     * @return
     */
    //用户模块的列表查询
    public Map<String,Object> findUserByParams(UserQuery userQuery){
        //实例化对象
        Map<String,Object> map = new HashMap<>();
        //初始化分页单位
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //开始分页
        PageInfo<User> plist = new PageInfo<>(selectByParams(userQuery));

        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());

        //返回结果
        return  map;
    }


    /**
     * 一、参数校验
     *      1、用户名不能为空  且不能重复
     *      2、密码不能为空
     *      3、邮箱不能为空
     *      4、电话不能为空   且符合格式要求
     * 二、设定默认值
     *      1、is_valid
     *      2、createDate 创建日期
     *      3、updateDate 修改时间
     *      4、密码加密
     *
     * 三、添加是否成功
     *
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)           //涉及到事务   就需要此注解
    //用户模块的添加
    public void addUser(User user){
        //1、参数校验
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());

        //用户名唯一
        User temp = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(temp != null,"用户名已存在");

        //2、默认值设置
        //分配人状态
        user.setIsValid(1);
        //创建日期
        user.setCreateDate(new Date());
        //修改日期
        user.setUpdateDate(new Date());
        //密码加密之后设置进去
        user.setUserPwd(Md5Util.encode("123456"));

        //3、添加是否成功
        //AssertUtil.isTrue(insertSelective(user)<1,"添加失败");     下面的insertHasKey方法   可以获取自动生长列的配置 所以用下面的
        //AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");   和上面的一样 因为 继承了

        AssertUtil.isTrue(insertHasKey(user)<1,"添加失败");

        //打印角色信息的id
        System.out.println(user.getId()+ "<<<" + user.getRoleIds());
        relaionUserRole(user.getId(),user.getRoleIds());

    }


    /**
     *
     * @param userId     用户Id
     * @param roleIds       角色Id
     */
    //操作中间表
    private void relaionUserRole(Integer userId, String roleIds) {
        //准备集合  存储对象
        List<UserRole> urlist = new ArrayList<>();

        //userId,roleId
        //判断是否选择了角色信息
        AssertUtil.isTrue(StringUtils.isBlank(roleIds),"请选择角色信息");

        //统计当前用户有多少个角色
        int count = userRoleMapper.countUserRoleNum(userId);
        //删除当前用户的角色
        if (count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) !=count,"用户角色删除失败");
        }

        String[] RoleStrId = roleIds.split(",");

        //遍历
        for(String rid : RoleStrId){
            //准备对象
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(rid));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            //存放到集合里去
            urlist.add(userRole);
        }
        //批量添加
        AssertUtil.isTrue(userRoleMapper.insertBatch(urlist) !=urlist.size(),"用户角色分配失败");

    }

    // 基本信息 参数校验
    private void checkUser(String userName, String email, String phone) {
        //1、用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");

        //2、邮箱不能为空
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");

        //3、手机号不能为空 且格式正确
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }


    /**
     * 一、验证
     *      1、当前用户的ID  已存在才可以修改
     *      2、邮箱不能为空
     *      3、电话不能为空   且符合格式要求
     * 二、设定默认值
     *      1、is_valid
     *      2、updateDate 修改时间
     * 三、修改是否成功
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)           //涉及到事务   就需要此注解
    //用户模块的修改
    public void changeUser(User user){
        //通过id获取用户信息
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //判断用户信息是否存在
        AssertUtil.isTrue(temp == null,"当前用户不存在");
        //校验参数
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        //修改用户名已经存在问题
        User temp2 = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(temp2 !=null && !(temp2.getId().equals(user.getId())),"用户名称已经存在");
        //设定默认值   修改时间
        user.setUpdateDate(new Date());

        //修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"修改失败了");
       // AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");  和上面的一样 因为 继承了

        relaionUserRole(user.getId(),user.getRoleIds());

    }


    @Transactional(propagation = Propagation.REQUIRED)           //涉及到事务   就需要此注解
    //用户模块批量删除
    public void deleteUserByIds(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length==0,"请选择要删除的记录");
        for (Integer userId : ids) {
            //统计当前用户有多少个角色
            int count = userRoleMapper.countUserRoleNum(userId);
            //删除当前用户的角色
            if (count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) !=count,"用户角色删除失败");
            }
        }
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"删除失败");
    }




}
