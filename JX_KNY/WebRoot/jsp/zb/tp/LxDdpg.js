/**
 * 临修调度派工 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('LxDdpg');                       //定义命名空间
LxDdpg.searchParam = {};
/*** 查询表单 start ***/
LxDdpg.searchLabelWidth = 90;
LxDdpg.searchAnchor = '95%';
LxDdpg.searchFieldWidth = 270;
LxDdpg.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: LxDdpg.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: LxDdpg.searchFieldWidth, labelWidth: LxDdpg.searchLabelWidth, defaults:{anchor:LxDdpg.searchAnchor},
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
		                var trainNo_comb = LxDdpg.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			},{ fieldLabel: "提票单号", name: 'faultNoticeCode', xtype: 'textfield'}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: LxDdpg.searchFieldWidth, labelWidth: LxDdpg.searchLabelWidth, defaults:{anchor:LxDdpg.searchAnchor},
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
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = LxDdpg.searchForm.getForm();	
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				LxDdpg.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = LxDdpg.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = form.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeIDX;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	LxDdpg.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/

/*** 提票列表 start ***/
LxDdpg.isDispatch = false; 
LxDdpg.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    tbar:[{
    	text:'派工', iconCls:'addIcon', handler:function(){
    		var grid = LxDdpg.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		LxDdpg.showSaveWin();
    	}
    },'refresh','-',
    {   
    	xtype:"radio",name:"isDispatch", 
    	boxLabel:'未派工'+"&nbsp;&nbsp;&nbsp;&nbsp;", 
    	checked:true, 
    	handler: function(radio, checked){
    		if(checked){
    			LxDdpg.isDispatch = false; 
    			LxDdpg.grid.store.load();
    		}
    	}
    },
    {   
    	xtype:"radio",name:"isDispatch", boxLabel:'已派工', 
    	handler: function(radio, checked){
    		if(checked){
    			LxDdpg.isDispatch = true; 
    			LxDdpg.grid.store.load();
    		}
    	}
	}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'派工', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
		renderer:function(v,x,r){			
			return "<img src='" + img + "' alt='派工' style='cursor:pointer' onclick='LxDdpg.dispatcher(\"" + v + "\")'/>";
		}
	},
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
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
		header:'处理班组名称', dataIndex:'revOrgName', editor:{  maxLength:50 }
	},{
		header:'故障部件', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
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
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true, editor:{  maxLength:300 }
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接票人名称', dataIndex:'revPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'施修方法', dataIndex:'methodDesc', hidden:true, editor:{  maxLength:200 }
	},{
		header:'处理结果', dataIndex:'repairResult', hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'销票人名称', dataIndex:'handlePersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
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
		header:'检修类型', dataIndex:'repairClass', hidden:true, editor:{  maxLength:20 }
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	LxDdpg.searchParam = searchParam;
    	this.store.load();
    }
});
LxDdpg.grid.store.on("beforeload", function(){	
	var searchParam = LxDdpg.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	}
	whereList.push({propName:'faultNoticeStatus', propValue: STATUS_DRAFT ,compare : Condition.EQ}) ;	//只查询待领活的票，未考虑已领活再重新调度派工的情况
	whereList.push({propName:'siteID', propValue: siteID ,compare : Condition.EQ}) ;
	whereList.push({propName:'repairClass', propValue: REPAIRCLASS_lX ,compare : Condition.EQ}) ;
	var sqlStr = " REV_ORGID IS NULL" ;
	if (LxDdpg.isDispatch) {
		var sqlStr = " REV_ORGID IS NOT NULL";	
	}
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 调度派工保存方法 start ***/
LxDdpg.idx = '';
LxDdpg.dispatcher = function(idx) {
	LxDdpg.showSaveWin();
	LxDdpg.idx = idx;
}
LxDdpg.saveFn = function() {	
	var ids = $yd.getSelectedIdx(LxDdpg.grid);	
	var form = LxDdpg.saveForm.getForm();
	if (ids.length < 1 && !Ext.isEmpty(LxDdpg.idx)) {
		ids = [];
		ids.push(LxDdpg.idx);
	}
	if (ids.length < 1) {
		MyExt.Msg.alert("尚未选择一条记录！");
		return;
	}
	if (!form.isValid()) return;
	var data = form.getValues();
	var cfg = {
        url: ctx + "/zbglTp!updateForLxDdpg.action", 
        params: {
        	ids: Ext.util.JSON.encode(ids)
        },
        jsonData: data,
        success: function(response, options){
            if(LxDdpg.loadMask)   LxDdpg.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true ) {
                LxDdpg.grid.store.load();
                alertSuccess();
                LxDdpg.saveWin.hide();				
            } else {
                alertFail(result.errMsg);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}
/*** 调度派工保存方法 end ***/
/*** 调度派工窗口 start ***/
LxDdpg.labelWidth = 90;
LxDdpg.fieldWidth = 170;
LxDdpg.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
LxDdpg.saveForm = new Ext.form.FormPanel({
    style: "padding:10px",     frame: true, 	 baseCls: "x-plain", labelAlign:"left", 
    align: "center",  		   layout:"column",  border:false,
    defaults: { xtype:"container", autoEl:"div", layout:"form", columnWidth:1,
    	defaults: {
    		xtype:"textfield", 
		    labelWidth:LxDdpg.labelWidth, anchor:"98%",
		    defaults: {
		    	width: LxDdpg.fieldWidth
		    }
    	}
    },    
	items:[{ 
    	items:[{
          fieldLabel:"临修处理班组", xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'revOrgID', allowBlank: false,
          returnField:[{widgetId:"handleOrgName_ID",propertyName:"text"},{widgetId:"handleOrgSeq_ID",propertyName:"orgseq"}],
          selectNodeModel: 'leaf',
          forDictHql:"[onlyFirstLevel]",
		  queryHql: " and orgid in (select orgid from WorkPlaceToOrg where workPlaceCode = '" + siteID + "') and orgdegree = 'tream' "
        },
        { id: "handleOrgName_ID", xtype: "hidden", name: "revOrgName"},
        { id: "handleOrgSeq_ID", xtype: "hidden", name: "revOrgSeq"}]
	}]
});
LxDdpg.saveWin = new Ext.Window({
    title:"临修调度派工", width: (LxDdpg.labelWidth + LxDdpg.fieldWidth + 8) + 60, 
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:LxDdpg.saveForm, 
    buttons: [{
        text: "调度派工", iconCls: "saveIcon", handler: LxDdpg.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: function(){ LxDdpg.saveWin.hide(); }
    }]
});
LxDdpg.showSaveWin = function() {
	LxDdpg.saveWin.show();
	var form = LxDdpg.saveForm.getForm();
	form.reset();
	form.findField("revOrgID").clearValue();
	var cfg = {
        url: ctx + "/workPlaceToOrg!hasWorkplaceBySiteID.action", 
        params: {
        	siteID: siteID
        },
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true ) {
                if (result.hasWorkplaceBySiteID == false) {
                	form.findField("revOrgID").queryHql = "[degree]tream";
                	form.findField("revOrgID").forDictHql = "";
                }
                				
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}
/*** 调度派工窗口 end ***/

/*** 界面布局 start ***/
LxDdpg.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, collapsed: true,  height: 150, bodyBorder: false,
        items:[LxDdpg.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ LxDdpg.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:LxDdpg.panel });
});