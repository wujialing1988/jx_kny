/**
 * 货车客车巡检信息录入
 */
Ext.onReady(function(){
	
	Ext.ns('TrainInspectRecord');
	
	TrainInspectRecord.trainIdx;
	//定义全局变量保存查询条件
	TrainInspectRecord.searchParam = {} ;
	
	TrainInspectRecord.grid = new Ext.yunda.Grid({
		viewConfig: true,
		loadURL: ctx + '/trainInspectRecord!pageList.action',
		saveFormColNum:1,	searchFormColNum:1, fieldWidth: 100,
		searchFn: function(searchParam){ 
			TrainInspectRecord.searchParam = searchParam ;
        	TrainInspectRecord.grid.store.load();
		},
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {xtype: 'hidden'}
		},{
			header:'列车主键', dataIndex:'trainIdx', hidden:true, editor: {xtype: 'hidden'}
		},{
			header:'车号', dataIndex:'trainNo', editor: {
				allowBlank: false, editable: true,
				xtype: "singlefieldcombo",
				entity: 'com.yunda.freight.zb.inspectrecord.entity.TrainInspectRecord',
				xfield: 'trainNo'
			}
		},{
			header:'责任人ID', dataIndex:'personResponsibleId', hidden:true, editor: {id: 'personResponsibleId', xtype: 'hidden'}
		},{
			header:'责任人', dataIndex: 'personResponsible', editor: {
				  id: 'empname_SelectWin_Id', xtype: 'OmEmployee_SelectWin', fieldLabel: '责任人',
				  hiddenName: 'personResponsible', displayField:'empname', valueField: 'empid',
				  returnField :[{widgetId: "personResponsibleId", propertyName: "empid"}],
				  editable: false, width: 100
			}
		},{
			header:'录入人ID', dataIndex: 'recordPersonId', width: 140, hidden:true, editor: {xtype: 'hidden'}
		},{
			header:'录入人', dataIndex: 'recordPerson', editor: {xtype: 'hidden'}
		},{
			header:'录入时间', dataIndex:'recordTime', width: 200, renderer: function(v) {
				if (v) return Ext.util.Format.date(new Date(v), 'Y-m-d H:i');
			}, editor: {xtype:'hidden'}
		},{
			header:'巡检类别', dataIndex: 'inspectType', editor: { 
		  	     xtype: 'EosDictEntry_combo', fieldLabel: '巡检类别',
			     hiddenName: 'inspectType', displayField: 'dictname', valueField: 'dictname',
			     dicttypeid:'XJLB', width:100
		   }
		},{
			header:'巡检详情', dataIndex:'inspectDetail', width: 200, editor: {xtype: 'textarea', maxLength: 100}
		},{
			header:'客车类型', dataIndex:'vehicleType', width: 60, hidden:true, editor: { xtype:"hidden",value:vehicleType }
		}],
		
		toEditFn: function(grid, rowIndex, e){},
		
		beforeSaveFn: function(data){
			data.trainIdx = TrainInspectRecord.trainIdx;
			return true; 
		},
		
		afterShowSaveWin: function(){
			// 默认带出当前登录人
			Ext.getCmp('personResponsibleId').setValue(empId);
			Ext.getCmp("empname_SelectWin_Id").setDisplayValue(empName,empName);
		},
		
		afterShowEditWin: function(record, rowIndex){
			Ext.getCmp("empname_SelectWin_Id").setDisplayValue(record.data.personResponsible,record.data.personResponsible);
		}
	})
	
	TrainInspectRecord.grid.store.on('beforeload', function() {
		var searchParam = TrainInspectRecord.searchParam ;
		searchParam.vehicleType = vehicleType;
		searchParam.trainIdx = TrainInspectRecord.trainIdx;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	new Ext.Viewport({
		layout: 'border', defaults: {layout: 'fit'},
		items: [{
			region : 'west',
			width: 340, split: true,
			items: TrainList.grid
		}, {
			region: 'center',
			items: TrainInspectRecord.grid
		}]
	});
});