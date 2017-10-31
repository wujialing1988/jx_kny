/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkCardNew');                       // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
    TrainWorkCardNew.record = null;	
    TrainWorkCardNew.count = 0;
    // 【检修记录工单】记录对象
    TrainWorkCardNew.workPlanStatus = "";
    // 当前记录单索引
    TrainWorkCardNew.index = '';
    //车型车号
	TrainWorkCardNew.cxch = '';
	//修程修次
	TrainWorkCardNew.xcxc = '';
	//开始时间
	TrainWorkCardNew.planBeginTime = '';
	//结束时间
	TrainWorkCardNew.planEndTime = '';
	TrainWorkCardNew.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修记录单详情显示模板
	 */
	TrainWorkCardNew.getDetailTpl = function() {
		if (Ext.isEmpty(TrainWorkCardNew.tpl)) {
			TrainWorkCardNew.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
   		                '<td class="pjjx-show-info-table-label" width="12%">检修记录单名称：</td><td width="13%">{activityName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">车辆信息：</td><td width="13%">{cxch} {xcxc}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">填单时间：</td><td width="13%">{recordDate}</td>',
		            '</tr>',
//					'<tr>',
//		                '<td class="pjjx-show-info-table-label" width="12%">检修记录单名称：</td><td width="13%">{activityName}</td>',
//		                '<td class="pjjx-show-info-table-label" width="12%">修程修次：</td><td width="13%">{xcxc}</td>',
//		                '<td class="pjjx-show-info-table-label" width="12%">计划结束时间：</td><td width="13%" colspan="5">{planEndTime}</td>',
//		            '</tr>',				
				'</table>'
			);
		}
		return TrainWorkCardNew.tpl;
	}
	
	// 动态生成表格列表（含嵌套列表）
	TrainWorkCardNew.createShowingTable = function(data){ 
		if(null == data){
			 Ext.Msg.alert('提示', "没有找到对应的车辆检修记录卡\n");
			return;
		}
	    //此处需要让其动态的生成一个table并填充数据  
	    var list = data;
	    var tableStr = '<table cellpadding="0" cellspacing="0">';
	    tableStr += '<thead>'
	    tableStr += '<tr>'
	    tableStr += '<td width="2%" align="center">序号</td>'
	    tableStr += '<td width="10%" >检修记录卡名称</td>'
	    tableStr += '<td width="10%" >检修检测项</td>'
	    tableStr += '<td width="34.5%" >技术要求</td>'
	    tableStr += '<td width="5.5%" >默认值</td>'
	    tableStr += '<td width="18%">结果</td>'
	    tableStr += '<td width="5%" >自检</td>'
	    tableStr += '<td width="15%">质量检查</td>'
	    tableStr += '</tr>'
	    tableStr += '</thead>'
	    tableStr += '<tbody>'
	    for (var i=0 ;i < list.length ; i++) {
	    	tableStr += '<tr>'
	    	tableStr += '<td width="2%">' + (i + 1) + '</td>' 
	    	tableStr += '<td width="10%">' + list[i].workCardName + '</td>' 
	    	if (list[i].workTaskBeanList && list[i].workTaskBeanList.length > 0) {
	    		tableStr += '<td colspan ="4">';'</td>'; 
	    		tableStr += '<table  cellpadding="-1" cellspacing="-1">' 
	    		var riList = list[i].workTaskBeanList;
	    		for (var j = 0; j < riList.length; j++) {
	    			var cls = "";
	    			if (0 === j) {
	    				cls = 'border-top:0;';
	    			}
	    			
		    		tableStr += '<tr>' 
		    		tableStr += '<td width="9.5%" style=" '+ cls +' border-left:0;">' + riList[j].workTaskName + '</td>'
		    		tableStr += '<td width="35.5%" style=" '+ cls +' ">' + riList[j].repairStandard + '</td>'
		    		if (riList[j].dataItemList && riList[j].dataItemList.length > 0) {
			    		tableStr += '<td width="23.5%" style=" '+ cls +' ">'
			    		tableStr += '<table cellpadding="0" cellspacing="0" width="110%">' 
			    		var diList = riList[j].dataItemList;
			    		for (var k = 0; k < diList.length; k++) {
			    			cls = "";
			    			if (0 === k) {
			    				cls = 'border-top:0;';
			    			}
			    			var clsred = (("数字"==diList[k].detectResulttype) && (parseInt(diList[k].detectResult) <= diList[k].minResult || parseInt(diList[k].detectResult) >= diList[k].maxResult))? 'color:red':"";
				    		tableStr += '<tr style =" '+ clsred +' ">'  
				    		tableStr += '<td width="21" align="left" style=" '+ cls +' border-left:0; " >' + (Ext.isEmpty(diList[k].minResult)? "&nbsp;" : diList[k].minResult) + " ~ " + (Ext.isEmpty(diList[k].maxResult) ? "&nbsp;" : diList[k].maxResult) + '</td>'
				    		tableStr += '<td width="40" align="right" style=" '+ cls +' " >' + diList[k].detectItemContent + "：" 
				    		tableStr += (0 == diList[k].isNotBlank ? "*" : "&nbsp") + '</td>' 
				    		tableStr += '<td width="38.3" align="center" style=" '+ cls  +' border-left:0; ">' + (Ext.isEmpty(diList[k].detectResult) ? "&nbsp;" : diList[k].detectResult) + '</td>' 
				    		tableStr += '</tr>' 
			    		}
			    		tableStr += '</table>' 
			    		tableStr += '</td>' 
		    		} else {
		    			tableStr += '<td width="23.5%" style=" '+ cls +' ">'
		    			tableStr += '<table cellpadding="0" cellspacing="0" width="110%">' 
		    			tableStr += '<td width="23.4%" style="border-top:0;  border-left:0; " >' + (Ext.isEmpty(riList[j].repairResult) ? "&nbsp;" : riList[j].repairResult) + '</td>'
	    				tableStr += '<td width="76%" style="border-top:0;" ">' + (Ext.isEmpty(riList[j].repairResult) ? "&nbsp;" : riList[j].repairResult) + '</td>'
	    				tableStr += '</table>' 
			    		tableStr += '</td>' 
		    		}
		    		tableStr += '</tr>' 
	    		}
	    		tableStr += '</table>'; 
	    		tableStr += '</td>';
	    	} else {

				tableStr += '<td width="10%" >&nbsp</td>'
			    tableStr += '<td width="34.5%" >&nbsp</td>'
			    tableStr += '<td width="5.5%">&nbsp</td>'
			    tableStr += '<td width="18%">&nbsp</td>'

	    	}
	    	if(!list[i].worker){
	    		tableStr += '<td width="10%">' +""+ '</td>';
	    	}else{
	    		tableStr += '<td width="5%">' +  list[i].worker + '</td>';
	    	} 
	    	tableStr += '<td width="10%" >'; 
	    	var qCResultList = list[i].qCResultList;
	       	if(null != qCResultList && 0 < qCResultList.length){   
		        for(var j = 0; j< qCResultList.length; j++){
		        	tableStr = tableStr + "<li>"+ qCResultList[j].checkItemName +":&nbsp&nbsp" + (Ext.isEmpty(qCResultList[j].qcEmpName)? "&nbsp;" :qCResultList[j].qcEmpName) + "</li>" ;  
		        }
	        }
	        tableStr = tableStr + '</td>';
	    	tableStr += '</tr>'
	    }
	    tableStr += '</tbody>'
	    tableStr += '</table>'
	    //将动态生成的table添加的事先隐藏的div中.  
	   return tableStr;    
 	}  
 	
   // 加载记录卡详情
	TrainWorkCardNew.loadRi = function(workPlanRepairActivityIDX) {
		if(TrainWorkCardNew.loadMask)    TrainWorkCardNew.loadMask.show();
		var dom = Ext.get('id_jx_record_card_detail').dom;	
		Ext.Ajax.request({
			url: ctx + '/workCardHis!findWorkCardInfoByWorkPlanRepairActivityIDX.action',
			params:{ workPlanRepairActivityIDX: workPlanRepairActivityIDX,
					workPlanStatus: TrainWorkCardNew.workPlanStatus},
		    //请求成功后的回调函数
		    success: function(response, options){		     
		        var result = Ext.util.JSON.decode(response.responseText);	
		        dom.innerHTML = TrainWorkCardNew.createShowingTable(result.workCardBeanList);
		        if(TrainWorkCardNew.loadMask)    TrainWorkCardNew.loadMask.hide();
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(TrainWorkCardNew.loadMask)    TrainWorkCardNew.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});			
	}
	/**
	 * 重新加载一个新的记录单详情
	 */ 
	TrainWorkCardNew.initFn = function() {
		// 获取检修记录单
		if(!(typeof(RQWorkCard.grid)=="undefined")){
			TrainWorkCardNew.record = RQWorkCard.grid.store.getAt(TrainWorkCardNew.index);
			TrainWorkCardNew.count = RQWorkCard.grid.store.getCount();
		}
		else if(!(typeof(TrainWorkPlanWorkRecord)=="undefined")){
			TrainWorkCardNew.record = TrainWorkPlanWorkRecord.grid.store.getAt(TrainWorkCardNew.index);
			TrainWorkCardNew.count = TrainWorkPlanWorkRecord.grid.store.getCount();
		}
		
		var values = {};
		data = TrainWorkCardNew.record.data;
		if(null !=data.recordDate){
			data.recordDate = new Date(data.recordDate).format('Y-m-d H:i');
		}
		
		// 重新设置车型车号等信息
		data.cxch = TrainWorkCardNew.cxch ;
		data.xcxc = TrainWorkCardNew.xcxc ;
		data.planBeginTime = TrainWorkCardNew.planBeginTime ;
		data.planEndTime = TrainWorkCardNew.planEndTime ;
		Ext.applyIf(values, data);						// 赋值检修记录单属性
		
		// 获取配件检修兑现单信息显示模板
		var tpl = TrainWorkCardNew.getDetailTpl();
		tpl.overwrite(Ext.get('id_jx_record_detail'), values);
		TrainWorkCardNew.loadRi(data.idx);	
	}

	/** **************** 定义全局函数结束 **************** */
	
	TrainWorkCardNew.win = new Ext.Window({
		title: '车辆检修记录单详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
			}
		},{
			text: '打印', iconCls: 'printerImg', handler: function(){
				//机车检修记录单
				var repairProjectIDX =  TrainWorkCardNew.record.get('repairProjectIDX');			
				var value = TrainWorkCardNew.record.id;
				var args = [value,repairProjectIDX].join('-');
				RQWorkCard.printFn(args);
			}
		}, '->', {
			text:'上一记录单', iconCls:'moveUpIcon', id:'id_t_move_up_btn', handler: function(){
				if (0 == TrainWorkCardNew.index) {
					MyExt.Msg.alert('已经是第一条记录单！');
					return;
				}
				TrainWorkCardNew.index--;
				TrainWorkCardNew.initFn();
			}
		}, {
			text:'下一记录单', iconCls:'movedownIcon', id:'id_t_move_down_btn', handler: function(){
				if (TrainWorkCardNew.count - 1 == TrainWorkCardNew.index) {
					MyExt.Msg.alert('已经是最后一条记录单！');
					return;
				}
				TrainWorkCardNew.index++;
				TrainWorkCardNew.initFn();
			}
		}],		
		layout: 'border',
		items: [{
			region: 'north', height: 80, layout: 'fit', frame: true, title: '车辆检修记录单信息',collapsible: true,
			items: {
				xtype: 'fieldset',
				html: '<div id="id_jx_record_detail"></div>'
			}
		}, {
			region: 'center', layout: 'fit',
			items: {
				title: '检修记录卡', layout: 'fit',border: false, 
				items: [{	
						html: '<div id="id_jx_record_card_detail"></div>'
					}]			
				}
		}],
		listeners: {
			show: function(){
			
				TrainWorkCardNew.initFn();
			}
		}
	});
});