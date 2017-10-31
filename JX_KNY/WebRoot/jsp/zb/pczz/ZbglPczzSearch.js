/**
 * 普查整治单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczzSearch');   //定义命名空间

	/* ************* 定义全局变量开始 ************* */
	ZbglPczzSearch.labelWidth = 100;
	ZbglPczzSearch.fieldWidth = 150;
	ZbglPczzSearch.searchParams = {};
	ZbglPczzSearch.zbglPczzIdx = "";
	/* ************* 定义全局变量结束 ************* */

 /**
 * 普查单查询Form
 */
ZbglPczzSearch.searchForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.25,
			defaults:{
				xtype:"textfield", width: ZbglPczzSearch.fieldWidth
			}
		},
		items:[{
			items:[{
				fieldLabel:"任务编号",
				name: 'pczzNo'
			}]
		},{
		  	items:[{
		  		width:200,
		  	  	fieldLabel:"发布单位",
				name: 'releaseJGMC'
		  	}]
		},{
		  	items:[{
		  		width:200,
		  	  	fieldLabel:"任务状态",
				name: 'status',
				xtype: 'combo',
            	hiddenName: 'status', 
            	displayField:'v',
            	valueField:'t',
            	store:new Ext.data.SimpleStore({
                        fields: ['v', 't'],
                        data : [
                                [STATUS_TORELEASE_CH,STATUS_TORELEASE],
                                [STATUS_RELEASED_CH,STATUS_RELEASED],
                                [STATUS_COMPLETE_CH,STATUS_COMPLETE]
                               ]
                    }),
            	triggerAction:'all',
            	mode:'local'
		  	}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			 var form = ZbglPczzSearch.searchForm.getForm();
				if (form.isValid()) {
					ZbglPczzSearch.grid.store.load();
				}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    ZbglPczzSearch.searchForm.getForm().reset();
				    // 重新加载表格
				    ZbglPczzSearch.grid.store.load();
			}
		}]
	})
	
/**
 * 普查单基本信息Form
 */
ZbglPczzSearch.baseInfoForm = new Ext.form.FormPanel({
		labelWidth: 65,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.5,
			defaults:{
				xtype:"textfield", width: ZbglPczzSearch.fieldWidth,allowBlank:true
			}
		},
		items:[{
			items:[{
				fieldLabel:"任务编号",
				name: 'pczzNo'
			},{
				fieldLabel:"开始时间",
				name: 'startDate',
				xtype:'my97date',
				format:'Y-m-d H:i:s',
				initNow:false
			}]
		},{
		  	items:[{
		  	  	fieldLabel:"发布单位",
				name: 'releaseJGMC'
		  	},{
		  	  	fieldLabel:"结束时间",
				name: 'endDate',
				xtype:'my97date',
				format:'Y-m-d H:i:s',
				initNow:false
		  	}]
		},{
			columnWidth:1,
			items:[{
		  		xtype:'textarea',
		  		width: 500,
		  	  	fieldLabel:"任务要求",
				name: 'pczzReq'
		  	}]
		},{
			columnWidth:1,
			items:[{
		  	  	fieldLabel:"任务状态",
				name: 'status'
		  	}]
		}]
	})
   
ZbglPczzSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczz!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglPczz!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglPczz!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,
    singleSelect:true,
    saveForm: ZbglPczzSearch.baseInfoForm,
    tbar:['refresh',
            {text:"打印", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglPczzSearch.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var reportUrl = "/zb/pczz/ZbglPczzSearch.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&pczzNo=" + data.pczzNo + "&releaseJGMC=" + data.releaseJGMC + "&status=" + data.status ;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("普查整治"));
    	     }
     }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'任务单号', dataIndex:'pczzNo', editor:{maxLength:50 },
		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
            var html = "";
            html = "<span><a href='#' onclick='ZbglPczzSearch.grid.toEditFn(\""+ ZbglPczzSearch.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
            return html;
        }
	},{
		header:'任务要求', dataIndex:'pczzReq', editor:{maxLength:500}
	},{
		header:'发布单位编码', dataIndex:'releaseJGDM', hidden:true, editor:{  maxLength:50 }
	},{
		header:'发布单位名称', dataIndex:'releaseJGMC', editor:{  maxLength:50 }
	},{
		header:'开始日期', dataIndex:'startDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
	},{
		header:'结束日期', dataIndex:'endDate', xtype:'datecolumn', editor:{xtype:'my97date', format:'Y-m-d H:i:s'},width: 150
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
		header:'任务类型', dataIndex:'pczzClass', hidden:true,editor:{  maxLength:20 ,xtype:'hidden'}
	}],
	createSaveWin: function(){
        this.disableAllColumns();
        ZbglPczzSearch.grid.saveWin = new Ext.Window({
        title:"普查整治任务", width:700, height:500, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
	    items: [{
	            region: 'north', layout: "fit",height:180,frame: true, bodyBorder: false,items:[this.saveForm]
	        },{
	            region : 'center', layout : 'fit',frame: true,  bodyBorder: false, height:250, items : ZbglPczzItemSearch.grid
	        }]
	});
},
beforeShowEditWin: function(record, rowIndex){
  	ZbglPczzSearch.zbglPczzIdx = record.get("idx");
	ZbglPczzItemSearch.grid.store.load();
  	return true; 
  },
afterShowEditWin: function(record, rowIndex){
    this.saveWin.setTitle('普查整治任务');
    var status = record.get('status');
	  if (STATUS_TORELEASE == status) {
			ZbglPczzItemSearch.grid.on('rowdblclick', ZbglPczzItemSearch.grid.toEditFn, ZbglPczzItemSearch.grid);
	   } else {
			ZbglPczzItemSearch.grid.un('rowdblclick', ZbglPczzItemSearch.grid.toEditFn, ZbglPczzItemSearch.grid);
    }
    var statusField = this.saveForm.find('name', 'status')[0];
    var value = statusField.getValue();
    if(value == STATUS_TORELEASE){
    	statusField.setValue("");
    	statusField.setValue(STATUS_TORELEASE_CH);
    	return;
    }
    if(value == STATUS_RELEASED){
    	statusField.setValue("");
    	statusField.setValue(STATUS_RELEASED_CH);
    	return;
    }
    if(value == STATUS_COMPLETE){
    	statusField.setValue("");
    	statusField.setValue(STATUS_COMPLETE_CH);
    	return;
    }
   }
});

//页面加载和数据模糊查询
ZbglPczzSearch.grid.store.on('beforeload', function() {
	ZbglPczzSearch.searchParams = ZbglPczzSearch.searchForm.getForm().getValues();
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzSearch.searchParams);
 })

 // 页面自适应布局
ZbglPczzSearch.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 143,
			collapsible: true,
			items: [ZbglPczzSearch.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center', 
			layout: "fit",
			bodyStyle:'padding-left:0px;', 
            bodyBorder: true,
			items: [ZbglPczzSearch.grid]
		}]
	});
});