/**
 * 配件检修质量检验_必检 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpQcCheckBJ');			// 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpQcCheckBJ.labelWidth = 100;
	PartsRdpQcCheckBJ.fieldWidth = 140;
	PartsRdpQcCheckBJ.searchParams = {};
	PartsRdpQcCheckBJ.qcItems = [];
	PartsRdpQcCheckBJ.qcContent = "";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数结束 ************** */
	PartsRdpQcCheckBJ.checkQueryFn = function() {
		PartsRdpQcCheckBJ.qcContent = "-1";
		for (var i = 0; i < PartsRdpQcCheckBJ.qcItems.length; i++) {
			var qcItem = PartsRdpQcCheckBJ.qcItems[i];
			if (Ext.getCmp(qcItem.id).checked) {
				PartsRdpQcCheckBJ.qcContent += "," + qcItem.inputValue;
			}
		}
		PartsRdpQcCheckBJ.grid.store.load();
	}
	// 初始化质量检查项的过滤条件
	PartsRdpQcCheckBJ.initQcItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
			var qcItem = objList[i];
			if (qcItem.checkWay != CHECK_WAY_BJ) {
				continue;
			}
			var editor = {};
			editor.xtype = "checkbox";
			editor.checked = true,
			editor.id = qcItem.qCItemNo;
	        editor.boxLabel = qcItem.qCItemName;
	        editor.inputValue = qcItem.qCItemNo;
	        editor.width = qcItem.qCItemName.length * 10 + 30;
	        editor.handler = PartsRdpQcCheckBJ.checkQueryFn;
	        PartsRdpQcCheckBJ.qcItems.push(editor);
		}
	}();
	/** ************** 定义全局函数开始 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	PartsRdpQcCheckBJ.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsRdp!findPartRdpQCItems.action',
		saveForm:PartsRdpQcCheckProcess.baseForm,
		saveWin:PartsRdpQcCheckProcess.win,
		tbar: [PartsRdpQcCheckBJ.qcItems, '-', 'refresh'],
		fields: [{
			dataIndex:'rdpIDX', header:'作业主键', hidden:true
		}, {
			dataIndex:'partsNo', header:'配件编号',
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
				if (Ext.isEmpty(value)) {
					value = "";
				}
				return value;
			}
		}, {
			dataIndex:'partsName', header:'配件名称',
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
				if (Ext.isEmpty(value)) {
					value = "";
				}
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpQcCheckBJ.grid.toEditFn(\""+ PartsRdpQcCheckBJ.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		}, {
			dataIndex:'specificationModel', header:'规格型号', width:200
		}, {
			dataIndex:'matCode', header:'物料编码'
		}, {
			dataIndex:'repairOrgID', header:'承修班组ID', hidden:true
		}, {
			dataIndex:'repairOrgName', header:'承修班组'
		}, {
			dataIndex:'repairOrgSeq', header:'承修班组序列', hidden:true
		}, {
			dataIndex:'dutyEmpID', header:'检修负责人ID', hidden:true
		}, {
			dataIndex:'dutyEmpName', header:'检修负责人', width:60
		}, {
			dataIndex:'qty', header:'待检验工单数量', width:70
		}, {
			dataIndex:'qcItemNo', header:'检查项编码', hidden:true
		}, {
			dataIndex:'qcItemName', header:'检查项名称'
		}, {
			dataIndex:'unloadTrainType', header:'下车车型号', renderer: function(value, mataData, record, rowIndex, colIndex, store) {
				var unloadTrainNo = record.data.unloadTrainNo;
				if (Ext.isEmpty(unloadTrainNo)) {
					return value;
				}
				return value + unloadTrainNo;
			}
		}, {
			dataIndex:'unloadTrainNo', header:'下车车号', hidden:true
		}, {
			dataIndex:'unloadRepairClass', header:'修程', width:50, renderer: function(value, mataData, record, rowIndex, colIndex, store) {
				var unloadRepairTime = record.data.unloadRepairTime;
				if (Ext.isEmpty(unloadRepairTime)) {
					return value;
				}
				return value + unloadRepairTime;
			}
		}, {
			dataIndex:'unloadRepairTime', header:'修次', width:50, hidden:true
		}, {
			dataIndex:'extendNo', header:'扩展编号', hidden:true, renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (!Ext.isEmpty(value)) {
					return jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(value);
				}
			}
		}, {
			dataIndex:'planStartTime', header:'计划开始时间', hidden:true
		}, {
			dataIndex:'planEndTime', header:'计划结束时间', hidden:true
		}],
		toEditFn: function(grid, rowIndex, e){
			 var record = this.store.getAt(rowIndex);
			 this.saveForm.getForm().loadRecord(record);
			 // 根据质量检验项设置质量检验处理窗口的名称
			 this.saveWin.setTitle("质量检验<span'>(" + record.get('qcItemName') + ")</span>");
			 
			 // 必检可支持批处理
			 var colModel = PartsRdpQcCheckProcess.grid.getColumnModel();
			 if (colModel.isHidden(1)) {
			 	colModel.setHidden(1, false);
			 }
			 this.saveWin.show();
			 // 编辑窗口显示后的函数处理
			 this.afterShowEditWin(record, rowIndex);
			 //清除节点主键信息
			 PartsRdpQcCheckProcess.rdpNodeIdx = "";
		},
		afterShowEditWin: function(record, rowIndex){
			// 设置当前处理的质量检验项编码
			PartsRdpQcCheckProcess.qcItemNo = record.get('qcItemNo');
			PartsRdpQcCheckProcess.qcItemName = record.get('qcItemName');
			PartsRdpQcCheckProcess.rdpIDX = record.get('rdpIDX');
			// 加载数据
			PartsRdpQcCheckProcess.grid.store.load();
			// 对下次车型号和下次修程进行单独赋值
			var unloadTrainType = record.get('unloadTrainType');
			var unloadTrainNo = record.get('unloadTrainNo');
			var unloadRepairClass = record.get('unloadRepairClass');
			var unloadRepairTime = record.get('unloadRepairTime');
			this.saveForm.find('name','trainType')[0].setValue(Ext.isEmpty(unloadTrainNo) ? unloadTrainType : unloadTrainType + unloadTrainNo);
			this.saveForm.find('name','repair')[0].setValue(Ext.isEmpty(unloadRepairTime) ? unloadRepairClass : unloadRepairClass + unloadRepairTime);
			var extendNo = record.get('extendNo');
			if (Ext.isEmpty(extendNo)) {
				return;
			}
			// 将扩展编号的json字符串转换为以“|”分割的样式
			extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(extendNo);
			this.saveForm.find('name', 'extendNo')[0].setValue(extendNo);
		}
	})
	PartsRdpQcCheckBJ.grid.store.on('beforeload', function(){
		var searchParams = 	MyJson.deleteBlankProp(PartsRdpQcCheckBJ.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		this.baseParams.qcContent = PartsRdpQcCheckBJ.qcContent;
		this.baseParams.checkWay = CHECK_WAY_BJ;
	});
	/** ************** 定义查询表单结束 ************** */
	
});

