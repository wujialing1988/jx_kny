/**
 * 普查整治单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczz');   //定义命名空间

   /** 获取最近的一个月 */
	var newDate = new Date();
	var nowDate = newDate.format('Y-m-d H:i:s');
	
	//最近一个月
	ZbglPczz.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	
	/* ************* 定义全局变量开始 ************* */
	ZbglPczz.labelWidth = 100;
	ZbglPczz.fieldWidth = 150;
	ZbglPczz.searchParams = {};
	ZbglPczz.zbglPczzIdx = "";
	ZbglPczz.workStatusString;
	/* ************* 定义全局变量结束 ************* */

	 /**
	 * 普查单查询Form
	 */
	ZbglPczz.searchForm = new Ext.form.FormPanel({
		labelWidth: 80,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			defaults:{
				xtype:"textfield", width: ZbglPczz.fieldWidth
			}
		},
		items:[{
			columnWidth:0.5,
			items:[{
				fieldLabel:"普查计划名称",
				name: 'pczzName'
			}]
		},{
			columnWidth:0.5,
			fieldWidth:270,
			labelWidth: 80,
			defaults:{anchor:"99%"},
			items:[{
				xtype: 'compositefield', fieldLabel : '普查发布时间', combineErrors: false,
	        	items: [{ 
	        		id:"releaseStartDateID",name: "releaseStartDate",  xtype: "my97date",format: "Y-m-d",initNow:false,width: 100
	        	},{
	        		xtype:'label', text: '至：'
	        	},{ 
	        		id:"releaseEndDateID",name: "releaseEndDate",initNow:false, xtype: "my97date",width: 100
				}]
			}]
		},{
			columnWidth:0.5,
			fieldWidth:270,
			labelWidth: 80,
			defaults:{anchor:"99%"},
			items:[{
				xtype: 'compositefield', fieldLabel : '普查开始时间', combineErrors: false,
	        	items: [{ 
	        		id:"startDateID",name: "startDate",  xtype: "my97date",format: "Y-m-d",value : ZbglPczz.getCurrentMonth(),width: 100
	        	},{
	        		xtype:'label', text: '至：'
	        	},{ 
	        		id:"endDateID",name: "endDate", xtype: "my97date",format: "Y-m-d", width: 100
				}]
			}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
				var form = ZbglPczz.searchForm.getForm();
				//刷新作业记录grid
				if (form.isValid()) {
					ZbglPczz.grid.store.load();
				}
				//刷新归档记录grid
				if (form.isValid()) {
					ZbglPczz.completeGrid.store.load();
				}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    ZbglPczz.searchForm.getForm().reset();
				    // 重新加载作业记录grid
				    ZbglPczz.grid.store.load();
				    // 重新加载归档记录grid
				    ZbglPczz.completeGrid.store.load();
			}
		}]
	})
	
	/**
	 * 普查单新增Form
 	*/
	ZbglPczz.saveForm = new Ext.form.FormPanel({
	    id:'saveOrsearchPanel',
		labelWidth: 65,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.5,
			defaults:{
				xtype:"textfield", width: ZbglPczz.fieldWidth,allowBlank:false
			}
		},
		items:[{
			items:[{
				fieldLabel:"开始时间",
				name: 'startDate',
				id:'startDate',
				xtype:'my97date',
				format:'Y-m-d H:i:s',
				initNow:false,	// 日期校验器
				value:nowDate,
		        validator: function() {
					var startDate =new Date(Ext.getCmp('startDate').getValue());
					var endDate = new Date(Ext.getCmp('endDate').getValue());
					if (startDate > endDate) {
						return "开始日期不能大于结束日期";
					}
				}
			},{
				fieldLabel:"普查名称",
				name: 'pczzName',
				maxLength:50
			}]
		},{
		  	items:[{
		  	  	fieldLabel:"结束时间",
				name: 'endDate',
				id:'endDate',
				xtype:'my97date',
				format:'Y-m-d H:i:s',
				initNow:false,
				allowBlank:true,
				// 日期校验器
				validator: function() {
					var startDate =new Date(Ext.getCmp('startDate').getValue());
					var endDate = new Date(Ext.getCmp('endDate').getValue());
					if (startDate > endDate) {
						return "结束日期不能小于开始日期";
					}
				}
		  	}]
		},{
			columnWidth:1,
			items:[{
		  		xtype:'textarea',
		  		width: 500,
		  	  	fieldLabel:"任务要求",
				name: 'pczzReq',
				maxLength:300
		  	},{
				name: 'status',
				fieldLabel:'状态',
				xtype:"hidden"
			},{
				name: 'idx',
				xtype:"hidden"
			}]
		}], 
		buttonAlign: 'center',
        buttons: [{
           text: "保存", id:'savebutton',iconCls: "saveIcon", handler: function(){
				ZbglPczz.grid.saveFn();
           }
          }, {
           text: "关闭", iconCls: "closeIcon", handler: function(){ this.findParentByType('window').hide(); }
        }]
	})
	
   
	 // 发布，归档操作处理
	ZbglPczz.updateStatusFn = function(status) {
		//判断有无选择选择行
		if (!$yd.isSelectedRecord(ZbglPczz.grid))
			return;
		//获得选中行的数据	
		var datas = ZbglPczz.grid.selModel.getSelections();
		//判断当前操作是归档还是发布
		if(status == "COMPLETE"){	
			//如果归档操作的对象，如果还未发布，返回操作，提示未发布
		    if(datas[0].data.status != "RELEASED") {
		    MyExt.Msg.alert("该整治计划还未发布！"); 
		    return;
		    }
		}else{
			//发布对象的已经发布过了，返回操作，提示已经发布
		    if(datas[0].data.status == "RELEASED") {
		    MyExt.Msg.alert("该整治计划已经发布！"); 
		    return;
		    }
		}
		var ids = $yd.getSelectedIdx(ZbglPczz.grid);
		ZbglPczz.grid.loadMask.show();
		Ext.Ajax.request({
			url : ctx + "/zbglPczz!updateStatus.action",
			params : {
				ids : ids,
				status : status
			},
			success : function(response, options) {
				ZbglPczz.grid.loadMask.hide();
				var result = Ext.util.JSON
						.decode(response.responseText);
				if (result.success) {
					//提示操作成功
					alertSuccess();
					//刷新表格页面
					ZbglPczz.grid.store.load();
					ZbglPczz.completeGrid.store.load();
				} else {
					alertFail(result.errMsg);
				}
			},
			failure : function(response, options) {
				ZbglPczz.grid.loadMask.hide();
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status
						+ "\n" + response.responseText);
			}
		});
	}
	
	ZbglPczz.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczz!findZbglPczzPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglPczz!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglPczz!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,
    saveForm: ZbglPczz.saveForm,
    tbar:['add','delete',{
    	    text:"发布", iconCls:"pluginIcon",handler:function(){
    	    	ZbglPczz.updateStatusFn(STATUS_RELEASED);
    	    }
    	  },{
    	    text:"归档", iconCls:"cmpIcon",handler:function(){
    	    	ZbglPczz.updateStatusFn(STATUS_COMPLETE);
    	    }
    	  }, 'refresh',{
    	    text:"EXCEL", iconCls:"resetIcon",
    	    	handler: function(){
    	    	
	    	    	//判断有无选择选择行
					if (!$yd.isSelectedRecord(ZbglPczz.grid))
						return;
					//获得选中行的数据	
					var datas = ZbglPczz.grid.selModel.getSelections();
    	    	
					var url = ctx + "/zbglPczz!exportZbglPczzListByParm.action";
	            	var params = [];
			        data = {name : "zbglPczzIDX",value : datas[0].data.idx}
	                params.push(data);
	            	exportExcel(url,params);
	    	    }
    	  },'-',{   
		    	xtype:"checkbox",name:"isFinish", 
		    	boxLabel:'已完毕'+"&nbsp;&nbsp;&nbsp;&nbsp;",
		    	id: 'WORK_STATUS_END',inputValue:WORK_STATUS_END,
		    	checked: true,
			    handler: function(){
			    	ZbglPczz.checkQuery();
		    		ZbglPczz.grid.store.load();
			    }
		    },{   
		    	xtype:"checkbox",name:"isFinish", 
		    	boxLabel:'作业中', 
		    	id: 'WORK_STATUS_ONGOING',inputValue:WORK_STATUS_ONGOING,
		    	checked: true,
		    	handler: function(){
			    	ZbglPczz.checkQuery();
		    		ZbglPczz.grid.store.load();
			    }
			}],
	fields: [
	//隐藏字段
	{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'发布单位编码', dataIndex:'releaseJGDM', hidden:true, editor:{  maxLength:50 }
	},{
		header:'任务类型', dataIndex:'pczzClass', hidden:true,editor:{  maxLength:20 ,xtype:'hidden'}
	},{
		header:'任务单号', dataIndex:'pczzNo',hidden:true, editor:{maxLength:50 }
	},{
		header:'发布单位', dataIndex:'releaseJGMC', hidden:true,editor:{  maxLength:50 }
	},
	
	//显示字段
	//文件上传
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_PCZZ, attachmentKeyIDX:'idx'}),
	{
		header:'普查名称', dataIndex:'pczzName', editor:{maxLength:500},
		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
            var html = "";
            html = "<span><a href='#' onclick='ZbglPczz.grid.toEditFn(\""+ ZbglPczz.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
            return html;
        }
	},{
		header:'开始时间', dataIndex:'startDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
	},{
		header:'结束时间', dataIndex:'endDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
	},{
		header:'任务状态', dataIndex:'status', editor:{  maxLength:20,xtype:'hidden' },
		renderer: function(v){
            switch(v){
                case STATUS_TORELEASE:
                    return STATUS_TORELEASE_CH;
                case STATUS_RELEASED:
                    return STATUS_RELEASED_CH;
                case STATUS_COMPLETE:
                    return STATUS_COMPLETE_CH;
                default:
                    return v;
            }
        }
	},{
		header:'作业情况', dataIndex:'workStatus', editor:{  maxLength:20,xtype:'hidden' },
		renderer: function(v){
            switch(v){
                case WORK_STATUS_END:
                    return "已完毕";
                case WORK_STATUS_ONGOING:
                    return "作业中";
                default:
                    return v;
            }
        }
	},{
	//任务要求
		header:'普查概述', dataIndex:'pczzReq', editor:{maxLength:500},
	},{
		header:'发布时间', dataIndex:'releaseDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
	},{
	//任务要求
		header:'发布人名称', dataIndex:'releasePersonName', editor:{maxLength:500},
	}],
	createSaveWin: function(){
        if(ZbglPczz.grid.saveForm == null) ZbglPczz.grid.createSaveForm();
        ZbglPczz.grid.saveWin = new Ext.Window({
            title:"新增普查整治任务", width:700, height:500, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
            layout: 'border',
            items: [{
		            region: 'north', layout: "fit", height:180,frame: true, bodyBorder: false,items:[this.saveForm]
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : ZbglPczzItem.grid
		        }]
        });
    },
	afterShowSaveWin: function(){
		ZbglPczz.saveForm.find('name', 'status')[0].setValue(STATUS_TORELEASE);
		this.enableAllColumns();
		Ext.getCmp('savebutton').enable();
		ZbglPczzItem.grid.getTopToolbar().disable();
		ZbglPczzItem.grid.store.removeAll();
	},
	afterSaveSuccessFn: function(result, response, options){
	   ZbglPczz.grid.store.reload();
	   alertSuccess();
	   ZbglPczz.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	   ZbglPczzItem.grid.getTopToolbar().enable();
	   ZbglPczz.zbglPczzIdx = result.entity.idx;
	   ZbglPczzItem.grid.store.load();
	 },
	afterShowEditWin: function(record, rowIndex){
       this.saveWin.setTitle('普查整治项');
       var status = record.get('status');
		if (STATUS_TORELEASE == status) {
			ZbglPczzItem.grid.on('rowdblclick', ZbglPczzItem.grid.toEditFn, ZbglPczzItem.grid);
		} else {
			ZbglPczzItem.grid.un('rowdblclick', ZbglPczzItem.grid.toEditFn, ZbglPczzItem.grid);
		}
     },
	beforeShowEditWin: function(record, rowIndex){
	  if(record.get("status") != STATUS_TORELEASE){
		   	 this.disableAllColumns();
		   	 ZbglPczz.zbglPczzIdx = record.get("idx");
		     ZbglPczzItem.grid.store.load();
		   	 ZbglPczzItem.grid.getTopToolbar().disable();
		   	 Ext.getCmp('savebutton').disable();
			 return true;
		}
	  else
	        this.enableAllColumns();
		    ZbglPczz.zbglPczzIdx = record.get("idx");
		    ZbglPczzItem.grid.store.load();
		    ZbglPczzItem.grid.getTopToolbar().enable();
		    Ext.getCmp('savebutton').enable();
			return true;
	},
	beforeDeleteFn: function(){
			var records = ZbglPczz.grid.selModel.getSelections();	    	
	    	var filter = 0;
	    	for(var i = 0; i < records.length; i++){
	    		if(records[i].get("status") == STATUS_RELEASED || records[i].get("status") == STATUS_COMPLETE){
	    			filter++;
	    			ZbglPczz.grid.selModel.deselectRow(ZbglPczz.grid.store.indexOfId(records[i].get("idx")));
	    		}
	    	}
	    	if(filter == records.length && filter != 0){
	    		MyExt.Msg.alert("所选记录已发布或已归档不能被删除");
	    		return false;
	    	}
	    	if(filter > 0){
	    		MyExt.Msg.alert(filter + "条记录不能被删除");
	    		return false;
	    	}
	    	return true;
		 },
		beforeSaveFn: function(data){ 
			//新增的时候，默认设置普查计划作业情况为作业中1
			data.workStatus = WORK_STATUS_ONGOING;
			return true; 
		}
	});


	//页面加载和数据模糊查询
	ZbglPczz.grid.store.on('beforeload', function() {
		ZbglPczz.searchParams = ZbglPczz.searchForm.getForm().getValues();
		ZbglPczz.searchParams.workStatusString = ZbglPczz.workStatusString;//添加作业情况查询条件
		ZbglPczz.searchParams.status = STATUS_TORELEASE+","+STATUS_RELEASED;//添加任务情况条件归档
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczz.searchParams);
	});
	
	//归档记录grid
	ZbglPczz.completeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczz!findZbglPczzPageList.action',                 //装载列表数据的请求URL
    tbar:[],
	fields: [
		//隐藏字段
		{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'发布单位编码', dataIndex:'releaseJGDM', hidden:true, editor:{  maxLength:50 }
		},{
			header:'任务类型', dataIndex:'pczzClass', hidden:true,editor:{  maxLength:20 ,xtype:'hidden'}
		},{
			header:'任务单号', dataIndex:'pczzNo',hidden:true, editor:{maxLength:50 }
			
		},{
			header:'发布单位', dataIndex:'releaseJGMC', hidden:true,editor:{  maxLength:50 }
		},
		
		//显示字段
		//文件上传
		Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_PCZZ, attachmentKeyIDX:'idx'}),
        {
			header:'普查名称', dataIndex:'pczzName', editor:{maxLength:500},
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	            html = "<span><a href='#' onclick='ZbglPczz.grid.toEditFn(\""+ ZbglPczz.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	            return html;
	        }
		},{
			header:'开始时间', dataIndex:'startDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
		},{
			header:'结束时间', dataIndex:'endDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
		},{
		//任务要求
			header:'普查概述', dataIndex:'pczzReq', editor:{maxLength:500},
		},{
			header:'发布时间', dataIndex:'releaseDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
		},{
		//任务要求
			header:'发布人名称', dataIndex:'releasePersonName', editor:{maxLength:500},
		}]
	});


	//页面加载和数据模糊查询
	ZbglPczz.completeGrid.store.on('beforeload', function() {
		ZbglPczz.searchParams = ZbglPczz.searchForm.getForm().getValues();
		ZbglPczz.searchParams.status = STATUS_COMPLETE;//添加任务情况条件归档
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczz.searchParams);
	});
	
	//编辑选项卡列表
	ZbglPczz.tabs = new Ext.TabPanel({
	    activeTab: 0, enableTabScroll:true, border:false,
	    items:[{
	            title: "作业记录", layout: "fit", border: false, items: [ ZbglPczz.grid ]
	        },{
	            title: "归档记录", layout: "fit", border: false, items: [ ZbglPczz.completeGrid ]
	        }]
	});

	/*** 界面布局 start ***/
	ZbglPczz.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        // 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 143,
			collapsible: true,
			items: [ZbglPczz.searchForm]
	    },{
	        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglPczz.tabs ]
	    }]
	};

 	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:ZbglPczz.panel });
	
	//状态多选按钮
	ZbglPczz.checkQuery = function(){
		ZbglPczz.workStatusString = "-1";
		if(Ext.getCmp("WORK_STATUS_END").checked){
			ZbglPczz.workStatusString = ZbglPczz.workStatusString + "," + WORK_STATUS_END;
		} 
		if(Ext.getCmp("WORK_STATUS_ONGOING").checked){
			ZbglPczz.workStatusString = ZbglPczz.workStatusString + "," + WORK_STATUS_ONGOING;
		} 
		ZbglPczz.grid.store.load();
	}
	
});