/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RecordScanQuery');                       // 定义命名空间
	RecordScanQuery.flag = '';
	RecordScanQuery.recordIDX = '';
	RecordScanQuery.idx = '';
	/*** 定义全局函数开始 ***/
	 RecordScanQuery.scanFn = function(idx){
		RecordScanQuery.idx = idx;
		 // Ajax请求
		Ext.Ajax.request({
			url: ctx + '/recordScanQuery!findRdpRecord.action',
			params:{id: idx},
			//请求成功后的回调函数
		    success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (null != result.partsRdp) {       //操作成功
		        	RecordCardNew.rdp = result.partsRdp;
		        	RecordCardNew.record = result.partsRecord;
		        	RecordScanQuery.flag  = "parts";
	        	  	RecordCardNew.initFn("parts");
	        	  	RecordScanQuery.recordIDX = result.partsRecord.recordIDX;
		        	Ext.getCmp("print").enable();
		        	Ext.getCmp("rdpWorkplan").enable();
		        	PartsRdpDetail.record  = result.partsRdp;
		        } else if(null != result.trainRdp){    		       
		           RecordScanQuery.flag  = "train";
		           RecordCardNew.rdp = result.trainRdp;
		           RecordCardNew.record = result.trainRecord;
		           TrainWorkPlanCommHis.workPlanStatus = result.trainRdp.workPlanStatus;
	               RecordCardNew.initFn("train"); 
	               RecordScanQuery.recordIDX = result.trainRecord.repairProjectIDX;
	               Ext.getCmp("print").enable();
		           Ext.getCmp("rdpWorkplan").enable();
		           var record = result.trainRdp;
		           TrainWorkPlanCommHis.setParams(record);		         
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	// 查询检修详情
	RecordScanQuery.qureyFn = function(){
	 	if(RecordScanQuery.flag  == "parts"){
	 		PartsRdpDetail.win.show();
	 	} 	
	 	if(RecordScanQuery.flag  == "train"){
	 		TrainWorkPlanCommHis.saveWin.show();
	 	} 	
	 }
	 // 打印
 	RecordScanQuery.printFn = function(){
 		var idx= Ext.getCmp("idx").getValue(); 
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/printerModule!getModelForPreview.action',
			params:{
				businessIDX: RecordScanQuery.recordIDX   // 记录单idx主键
			},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	var entity = result.entity
		        	var deployCatalog = entity.deployCatalog;		// 报表部署目录
					var displayName = entity.displayName;			// 报表显示名称
					var deployName = entity.deployName;				// 报表部署名称
					while(deployCatalog.indexOf('.') >= 0) {
						deployCatalog = deployCatalog.replace('.', '/');
					}
					var reportUrl = "/" + deployCatalog + "/" + deployName;
					
					var url = reportUrl + "?ctx=" + ctx.substring(1);
					var dataUrl = reportUrl + "&idx=" + idx;		// 检修记录单实例idx主键
                	window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI(displayName));
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
	/*** 定义全局函数结束 ***/
	
	/*** 查询表单 start ***/
	RecordScanQuery.searchLabelWidth = 110;
	RecordScanQuery.searchFieldWidth = 60;
	RecordScanQuery.searchForm = new Ext.form.FormPanel({
		layout: 'column', defaults: {
			columnWidth: .2, layout: 'form', 
			defaults: {
				width: RecordScanQuery.searchFieldWidth	}
		},
		items:[{
		   columnWidth: .6, layout: 'form', 
			items:[{ name: 'idx', id: 'idx', xtype:'textfield',width: 400,
			listeners: {
				"change": function(){
					RecordScanQuery.scanFn(this.getValue());
				}
			}
			}]
//		},{
//			items:[{id: 'QRcode', text:' ', xtype:'button',iconCls:'addIcon'}]			         
        },{       	
		    items:[{ id: 'print', text: "打印", iconCls: "printerIcon", xtype:'button', disabled: true, handler: RecordScanQuery.printFn 
		    }]              		
		},{
	        items:[{id: 'rdpWorkplan', text: "查看检修详情", iconCls: "", xtype:'button',disabled: true, handler: RecordScanQuery.qureyFn 
	        }] 
	    }]               		    
	});
	/*** 查询表单 end ***/
	
	/*** 界面布局 start ***/
	RecordScanQuery.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',height: 50, bodyBorder: false,
	        items:[RecordScanQuery.searchForm], frame: true,  xtype: "panel"
	    },{
	         region : 'center', layout : 'border', bodyBorder: false, autoScoll: true,
	         items : [{
		        id: 'qrcodelist', region: 'north', height: 140, layout: 'fit', frame: true, collapsible: true,
		        items:[{
					xtype: 'fieldset',title: '检修记录单基本信息',
					html: '<div id="record_detail"></div>'
				}], frame: true,  xtype: "panel"
		    },{
		        id: 'qrcode', region: 'center', layout: "fit", bodyBorder: false, 
		        items:[{
//					title: '', layout: 'fit',border: false, 
					items: [{	
							html: '<div id="record_card_detail"></div>'
						}]			
					}], title: "", xtype: "panel"
		    }]
	    }]
	};
	var viewport = new Ext.Viewport({ layout:'fit', items:RecordScanQuery.panel });

	
});