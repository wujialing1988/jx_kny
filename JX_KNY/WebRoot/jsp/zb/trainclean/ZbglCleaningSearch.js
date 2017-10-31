/**
 * 机车保洁记录 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglCleaning');                       //定义命名空间
ZbglCleaning.searchParam = {};
ZbglCleaning.labelWidth = 70;
//最近一个月
ZbglCleaning.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	return MonthFirstDay.format('Y-m-d');
}
ZbglCleaning.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglCleaning!getCleaningList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:[{   
            text:"打印", iconCls:"printerIcon", handler: function(){
                var form = ZbglCleaning.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("inTime_Id").getValue() != ""){
		        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d H:i');
		        }
		        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
		        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/trainclean/ZbglCleaning.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeShortName=" + data.trainTypeShortName +
				"&trainNo=" + data.trainNo + "&cleaningLevel=" + data.cleaningLevel + "&trainLevel=" + data.trainLevel + "&siteName=" + data.siteName;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车保洁记录"));
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
		header:'配属段', dataIndex:'dName', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype: "datecolumn", format: "Y-m-d H:i", 
		searcher:{id:'inTime_Id',xtype:'my97date', my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, initNow: false, validator: ZbglCleaning.checkDate}
	},{
		header:'入段去向', dataIndex:'trainToGo', editor:{},
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		},
		searcher:{xtype: 'textfield'}, width: 100
	},{
		header:'保洁等级', dataIndex:'cleaningLevel', editor:{  maxLength:100 }
	},{
		header:'机车等级', dataIndex:'trainLevel', editor:{  maxLength:100 }
	},{
		header:'保洁时间', dataIndex:'cleaningTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'保洁人ID', dataIndex:'dutyPersonId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'保洁人', dataIndex:'dutyPersonName', editor:{  maxLength:50 }
	},{
		header:'备注', dataIndex:'remarks', searcher:{ xtype: "textfield" }
	},{
		header:'站点', dataIndex:'siteName', searcher:{ xtype: "textfield" }
	}]
});
ZbglCleaning.searchForm = new Ext.form.FormPanel({
		labelWidth: ZbglCleaning.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        items: [{
        	columnWidth:.25, layout:'form', defaults: {
				width:ZbglCleaning.fieldWidth        		
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
			columnWidth:.3, layout:'form', defaults: {
				width:ZbglCleaning.fieldWidth        		
        	},
			items:[{
				id:"trainNo",xtype:'textfield',fieldLabel:'车号',name:"trainNo"
			},{
				xtype: 'compositefield', fieldLabel : '入段日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "inTime",  xtype: "my97date",format: "Y-m-d",  
		             value : ZbglCleaning.getCurrentMonth(),
					width: 100},
					{
			    	    xtype:'label', text: '至：'
				    },
					{ id:"toWarehousingTime",name: "inTime", xtype: "my97date",format: "Y-m-d",  
					width: 100}]
			}]
     	},{
     		columnWidth:.25, layout:'form', 
     		items:[ {
	         	id:'clean_combo',
	         	fieldLabel:'保洁等级',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'cleaningLevel',
				dicttypeid:'JCZB_CLEAN_LEVEL',
				displayField:'dictname',valueField:'dictname'
	         },{
	         	id:'jc_combo',
	         	fieldLabel:'机车等级',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'trainLevel',
				dicttypeid:'JCZB_JC_LEVEL',
				displayField:'dictname',valueField:'dictname'
	         }]
        }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			ZbglCleaning.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	ZbglCleaning.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("clean_combo").clearValue();
            	Ext.getCmp("jc_combo").clearValue();
            	ZbglCleaning.searchParam = {};
			    ZbglCleaning.grid.store.load();
            }
        }]
	});
ZbglCleaning.grid.un('rowdblclick', ZbglCleaning.grid.toEditFn, ZbglCleaning.grid);
ZbglCleaning.grid.store.on("beforeload", function(){
		ZbglCleaning.searchParam = ZbglCleaning.searchForm.getForm().getValues();
		var searchParam = ZbglCleaning.searchParam;
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
    	      items:[ZbglCleaning.searchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      items:[ZbglCleaning.grid]
    	   }]});
});