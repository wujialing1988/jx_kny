/**
 * 机车上砂记录 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglSanding'); 
  /** 获取当前月份的第一天和最后一天*/
	ZbglSanding.getCurrentMonth = function(arg) {
	  var Nowdate = new Date();//获取当前date
	  var currentYear = Nowdate.getFullYear();//获取年度
	  var currentMonth = Nowdate.getMonth();//获取当前月度
	  var currentDay = Nowdate.getDate();//获取当前日
	  var MonthFirstDay = new Date(currentYear,currentMonth-1,currentDay);
		if (arg == 'begin') {
			return MonthFirstDay.format('Y-m-d');
		} else if (arg == 'end') {
			return Nowdate.format('Y-m-d');
		}
	}
    /* ************* 定义全局变量开始 ************* */
	ZbglSanding.labelWidth = 90;
	ZbglSanding.fieldWidth = 270;
	ZbglSanding.searchAnchor = '95%';
	ZbglSanding.searchParams = {};
	ZbglSanding.isOverTime = "";
	/* ************* 定义全局变量结束 ************* */
ZbglSanding.searchForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		defaults:{
				layout:"form",
				columnWidth:0.33,
				defaults:{
					xtype:"textfield",anchor:"98%" ,minListWidth: 200//width: ZbglSanding.fieldWidth
				}
		},
		items:[{
			items:[
				   {
    				id:"trainType_combo",	
    				fieldLabel: "车型",
    				hiddenName: "trainTypeIDX",
    				xtype: "Base_combo",
    			    business: 'trainType',
    			    entity:'com.yunda.jx.base.jcgy.entity.TrainType',
                    fields:['typeID','shortName'],
                    queryParams: {'isCx':'yes'},
        		    displayField: "shortName", valueField: "typeID",
                    pageSize: 0, minListWidth: 200,
                    editable:false
        		},{
				id:"startDate", fieldLabel: '入段日期<br>(开始)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
				value: ZbglSanding.getCurrentMonth('begin'), width:ZbglSanding.labelWidth}]
		},{
			items:[{
    			id:"trainNo",xtype:'textfield',fieldLabel:'车号',name:"trainNo"
			},{
				id:"overDate", fieldLabel: '入段日期<br>(结束)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
			    value: ZbglSanding.getCurrentMonth('end'),width:ZbglSanding.labelWidth
			}]
		}, {
			items:[{
				fieldLabel: '站场',
				id: 'site_combo',
				name: 'siteName',
				xtype:"Base_combo",
				business:'workPlace',
				entity:"com.yunda.jxpz.workplace.entity.WorkPlace",		
				queryHql:"from WorkPlace",
				returnField: [{widgetId:"site_combo",propertyName:"workPlaceName"}],
				fields:['workPlaceName'],
				displayField:"workPlaceName",
				valueField: "siteName",
				isAll: 'yes'
			  },{
				fieldLabel:"配属段",
				name: 'dName'
			}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			  ZbglSanding.grid.store.load();
			  var form = ZbglSanding.searchForm.getForm();
			  var searchParam = form.getValues();
			  var beginDate = form.findField("startDate").getValue();
			  var endDate = form.findField("overDate").getValue();
			  if (endDate < beginDate) {
				MyExt.TopMsg.msg('提示', "入段结束日期不能比入段开始日期早！", false,1);
				  return;
			   }	    
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
			    ZbglSanding.searchForm.getForm().reset();
                Ext.getCmp('trainType_combo').clearValue();
			    Ext.getCmp('site_combo').clearValue();
			    // 重新加载表格
			    ZbglSanding.grid.store.load();
			}
		}]
	})
	
 /************ 上砂用时不足标准时间 结束 **************/
