;(function($,window,document,undefined){

var ZTreeUtilsPQ = function () { };
    ZTreeUtilsPQ.prototype = {
        getZTreeObj: function(treeId){
        	if(typeof treeId == "string"){
                return $.fn.zTree.getZTreeObj(treeId);
			}else{
        		treeId
			}
		},
		/*根据treeId获取当前zTree选中的值*/
        getSelectNodesArr : function (treeId) {
            try {
                var zTree = myTreeUtilsPq.getZTreeObj(treeId),
                    nodes = zTree.getSelectedNodes();
                var name = "";
                var id = "";
                nodes.sort(function compare(a, b) { return a.id - b.id; });
                for (var i = 0, l = nodes.length; i < l; i++) {
                    name = nodes[i].name;
                    id = nodes[i].id;
                }
                return [name, id];
            } catch(e) {
                return ["",""];
                console.info("没有找到id为："+treeId+"的zTree！");
            }
        },
        /**
		 * 根据节点数据的属性搜索，获取条件完全匹配的节点数据 JSON 对象 需要精确匹配的属性名称
		 *treeId 树ID
		 * key数据的属性（id,name,pid）
		 *
         */
		fnGetNodeByParam : function (treeId,key,value,parentNode) {
            return myTreeUtilsPq.getZTreeObj(treeId).getNodeByParam("id", 1, parentNode);
        },

		/*递归打开被选中的select上级*/
        fnOpenParentNode : function(treeId,node) {
            node = myTreeUtilsPq.getZTreeObj(treeId).getNodeByParam("id", node.pId, null);
            zTree.expandNode(node, true);

            if(!util.isEmpty(node.pId)) {
                myTreeUtilsPq.openParentNode(zTree,node);
            }
        },
		/* 获取有复选框组的name与id */
        getCheckedNodesArr :  function (treeId) {
            try {
                var zTree = myTreeUtilsPq.getZTreeObj(treeId),
                    nodes = zTree.getCheckedNodes(true);
                var name = [];
                var id = [];
                for (var i = 0, l = nodes.length; i < l; i++) {
                    name.push(nodes[i].name);
                    id.push(nodes[i].id);
                }
                return [name, id];
            } catch(e) {
                return [[], []];
                console.info(e.message);
            }
        },
        getCheckedSigleArrs : function (treeId) {
            var zTree = myTreeUtilsPq.getZTreeObj(treeId),
                nodes = zTree.getCheckedNodes(true);
            var ids = [], pids = [], pNode = {};
            var level = -1;
            for (var i = 0, l = nodes.length; i < l; i++) {
                if(nodes[i].isParent && (level == -1 || nodes[i].level == level)) {
                    pids.push(nodes[i].id);
                    level = nodes[i].level;
                    pNode = nodes[i];
                    ids.push(nodes[i].id);
                } else if(pNode.id != nodes[i].getParentNode().id) {
                    ids.push(nodes[i].id);
                }

            }
            if(pids.length == 1){
                if(pids[0] == ids[0]){
                    nodes = zTree.getNodesByParam("pId", pids[0], null);
                    for (var i = 0; i < nodes.length; i++){
                        pids.push(nodes[i].id);
                    }
                } else {
                    for (var i = 0; i < ids.length; i++){
                        pids.push(ids[i]);
                    }
                }
            } else if(pids.length == 0) {
                pids = ids;
            }
            return [ids, pids];
        },

		/*清空所有zTree选中的checkbox节点*/
        clearTreesChecked :  function(treeId,value) {
            myTreeUtilsPq.getZTreeObj(treeId).checkAllNodes(util.isEmptyDefaultVal(value,false));
        },

		/*清空所有zTree背景为选中状态的节点*/
        clearTreesSelected : function (treeId) {
            var treeObj = myTreeUtilsPq.getZTreeObj(treeId);
            var nodes = treeObj.getNodes();
            treeObj.selectNode(nodes, false);
        },

        /**
		 *选中指定索引
         * @param treeId
         * @param selectIndex 选中选引 从1开始
         * @returns {*}
         */
        setSelectFirstNode : function (treeId,selectIndex) {
            try {
                var zTree = myTreeUtilsPq.getZTreeObj(treeId);
                var nodes = zTree.getNodes();
                var nodesLength = nodes.length;

                if(selectIndex > nodesLength) {
                    selectIndex = 1;
                }

                if(nodes.length > 0) {
                    selectIndex -= 1;
                    zTree.selectNode(nodes[selectIndex]);
                    return nodes[selectIndex];
                }
                return null;
            } catch(e) {
                console.info("没有找到id为："+inputId+"的zTree！");
                console.info(e.message);
            }
        }
    }

window["ztreeUtilsPQ"] = new ZTreeUtilsPQ();

})(jQuery,window,document);
