/**
 * 
 *添加配件
 * 
 */

Ext.onReady(function() {
	Ext.namespace('JcpjzdBuildWin');
	/** ************ 定义全局变量开始 ************ */
	JcpjzdBuildWin.fieldWidth = 70;
	//查询参数对象
	JcpjzdBuildWin.searchParam = {};		
	 //当前点击的树节点id
	JcpjzdBuildWin.parentIDX = "ROOT_0";
	 //当前点击的树节点id
	JcpjzdBuildWin.jcpjbm  = "";
	JcpjzdBuildWin.jcpjmc  = "";
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
   // 重新加载
	JcpjzdBuildWin.reloadTree = function(path) {
        JcpjzdBuildWin.tree.root.reload(function() {
        	if (!path) {
				JcpjzdBuildWin.tree.getSelectionModel().select(JcpjzdBuildWin.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			JcpjzdBuildWin.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        JcpjzdBuildWin.tree.expandPath(path);
	        JcpjzdBuildWin.tree.selectPath(path);
        }
	}
	
	// 选择一个节点时的初始化函数处理
	JcpjzdBuildWin.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
    	// 获取当前节点的路径信息
    	JcpjzdBuildWin.treePath = node.getPath();	
    	JcpjzdBuildWin.parentIDX = node.id;
		// 重新加载作业节点表格数据
//    	JcpjzdBuildWin.grid.store.load();
    	
    }
	//   添加明细
	JcpjzdBuildWin.insertRow = function(addnumber){
		for(var j=0; j<addnumber; j++){
    		var partsId = JcpjzdBuildWin.jcpjbm;
			var partsName = JcpjzdBuildWin.jcpjmc;
			var record_v  = new Ext.data.Record();
			record_v.set("partsId",partsId);
			record_v.set("partsName",partsName);
			record_v.set("wzmc","");
			record_v.set("wzdm","");
	        JcpjzdBuildWin.addGrid.store.insert(0, record_v); 
	        JcpjzdBuildWin.addGrid.getView().refresh(); 
	        JcpjzdBuildWin.addGrid.getSelectionModel().selectRow(0);
		}
	}
//	JcpjzdBuildWin.insertRow = function(addnumber){
//	    JcpjzdBuildWin.addGrid.store.removeAll();
//	    var datas =  JcpjzdBuildWin.grid.getSelectionModel().getSelections();
//		for(var j=0; j<addnumber; j++){
//			  for (var i = 0; i < datas.length; i++){
//		        	var data_v = datas[ i ].data ;
//	        		var partsId = data_v.jcpjbm;
//					var partsName = data_v.jcpjmc;
//					var record_v  = new Ext.data.Record();
//					record_v.set("partsId",partsId);
//					record_v.set("partsName",partsName);
//			        JcpjzdBuildWin.addGrid.store.insert(0, record_v); 
//			        JcpjzdBuildWin.addGrid.getView().refresh(); 
//			        JcpjzdBuildWin.addGrid.getSelectionModel().selectRow(0);
//			  }
//		}
//	}
	// 删除明细
	JcpjzdBuildWin.deleteFn = function(){
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
//	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        var data = JcpjzdBuildWin.addGrid.selModel.getSelections();
	        if(data.length == 0 ){
	        	MyExt.Msg.alert("尚未选择一条记录！");
	            return;
	        }
	        var storeAt = JcpjzdBuildWin.store.indexOf(data[0]);
	        var records = JcpjzdBuildWin.store.getModifiedRecords();
	        var count = records.length; 
	        var j = storeAt + 1;
	        if(count-1 == storeAt){
	        	j = storeAt-1;
	        }
		    JcpjzdBuildWin.addGrid.getSelectionModel().selectRow(j);
		    for (var i = 0; i < data.length; i++){
		        JcpjzdBuildWin.addGrid.store.remove(data[i]);
	    }
	}

	/** ************ 定义全局函数结束 ************ */

	//零部件名称树 
	JcpjzdBuildWin.tree = new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
		loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jcpjzdBuild!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	        text: '零部件名称',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,    
	    autoScroll : true,
	    autoShow : true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	   			JcpjzdBuildWin.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		JcpjzdBuildWin.reloadTree();
	    	},
	        click: function(node, e) {
	        	if(node.leaf){
	        		JcpjzdBuildWin.jcpjbm = node.id;
	        		JcpjzdBuildWin.jcpjmc = node.attributes.jcpjmc;
	        		// 点击添加明细
					JcpjzdBuildWin.insertRow(1);	
	        	}else{
        			JcpjzdBuildWin.jcpjbm = "";
	        		JcpjzdBuildWin.jcpjmc = "";
	        	}
	        	
	        },
	    	beforeload:  function(node){
			    JcpjzdBuildWin.tree.loader.dataUrl = ctx + '/jcpjzdBuild!tree.action?fjdId=' + node.id;
			}
	    }, enableDD:true    
	});
	
	JcpjzdBuildWin.tree.getSelectionModel().on('selectionchange', function( me, node ){
		JcpjzdBuildWin.initFn(node)
	})
	
