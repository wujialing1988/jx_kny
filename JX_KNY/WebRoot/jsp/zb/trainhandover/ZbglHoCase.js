/**
 * 机车保洁记录 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoCase');                       //定义命名空间
ZbglHoCase.searchParam = {};
ZbglHoCase.labelWidth = 70;
//20160815 林欢 由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
//ZbglHoCase.isDoJt6Flag = null;
//ZbglHoCase.isDoNumFlag = null;
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
    loadURL: ctx + '/mobile!getTrainHandOverTasks.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:[{   
            text:"确认", iconCls:"checkIcon", handler: function(){
                var grid = ZbglHoCase.grid;
	    		if(!$yd.isSelectedRecord(grid)) return;
	    		var data = grid.selModel.getSelections();
	    		if (data.length > 1) {
	    			MyExt.Msg.alert("只能选择一条记录");
		        	return;	        	
	    		}
	    		ZbglHoCase.win.show();
	    		ZbglHoCase.baseForm.getForm().reset();
	            ZbglHoCase.baseForm.getForm().loadRecord(data[0]);
	            HandOverOperateForm.infoForm.getForm().reset();
//	            Ext.getCmp("fromPersonId_Id").clearValue();
	            HandOverModelList.grid.store.load();
	            
	            var record = data[0] ;
	            //20160815 林欢 由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
	            //赋值标示字段
		        //ZbglHoCase.isDoJt6Flag = record.json.isDoJt6Flag;
		        //ZbglHoCase.isDoNumFlag = record.json.isDoNumFlag;
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
		header:'整备后去向', dataIndex:'toGo', editor:{  maxLength:100 },
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		}
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
		var record = this.store.getAt(rowIndex);
		ZbglHoCase.win.show();
		ZbglHoCase.baseForm.getForm().reset();
        ZbglHoCase.baseForm.getForm().loadRecord(record);
        HandOverOperateForm.infoForm.getForm().reset();
//        Ext.getCmp("fromPersonId_Id").clearValue();
        HandOverModelList.grid.store.load();
        
        //20160815 林欢 由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
        //赋值标示字段
        //ZbglHoCase.isDoJt6Flag = record.json.isDoJt6Flag;
        //ZbglHoCase.isDoNumFlag = record.json.isDoNumFlag;
        
	}
});
ZbglHoCase.searchForm = new Ext.form.FormPanel({
		labelWidth: ZbglHoCase.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        items: [{
        	columnWidth:.33, layout:'form', defaults: {
				width:ZbglHoCase.fieldWidth        		
        	},
			items:[{
				id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
				hiddenName: "trainTypeIDX",
				displayField: "shortName", valueField: "typeID",
				pageSize: 20, minListWidth: 200,   
				editable:false
			},{
     			id:'train_to_go',
				xtype: 'Base_comboTree',hiddenName: 'trainToGo',
				fieldLabel: '入段去向',
				treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
				queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
				selectNodeModel: 'leaf'
    		}]
		},{
			columnWidth:.33, layout:'form', defaults: {
				width:ZbglHoCase.fieldWidth        		
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
ZbglHoCase.baseForm = new Ext.form.FormPanel({
	labelWidth: ZbglHoCase.labelWidth,
    border: false, layout: "column", style: "padding:10px",
    defaults:{
		xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5, 
		defaults:{
			style: 'border:none; background:none;', 
			xtype:"textfield", readOnly: true,
			anchor:"100%",width:ZbglHoCase.fieldWidth 
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
// 【保洁操作处理】窗口
ZbglHoCase.win = new Ext.Window({
	title:"交接操作",
	width:900, height:450, layout:"fit",
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
				items:[ZbglHoCase.baseForm]
			}, {
				region:"center", 
				frame: true,
				items:[{
			            	layout: 'fit', collapsible:false, frame: true,
							items: HandOverOperateForm.infoForm, autoHeight: true
						},{
				    		xtype: "fieldset",
				    		title: "交接情况",
				    		autoHeight: true,   		
				    		items: {
				    			layout: "fit", height: 130, autoScroll: true,
								items:HandOverModelList.grid
				    		}
						},{
				    		xtype: "fieldset", 
				    		title: "", 
				    		layout: "form",
				    		autoHeight: true,
				    		items: HandOverOperateForm.confirmXCForm
			            }]
			}]
	}],
    buttonAlign:"center",
    buttons: [{
    	id:"comitHoCase_idx",
        text: "确认", iconCls: "checkIcon", handler: function(){
        
 			//表单验证是否通过
	        var form = HandOverOperateForm.infoForm.getForm(); 
	        if (!form.isValid()) return;
	        var data = form.getValues();
	        var data_v = ZbglHoCase.baseForm.getForm().getValues();
	        var data_r = HandOverOperateForm.confirmXCForm.getForm().getValues();
	        data.trainTypeShortName = data_v.trainTypeShortName ;
	        data.trainTypeIDX = data_v.trainTypeIDX ;
	        data.trainNo = data_v.trainNo ;
	        data.rdpIdx = data_v.idx ;
	        data.remarks = data_r.remarks ;
	        
	        //判断是否选择了做范围活
	        if(!data_r.isDoZbglRdpWi){
	        	MyExt.Msg.alert("请选择是否做范围活！");
				return;
	        }
	        
			//20160815 林欢 由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
			/*
			if(data_r.isDoZbglRdpWi == 0 && ZbglHoCase.isDoJt6Flag){
	        	MyExt.Msg.alert("该整备单下还有jt6提票未处理,必须做范围活！");
				return;
	        }
			
			//不包含本次，例如，配置的是3次，那么就是前3次，本次算第4次
			if(data_r.isDoZbglRdpWi == 0 && ZbglHoCase.isDoNumFlag){
	        	MyExt.Msg.alert("该车型车号未在规定整备单次数中做过范围活,必须做范围活！");
				return;
	        }*/
	        
	        var count = HandOverModelList.grid.store.getTotalCount();
			var datas = new Array();
			if(count > 0){
				for (var i = 0; i < count; i++) {
					var data_s = {} ;
					data_s = HandOverModelList.grid.store.getAt(i).data;
					if(Ext.isEmpty(data_s.handOverItemStatus) && Ext.isEmpty(data_s.handOverResultDesc)){
						MyExt.Msg.alert("请填写情况记录！");
						return;
					}
					datas.push(data_s);
				}
			}
			
			//点击一次的时候禁用
			Ext.getCmp("comitHoCase_idx").disable();
			
	        Ext.Ajax.request({
			        url: ctx + "/mobile!finishHoTask.action",
			        params: {rdpJson : Ext.util.JSON.encode(data),itemJson : Ext.util.JSON.encode(datas),isDoZbglRdpWi:data_r.isDoZbglRdpWi },
			        success: function(response, options){
			        	//点击一次的时候禁用
						Ext.getCmp("comitHoCase_idx").enable();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                ZbglHoCase.grid.store.reload(); 
			                ZbglHoCase.win.hide();
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			        	//点击一次的时候禁用
						Ext.getCmp("comitHoCase_idx").enable();
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			    });
        }
    },{
        text: "取消", iconCls: "deleteIcon", handler: function(){
        	ZbglHoCase.win.hide();
        }
    }]
})
ZbglHoCase.grid.store.on("beforeload", function(){
		ZbglHoCase.searchParam = ZbglHoCase.searchForm.getForm().getValues();
		var searchParam = ZbglHoCase.searchParam;
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
    	      items:[ZbglHoCase.searchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      items:[ZbglHoCase.grid]
    	   }]});
});