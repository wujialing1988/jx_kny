/**
 * 检修记录单列表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RQWorkCard');                       //定义命名空间
	
	//车型车号
	RQWorkCard.cxch = '';
	//修程修次
	RQWorkCard.xcxc = '';
	//开始时间
	RQWorkCard.planBeginTime = '';
	//结束时间
	RQWorkCard.planEndTime = '';
	RQWorkCard.rpdIdx = '';
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 打印预览
	 * @param args 机车检修记录单、与机车检修基础维护记录单主键
	 * 形如: 'E6A08C66526941119AE25135F66A1982'-'8a8284f250e9e6430150ea1ac3d00005'
	 */
	RQWorkCard.printFn = function(args) {
		var idx = args.split('-')[0];			// 检修记录单实例idx主键
		var recordIDX = args.split('-')[1];		// 记录单idx主键
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/printerModule!getModelForPreview.action',
			params:{
				businessIDX: recordIDX
			},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	var entity = result.entity
		        	var deployCatalog = entity.deployCatalog;		// 报表部署目录
					var displayName = entity.displayName;			// 报表显示名称
					var deployName = entity.deployName;				// 报表部署名称
					while(deployCatalog.indexOf('.') >= 0) {
						deployCatalog = deployCatalog.replace('.', '/');
					}
					var reportUrl = "/" + deployCatalog + "/" + deployName;
					
					var url = reportUrl + "?ctx=" + ctx.substring(1);
					var dataUrl = reportUrl + "&idx=" + idx;		// 机车检修记录单实例idx主键
                	window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI(displayName));
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}

	// 重新加载【作业节点树】
	RQWorkCard.reloadTree = function(path) {
        RQWorkCard.tree.root.reload(function() {
        	if (!path) {
				RQWorkCard.tree.getSelectionModel().select(RQWorkCard.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			RQWorkCard.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        RQWorkCard.tree.expandPath(path);
	        RQWorkCard.tree.selectPath(path);
        }
	}
	/** **************** 定义全局函数结束 **************** */	
	
	/** **************** 定义节点树开始 **************** */
	// 机车检修作业流程节点树型列表
	RQWorkCard.tree =  new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jobProcessNode!findFirstNodeTree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '作业流程节点',
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
	    		RQWorkCard.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		RQWorkCard.reloadTree();
	    	},
	        click: function(node, e) {
	        },
	        beforeload:  function(node){
			    RQWorkCard.tree.loader.dataUrl = ctx + '/jobProcessNode!findFirstNodeTree.action?workPlanIDX=' + RQWorkCard.rpdIdx;
			},
			load: function(node) {
			}		
	    },
	    enableDD:true
	});
	
	// 选中的树节点变化时的事件监听函数
	RQWorkCard.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
		if(!Ext.isEmpty(node) && !Ext.isEmpty(node.id)){
	    	RQWorkCard.nodeIDX = node.id;
		}
    	RQWorkCard.grid.store.load();
	});
	/** **************** 定义节点树结束 **************** */
	
	RQWorkCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workPlanRepairActivity!findWorkPlanRecordByNodeIDX.action',    // 装载列表数据的请求URL
	    storeAutoLoad:false,
	    searchFormColNum:2,
	    fieldWidth: 200,
	    singleSelect: true,
	    tbar:[ {
				xtype:'textfield', id:'txt_activityC_name_no', enableKeyEvents:true, emptyText:'输入编码或名称快速检索', listeners: {
		    		keyup: function(filed, e) {
						// 如果敲下Enter（回车键），则触发添加按钮的函数处理
						if (e.getKey() == e.ENTER){
							RQWorkCard.grid.store.load();
						}
		    		}
	    		}
			}, {
				text:'查询', iconCls:'searchIcon', handler: function(){	
					RQWorkCard.grid.store.load();
				}
			}, {
				text:'重置', iconCls:'resetIcon', handler: function(){
					Ext.getCmp('txt_activityC_name_no').reset();	
					RQWorkCard.grid.store.load();
				}
			}],
		fields: [{
			header:'打印', dataIndex:'idx', width: 40, renderer: function(value, metaData, record, rowIndex, colIndex, store) {	
				var repairProjectIDX = record.get('repairProjectIDX');
				var args = [value,repairProjectIDX].join('-');		
				return "<img src='" + printerImg + "' alt='打印' style='cursor:pointer' onclick='RQWorkCard.printFn(\"" + args + "\")'/>";
			},searcher:{  disabled:true}
		},{
			header:'机车检修记录单idx主键', dataIndex:'repairProjectIDX', hidden:true
		},{
			header:'检修记录单编码', dataIndex:'activityCode', editor:{  maxLength:50 }
		},{
	        header:'检修记录单名称', dataIndex:'activityName',width:200, editor:{  maxLength:50 }
    	},{ header:"填单时间", dataIndex:"recordDate",xtype: "datecolumn",format: "Y-m-d H:i",width:150
	    }/*,{
	        header:'完成情况', dataIndex:'completPercent',width:200, editor:{  maxLength:50 },searcher:{  disabled:true},
	        renderer :function(a,b,c,d){
				return Math.round(parseInt(a)) + "%";
			}
	    }*/],
		searchFn: function(searchParam){
			RQWorkCard.searchParams = searchParam ;
			RQWorkCard.grid.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
			// 当前记录单索引
	        TrainWorkCardNew.index = rowIndex;
	        TrainWorkCardNew.record = RQWorkCard.grid.store.getAt(rowIndex);
	        //车型车号
			TrainWorkCardNew.cxch = RQWorkCard.cxch;
			//修程修次
			TrainWorkCardNew.xcxc = RQWorkCard.xcxc;
			//开始时间
			TrainWorkCardNew.planBeginTime = RQWorkCard.planBeginTime;
			//结束时间
			TrainWorkCardNew.planEndTime = RQWorkCard.planEndTime;
	        
	        TrainWorkCardNew.win.show();
	    }
	});
	
	//加载前过滤
	RQWorkCard.grid.store.on('beforeload',function(){
		var searchParam = {};
		searchParam.nodeIdx = RQWorkCard.nodeIDX;	
		searchParam.rdpIDX = RQWorkCard.rpdIdx; //兑现单主键
		searchParam.workPlanStatus = RQWorkCard.workPlanStatus; //是否转历史的状态
		searchParam.activityCode = Ext.getCmp('txt_activityC_name_no').getValue();
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	RQWorkCard.tab = new Ext.Panel({
	    layout: "border",maximized:true,
    	items:[{
    		 region: 'west', title:"节点",layout: "fit", width: 279,minSize: 260, maxSize: 300,    collapsible : true,autoScroll: true, 
    		 split: true, bodyBorder: false, items:[RQWorkCard.tree]		
    	},{
    		  split: true, region: 'center',  title:"记录单",layout: "fit", bodyBorder: false, bodyStyle:'padding-left:-10px;',
    		 items:[RQWorkCard.grid]
    	}]
	});
	
});