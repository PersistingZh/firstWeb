var gSysDict = null;
var SysDict = function() {};


SysDict.prototype = {
    pid:null,
    // 加载字典树结构
    fnGetSysDicTree : function(id,type,callback) {
        var condition = {}, orderMap = {};
        if(id){
            condition["sdPid"] = "eq@"+id;
        }else{
            condition["sdPid"] = "eq@0";
        }
        if(type){
            condition["type"] = "eq@"+type;
        }
        common.fnShowProgress();
        var requestObj = JSON.stringify(condition);
        $.post({
            type: "post",
            contentType: "application/json; charset=utf-8",
            data: requestObj,
            url: "sysDictWeb/getSysDictListByCondition.action",
            dataType: "json",
            success: function (objList) {
                //ztree 设置
                var settingX = {
                    view: {
                        dblClickExpand: false,
                        showIcon: true //是否展示按钮
                    },
                    data: {
                        simpleData: {
                            enable: true
                        }
                    },
                    /*回调函数*/
                    callback: {
                        onClick: onClick,
                        onExpand: onExpand
                    }
                };
                var qybzNodes = [];//ztree 树数据
                var tempqybzMap = {};//ztree 内部树数据
                var tempStrPath = [];//ztree 临时id变量
                /*遍历循环组装ztree树数据*/
                for(var i=0;i<objList.length;i++){
                    tempqybzMap["id"] = objList[i].id;
                    tempqybzMap["name"] = objList[i].sdName;
                    tempqybzMap["leve"] = objList[i].sdLevel;
                    tempqybzMap["pathName"] = objList[i].pathName.replace(/#/g,"/");
                    /*如果是一级父节点*/
                    tempqybzMap["pId"] = objList[i].sdPid;
                    if(objList[i].isEnd == 0){
                        tempqybzMap["isParent"] = true;
                    }else{
                        tempqybzMap["isParent"] = false;
                    }
                    qybzNodes.push(tempqybzMap);
                    tempqybzMap = {};
                }
                function onExpand(event, treeId, treeNode) {
                    if(treeNode.open){
                        qybzNodes = [];
                        var condition = {};
                        condition["sdPid"] = "eq@"+treeNode.id;
                        var requestObj = JSON.stringify(condition);
                        $.post({
                            type: "post",
                            contentType: "application/json; charset=utf-8",
                            data: requestObj,
                            url: "sysDictWeb/getSysDictListByCondition.action",
                            dataType: "json",
                            success: function (objLists) {
                                for(var i=0;i<objLists.length;i++){
                                    tempStrPath = objLists[i].path.split("#");
                                    tempqybzMap["pId"] = tempStrPath[tempStrPath.length-3];
                                    tempqybzMap["id"] = objLists[i].id;
                                    tempqybzMap["name"] = objLists[i].sdName;
                                    tempqybzMap["leve"] = objLists[i].sdLevel;
                                    tempqybzMap["pathName"] = objLists[i].pathName.replace(/#/g,"/");
                                    if(objLists[i].isEnd == 0){
                                        tempqybzMap["isParent"] = true;
                                    }else{
                                        tempqybzMap["isParent"] = false;
                                    }
                                    qybzNodes.push(tempqybzMap);
                                    tempqybzMap = {};
                                }
                                var treeObj = $.fn.zTree.getZTreeObj("sysDicTree");
                                if(!treeNode.hasOwnProperty("children")){
                                    qybzNodes = treeObj.addNodes(treeNode,0,qybzNodes,false);
                                }
                            }
                        });
                    }
                }
                /*ztree点击事件*/
                function onClick(event, treeId, treeNode) {
                    $("#parent_id").val(treeNode?treeNode.id:"");
                    $("#parent_name").val(treeNode?treeNode.name:"");
                    $("#qyName").html(treeNode?treeNode.pathName:"");

                    gSysDict.pid=treeNode.id;
                    if(util.typeOf(callback,"string")){
                        eval(callback+"()");
                    }else{
                        callback();
                    }
                }
                /*初始化ztree模型*/
                $.fn.zTree.init($("#sysDicTree"), settingX, qybzNodes);
                common.fnHideProgress();
            }
        });

    },
    fnInitSjzdMngList : function (){
        var queryParams = function (params) {
            var condition = {};
            if($("#parent_id").val()){
                condition["sdPid"] = "eq@"+ $("#parent_id").val();
            }else{
                condition["sdPid"] = "eq@0";
            }
            var pagesMap={};
            var ordersMap = {};
            var param = {
                condition: condition,
                pagesMap: pagesMap,
                ordersMap:ordersMap
            };
            return  param;
        }
        var columns = [
            {
                field:'sdName',
                title: '名称',
                headerSort: false,
                width:200
            }, {
                type: 'action',
                title: '操作',
                headerSort: false,
                formatter:  function(cellComponent, formatterParams){
                    var obj = cellComponent.getData();/*当前行数据*/
                    var value =cellComponent.getValue();
                    var editHTML = "<span class='label label-primary action' onclick='gSysDict.fnSjzdAddOrEdit(\""+obj.id+"\",\""+obj.sdPid+"\")'>编辑</span>";
                    var deleteHTML = "<span class='label label-danger action' onclick='gSysDict.fnDelSysDict(\""+obj.id+"\")'>删除</span>";
                    return editHTML+"&nbsp;&nbsp;"+deleteHTML+"&nbsp;&nbsp;";
                }
            }
        ];
        tblCommon.initTable('#sjzdMngList',{
            index: "id",                    //每一行的唯一标识，一般为主键列
            ajaxURL:"sysDictWeb/getSysDictByPage.action",
            pagination:"remote",
            ajaxParams: queryParams(),
            height: function(){
                return ($(window).height() -175);
            },
            columns:columns
        });
    },
    /*新增字典项*/
    fnSjzdAddOrEdit : function(id,pid){
        // 当前parent_id
        $("#parent_id").val(pid?pid:"");
        var dialogTitle=id==null?"新增字典":"修改字典";
        $.post("sysDict/addOrUpSysDict.action", {"id": id}, function (roleHtml) {
            bootbox.dialog({
                message : roleHtml,
                title :dialogTitle,
                // 退出dialog时的回调函数，包括用户使用ESC键及点击关闭
                onEscape : function() {
                },
                // 是否显示此dialog，默认true
                show : true,
                // 是否显示body的遮罩，默认true
                //backdrop : true,
                // 是否显示关闭按钮，默认true
                closeButton : true,
                // 是否动画弹出dialog，IE10以下版本不支持
                animate : true,
                // dialog的类名
                // className: ,
                buttons : {
                    Cancel : {
                        label : "取消",
                        className : "btn-default",
                        callback : function() {

                        }
                    },
                    OK : {
                        label : "保存",
                        className : "btn-primary",
                        callback : function() {
                            $("#addSysDictForm #btnSubmit").click();
                            return false;
                        }
                    }
                }
            });
        });
    },
    fnSysDictSubmit : function(){
        var inst = util.getFormModel($("#addSysDictForm"));
        inst.sdPid = $("#parent_id").val();
        common.fnShowProgress();
        $.post("sysDictWeb/addOrUpdateSysDict.action", inst, function (resultObj) {
            common.fnHideProgress();
            bootbox.hideAll();
            if(resultObj[0]){
                gMainFun.alert(resultObj[1],"success");
            }else{
                gMainFun.alert(resultObj[1],"danger");
            }
            //gSysDict.fnGetSysDicTree(0);
            //gSysDict.fnInitSjzdMngList(0);
            if($("#parent_id").val().length==0){
                gSysDict.fnInitSjzdMngList(0);
            }else {
                gSysDict.fnInitSjzdMngList($("#parent_id").val());
            }
        });
    },
    fnSjzdUpdatUseState : function(id,pid,typ){
        var title = typ==0?"确定要启用该字典项吗?":typ==1?"确定要禁用该字典项吗?":"";
        bootbox.confirm(title, function(returnVal) {
            if(returnVal){
                $.post("mTblSysDict/deleteSysDicById.action",{"id":id,"typ":typ}, function (str) {
                    if(str){
                        gSysDict.fnGetSysDicTree(id);
                        gSysDict.fnInitSjzdMngList(pid);
                        bootbox.alert("操作成功!");
                    }else{
                        bootbox.alert("操作失败!");
                    }
                });
            }
        });
    },
    fnUpdateSjzdOrderById : function(org1,org2,pid){
        if(org1!="null" && org2!="null"){
            $.post("/mTblSysDict/updateSysdicOrderById.action",{"org1":org1,"org2":org2}, function () {
                // gSysDict.fnGetSysDicTree(org1);
                gSysDict.fnInitSjzdMngList(pid);
            });
        }
    },
    //删除权限模块
    fnDelSysDict : function(id){
        bootbox.confirm("确定要删除该字典项吗?", function(returnVal) {
            if(returnVal){
                common.fnShowProgress();
                $.post("sysDictWeb/deleteSysDict",{"id":id},function(resultObj){
                    common.fnHideProgress();
                    bootbox.hideAll();
                    if(resultObj[0]){
                        gMainFun.alert(resultObj[1],"success");
                    }else{
                        gMainFun.alert(resultObj[1],"danger");
                    }
                    gSysDict.fnGetSysDicTree(0);
                    gSysDict.fnInitSjzdMngList();
                });
            }
        });
    },
}
gSysDict=new SysDict();