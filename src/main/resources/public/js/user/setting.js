layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听表单提交
    form.on("submit(saveBtn)",function (data){

        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",           //跳转到controller页面的对应的  @RequestMapring
            data:{
                userName: data.field.userName,
                phone: data.field.phone,
                email: data.field.email,
                trueName: data.field.trueName,
                id: data.field.id
            },
            dataType:"json",
            success:function (msg){
                if (msg.code == 200){
                    layer.msg("保存成功",function (){
                        //清空Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //页面跳转
                        window.parent.location.href = ctx+"/index";
                    })
                }
            }
        })
    })
})