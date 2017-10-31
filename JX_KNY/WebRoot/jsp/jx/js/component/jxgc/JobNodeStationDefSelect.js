//工位选择控件，目前供机车作业计划使用
Ext.ns('jx.jxgc.JobNodeStationDefSelect');
jx.jxgc.JobNodeStationDefSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    //初始化容器
    initComponent: function(config) {
        jx.jxgc.JobNodeStationDefSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        jx.jxgc.JobNodeStationDefSelect.returnFn = this.returnFn;        
    },
    //点击控件触发事件
    onTriggerClick: function() {
        if(this.disabled)   return;
        if(jx.jxgc.JobNodeStationDefSelect.win == null){
        	jx.jxgc.JobNodeStationDefSelect.createWin();
        }   
        jx.jxgc.JobNodeStationDefSelect.nodeIDX = this.nodeIDX;
        jx.jxgc.JobNodeStationDefSelect.grid.store.load();
        jx.jxgc.JobNodeStationDefSelect.win.show();
        
    },
    //返回值方法
    returnFn:function(stationObject){    //选择确定后触发函数，用于处理返回记录
    	
    },
    nodeIDX:''
    
});

/***************定义全部变量--开始****************/
//定义弹出win和grid
jx.jxgc.JobNodeStationDefSelect.win = null;
jx.jxgc.JobNodeStationDefSelect.grid = null;

//定义其他工位选择win和grid
jx.jxgc.JobNodeStationDefSelect.otherWin == null;
jx.jxgc.JobNodeStationDefSelect.tree = null;

//定义查询变量
jx.jxgc.JobNodeStationDefSelect.nodeIDX = '';
jx.jxgc.JobNodeStationDefSelect.searchParams = {};

//定义工位主键
jx.jxgc.JobNodeStationDefSelect.gridIdx = '';

jx.jxgc.JobNodeStationDefSelect.selectedIds = [];

/***************定义全部变量--结束****************/

//创建弹出窗口
jx.jxgc.JobNodeStationDefSelect.createWin = function(){
	//创建选择工位grid
    if(jx.jxgc.JobNodeStationDefSelect.grid == null) {
       jx.jxgc.JobNodeStationDefSelect.createGrid();
    }
    //创建主界面窗口
    if(jx.jxgc.JobNodeStationDefSelect.win == null){
      jx.jxgc.JobNodeStationDefSelect.win = new Ext.Window({
		 title:"工位选择", width:550, height:360, closeAction:"hide", modal:true, layout:"fit", buttonAlign:"center",
		 items:[jx.jxgc.JobNodeStationDefSelect.grid],
		 buttons:[{
			 text: "确定", iconCls:"saveIcon",
			 handler:function(){
				 jx.jxgc.JobNodeStationDefSelect.submit();
	    	  }
		 },{
			 text: "关闭", iconCls:"closeIcon",
			 handler:function(){
				 jx.jxgc.JobNodeStationDefSelect.win.hide();
			 }
		 }]
	  });
    }
}

