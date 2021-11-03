layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(saveBtn)', function(data) {
        //layer.msg(JSON.stringify(data.field));
        var fielData = data.field;

        //ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                "oldPassword":fielData.old_password,
                "newPassword":fielData.new_password,
                "confirmPwd":fielData.again_password
            },
            dataType:"json",
            success:function (data){
                if(data.code==200){

                    layer.msg("修改密码成功了，系统三秒钟后自动退出",function (){
                        //清空Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"})
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"})
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"})

                        //跳转回登录页面
                        window.parent.location.href=ctx+"/index";
                    })
                }else {
                    //登录失败
                    layer.msg(data.msg);
                }
            }
        })


        //取消默认行为
        return false;

    });


});