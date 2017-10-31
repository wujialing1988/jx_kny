/**
 * 工序延迟
 */
Ext.onReady(function(){
	Ext.namespace('WorkSeqPutOffHis');                       //定义命名空间
	WorkSeqPutOffHis.searchParam = {};
	WorkSeqPutOffHis.calTime = function(v) {
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
	WorkSeqPutOffHis.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/nodeCaseDelay!findWorkSeqPutOff.action?isHis=true',                 
	    saveFormColNum:3, searchFormColNum:2,
	    viewConfig: null,
	    storeAutoLoad: false,
	    tbar:['search',/*{
	    	text:"查看流程图",
	    	iconCls:"chart_organisationIcon",
	    	handler:function(){
		    	if(!$yd.isSelectedRecord(WorkSeqPutOffHis.grid,true))
	    			return;
		    	var record = WorkSeqPutOffHis.grid.selModel.getSelections();
	    		if(record.length != 1){
	    			MyExt.Msg.alert("只能选择一项记录");
	    			return;
	    		}
	    		window.open(ctx+"/workSeq!seeWorkFlow.action?processInstID=" + record[0].get("processInstID")+"&rdpIdx=" + record[0].get("rdpIDX"));
	    	}
	    },*/'refresh'],
		fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor: { }, width:70
		},{
			header:'车号', dataIndex:'trainNo', editor: { }, width:70
		},{
			header:'修程', dataIndex:'repairClassName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'修次', dataIndex:'repairTimeName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'作业流程', dataIndex:'processCaseName', editor: { }, width:110,
			searcher:{disabled: true}
		},{
			header:'延误工序', dataIndex:'nodeName', editor: { }, width:110,
			searcher:{xtype: 'textfield', fieldLabel: '延误工序'}
		},{
			header:'计划开始时间', dataIndex:'planBeginTimeStr', editor: { }, width:110,
			searcher:{disabled: true}
		},{
			header:'计划结束时间', dataIndex:'planEndTimeStr', editor: { }, width:110,
			searcher:{disabled: true}
		},{
			header:'实际开始时间', dataIndex:'realBeginTimeStr', editor: { }, width:110, 
			searcher:{disabled: true}
		},{
			header:'实际结束时间', dataIndex:'realEndTimeStr', editor: { }, width:110, 
			searcher:{disabled: true}
		},{
			header:'额定工期', dataIndex:'ratedWorkMinutes', editor: { }, 
			searcher:{disabled: true}, 
			renderer:function(v){
				if(v){
					return WorkSeqPutOffHis.calTime(v);
				}
			}
		},{
			header:'实际工期', dataIndex:'realWorkMinutes', editor: { }, 
			searcher:{disabled: true}, 
			renderer:function(v){
				if(v){
					return WorkSeqPutOffHis.calTime(v);
				}
			}
		},{
			header:'延误时间', dataIndex:'delayTimeStr', editor: { }, renderer:function(v){
				if(v){
					return WorkSeqPutOffHis.calTime(v);
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
			  dicttypeid:'JXGC_WORK_SEQ_DELAY'
			}
		},{
			header:'延误原因', dataIndex:'delayReason', editor: { },
			searcher:{disabled: true}
		},{
			header:'作业班组', dataIndex:'teamOrgName', editor: { }, width:160
		},{
			header:'作业人员', dataIndex:'workerNames', editor: { }, sortable: false,
			searcher:{disabled: true}
		},{
			header:'作业状态', dataIndex:'workPlanStatus',
        	renderer : function(v){
                if(v == rdp_status_new)return "新兑现";
                else if(v == rdp_status_handling)return "处理中";
                else if(v == rdp_status_handled) return "已处理";
                else if(v == rdp_status_nullify)return "终止";
                else return "";
            },
			searcher:{
		        	xtype: 'combo',
		            fieldLabel: '作业状态',
		            hiddenName:'billStatus',
		            store:new Ext.data.SimpleStore({
					    fields: ['v', 't'],
    					data : [[rdp_status_handling,'处理中'],[rdp_status_handled,'已处理'],[rdp_status_nullify,'终止']]
					}),
					valueField:'v',
					displayField:'t',
					triggerAction:'all',
					mode:'local',
					editable: false
		        }

		},{
			header:'RdpIDX', dataIndex:'rdpIDX', hidden:true, editor: { }
		}],
		toEditFn:function(grid, ri, e){
		},
		searchFn:function(searchParam){
			WorkSeqPutOffHis.searchParam = searchParam ;
	        this.store.load();
		},
		refreshButtonFn: function(){                        //点击刷新按钮触发的函数
	        WorkSeqPutOffHis.grid.store.reload();
	    }
	});
	WorkSeqPutOffHis.grid.store.on("beforeload",function(){
		var searchParam = MyJson.clone(WorkSeqPutOffHis.searchParam) || {};
		searchParam = MyJson.deleteBlankProp(searchParam);
		var json = Ext.util.JSON.encode(searchParam);		
		this.baseParams.entityJson = json;
	});
	WorkSeqPutOffHis.win =  new Ext.Window({
	    title: "工序延误历史记录", maximizable: true, maximized: true, width: 705, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
	    items: [WorkSeqPutOffHis.grid],
	    buttons: [{
	                text: "关闭", iconCls: "closeIcon", handler: function(){ WorkSeqPutOffHis.win.hide(); }
	            }]
	});
});