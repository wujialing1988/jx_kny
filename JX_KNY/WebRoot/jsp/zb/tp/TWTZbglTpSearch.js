/**
 * 临碎修提票查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglTpSearch');                       //定义命名空间
ZbglTpSearch.searchParam = {};
/*** 查询表单 start ***/
ZbglTpSearch.searchLabelWidth = 90;
ZbglTpSearch.searchAnchor = '95%';
ZbglTpSearch.searchFieldWidth = 270;
//最近一个月
ZbglTpSearch.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	return MonthFirstDay.format('Y-m-d');
}
ZbglTpSearch.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglTpSearch.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: ZbglTpSearch.searchFieldWidth, labelWidth: ZbglTpSearch.searchLabelWidth, defaults:{anchor:ZbglTpSearch.searchAnchor},
			items:[{ 
				fieldLabel: "提票单号", name: 'faultNoticeCode', xtype: 'textfield'
			},{			
				xtype: 'EosDictEntry_combo', id:'result_lx',hiddenName: 'repairResult',
				displayField:'dictname',valueField:'dictid',
				fieldLabel: '临修处理结果', dicttypeid: 'JCZB_LXTP_REPAIRRESULT'
			},{
				xtype: 'compositefield', fieldLabel : '提票日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "noticeTime",  xtype: "my97date",format: "Y-m-d",  
		             value : ZbglTpSearch.getCurrentMonth(),
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
			fieldWidth: ZbglTpSearch.searchFieldWidth, labelWidth: ZbglTpSearch.searchLabelWidth, defaults:{anchor:ZbglTpSearch.searchAnchor},
			items:[{				
	        	xtype: 'combo',
	            fieldLabel: '提票状态',
	            hiddenName:'faultNoticeStatus',
	            store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : tpStatus
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				editable: false			
			},{			
				xtype: 'EosDictEntry_combo',hiddenName: 'repairResult', id: 'result_cx',
				displayField:'dictname',valueField:'dictid',
				fieldLabel: '碎修处理结果', dicttypeid: 'JCZB_TP_REPAIRRESULT'
			},{ fieldLabel: "发现人", name: 'discover', xtype: 'textfield'}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglTpSearch.searchForm.getForm();	
		        var searchParam = form.getValues();
				if(!Ext.isEmpty(Ext.get("result_lx").dom.value)){
					searchParam.repairResult = searchParam.repairResult[0];
				}
                	searchParam = MyJson.deleteBlankProp(searchParam);
				searchParam.trainNo = trainNo;
				searchParam.trainTypeIDX = trainTypeIDX;
				ZbglTpSearch.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglTpSearch.searchForm;
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
            	searchParam = {};
            	ZbglTpSearch.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/

/*** 提票列表 start ***/
ZbglTpSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    tbar:['refresh',
            {text:"打印", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglTpSearch.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("inTime_Id").getValue() != ""){
		        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d') + " 00:00:00";
		        }
		        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
		        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/tp/ZbglTpSearch.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeIDX=" + data.trainTypeIDX +
				"&trainNo=" + data.trainNo + "&faultNoticeCode=" + data.faultNoticeCode + "&faultNoticeStatus=" + data.faultNoticeStatus + "&repairResult=" + data.repairResult;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("临碎修提票"));
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
    },
	{
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
		header:'故障原因', dataIndex:'faultReason', editor:{  maxLength:500 }
	},{
		header:'处理描述', dataIndex:'repairDesc', editor:{  maxLength:500 }
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
		header:'接票人', dataIndex:'revPersonName', editor:{  maxLength:25 }
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'施修方法', dataIndex:'methodDesc', editor:{  maxLength:200 }
	},{
		header:'处理结果', dataIndex:'repairResult', editor:{ xtype:'numberfield', maxLength:2 },
		renderer: function(value, metaData, record, rowIndex, colIndex, store){
			if (Ext.isEmpty(value))
				return "";
            if (record.get("repairClass") == REPAIRCLASS_SX)
            	return EosDictEntry.getDictname("JCZB_TP_REPAIRRESULT",value);
            else if (record.get("repairClass") == REPAIRCLASS_lX)
            	return EosDictEntry.getDictname("JCZB_LXTP_REPAIRRESULT",value);
        }
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'销票人', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'处理人', dataIndex:'repairEmp', editor:{  maxLength:25 }
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
    	ZbglTpSearch.searchParam = searchParam;
    	this.store.load();
    }
});
ZbglTpSearch.grid.store.on("beforeload", function(){	
	ZbglTpSearch.searchParam = ZbglTpSearch.searchForm.getForm().getValues();
	var searchParam = ZbglTpSearch.searchParam;
	searchParam = MyJson.deleteBlankProp(searchParam);
	var whereList = [] ;
	for(prop in searchParam){
			if('noticeTime' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'noticeTime',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'noticeTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'noticeTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'noticeTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
		 	if('repairResult' == prop){
		 		if(!Ext.isEmpty(Ext.get("result_lx").dom.value) && Ext.isEmpty(Ext.get("result_cx").dom.value)){
					searchParam.repairResult = searchParam.repairResult[0];
				}
				if(!Ext.isEmpty(Ext.get("result_lx").dom.value)&& !Ext.isEmpty(Ext.get("result_cx").dom.value)){
					whereList.push({propName:'repairResult',propValue:searchParam[prop],compare:Condition.IN});
					continue;
				}
		 		whereList.push({propName:'repairResult',propValue:searchParam[prop],compare:Condition.EQ});
		 		continue;
		 	}
			whereList.push({propName:prop,propValue:searchParam[prop]});
		}
	whereList.push({propName:'trainTypeIDX',propValue:trainTypeIDX,compare:Condition.EQ,stringLike: false});
	whereList.push({propName:'trainNo',propValue:trainNo,compare:Condition.EQ,stringLike: false});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 界面布局 start ***/
ZbglTpSearch.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, height: 200, bodyBorder: false,
        items:[ZbglTpSearch.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglTpSearch.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglTpSearch.panel });
});