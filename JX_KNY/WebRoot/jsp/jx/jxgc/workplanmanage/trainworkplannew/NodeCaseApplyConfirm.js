0
/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('NodeCaseApplyConfirm');
	
	/** **************** 定义全局变量开始 **************** */
	NodeCaseApplyConfirm.labelWidth = 100;
	NodeCaseApplyConfirm.fieldWidth = 140;
	NodeCaseApplyConfirm.parentIDX = '';
	NodeCaseApplyConfirm.type = '';
	NodeCaseApplyConfirm.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});	
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 同意或取消车间调度申请
    NodeCaseApplyConfirm.confirm = function(idx, nodeIDX, type){
		NodeCaseApplyConfirm.type = type;
    	var opinionsForm  = Ext.getCmp('opinions_form').getForm();
    	if(EDIT_STATUS_ON == type ){
    		opinionsForm.findField('opinions').setValue('同意');
    	}else {
    		opinionsForm.findField('opinions').setValue('');
    	}
		NodeCaseApplyConfirm.opinionsWin.show();
   }
	 // 段调度申请审批
    NodeCaseApplyConfirm.approve = function(idx){
    	NodeCaseApplyConfirm.approveWin.show();
    }
   	/** **************** 定义全局函数开始 **************** */
   
	// 上方表格【子节点延误申请记录表格】定义
	NodeCaseApplyConfirm.grid = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/jobProcessNodeUpdateApply!findChildrenNodeApplyList.action',
		viewConfig: null,
		page:false, singleSelect:true, remoteSort: false,
		tbar:[{	text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						NodeCaseApplyConfirm.grid.store.reload();
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
			dataIndex: 'reason', header: '申请说明', width: 170,renderer: function(value, metaData, record, rowIndex, colIndex, store){
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
			dataIndex:"opinions", header: '审核意见', width: 170,renderer: function(value, metaData, record, rowIndex, colIndex, store){
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
				if(EDIT_STATUS_WAIT == NodeCaseApplyConfirm.editStatus && EDIT_STATUS_WAIT == value){
					html += "<span>待审核</span> &nbsp&nbsp ";
				}else if(EDIT_STATUS_WAIT == value){
		    		html = "<span><a href='#' onclick = 'NodeCaseApplyConfirm.confirm(\"" + idx +													// [0]
						"\",\""+ nodeIDX + "\",\""+ EDIT_STATUS_ON + "\")'>同意</a></span> &nbsp&nbsp ";
		    		html += "<span><a href='#' onclick = 'NodeCaseApplyConfirm.approve(\"" + idx +													// [0]
							 "\")'>申请审批</a></span> &nbsp&nbsp ";
		    		html += "<span><a href='#' onclick = 'NodeCaseApplyConfirm.confirm(\""+ idx +													// [0]
						"\",\""+ nodeIDX + "\",\""+ EDIT_STATUS_UN + "\")'>拒绝</a></span>";
		            return html; 
				} 
				return html;
	    	   }	
		}]
	});
	
	// Grid数据加载前的查询条件传递 
	NodeCaseApplyConfirm.grid.store.on('beforeload', function(){
		this.baseParams.parentIDX = NodeCaseApplyConfirm.parentIDX;
	});
	
	// 延期申请列表窗口
	NodeCaseApplyConfirm.showWin = new Ext.Window({
		title:"申请延期确认",padding:"10px",
		modal: true,
		closeAction: 'hide',
		width:1020, height:460,
		layout:"border",
		items:[{ id:'traininfo_form',
			xtype:'form', region:'north', height:60, padding:"10px",
			labelWidth: NodeCaseApplyConfirm.labelWidth - 40,
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
//		},{	id:'opinions_form',		
//			xtype:'form', region:"south", height:100,  padding:"10px",
//			labelWidth: NodeCaseApplyConfirm.labelWidth,
//			frame:true, layout:"column",
//			defaults: {
//				layout:"form",
//				columnWidth:1			
//			},
//			items: [{
//				items:[{
//					name:'opinions', xtype:"textarea", fieldLabel:"审核意见",  disabled:false,
//					 anchor:'80%', allowBlank:false, 
//					width:417, maxLength:200
//				}]
//			}]
		}, {
			region:"center", layout: 'fit',
			items: [NodeCaseApplyConfirm.grid]
		}],
		listeners: {
			show: function(me, eOpts) {
				var form = Ext.getCmp('traininfo_form');
				var trainInfo = NodeCaseApplyConfirm.trainInfo.split('_');
				form.find('name', 'trainTypeShortName')[0].setValue(trainInfo[0]);
				form.find('name', 'trainNo')[0].setValue(trainInfo[1]);
				form.find('name', 'repairClassTimeName')[0].setValue(trainInfo[2]+"|"+trainInfo[3]);
				form.find('name', 'dNAME')[0].setValue(trainInfo[4]);
				// 用于标识是否执行了“原因”的编辑
				NodeCaseApplyConfirm.isEdited = false;
				NodeCaseApplyConfirm.grid.store.load();
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
	NodeCaseApplyConfirm.opinionsWin = new Ext.Window({
		title:"延期确认",padding:"10px",
		modal: true,
		closeAction: 'hide',
		width:500, height:260,
		layout:"fit",
		items:[{
			id:'opinions_form',		
			xtype:'form', padding:"10px",
			labelWidth: NodeCaseApplyConfirm.labelWidth,
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
				var sm = NodeCaseApplyConfirm.grid.getSelectionModel();
				if (sm.getCount()<= 0 ) {
					MyExt.Msg.alert("请选择一条数据！");
					return;    
				}
				var records = sm.getSelections();				
				data = records[0].data;	
//			    	data.idx = idx;
//			    	data.nodeIDX = nodeIDX;
		    	data.opinions = opinionsForm.findField('opinions').getValue();
		    	//			    	data.approvalEmpID = empId;
//			    	data.approvalEmpName = empName;
			    	data.editStatus = NodeCaseApplyConfirm.type;
			   
		    	if (NodeCaseApplyConfirm.loadMask) NodeCaseApplyConfirm.loadMask.show();
				   	var cfg = {
				        url: ctx + "/jobProcessNodeUpdateApply!updateConfirmApply.action",
			 			jsonData: data,
				        timeout: 600000,
				        success: function(response, options){
							if(NodeCaseApplyConfirm.loadMask) NodeCaseApplyConfirm.loadMask.hide();
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null && result.success == true) {
				                alertSuccess();
				                Ext.getCmp('opinions_form').getForm().reset();
				            	NodeCaseApplyConfirm.grid.store.load(); // 重新加载申请列表
				            	TrainWorkPlanEdit.store.load();
				            	NodeCaseApplyConfirm.opinionsWin.hide();
				            } else {
				                alertFail(result.errMsg);
				            }
				        },
				        failure: function(response, options){	
				        	if(NodeCaseApplyConfirm.loadMask) NodeCaseApplyConfirm.loadMask.hide();
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
	
	
	// 父节点申请延期
	NodeCaseApplyConfirm.approveWin = new Ext.Window({
		title:"审批申请",padding:"10px",
		modal: true,
		closeAction: 'hide',
		width:500, height:260,
		layout:"fit",
		items:[{
			id:'approve_form',		
			xtype:'form', padding:"10px",
			labelWidth: NodeCaseApplyConfirm.labelWidth,
			frame:true, layout:"column",
			defaults: {
				layout:"form",
				columnWidth:1			
			},
			items: [{
				items:[{
					name:'reason', xtype:"textarea", fieldLabel:"申请说明",  disabled:false,
					anchor:'90%', allowBlank:false, height:100,
					width:430, maxLength:150
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'addIcon', handler: function() {
				var approveForm  = Ext.getCmp('approve_form').getForm();
				if (!approveForm.isValid()) {			
					MyExt.Msg.alert("请填写申请说明！");
					return;    
				}
				var sm = NodeCaseApplyConfirm.grid.getSelectionModel();
				if (sm.getCount()<= 0 ) {
					MyExt.Msg.alert("请选择一条数据！");
					return;    
				}
				var records = sm.getSelections();				
				data = records[0].data;	
				 // 段调度申请延期审批
		    	var cfg = {
			        url: ctx + '/jobProcessNodeUpdateApply!saveApproveParentNode.action',
			        params: {idx: data.idx, reason: approveForm.findField('reason').getValue()},
			        success: function(response, options){
			        	if(NodeCaseApplyConfirm.loadMask) NodeCaseApplyConfirm.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
    	            		NodeCaseApplyConfirm.editStatus = EDIT_STATUS_WAIT;
    	            		NodeCaseApplyConfirm.grid.store.load();
				            TrainWorkPlanEdit.store.reload();
				            NodeCaseApplyConfirm.approveWin.hide();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
			        }
			    };
			    Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
			        if(btn != 'yes')    return;
					NodeCaseApplyConfirm.loadMask.show();
			        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			    });
				}
		},{
			text: '取消', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	// 显示申请列表
	NodeCaseApplyConfirm.showApplyWin = function(parentIDX,trainInfo,editStatus){
		NodeCaseApplyConfirm.trainInfo = trainInfo;
		NodeCaseApplyConfirm.parentIDX = parentIDX; 
		NodeCaseApplyConfirm.editStatus = editStatus;
		NodeCaseApplyConfirm.showWin.show();
	}
	
})