/*整备范围*/
Ext.onReady(function(){
Ext.namespace('ZbFw');                       //定义命名空间

	/* ************* 定义全局变量开始 ************* */
	ZbFw.labelWidth = 100;
	ZbFw.fieldWidth = 150;
	ZbFw.zbFwIdx = "";
	ZbFw.searchParams = {};
	ZbFw.isAddAndNew = false; 
	/* ************* 定义全局变量结束 ************* */
	
ZbFw.searchForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.33,
			defaults:{
				xtype:"textfield", width: ZbFw.fieldWidth
			}
		},
		items:[{
			items:[{
    		        id:"trainType_combo",	
    				fieldLabel: "车辆车型",
    				hiddenName: "trainTypeIDX",
    				xtype: "Base_combo",
    			    business: 'trainVehicleType',
    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                    fields:['idx','typeName','typeCode'],
                    queryParams: {'vehicleType':vehicleType},// 表示
        		    displayField: "typeCode", valueField: "idx",
                    pageSize: 0, minListWidth: 200,
                    editable:true
        	}]
		}, {
			items:[{
				fieldLabel:"范围名称",
				width:150,
				name: 'fwName'
			}]
		},{

		  	items:[{
		  	  	fieldLabel:"范围编码",
		  	  	width: 170,
				name: 'fwCode'
		  	}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			 var form = ZbFw.searchForm.getForm();
				if (form.isValid()) {
					ZbFw.grid.store.load();
				}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    ZbFw.searchForm.getForm().reset();
                    Ext.getCmp('trainType_combo').clearValue();
				    // 重新加载表格
				    ZbFw.grid.store.load();
			}
		}]
	})
	// 配置流程节点
ZbFw.configNodeFn = function() {
	var sm = ZbFw.grid.getSelectionModel();
	if (sm.getCount() <= 0) {
		MyExt.Msg.alert('尚未选择一条记录！');
		return;
	}
	if (sm.getCount() > 1) {
		MyExt.Msg.alert('请只选择一条记录进行流程节点设置');
		return;
	}
	var record = sm.getSelections()[0];
	ZbglJobProcessNodeDef.zbfwIDX = record.get('idx');
	ZbglJobProcessNodeDef.processName = record.get('fwName');
	ZbglJobProcessNodeDef.tree.root.setText(record.get('fwName'));
	Ext.getCmp('tabpanel_node').setTitle(record.get('fwName') + " - 作业节点");
	
	ZbglJobProcessNodeDef.win.show();
}	
//复制表单
ZbFw.copyForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:2px",		labelWidth: ZbFw.labelWidth,
	    items: [
	    {
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ZbFw.labelWidth,
	            columnWidth: 1, 
	            items: [
	        	{
					id:"fwName_copy",
					fieldLabel:"范围名称",
					width:150,allowBlank:false,
					name: 'fwName'
				},
	            {id:"trainTypeName_copy",fieldLabel:"车型名称",name: 'trainTypeName',xtype:"hidden"},
	            {id:"trainTypeShortName_copy",fieldLabel:"车型简称",name: 'trainTypeShortName',xtype:"hidden"},
	            {id:"idx_copy",fieldLabel:"id主键",name: 'idx',xtype:"hidden"}
	            ]
	        }
	        ]
	    }]
	});
ZbFw.copyWin = new Ext.Window({
	    title:"复制", maximizable:true, width:400, height:200,
	    plain:true,  layout:"fit", closeAction:"hide",
	    items: ZbFw.copyForm, 
		buttonAlign: 'center',
		buttons:[{
			text:'复制', iconCls:'saveIcon', handler: function() {
					var form = ZbFw.copyForm.getForm(); 
			        if (!form.isValid()) return;
			        var data = form.getValues();
				    var cfg = {
				        scope: this, url: ctx + '/zbFwCopy!copyJobProcessDef.action',
				        jsonData: data,
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null) {
				            	alertSuccess();
				            	ZbFw.copyWin.hide();
				                ZbFw.grid.store.load();
				            }else {
			                        alertFail(result.errMsg);
			                    }
				        }
				     };
			    	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			}
		},{
			text:'关闭', iconCls:'closeIcon', handler: function() {
                    ZbFw.copyWin.hide();
				    // 重新加载表格
				    ZbFw.grid.store.load();
			}
		}]
	});
