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
		loadURL: ctx + '/nodeCaseDelay!findWorkSeqPutOff.action?orgid=' + teamOrgId,                 //装载列表数据的请求URL
	    saveFormColNum:3, searchFormColNum:2,
	    tbar:['search',{
	    	text:"填写延误原因",
	    	iconCls:"cmpIcon",
	    	handler:function(){
	    		if(!$yd.isSelectedRecord(PutOff.grid,true))
	    			return;
	    		var ids = $yd.getSelectedIdx(PutOff.grid);
	    		if(ids.length > 1){
	    			MyExt.Msg.alert("只能选择一条数据");
	    			return;
	    		}
	    		var r = PutOff.grid.selModel.getSelections();
	    		PutOff.show(r[0]);
	    	}
	    },{
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
			header:'车型', dataIndex:'trainTypeShortName', editor: { }, width:70, searcher:{anchor:'98%'}
		},{
			header:'车号', dataIndex:'trainNo', editor: { }, width:70, searcher:{anchor:'98%'}
		},{
			header:'修程', dataIndex:'repairClassName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'修次', dataIndex:'repairTimeName', editor: { }, width:70,
			searcher:{disabled: true}
		},{
			header:'作业流程', dataIndex:'processCaseName', editor: { },
			searcher:{disabled: true}
		}/*,{
			header:'节点编码', dataIndex:'nodeCaseCode', editor: { },
			searcher:{disabled: true}
		}*/,{
			header:'延误工序', dataIndex:'nodeName', editor: { }, searcher:{anchor:'98%'}
		},{
			header:'计划开始时间', dataIndex:'planBeginTimeStr', editor: { },
			searcher:{disabled: true}
		},{
			header:'计划结束时间', dataIndex:'planEndTimeStr', editor: { },
			searcher:{disabled: true}
		},{
			header:'作业班组', dataIndex:'teamOrgName', editor: { }, width:160,
			searcher:{disabled: true}
		},{
			header:'作业人员', dataIndex:'workerNames', editor: { }, sortable: false,
			searcher:{disabled: true}
		},{
			header:'机车承修状态', dataIndex:'workPlanStatus',
        	renderer : function(v){
                if(v == rdp_status_new)return "新兑现";
                else if(v == rdp_status_handling)return "处理中";
                else if(v == rdp_status_handled) return "已处理";
                else if(v == rdp_status_nullify)return "终止";
                else return "";
            },
			searcher:{
		        	xtype: 'combo',
		            fieldLabel: '机车承修状态',
		            hiddenName:'workPlanStatus',
		            store:new Ext.data.SimpleStore({
					    fields: ['v', 't'],
    					data : [[rdp_status_handling,'处理中'],[rdp_status_handled,'已处理'],[rdp_status_nullify,'终止']]
					}),
					valueField:'v',
					displayField:'t',
					triggerAction:'all',
					mode:'local',
					editable: false,
					anchor:'98%'
		        }
		},{
			header:'实际开始时间', dataIndex:'realBeginTimeStr', editor: { }, hidden:true
		},{
			header:'延误时间', dataIndex:'delayTimeStr', editor: { }, renderer:function(v){
				if(v){
					return PutOff.calTime(v);
				}
			},
			searcher:{disabled: true}
		},{
			header:'延误原因', dataIndex:'delayReason', editor: { }, hidden:true
		},{
			header:'RdpIDX', dataIndex:'rdpIDX', hidden:true, editor: { }
		},{
			header:'tecProcessCaseIdx', dataIndex:'tecProcessCaseIDX', hidden:true, editor: { }
		},{
			header:'nodeIdx', dataIndex:'nodeIDX', hidden:true, editor: { }
		}/*,{
			header:'processInstID', dataIndex:'processInstID', hidden:true, editor: { }
		}*/,{
			header:'tempDelayTime', dataIndex:'tempDelayTime', hidden:true, editor: { }
		}],
		toEditFn:function(grid, ri, e){
			var r =  grid.store.getAt(ri);
			PutOff.show(r);
		},
		searchFn:function(searchParam){
			PutOff.searchParam = searchParam ;
	        this.store.load();
		}
	});
	PutOff.grid.store.on("beforeload",function(){
		var searchParam = MyJson.clone(PutOff.searchParam) || {};
		searchParam = MyJson.deleteBlankProp(searchParam);	
		if(Ext.isEmpty(searchParam.workPlanStatus)) {
			searchParam.workPlanStatus = rdp_status_handling;
		}
		var json = Ext.util.JSON.encode(searchParam);		
		this.baseParams.entityJson = json;
	});
	PutOff.show = function(r){
		Ext.getCmp("delay_Type").setValue("");
		jQuery.ajax({
			url: ctx + "/nodeCaseDelay!getDelayInfo.action",
			data:{noceCaseIdx: r.get("idx")},
			dataType:"json",
			type:"post",
			success:function(data){
				var e = data.entity;
				if(e){
					Ext.getCmp("delay_Reason").setValue(e.delayReason);
					Ext.getCmp("delay_Time").setValue(e.delayTime);
					Ext.getCmp("delay_Type").setValue(e.delayType);
					Ext.getCmp("delayIdx").setValue(e.idx);
				}else{
					Ext.getCmp("delayIdx").setValue("");
					Ext.getCmp("delay_Time").setValue(r.get("tempDelayTime"));
				}
			}
		});
		UI.baseForm.getForm().loadRecord(r);
		UI.handlerWin.show();
	}
	
	PutOff.saveFn = function(){
		var form  = UI.baseForm.getForm();
		if(!form.isValid()){
			return false;
		}
		var data = form.getValues();
		data.nodeCaseIdx = data.idx;
		delete data.idx;
		if(data.delayIdx)
			data.idx = data.delayIdx;
		delete data.delayIdx;		
		delete data.trainTypeShortName;
		delete data.trainNo;		
		delete data.repairClassName;
		delete data.repairTimeName;
		data.planBeginTime = new Date(Date.parse(data.planBeginTimeStr.replace(/-/g,   "/")+":00"));
		data.planEndTime = new Date(Date.parse(data.planEndTimeStr.replace(/-/g,   "/")+":00"));
		data.realBeginTime = new Date();//new Date(Date.parse(data.realBeginTimeStr.replace(/-/g,   "/")+":00"));
		delete data.planBeginTimeStr;
		delete data.planEndTimeStr;
		delete data.realBeginTimeStr;
		
		
		jQuery.ajax({
			url: ctx + "/nodeCaseDelay!saveDelay.action",
			data:{data:Ext.util.JSON.encode(data)},
			dataType:"json",
			type:"post",
			success:function(data){
				if(data.entity){
					Ext.getCmp("delayIdx").setValue(data.entity.idx);
					PutOff.grid.store.load();
					alertSuccess();
				}else{
					alertFail(data.errMsg);
				}
			}
		});
	}
	//页面自适应布局
	var viewport = new Ext.Viewport({layout:"fit", items:[ PutOff.grid ]});
});