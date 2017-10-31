Ext.onReady(function(){
Ext.namespace('NodeAndWorkCardEditSearch'); 
NodeAndWorkCardEditSearch.processTips = null;
/*
 * 显示处理遮罩
 */
NodeAndWorkCardEditSearch.showtip = function(win,msg){
	NodeAndWorkCardEditSearch.processTips = new Ext.LoadMask(win.getEl(),{
		msg: msg || "正在处理你的操作，请稍后...",
		removeMask:true
	});
	NodeAndWorkCardEditSearch.processTips.show();
}
/*
 * 隐藏处理遮罩
 */

NodeAndWorkCardEditSearch.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{
            title: "流程节点", border: false, layout: "border", buttonAlign : "center",
            items: [{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	            height: 200, bodyBorder: false,
	            items:[JobProcessNodeSearch.form], frame: true
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRelSearch.grid ]
	        }],
	        buttons: [{
			        text: "关闭", iconCls: "closeIcon", handler: function(){ NodeAndWorkCardEditSearch.win.hide(); }
			    }]
        },{
            id: "WorkCardEditTabSearch", title: "作业工单", border: false, layout: "fit", 
            items:[WorkCardEditSearch.grid]
        },{
            id: "NodeExtConfigTabSearch", title: "扩展配置", border: false, layout: "fit", 
            items:[JobNodeExtConfigSearch.form]
        }]
});
NodeAndWorkCardEditSearch.win = new Ext.Window({
    title:"节点查看", width: 800, maximized : true, layout: 'fit',
    plain:true, closeAction:"hide", buttonAlign:'center',  
    items:NodeAndWorkCardEditSearch.tabs
});

NodeAndWorkCardEditSearch.showEditWin = function() {
	var nodeForm = JobProcessNodeSearch.form.getForm();
	nodeForm.reset();
	nodeForm.findField("workStationBelongTeam").clearValue();
	nodeForm.findField("workStationBelongTeamName").setValue("");
	var cfg = {
        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
        params: {nodeIDX: JobProcessNodeSearch.idx},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {
            	var record = new Ext.data.Record();
                var entity = result.entity;
                for(var i in entity){
                	if(i == 'planBeginTime'){
                		record.set(i, new Date(entity[i]));
                		JobProcessNodeSearch.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
                		continue;	                    		
                	}
                	if(i == 'planEndTime'){
                		record.set(i, new Date(entity[i]));
                		continue;	                    		
                	}
                	record.set(i, entity[i]);
                }           	
            	nodeForm.loadRecord(record);
                var hours = (entity.ratedWorkMinutes / 60).toString().split(".")[0];
				var minutes = entity.ratedWorkMinutes - hours * 60;
				nodeForm.findField("workHours").setValue(hours);
				nodeForm.findField("workMinutes").setValue(minutes);
				
				nodeForm.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
				nodeForm.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
				nodeForm.findField("workStationName").setValue(entity.workStationName);
				JobProcessNodeSearch.nodeIDX = entity.nodeIDX;
				nodeForm.findField("workStationName").nodeIDX = JobProcessNodeSearch.nodeIDX;
				nodeForm.findField("workCalendarIDX").clearValue();
				if (!Ext.isEmpty(entity.workCalendarIDX)) {			    
	                var cfg = {
				        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
	        			params: {infoIdx: entity.workCalendarIDX},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.defInfo != null) {
				                nodeForm.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
				            }
				        }
				    };
	    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				}
				/*
				if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
					nodeForm.findField("workStationBelongTeam").disable();
					nodeForm.findField("workStationName").disable();
				} else if (entity.status == NODE_STATUS_UNSTART) {
					nodeForm.findField("workStationBelongTeam").enable();
					nodeForm.findField("workStationName").enable();
				}*/
				nodeForm.findField("workStationBelongTeam").disable();
				nodeForm.findField("workStationName").disable();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	JobProcessNodeRelSearch.grid.store.load();
	NodeAndWorkCardEditSearch.win.show();
	
}
});