ZbFw.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbFw!pageList.action',              //装载列表数据的请求URL
    saveURL: ctx + '/zbFw!saveZbFw.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbFw!logicDelete.action',            //删除数据的请求URL
    saveWinWidth: 600,
    saveWinHeight:300,
    tbar: ['add',{text:"设置流程节点", iconCls:"editIcon",
    	    handler: ZbFw.configNodeFn
    	   }, {
	    	text: '复制', iconCls: 'wrenchIcon', handler: function() {
	    		var sm = ZbFw.grid.getSelectionModel();
	    		if (sm.getCount() <= 0) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    		if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行复制！');
	    			return;
	    		}
	    		ZbFw.copyWin.show();
	    		ZbFw.copyForm.getForm().reset();
	    		ZbFw.copyForm.getForm().loadRecord(sm.getSelections()[0]);
                Ext.getCmp('trainType_combo_copy').clearValue();
                var trainType_combo_copy =  Ext.getCmp("trainType_combo_copy");
		        trainType_combo_copy.reset();
		        trainType_combo_copy.getStore().removeAll();
		        trainType_combo_copy.cascadeStore();
	    	}
	    },
    	   'delete','refresh',{
    	   text:"设置适用机车", iconCls:"queryIcon",hidden:true,
	    	 handler: function(){
	    	 	if (!$yd.isSelectedRecord(ZbFw.grid)){
					MyExt.Msg.alert("请选择一条数据!");
					return;
	    	 	}
	    	 	
	    	 	//讲当前选择的行的车型传递到机车选择列表中
	    	 	var sm = ZbFw.grid.getSelectionModel();
	    	 	if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行复制！');
	    			return;
	    		}
	    	 	
	    	 	var record = sm.getSelections()[0];
	    	 	TrianInfoWin.trainTypeIDX = record.data.trainTypeIDX;//车型idx
				TrianInfoWin.trainTypeShortName = record.data.trainTypeShortName;//车型名称
				TrianInfoWin.zbFwIdx = record.data.idx;//范围idx
				TrianInfoWin.fwName = record.data.fwName;//范围名称
				
				Ext.getCmp("trainTypeShortNameShow").setText(TrianInfoWin.trainTypeShortName);
	    	 	
			    ZbfwTrianCenterWin.zbFwIdx = record.data.idx;//范围主键idx
	    		ZbfwTrianCenterWin.selectWin.show();
	    		ZbfwTrianCenterWin.grid.store.load();
	    	}
	    },{
    	   text:"设置适用车型", iconCls:"queryIcon",
	    	 handler: function(){
	    	 	if (!$yd.isSelectedRecord(ZbFw.grid)){
					MyExt.Msg.alert("请选择一条数据!");
					return;
	    	 	}
	    	 	
	    	 	var sm = ZbFw.grid.getSelectionModel();

	    	 	if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行设置！');
	    			return;
	    		}
	    	 	
	    	 	if (sm.getCount() == 1) {
	    	 		var record = sm.getSelections()[0];
	    	 		TrainType.trainVehicleCode = record.get('trainVehicleCode');
	    		}else{
	    			TrainType.trainVehicleCode = "" ;
	    		}
	    		
	    		TrainType.whereList = [] ;
	    		var sqlStr = "T_TYPE_CODE not in (select t.train_vehicle_code from ZB_FW_VEHICLE_RELATION t "; 
					sqlStr += " inner join ZB_ZBFW f on t.zbfw_idx = f.idx and f.Record_Status = 0 and f.idx <> '"+record.get('idx')+"')" ; 	
	    		TrainType.whereList.push({sql: sqlStr, compare:Condition.SQL}); //排除已选择过的数据
	    		TrainType.grid.store.load();
	    		TrainType.selectWin.show();
	    		
	    	}
	    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'范围编码', dataIndex:'fwCode',editor:{ maxLength:50 ,allowBlank:false},
		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
            var html = "";
            html = "<span><a href='#' onclick='ZbFw.configNodeFn()'>"+value+"</a></span>";
            return html;
        }
	},{
		header:'车型', dataIndex:'trainTypeShortName',hidden:true, editor:{
				hidden:true,
				fieldLabel:"车型",
				id:"trainType_combJ", xtype: "Base_combo",
				hiddenName: "trainTypeShortName",
				entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
				queryHql:"select t from TrainVehicleType t where t.recordStatus=0 and t.vehicleType = '10' ",	
				returnField: [{widgetId:"trainTypeIDX",propertyName:"idx"},{widgetId:"trainTypeName",propertyName:"typeName"}],
				displayField: "typeCode", valueField: "typeCode",
				pageSize: 20,
				editable:true  ,
				fields:["idx", "typeCode", "typeName"]
			}
	},{
		header:'范围名称', dataIndex:'fwName', editor:{maxLength:100,allowBlank:false, width: 400}
	},{
		header:'范围描述', dataIndex:'fwDesc', editor:{  maxLength:500,xtype:'textarea', width: 400 }
	},{
	   	header:'适用作业性质编码', dataIndex:'workNatureCode', hidden:true, editor: { xtype:'hidden',id:'workNatureCode' }
	},
	// 修改为单选
		{
		header:'适用作业性质', dataIndex:'workNature',hidden:true, editor:{
			id:'workNature_combo',
			xtype: 'EosDictEntry_combo',
			hiddenName: 'workNature',
			dicttypeid:'FREIGHT_WORKNATURE',
			displayField:'dictname',valueField:'dictname',
			hasEmpty:"false",
			hidden:true,
			returnField: [{widgetId:"workNatureCode",propertyName:"dictid"}]
		}
	},{
		header:'适用车辆编码', dataIndex:'trainVehicleCode',hidden:true, editor:{xtype:"hidden"}
	},{
		header:'适用车型', dataIndex:'trainVehicleName', editor:{xtype:"hidden"}
	},{
		header:'客货类型', dataIndex:'vehicleType',hidden:true, editor:{xtype:"hidden",value:vehicleType}
	},{
	    header:'站场ID', dataIndex:'siteID',hidden:true,editor:{  maxLength:50,xtype:'hidden'}
	},{
	    header:'车型ID', dataIndex:'trainTypeIDX',hidden:true,editor:{  id:"trainTypeIDX",xtype:'hidden'}
	},{
	    header:'车型名称', dataIndex:'trainTypeName',hidden:true,editor:{  id:"trainTypeName",xtype:'hidden'}
	}],
  afterShowSaveWin: function(){
	   ZbFw.setfwCodeFn(this.saveForm);
	   var trainType_combJ =  Ext.getCmp("trainType_combJ");
        trainType_combJ.reset();
        trainType_combJ.getStore().removeAll();
        trainType_combJ.cascadeStore();
        
        // 清空作业性质
//       Ext.getCmp('workNature_multyComboTree').clearValue();
        
	},
  afterShowEditWin: function(record, rowIndex){
  	 	var trainType_combJ =  Ext.getCmp("trainType_combJ");
        trainType_combJ.reset();
        trainType_combJ.getStore().removeAll();
        trainType_combJ.cascadeStore();
	    Ext.getCmp("trainType_combJ").setDisplayValue(record.get("trainTypeIDX"), record.get("trainTypeShortName"));
	   
	    // 清空作业性质
//        Ext.getCmp('workNature_multyComboTree').clearValue();
//        Ext.getCmp('workNature_multyComboTree').setDisplayValue(record.get("workNature"), record.get("workNature"));
	},
  afterSaveSuccessFn: function(result, response, options){
	        alertSuccess();
	        ZbFw.grid.saveWin.hide();
	        ZbFw.grid.store.load();
	    } 
	});
	//页面加载和数据模糊查询
  ZbFw.grid.store.on('beforeload', function() {
		ZbFw.searchParams = ZbFw.searchForm.getForm().getValues();
		ZbFw.searchParams.vehicleType = vehicleType ;
		ZbFw.searchParams = MyJson.deleteBlankProp(ZbFw.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbFw.searchParams);
	})
	
