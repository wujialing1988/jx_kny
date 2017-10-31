/**
 * 机车信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JczlTrainServiceWin');                       //定义命名空间
	JczlTrainServiceWin.searchParam = {} ;
	JczlTrainServiceWin.marshallingCode = '';
	
	JczlTrainServiceWin.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + 'jczlTrain!findVehicleKindTree.action'
		}),
		root : new Ext.tree.AsyncTreeNode({
			text : "车种",
			id : '',
			leaf : false
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
		listeners : {
			render : function() {		        	
    			this.root.reload();
			    this.getRootNode().expand();
    		},
			beforeload: function(node){
				this.loader.dataUrl = ctx + '/jczlTrain!findVehicleKindTree.action?vehicleType=20';
			},
    		click: function(node, e) {
    			if(node.leaf){
    				JczlTrainServiceWin.searchParam.vehicleKindCode = node.id;
				}else {
					JczlTrainServiceWin.searchParam.vehicleKindCode = '';
				}
				JczlTrainServiceWin.grid.store.reload();
    		}
		}		
	});	
	
	JczlTrainServiceWin.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jczlTrain!findjczlTrainList.action',                 //装载列表数据的请求URL
	     singleSelect: true,
	     tbar: ['search',{
	    	text : "刷新", iconCls : "refreshIcon",
			handler : function(){
				JczlTrainServiceWin.grid.store.reload();
			}}], 
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'车型主键', dataIndex:'trainTypeIDX', hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', width:40	
		},{
			header:'车号', dataIndex:'trainNo', width:40
		},{
			header:'车种编号', dataIndex:'vehicleKindCode' , hidden:true			
		},{
			header:'车种', dataIndex:'vehicleKindName' 	, width:40, searcher:{disabled: true}		
		},{
			header:'使用别ID', dataIndex:'trainUse', hidden: true
		},{
			header:'使用类别', dataIndex:'trainUseName', hidden: true,  editor:{xtype:'hidden' }
		},{
			header:'制造厂家主键', dataIndex:'makeFactoryIDX',hidden: true
		},{
			header:'制造厂家', dataIndex:'makeFactoryName', searcher:{disabled: true}
		},{
			header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', 
			editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
			searcher:{disabled: true}
		},{
			header:'配属局ID', dataIndex:'bId', hidden: true, searcher:{disabled: true}
		},{
			header:'配属段ID', dataIndex:'dId', hidden: true , searcher:{disabled: true}
		},{
			header:'配属局', dataIndex:'bName', hidden: true,
			editor:{xtype: "hidden"},
			searcher:{disabled: true}
		},{
			header:'配属段', dataIndex:'dName', hidden: true,
			editor:{xtype: "hidden"},
			searcher:{disabled: true}
		},
		//需求新增维护字段 
		//配属日期、改配日期、改配单位、命令号、状态、报废日期、报废原因
		{
			header:'配属日期', dataIndex:'attachmentTime', xtype:'datecolumn',  hidden: true,
			editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
			searcher:{disabled: true}
		},{
			header:'命令号', dataIndex:'orderNumber',hidden: true, editor:{maxLength:20,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:'类型', dataIndex:'vehicleType',  hidden: true, 
			searcher: {disabled: true}
		},{
			header:'登记人', dataIndex:'registerPersonName', hidden: true,
			editor:{xtype: "hidden"},searcher:{disabled: true}
		},{
			header:'登记时间', dataIndex:'registerTime', xtype:'datecolumn', hidden: true,
			editor:{xtype: "hidden"},searcher:{disabled: true}
		},{
			header:'备注', dataIndex:'remarks',
			searcher:{ disabled:true }
		},{
			header:'编组编号', dataIndex:'marshallingCode', hidden: true, value:JczlTrainServiceWin.marshallingCode,
			searcher:{ disabled:true }
		}],
		 toEditFn: Ext.emptyFn // 覆盖双击编辑事件
	});
	//查询前添加过滤条件
	JczlTrainServiceWin.grid.store.on('beforeload' , function(){
		var searchParam = JczlTrainServiceWin.searchParam;
		searchParam.vehicleType = 20;
//		searchParam.vehicleKindCode =  JczlTrainServiceWin.vehicleKindCode;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});

	//质量记录单查询结果窗口
	JczlTrainServiceWin.addWin = new Ext.Window({
		title: "车辆信息", maximizable:false, layout: "border", 
		closeAction: "hide", modal: true, maximized: false ,height: 510, width:800, buttonAlign:"center",
        bodyStyle:'padding-left:10px; padding-top:10px;',collapsible:true,
        bodyBorder: false,
        items:[{region: "west",
			width: 200,
			layout: "fit",
			items: JczlTrainServiceWin.tree
		},{
			region: "center",
			layout: "fit",items:JczlTrainServiceWin.grid
		}],
		listeners: {
			beforeshow: function() {	
				var sm = marshalling.grid.getSelectionModel();
				 if (sm.getCount()<=0) {
					MyExt.Msg.alert("请选择一条编组信息！");
					return false;
				}else if (sm.getCount() > 0) {
					var records = sm.getSelections();
					JczlTrainServiceWin.marshallingCode = records[0].data.marshallingCode;
				}		
				JczlTrainServiceWin.grid.store.load(); 
			}
		},
		buttons:[{
			text: "添加", iconCls: "addIcon", handler: function(){JczlTrainServiceWin.submit();}
		},{	text: "关闭", iconCls: "closeIcon", handler: function(){JczlTrainServiceWin.addWin.hide();}
		}]
	});
	//  确认提交方法，后面可覆盖此方法完成查询
	JczlTrainServiceWin.submit = function(){alert("请覆盖，JczlTrainServiceWin.submit 方法完成自己操作业务！");}
});