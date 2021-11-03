layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    //监听表单事件   添加修改共用一个js页面
    form.on("submit(addOrUpdateSaleChance)",function (obj){
        //加载层特效
        var  index = layer.msg("数据正在提交中，请稍后",{icon: 16,time:false,shade:0.8});

        var url = ctx+"/sale_chance/save";
        //提示信息
        var hint = "添加成功";

        //判断是添加还是修改   有id，修改  没有id，添加
        if($("input[name = id]").val()){

            //如果存在id 就是修改  就跳转到controller层的update
            url = ctx+"/sale_chance/update";
            //提示信息
            hint = "修改成功";
        }

        //接收到 add_update.ftl返回的信息
        //发送ajax
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (obj){
                if (obj.code==200){
                    //提示信息
                   // layer.msg("添加成功",{icon:5});
                    layer.msg(hint);
                    //关闭加载层   上面定义的   提交渲染
                    layer.close(index);
                    //关闭iframe
                    layer.closeAll("iframe");
                    //刷新页面 重新渲染表格数据
                    window.parent.location.reload();
                }else {
                    layer.msg(obj.msg,{icon:6});
                }
            }
        });
        //取消跳转
        return false;
    })


            //用上面的就可以
//     /**
//      * 监听submit事件
//      * 实现营销机会的添加与更新
//      */
//     form.on("submit(addOrUpdateSaleChance)", function (data) {
// // 提交数据时的加载层 （https://layer.layui.com/）
//         var index = layer.msg("数据提交中,请稍后...",{
//             icon:16, // 图标
//             time:false, // 不关闭
//             shade:0.8 // 设置遮罩的透明度
//         });
//         // 请求的地址
//         var url = ctx + "/sale_chance/save";
//         // 发送ajax请求
//         $.post(url, data.field, function (result) {
//                 // 操作成功
//             if (result.code == 200) {
//                 // 提示成功
//                 layer.msg("操作成功！");
//                 // 关闭加载层
//                 layer.close(index);
//                 // 关闭弹出层
//                 layer.closeAll("iframe");
//                 // 刷新父页面，重新渲染表格数据
//                 parent.location.reload();
//             } else {
//                 layer.msg(result.msg);
//             }
//         });
//         return false; // 阻止表单提交
//     });

    // 取消功能
    $("#closeBtn").click(function (){
        //获取弹出层的索引值
        var  idx = parent.layer.getFrameIndex(window.name);
        //根据索引关闭
        parent.layer.close(idx);
    })

    // 添加下拉框
    //通过选择器获取隐藏域ID 找到分配人
    var assignMan = $("input[name = man]").val();
    //发送ajax
    $.ajax({
        type: "post",
        url: ctx+"/user/sales",
        dataType: "json",
        success:function (data){
            //遍历  找出当前分配人
            for (var x in data) {
                //判断是否是当前分配人
                if (data[x].id == assignMan){
                    $("#assignMan").append("<option selected value='" + data[x].id + "'>"+data[x].uname + "</option>");
                }else {
                    $("#assignMan").append("<option value='" + data[x].id + "'>"+data[x].uname + "</option>");

                }
            }
            //重新渲染界面
            layui.form.render("select");
        }
    })

});