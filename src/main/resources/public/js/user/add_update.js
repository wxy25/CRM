layui.use(['form', 'layer',"formSelects"], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        formSelects= layui.formSelects;

    /**
     * 添加或更新用户
    //  */
    // console.log("你好");
    // form.on("submit(addOrUpdateUser)",function (data){
    //     //判断是添加还是修改   默认是添加
    //     var url = ctx+"/user/save";
    //
    //     if ($("input[name =id]").val()){
    //         url = ctx+"/user/update";
    //     }
    //
    //     //发送ajax请求
    //     $.post(url,data.field,function(result){
    //         if (result.code == 200){
    //             //刷新
    //             parent.location.reload();
    //         }else {
    //             layer.msg(result.msg,{icon : 5});
    //         }
    //
    //     },"json")
    //     //取消默认跳转
    //     return  false;
    //
    // });

    /**
     * 添加或更新用户
     */
    form.on("submit(addOrUpdateUser)", function (data) {
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false,
            shade: 0.8});
        var url = ctx + "/user/save";
        if($("input[name='id']").val()){
            url = ctx + "/user/update";
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有ifream层
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;
    });
    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    /**
     * 加载角色信息
     */
    formSelects.config('selectId', {
        type: 'post',                //请求方式: post, get, put, delete...
        searchUrl: ctx+'/role/findRoles?userId='+$("input[name=id]").val(),              //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
        keyName: 'roleName',            //自定义返回数据中name的key, 默认 name
        keyVal: 'id',            //自定义返回数据中value的key, 默认 value
        //当有搜索内容时, 点击选项是否清空搜索内容, 默认不清空
    }, true);


});
