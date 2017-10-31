/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanTheDynamic');                       // 定义命名空间
	TrainWorkPlanTheDynamic.flag = '';
	TrainWorkPlanTheDynamic.recordIDX = '';
	TrainWorkPlanTheDynamic.idx = '';
	TrainWorkPlanTheDynamic.searchLabelWidth = 110;
	TrainWorkPlanTheDynamic.searchFieldWidth = 60;
	
	/*** 定义全局函数开始 ***/

	// 生成所有数据的动态
    TrainWorkPlanTheDynamic.planGenerateFn = function(){
		 // Ajax请求
		Ext.Msg.confirm("提示  ", "确认生成当日生产动态？  ", function(btn) {
			if ('yes' == btn) {
				Ext.Ajax.request({
					url: ctx + '/trainWorkPlanDynamic!insertTheDynamic.action',
					params:{planGenerateDate: TrainWorkPlanDynamic.planGenerateDate},
					//请求成功后的回调函数
				    success: function(response, options){
				    	 var result = Ext.util.JSON.decode(response.responseText);
				    	 if (result.errMsg == null && result.success == true) {
		               		alertSuccess();
							TrainWorkPlanDynamic.grid.store.reload();
			            } else {
			                alertFail(result.errMsg);
			            }
				        
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		});
	}
	// 提交所有的动态
    TrainWorkPlanTheDynamic.planSaveFn = function(){
		 // Ajax请求
		Ext.Msg.confirm("提示  ", "确认生成当日生产动态？  ", function(btn) {
			if ('yes' == btn) {
				Ext.Ajax.request({
					url: ctx + '/trainWorkPlanDynamic!saveTheDynamic.action',
					//请求成功后的回调函数
				    success: function(response, options){
				    	 var result = Ext.util.JSON.decode(response.responseText);
				    	 if (result.errMsg == null && result.success == true) {
		               		alertSuccess();
							TrainWorkPlanDynamic.grid.store.reload();
			            } else {
			                alertFail(result.errMsg);
			            }
				        
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		});
	}
	/*** 定义全局函数结束 ***/
	
	/*** 查询表单 start ***/

	TrainWorkPlanTheDynamic.searchForm = new Ext.form.FormPanel({
		layout:"column", border:false, style:"padding:10px",
	    align:'center',baseCls: "x-plain",
		defaults: { 
		    layout:'form', align:'center', baseCls: "x-plain", xtype: 'panel',columnWidth: 0.15,
		    border:false
	    },
		items:[{ labelWidth: 80, columnWidth: 0.4,
			 items: [{
				fieldLabel: "查询日期", name: 'planGenerateDate', xtype:"my97date",  width: 160,format: "Y-m-d", allowBlank: false		
			 }]
		},{ labelWidth: 1,
			items: [{ 
			 	text:'查询', iconCls:'searchIcon', xtype: 'button', width: 80,
				handler: function(){ 
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
			        var searchParam = form.getValues();
	                searchParam = MyJson.deleteBlankProp(searchParam);
//	                TrainWorkPlanDynamic.planGenerateDate = searchParam.planGenerateDate;
					TrainWorkPlanDynamic.grid.searchFn(searchParam); 
					WorkPlanWartoutTrain.grid.searchFn(searchParam); 
					TrainInplan.grid.searchFn(searchParam); 
					WorkPlanRepairReport.grid.searchFn(searchParam); 
					ExpandInformation.grid.searchFn(searchParam); 
				}	
			}]				
		},{  
			items: [{ 
					text:'重置', iconCls:'resetIcon', xtype: 'button', width: 80,
					handler: function(){ 				
						var form = TrainWorkPlanTheDynamic.searchForm.getForm();
		            	form.reset();
		            	var searchParam = form.getValues();
		            	TrainWorkPlanDynamic.grid.searchFn(searchParam); 
		            	WorkPlanWartoutTrain.grid.searchFn(searchParam); 
		            	TrainInplan.grid.searchFn(searchParam); 
		            	WorkPlanRepairReport.grid.searchFn(searchParam); 
		            	ExpandInformation.grid.searchFn(searchParam); 
				}
			}]				
		},{   
			items: [{ 
					text:'更新动态', iconCls:'editIcon', xtype: 'button', width: 80,
					handler: function(){ 				
						TrainWorkPlanTheDynamic.planGenerateFn();
				}
			}]				
		},{  
			items: [{ 
					text:'提交', iconCls:'saveIcon', xtype: 'button', width: 80,
					handler: function(){ 				
						TrainWorkPlanTheDynamic.planSaveFn();
				}
			}]				
	
	    }]

	});
	/*** 查询表单 end ***/

	/*** 界面布局 start ***/
	TrainWorkPlanTheDynamic.tabs = {
        region : 'center', xtype:"tabpanel",activeTab:0, bodyBorder: false, 
        items : [{            		            
        	title:"当日检修动态",
			layout:"fit",					
			items:TrainWorkPlanDynamic.grid,
			listeners: {
				activate: function() {
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
	       			var searchParam = form.getValues();
	       			TrainWorkPlanDynamic.grid.searchFn(searchParam); 
					TrainWorkPlanDynamic.grid.store.load();
				}
			}
        },{
			title:"修峻待离段机车",
			layout:"fit",					
			items:WorkPlanWartoutTrain.grid,
			listeners: {
				activate: function() {
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
	       			var searchParam = form.getValues();
	       			WorkPlanWartoutTrain.grid.searchFn(searchParam); 
//					WorkPlanWartoutTrain.grid.store.load();
				}
			}
        },{
			title:"入段计划",
			layout:"fit",					
			items:TrainInplan.grid,
			listeners: {
				activate: function() {
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
	       			var searchParam = form.getValues();
	       			TrainInplan.grid.searchFn(searchParam); 
				}
			}
        },{
			title:"任务统计",
			layout:"fit",					
			items:WorkPlanRepairReport.grid,
			listeners: {
				activate: function() {
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
	       			var searchParam = form.getValues();
	       			WorkPlanRepairReport.grid.searchFn(searchParam); 
				}
			}
        },{
			title:"重要信息维护",
			layout:"fit",					
			items:ExpandInformation.grid,
			listeners: {
				activate: function() {
					var form = TrainWorkPlanTheDynamic.searchForm.getForm();	
	       			var searchParam = form.getValues();
	       			ExpandInformation.grid.searchFn(searchParam); 
				}
			}
		}]      
	}
	
	TrainWorkPlanTheDynamic.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',height: 60, bodyBorder: false,
	        items:[TrainWorkPlanTheDynamic.searchForm], frame: true,  xtype: "panel"
	    },{
	         region : 'center', layout : 'border', bodyBorder: false, autoScoll: true,
	         items : TrainWorkPlanTheDynamic.tabs
	    }]
	};
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainWorkPlanTheDynamic.panel });

	
});