//创建主表格
jx.jxgc.JobNodeStationDefSelect.createGrid = function(){
	if(jx.jxgc.JobNodeStationDefSelect.grid)  return;
		jx.jxgc.JobNodeStationDefSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workStation!findSelectPageList.action',                 //装载列表数据的请求URL
	    storeAutoLoad: false,
	    singleSelect: true,    
	    tbar:[{
			xtype:"combo", hiddenName:"queryType", displayField:"type",
	        width: 80, valueField:"type", value:"工位编码", mode:"local",triggerAction: "all",
			store: new Ext.data.SimpleStore({
				fields: ["type"],
				data: [ ["工位编码"], ["工位名称"]]
			})
		},{	            
	        xtype:"textfield",  width: 100
		},{
			text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
			handler : function(){
				var grid = jx.jxgc.JobNodeStationDefSelect.grid;
				var typeName = grid.getTopToolbar().get(1).getValue();
				var querytype = grid.getTopToolbar().get(0).getValue();
				if(querytype == '工位编码'){
					delete jx.jxgc.JobNodeStationDefSelect.searchParams["workStationName"];
					jx.jxgc.JobNodeStationDefSelect.searchParams.workStationCode = typeName;
				}else if(querytype == '工位名称'){
					delete jx.jxgc.JobNodeStationDefSelect.searchParams["workStationCode"];
					jx.jxgc.JobNodeStationDefSelect.searchParams.workStationName = typeName;
				}
				jx.jxgc.JobNodeStationDefSelect.grid.getStore().load();
			}
		},{
			text : "重置",iconCls : "resetIcon",
			handler : function(){
				//清空搜索输入框
				var grid = jx.jxgc.JobNodeStationDefSelect.grid;
				var stationName = grid.getTopToolbar().get(1);
				stationName.setValue("");
				jx.jxgc.JobNodeStationDefSelect.searchParams.workStationName = "";
				jx.jxgc.JobNodeStationDefSelect.searchParams.workStationCode = "";
				jx.jxgc.JobNodeStationDefSelect.grid.getStore().load();
			}
		},{
	        text:"选择其他工位",iconCls:"addIcon",handler: function(){
	        		//创建其他工位选择表格
				    if(jx.jxgc.JobNodeStationDefSelect.tree == null) {
				       jx.jxgc.JobNodeStationDefSelect.createStationOtherTree();
				    }
				  	//创建其他工位选择窗口
				    if(jx.jxgc.JobNodeStationDefSelect.otherWin == null) {
				       jx.jxgc.JobNodeStationDefSelect.createStationOtherWin();
				    }
				    
				    // 获取已选择的工位主键，用于过滤工位树
		        	var store = jx.jxgc.JobNodeStationDefSelect.grid.store;
		        	var selectedIds = [];
		        	store.each(function(record){
		        		this.push(record.get('repairLineIdx'));
		        	}, selectedIds);
		        	jx.jxgc.JobNodeStationDefSelect.selectedIds = selectedIds;				    
		        	jx.jxgc.JobNodeStationDefSelect.otherWin.show();
		        	
	        }        
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工位编码', dataIndex:'workStationCode', width: 100, editor:{  }
		},{
			header:'工位名称', dataIndex:'workStationName', width: 100, editor:{ }
		},{
			header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor:{ }
		},{
			header:'流水线', dataIndex:'repairLineName', width: 100, editor:{ xtype:'hidden', maxLength:100 }
		},{
			header:'工作班组', dataIndex:'teamOrgName', width: 100, editor:{ }
		},{
			header:'工作班组ID', dataIndex:'teamOrgId', hidden:true, editor:{ }
		},{
			header:'工作班组序列', dataIndex:'teamOrgSeq', hidden:true, editor:{ }
		},{
			header:'所属台位编码', dataIndex:'deskCode', hidden:true,editor:{ xtype:'hidden'}
		},{
			header:'所属台位', dataIndex:'deskName', hidden:true,width: 200, editor:{ xtype:'hidden'}
		}],
		listeners: {
			render: function(grid) {
				if (Ext.isEmpty(jx.jxgc.JobNodeStationDefSelect.nodeIDX)) {
					grid.getTopToolbar().get(4).hide();
				}else{
					grid.getTopToolbar().get(4).show();
				}
			}
		}
	});
	//移除事件
	jx.jxgc.JobNodeStationDefSelect.grid.un('rowdblclick',jx.jxgc.JobNodeStationDefSelect.grid.toEditFn,jx.jxgc.JobNodeStationDefSelect.grid);
	
	//工位表格加载前方法
	jx.jxgc.JobNodeStationDefSelect.grid.store.on("beforeload", function(){
		var searchParam = jx.jxgc.JobNodeStationDefSelect.searchParams;
		searchParam.nodeIDX = jx.jxgc.JobNodeStationDefSelect.nodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
}
// 递归获取勾选的树节点
	jx.jxgc.JobNodeStationDefSelect.getHasChkedNode = function(node, array) {
		if(typeof(node) == 'undefined') return false;
		if(node.attributes.checked == true && node.disabled != true) {
			array.push(node.id);
		}
		var childs = node.childNodes; //找到该节点的所有子节点
		for(var i = 0; i < childs.length; i++){
			arguments.callee(childs[i], array);
		}
	}
  
//创建其他工位选择窗口
jx.jxgc.JobNodeStationDefSelect.createStationOtherWin = function(){
   if(jx.jxgc.JobNodeStationDefSelect.otherWin)  return;
   jx.jxgc.JobNodeStationDefSelect.otherWin = new Ext.Window({
   		title:"选择工位",
		width: 300,
		height: 600,
		modal: true,
		layout: 'fit',
		closeAction: 'hide',
		items:[jx.jxgc.JobNodeStationDefSelect.tree],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: function() {
				var ids = [];
				jx.jxgc.JobNodeStationDefSelect.getHasChkedNode(jx.jxgc.JobNodeStationDefSelect.tree.getRootNode(), ids);
				if (Ext.isEmpty(ids)) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				
				var datas = [];
				
				for (var i = 0; i < ids.length; i++) {
					var jobNodeProjectDef = {};
					jobNodeProjectDef.nodeIDX = jx.jxgc.JobNodeStationDefSelect.nodeIDX;
					jobNodeProjectDef.workStationIDX = ids[i];
					datas.push(jobNodeProjectDef);
				}
		    	// Ajax后台数据处理
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
		            url: ctx + '/jobNodeStationDef!save.action',
		            jsonData: datas,
		            success: function(response, options){
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                	alertSuccess();
		                	jx.jxgc.JobNodeStationDefSelect.grid.store.reload();
		                	// 一个作业节点只能关联一条流水线上的一个工位，因为添加了一个工位后，从树上移除已经添加了的工位的流水线
		                	for (var i = 0; i < ids.length; i++) {
		                		var node = jx.jxgc.JobNodeStationDefSelect.tree.getNodeById(ids[i]);
		                		var repairLineIdx = node.attributes.repairLineIdx;
		                		// 移除流水线
		                		jx.jxgc.JobNodeStationDefSelect.tree.getRootNode().removeChild(jx.jxgc.JobNodeStationDefSelect.tree.getNodeById(repairLineIdx));
		                	}
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        }));
				
			}
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				jx.jxgc.JobNodeStationDefSelect.otherWin.hide();
			}
		}], 
		listeners: {
			show: function(window){
				// 加载流水线树
				jx.jxgc.JobNodeStationDefSelect.tree.getRootNode().reload();
				jx.jxgc.JobNodeStationDefSelect.tree.getRootNode().expand();
			},
			hide: function(){
			}
		}
	});	
}

