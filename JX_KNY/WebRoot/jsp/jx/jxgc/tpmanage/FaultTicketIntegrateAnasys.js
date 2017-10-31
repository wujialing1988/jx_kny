/**
 * 提票查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FaultTicketAnasys');                       //定义命名空间
	FaultTicketAnasys.searchParam = {};
	/*** 查询表单 start ***/
	FaultTicketAnasys.searchLabelWidth = 70;
	FaultTicketAnasys.fieldWidth = 180;
	FaultTicketAnasys.searchAnchor = '95%';
	FaultTicketAnasys.isChecked = 1;
	FaultTicketAnasys.workPlanStatus = "";

	// 【统计】按钮点击触发的函数
	FaultTicketAnasys.statisticsFn = function(value) {
		var form = FaultTicketAnasys.searchForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var data = form.getValues();
		var url = "";
		var reportUrl = "/jxgc/FaultTicketIntegrateAnasys.cpt";
		var dataUrl = "";
		var startTime = data.startTime;			// 开始时间
		var endTime = data.endTime;		// 时间

		dataUrl = reportUrl + "&start_time=" + startTime + "&end_time=" + endTime;  
		dataUrl += type;
		url = getReportEffectivePath(dataUrl);
		var h = jQuery("#report").height();
		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
	};
	
	
	FaultTicketAnasys.searchForm = new Ext.form.FormPanel({
		layout:"column", border:false, style:"padding:10px",
	    align:'center',baseCls: "x-plain",
		defaults: { 
		    layout:'form', align:'center', baseCls: "x-plain", xtype: 'panel',columnWidth: 0.5,
		    border:false
	    },
		items:[{ labelWidth: 60,
			 items: [{
			 	xtype: 'compositefield', fieldLabel:"开始时间",  combineErrors: false,
				items:[{ 
					id:"startTime", xtype:"my97date",  width: 110, hiddenName: 'startTime',format: "Y-m-d", allowBlank: false,
					my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: true
				},{	xtype: 'label', text: '  结束时间:', style: 'height:23px; line-height:23px;'	
				},{ id:"endTime", xtype:"my97date", width: 110, hiddenName: 'endTime', format: "Y-m-d", 	my97cfg: {dateFmt:"yyyy-MM-dd"},allowBlank: false,
					initNow: true
				}]	
			 }]
		},{  columnWidth: 0.25,labelWidth: 1,
			items: [{ 
			 	text:'查询', iconCls:'searchIcon', xtype: 'button', width: 80,
				handler: function(){ 					
					FaultTicketAnasys.statisticsFn();
				}	
			}]				
		},{  columnWidth: 0.25,  
			items: [{ 
					text:'重置', iconCls:'resetIcon', xtype: 'button', width: 80,
					handler: function(){ 				
					var form = FaultTicketAnasys.searchForm.getForm();
	            	form.reset();
            		FaultTicketAnasys.isChecked = 1;
					FaultTicketAnasys.statisticsFn();
				}
			}]				
	
	    }]

	});
	
	/*** 查询表单 end ***/


	// 页面自适应布局
	FaultTicketAnasys.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			title: '提票分析', frame: true,
			region: 'north',
			height: 80,
			border: true,
			collapsible: true,
			collapseMode:'mini',
			split: true,
			items: [FaultTicketAnasys.searchForm]
		}, {
			// 统计报表结果显示区域
			id:"report",
			region : 'center', layout : 'fit', bodyBorder: false, 
			split:true,
			items:[]
		}]
	});
		// 页面初始化操作
	FaultTicketAnasys.init = function(){
		 FaultTicketAnasys.statisticsFn();
	};
	FaultTicketAnasys.init();
});