//定义命名空间
ZbglSanding.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglSanding!findSandingList.action',   //装载列表数据的请求URL
    tbar: ['refresh',
            {text:"打印", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglSanding.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("startDate").getValue() != ""){
		        	startDate = Ext.getCmp("startDate").getValue().format('Y-m-d H:i');
		        }
		        if(Ext.getCmp("overDate").getValue() != ""){
		        	overDate = Ext.getCmp("overDate").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/trainonsand/ZbglSanding.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeIDX=" + data.trainTypeIDX +
				"&trainNo=" + data.trainNo + "&dName=" + data.dName + "&siteName=" + data.siteName;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车上砂记录"));
    	     }
     },{
     	id:"ck",
		xtype:"checkbox", boxLabel:"只显示不足标准时间记录",
		handler: function(){
		  if(Ext.getCmp("ck").checked){
		  	ZbglSanding.isOverTime = 0;
		  } else {
		  	ZbglSanding.isOverTime = "";
		  }
		  ZbglSanding.grid.store.load();
		}
     }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车出入段台账主键', dataIndex:'trainAccessAccountIDX', hidden:true,editor:{  maxLength:50 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
        header:'配属段',dataIndex:'dName',editor:{ maxLength:50}
	},{
        header:'入段时间', dataIndex:'inTime', xtype:'datecolumn',editor:{ xtype:'my97date'},format:'Y-m-d H:i:s',width: 300
	},{
		header:'责任人ID', dataIndex:'dutyPersonId', hidden:true,editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'责任人', dataIndex:'dutyPersonName', editor:{  maxLength:50 }
	},{
		header:'上砂开始时间', dataIndex:'startTime', xtype:'datecolumn', format:'Y-m-d H:i:s',width: 300
	},{
		header:'上砂结束时间', dataIndex:'endTime', xtype:'datecolumn', format:'Y-m-d H:i:s',width: 300
	},{
		header:'上砂时间(分钟)', dataIndex:'sandingTime', editor:{ xtype:'numberfield' },width: 200,
		renderer: function(v) {
			if(!Ext.isEmpty(v)) {
				return (v/60).toFixed(2);
			}
			return '';
		}
	},{
		header:'标准上砂时间(分钟)', dataIndex:'standardSandingTime', editor:{ xtype:'numberfield' },width: 200,
		renderer: function(v) {
			if(!Ext.isEmpty(v)) {
				return (v/60).toFixed(2);
			}
			return '';
		}
	},{
		header:'是否超时', dataIndex:'isOverTime', hidden:true,editor:{ xtype:'numberfield', maxLength:1 },
		renderer: function(v) {
				if (v == 0) {
					var id = "clr"+Math.random()
					setTimeout(function(){
						var node = document.getElementById(id);
						if (null != node) {
							node.parentNode.parentNode.parentNode.style.color= 'red';
						}
					},50);
				}
			}
	},{
		header:'上砂量(KG)', dataIndex:'sandNum'
	},{
		header:'站场ID', dataIndex:'siteId', hidden:true,editor:{  maxLength:50 }
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	}]
});
    //查询前验证
	ZbglSanding.grid.store.on('beforeload', function() {
		ZbglSanding.searchParams = ZbglSanding.searchForm.getForm().getValues();
		// ？MyJson.deleteBlankProp方法会删除0属性？
		var searchParams = MyJson.deleteBlankProp(ZbglSanding.searchParams);
		searchParams.isOverTime = ZbglSanding.isOverTime;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
    //表格加载自动将不足标准用时的上砂记录标红
ZbglSanding.grid.store.on('load', function(r) {
	 var gridcount = 0;
	 this.each(function(r){
	 if(r.get('isOverTime') == 0){
       ZbglSanding.grid.getView().getRow(gridcount).childNodes[0].style.color = 'red';
     }
     	gridcount = gridcount + 1; 
	 })
});

ZbglSanding.grid.un('rowdblclick',ZbglSanding.grid.toEditFn,ZbglSanding.grid);
  	// 页面自适应布局
	ZbglSanding.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 143,
			collapsible: true,
			items: [ZbglSanding.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center', 
			layout: "fit",
			bodyStyle:'padding-left:0px;', 
            bodyBorder: true,
			items: [ZbglSanding.grid]
		}]
	});
});