//创建其他工位选择表格
jx.jxgc.JobNodeStationDefSelect.createStationOtherTree = function(){
 	if(jx.jxgc.JobNodeStationDefSelect.tree)  return;
 	jx.jxgc.JobNodeStationDefSelect.tree = new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/repairLine!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '流水线',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    	},
	        beforeload:  function(node){
			    jx.jxgc.JobNodeStationDefSelect.tree.loader.dataUrl = ctx + '/repairLine!tree.action?parentIDX=' + node.id;
			},
			load: function(node) {
				if (node.getDepth() != 0) {
					return;
				}
				// 过滤树节点，移除已经添加到工位组的工位
				if (!Ext.isEmpty(jx.jxgc.JobNodeStationDefSelect.selectedIds)) {
					for (var i in jx.jxgc.JobNodeStationDefSelect.selectedIds) {
						var childNodes = node.childNodes;
						for (var j in childNodes) {
							if (jx.jxgc.JobNodeStationDefSelect.selectedIds[i] == childNodes[j].id) {
								node.removeChild(childNodes[j]);
							}
						}
					}
				}
			}
	    }    
	});
}


//定义点击确定按钮的操作
jx.jxgc.JobNodeStationDefSelect.submit = function(){
	if(!$yd.isSelectedRecord(jx.jxgc.JobNodeStationDefSelect.grid, true)){
		MyExt.Msg.alert("请选择工位!");		 
		return;
	 }
	var stationData = jx.jxgc.JobNodeStationDefSelect.grid.selModel.getSelections();
	var stationObject = {};
	stationObject.workStationIDX = stationData[0].get("idx");
	stationObject.workStationCode = stationData[0].get("workStationCode");
	stationObject.workStationName = stationData[0].get("workStationName");
	stationObject.repairLineIDX = stationData[0].get("repairLineIDX");
	stationObject.repairLineName = stationData[0].get("repairLineName");
	stationObject.teamOrgId = stationData[0].get("teamOrgId");;
	stationObject.teamOrgName = stationData[0].get("teamOrgName");;
	stationObject.teamOrgSeq = stationData[0].get("teamOrgSeq");;
	jx.jxgc.JobNodeStationDefSelect.returnFn(stationObject);
	jx.jxgc.JobNodeStationDefSelect.win.hide();
}

//注册为Ext容器组件
Ext.reg('JobNodeStationDefSelect', jx.jxgc.JobNodeStationDefSelect);