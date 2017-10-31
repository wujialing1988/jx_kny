Ext.onReady(function(){
Ext.namespace('ZbglTpCheck');                       //定义命名空间
ZbglTpCheck.searchParam = {};
/*** 查询表单 start ***/
ZbglTpCheck.searchLabelWidth = 90;
ZbglTpCheck.searchAnchor = '95%';
ZbglTpCheck.searchFieldWidth = 270;

ZbglTpCheck.Status = 'TODO';
//最近一个月
ZbglTpCheck.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	return MonthFirstDay.format('Y-m-d');
}
ZbglTpCheck.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglTpCheck.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: ZbglTpCheck.searchFieldWidth, labelWidth: ZbglTpCheck.searchLabelWidth, defaults:{anchor:ZbglTpCheck.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				hiddenName: "trainTypeIDX",
				displayField: "shortName", valueField: "typeID",
				pageSize: 0, minListWidth: 200,
				editable:true,
				forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = ZbglTpCheck.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}    	
			},{ 
				fieldLabel: "提票单号", name: 'faultNoticeCode', xtype: 'textfield'
			},{
				xtype: 'compositefield', fieldLabel : '提票日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "noticeTime",  xtype: "my97date",format: "Y-m-d",  
		             value : ZbglTpCheck.getCurrentMonth(),
					width: 100},
					{
			    	    xtype:'label', text: '至：'
				    },
					{ id:"toWarehousingTime",name: "noticeTime", xtype: "my97date",format: "Y-m-d",  
					width: 100}]
			}
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: ZbglTpCheck.searchFieldWidth, labelWidth: ZbglTpCheck.searchLabelWidth, defaults:{anchor:ZbglTpCheck.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
//				minLength : 4, 
				maxLength : 4,
				vtype: "numberInt",
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
				isAll: 'yes',
				editable:true
			},{ fieldLabel: "发现人", name: 'discover', xtype: 'textfield'}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglTpCheck.searchForm.getForm();	
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                	searchParam = MyJson.deleteBlankProp(searchParam);
			
				ZbglTpCheck.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglTpCheck.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo","EosDictEntry_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = ZbglTpCheck.searchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeIDX;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	ZbglTpCheck.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/


