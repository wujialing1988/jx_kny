/**
 * 工作日历 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkCalendarInfo');                       //定义命名空间
WorkCalendarInfo.searchParam = {};
WorkCalendarInfo.idx = "";
WorkCalendarInfo.labelWidth = 100;
//修改工作日历窗口
WorkCalendarInfo.updateWin = new Ext.Window({
		title: "设置工作时间", maximizable:false,maximized:true, layout: "fit", 
		closeAction: "hide", modal: true, buttonAlign:"center",
		items: [{id:"setWin"}],
		buttons: [{
			text: "关闭", iconCls:"closeIcon", handler:function(){
				WorkCalendarInfo.updateWin.hide();
				WorkCalendarInfo.grid.store.load();
			}
		}]
	});
WorkCalendarInfo.fieldWidth = 180;
WorkCalendarInfo.anchor = '95%';
WorkCalendarInfo.timefieldwidth = 80;//下拉宽度
WorkCalendarInfo.timefieldformat = 'H:i:s';//格式
WorkCalendarInfo.timefieldminValue = '00:00';//最小时间
WorkCalendarInfo.timefieldmaxValue = '23:50';//最小时间
WorkCalendarInfo.timefieldincrement = 30;//递增步长
WorkCalendarInfo.timefieldDefault = '00:00:00';
//新增日历form
WorkCalendarInfo.addForm = new Ext.form.FormPanel({
	layout:"form", border:true, labelAlign: "right",style:"padding:10px",
	labelWidth: WorkCalendarInfo.labelWidth, align:'center',baseCls: "x-plain",
	defaultType:'textfield',defaults:{anchor:"98%"}, 
	items:[{
		xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain",layout:"form",
			labelWidth: WorkCalendarInfo.labelWidth,	columnWidth:1.0,	defaults:{anchor:WorkCalendarInfo.anchor},
			items:[
				{ id:"idx",fieldLabel:'idx主键', name:'idx',hidden:true,width:WorkCalendarInfo.fieldWidth},
				{ id:"calendarName",fieldLabel:'工作日历名称',name:'calendarName',allowBlank: false,maxLength:32,width:190}
			]
		}]
	},{
		xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: WorkCalendarInfo.labelWidth,	columnWidth:0.5,	defaults:{anchor:WorkCalendarInfo.anchor},
			items:[
				{ id:"defPeriod1Begin",xtype:"timefield",fieldLabel:'默认时段1-始',name:'period1Begin',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
				{ id:"defPeriod2Begin",xtype:"timefield",fieldLabel:'默认时段2-始',name:'period2Begin',
  					  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
					  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
					  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
					{ id:"defPeriod3Begin",xtype:"timefield",fieldLabel:'默认时段3-始',name:'period3Begin',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
				{ id:"defPeriod4Begin",xtype:"timefield",fieldLabel:'默认时段4-始',name:'period4Begin',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement} 
			]
		},{
			align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: WorkCalendarInfo.labelWidth,	columnWidth:0.5,	defaults:{anchor:WorkCalendarInfo.anchor},
			items:[
				{ id:"defPeriod1End",xtype:"timefield",fieldLabel:'默认时段1-终',name:'period1End',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
				{ id:"defPeriod2End",xtype:"timefield",fieldLabel:'默认时段2-终',name:'period2End',
  					  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
					  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
					  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
					{ id:"defPeriod3End",xtype:"timefield",fieldLabel:'默认时段3-终',name:'period3End',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement},
				{ id:"defPeriod4End",xtype:"timefield",fieldLabel:'默认时段4-终',name:'period4End',
				  format: WorkCalendarInfo.timefieldformat,  minValue: WorkCalendarInfo.timefieldminValue, 
				  value:WorkCalendarInfo.timefieldDefault,maxValue: WorkCalendarInfo.timefieldmaxValue, 
				  width:WorkCalendarInfo.timefieldwidth, increment: WorkCalendarInfo.timefieldincrement}
			]
		}]
	},{
		xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: WorkCalendarInfo.labelWidth,	columnWidth:1.0,	defaults:{anchor:WorkCalendarInfo.anchor},
			items:[
				{ id:"remarks",fieldLabel:'备注',name:'remark',
				xtype:'textarea', height:80, maxLength:400,
				disabled:false,width:WorkCalendarInfo.fieldWidth}
			]
		}]
	}]
});
//新增表单数据验证
WorkCalendarInfo.validatorDefaultInfo = function (){
		var bg1 = Ext.getCmp("defPeriod1Begin").value; 
		var ed1 = Ext.getCmp("defPeriod1End").value;
		var bg2 = Ext.getCmp("defPeriod2Begin").value;
		var ed2 = Ext.getCmp("defPeriod2End").value;
		var bg3 = Ext.getCmp("defPeriod3Begin").value;
		var ed3 = Ext.getCmp("defPeriod3End").value;
		var bg4 = Ext.getCmp("defPeriod4Begin").value;
		var ed4 = Ext.getCmp("defPeriod4End").value;
		//首先判断第一组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg1 == null || bg1 == "")   { MyExt.Msg.alert("请正确填写默认时段1的开始时间");			return false; } 
		if(ed1 == null || ed1 == "")   { MyExt.Msg.alert("请正确填写默认时段1的结束时间");			return false; } 
		if((ed1 != "00:00:00" || bg1 != "00:00:00") && bg1 >= ed1)  				{ MyExt.Msg.alert("默认时段1的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第二组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg2 == null || bg2 == "")   { MyExt.Msg.alert("请正确填写默认时段2的开始时间");			return false; } 
		if(ed2 == null || ed2 == "")   { MyExt.Msg.alert("请正确填写默认时段2的结束时间");			return false; } 
		if(ed2 != "00:00:00" && bg2 >= ed2)                 { MyExt.Msg.alert("默认时段2的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第三组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg3 == null || bg3 == "")   { MyExt.Msg.alert("请正确填写默认时段3的开始时间");			return false; } 
		if(ed3 == null || ed3 == "")   { MyExt.Msg.alert("请正确填写默认时段3的结束时间");			return false; } 
		if(ed3 != "00:00:00" && bg3 >= ed3)                 { MyExt.Msg.alert("默认时段3的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第四组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg4 == null || bg4 == "")   { MyExt.Msg.alert("请正确填写默认时段4的开始时间");			return false; } 
		if(ed4 == null || ed4 == "")   { MyExt.Msg.alert("请正确填写默认时段4的结束时间");			return false; } 
		if(ed4 != "00:00:00" && bg4 >= ed4)                 { MyExt.Msg.alert("默认时段4的结束时间不应早于或等于开始时间");	return false; }
		
		if(bg2 != "00:00:00" && bg2 < ed1){
			MyExt.Msg.alert("默认时段2的开始时间不能早于默认时段1的结束时间");
			return false;
		}
		if(bg3 != "00:00:00" && bg3 < ed2){
			MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段2的结束时间");
			return false;
		}
		if(bg4 != "00:00:00" && bg4 < ed3){
			MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段3的结束时间");
			return false;
		}
		if (bg2 != "00:00:00" && ed1 == "00:00:00") {
			MyExt.Msg.alert("默认时段2的开始时间不能早于默认时段1的结束时间");
			return false;
		}
		if (bg3 != "00:00:00") {
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段2的结束时间");
				return false;
			}
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段1的结束时间");
				return false;
			}
		}
		if (bg4 != "00:00:00") {
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段1的结束时间");
				return false;
			}
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段2的结束时间");
				return false;
			}
			if (ed3 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段3的结束时间");
				return false;
			}
		}
		
		return true;
	}
/**
 * 新增工作日历窗口
 */
