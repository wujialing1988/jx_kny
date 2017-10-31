Ext.onReady(function(){
Ext.namespace('NodeAndWorkCardEditWin'); 
NodeAndWorkCardEditWin.processTips = null;
/*
 * 显示处理遮罩
 */
NodeAndWorkCardEditWin.showtip = function(win,msg){
	NodeAndWorkCardEditWin.processTips = new Ext.LoadMask(win.getEl(),{
		msg: msg || "正在处理你的操作，请稍后...",
		removeMask:true
	});
	NodeAndWorkCardEditWin.processTips.show();
}
/*
 * 隐藏处理遮罩
 */
NodeAndWorkCardEditWin.hidetip = function(){
	NodeAndWorkCardEditWin.processTips.hide();
}
NodeAndWorkCardEditWin.reloadParent = function(nodeIdx) {
	WorkPlanGantt.loadFn("expanded", nodeIdx);
}
NodeAndWorkCardEditWin.saveFn = function() {
	var nodeFormEditWin = JobProcessNodeEditWin.form.getForm();
	if (!nodeFormEditWin.isValid()) return;       
    var data = nodeFormEditWin.getValues();
    data.ratedWorkMinutes = nodeFormEditWin.findField("workHours").getValue()*60 + nodeFormEditWin.findField("workMinutes").getValue();
    delete data.workHours;
    delete data.workMinutes;
    var datas = new Array();
	for (var i = 0; i < JobProcessNodeRelEditWin.grid.store.getCount(); i++) {
		var relData = {} ;
		relData = JobProcessNodeRelEditWin.grid.store.getAt(i).data;
		if(Ext.isEmpty(relData.preNodeIDX)){
			continue;
		}
		relData.nodeIDX = JobProcessNodeEditWin.idx;
		datas.push(relData);
	}
	for (var i = 0; i < datas.length; i++) {
		var preNodeIDX = datas[i].preNodeIDX;
		for (var j = (i + 1); j < datas.length; j++) {
			var preNodeIDX1 = datas[j].preNodeIDX;
			if (preNodeIDX == preNodeIDX1) {
				MyExt.Msg.alert("不能重复选择同一节点为前置节点");
				return;
			}
		}
	}
    var cfg = {
        url: ctx + "/jobProcessNode!saveOrUpdateNodeAndRel.action", jsonData: data,
        timeout: 600000,
        params: {rels: Ext.util.JSON.encode(datas)},
        success: function(response, options){
			if(NodeAndWorkCardEditWin.processTips) NodeAndWorkCardEditWin.hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                if (data.idx)
                	NodeAndWorkCardEditWin.reloadParent(data.idx);
                else if (data.parentIDX)
                	NodeAndWorkCardEditWin.reloadParent(data.parentIDX);
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){	
        	if(NodeAndWorkCardEditWin.processTips) NodeAndWorkCardEditWin.hidetip();
	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	    }
    };
    var msg = "前置节点均不影响此节点的计划开始时间,计划开始时间以界面设置为准,";
//    if (data.planMode == PLANMODE_MUNAUL) {
//    	msg = "手动计划模式下前置节点均不影响此节点的计划开始时间,计划开始时间以界面设置为准,";		
//	} else if (data.planMode == PLANMODE_AUTO) {
//    	msg = "自动计划模式下此节点的计划开始时间会根据前置节点及延搁时间排程,";	
//    	if (JobProcessNodeEditWin.planBeginTime != null && JobProcessNodeEditWin.planBeginTime != data.planBeginTime) {
//    		msg = "您修改了计划开始时间，计划模式自动调整为手动计划模式,前置节点均不影响此节点的计划开始时间,";
//    		data.planMode = PLANMODE_MUNAUL;
//    	}
//	}
    Ext.Msg.confirm("提示  ", msg + "确定操作？  ", function(btn){
        if(btn != 'yes')    return;
        NodeAndWorkCardEditWin.showtip(TrainWorkPlanEditWin.tabPanel);
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });
}

NodeAndWorkCardEditWin.showEditWin = function() {
	var nodeFormEditWin = JobProcessNodeEditWin.form.getForm();
	nodeFormEditWin.reset();
	nodeFormEditWin.findField("workStationBelongTeam").clearValue();
	nodeFormEditWin.findField("workStationBelongTeamName").setValue("");
	var cfg = {
        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
        params: {nodeIDX: JobProcessNodeEditWin.idx},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {
            	var record = new Ext.data.Record();
                var entity = result.entity;
                for(var i in entity){
                	if(i == 'planBeginTime'){
                		record.set(i, new Date(entity[i]));
                		JobProcessNodeEditWin.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
                		continue;	                    		
                	}
                	if(i == 'planEndTime'){
                		record.set(i, new Date(entity[i]));
                		continue;	                    		
                	}
                	record.set(i, entity[i]);
                }           	
            	nodeFormEditWin.loadRecord(record);
                var hours = (entity.ratedWorkMinutes / 60).toString().split(".")[0];
				var minutes = entity.ratedWorkMinutes - hours * 60;
				nodeFormEditWin.findField("workHours").setValue(hours);
				nodeFormEditWin.findField("workMinutes").setValue(minutes);
				nodeFormEditWin.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
				nodeFormEditWin.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
				nodeFormEditWin.findField("workStationName").setValue(entity.workStationName);
				JobProcessNodeEditWin.nodeIDX = entity.nodeIDX;
				nodeFormEditWin.findField("workStationName").nodeIDX = JobProcessNodeEditWin.nodeIDX;
				nodeFormEditWin.findField("workCalendarIDX").clearValue();
				if (!Ext.isEmpty(entity.workCalendarIDX)) {			    
	                var cfg = {
				        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
	        			params: {infoIdx: entity.workCalendarIDX},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.defInfo != null) {
				                nodeFormEditWin.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
				            }
				        }
				    };
	    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				}
				if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
					nodeFormEditWin.findField("workStationBelongTeam").disable();
					nodeFormEditWin.findField("workStationName").disable();
				} else if (entity.status == NODE_STATUS_UNSTART) {
					nodeFormEditWin.findField("workStationBelongTeam").enable();
					nodeFormEditWin.findField("workStationName").enable();
				}
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	JobProcessNodeRelEditWin.grid.store.load();
}
NodeAndWorkCardEditWin.disableNodeForm = function() {
	var nodeFormEditWin = JobProcessNodeEditWin.form.getForm();
	nodeFormEditWin.findField("nodeName").disable();
	nodeFormEditWin.findField("ratedWorkMinutes").disable();
	nodeFormEditWin.findField("workHours").disable();
	nodeFormEditWin.findField("workMinutes").disable();
	nodeFormEditWin.findField("nodeDesc").disable();
	nodeFormEditWin.findField("planBeginTime").disable();
	nodeFormEditWin.findField("workStationName").disable();
	nodeFormEditWin.findField("workCalendarIDX").disable();
	nodeFormEditWin.findField("workStationBelongTeam").disable();
	nodeFormEditWin.findField("planMode").disable();
	Ext.getCmp("nodeSaveBtnEditWin").setVisible(false);
}
NodeAndWorkCardEditWin.disableNodeRelGrid = function() {
	JobProcessNodeRelEditWin.grid.disable();
}
NodeAndWorkCardEditWin.enableNodeForm = function() {
	var nodeFormEditWin = JobProcessNodeEditWin.form.getForm();
	nodeFormEditWin.findField("nodeName").enable();
	nodeFormEditWin.findField("ratedWorkMinutes").enable();
	nodeFormEditWin.findField("workHours").enable();
	nodeFormEditWin.findField("workMinutes").enable();
	nodeFormEditWin.findField("nodeDesc").enable();
	nodeFormEditWin.findField("planBeginTime").enable();
	nodeFormEditWin.findField("workStationName").enable();
	nodeFormEditWin.findField("workCalendarIDX").enable();
	nodeFormEditWin.findField("workStationBelongTeam").enable();
	nodeFormEditWin.findField("planMode").enable();
	Ext.getCmp("nodeSaveBtnEditWin").setVisible(true);
}
NodeAndWorkCardEditWin.enableNodeRelGrid = function() {
	JobProcessNodeRelEditWin.grid.enable();
}
});