//	// 配件列表 
//	JcpjzdBuildWin.grid = new Ext.yunda.Grid({
//		region : 'center',
//		viewConfig:null, width:400,
//		loadURL : ctx + "/jcpjzdBuild!pageList.action",
//		tbar: ['增加数量：',
//	    	 {xtype:'textfield',id:"addnumber", name:"addnumber",maxLength:2 ,vtype: "positiveInt",
//	    	 	 allowBlank:false,  width:JcpjzdBuildWin.fieldWidth },
//	    	 	'&nbsp;<span id="spUnit"><span>&nbsp;',
//	    	{text:'添加明细', iconCls:'addIcon', handler:function(){
//				//表单验证是否通过
//		         var sm = JcpjzdBuildWin.grid.getSelectionModel();
//		        if (sm.getCount() < 1) {
//		            MyExt.Msg.alert("尚未选择一条记录！");
//		            return;
//		        }
//				var addnumber = Ext.getCmp("addnumber").getValue();
//				if(addnumber==""){
//				   MyExt.Msg.alert("请输入添加数量");
//				   return ;
//				}
//				JcpjzdBuildWin.insertRow(addnumber);
//	    	}
//    	},'-','search'],
//		fields : [{
//			header : '编码', dataIndex : 'jcpjbm',  width:100
//		}, {
//			header : '标准名称', dataIndex : 'jcpjmc',  width:170
//		}]
//		
//	});
//	JcpjzdBuildWin.grid.un('rowdblclick', JcpjzdBuildWin.grid.toEditFn, JcpjzdBuildWin.grid); //取消编辑监听
	
		// 数据容器
	JcpjzdBuildWin.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:false, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/jobProcessPartsOffList!pageList.action',
		fields:['idx','wzmc','wzdm','partsName','partsId']
	});
	// 添加配件明细 
	JcpjzdBuildWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	//材料规格型号，选择模式，勾选框可多选
	JcpjzdBuildWin.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	JcpjzdBuildWin.addGrid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, selModel: JcpjzdBuildWin.sm, loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    store: JcpjzdBuildWin.store,
	    colModel: new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),
	        { sortable:false, header:'idx', dataIndex:'idx', hidden: true},
	        { sortable:false, header : '编码', dataIndex : 'partsId', hidden: true, width:100},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName', width:100},
         	{ sortable:false, header: "存放位置",  dataIndex: "wzmc", maxLength:100,
	    		editor:{	
	    			id: "wzmc_id_d",
					xtype: "Base_combo",
					fieldLabel: "位置",
					hiddenName: "wzmc",
					returnField: [{ widgetId: "wzdm_id_d", propertyName: "partId" }],
					displayField: "partName", valueField: "partName",
					entity: "com.yunda.jx.component.entity.EquipPart",
					fields: ["partId", "partName"],
					pageSize: 20,
					minListWidth: 200,
					editable: false}
			}, {
				header: '位置名称编码', dataIndex: 'wzdm', hidden: true,
				editor: { id: "wzdm_id_d", xtype:'hidden' }    
	    	},{ header:'partsId', dataIndex:'partsId',hidden: true}
	    ]),
		tbar:[
			'增加数量：',
	    	 {xtype:'textfield',id:"addnumber", name:"addnumber",maxLength:2 ,vtype: "positiveInt",
	    	 	 allowBlank:false,  width:JcpjzdBuildWin.fieldWidth },
	    	 	'&nbsp;<span id="spUnit"><span>&nbsp;',
	    	{text:'添加明细', iconCls:'addIcon', handler:function(){
		        if (Ext.isEmpty(JcpjzdBuildWin.jcpjbm)) {
		            MyExt.Msg.alert("尚未选中一条机车零部件记录！");
		            return;
		        }
				var addnumber = Ext.getCmp("addnumber").getValue();
				if(addnumber==""){
				   MyExt.Msg.alert("请输入添加数量");
				   return ;
				}
				JcpjzdBuildWin.insertRow(addnumber);
	    	}
    	},'->',{
			text:'确认添加',iconCls:'saveIcon',handler:function(){JcpjzdBuildWin.submit();}
		}, '&nbsp;&nbsp;',{
			text:'删除明细', iconCls:'deleteIcon', handler:JcpjzdBuildWin.deleteFn
	    }],
		listeners: {
			afteredit:function(e){
            	var wzdm = Ext.getCmp("wzdm_id_d").getValue();
            	if(e.field == "wzmc"){
            		e.record.set('wzdm', wzdm);
            		JcpjzdBuildWin.addGrid.getView().refresh();
            	}
			}
		}
	});
		//设置查询条件	
	JcpjzdBuildWin.store.on("beforeload", function(){
		var beforeloadParam = {processIDX: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	
	//新增编辑窗口
	JcpjzdBuildWin.win = new Ext.Window({
		title: "机车零部件",  width:850, height:400,  layout: "border", closeAction: "hide",
		items: [{
			region: "west",
			width: 250,
			collapsible: true,
			iconCls: 'icon-expand-all',
			title: '<span style="font-weight:normal">机车零部件树<span style="color:gray;"></span></span>',	
			tools: [{
				id: 'refresh', handler: function() {
					  JcpjzdBuildWin.tree.root.reload();
                      JcpjzdBuildWin.tree.getRootNode().expand();
				}
			}],
			layout: "fit",
			items: [JcpjzdBuildWin.tree]
		}, {
			layout: "fit",
			region: "center",
		    items: [JcpjzdBuildWin.addGrid]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				JcpjzdBuildWin.tree.getSelectionModel().clearSelections();
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			show: function(window) {
	            JcpjzdBuildWin.addGrid.store.removeAll();  // 新增时清空列表数据
				JcpjzdBuildWin.reloadTree();	
			}
		}
	});

//	//store载入前查询
//	JcpjzdBuildWin.grid.store.on("beforeload", function(){
//		var idx = JcpjzdBuildWin.parentIDX ;
//		var searchParam = {};
//		searchParam.fjdId = idx;
//		if("ROOT_0"==idx||""==idx ){
//	    	searchParam = {};
//	    }
//		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
//	});

	//确认提交方法，后面可覆盖此方法完成查询
	JcpjzdBuildWin.submit = function(){alert("请覆盖，TechReformProject.submit 方法完成自己操作业务！");};

});