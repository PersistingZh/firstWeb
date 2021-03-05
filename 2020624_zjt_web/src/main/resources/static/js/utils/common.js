var creatorId, creatorName, pageJ = null, pagesize, pagenum = 2, isHome = true;
var sysDict = {}, sysDictName = {};
$(window).load(function () {
    $("body").delegate("#sidebar li", "click", function (event) {
        $(this).parents(".sidebar").find("li").removeClass("active");
        $(this).addClass("active");
    });

    $(document).ajaxComplete(function (event, xhr, settings) {
        if (xhr.getResponseHeader("sessionstatus") == "timeOut") {
            if (xhr.getResponseHeader("loginPath")) {
                if (xhr.getResponseHeader("loginrepeat")) {
                    alert(xhr.getResponseHeader("loginrepeat"));
                } else {
                    alert("会话过期，请重新登陆!");
                }
                window.location.replace(xhr.getResponseHeader("loginPath"));
                return;
            } else {
                alert("请求超时请重新登陆 !");
            }
        }
    });
});

var common = null;
var Common = function () {
};

Common.prototype = {
    maxPageSize: 15, /* 设置分频显示最大数 */
    getOrderImageHTML: function (isUp, onclickHTML) {
        var html = "<span ";
        if (isUp) {
            html += "class='fa fa-chevron-up'  style='cursor: pointer' onclick='" + onclickHTML + "' title='向上' >";
        } else {
            html += "class='fa fa-chevron-down' style='cursor: pointer' onclick='" + onclickHTML + "' title='向下' >";
        }
        html += "</span>"
        return html;
    },
    getDeleteImageHTML: function (onclickHTML, title) {
        return "<span class='state-border-danger' onclick='" + onclickHTML + "' title='删除'>" + (title ? title : "删除") + "</span>";
    },
    getInfoImageHTML: function (onclickHTML) {
        return "<span class='label label-primary'  onclick='" + onclickHTML + "' title='查看'></span>";
    },
    getRenameImageHTML: function (onclickHTML, title) {
        return "<span class='state-border-danger' onclick='" + onclickHTML + "' title='" + (title != '' ? title : "重命名") + "'></span>";
    },
    getValidityHTML: function (obj_id, onclickHTML, isValidity) {
        var html = "<input hidefocus='true' type='checkbox' id='vali_" + obj_id + "' onclick='" + onclickHTML + "' ";
        if (isValidity) {
            html += " checked ";
        }
        html += " />";
        return html;
    },
    getSaveHTML: function (onclickHTML) {
        return "<span class='label label-primary' onclick='" + onclickHTML + "' title='保存'></span>";
    },
    getModifyHTML: function (onclickHTML, title) {
        return "<span class='label label-primary'    onclick='" + onclickHTML + "' title='编辑'>" + (title ? title : "编辑") + "</span>";
    },
    getSetUserHTML: function (onclickHTML, title) {
        return "<span class='label label-primary' onclick='" + onclickHTML + "' title='设置周期'>" + (title ? title : "设置周期") + "</span>";
    },
    getViewHTML: function (onclickHTML, title) {
        return "<span class='label label-primary' onclick='" + onclickHTML + "' title='查看'>" + (title ? title : "查看") + "</span>";
    },
    getAddHTML: function (onclickHTML, title) {
        var _title = "添加";
        if (title) {
            _title = title;
        }

        return "<span class='label label-primary'  onclick='" + onclickHTML + "' title='" + _title + "'>" + _title + "</span>";

    },
    getPopoverHTML: function (option) {
        if (util.isEmptyDefaultVal(option.content) == "") {
            return "";
        } else {
            var popHtml = [];
            var options = $.extend({}, {
                id: "",
                trigger: "hover",
                animation: "pop",
                title: "",
                placement: "top",
                popwidth: ""
            }, option);
            popHtml.push("<div id='" + options.id + "' style='height:18px;' class='pop_tr textElli-md'",
                "data-width='" + options.popwidth + "' data-trigger='" + options.trigger + "' data-animation='" + options.animation + "'",
                " data-placement='auto-" + options.placement + "' data-title='" + options.title + "'  >",
                "<div class='hidee'>" + options.content + "</div>" + util.isEmptyDefaultVal(options.value) + "</div>");
            return popHtml.join('');
        }
    },
    getPopoverSPAN: function (option) {
        if (util.isEmptyDefaultVal(option.content) == "") {
            return "";
        } else {
            var popHtml = [];
            var options = $.extend({}, {
                id: "",
                trigger: "hover",
                animation: "pop",
                title: "",
                placement: "top",
                popwidth: ""
            }, option);
            popHtml.push("<span id='" + options.id + "' class='pop_tr inline'",
                "data-width='" + options.popwidth + "' data-trigger='" + options.trigger + "' data-animation='" + options.animation + "'",
                " data-placement='auto-" + options.placement + "' data-title='" + options.title + "'>",
                "<span class='hidee'>" + options.content + "</span>" + util.isEmptyDefaultVal(options.value) + "</span>");
            return popHtml.join('');
        }
    },
    getServerDate: function (timeObj) {
        SystemUtilDwr.getServerDate(function (data) {
            $("#" + timeObj).val(data);
        });
    },
    getYesImage: function () {
        return "<span class='finished'></span>";
    },
    getAlertImage: function () {
        return "<img src='image/qietu/alert.gif'/>";
    },
    fnBindSearchTxt: function (options) { // 搜索文本框邦定
        var timeoutId;
        $(options.btnSearch).click(function () {
            options.fnDo();
        });
        $(options.searchEl).keydown(function (e) {
            if (e.keyCode == 13) {
                if (this.value == options.searchTxt) {
                    this.select();
                    return false;
                } else {
                    /* 对上次未完成的延时操作进行取消 */
                    clearTimeout(timeoutId);
                    /* 对于服务器端进行交互延迟500ms，避免快速打字造成的频繁请求 */
                    timeoutId = setTimeout(function () {
                        options.fnDo();
                    }, 600);
                }
            }
        }).focus(function () {
            $(this).css("color", "#000000");
            this.value = '';
        }).blur(function () {
            if (this.value == '') {
                this.value = options.searchTxt;
                $(this).css("color", "#aaaaaa");
            }
        }).val(options.searchTxt).css("color", "#aaaaaa");
    },
    fnAjaxPage: function (frm, page, param) {
        /* 设置当前页 */
        //common.fnShowProgress();
        $.post(page + ".action", param, function (data) {
            $(frm.trim()).html(data);
            //common.fnHideProgress();
        });
    },
    fnAjaxPageWx: function (url) {
        window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx77e11672b3d0e978&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
    },
    //fnGetPageCondition : function() {
    //	pagenum = $("#pagenum").val() * 1;
    //	var pageCondtion = {
    //		firstResult : (pagenum - 1) * pagesize,
    //		maxResult : pagesize
    //	}
    //	return pageCondtion;
    //},
    fnTryCatch: function (fun, msg) {
        try {
            fun();
        } catch (e) {
            alert(msg + ":" + e);
        }
    },
    fnSetTabChange: function (tabIndex, tabClass) {
        $(tabClass).hide();
        $((tabClass + ":eq(" + tabIndex + ")")).show();
    },
    subTabChange: function (parentId, targetClass, showObjId) {
        $("#" + parentId).find("." + targetClass).hide();
        $("#" + parentId + " #" + showObjId).show();
    },
    /* 城市级联 */
    fnCascade: function (divId, pid, cid, did, type) {
        /* 初始化所有省份 */
        if (type == "0") {
            common.fnFindAllProvinces(divId, '', '', '')
        }
        /* 只传省份 */
        if (type == "1") {
            common.fnFindCasCitys(divId, pid)
        }
        /* 只传市 */
        if (type == "2") {
            common.fnFindCasDistricts(divId, cid);
        }
        /* 传省市区 */
        if (type == "3") {
            common.fnFindAllProvinces(divId, pid, cid, did);
        }
    },
    fnFindAllProvinces: function (divId, pid, cid, did) {
        // common.fnShowProgress();
        CityJs.fnFindAllProvinces(function (objList) {
            var proTemp;
            var htmlInfo = [];
            var data = objList;
            for (var i = 0; i < data.length; i++) {
                proTemp = data[0];
                var province = data[i];
                htmlInfo.push("<option value='" + province.pid + "' ", province.pid == pid ? 'selected' : '', ">", province.name, "</option>");
            }
            $(divId + " select").eq(0).html(htmlInfo.join(""));

            if (util.isEmptyDefaultVal(pid) != "") {
                common.fnFindCasCitys(divId, pid, cid, did);
            } else {
                common.fnFindCasCitys(divId, proTemp.pid, cid, did);
            }
            // common.fnHideProgress();
        });
    },
    fnShowProgress: function (msg, containerId) {
        if (containerId) {
            containerId = "#" + containerId;
        }
        containerId = containerId || "body";

        var runMsg = msg || "运行中...";
        if ($("#progressEl", containerId).length == 0) {
            $(['<div id="progressEl" data-retain="0" class="hidee" style="z-index: 9999;position: absolute; top:0px;bottom:0px;left:0px;right:0px;">',
                '<div style="position: absolute; top:0px;bottom:0px;left:0px;right:0px;filter: alpha(opacity=0);-moz-opacity: 0.0;opacity: 0.0;background-color: white;"></div>',
                '<div style="position: fixed;top: 50%;left: 50%;margin-left: -75px;margin-top: -70px; width: 150px;min-height: 50px;border-radius:10px;text-align:center;background: rgba(204,204,204,.3);padding: 25px 0;">',
                '<div class="loader loader--audioWave"></div><div class="loader-text" style="color:black;">', runMsg, '</div></div>',
                '<div class="msg"  style="font-size: 14px; color:black;"></div>',
                '</div>'
            ].join('')).appendTo(containerId).click(function (e) {
                return false;
            });
        }

        var $dom = $("#progressEl", containerId);
        var retain = $dom.data("retain");
        $dom.data("retain", (retain * 1 + 1));

        $dom.show().find(".loader-text").text(runMsg);
    },
    fnHideProgress: function (containerId, skipRetain) {
        skipRetain = (skipRetain || true) || (containerId != true);
        if (containerId) {
            containerId = "#" + containerId;
        }
        containerId = containerId || "body";

        var $progress = $("#progressEl", containerId),
            retain = ($progress.data("retain") * 1 - 1);

        $progress.data("retain", retain);

        if (retain <= 0 || skipRetain) {
            $progress.hide();
        }
    },
    hideBootboxLoading: function () {
        $("body").removeClass("modal-open");
        $("body #bootbox-loading").remove();
    },
    fnShowBigImgDlg: function (imgObj) {
        if ($(imgObj).attr("src") == "") {
            return;
        }
        $("#showBigImgDlg #imgObj").attr("src", $(imgObj).attr("src"));
        $("#showBigImgDlg").show();
    },
    fnCloseBigImgDlg: function () {
        $("#showBigImgDlg #imgObj").attr("src", "image/untitled.png");
        $("#showBigImgDlg").hide();
    },
    fnAddTooltip: function (objId) {
        $(objId).addClass("form-erro").focus().tooltip("show");
    },
    fnAddNav: function (modelName) {
        currentPageId = "";//清除当前页面的id
        $("#nav-li").html(modelName);

    },
    showImg: function (self) {
        var postHtml = [];
        postHtml.push("<div class='row center' style=' overflow: auto; max-height: 900px;mix-width: 600px;'>");
        postHtml.push("	<img src='", $(self).attr("src"), "' style='max-width: 1200px;mix-width: 900px;'/>");
        postHtml.push("</div>");
        postHtml.push("<div>");
        /*postHtml.push('<a  href = "ShareDownloadImageServlet?numv='+$(self).attr("numv")+'&takenId='+$(self).attr("takenId")+'">图片下载</a>');
        postHtml.push("</div>");*/
        bootbox.dialog({
            message: postHtml.join(""),
            title: "图片预览",
            className: "modal_absolute",
            onEscape: function () {
            }, show: true, backdrop: true, closeButton: true, animate: true,
            buttons: {
                OK: {
                    label: "下载",
                    className: "btn-danger",
                    callback: function () {
                        // $("."+id).click();
                        // console.log(self);
                        // console.log(self.title);
                        var values = $(self).attr("value");
                        // 路径 — 名称
                        util.fnDownload(self.title, values);
                        gMainFun.alert("下载成功", "success");
                    }
                },
                Cancel: {
                    label: "关闭", className: "btn-default", callback: function () {
                    }
                },
            }
        });
    },
    fnCheckDivNotInput: function (obj) {
        if ($(obj).html() != "") {
            $(obj).parents(".form-group").removeClass("has-error has-danger");
            $(obj).next().html("");
        } else {
            $(obj).parents(".form-group").addClass("has-error has-danger");
            $(obj).next().html($(obj).attr("title"));
        }
    },
    //字典表组装
    /**
     * @auto lmm
     * @param id 组装位置
     * @param key 组装位置
     * @version v1.0
     * @return id 为option值
     */
    fnInitOptionValueByKey: function (id, keyEle) {
        jQuery.each(sysDict, function (key, obj) {
            if (key == keyEle) {
                $(id).html("<option value=''>请选择</option>");
                jQuery.each(obj, function (key2, value) {
                    $(id).append("<option value='" + value["id"] + "'>" + value["sdName"] + "</option>");
                });
            }
        });

    },
    /**
     * @auto lmm
     * @param id 组装位置
     * @param key 组装位置
     * @version v1.0
     * @return sdCode 为option值
     */
    fnInitOptionCodeByKey: function (id, keyEle) {
        jQuery.each(sysDict, function (key, obj) {
            if (key == keyEle) {
                $(id).html("<option value=''>请选择</option>");
                jQuery.each(obj, function (key2, value) {
                    if (value["sdCode"]) {
                        $(id).append("<option value='" + value["sdCode"] + "' data-sdVal= '" + value["sdValue"] + "'>" + value["sdName"] + "</option>");
                    }
                });
            }
        });

    },
    numbergsh: function (data, val) {
        var numbers = '';
        // 保留几位小数后面添加几个0
        for (var i = 0; i < val; i++) {
            numbers += '0';
        }
        var s = 1 + numbers;
        // 如果是整数需要添加后面的0
        var spot = "." + numbers;
        // Math.round四舍五入
        //  parseFloat() 函数可解析一个字符串，并返回一个浮点数。
        var value = Math.round(parseFloat(data) * s) / s;
        // 从小数点后面进行分割
        var d = value.toString().split(".");
        if (d.length == 1) {
            value = value.toString() + spot;
            return value;
        }
        if (d.length > 1) {
            if (d[1].length < 2) {
                value = value.toString() + "0";
            }
            return value;
        }
    },
    /**
     * 设置搜索城市地址树结构
     * @param treeId 树的id
     * @param formId null
     * @param parentId 隐藏域 Id
     * @param havaPid 是否包含父id
     * @param callback null
     */
    fnGetSysCityTree: function (treeId, formId, parentId, havaPid, callback) {
        common.treeDiv = treeId;
        common.treeDiv_treebox = treeId + "_treebox";

        var proddistCodeCurr = $("#" + treeId + "_cityId").val();
        var condition = {}, orderMap = {"orderr": "asc"};
        if (!util.isEmpty(parentId)) {
            condition["sdPid"] = parentId;
        } else {
            condition["sdPid"] = "行政区域";
        }
        if (!util.isEmpty(havaPid)) {
            condition["havaPid"] = havaPid;
        }
        var req = {
            condition: condition,
            ordersMap: orderMap
        };
        var requestObj = JSON.stringify(req);
        $.post({
            type: "post",
            contentType: "application/json; charset=utf-8",
            data: requestObj,
            url: "sysDictWeb/getCityListByMap.action",
            dataType: "json",
            success: function (objData) {
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
                    //回调函数
                    callback: {
                        //onExpand: onExpand,
                        onClick: onClick
                    }
                };
                var qybzNodes = [];//ztree 树数据
                var tempqybzMap = {};//ztree 内部树数据
                qybzNodes.push(
                    // {"id":"f01","name":"无区域","leve":"0","pId":"","isParent":true }
                );
                //遍历循环组装ztree树数据
                var isTrue = true;
                for (var i = 0; i < objData.length; i++) {
                    tempqybzMap["id"] = objData[i].sdCode;
                    tempqybzMap["name"] = objData[i].sdName;
                    if (objData[i].sdCode == parentId && isTrue) {
                        $("#" + treeId + "_cityName").val("请选择区域");
                        $("#" + treeId + "_cityId").val(parentId);
                    }
                    if (objData[i].sdCode == proddistCodeCurr) {
                        isTrue = false;
                        $("#" + treeId + "_cityName").val(objData[i].sdName);
                        $("#" + treeId + "_cityId").val(objData[i].sdCode);
                        tempqybzMap["open"] = true;
                    }
                    // tempqybzMap["longitude"] = objData[i].longitude;
                    // tempqybzMap["latitude"] = objData[i].latitude;
                    tempqybzMap["leve"] = objData[i].sdLevel;
                    tempqybzMap["pId"] = objData[i].sdPid;
                    tempqybzMap["isParent"] = true;//文件夹图标
                    if (objData[i].isEnd != 0) {
                        tempqybzMap["isParent"] = false;
                    }
                    //如果是一级父节点
                    if (objData[i].sdLevel == 1) {
                        tempqybzMap["open"] = true;
                    }
                    qybzNodes.push(tempqybzMap);
                    tempqybzMap = {};
                }

                //ztree点击事件
                function onClick(event, treeIdSelf, treeNode) {
                    $("#" + treeId + "_cityName").val((treeNode ? treeNode.name : ""));
                    $("#" + treeId + "_cityId").val((treeNode ? treeNode.id : ""));
                    common.hideMenuX(treeId + "_treebox");
                    if (callback) {
                        if (util.typeOf(callback, "string")) {
                            eval(callback + "(" + JSON.stringify(treeNode) + ")");
                        } else {
                            callback(JSON.stringify(treeNode));
                        }
                    }
                }

                function onExpand(event, treeId, treeNode) {
                    if (treeNode.open) {
                        qybzNodes = [];
                        var condition1 = {};
                        condition1["sdPid"] = treeNode.id;
                        //condition["cityParent"] = "eq@" + treeNode.id;
                        //condition["isDel"] = "eq@0";
                        var orderMap1 = {};
                        var req1 = {
                            condition: condition1,
                            ordersMap: orderMap1
                        };
                        var requestObj1 = JSON.stringify(req1);
                        $.post({
                            type: "post",
                            contentType: "application/json; charset=utf-8",
                            data: requestObj1,
                            url: "sysDictWeb/getCityListByMap.action",
                            dataType: "json",
                            success: function (objLists) {
                                console.info(objLists);
                                for (var i = 0; i < objLists[0].length; i++) {
                                    tempqybzMap["pId"] = objLists[0][i].sdPid;
                                    tempqybzMap["id"] = objLists[0][i].sdCode;
                                    tempqybzMap["name"] = objLists[0][i].sdName;
                                    tempqybzMap["leve"] = objLists[0][i].sdLevel;
                                    if (objLists[0][i].isEnd == 0) {
                                        tempqybzMap["isParent"] = true;
                                    } else if (objLists[0][i].isEnd == 1) {
                                        tempqybzMap["isParent"] = false;
                                    }
                                    qybzNodes.push(tempqybzMap);
                                    tempqybzMap = {};
                                }
                                var treeObj = $.fn.zTree.getZTreeObj("treeDiv_treebox");
                                if (!treeNode.hasOwnProperty("children")) {
                                    qybzNodes = treeObj.addNodes(treeNode, 0, qybzNodes, false);
                                }
                            }
                        });
                    }
                }
                //初始化ztree模型
                $.fn.zTree.init($("#" + treeId + "_ul"), settingX, qybzNodes);
                common.fnHideProgress();
            }
        });
        $("#" + treeId + "_cityName").click(function () {
            if ($("#" + treeId + "_treebox").is(':hidden')) {
                common.showMenuX(treeId + "_treebox");
            } else {
                common.hideMenuX(treeId + "_treebox");
            }
            $("body").bind("mousedown", common.onBodyDown);
        });
    },
    fnGetSysGaTree: function (treeId, formId, parentId, havaPid, callback) {
        common.treeDiv = treeId;
        common.treeDiv_treebox = treeId + "_treebox";
        var proddistCodeCurr = $("#" + treeId + "_gaId").val();
        var condition = {}, orderMap = {"orderr": "asc"};
        condition["type"] = "公安机构";
        if (!util.isEmpty(havaPid)) {
            condition["havaPid"] = havaPid;
        }
        var req = {
            condition: condition,
            ordersMap: orderMap
        };
        var requestObj = JSON.stringify(req);
        $.post({
            type: "post",
            contentType: "application/json; charset=utf-8",
            data: requestObj,
            url: "sysDictWeb/getCityListByMap.action",
            dataType: "json",
            success: function (objData) {
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
                    //回调函数
                    callback: {
                        //onExpand: onExpand,
                        onClick: onClick
                    }
                };
                var qybzNodes = [];//ztree 树数据
                var tempqybzMap = {};//ztree 内部树数据
                qybzNodes.push(
                );
                //遍历循环组装ztree树数据
                var isTrue = true;
                for (var i = 0; i < objData.length; i++) {
                    tempqybzMap["id"] = objData[i].sdCode;
                    tempqybzMap["name"] = objData[i].sdName;
                    if (objData[i].sdCode == parentId && isTrue) {
                        $("#" + treeId + "_gaName").val("请选择公安组织架构");
                        $("#" + treeId + "_gaId").val(parentId);
                    }
                    if (objData[i].sdCode == proddistCodeCurr) {
                        isTrue = false;
                        $("#" + treeId + "_gaName").val(objData[i].sdName);
                        $("#" + treeId + "_gaId").val(objData[i].sdCode);
                        tempqybzMap["open"] = true;
                    }
                    // tempqybzMap["longitude"] = objData[i].longitude;
                    // tempqybzMap["latitude"] = objData[i].latitude;
                    tempqybzMap["leve"] = objData[i].sdLevel;
                    tempqybzMap["pId"] = objData[i].sdPid;
                    tempqybzMap["isParent"] = true;//文件夹图标
                    if (objData[i].isEnd != 0) {
                        tempqybzMap["isParent"] = false;
                    }
                    //如果是一级父节点
                    if (objData[i].sdLevel == 1) {
                        tempqybzMap["open"] = true;
                    }
                    qybzNodes.push(tempqybzMap);
                    tempqybzMap = {};
                }

                //ztree点击事件
                function onClick(event, treeIdSelf, treeNode) {
                    $("#" + treeId + "_gaName").val((treeNode ? treeNode.name : ""));
                    $("#" + treeId + "_gaId").val((treeNode ? treeNode.id : ""));
                    common.hideMenuX(treeId + "_treebox");
                    if (callback) {
                        if (util.typeOf(callback, "string")) {
                            eval(callback + "(" + JSON.stringify(treeNode) + ")");
                        } else {
                            callback(JSON.stringify(treeNode));
                        }
                    }
                }

                function onExpand(event, treeId, treeNode) {
                    if (treeNode.open) {
                        qybzNodes = [];
                        var condition1 = {};
                        condition1["sdPid"] = treeNode.id;

                        var orderMap1 = {};
                        var req1 = {
                            condition: condition1,
                            ordersMap: orderMap1
                        };
                        var requestObj1 = JSON.stringify(req1);
                        $.post({
                            type: "post",
                            contentType: "application/json; charset=utf-8",
                            data: requestObj1,
                            url: "sysDictWeb/getCityListByMap.action",
                            dataType: "json",
                            success: function (objLists) {
                                for (var i = 0; i < objLists[0].length; i++) {
                                    tempqybzMap["pId"] = objLists[0][i].sdPid;
                                    tempqybzMap["id"] = objLists[0][i].sdCode;
                                    tempqybzMap["name"] = objLists[0][i].sdName;
                                    tempqybzMap["leve"] = objLists[0][i].sdLevel;
                                    if (objLists[0][i].isEnd == 0) {
                                        tempqybzMap["isParent"] = true;
                                    } else if (objLists[0][i].isEnd == 1) {
                                        tempqybzMap["isParent"] = false;
                                    }
                                    qybzNodes.push(tempqybzMap);
                                    tempqybzMap = {};
                                }
                                var treeObj = $.fn.zTree.getZTreeObj("treeDiv_treebox");
                                if (!treeNode.hasOwnProperty("children")) {
                                    qybzNodes = treeObj.addNodes(treeNode, 0, qybzNodes, false);
                                }
                            }
                        });
                    }
                }

                //初始化ztree模型
                $.fn.zTree.init($("#" + treeId + "_ul"), settingX, qybzNodes);
                common.fnHideProgress();
            }
        });
        $("#" + treeId + "_gaName").click(function () {
            if ($("#" + treeId + "_treebox").is(':hidden')) {
                common.showMenuX(treeId + "_treebox");
            } else {
                common.hideMenuX(treeId + "_treebox");
            }
            $("body").bind("mousedown", common.onBodyDown);
        });


    },
    fnGetSysAreaTree: function (treeId, formId, parentId, havaPid, callback) {
        common.treeDiv = treeId;
        common.treeDiv_treebox = treeId + "_treebox";
        var proddistCodeCurr = $("#" + treeId + "_areaCode").val();
        var condition = {}, orderMap = {"orderr": "asc"};
        if (!util.isEmpty(parentId)) {
            condition["sdPid"] = parentId;
        }else {
            condition["sdPid"] = "区域类型";
        }
        if (!util.isEmpty(havaPid)) {
            condition["havaPid"] = havaPid;
        }
        var req = {
            condition: condition,
            ordersMap: orderMap
        };
        var requestObj = JSON.stringify(req);
        $.post({
            type: "post",
            contentType: "application/json; charset=utf-8",
            data: requestObj,
            url: "sysDictWeb/getCityListByMap.action",
            dataType: "json",
            success: function (objData) {
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
                    //回调函数
                    callback: {
                        //onExpand: onExpand,
                        onClick: onClick
                    }
                };
                var qybzNodes = [];//ztree 树数据
                var tempqybzMap = {};//ztree 内部树数据
                qybzNodes.push(
                );
                //遍历循环组装ztree树数据
                var isTrue = true;
                for (var i = 0; i < objData.length; i++) {
                    tempqybzMap["id"] = objData[i].sdCode;
                    tempqybzMap["name"] = objData[i].sdName;
                    if (objData[i].sdCode == parentId && isTrue) {
                        $("#" + treeId + "_areaTypeName").val("请选择区域类型");
                        $("#" + treeId + "_areaCode").val(parentId);
                    }
                    if (objData[i].sdCode == proddistCodeCurr) {
                        isTrue = false;
                        $("#" + treeId + "_areaTypeName").val(objData[i].sdName);
                        $("#" + treeId + "_areaCode").val(objData[i].sdCode);
                        tempqybzMap["open"] = true;
                    }
                    // tempqybzMap["longitude"] = objData[i].longitude;
                    // tempqybzMap["latitude"] = objData[i].latitude;
                    tempqybzMap["leve"] = objData[i].sdLevel;
                    tempqybzMap["pId"] = objData[i].sdPid;
                    tempqybzMap["isParent"] = true;//文件夹图标
                    if (objData[i].isEnd != 0) {
                        tempqybzMap["isParent"] = false;
                    }
                    //如果是一级父节点
                    if (objData[i].sdLevel == 1) {
                        tempqybzMap["open"] = true;
                    }
                    qybzNodes.push(tempqybzMap);
                    tempqybzMap = {};
                }

                //ztree点击事件
                function onClick(event, treeIdSelf, treeNode) {
                    $("#" + treeId + "_areaTypeName").val((treeNode ? treeNode.name : ""));
                    $("#" + treeId + "_areaCode").val((treeNode ? treeNode.id : ""));
                    common.hideMenuX(treeId + "_treebox");
                    if (callback) {
                        if (util.typeOf(callback, "string")) {
                            eval(callback + "(" + JSON.stringify(treeNode) + ")");
                        } else {
                            callback(JSON.stringify(treeNode));
                        }
                    }
                }

                function onExpand(event, treeId, treeNode) {
                    if (treeNode.open) {
                        qybzNodes = [];
                        var condition1 = {};
                        condition1["sdPid"] = treeNode.id;

                        var orderMap1 = {};
                        var req1 = {
                            condition: condition1,
                            ordersMap: orderMap1
                        };
                        var requestObj1 = JSON.stringify(req1);
                        $.post({
                            type: "post",
                            contentType: "application/json; charset=utf-8",
                            data: requestObj1,
                            url: "sysDictWeb/getCityListByMap.action",
                            dataType: "json",
                            success: function (objLists) {
                                for (var i = 0; i < objLists[0].length; i++) {
                                    tempqybzMap["pId"] = objLists[0][i].sdPid;
                                    tempqybzMap["id"] = objLists[0][i].sdCode;
                                    tempqybzMap["name"] = objLists[0][i].sdName;
                                    tempqybzMap["leve"] = objLists[0][i].sdLevel;
                                    if (objLists[0][i].isEnd == 0) {
                                        tempqybzMap["isParent"] = true;
                                    } else if (objLists[0][i].isEnd == 1) {
                                        tempqybzMap["isParent"] = false;
                                    }
                                    qybzNodes.push(tempqybzMap);
                                    tempqybzMap = {};
                                }
                                var treeObj = $.fn.zTree.getZTreeObj("treeDiv_treebox");
                                if (!treeNode.hasOwnProperty("children")) {
                                    qybzNodes = treeObj.addNodes(treeNode, 0, qybzNodes, false);
                                }
                            }
                        });
                    }
                }

                //初始化ztree模型
                $.fn.zTree.init($("#" + treeId + "_ul"), settingX, qybzNodes);
                common.fnHideProgress();
            }
        });
        $("#" + treeId + "_areaTypeName").click(function () {
            if ($("#" + treeId + "_treebox").is(':hidden')) {
                common.showMenuX(treeId + "_treebox");
            } else {
                common.hideMenuX(treeId + "_treebox");
            }
            $("body").bind("mousedown", common.onBodyDown);
        });


    },
    showMenuX: function (boxdiv) {
        $("#" + boxdiv).fadeIn();
    },
    onBodyDownX: function (event) {
        if (!(event.target.id == "ztreeTjSearchDivX" || $(event.target).parents("#ztreeTjSearchDivX").length > 0)) {
            common.hideMenuX2();
        }
    },
    hideMenuX: function (boxdiv) {
        $("#" + boxdiv).fadeOut("fast");
        $("body").unbind("mousedown", common.onBodyDown);
    },
    onBodyDown: function (event) {
        if (!(event.target.id == common.treeDiv_treebox || event.target.id == common.treeDiv ||
            $(event.target).parents(common.treeDiv_treebox).length > 0 || $(event.target).is("[treenode_switch]"))) {
            common.hideMenuX(common.treeDiv_treebox);
        }
    },
    //进行城市树结构加载操作
    tjZtreeShowX: function () {
       /* $('#ztreeTjSearchDivX').show();*/
        $("body").bind("mousedown", function (e) {
            common.onBodyDownX(e);
        });
    },
    onBodyDownX: function (event) {
        if (!(event.target.id == "ztreeTjSearchDivX" || $(event.target).parents("#ztreeTjSearchDivX").length > 0)) {
            common.hideMenuX2();
        }
    },
    hideMenuX2: function () {
        $("#ztreeTjSearchDivX").fadeOut("fast");
        $("body").unbind("mousedown", function (e) {
            common.onBodyDownX(e);
        });
    },
    //根据出生日期算出年龄
    jsGetAge: function (strBirthday) {
        var returnAge;
        var strBirthdayArr = strBirthday.split("-");
        var birthYear = strBirthdayArr[0];
        var birthMonth = strBirthdayArr[1];
        var birthDay = strBirthdayArr[2];

        d = new Date();
        var nowYear = d.getFullYear();
        var nowMonth = d.getMonth() + 1;
        var nowDay = d.getDate();

        if (nowYear == birthYear) {
            returnAge = 0;//同年 则为0岁
        } else {
            var ageDiff = nowYear - birthYear; //年之差
            if (ageDiff > 0) {
                if (nowMonth == birthMonth) {
                    var dayDiff = nowDay - birthDay;//日之差
                    if (dayDiff < 0) {
                        returnAge = ageDiff - 1;
                    } else {
                        returnAge = ageDiff;
                    }
                } else {
                    var monthDiff = nowMonth - birthMonth;//月之差
                    if (monthDiff < 0) {
                        returnAge = ageDiff - 1;
                    } else {
                        returnAge = ageDiff;
                    }
                }
            } else {
                returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
            }
        }
        return returnAge;//返回周岁年龄
    },
    //预案文件下载
    downLoadFile: function (dom) {
        var filePath = $(dom).attr("value");
        var fileName = $(dom).attr("name");
        var newFileName = fileName.substring(0, fileName.indexOf("."));
        util.fnDownload(filePath, newFileName);
    },
    showListImgByImgPath: function (srcValue) {
        if (!util.isEmpty(srcValue)) {
            return "<img src='mFileUpdate/fileDownload?filename=" + srcValue + "&filepath=" + srcValue + "' style='width: 100px;height:60px'/>";
        }
    },
    dateFormat : function (fmt, date) {
        var ret;
        const opt = {
            "Y+": date.getFullYear().toString(),        // 年
            "m+": (date.getMonth() + 1).toString(),     // 月
            "d+": date.getDate().toString(),            // 日
            "H+": date.getHours().toString(),           // 时
            "M+": date.getMinutes().toString(),         // 分
            "S+": date.getSeconds().toString()          // 秒
            // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (var k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
            };
        };
        return fmt;
    }
};