ZbglTpCheck.saveForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false, frame: true, labelWidth: ZbglTpCheck.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:50,
            columnWidth: 0.5, 
            items: [
        		{ fieldLabel:"放行人", name:"handlePersonName", value: empname, width:50, style:"border:none;background:none;", readOnly:true},
        		{ xtype: "hidden", name: "handlePersonId", value: empid}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:60,
            columnWidth: 0.5, 
            items: [
            	{ fieldLabel:"放行时间", name:"handleTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, width: 150, allowBlank: false}
            ]
        }]
    },{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", anchor: "98%" ,
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", 
            labelWidth: 60,      columnWidth: 1, 
            items: [{ xtype: "textarea", fieldLabel:"放行原因", name:"exceptionReason", width:300, maxLength: 300}]
       }]
    }]
});
ZbglTpCheck.saveFn = function() {
    var form = ZbglTpCheck.saveForm.getForm(); 
    if (!form.isValid()) return;
    var data = form.getValues();   
    
    var datas = ZbglTpCheck.grid.selModel.getSelections();
    if (datas.length < 1) {
    	MyExt.Msg.alert("请选择提票单记录");
	    return;
    }
    var tpExceptionAry = [];
	for(var i = 0; i < datas.length; i++){
		var tpException = {};
		tpException.rdpIDX = datas[i].get("rdpIDX");
		tpException.tpIDX = datas[i].get("idx");
		tpException.exceptionReason = data.exceptionReason;
		tpException.handlePersonId = data.handlePersonId;
		tpException.handlePersonName = data.handlePersonName;
		tpException.handleTime = data.handleTime;
        tpExceptionAry.push(tpException);
    }  
    var params = {
    	tpExceptionAry : Ext.util.JSON.encode(tpExceptionAry)
    };
    
    if(ZbglTpCheck.grid.loadMask)   ZbglTpCheck.grid.loadMask.show();
    var cfg = {
        scope: this, url: ctx + '/zbglTpException!saveForLwfx.action', 
        params: params,
        success: function(response, options){
            if(ZbglTpCheck.grid.loadMask)   ZbglTpCheck.grid.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true) {
            	ZbglTpCheck.grid.store.load();
            	ZbglTpCheck.saveWin.hide();
            	alertSuccess();                
            } else {
                alertFail(result.errMsg);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
}

ZbglTpCheck.saveWin = new Ext.Window({
    title:"放行原因填写", width: 450, height:170,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:ZbglTpCheck.saveForm, 
    buttons: [{
        text: "确认", iconCls: "saveIcon", handler: ZbglTpCheck.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglTpCheck.saveWin.hide(); }
    }]
});



/*** 提票列表 start ***/
ZbglTpCheck.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!findTpWhenToDoAndOnGoing.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/zbglTp!delete.action',
    viewConfig: null,
    tbar:[{
	    	text:"遗留活", iconCls:"addIcon",
	    	handler: function() {
	    		//在开始跟踪操作前，重载表格，
	    		ZbglTpCheck.grid.store.load();
	    		var sm = ZbglTpCheck.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择一条记录！');
					return;
				}
				if (sm.getCount() > 1) {
					MyExt.Msg.alert('请只选择一条查看！');
					return;
				}
	    		Ext.Msg.confirm("提示  ", "是否要对该提票遗留活？  ", function(btn){
                if(btn != 'yes')    return;	
                ZbglTpCheck.saveWin.show();
                
//				Ext.Ajax.request({
//                    url: ctx + '/zbglTp!saveReleaseRelation.action',  
//                    params: {
//                             jt6IDX: idx,
//                             rdpIDX: rdpIDX
//                             },
//                    success: function(respinse, options){
//                   //   var result = Ext.util.JSON.decode(response.responseText);
//                        MyExt.Msg.alert("操作成功！已被遗留活");  
//                        ZbglTpCheck.grid.store.load();
//                    },
//                    failure: function(respinse, options){
//                        MyExt.Msg.alert("操作失败！");
//                        ZbglTpCheck.grid.store.load();
//                    }
//                });
	            });
	    	}
	    },{
	    	text:"恢复遗留活", iconCls:"editIcon",
	    	handler: function() {
	    		ZbglTpException.showWin.show();
	    		ZbglTpException.grid.store.load();
	    	}
	    },'-','delete','refresh','-',{   
    	xtype:"checkbox",name:"tpStatus", 
    	boxLabel:'待接活'+"&nbsp;&nbsp;&nbsp;&nbsp;",
    	id: 'todo',inputValue:"TODO",
    	checked:true, 
	    handler: function(){
	    	ZbglTpCheck.checkQuery();
    		ZbglTpCheck.grid.store.load();
	    }
    },
    {   
    	xtype:"checkbox",name:"tpStatus", boxLabel:'待销活', 
    	id: 'ongoing',inputValue:"ONGOING",
    	checked:true,
    	handler: function(){
	    	ZbglTpCheck.checkQuery();
    		ZbglTpCheck.grid.store.load();
	    }
	}],
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	Attachment.createColModeJson({ header:'文件附件',width:70,attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx', disableButton:['删除','新增']}),
	Attachment.createColModeJson({ header:'图片附件',width:70,attachmentKeyName:UPLOADPATH_TP_IMG, attachmentKeyIDX:'idx', disableButton:['删除','新增']}),
    {
        header:'录音记录', width:60, dataIndex:'audioAttIdx',
        renderer: function(value, metadata, record, rowIndex, colIndex, store){
            if(Ext.isEmpty(value))  return '';
            var html = '<img src="' + ctx 
                + '/frame/resources/images/toolbar/control_play_blue.png" border="0" style="cursor:hand" onclick=AttAudio.play("' + value + '")>';
            return html;
        }
    },{
		header:'是否跟踪', dataIndex:'isTracked', editor:{  maxLength:20 },hidden:true,
        renderer: function(v){
            switch(v){
                case ISTRACKED_YES:
                    return "跟踪";
                case ISTRACKED_NO:
                    return "未跟踪";
                default:
                    return "未跟踪";
            }
        }
	},{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
	},{
		header:'提票人', dataIndex:'noticePersonName', editor:{  maxLength:25 }, width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'发现人', dataIndex:'discover',  width : 80
	},{
		header:'故障部件', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
	},{
		header:'故障原因', dataIndex:'faultReason', editor:{  maxLength:500 },width : 200
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	},{
		header:'提票状态', dataIndex:'faultNoticeStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case STATUS_INIT:
                    return STATUS_INIT_CH;
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                case STATUS_CHECK:
                    return STATUS_CHECK_CH;
                default:
                    return v;
            }
        }
	},{
		header:'配属段编码', dataIndex:'dID', hidden:true, editor:{  maxLength:20 }
	},{
		header:'配属段名称', dataIndex:'dName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'故障部件编码', dataIndex:'faultFixFullCode', hidden:true, editor:{  maxLength:200 }
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true, editor:{  maxLength:50 }
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true, editor:{  maxLength:300 }
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接票人', dataIndex:'revPersonName', editor:{  maxLength:25 }, hidden:true
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150, hidden:true
	},{
		header:'施修方法', dataIndex:'methodDesc', editor:{  maxLength:200 }, hidden:true
	},{
		header:'处理结果描述', dataIndex:'repairDesc',width : 250, editor:{  maxLength:25 }, hidden:true
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }, hidden:true
	},{
		header:'销票人', dataIndex:'handlePersonName', editor:{  maxLength:25 }, hidden:true
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150, hidden:true
	},{
		header:'处理人', dataIndex:'repairEmp', editor:{  maxLength:25 }, hidden:true
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true, editor:{  maxLength:50 }
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'检修类型', dataIndex:'repairClass', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_lX:
                    return "临修";
                default:
                    return v;
            }
        }
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglTpCheck.searchParam = searchParam;
    	this.store.load();
    }
});

ZbglTpCheck.checkQuery = function(){
	ZbglTpCheck.Status = "-1";
	if(Ext.getCmp("todo").checked){
		ZbglTpCheck.Status = ZbglTpCheck.Status + "," + "TODO";
	} 
	if(Ext.getCmp("ongoing").checked){
		ZbglTpCheck.Status = ZbglTpCheck.Status + "," + "ONGOING";
	} 
	ZbglTpCheck.grid.store.load();
}

ZbglTpCheck.grid.store.on("beforeload", function(){	
	ZbglTpCheck.searchParam = ZbglTpCheck.searchForm.getForm().getValues();
	var searchParam = ZbglTpCheck.searchParam;
	searchParam.faultNoticeStatus = ZbglTpCheck.Status;
    delete searchParam.noticeTime;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);	
	var startDate = "";
    var overDate = "";
    if(Ext.getCmp("inTime_Id").getValue() != ""){
        startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d H:i');
    }
    if(Ext.getCmp("toWarehousingTime").getValue() != ""){
        overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
    }
	this.baseParams.startDate = startDate;
	this.baseParams.overDate = overDate;
});

/*** 提票列表 end ***/

/*** 界面布局 start ***/
ZbglTpCheck.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, height: 200, bodyBorder: false,
        items:[ZbglTpCheck.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglTpCheck.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglTpCheck.panel });
});