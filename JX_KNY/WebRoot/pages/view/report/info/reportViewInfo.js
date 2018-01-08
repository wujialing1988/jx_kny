Ext.onReady(function(){
	
	Ext.ns('ReportViewInfo');
	
	ReportViewInfo.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/reportView!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/reportView!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/reportView!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    labelWidth: 125,                                     //查询表单中的标签宽度
	    fieldWidth: 200,
	    tbar:['报表分类:','-',{
			id:'reportTypeName_search_combo',
			xtype: 'EosDictEntry_combo',
			dicttypeid:'REPORT_TYPE',
			displayField:'dictname',
			valueField:'dictid',
			hasEmpty:"false",
			listeners:{
				"select":function(){
					ReportViewInfo.grid.store.reload();
				}
			}
		},{
			text: "重置",
			iconCls: "resetIcon",
			handler: function(){
				Ext.getCmp("reportTypeName_search_combo").clearValue();
				ReportViewInfo.grid.store.reload();
			}
		}, "add", "delete", "refresh"],
		fields: [{
			header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
		},{
			header:'显示名称', dataIndex:'displayName',editor:{ allowBlank:false ,maxLength:50  }
		},{
			header:'报表路径', dataIndex:'reportUrl', editor:{allowBlank:false , maxLength:50  }
		},{
			header:'排序', dataIndex:'seqNo', editor:{
				xtype:'numberfield',
		        allowDecimals:false,
		        allowNegative:false,
		        minValue:1,
		        maxValue:99,
		        allowBlank:false }
		},{
			header:'报表分类', dataIndex:'reportTypeName',editor:{
					id:'reportTypeName_combo',
					xtype: 'EosDictEntry_combo',
					hiddenName: 'reportTypeName',
					dicttypeid:'REPORT_TYPE',
					displayField:'dictname',valueField:'dictname',
					allowBlank:false,
					hasEmpty:"false",
					returnField: [{widgetId:"reportType",propertyName:"dictid"}]
		        }, searcher: {anchor:'98%'}
		},{
			header:'报表分类编码', dataIndex:'reportType', hidden:true, editor:{ xtype:'hidden',id:'reportType' }
		},{
	   		header:'备注', dataIndex:'remark',editor: { maxLength:20,xtype:'textarea' }
		}],
		searchFn: function(searchParam){ 
			TrainType.searchParam = searchParam ;
	        TrainType.grid.store.load();
		}
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:ReportViewInfo.grid });
		
	//查询前添加过滤条件
	ReportViewInfo.grid.store.on('beforeload' , function(){
			ReportViewInfo.searchParam = {} ;
			var reportType = Ext.getCmp("reportTypeName_search_combo").getValue();
			ReportViewInfo.searchParam.reportType = reportType ;
			var searchParam = ReportViewInfo.searchParam ;
			searchParam = MyJson.deleteBlankProp(searchParam);
			this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
});