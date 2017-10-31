/**
 * 配件检修检测项实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordRI');                       //定义命名空间
	
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpRecordRI.labelWidth = 100;
	PartsRdpRecordRI.fieldWidth = 100;
	PartsRdpRecordRI.rdpRecordCardIDX = "###";				// 记录卡实例主键
	PartsRdpRecordRI.rowIndex = -1;							// 记录当前正在被操作的【检修检测项】的索引值
	PartsRdpRecordRI.status = RECORD_RI_STATUS_WCL;			// 状态
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数结束 ************** */
	// 【检修检测项】提交（暂存）功能的Ajax请求处理
	PartsRdpRecordRI.ajaxRequset = function(isTemporary, entityJson, datas) {
		self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/partsRdpRecordRI!saveTemporary.action',
			jsonData: datas,
			params: {
				isTemporary: isTemporary,
				entityJson: Ext.util.JSON.encode(entityJson)
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            PartsRdpRecordRI.grid.store.reload(); 
		            
		            // 如果是提交操作,重新加载【检修检测项】表格数据后，数据记录会发生变化，默认重新选择之前操作记录的前一条记录
		            if (!isTemporary) {
		            	PartsRdpRecordRI.rowIndex = parseInt(PartsRdpRecordRI.rowIndex) - 1;
		            }
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
	};
	
	// 【检修检测项】提交（暂存）功能函数
	// 参数说明：isTemporary - true：表示【暂存】，false：表示【提交】
	PartsRdpRecordRI.submitFn = function(isTemporary) {
		var form = PartsRdpRecordRI.form.getForm();
		if (!form.isValid() && !isTemporary) {
			return;
		}
		var entityJson = form.getValues();
		if (Ext.isEmpty(entityJson.idx)) {
			MyExt.Msg.alert("尚未选择一条记录！");
			return;
		}
		var datas = [];
		for (var i = 0; i < PartsRdpRecordDI.store.getCount(); i++) {
			datas.push(PartsRdpRecordDI.store.getAt(i).data);
		}
		
		// 如果不是【暂存】则要给出是否确认提交的提示信息
		if (!isTemporary) {
			for (var k = 0; k < datas.length; k++) {
				// 检验是否有必填的“数据项”未填写“检测结果”
				if (datas[k].isBlank == IS_BLANK_YES) {
					if (null == datas[k].dataItemResult || Ext.isEmpty(datas[k].dataItemResult.trim())) {
						MyExt.Msg.alert('数据项<span style="color:red;">[' + datas[k].dataItemName + ']</span>必须填写检测结果！');
						return;
					}
				}
			}
			Ext.Msg.confirm('提示', '是否确认提交？', function(btn) {
				if ('yes' == btn) {
					PartsRdpRecordRI.ajaxRequset(isTemporary, entityJson, datas);
				}
			});
		} else {
			PartsRdpRecordRI.ajaxRequset(isTemporary, entityJson, datas);
		}
	}
	
	// 【检修检测项】表格选中一行记录后的初始化操作
	PartsRdpRecordRI.initFn = function(rowIndex) {
		var record = PartsRdpRecordRI.grid.store.getAt(rowIndex);
		// 右下角表单数据的初始化
		PartsRdpRecordRI.form.getForm().loadRecord(record);
		
		var repairResult = record.get('repairResult');
		// 如果“检修检测结果”字段没有值，则默认设置为“合格”
		var repairResultCombo = PartsRdpRecordRI.form.find('name', 'repairResult')[0];
		if (!Ext.isEmpty(repairResult)) {
			repairResultCombo.setDisplayValue(record.get('repairResult'), record.get('repairResult'));
		} else {
			var store = repairResultCombo.getStore();
			if (store.getCount() > 1) {
				var r = store.getAt(1);
				repairResultCombo.setDisplayValue(r.data.dictname, r.data.dictname);
			}
		}
//		if (Ext.isEmpty(repairResult.getValue())) {
//			repairResult.setValue(REPAIR_RESULT_HG);
//		}
		
//		PartsRdpRecordRI.form.find('name', 'repairResult')[0].enable();				// 启用“检修检测项结果”
//		PartsRdpRecordRI.form.find('name', 'remarks')[0].enable();							// 启用“备注”
//		Ext.getCmp('btn_tj').enable();														// 启用“提交”
//		Ext.getCmp('btn_zc').enable();														// 启用“暂存”		
		
		// 已处理的记录，不能进行再次操作
		var riStatus = record.get('status');
		var recordCardStatus = RecordCardProcess.saveForm.find('name', 'status')[0].getValue();
		if (RECORD_RI_STATUS_YCL == riStatus || PARTS_RDP_STATUS_DCL != recordCardStatus) {						// 已处理
			PartsRdpRecordRI.form.find('name', 'repairResult')[0].disable();				// 禁用“检修检测项结果”
			PartsRdpRecordRI.form.find('name', 'remarks')[0].disable();							// 禁用“备注”
			Ext.getCmp('btn_tj').disable();														// 禁用“提交”
			Ext.getCmp('btn_zc').disable();														// 禁用“暂存”												
		} else if (riStatus == RECORD_RI_STATUS_WCL && PARTS_RDP_STATUS_DCL == recordCardStatus){				// 未处理
			PartsRdpRecordRI.form.find('name', 'repairResult')[0].enable();				// 启用“检修检测项结果”
			PartsRdpRecordRI.form.find('name', 'remarks')[0].enable();							// 启用“备注”
			Ext.getCmp('btn_tj').enable();														// 启用“提交”
			Ext.getCmp('btn_zc').enable();														// 启用“暂存”		
		}
		
		// 重新加载【检测项】表格
		//Ext.getCmp('check_Base_combo').clearValue();
		PartsRdpRecordDI.grid.store.removeAll();
		PartsRdpRecordDI.rdpRecordRIIDX = record.get('idx');
		PartsRdpRecordDI.grid.store.load();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义检修检测项表格开始 ************** */
	PartsRdpRecordRI.grid = new Ext.yunda.Grid({
		storeAutoLoad: false,
		border: false,
	    loadURL: ctx + '/partsRdpRecordRI!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpRecordRI!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpRecordRI!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true,
	    tbar:[{
	    	xtype:'label', text:'状态：'
    	}, {
    		xtype: 'radio', boxLabel: '未处理', checked: true, name: 'status_ri', inputValue: RECORD_RI_STATUS_WCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordRI.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordRI.grid.store.load();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '已处理', name: 'status_ri', inputValue: RECORD_RI_STATUS_YCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordRI.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordRI.grid.store.load();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '所有', name: 'status_ri', inputValue: -1, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordRI.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordRI.grid.store.load();
    			}
    		}
    	}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'记录卡实例主键', dataIndex:'rdpRecordCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检修检测项主键', dataIndex:'recordRIIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检修检测项编号', dataIndex:'repairItemNo', hidden:true, editor:{  maxLength:30 }
		},{
			header:'顺序号', dataIndex:'seqNo', width: 20, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'检修检测项名称', dataIndex:'repairItemName', width:35, editor:{  maxLength:50 }
		},{
			header:'技术要求', dataIndex:'repairStandard', editor:{  maxLength:500 }
		},{
			header:'检测结果', dataIndex:'repairResult', hidden:true, editor:{  maxLength:30 }
		},{
			header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:500 }
		},{
			header:'状态', dataIndex:'status', hidden:true, editor:{  maxLength:20 }
		}],
		listeners: {
			rowclick: function(grid, rowIndex, e) {
				PartsRdpRecordRI.rowIndex = rowIndex;
				// 初始化页面数据
				PartsRdpRecordRI.initFn(PartsRdpRecordRI.rowIndex);
			}
		}
	});
	// 取消表格默认的行双击进行编辑的事件监听
	PartsRdpRecordRI.grid.un('rowdblclick', PartsRdpRecordRI.grid.toEditFn, PartsRdpRecordRI.grid);
	// 默认按顺序号正序排序
	PartsRdpRecordRI.grid.store.setDefaultSort('seqNo', 'ASC');
	// 列表数据容器加载时的过滤条件设置
	PartsRdpRecordRI.grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpRecordCardIDX = PartsRdpRecordRI.rdpRecordCardIDX;							// 记录卡实例主键
		
		if (PartsRdpRecordRI.status != -1) {
			searchParams.status = PartsRdpRecordRI.status;												// 状态
		}
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 列表数据容器加载后的函数事件监听
	PartsRdpRecordRI.grid.store.on('load', function() {
		var count = PartsRdpRecordRI.grid.store.getCount();
		if (count <= 0) {
        	// 如果已经没有可处理的【检修检测项】，则清空右下角的表单和表格数据
        	PartsRdpRecordRI.form.getForm().reset();
        	PartsRdpRecordDI.grid.store.removeAll();
        	// 禁用页面功能按钮（组件）
        	PartsRdpRecordRI.form.find('name', 'repairResult')[0].clearValue();					// 禁用“检修检测项结果”
        	PartsRdpRecordRI.form.find('name', 'repairResult')[0].disable();					// 禁用“检修检测项结果”
			PartsRdpRecordRI.form.find('name', 'repairResult')[0].clearInvalid();
			PartsRdpRecordRI.form.find('name', 'remarks')[0].disable();							// 禁用“备注”
			Ext.getCmp('btn_tj').disable();														// 禁用“提交”
			Ext.getCmp('btn_zc').disable();			
			return;
		}
		var sm = PartsRdpRecordRI.grid.getSelectionModel();
		if (PartsRdpRecordRI.rowIndex < 0 || PartsRdpRecordRI.rowIndex >= count) {
			PartsRdpRecordRI.rowIndex =  0;
		}
		sm.selectRow(PartsRdpRecordRI.rowIndex);
        PartsRdpRecordRI.initFn(PartsRdpRecordRI.rowIndex);
	});
	/** ************** 定义检修检测项表格结束 ************** */
	
	/** ************** 定义检修检测项表单开始 ************** */
	PartsRdpRecordRI.form = new Ext.form.FormPanel({
		labelWidth:PartsRdpRecordRI.labelWidth,
		labelAlign:"left",
		padding:"0 10px",
		defaultType:"textfield",
		items:[{
			fieldLabel:"检修检测项名称",
			style: 'border:none; background:none;',
			name:'repairItemName',
			readOnly: true,
			anchor:"100%"
		}, {
			fieldLabel:"技术要求",
			style: 'border:none; background:none;',
			name:'repairStandard',
			readOnly: true,
			anchor:"100%"
		}, {
	    	fieldLabel: '检修检测结果',
			name: 'repairResult', 
			xtype: 'EosDictEntry_combo', 
			dicttypeid: 'PJJX_RECORD_RI_RESULT',
			allowBlank:false,
			displayField:'dictname',valueField:'dictname',
			editable:true,
			anchor:'80%'
	    }, {
			xtype:"textarea",
			name:"remarks",
			fieldLabel:"备注", height: 53, maxLength: 500,
			anchor:"100%"
		}, {
			xtype:'hidden', name:'idx', fieldLabel: 'idx主键'
		}, {
			xtype:'hidden', name:'status', fieldLabel: '状态'
		}]

	});
	/** ************** 定义检修检测项表单结束 ************** */
	
});