// 机车选择方法
TrainType.submit = function(){

	var idxs = [];				
	//未选择记录，直接返回
	var records = TrainType.grid.getSelectionModel().getSelections();
    if(records.length == 0){
    	MyExt.Msg.alert("尚未选择适用车辆");
    	return;
    }
    var trianCodes = [];
    var trianTypes = [];
    for (var i = 0; i < records.length; i++) {
    	trianCodes.push(records[i].get("typeCode"));
    	trianTypes.push(records[i].get("typeName"));
    }
    if(trianCodes.toString().length > 499){
    	 MyExt.Msg.alert("选择适用车辆超过最大限制,请重新选择");
    	 return;
    }
    
    var fwrecord = ZbFw.grid.getSelectionModel().getSelections();
    for (var j = 0; j < fwrecord.length; j++) {
    	idxs.push(fwrecord[j].data.idx);
    }
    
	 Ext.Msg.confirm("提示","确认选择", function(btn){
		if(btn == 'yes'){
			showtip();
	    	$.ajax({
		    	url: ctx + "/zbFw!setTrainVehicle.action",
		    	data:{idxs:idxs+"", trianCodes:trianCodes+"" ,trianTypes:trianTypes+""},
		    	type:"post",
		    	dataType:"json",
		    	success:function(data){
		    		hidetip();
		    		if (data.errMsg != null) {
		    			MyExt.Msg.alert(data.errMsg);
	    				return;
		    		}
		    		//刷新GRID
		    		ZbFw.grid.store.reload();
		    		TrainType.selectWin.hide();
		    		alertSuccess();
		    	}
		    });	
		}
	  });
}


   // 自动设置【编号】字段值
  ZbFw.setfwCodeFn = function(form) {
  	Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "ZBGL_ZBFW_BH_CODE"},
			success: function(response, options){
		        result = Ext.util.JSON.decode(response.responseText);
		        form.find('name', 'fwCode')[0].setValue(result.rule);
		        //ZbFw.zbFwcode = result.rule;
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
  }	
	
 // 页面自适应布局
ZbFw.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 143,
			collapsible: true,
			items: [ZbFw.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center', 
			layout: "fit",
			bodyStyle:'padding-left:0px;', 
            bodyBorder: true,
			items: [ZbFw.grid]
		}]
	});
})