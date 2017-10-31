/**
 * 配件检修质量检验_必检 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpQcCheckProcess');			// 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpQcCheckProcess.labelWidth = 80;
	PartsRdpQcCheckProcess.fieldWidth = 140;
	PartsRdpQcCheckProcess.qcItemNo = "";				// 当前处理的质量检验项编码
	PartsRdpQcCheckProcess.qcItemName = "";				// 当前处理的质量检验项名称
	PartsRdpQcCheckProcess.rdpIDX = "";					// 作业主键
	PartsRdpQcCheckProcess.rdpNodeIdx ="";				//作业节点主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数结束 ************** */
	// 签名提交
	PartsRdpQcCheckProcess.signFn = function(rdpRecordCardIDXs, qCItemNo, qRResult, afterSignFn) {
		self.loadMask.show();
		Ext.Ajax.request({
			scope: PartsRdpQcCheckProcess.grid,
			url: ctx + '/partsRdpQR!signAndSubmit.action',
			jsonData:rdpRecordCardIDXs,
			params:{
				qCItemNo:qCItemNo, 
				qRResult:qRResult
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            // 隐藏签名提交窗口
		            PartsRdpQcCheckProcess.finishWin.hide();
		            this.store.reload({
		            	// 记录加载成功后的回调函数
		            	callback:function(){
				            if (afterSignFn) {
				            	afterSignFn(PartsRdpQcCheckSub.index);
				            }
		           		}
		            }); 
		        } else {                           //操作失败
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
	// 返修
	PartsRdpQcCheckProcess.backFn = function(rdpRecordCardIDXs, qCItemNo, afterBackFn) {
		self.loadMask.show();
		Ext.Ajax.request({
			scope: PartsRdpQcCheckProcess.grid,
			url: ctx + '/partsRdpQR!updateToBack.action',
			jsonData:rdpRecordCardIDXs,
			params:{
				qCItemNo:qCItemNo
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload({
		            	// 记录加载成功后的回调函数
		            	callback:function(){
				            if (afterBackFn) {
				            	afterBackFn(PartsRdpQcCheckSub.index);
				            }
		           		}
		            }); 
		        } else {                           //操作失败
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
	/** ************** 定义全局函数开始 ************** */
		
	/** ************** 定义配件检修作业计划单信息表单开始 ************** */
	PartsRdpQcCheckProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:PartsRdpQcCheckProcess.labelWidth,
		border:false, baseCls:'x-plain',
		labelAlign:"left",
		layout:"column",
		bodyStyle:"padding:0 10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.175, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
			labelWidth:PartsRdpQcCheckProcess.labelWidth - 10,
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}, {
				fieldLabel:"下车车型号", name:"trainType"
			}]
		}, {
			labelWidth:PartsRdpQcCheckProcess.labelWidth - 10,
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}, {
				fieldLabel:"下车修程", name:"repair"
			}]
		}, {
			columnWidth:0.4,
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}, {
				fieldLabel:"计划开始时间", name:"planStartTime"
			}]
		}, {
			columnWidth:0.25,
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}, {
				fieldLabel:"计划结束时间", name:"planEndTime"
			}]
		}]
	})
	/** ************** 定义配件检修作业计划单信息表单结束 ************** */
	
	/** ************** 定义【签名提交】窗口开始 ************** */
	PartsRdpQcCheckProcess.finishWin = new Ext.Window({
		title:"签名提交",
		iconCls:'edit1Icon',
		width:518, height:161,
		plain:true, modal:true,
		closeAction:'hide',
		layout:"fit",
		items:[{
			xtype:"form",
			border:false, baseCls:'x-plain',
			labelWidth:100,
			labelAlign:"left",
			layout:"form",
			padding:"10px",
			items:[{
				xtype:"textarea",
				name:'qRResult',
				fieldLabel:"检验结果", maxLength: 50,
				anchor:"100%" 
			}]
		}],
		listeners:{
			hide: function(window) {
				window.find('xtype', 'form')[0].getForm().reset();
			}
		},
		buttonAlign:'center',
		buttons:[{
			text:'确认', iconCls:'yesIcon', handler: function() {
				var form = PartsRdpQcCheckProcess.finishWin.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var sm = PartsRdpQcCheckProcess.grid.getSelectionModel();
				var records = sm.getSelections();
				var rdpRecordCardIDXs = [];
				for (var i = 0; i < records.length; i++) {
					rdpRecordCardIDXs.push(records[i].get('idx'));
				}
				var qRResult = PartsRdpQcCheckProcess.finishWin.find('name', 'qRResult')[0].getValue();
				PartsRdpQcCheckProcess.signFn(rdpRecordCardIDXs, PartsRdpQcCheckProcess.qcItemNo, qRResult);
			}
		}, {
			text:'取消', iconCls:'cancelIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** ************** 定义【签名提交】窗口结束 ************** */
	
	/** ************** 定义【提票】窗口开始 ************** */
	PartsRdpQcCheckProcess.insertNoticeWin = new Ext.Window({
		title:"提票",
		iconCls:'page_edit_1Icon',
		width:518, height:221,
		plain:true, modal:true,
		closeAction:'hide',
		layout:"fit",
		items:[{
			xtype:"form",
			border:false, baseCls:'x-plain',
			labelWidth:50,
			labelAlign:"left",
			layout:"form",
			padding:"10px",
			items:[{
				xtype:'textfield',
				fieldLabel:'编号', maxLength: 50,
				allowBlank: false,
				name:'noticeNo',
				anchor:"80%" 
			}, {
				xtype:"textarea",
				name:'noticeDesc',
				allowBlank: false,
				height:100,
				fieldLabel:"描述", maxLength: 500,
				anchor:"100%" 
			}]
		}],
		buttonAlign:'center',
		buttons:[{
			text:'确认', iconCls:'yesIcon', handler: function() {
				var form = PartsRdpQcCheckProcess.insertNoticeWin.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				data.rdpIDX = PartsRdpQcCheckProcess.rdpIDX;
				data.rdpNodeIDX = PartsRdpQcCheckProcess.rdpNodeIdx;
				self.loadMask.show();
				Ext.Ajax.request({
					scope: PartsRdpQcCheckProcess.grid,
					url: ctx + '/partsRdpNotice!submitNotice.action',
					jsonData:data,
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 隐藏【签名提交】处理窗口
				            PartsRdpQcCheckProcess.insertNoticeWin.hide();
				        } else {                           //操作失败
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
		}, {
			text:'取消', iconCls:'cancelIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}], 
		// 提票窗口显示后，自动设置提票单“编号”字段值（可修改）
		listeners:{
			show: function(window) {
				// 提票窗口显示时，重置“描述”字段
				window.find('name', 'noticeDesc')[0].reset();
				// 自动生成编号
				Ext.Ajax.request({
					url: ctx + "/codeRuleConfig!getConfigRule.action",
					params: {ruleFunction: "PJJX_QC_NOTICE_NO"},
					success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
							PartsRdpQcCheckProcess.insertNoticeWin.find('name', 'noticeNo')[0].setValue(result.rule);
						}
					},
					failure: function(response, options){
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		}
	
	});
	/** ************** 定义【提票】窗口结束 ************** */
	
	/** ************** 定义质量检验记录卡表格开始 ************** */
	PartsRdpQcCheckProcess.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsRdpRecordCard!pageQuery.action',
		tbar:[{
			text:'签名提交', iconCls:'edit1Icon', handler:function() {
				var sm = PartsRdpQcCheckProcess.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert("尚未选择一条记录！");
					return;
				}
				// 显示签名提交处理窗口
				PartsRdpQcCheckProcess.finishWin.show();
			}
		}, {
			text:'返修', iconCls:'deleteIcon', handler:function() {
				var sm = PartsRdpQcCheckProcess.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert("尚未选择一条记录！");
					return;
				}
				Ext.Msg.confirm('提示', '是否确认返修？', function(btn) {
					if (btn == 'yes') {
						var records = sm.getSelections();
						var rdpRecordCardIDXs = [];
						for (var i = 0; i < records.length; i++) {
							rdpRecordCardIDXs.push(records[i].get('idx'));
						}
						PartsRdpQcCheckProcess.backFn(rdpRecordCardIDXs, PartsRdpQcCheckProcess.qcItemNo);
					}
				});
			}
		}, {
			text:'提票', iconCls:'page_edit_1Icon', handler:function() {
				// 显示提票处理窗口
				PartsRdpQcCheckProcess.insertNoticeWin.show();
			}
		}],
		saveForm: PartsRdpQcCheckSub.saveForm,			// 定义文件PartsRdpQcCheckSub.js
	    saveWin: PartsRdpQcCheckSub.win,				// 定义文件PartsRdpQcCheckSub.js
		fields:[{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true
		},{
			header:'记录单主键', dataIndex:'rdpRecordIDX', hidden:true
		},{
			header:'记录卡主键', dataIndex:'recordCardIDX', hidden:true
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width: 20
		},{
			header:'记录卡编号', dataIndex:'recordCardNo', width: 40
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', width: 100,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpQcCheckProcess.grid.toEditFn(\""+ PartsRdpQcCheckProcess.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true
		},{
			header:'作业人', dataIndex:'workEmpName', width: 55
		},{
			header:'作业开始时间', dataIndex:'workStartTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, hidden:true
		},{
			header:'作业结束时间', dataIndex:'workEndTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, hidden:true
		},{
			header:'质量检验', dataIndex:'qcContent', width: 55, hidden:true
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true
		},{
			header:'领活人', dataIndex:'handleEmpName', width: 30, hidden:true
		},{
			header:'状态', dataIndex:'status', width: 30, align:'center', hidden:true, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态";
			}
		},{
			header:'检修情况描述', dataIndex:'remarks', hidden: true
		}],
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】处理窗口
		toEditFn: function(grid, rowIndex, e){
			// 显示【检修作业工单】处理窗口
	        this.saveWin.show();
	        var record = this.store.getAt(rowIndex);
	        
	    	// 调用【检修作业工单】处理窗口的初始化函数，初始化【检修作业工单】处理窗口的各个字段值
	    	PartsRdpQcCheckSub.initFn(record);
	    	// 记录当前处理的【检修作业工单】的索引值，用以在处理窗口点击【上一工单】【下一工单】进行工单的快速切换
	    	PartsRdpQcCheckSub.index = parseInt(rowIndex);
	    	
	        // 设置【配件编号】【配件名称】【规格型号】【扩展编号】等固定字段值
	    	PartsRdpQcCheckSub.baseForm.find('name', 'partsNo')[0].setValue(PartsRdpQcCheckProcess.baseForm.find('name', 'partsNo')[0].getValue());
	    	PartsRdpQcCheckSub.baseForm.find('name', 'partsName')[0].setValue(PartsRdpQcCheckProcess.baseForm.find('name', 'partsName')[0].getValue());
	    	PartsRdpQcCheckSub.baseForm.find('name', 'specificationModel')[0].setValue(PartsRdpQcCheckProcess.baseForm.find('name', 'specificationModel')[0].getValue());
	    	PartsRdpQcCheckSub.baseForm.find('name', 'extendNo')[0].setValue(PartsRdpQcCheckProcess.baseForm.find('name', 'extendNo')[0].getValue());
	    	//清除节点主键信息
			 PartsRdpQcCheckProcess.rdpNodeIdx = record.get("rdpNodeIDX");
	    }
	}) 
	PartsRdpQcCheckProcess.grid.store.setDefaultSort('seqNo', 'ASC');
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpQcCheckProcess.grid.store.on('beforeload', function() {
		var whereList = []; 
		var sql = "idx In (Select Rdp_Record_Card_IDX From PJJX_Parts_Rdp_QR Where Record_Status = 0 And QC_Item_No='" + PartsRdpQcCheckProcess.qcItemNo + "' And status= '" + QC_STATUS_DCL + "')"
		whereList.push({sql: sql, compare:Condition.SQL});
		sql = "idx In (Select Rdp_Record_Card_IDX From PJJX_Parts_Rdp_QC_Participant Where Record_Status = 0 And QC_EmpID='" + empid + "' And QC_Item_No='" + PartsRdpQcCheckProcess.qcItemNo + "')"
		whereList.push({sql: sql, compare:Condition.SQL});
		whereList.push({propName:"status", propValue:PARTS_RDP_STATUS_ZLJYZ, compare:Condition.EQ});
		
		// 添加作业主键过滤
		whereList.push({propName:"rdpIDX", propValue:PartsRdpQcCheckProcess.rdpIDX, compare:Condition.EQ});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	// 质量检查完成后自动关闭处理窗口，用以规避bug(2108)
	PartsRdpQcCheckProcess.grid.store.on('load', function(store, records, options) {
		if (records.length <= 0) {
			PartsRdpQcCheckProcess.win.hide();
		}
	});
	/** ************** 定义质量检验记录卡表格结束 ************** */
	
	PartsRdpQcCheckProcess.win = new Ext.Window({
		title:"质量检验",
		width:1200, height:700, 
		closeAction:'hide',
		modal:true,
		layout:"border",
		defaults:{layout:'fit'},
		items:[
			{
				height:100,
				collapsible: true,
				frame:true,
				layout:'fit',
				region:"north",
				title:"<span style='font-weight:normal;'>检修记录单基本信息<span>",
				items:[PartsRdpQcCheckProcess.baseForm]
			},
			{
				region:"center",
				items:PartsRdpQcCheckProcess.grid
			}
		], 
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			// 隐藏【质量检验】处理窗口时，重新加载主表格数据
			hide: function() {
				PartsRdpQcCheckBJ.grid.store.reload();
				PartsRdpQcCheckCJ.grid.store.reload();
			}
		}
	})
});

