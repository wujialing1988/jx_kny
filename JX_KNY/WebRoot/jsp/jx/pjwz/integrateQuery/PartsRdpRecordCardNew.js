/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordCardNew');                       // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
//    PartsRdpRecordCardNew.index = -1;							
    PartsRdpRecordCardNew.record = null;							 // 【检修记录工单】记录对象
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修记录单详情显示模板
	 */
	PartsRdpRecordCardNew.getDetailTpl = function() {
		if (Ext.isEmpty(PartsRdpRecordCardNew.tpl)) {
			PartsRdpRecordCardNew.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%" colspan="3">{partsName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">记录单编号：</td><td width="13%" >{recordNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">记录单名称：</td><td width="13%" colspan="5">{recordName}</td>',
		            '</tr>',				
				'</table>'
			);
		}
		return PartsRdpRecordCardNew.tpl;
	}
	
	/** **************** 定义全局函数开始 **************** */
	
	// 动态生成表格列表（含嵌套列表）
	PartsRdpRecordCardNew.createShowingTable = function(data){ 

	    var list = Ext.decode(data.list);
	    var tableStr = '<table cellpadding="0" cellspacing="0">';
	    tableStr += '<thead>'
	    tableStr += '<tr>'
	    tableStr += '<td width="2%" align="center">序号</td>'
	    tableStr += '<td width="10%" >检修记录卡名称</td>'
	    tableStr += '<td width="10%" >检修检测项</td>'
	    tableStr += '<td width="42%" >技术要求</td>'
	    tableStr += '<td width="21%">结果</td>'
	    tableStr += '<td width="5%" >自检</td>'
	    tableStr += '<td width="10%">质量检查</td>'
	    tableStr += '</tr>'
	    tableStr += '</thead>'
	    tableStr += '<tbody>'
	    for (var i=0 ;i < list.length ; i++) {
	    	tableStr += '<tr>'
	    	tableStr += '<td>' + (i + 1) + '</td>' 
	    	tableStr += '<td>' + list[i].recordCardDesc + '</td>' 
	    	if (list[i].partsRdpRecordRiList && list[i].partsRdpRecordRiList.length > 0) {
	    		tableStr += '<td colspan ="3">';'</td>'; 
	    		tableStr += '<table  cellpadding="-1" cellspacing="-1">' 
	    		var riList = list[i].partsRdpRecordRiList;
	    		for (var j = 0; j < riList.length; j++) {
	    			var cls = "";
	    			if (0 === j) {
	    				cls = 'border-top:0;';
	    			}
	    			
		    		tableStr += '<tr>' 
		    		tableStr += '<td width="12%" style=" '+ cls +' border-left:0; ">' + riList[j].repairItemName + '</td>'
		    		tableStr += '<td width="56%" style=" '+ cls +' ">' + riList[j].repairStandard + '</td>'
		    		if (riList[j].partsRdpRecordDiList && riList[j].partsRdpRecordDiList.length > 0) {
			    		tableStr += '<td width="26.9%" style=" '+ cls +' ">'
			    		tableStr += '<table cellpadding="0" cellspacing="0" width="110%">' 
			    		var diList = riList[j].partsRdpRecordDiList;
			    		for (var k = 0; k < diList.length; k++) {
			    			cls = "";
			    			if (0 === k) {
			    				cls = 'border-top:0;';
			    			}
			    			
			    			var clsred = (isNaN(diList[k].dataItemResult) || (diList[k].dataItemResult >= diList[k].minResult && diList[k].dataItemResult <= diList[k].maxResult))? "":'color:red';
				    		tableStr += '<tr style = " '+ clsred +' ">' 
				    		tableStr += '<td width="50%" align="right" style=" '+ cls +' border-left:0; ">' + diList[k].dataItemName + "：" 
				    		tableStr += (1 == diList[k].isBlank ? "*" : "nbsp") + '</td>' 
				    		tableStr += '<td width="50%" align="center" style=" '+ cls +' border-left:0; ">' + (Ext.isEmpty(diList[k].dataItemResult)?"&nbsp;" :diList[k].dataItemResult) + '</td>' 
				    		tableStr += '</tr>' 
			    		}
			    		tableStr += '</table>' 
			    		tableStr += '</td>' 
		    		} else {
	    				tableStr += '<td width="26.9%" align="center" style=" '+ cls +' ">' + (Ext.isEmpty(riList[j].repairResult) ? "&nbsp;" : riList[j].repairResult) + '</td>'
		    		}
		    		tableStr += '</tr>' 
	    		}
	    		tableStr += '</table>' 
	    		tableStr += '</td>' 
	    	} else {
		    	 tableStr += '<td width="9.5%" >&nbsp</td>'
	   			 tableStr += '<td width="42%" >&nbsp</td>'
	   			 tableStr += '<td width="20.5%">&nbsp</td>'
	    	}
	    	if(!list[i].workEmpName){
	    		tableStr += '<td>' +""+'</td>';
	    	}else{
	    		tableStr += '<td>' +  list[i].workEmpName + '</td>';
	    	} 
	    	tableStr += '<td>'; 
	    	var partsRdpQRList = list[i].partsRdpQRList;
	       	if(null != partsRdpQRList && 0 < partsRdpQRList.length){   
		        for(var j = 0; j< partsRdpQRList.length; j++){
		        	tableStr = tableStr + "<li>"+ partsRdpQRList[j].qCItemName +":&nbsp&nbsp" + (Ext.isEmpty(partsRdpQRList[j].qREmpName)? "&nbsp;" :partsRdpQRList[j].qREmpName) + "</li>" ;  
		        }
	        }
	        tableStr = tableStr + "</td>";
	    	tableStr += '</tr>'
	    }
	    tableStr += '</tbody>'
	    tableStr += '</table>'
	    //将动态生成的table添加的事先隐藏的div中.  
	   return tableStr;    
 	}  
 	
   // 加载记录卡详情
	PartsRdpRecordCardNew.loadRi = function(rdpRecordIDX) {
		var dom = Ext.get('id_record_card_detail').dom;	
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/partsRdpRecordCard!integrateQueryCardList.action',
			params:{ rdpRecordIDX: rdpRecordIDX},
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);	
		        var  partsRdpRecordCardList = Ext.util.JSON.decode(result.partsRdpRecordCardList);
		        dom.innerHTML = PartsRdpRecordCardNew.createShowingTable(partsRdpRecordCardList);
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
	PartsRdpRecordCardNew.initFn = function() {
		// 获取检修记录单
		PartsRdpRecordCardNew.record = PartsRdpRecord.grid.store.getAt(PartsRdpRecordCardNew.index);
		var values = {},
		data = PartsRdpRecordCardNew.record.data;
		data.specificationModel = PartsRdpRecordCardNew.specificationModel;
		Ext.applyIf(values, data);						// 赋值检修记录单属性
		
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpRecordCardNew.getDetailTpl();
		tpl.overwrite(Ext.get('id_record_detail'), values);
		PartsRdpRecordCardNew.loadRi(data.idx);	
	}

	/** **************** 定义全局函数结束 **************** */
	
	PartsRdpRecordCardNew.win = new Ext.Window({
		title: '检修记录单详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
				
			}
		},{
			text: '打印', iconCls: 'printerImg', handler: function(){
				var data = PartsRdpRecord.grid.store.getAt(PartsRdpRecordCardNew.index).data;
				var args = [data.idx,data.recordIDX].join('-');
				PartsRdpRecord.printFn(args);
			}
		}, '->', {
			text:'上一工单', iconCls:'moveUpIcon', id:'id_r_move_up_btn', handler: function(){
				if (0 == PartsRdpRecordCardNew.index) {
					MyExt.Msg.alert('已经是第一条记录单！');
					return;
				}
				PartsRdpRecordCardNew.index--;
				PartsRdpRecordCardNew.initFn();
			}
		}, {
			text:'下一工单', iconCls:'movedownIcon', id:'id_r_move_down_btn', handler: function(){
				if (PartsRdpRecord.grid.store.getCount() - 1 == PartsRdpRecordCardNew.index) {
					MyExt.Msg.alert('已经是最后一条记录单！');
					return;
				}
				PartsRdpRecordCardNew.index++;
				PartsRdpRecordCardNew.initFn();
			}
		}],		
		layout: 'border',
		items: [{
			region: 'north',  title: '配件检修记录单信息', height: 100, layout: 'fit', frame: true, collapsible: true,
			items: {
				xtype: 'fieldset',
				html: '<div id="id_record_detail"></div>'
			}
		}, {
			region: 'center', layout: 'fit',
			items: {
				title: '检修记录卡', layout: 'fit',border: false, 
				items: [{	
						html: '<div id="id_record_card_detail"></div>'
					}]			
				}
		}],
		listeners: {
			show: function(){
				PartsRdpRecordCardNew.initFn();
			}
		}
	});
});