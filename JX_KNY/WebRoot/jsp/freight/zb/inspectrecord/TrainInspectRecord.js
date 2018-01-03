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
		saveURL: ctx + '/trainInspectRecord!saveOrUpdate.action',
		loadURL: ctx + '/trainInspectRecord!pageList.action',
		deleteURL: ctx + '/trainInspectRecord!logicDelete.action',
		saveFormColNum:1,	
		searchFormColNum:1,
		fieldWidth: 200,
		viewConfig: {forceFit: true },
		searchFn: function(searchParam){ 
			TrainInspectRecord.searchParam = searchParam ;
        	TrainInspectRecord.grid.store.load();
		},
		fields: [{
			header:i18n.InspectionInfoInput.idx, dataIndex:'idx', hidden:true, editor: {xtype: 'hidden'}
		},{
			header:i18n.InspectionInfoInput.trainIdx , dataIndex:'trainIdx', hidden:true, editor: {xtype: 'hidden'}
		},{
			header:i18n.InspectionInfoInput.trainNum, dataIndex:'trainNo', editor: {
				fieldLabel: i18n.InspectionInfoInput.trainNum,
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:true
			}
		},{
			header:i18n.InspectionInfoInput.responsibleId, dataIndex:'personResponsibleId', hidden:true, editor: {id: 'personResponsibleId', xtype: 'hidden'}
		},{
			header:i18n.InspectionInfoInput.responsibleName, dataIndex: 'personResponsible',width: 120, editor: {
				  id: 'empname_SelectWin_Id', xtype: 'OmEmployee_SelectWin', fieldLabel: i18n.InspectionInfoInput.responsibleName,
				  hiddenName: 'personResponsible', displayField:'empname', valueField: 'empname',
				  returnField :[{widgetId: "personResponsibleId", propertyName: "empid"}],
				  editable: false, width: 100
			}
		},{
			header:i18n.InspectionInfoInput.recordPersonId, dataIndex: 'recordPersonId', width: 140, hidden:true, editor: {xtype: 'hidden'}
		},{
			header:i18n.InspectionInfoInput.recordPersonName, dataIndex: 'recordPerson',width: 120, editor: {xtype: 'hidden'}
		},{
			header:i18n.InspectionInfoInput.recordTime, dataIndex:'recordTime', width: 120, renderer: function(v) {
				if (v) return Ext.util.Format.date(new Date(v), 'Y-m-d H:i');
			}, editor: {xtype:'hidden'}
		},{
			header:i18n.InspectionInfoInput.inspectType, dataIndex: 'inspectType', editor: { 
		  	     xtype: 'EosDictEntry_combo', fieldLabel: i18n.InspectionInfoInput.inspectType,
			     hiddenName: 'inspectType', displayField: 'dictname', valueField: 'dictname',
			     dicttypeid:'XJLB', width:100
		   }
		},{
			header:i18n.InspectionInfoInput.inspectDetail, dataIndex:'inspectDetail', editor: {xtype: 'textarea', maxLength: 100}
		},{
			header:i18n.InspectionInfoInput.vehicleType, dataIndex:'vehicleType', width: 60, hidden:true, editor: { xtype:"hidden",value:vehicleType }
		}],
		
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