common = new Common();


/* 图片容错. */
function imgNoFind(img) {
    img.src = "images/bg/user_ico.png";
    img.onerror = null;
    /* 控制不要一直跳动 */
}

/* cookie */
function SetCookie(name, value)// 两个参数，一个是cookie的名子，一个是值
{
    var Days = 30; // 此 cookie 将被保存 30 天
    var exp = new Date(); // new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getCookie(name)// 取cookies函数
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null)
        return unescape(arr[2]);
    return null;

}

function delCookie(name)// 删除cookie
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}



function watermark(settings) {

    //默认设置
    var defaultSettings={
        watermark_txt:"text",
        watermark_x:8,//水印起始位置x轴坐标
        watermark_y:7,//水印起始位置Y轴坐标
        watermark_rows:20,//水印行数
        watermark_cols:20,//水印列数
        watermark_x_space:100,//水印x轴间隔
        watermark_y_space:50,//水印y轴间隔
        watermark_color:'#333',//水印字体颜色
        watermark_alpha:0.1,//水印透明度
        watermark_fontsize:'18px',//水印字体大小
        watermark_font:'微软雅黑',//水印字体
        watermark_width:200,//水印宽度
        watermark_height:100,//水印长度
        watermark_angle:15//水印倾斜度数
    };
    //采用配置项替换默认值，作用类似jquery.extend
    if(arguments.length===1&&typeof arguments[0] ==="object" )
    {
        var src=arguments[0]||{};
        for(key in src)
        {
            if(src[key]&&defaultSettings[key]&&src[key]===defaultSettings[key])
                continue;
            else if(src[key])
                defaultSettings[key]=src[key];
        }
    }

    var oTemp = document.createDocumentFragment();

    //获取页面最大宽度
    var page_width = Math.max(document.body.scrollWidth,document.body.clientWidth)-50;
    //获取页面最大长度
    var page_height = Math.max(document.body.scrollHeight,document.body.clientHeight)-50;

    //如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
    if (defaultSettings.watermark_cols == 0 || (parseInt(defaultSettings.watermark_x + defaultSettings.watermark_width *defaultSettings.watermark_cols + defaultSettings.watermark_x_space * (defaultSettings.watermark_cols - 1)) > page_width)) {
        defaultSettings.watermark_cols = parseInt((page_width-defaultSettings.watermark_x+defaultSettings.watermark_x_space) / (defaultSettings.watermark_width + defaultSettings.watermark_x_space));
        defaultSettings.watermark_x_space = parseInt((page_width - defaultSettings.watermark_x - defaultSettings.watermark_width * defaultSettings.watermark_cols) / (defaultSettings.watermark_cols - 1));
    }
    //如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
    if (defaultSettings.watermark_rows == 0 || (parseInt(defaultSettings.watermark_y + defaultSettings.watermark_height * defaultSettings.watermark_rows + defaultSettings.watermark_y_space * (defaultSettings.watermark_rows - 1)) > page_height)) {
        defaultSettings.watermark_rows = parseInt((defaultSettings.watermark_y_space + page_height - defaultSettings.watermark_y) / (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
        defaultSettings.watermark_y_space = parseInt(((page_height - defaultSettings.watermark_y) - defaultSettings.watermark_height * defaultSettings.watermark_rows) / (defaultSettings.watermark_rows - 1));
    }
    var x;
    var y;
    for (var i = 0; i < defaultSettings.watermark_rows; i++) {
        y = defaultSettings.watermark_y + (defaultSettings.watermark_y_space + defaultSettings.watermark_height) * i;
        for (var j = 0; j < defaultSettings.watermark_cols; j++) {
            x = defaultSettings.watermark_x + (defaultSettings.watermark_width + defaultSettings.watermark_x_space) * j;

            var mask_div = document.createElement('div');
            mask_div.id = 'mask_div' + i + j;
            mask_div.appendChild(document.createTextNode(defaultSettings.watermark_txt));
            //设置水印div倾斜显示
            mask_div.style.webkitTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.MozTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.msTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.OTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.transform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.visibility = "";
            mask_div.style.position = "absolute";
            mask_div.style.left = x + 'px';
            mask_div.style.top = y + 'px';
            mask_div.style.overflow = "hidden";
            mask_div.style.zIndex = "11";
            mask_div.style.pointerEvents="none";
            //mask_div.style.border="solid #eee 1px";
            mask_div.style.opacity = defaultSettings.watermark_alpha;
            mask_div.style.fontSize = defaultSettings.watermark_fontsize;
            mask_div.style.fontFamily = defaultSettings.watermark_font;
            mask_div.style.color = defaultSettings.watermark_color;
            mask_div.style.textAlign = "center";
            mask_div.style.width = defaultSettings.watermark_width + 'px';
            mask_div.style.height = defaultSettings.watermark_height + 'px';
            mask_div.style.display = "block";
            oTemp.appendChild(mask_div);
        };
    };
    document.body.appendChild(oTemp);
}