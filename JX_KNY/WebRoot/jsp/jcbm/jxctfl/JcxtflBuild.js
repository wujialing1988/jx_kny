//机车系统分类维护
Ext.onReady(function(){
     Ext.namespace('JcxtflBuild');  
     
     JcxtflBuild.shortName = "";
	 JcxtflBuild.path;			// 要添加到目标的机车构型节点路径
	 
	// 重新加载树
	JcxtflBuild.reloadTree = function(path) {
        JcxtflBuild.tree.root.reload(function() {
        	if (!path) {
				JcxtflBuild.tree.getSelectionModel().select(JcxtflBuild.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			JcxtflBuild.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        JcxtflBuild.tree.expandPath(path);
	        JcxtflBuild.tree.selectPath(path);
        }
	}
     
	// 机车系统分类树型列表
	JcxtflBuild.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jcxtflBuild!tree.action"
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: '',
	        id: "1",
	        leaf: false,
	        expanded :true
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	collapsed : false,
    	listeners : {
			 beforeload: function(node, e){	        	
            	JcxtflBuild.tree.getLoader().dataUrl = ctx + '/jcxtflBuild!tree.action?parentIDX=' + node.id + '&shortName=' + JcxtflBuild.shortName;
	        },
    		click: function(node, e) {
//    			JcxtflBuild.initFn(node)
	        }
		}
	});
	JcxtflBuild.tree.getSelectionModel().on('selectionchange', function( me, node ){
		JcxtflBuild.initFn(node)
	})
	
	//选择一个流程节点时的初始化机车构型Grid
	JcxtflBuild.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
		// 获取当前节点的路径信息
		JcxtflBuild.nodeIDX = node.id;
		JcxtflBuild.grid.store.load();
		
	}
	
	function isNull(data){
		return (data =="" || data == undefined || data == null) ? true : false;
	}
	
	/** 添加已选择的机车构型到指定节点 */
	JcxtflBuild.buildJcgx = function(data, type) {
		if (isNull(JcgxBuild.shortName) || isNull(JcgxBuild.nodeIDX)) {
			MyExt.Msg.alert("请选择需要构型的节点");
			return;
		}
		var dataAry = new Array();
		for (var i = 0; i < data.length; i++) {
			dataAry.push(data[i].data);
		}
		if (dataAry.length <= 0) {
			MyExt.Msg.alert("请选择一条记录");
			return;
		}
		var tip = '是否确认添加已选择多个分类到：<span style="color:green;font-weight:bold;">' + JcxtflBuild.path + '&nbsp;</span>？';
		if (1 === dataAry.length) {
			tip = '是否确认添加&nbsp;<b>' + dataAry[0].flmc + '</b>&nbsp;到：<span style="color:green;font-weight:bold;">' + JcxtflBuild.path + '&nbsp;</span>？'
		}
		Ext.Msg.confirm('提示', tip, function(btn) {
			if (btn != 'yes') return;
			if (self.loadMask) self.loadMask.show();
			Ext.Ajax.request({
				url : ctx + "/jcgxBuild!insertJcgx.action",
				jsonData : dataAry,
				params : {
					type : type,
					shortName : JcgxBuild.shortName,
					nodeIDX : JcgxBuild.nodeIDX
				},
				success : function(response, options) {
	        		if (self.loadMask) self.loadMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.success == true) {
						JcgxBuild.changed = true;
						//JcxtflBuild.win.hide();
						// 重新刷新树结构
						JcxtflBuild.tree.getSelectionModel().clearSelections();
						JcgxBuild.reloadTree(JcgxBuild.treePath);
						JcgxBuild.grid.store.reload();
						alertSuccess("机车构型添加成功！");						
					} else {
						alertFail(result.errMsg);
					}
				},
				failure : function(response, options) {
	        		if (self.loadMask) self.loadMask.hide();
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		});
	}
	
	
    //定义下节系统分类表格
	JcxtflBuild.grid = new Ext.yunda.Grid({
		loadURL: ctx + "/jcxtflBuild!pageQuery.action",
	    storeAutoLoad:false,
	    storeId:'coID',  
		tbar:[{
				text:"确定当前分类", iconCls:"editIcon",
				handler:function(){
				  	var data = JcxtflBuild.grid.selModel.getSelections();
				  	JcxtflBuild.buildJcgx(data, "single")
				}
			},{
				text:"确定当前分类及其下级分类", iconCls:"editIcon",
				handler:function(){
				 	var data = JcxtflBuild.grid.selModel.getSelections();
				  	JcxtflBuild.buildJcgx(data, "all")
			}
		}],
	    fields: [{
	    	header:'主键', dataIndex:'coID',hidden:true
	    },{
			header:'父类节点', dataIndex:'fjdID',hidden:true
		},{
			header:'分类编码', dataIndex:'flbm'
		},{
			header:'分类名称', dataIndex:'flmc'
		},{
			header:'零部件名称', dataIndex:'lbjbm'
		},{
			header:'适用车型', dataIndex:'sycx',width:200
		},{
			header:'是否有子节点', dataIndex:'coHaschild',hidden:true
		}],
		toEditFn: function(){}
    });
 	JcxtflBuild.grid.store.on('beforeload', function() {
		var whereList = [];
		whereList.push({
			propName : 'fjdID',
			propValue : JcxtflBuild.nodeIDX,
			compare : Condition.EQ,
			stringLike : false
		});
		var sql = "'/' || sycx || '/' like '%/" + JcxtflBuild.shortName + "/%'";
		whereList.push({
			compare : Condition.SQL,
			sql: sql
		});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "flmc";
		this.baseParams.dir = "ASC";
	});
	
	// 定义机车系统分类护窗口
	JcxtflBuild.win = new Ext.Window({
		title : "系统分类",
		width : 1000, height : 600,
		closeAction : 'hide',
		modal : true,
		layout : "border",
		defaults : {
			layout : 'fit', /*border : false, */region : "west"
		},
		items : [{
			title : '<span style="font-weight:normal">机车系统分类</span>',
			iconCls : 'icon-expand-all',
			collapsible : true,
			width : 279,
			items : JcxtflBuild.tree
		}, {
			region : 'center',
			items : JcxtflBuild.grid
		}],
		
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}],
		
		listeners: {
			show: function() {
				JcgxBuild.changed = false;
				this.setTitle(JcxtflBuild.path);
				JcxtflBuild.tree.root.setText(JcgxBuild.shortName);
				JcxtflBuild.reloadTree();
			},
			hide: function() {
				JcxtflBuild.tree.getSelectionModel().clearSelections();
				if (JcgxBuild.changed) {
					// 重新加载上级页面的树组件
					JcgxBuild.reloadTree(JcgxBuild.treePath);
					JcgxBuild.grid.store.reload();
				}
				
			}
		}
	});
	
});