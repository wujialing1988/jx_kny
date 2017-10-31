0
/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('FirstNodeCaseApplyConfirm');
	
	/** **************** 定义全局变量开始 **************** */
	FirstNodeCaseApplyConfirm.labelWidth = 100;
	FirstNodeCaseApplyConfirm.fieldWidth = 140;
	FirstNodeCaseApplyConfirm.type = '';
	FirstNodeCaseApplyConfirm.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});	
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 同意或取消车间调度申请
    FirstNodeCaseApplyConfirm.confirm = function(idx, nodeIDX, type){
		FirstNodeCaseApplyConfirm.type = type;
    	var opinionsForm  = Ext.getCmp('opinions_form').getForm();
    	if(EDIT_STATUS_ON == type ){
    		opinionsForm.findField('opinions').setValue('同意');
    	}else {
    		opinionsForm.findField('opinions').setValue('');
    	}
		FirstNodeCaseApplyConfirm.opinionsWin.show();
   }
   	/** **************** 定义全局函数开始 **************** */
   
	// 上方表格【父节点延误申请记录表格】定义
	FirstNodeCaseApplyConfirm.grid = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/jobProcessNodeUpdateApply!findFirstNodeApplyList.action',
		viewConfig: null,
		page:false, singleSelect:true, remoteSort: false,
		tbar:[{	text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						self.location.reload();
					}							
				}],
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden:true
		}, {
			dataIndex: 'nodeName', header: '节点名称', width: 110
		}, {
			dataIndex: 'nodeIDX', header: '节点idx', hidden:true	
		}, {
			dataIndex: 'planBeginTime', header: '原计划开始', xtype: "datecolumn", format:"m-d H:i", width: 80
		}, {
			dataIndex: 'planEndTime', header: '原计划结束', xtype: "datecolumn", format:"m-d H:i", width: 80
		}, {
			dataIndex: 'newPlanBeginTime', header: '申请开始', xtype: "datecolumn", format:"m-d H:i", width: 80
		}, {
			dataIndex: 'newPlanEndTime', header: '申请结束', xtype: "datecolumn", format:"m-d H:i", width: 80
		}, {	
			dataIndex: 'reason', header: '申请说明', width: 240,renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var result = "" ;
				metaData.attr = 'style="white-space:normal;"';
				var empName = record.get('empName');
				if(Ext.isEmpty(empName)){
					result = value ;
				}else{
					result = empName + ":" + value;
				}
				return result;
			}
		}, {	
			dataIndex: 'empName', header: '申请人', width: 200,hidden:true
//			renderer: function(value, metaData, record, rowIndex, colIndex, store){
//			if (!Ext.isEmpty(value)) {			 
//				var cfg = {
//					        scope: this, url: ctx + '/jobProcessNodeUpdateApply!getWorkempName.action',
//		        			params: {operator: value},
//					        success: function(response, options){
//					            var result = Ext.util.JSON.decode(response.responseText);
//					            if (result.defInfo != null) {
//					              return result.defInfo.empname;
//					            }
//					        }
//					    };
//		    	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));	
//			}
//			else return '';
//			}
		}, {	
			dataIndex:"opinions", header: '审核意见', width: 240,renderer: function(value, metaData, record, rowIndex, colIndex, store){
				metaData.attr = 'style="white-space:normal;"';
				return value;
			}
		 },{
	  		 dataIndex: 'editStatus',header: '操作', width: 150,
			 renderer: function(value, metaData, record, rowIndex, colIndex, store){
	  		 	var idx = record.get('idx');
	  		 	var nodeIDX = record.get('nodeIDX');
	  		 	var newPlanBeginTime = record.get('newPlanBeginTime');
	  		 	var newPlanEndTime = record.get('newPlanEndTime');
				var html = "";
				if(EDIT_STATUS_WAIT == value){
		    		html = "<span><a href='#' onclick = 'FirstNodeCaseApplyConfirm.confirm(\"" + idx +													// [0]
						"\",\""+ nodeIDX + "\",\""+ EDIT_STATUS_ON + "\")'>同意</a></span> &nbsp&nbsp ";
		    		html += "<span><a href='#' onclick = 'FirstNodeCaseApplyConfirm.confirm(\""+ idx +													// [0]
						"\",\""+ nodeIDX + "\",\""+ EDIT_STATUS_UN + "\")'>拒绝</a></span>";
		            return html; 
				}
				return html;
	    	   }	
		}]
	});
	
	// Grid数据加载前的查询条件传递 
	FirstNodeCaseApplyConfirm.grid.store.on('beforeload', function(){
		this.baseParams.workPlanIDX = FirstNodeCaseApplyConfirm.workPlanIDX;
	});
	
	// 延期申请列表窗口
	FirstNodeCaseApplyConfirm.showWin = new Ext.Window({
		title:"申请延期确认",padding:"10px",
		modal: true,
		closeAction: 'hide',
		width:1020, height:460,
		layout:"border",
		items:[{ id:'traininfo_form',
			xtype:'form', region:'north', height:60, padding:"10px",
			labelWidth: FirstNodeCaseApplyConfirm.labelWidth - 40,
			frame:true, layout:"column",
			defaults: {
				layout:"form",
				columnWidth:0.25,
				defaults:{
					xtype: 'textfield', anchor:'100%', readOnly:true, 
					style:'background:none;border:none;font-weight:bold;'
				}
			},
			items: [{
				items:[{
					name:'trainTypeShortName', fieldLabel: '车型'
				}]
			}, {
				items:[{
					name:'trainNo', fieldLabel: '车号'
				}]
			}, {
				items:[{
					name:'repairClassTimeName', fieldLabel: '修程修次'
				}]
			}, {
				items:[{
					name:'dNAME', fieldLabel: '配属段'
				}]
			}]
		}, {
			region:"center", layout: 'fit',
			items: [FirstNodeCaseApplyConfirm.grid]
		}],
		listeners: {
			show: function(me, eOpts) {
				var form = Ext.getCmp('traininfo_form');
				var trainInfo = FirstNodeCaseApplyConfirm.trainInfo.split('_');
				form.find('name', 'trainTypeShortName')[0].setValue(trainInfo[0]);
				form.find('name', 'trainNo')[0].setValue(trainInfo[1]);
				form.find('name', 'repairClassTimeName')[0].setValue(trainInfo[2]+"|"+trainInfo[3]);
				form.find('name', 'dNAME')[0].setValue(trainInfo[4]);
				// 用于标识是否执行了“原因”的编辑
				FirstNodeCaseApplyConfirm.isEdited = false;
				FirstNodeCaseApplyConfirm.grid.store.load();
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	// 申请确认窗口
	FirstNodeCaseApplyConfirm.opinionsWin = new Ext.Window({
		title:"延期确认",padding:"10px",
		modal: true,
		closeAction: 'hide',
		width:500, height:260,
		layout:"fit",
		items:[{
			id:'opinions_form',		
			xtype:'form', padding:"10px",
			labelWidth: FirstNodeCaseApplyConfirm.labelWidth,
			frame:true, layout:"column",
			defaults: {
				layout:"form",
				columnWidth:1			
			},
			items: [{
				items:[{
					name:'opinions', xtype:"textarea", fieldLabel:"审核意见",  disabled:false,
					 anchor:'90%', allowBlank:false, height:100,
					width:430, maxLength:150
				}]
			}]
		}],
		listeners: {
			show: function(me, eOpts) {
				
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'addIcon', handler: function() {
				var opinionsForm  = Ext.getCmp('opinions_form').getForm();
				if (!opinionsForm.isValid()) {			
					MyExt.Msg.alert("请填写审核意见！");
					return;    
				}
				var sm = FirstNodeCaseApplyConfirm.grid.getSelectionModel();
				if (sm.getCount()<= 0 ) {
					MyExt.Msg.alert("请选择一条数据！");
					return;    
				}
				var records = sm.getSelections();				
				data = records[0].data;	
		    	data.opinions = opinionsForm.findField('opinions').getValue();
		    	//			    	data.approvalEmpID = empId;
//			    	data.approvalEmpName = empName;
			    	data.editStatus = FirstNodeCaseApplyConfirm.type;
			   
		    	if (FirstNodeCaseApplyConfirm.loadMask) FirstNodeCaseApplyConfirm.loadMask.show();
				   	var cfg = {
				        url: ctx + "/jobProcessNodeUpdateApply!updateFirstNodeApply.action",
			 			jsonData: data,
				        timeout: 600000,
				        success: function(response, options){
							if(FirstNodeCaseApplyConfirm.loadMask) FirstNodeCaseApplyConfirm.loadMask.hide();
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null && result.success == true) {
				                alertSuccess();
				                Ext.getCmp('opinions_form').getForm().reset();
				            	FirstNodeCaseApplyConfirm.grid.store.load(); // 重新加载申请列表
				            	FirstNodeCaseApplyConfirm.opinionsWin.hide();
				            } else {
				                alertFail(result.errMsg);
				            }
				        },
				        failure: function(response, options){	
				        	if(FirstNodeCaseApplyConfirm.loadMask) FirstNodeCaseApplyConfirm.loadMask.hide();
					        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					    }
				    };
				    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				}
		},{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
})