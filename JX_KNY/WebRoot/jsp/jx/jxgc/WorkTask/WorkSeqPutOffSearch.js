/**
 * 工序延迟
 */
Ext.onReady(function(){
	Ext.namespace('PutOff');                       //定义命名空间
	PutOff.searchParam = {};
	PutOff.calTime = function(v) {
		var x = parseInt(v);
		var h = parseInt(x / 60);
		var m = (v - h * 60).toFixed(0);
		if(h > 0) {
			if(m > 0) {
    			return h + "小时" + m + "分钟";
			} else {
				return h + "小时";
			}
		} else {
			return m + "分钟"	;	
		}
	}
	PutOff.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/nodeCaseDelay!findWorkSeqPutOff.action?isSearch=true',                 //装载列表数据的请求URL
	    saveFormColNum:3, searchFormColNum:2,
	    viewConfig: null,
	    tbar:['search',/*{
	    	text:"查看流程图",
	    	iconCls:"chart_organisationIcon",
	    	handler:function(){
		    	if(!$yd.isSelectedRecord(PutOff.grid,true))
	    			return;
		    	var record = PutOff.grid.selModel.getSelections();
	    		if(record.length != 1){
	    			MyExt.Msg.alert("只能选择一项记录");
	    			return;
	    		}
	    		window.open(ctx+"/workSeq!seeWorkFlow.action?processInstID=" + record[0].get("processInstID")+"&rdpIdx=" + record[0].get("rdpIDX"));
	    	}
	    },*/{
	    	text:"查看历史记录",
	    	iconCls:"application_view_listIcon",
	    	handler:function(){
	    		WorkSeqPutOffHis.win.show();
	    		WorkSeqPutOffHis.grid.store.load();
	    	}
	    },'refresh'],
		fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor: { }, width:70, searcher: {anchor:'98%'}
		},{
			header:'车号', dataIndex:'trainNo', editor: { }, width:70, searcher: {anchor:'98%'}
		},{
			header:'修程', dataIndex:'repairClassName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'修次', dataIndex:'repairTimeName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'作业流程', dataIndex:'processCaseName', editor: { }, width:100,
			searcher:{disabled: true}
		},{
			header:'延误工序', dataIndex:'nodeName', editor: { }, width:110,
			searcher:{xtype: 'textfield', fieldLabel: '延误工序', anchor:'98%'}
		},{
			header:'计划开始时间', dataIndex:'planBeginTimeStr', editor: { }, width:150,
			searcher:{disabled: true}
		},{
			header:'计划结束时间', dataIndex:'planEndTimeStr', editor: { }, width:150,
			searcher:{disabled: true}
		},{
			header:'实际开始时间', dataIndex:'realBeginTimeStr', editor: { }, hidden:true,
			searcher:{disabled: true}
		},{
			header:'延误时间', dataIndex:'delayTimeStr', editor: { }, renderer:function(v){
				if(v){
					return PutOff.calTime(v);
				}
			},
			searcher:{disabled: true}
		},{
			header:'延误类型', dataIndex:'delayType', editor: { },
			renderer:function(v){
				if(v){
					return EosDictEntry.getDictname('JXGC_WORK_SEQ_DELAY', v);
				}
			},
			searcher:{
			  xtype: 'EosDictEntry_combo', fieldLabel: '延误类型',
			  hiddenName: 'delayType', displayField: 'dictname', valueField: 'dictid',
			  dicttypeid:'JXGC_WORK_SEQ_DELAY',
			  anchor:'98%'
			}
		},{
			header:'延误原因', dataIndex:'delayReason', editor: { },
			searcher:{disabled: true}
		},{
			header:'作业班组', dataIndex:'teamOrgName', editor: { }, width:160, searcher:{anchor:'98%'}
		},{
			header:'作业人员', dataIndex:'workerNames', editor: { }, sortable: false,
			searcher:{disabled: true}
		},{
			header:'状态', dataIndex:'workPlanStatus',
        	renderer : function(v){
                if(v == rdp_status_new)return "新兑现";
                else if(v == rdp_status_handling)return "处理中";
                else if(v == rdp_status_handled) return "已处理";
                else if(v == rdp_status_nullify)return "终止";
                else return "";
            },
			searcher:{disabled: true}
		},{
			header:'RdpIDX', dataIndex:'rdpIDX', hidden:true, editor: { }
		}],
		toEditFn:function(grid, ri, e){
		},
		searchFn:function(searchParam){
			PutOff.searchParam = searchParam ;
	        this.store.load();
		}
	});
	PutOff.grid.store.on("beforeload",function(){
		var searchParam = MyJson.clone(PutOff.searchParam) || {};
		searchParam = MyJson.deleteBlankProp(searchParam);
		searchParam.workPlanStatus = rdp_status_handling;
		var json = Ext.util.JSON.encode(searchParam);		
		this.baseParams.entityJson = json;
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({layout:"fit", items:[ PutOff.grid ]});
});