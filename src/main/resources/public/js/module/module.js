layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx+'/module/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose:true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function
                    (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if(d.grade==1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });


    // 头部工具栏的绑定
    table.on('toolbar(munu-table)', function(obj){
        switch(obj.event){
            case "expand":
                treeTable.expandAll('#munu-table');
                break;
            case "fold":
                treeTable.foldAll('#munu-table');
                break;
        };
    });
});