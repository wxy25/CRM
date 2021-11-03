layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(login)', function(data) {
       var fielData = data.field;

       if (fielData.username == 'undefinded' || fielData.username==''){
           layer.msg("用户名不能为空！");
           return ;
       }

       if(fielData.password == 'undefinded' || fielData.password == ''){
           layer.msg("密码不能为空");
           return
       }

       //ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                "userName":fielData.username,
                "userPwd":fielData.password
            },
            dataType:"json",
            success:function (msg){
                //resultInfo
                if(msg.code==200){
                    //成功了     成功时，页面跳转过快  看不到本条消息  不写也可以
                   // layer.msg("登录成功了!",{icon:5});
                    //跳转
                    //window.location.href=ctx+"/main";
                    layer.msg("登录成功了",function (){
                        //将用户的数据存储到cookie里面
                        $.cookie("userIdStr",msg.result.userIdStr);
                        $.cookie("userName",msg.result.userName);
                        $.cookie("trueName",msg.result.trueName);

                        //添加记住我的判断
                        if ($("input[type='checkbox']").is(":checked")){
                            //设置生命周期
                            $.cookie("userIdStr",msg.result.userIdStr,{expires: 7});
                            $.cookie("userName",msg.result.userName,{expires: 7});
                            $.cookie("trueName",msg.result.trueName,{expires: 7});
                        }

                        //跳转页面
                        window.location.href=ctx+"/main";
                    })
                }else {
                    //登录失败
                    layer.msg(msg.msg);
                }
            }
        })


        //取消默认行为
        return false;

    });

    
});