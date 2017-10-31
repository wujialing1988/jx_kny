/**
 * 机车交接单项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoCaseItem');                       //定义命名空间

ZbglHoCaseItem.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglHoCaseItem!pageQuery.action',                 //装载列表数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    singleSelect: true,
    storeAutoLoad: false,
    tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'交接单主键', dataIndex:'handOverRdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'交接项ID', dataIndex:'handOverItemModelIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'交接项', dataIndex:'parentItemName', editor:{  maxLength:100 }
	},{
		header:'检查项', dataIndex:'handOverItemName', editor:{  maxLength:100 }
	},{
		header:'交接状态', dataIndex:'handOverItemStatus', editor:{  maxLength:50 },
		renderer:function(v, metaData, record, rowIndex, colIndex, store){
    	  	var html = "";
    	  	if(!Ext.isEmpty(v) ){
    	  		var statusArray = v.split(",");
				for (var i = 0; i < statusArray.length; i++){
					var id = (rowIndex+1) + "" + (i+1);
					html+= "<INPUT TYPE='checkbox' id='" + id + "' CHECKED disabled>"+statusArray[i];
				}
    	  	}
    	  	return html;
    	  }
	},{
		header:'交接情况描述', dataIndex:'handOverResultDesc', editor:{  maxLength:1000 }
	},{
		header:'父交接项ID', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
	}]
});
ZbglHoCaseItem.grid.store.on("beforeload",function(){
	var searchParam = {};
	searchParam.handOverRdpIDX = ZbglHoCase.idx;
	var whereList = [];
	for (prop in searchParam) {
		if(prop == 'handOverRdpIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
	    	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);	
})
ZbglHoCaseItem.grid.un('rowdblclick', ZbglHoCaseItem.grid.toEditFn, ZbglHoCaseItem.grid);
//基本信息表单
ZbglHoCaseItem.caseForm = new Ext.form.FormPanel({
	labelWidth:ZbglHoCaseItem.labelWidth,
	border: false,
	labelAlign:"left",
	layout:"column",
	bodyStyle:"padding:10px;",
	defaults:{
		xtype:"container", autoEl:"div", layout:"form", columnWidth:0.33, 
		defaults:{
			style: 'border:none; background:none;', 
			xtype:"textfield", readOnly: true,
			anchor:"100%"
		}
	},
	items:[{
		items:[{
			fieldLabel:"机车型号", name:"trainType"
		}, {
			fieldLabel:"入段时间", name:"inTime"
		}]
	}, {
		items:[{
			fieldLabel:"配属段", name:"dName"
		}, {
			fieldLabel:"迎检时间", name:"arrivedTime"
		}]
	}, {
		items:[{
			fieldLabel:"站场", name:"siteName"
		}, {
			fieldLabel:"交车司机", name:"fromPersonName"
		}]
	},{
		columnWidth:0.6,
		items:[{
			fieldLabel:"备注", name:"remarks"
		}]
	},{
		columnWidth:0.4,
		items:[ {
			fieldLabel:"迎检人", name:"toPersonName"
		}]
	}]
});
//接车记录详情口
ZbglHoCaseItem.win = new Ext.Window({
		title: "整备迎检记录详情",
		width: 950, height: 500, 
		maximizable:false, maximized: false, modal: true,
		closeAction: "hide",
		layout: "fit",
		items:[{
				xtype:"panel",frame:true, title:'基本信息',
				height: 140,
				border: true,
				collapsible: true,
				items: [ZbglHoCaseItem.caseForm]
			}/*, {
				title:"交接项目",
				region: 'center',
				layout: 'fit',
				border: false,
				items: [ZbglHoCaseItem.grid]
			}*/],
		buttonAlign:"center",
		buttons: [{
	        text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglHoCaseItem.win.hide(); }
	    }]
    });
});