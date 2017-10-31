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
    loadURL: ctx + '/mobile!getTrainCleaningTasks.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:[{   
            text:"确认", iconCls:"checkIcon", handler: function(){
                var grid = ZbglCleaning.grid;
	    		if(!$yd.isSelectedRecord(grid)) return;
	    		var data = grid.selModel.getSelections();
	    		if (data.length > 1) {
	    			MyExt.Msg.alert("只能选择一条记录");
		        	return;	        	
	    		}
	    		ZbglCleaning.win.show();
	    		ZbglCleaning.baseForm.getForm().reset();
	            ZbglCleaning.baseForm.getForm().loadRecord(data[0]);
            	ZbglCleaning.saveForm.getForm().reset();
            	Ext.getCmp("clean_combo").clearValue();
            	Ext.getCmp("jc_combo").clearValue();
            } 
        },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype: "datecolumn", format: "Y-m-d H:i"
		//searcher:{id:'inTime_Id',xtype:'my97date', my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, initNow: false, validator: ZbglCleaning.checkDate}
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
		header:'整备后去向', dataIndex:'toGo', editor:{  maxLength:100 }
	},{
		header:'交验交车人', dataIndex:'fromPersonName', editor:{  maxLength:100 }
	},{
		header:'交验接车人', dataIndex:'toPersonName', editor:{ maxLength:100 }
	},{
		header:'出段车次', dataIndex:'outOrder', editor:{  maxLength:50 }
	},{
		header:'备注', dataIndex:'remarks', searcher:{ xtype: "textfield" }
	}],
	toEditFn: function(grid, rowIndex, e){
		var record = this.store.getAt(rowIndex);;
		ZbglCleaning.win.show();
		ZbglCleaning.baseForm.getForm().reset();
        ZbglCleaning.baseForm.getForm().loadRecord(record);
    	ZbglCleaning.saveForm.getForm().reset();
    	Ext.getCmp("clean_combo").clearValue();
    	Ext.getCmp("jc_combo").clearValue();
	}
});
ZbglCleaning.searchForm = new Ext.form.FormPanel({
		labelWidth: ZbglCleaning.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        items: [{
        	columnWidth:.33, layout:'form', defaults: {
				width:ZbglCleaning.fieldWidth        		
        	},
			items:[{
				id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
				hiddenName: "trainTypeIDX",
				displayField: "shortName", valueField: "typeID",
				pageSize: 20, minListWidth: 200,   
				editable:false
			},{
				xtype: 'compositefield', fieldLabel : '入段日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "inTime",  xtype: "my97date",format: "Y-m-d H:i",  
		             value : ZbglCleaning.getCurrentMonth(),
					width: 140},
					{
			    	    xtype:'label', text: '至：'
				    },
					{ id:"toWarehousingTime",name: "inTime", xtype: "my97date",format: "Y-m-d H:i",  
					width: 140}]
			}]
		},{
			columnWidth:.33, layout:'form', defaults: {
				width:ZbglCleaning.fieldWidth        		
        	},
			items:[{
				id:"trainNo",xtype:'textfield',fieldLabel:'车号',name:"trainNo"
			}]
     	},{
     		columnWidth:.33, layout:'form', 
     		items:[ {
//				xtype:'textfield',fieldLabel:'站点',id:'siteName',name:"siteName"
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
            	ZbglCleaning.searchParam = {};
			    ZbglCleaning.grid.store.load();
            }
        }]
	});
ZbglCleaning.baseForm = new Ext.form.FormPanel({
	labelWidth: ZbglCleaning.labelWidth,
    border: false, layout: "column", style: "padding:10px",
    defaults:{
		xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5, 
		defaults:{
			style: 'border:none; background:none;', 
			xtype:"textfield", readOnly: true,
			anchor:"100%",width:ZbglCleaning.fieldWidth 
		}
	},
    items: [{
		items:[{
			id:"trainTypeShortName",xtype:'textfield',fieldLabel:'车型',name:"trainTypeShortName"
		},{
			id:"trainTypeIDX",xtype:'textfield',fieldLabel:'车型id',name:"trainTypeIDX",hidden:true
		},{
			xtype:'textfield',fieldLabel:'任务单id',name:"idx",hidden:true
		}]
	},{
		items:[{
			xtype:'textfield',fieldLabel:'车号',name:"trainNo"
		}]
 	}]
});
ZbglCleaning.saveForm = new Ext.form.FormPanel({
	labelWidth: ZbglCleaning.labelWidth,
    border: false, layout: "column", style: "padding:10px",
    items: [{
    	columnWidth:1, layout:'form', defaults: {
			width:ZbglCleaning.fieldWidth        		
    	},
		items:[{
	         	id:'clean_combo',
	         	fieldLabel:'保洁等级',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'cleaningLevel',
				dicttypeid:'JCZB_CLEAN_LEVEL',
				allowBlank:false,
				displayField:'dictname',valueField:'dictname'
	         },{
	         	id:'jc_combo',
	         	fieldLabel:'机车等级',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'trainLevel',
				dicttypeid:'JCZB_JC_LEVEL',
				allowBlank:false,
				displayField:'dictname',valueField:'dictname'
	         },{
				id:"remarks",xtype:'textarea',fieldLabel:'备注',name:"remarks",maxLength:100,width:300
			}]
	}]
});
// 【保洁操作处理】窗口
ZbglCleaning.win = new Ext.Window({
	title:"保洁操作",
	width:900, height:400, layout:"fit",
	closeAction:'hide',
	plain: true, modal: true,
	items:[{
		xtype:"panel",
		border: false,
		layout:"border",
		tbar:[],
		defaults:{layout:'fit'},
		items:[
			{
				title:"整备基本信息",
				region:"north",
				height:80,
				frame:true,
//					margins:"10px 0 0 0",
				collapseFirst: false, collapsible: true,
				items:[ZbglCleaning.baseForm]
			}, {
				region:"center", height:185, 
				frame: true,
				items:ZbglCleaning.saveForm
			}]
	}],
    buttonAlign:"center",
    buttons: [{
        text: "确认", iconCls: "checkIcon", handler: function(){
 			//表单验证是否通过
	        var form = ZbglCleaning.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        var data = form.getValues();
	        var data_v = ZbglCleaning.baseForm.getForm().getValues();
	        data.trainTypeShortName = data_v.trainTypeShortName ;
	        data.trainTypeIDX = data_v.trainTypeIDX ;
	        data.trainNo = data_v.trainNo ;
	        data.rdpIdx = data_v.idx ;
	        Ext.Ajax.request({
			        url: ctx + "/mobile!finishTrainCleaningTask.action",
			        params: {json : Ext.util.JSON.encode(data) },
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                ZbglCleaning.grid.store.reload(); 
			                ZbglCleaning.win.hide();
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			    });
        }
    },{
        text: "取消", iconCls: "deleteIcon", handler: function(){
        	ZbglCleaning.win.hide();
        }
    }]
})
ZbglCleaning.grid.store.on("beforeload", function(){
		ZbglCleaning.searchParam = ZbglCleaning.searchForm.getForm().getValues();
		var searchParam = ZbglCleaning.searchParam;
		
		var startDate = Ext.getCmp("inTime_Id").getValue();
		var endDate = Ext.getCmp("toWarehousingTime").getValue();
		searchParam.startDate = startDate;
		searchParam.endDate = endDate;
		delete searchParam.inTime;
		
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.searchJson = Ext.util.JSON.encode(searchParam);	
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