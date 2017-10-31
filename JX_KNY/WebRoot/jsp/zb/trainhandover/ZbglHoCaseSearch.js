/**
 * 机车交接单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoCase');                       //定义命名空间
ZbglHoCase.searchParam = {};
ZbglHoCase.idx = "";
ZbglHoCase.labelWidth = 70;
//最近一个月
ZbglHoCase.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	return MonthFirstDay.format('Y-m-d');
}
ZbglHoCase.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglHoCase!getCaseList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:[{
    	text: '详情查看', iconCls:'messageIcon', handler:function(){
			var grid = ZbglHoCase.grid;
			if(!$yd.isSelectedRecord(grid)) return;
			var data = grid.selModel.getSelections();
			if(data.length > 1){
				MyExt.Msg.alert("只能查看一条记录");
				return;
			}
			var record = data[0];
			ZbglHoCase.idx = record.get("idx");
			ZbglHoCaseItem.win.show();
			ZbglHoCaseItem.grid.store.load();
			ZbglHoCaseItem.caseForm.getForm().loadRecord(record);
			// 设置入段时间的显示值
			if(record.get('inTime') != null) ZbglHoCaseItem.caseForm.find('name', 'inTime')[0].setValue(record.get('inTime').format('Y-m-d H:m'));
			// 设置接车时间的显示值
			if(record.get('handOverTime') != null) ZbglHoCaseItem.caseForm.find('name', 'handOverTime')[0].setValue(record.get('handOverTime').format('Y-m-d H:m'));
			var trainType = record.get('trainTypeShortName') == null ? '' : record.get('trainTypeShortName') ;
			var trainNo = record.get('trainNo') == null ? '' : record.get('trainNo') ;
			ZbglHoCaseItem.caseForm.find('name', 'trainType')[0].setValue(trainType+"|"+trainNo);
    	}
    },{   
            text:"打印接车单", iconCls:"printerIcon", handler: function(){
                var sel = ZbglHoCase.grid.selModel.getSelections();
                if(sel.length != 1){
                    MyExt.Msg.alert("请选择一条记录!");
                    return ;
                }
                var idx = sel[0].get("idx");                              
                var url = "/zb/trainhandover/ZbglHoCase.cpt";
                if(idx){
                    url += "&idx=" + idx;
                    window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(url))+"&title=" + encodeURI("机车交接单"));
                }
            } 
        },{text:"导出", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglHoCase.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("inTime_Id").getValue() != ""){
		        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d') + " 00:00:00";
		        }
		        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
		        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/trainhandover/ZbglHoCaseList.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeShortName=" + data.trainTypeShortName +
				"&trainNo=" + data.trainNo + "&dName=" + data.dName + "&siteName=" + data.siteName;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("整备交接列表"));
    	     }
     },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'整备任务单主键', dataIndex:'rDPIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'入段去向', dataIndex:'trainToGo', editor:{},
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		},
		searcher:{xtype: 'textfield'}, width: 100
	},{
		header:'配属段', dataIndex:'dName', editor:{  maxLength:50 }
	},{
		header:'入段车次', dataIndex:'handOverTrainOrder', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype: "datecolumn", format: "Y-m-d H:i", 
		searcher:{id:'inTime_Id',xtype:'my97date', my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, initNow: false, validator: ZbglHoCase.checkDate}
	},{
		header:'交接时间', dataIndex:'handOverTime', xtype:'datecolumn',format: "Y-m-d H:i", editor:{ xtype:'my97date' }
	},{
		header:'交车人ID', dataIndex:'fromPersonId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'交车司机', dataIndex:'fromPersonName', editor:{  maxLength:50 }
	},{
		header:'接车人ID', dataIndex:'toPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接车人', dataIndex:'toPersonName', editor:{  maxLength:50 }
	},{
		header:'备注', dataIndex:'remarks', searcher:{ xtype: "textfield" }
	},{
		header:'站点', dataIndex:'siteName', searcher:{ xtype: "textfield" }
	}],
	toEditFn: function(grid, rowIndex, e){
		var record = this.store.getAt(rowIndex);;
		ZbglHoCase.idx = record.get("idx");
		ZbglHoCaseItem.win.show();
		ZbglHoCaseItem.grid.store.load();
		ZbglHoCaseItem.caseForm.getForm().loadRecord(record);
		// 设置入段时间的显示值
		if(record.get('inTime') != null) ZbglHoCaseItem.caseForm.find('name', 'inTime')[0].setValue(record.get('inTime').format('Y-m-d H:m'));
		// 设置接车时间的显示值
		if(record.get('handOverTime') != null) ZbglHoCaseItem.caseForm.find('name', 'handOverTime')[0].setValue(record.get('handOverTime').format('Y-m-d H:m'));
		var trainType = record.get('trainTypeShortName') == null ? '' : record.get('trainTypeShortName') ;
		var trainNo = record.get('trainNo') == null ? '' : record.get('trainNo') ;
		ZbglHoCaseItem.caseForm.find('name', 'trainType')[0].setValue(trainType+"|"+trainNo);
	}
});
ZbglHoCase.searchForm = new Ext.form.FormPanel({
		labelWidth: ZbglHoCase.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        items: [{
        	columnWidth:.15, layout:'form', defaults: {
				width:ZbglHoCase.fieldWidth        		
        	},
			items:[{
				id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
				hiddenName: "trainTypeShortName",
				displayField: "shortName", valueField: "shortName",
				pageSize: 20, minListWidth: 200,   
				editable:false
			}, {
				xtype:'textfield',fieldLabel:'站点',id:'siteName',name:"siteName"
			}]
		},{
			columnWidth:.25, layout:'form', defaults: {
				width:ZbglHoCase.fieldWidth        		
        	},
			items:[{
				id:"trainNo",xtype:'textfield',fieldLabel:'车号',name:"trainNo"
			},{
				xtype: 'compositefield', fieldLabel : '入段日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "inTime",  xtype: "my97date",format: "Y-m-d H:i",  
		             value : ZbglHoCase.getCurrentMonth(),
					width: 100},
					{
			    	    xtype:'label', text: '至：'
				    },
					{ id:"toWarehousingTime",name: "inTime", xtype: "my97date",format: "Y-m-d H:i",  
					width: 100}]
			}]
     	},{
     		columnWidth:.25, layout:'form', 
     		items:[ {
	         	xtype:'textfield',fieldLabel:'配属段',name:"dName"
	         },{
	         	xtype:'textfield',fieldLabel:'入段车次',name:"handOverTrainOrder"
	         }]
        },{
        	columnWidth:.15, layout:'form', 
     		items:[{
     			id:'train_to_go',
				xtype: 'Base_comboTree',hiddenName: 'trainToGo',
				fieldLabel: '入段去向',
				treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
				queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
				selectNodeModel: 'leaf'
    		}]
        }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			ZbglHoCase.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	ZbglHoCase.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("train_to_go").clearValue();
            	ZbglHoCase.searchParam = {};
			    ZbglHoCase.grid.store.load();
            }
        }]
	});
ZbglHoCase.grid.store.on("beforeload", function(){
		ZbglHoCase.searchParam = ZbglHoCase.searchForm.getForm().getValues();
		var searchParam = ZbglHoCase.searchParam;
		delete searchParam.inTime;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);	
		var startDate = "";
        var overDate = "";
        if(Ext.getCmp("inTime_Id").getValue() != ""){
        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d H:i');
        }
        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
        }
		this.baseParams.startDate = startDate;
		this.baseParams.overDate = overDate;
	});
//页面自适应布局
var viewport = new Ext.Viewport({
		   layout:'border',
    	   items:[{
    	      region:'north',
	    	  frame:true,
    	      collapsible :true,
    	      height:140,
    	      title:'查询',
    	      items:[ZbglHoCase.searchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      items:[ZbglHoCase.grid]
    	   }]});
});