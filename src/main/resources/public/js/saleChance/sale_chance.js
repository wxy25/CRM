layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 营销机会列表展示
     */
    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url: ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth: 95,
        page: true, // 开启分页
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "saleChanceListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'chanceSource', title: '机会来源', align: "center"},
            {field: 'customerName', title: '客户名称', align: 'center'},
            {field: 'cgjl', title: '成功几率', align: 'center'},
            {field: 'overview', title: '概要', align: 'center'},
            {field: 'linkMan', title: '联系人', align: 'center'},
            {field: 'linkPhone', title: '联系电话', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {field: 'createMan', title: '创建人', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'uname', title: '指派人', align: 'center'},
            {field: 'assignTime', title: '分配时间', align: 'center'},
            {
                field: 'state', title: '分配状态', align: 'center', templet: function (d) {
                    return formatterState(d.state);
                }
            },
            {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    return formatterDevResult(d.devResult);
                }
            },
            {title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center", minWidth: 150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state) {
        if (state == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value) {
        if (value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (value == 1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if (value == 2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if (value == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    //实现搜索功能  页面重载
    $(".search_btn").click(function(){
        //搜索         方法一   tableIns 找寻头部工具栏
        tableIns.reload({
            where:{ //设定异步数据接口的额外参数
                //通过name属性获取客户名
                customerName:$("input[name=customerName]").val(),
                //通过name属性获取创建人
                createMan:$("input[name=createMan]").val(),
                state:$("#state").val()
            },
            page:{
                curr: 1   //重新从第一页开始
            }
        })

        // //搜索         方法二   table 找寻行内工具栏
        // table.reload("saleChanceListTable",{
        //     where:{ //设定异步数据接口的额外参数
        //         //通过name属性获取客户名
        //         customerName:$("input[name=customerName]").val(),
        //         //通过name属性获取创建人
        //         createMan:$("input[name=createMan]").val(),
        //         state:$("#state").val()
        //     },
        //     page:{
        //         curr: 1   //重新从第一页开始
        //     }
        // })

    })

    // 营销机会管理添加
    //绑定头部工具栏
    //触发事件
    table.on('toolbar(saleChances)',function (obj){
        var checkStatus = table.checkStatus(obj.config.id);
        console.log(checkStatus);
        console.log(checkStatus.data);

        switch (obj.event){
            case 'add':
                //layer.msg("添加");
                addOrUpdateSaleChanceDialog();
                break;
            case 'del':
               // layer.msg("删除");
                deleteSaleChance(checkStatus.data);
                break;
        };
    });

    function deleteSaleChance(data){
        //判断
        if (data.length == 0){
            layer.msg("请选择要删除的数据");
            return ;
        }

        //控制打印被删除数据的id  测试用的
        //console.log(ids.toString());

        layer.confirm("你确定要删除这些数据吗？",{
            btn:["确定","取消"],
        },function (index){
            //关闭询问框
            layer.close(index);
            //收集数据
            var ids = [];
            for (var x in data){
                ids.push(data[x].id);
            }
            //发送ajax删除  操作
            $.ajax({
                type:"post",
                url: ctx+"/sale_chance/dels/",
                data:{"ids":ids.toString()},
                dataType:"json",
                success:function (result){
                    if (result.code == 200){
                        layer.msg("删除成功",{icon: 5});
                        //重新加载一下数据（刷新页面）
                        tableIns.reload();
                    }else {
                        //失败的提示
                        layer.msg(result.msg());
                    }
                }
            });

        });
    }


    //添加修改函数
    function addOrUpdateSaleChanceDialog(saleChanceId) {
        var title = "<h2>营销机会管理 - 机会添加</h2>";
        var url = ctx + "/sale_chance/addOrUpdateSaleChancePage";

        if (saleChanceId){
            url = url+"?id="+saleChanceId;
        }

        //弹出层
        layui.layer.open({
            title:title,
            type:2, //iframe
            content: url,
            area:["500px","620px"],
            maxmin:true
        });
    }


    //绑定行内工具栏
    //工具条事件
    table.on('tool(saleChances)',function (obj){
        //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var  data = obj.data;  //获取当前行数据
        var layEvent = obj.event;  //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr;        //获取当前行的DOM对象  如果有的话
        if (layEvent == 'del'){
            layer.confirm("你确定要删除这些数据吗？",{
                btn:["确定","取消"],
            },function (index){
                //关闭询问框
                layer.close(index);

                //发送ajax删除  操作
                $.ajax({
                    type:"post",
                    url: ctx+"/sale_chance/dels/",
                    data:{"ids":data.id},
                    dataType:"json",
                    success:function (result){
                        if (result.code == 200){
                            layer.msg("删除成功",{icon: 5});
                            //重新加载一下数据（刷新页面）
                            tableIns.reload();
                        }else {
                            //失败的提示
                            layer.msg(result.msg());
                        }
                    }
                });
            });

                    //没看出来是干嘛的
            // layer.confirm('真的要删除行吗',function (index){
            //     obj.del();      //删除当前行的内容  并更新缓存
            //     layer.close(index);
            //     //向服务器发送删除指令
            //     layer.msg("del",{icon:5});
            // });
        }else if (layEvent == 'edit'){          //编辑
            //调用方法  添加修改  共用一个方法  带id的  就是修改  不带的  就是添加   (选中记录的ID)
            addOrUpdateSaleChanceDialog(data.id);
        }
    });



})