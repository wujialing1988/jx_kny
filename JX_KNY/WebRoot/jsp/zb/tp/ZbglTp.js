/**
 * JT6提票 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglTp');                       //定义命名空间
ZbglTp.searchParam = {};
/*** 查询表单 start ***/
ZbglTp.searchLabelWidth = 90;
ZbglTp.searchAnchor = '95%';
ZbglTp.searchFieldWidth = 270;
ZbglTp.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglTp.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: ZbglTp.searchFieldWidth, labelWidth: ZbglTp.searchLabelWidth, defaults:{anchor:ZbglTp.searchAnchor},
			items:[{
    				fieldLabel: "车辆车型",
    				hiddenName: "trainTypeIDX",
    				xtype: "Base_combo",
    			    business: 'trainVehicleType',
    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                    fields:['idx','typeName','typeCode'],
                    queryParams: {'vehicleType':vehicleType},// 根据车辆类型查询所对应的车辆种类
        		    displayField: "typeCode", valueField: "idx",
                    pageSize: 0, minListWidth: 200,
                    editable:true
        		},{ fieldLabel: "提票单号", name: 'faultNoticeCode', xtype: 'textfield'}
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: ZbglTp.searchFieldWidth, labelWidth: ZbglTp.searchLabelWidth, defaults:{anchor:ZbglTp.searchAnchor},
			items:[{
				fieldLabel: '车号',
				xtype: "textfield",
				name:'trainNo',
				maxLength : 4,
			    vtype: "numberInt"
			},{				
	        	xtype: 'combo',
	            fieldLabel: '状态',
	            hiddenName:'faultNoticeStatus',
	            store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : tpStatus
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				editable: false			
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglTp.searchForm.getForm();	
		        var searchParam = form.getValues();
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglTp.grid.searchFn(searchParam);
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglTp.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
            	searchParam = {};
            	ZbglTp.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/

/*** 提票列表 start ***/
ZbglTp.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglTp!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglTp!logicDeleteTP.action',            //删除数据的请求URL
    viewConfig: null,
    tbar:[{
    	text:'故障提票', iconCls:'addIcon', handler:function(){
    		ZbglTp.showSaveWin();
    	}
    },'delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
	{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车辆车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 80
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 80
	},{
		header:'提票人', dataIndex:'noticePersonName', editor:{  maxLength:25 }, width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'故障位置', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'常见故障', dataIndex:'faultName', editor:{  maxLength:250 }
	},{
		header:'故障现象', dataIndex:'faultDesc', editor:{  maxLength:500 }
	}/*,{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	}*/,{
		header:'状态', dataIndex:'faultNoticeStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case STATUS_INIT:
                    return STATUS_INIT_CH;
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                default:
                    return v;
            }
        }
	},{
		header:'配属段编码', dataIndex:'dID', hidden:true, editor:{  maxLength:20 }
	},{
		header:'配属段名称', dataIndex:'dName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'故障部件编码', dataIndex:'faultFixFullCode', hidden:true, editor:{  maxLength:200 }
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true, editor:{  maxLength:50 }
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true, editor:{  maxLength:300 }
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接票人名称', dataIndex:'revPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'施修方法', dataIndex:'methodDesc', hidden:true, editor:{  maxLength:200 }
	},{
		header:'处理结果', dataIndex:'repairResult', hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'销票人名称', dataIndex:'handlePersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true, editor:{  maxLength:50 }
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'检修类型', dataIndex:'repairClass', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case REPAIRCLASS_SX:
                    return "列检";
                case REPAIRCLASS_lX:
                    return "临修";
                default:
                    return v;
            }
        }
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglTp.searchParam = searchParam;
    	this.store.load();
    },
	beforeDeleteFn: function(){  
		var data = ZbglTp.grid.selModel.getSelections();
		for (var i = 0; i < data.length; i++){
	        if (!Ext.isEmpty(data[ i ].get("faultNoticeStatus")) && data[ i ].get("faultNoticeStatus") != STATUS_INIT) {
	        	MyExt.Msg.alert("只能删除初始化状态的提票记录");
	        	return false;
	        }
	    }
        return true;
    }
});

ZbglTp.grid.store.on("beforeload", function(){	
	var searchParam = ZbglTp.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {	
		if(prop =='trainTypeIDX') {
			whereList.push({propName:prop, propValue: searchParam[prop],compare : Condition.EQ, stringLike:false});
		}
		else 
        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	}
	whereList.push({propName:'faultNoticeStatus', propValues: [STATUS_INIT,STATUS_DRAFT] ,compare : Condition.IN}) ;
	whereList.push({propName:'noticePersonId', propValue: empid ,compare : Condition.EQ}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 提票详细信息window end ***/
ZbglTp.editWin = new Ext.Window({
	title: "故障提票编辑", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: [ZbglTpWin.panel]
});
ZbglTpWin.closeWin = function() {
	ZbglTp.editWin.hide();
	ZbglTp.saveWin.hide();
	ZbglTp.grid.store.load();
};
/*** 提票详细信息window end ***/

/*** 临碎修提票window start ***/
ZbglTp.saveForm = new Ext.form.FormPanel({
    layout: "form",     border: false,      style: "padding:10px",      labelWidth: 60,
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "98%" },
    items: [
    	{
			fieldLabel: "车辆车型",
			allowBlank:false ,
			hiddenName: "trainTypeIDX",
			xtype: "Base_combo",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            queryParams: {'vehicleType':vehicleType},// 根据车辆类型查询所对应的车辆种类
		    displayField: "typeCode", valueField: "idx",
		    returnField: [{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
            pageSize: 0, minListWidth: 200,
            editable:true
		},{
			fieldLabel: '车号',
			allowBlank:false ,
			xtype: "textfield",
			name:'trainNo',
			maxLength : 8,
		    vtype: "numberInt"
		},{ id:'trainTypeShortNameId', xtype:'hidden', name: 'trainTypeShortName' }
    	]
});
ZbglTp.saveWin = new Ext.Window({
    title:"故障提票", width: (ZbglTp.grid.labelWidth + ZbglTp.grid.fieldWidth + 8) + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, modal:true,
    items:ZbglTp.saveForm, 
    buttons: [{
        text: "确定", iconCls: "saveIcon", handler: function() {        	
		    var form = ZbglTp.saveForm.getForm(); 
		    if (!form.isValid()) return;
		    var data = form.getValues();
		    trainTypeIDX = data.trainTypeIDX;
		    trainTypeShortName = data.trainTypeShortName;
		    trainNo = data.trainNo;
		    //dId = data.dId;
		    //dName = data.dName;
		    ZbglTp.editWin.show();
		    ZbglTpWin.initSaveWin();
        }
    }, {
        text: "取消", iconCls: "closeIcon", handler: function(){ ZbglTp.saveWin.hide(); }
    }]
});

ZbglTp.showSaveWin = function(rowIndex) {	
//	var form = ZbglTp.saveForm.getForm();
//	form.reset();
	ZbglTp.saveWin.show();
};

/*** 临碎修提票window end ***/

/*** 界面布局 start ***/
ZbglTp.panel = {
    xtype: "panel", layout: "border",
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, collapsed: true,  height: 172, bodyBorder: false,
        items:[ZbglTp.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglTp.grid ]
    }]
};

//页面自适应布局
new Ext.Viewport({ layout:'fit', items:ZbglTp.panel });
});