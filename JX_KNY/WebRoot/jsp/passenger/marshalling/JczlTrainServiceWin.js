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
			text : i18n.MarshallingInfoMaintain.vehicleKindName,
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
	    	text : i18n.MarshallingInfoMaintain.refresh, iconCls : "refreshIcon",
			handler : function(){
				JczlTrainServiceWin.grid.store.reload();
			}}], 
		fields: [{
			header:i18n.MarshallingInfoMaintain.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.MarshallingInfoMaintain.trainTypeIdx, dataIndex:'trainTypeIDX', hidden:true
		},{
			header:i18n.MarshallingInfoMaintain.trainTypeName, dataIndex:'trainTypeShortName', width:40	
		},{
			header:i18n.MarshallingInfoMaintain.trainNumber, dataIndex:'trainNo', width:40
		},{
			header:i18n.MarshallingInfoMaintain.vehicleKindCode, dataIndex:'vehicleKindCode' , hidden:true			
		},{
			header:i18n.MarshallingInfoMaintain.vehicleKindName, dataIndex:'vehicleKindName' 	, width:40, searcher:{disabled: true}		
		},{
			header:i18n.MarshallingInfoMaintain.trainUseId, dataIndex:'trainUse', hidden: true
		},{
			header:i18n.MarshallingInfoMaintain.trainUseName, dataIndex:'trainUseName', hidden: true,  editor:{xtype:'hidden' }
		},{
			header:i18n.MarshallingInfoMaintain.makeFactoryIDX, dataIndex:'makeFactoryIDX',hidden: true
		},{
			header:i18n.MarshallingInfoMaintain.makeFactoryName, dataIndex:'makeFactoryName', searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.productionDate, dataIndex:'leaveDate', xtype:'datecolumn', 
			editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
			searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.bId, dataIndex:'bId', hidden: true, searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.dId, dataIndex:'dId', hidden: true , searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.attachBureauName, dataIndex:'bName', hidden: true,
			editor:{xtype: "hidden"},
			searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.attachPeriodName, dataIndex:'dName', hidden: true,
			editor:{xtype: "hidden"},
			searcher:{disabled: true}
		},
		//需求新增维护字段 
		//配属日期、改配日期、改配单位、命令号、状态、报废日期、报废原因
		{
			header:i18n.MarshallingInfoMaintain.attachmentTime, dataIndex:'attachmentTime', xtype:'datecolumn',  hidden: true,
			editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
			searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.orderNumber, dataIndex:'orderNumber',hidden: true, editor:{maxLength:20,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.vehicleType, dataIndex:'vehicleType',  hidden: true, 
			searcher: {disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.registerPersonName, dataIndex:'registerPersonName', hidden: true,
			editor:{xtype: "hidden"},searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.registerTime, dataIndex:'registerTime', xtype:'datecolumn', hidden: true,
			editor:{xtype: "hidden"},searcher:{disabled: true}
		},{
			header:i18n.MarshallingInfoMaintain.remarks, dataIndex:'remarks',
			searcher:{ disabled:true }
		},{
			header:i18n.MarshallingInfoMaintain.marshallingCode, dataIndex:'marshallingCode', hidden: true, value:JczlTrainServiceWin.marshallingCode,
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
		title: i18n.MarshallingInfoMaintain.trainInfo, maximizable:false, layout: "border", 
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
					MyExt.Msg.alert(i18n.MarshallingInfoMaintain.msg1);
					return false;
				}else if (sm.getCount() > 0) {
					var records = sm.getSelections();
					JczlTrainServiceWin.marshallingCode = records[0].data.marshallingCode;
				}
				JczlTrainServiceWin.tree.root.reload();
				JczlTrainServiceWin.grid.store.load(); 
			}
		},
		buttons:[{
			text: i18n.MarshallingInfoMaintain.add, iconCls: "addIcon", handler: function(){JczlTrainServiceWin.submit();}
		},{	text: i18n.MarshallingInfoMaintain.close, iconCls: "closeIcon", handler: function(){JczlTrainServiceWin.addWin.hide();}
		}]
	});
	//  确认提交方法，后面可覆盖此方法完成查询
	JczlTrainServiceWin.submit = function(){alert("请覆盖，JczlTrainServiceWin.submit 方法完成自己操作业务！");}
});