/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RecordCardNew');                       // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
//    RecordCardNew.record = null;							 // 【检修记录工单】记录对象  
    // 当前记录单索引
    RecordCardNew.index = '';
    //车型车号
	RecordCardNew.cxch = '';
	//修程修次
	RecordCardNew.xcxc = '';
	//开始时间
	RecordCardNew.planBeginTime = '';
	//结束时间
	RecordCardNew.planEndTime = '';
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
	// 获取检修记录单详情显示模板--
	RecordCardNew.getDetailTpl = function() {
		if (Ext.isEmpty(RecordCardNew.tpl)) {
			RecordCardNew.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">检修记录单编号：</td><td width="13%" colspan="3">{activityCode}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">车型车号：</td><td width="13%">{trainTypeShortName}|{trainNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">计划开始时间：</td><td width="13%" colspan="2">{planBeginTime}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">检修记录单名称：</td><td width="13%" colspan="3">{activityName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">修程修次：</td><td width="13%">{repairClassName}{repairtimeName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">计划结束时间：</td><td width="13%" colspan="2">{planEndTime}</td>',
		            '</tr>',				
				'</table>'
			);
		}
		return RecordCardNew.tpl;
	}
	// 获取检修记录单详情显示模板 -- 
	RecordCardNew.getPartsRdpInfoTpl = function() {
		if (Ext.isEmpty(RecordCardNew.tp2)) {
			RecordCardNew.tp2 = new Ext.XTemplate(
			'<table class="pjjx-show-info-table">',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%" colspan="3">{partsName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">记录单编号：</td><td width="13%">{recordNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">记录单名称：</td><td width="13%" colspan="5">{recordName}</td>',
	            '</tr>',				
			'</table>'
			);
		}
	    return RecordCardNew.tp2;
	}
 	
   // 加载train记录卡详情
	RecordCardNew.loadTrain = function(workPlanRepairActivityIDX) {
		var dom = Ext.get('record_card_detail').dom;	
		Ext.Ajax.request({
			url: ctx + '/workCard!findWorkCardInfoByWorkPlanRepairActivityIDX.action',
			params:{ workPlanRepairActivityIDX: workPlanRepairActivityIDX},
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);	
		        dom.innerHTML = CreateShowTable.showTrainTable(result.workCardBeanList);
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});			
	}
	
	 // 加载parts记录卡详情
	RecordCardNew.loadParts = function(rdpRecordIDX) {
		var dom = Ext.get('record_card_detail').dom;	
		Ext.Ajax.request({
			url: ctx + '/partsRdpRecordCard!integrateQueryCardList.action',
			params:{ rdpRecordIDX: rdpRecordIDX},
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);	
		        var  partsRdpRecordCardList = Ext.util.JSON.decode(result.partsRdpRecordCardList);
		        dom.innerHTML = CreateShowTable.showPatrsTable(partsRdpRecordCardList);
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});			
	}
	/**
	 * 重新加载一个新的记录单详情
	 */ 
	RecordCardNew.initFn = function(flag) {
		// 获取检修记录单
		var values =  RecordCardNew.rdp;
		var tpl;
		// 获取配件检修兑现单信息显示模板
		if(flag == "parts"){
		    tpl = RecordCardNew.getPartsRdpInfoTpl();
		    RecordCardNew.loadParts(RecordScanQuery.idx );
		}else if(flag == "train"){		
		    tpl = RecordCardNew.getDetailTpl();
		    RecordCardNew.loadTrain(RecordScanQuery.idx );	   	
		}	
		 Ext.applyIf(values,  RecordCardNew.record);  // 赋值检修记录单属性
		// 格式化“开始时间”和“结束时间”
		if (!Ext.isEmpty(values.planBeginTime)) {
			if (Ext.isString(values.planBeginTime)) {
				values.planBeginTime = values.planBeginTime;
			} else {
				values.planBeginTime = new Date(values.planBeginTime).format('Y-m-d H:i');
			}
		}
		if (!Ext.isEmpty(values.planEndTime)) {
			if (Ext.isString(values.planEndTime)) {
				values.planEndTime = values.planEndTime;
			} else {
				values.planEndTime = new Date(values.planEndTime).format('Y-m-d H:i');
			}
		}
	
		tpl.overwrite(Ext.get('record_detail'), values);
	
	}

	/** **************** 定义全局函数结束 **************** */

});