WorkCalendarInfo.addWin = new Ext.Window({
	title: "新增工作日历", maximizable:false, width:480, height:305, layout: "fit", 
	closeAction: "hide", modal: true, buttonAlign:"center",
	items: [WorkCalendarInfo.addForm],
	buttons: [{
	    text: "保存", iconCls:"saveIcon",handler:function(){
	    	
			if(!WorkCalendarInfo.addForm.getForm().isValid()){return;} //未能通过验证
			if(!WorkCalendarInfo.validatorDefaultInfo()){return;} //未能通过验证
			var data = WorkCalendarInfo.addForm.getForm().getValues();
			data.isDefault    = "0";  //非默认日历

			Ext.Ajax.request({
				url: ctx + "/workCalendarInfo!saveOrUpdate.action",
				jsonData: data,
				success: function(response, options){
				       var result = Ext.util.JSON.decode(response.responseText);
				       if (result.errMsg == null) {
				       		alertSuccess();
				       } else {
				           alertFail(result.errMsg);
				       }
				       WorkCalendarInfo.grid.store.load();
				},
				failure: function(response, options){
				       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
	    }
	},{
		text: "关闭", iconCls:"closeIcon", handler:function(){
			WorkCalendarInfo.addWin.hide();
		}
	}]
});



WorkCalendarInfo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workCalendarInfo!findCalendarInfoList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workCalendarInfo!saveOrUpdate.action',                 //新增请求URL
    deleteURL: ctx + '/workCalendarInfo!logicDelete.action',                 //新增请求URL
    tbar: ['add','delete', {
    	text: '设为默认', iconCls: 'configIcon', handler: function() {
    		var sm = WorkCalendarInfo.grid.getSelectionModel();
    		if (sm.getCount() <= 0) {
    			MyExt.Msg.alert('尚未选择任何记录！');
    			return;
    		}
    		if (sm.getCount() > 1) {
    			MyExt.Msg.alert('只能选择一条记录为默认工作日历！');
    			return;
    		}
    		// 获取选区里的第一条记录的idx主键
    		var record = sm.getSelected();
    		if (IS_DEFAULT_YES == record.get('isDefault')) {
    			MyExt.Msg.alert('当前工作日历已经为默认！');
    			return;
    		}
    		var idx = sm.getSelected().get('idx');
    		// Ajax后台请求
    		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
    			scope: WorkCalendarInfo.grid,
    			params: {id: idx},
    			url: ctx + '/workCalendarInfo!updateToDefault.action'
    		}));
     	}
    } ,'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'日历名称', dataIndex:'calendarName', editor:{  maxLength:50 }
	},{
		header:'工作时间1', dataIndex:'period1Begin', editor:{  maxLength:8 }
	},{
		header:'工作时间2', dataIndex:'period2Begin', editor:{  maxLength:8 }
	},{
		header:'工作时间3', dataIndex:'period3Begin', editor:{  maxLength:8 }
	},{
		header:'工作时间4', dataIndex:'period4Begin', editor:{  maxLength:8 }
	},{
		header:'是否默认', dataIndex:'isDefault',
		renderer : function(v){
			if(v == '0') return "否"
			else if(v == '1') return "是"
			else return null
		}
	},{
		header:'备注', dataIndex:'remarks', searcher:{ xtype: "textfield" }
	}],
	addButtonFn: function(){
		WorkCalendarInfo.addWin.show();
		WorkCalendarInfo.addForm.getForm().reset();
	},
	beforeDeleteFn: function(){     
		var records=WorkCalendarInfo.grid.selModel.getSelections();
		for (var i = 0; i < records.length; i++) {
			if('1' == records[i].data.isDefault){
				MyExt.Msg.alert("默认工作日历不能删除！");
	   	 	    return ;
			}
		}
        return true;
    },
	toEditFn: function(grid, rowIndex, e){
		var record = this.store.getAt(rowIndex);
		WorkCalendarInfo.updateWin.show();
		var h = jQuery("#setWin").height();
		var url = ctx+"/jsp/baseapp/workcalendar/WorkCalendar.jsp?infoIdx=" + record.get("idx") ;
		document.getElementById("setWin").innerHTML =  "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src="+url+"></iframe>";
//		document.getElementById("infoIdx").value = record.get("idx");
	}
});


//页面自适应布局
var viewport = new Ext.Viewport({
		   layout:'fit',
    	   items:[ WorkCalendarInfo.grid ]
    	   });
});